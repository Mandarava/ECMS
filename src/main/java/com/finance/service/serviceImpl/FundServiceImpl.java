package com.finance.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.finance.dao.FundDao;
import com.finance.dao.FundNetDao;
import com.finance.dao.ProfitDao;
import com.finance.exception.BusinessException;
import com.finance.model.JavaBean.SinaFinanceFund;
import com.finance.model.pojo.Fund;
import com.finance.model.pojo.FundNet;
import com.finance.model.pojo.Profit;
import com.finance.service.FundService;
import com.finance.util.excel.ExportExcelUtil;
import com.finance.util.myutil.HttpConnectionManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Service
public class FundServiceImpl implements FundService {

    private static Logger logger = LoggerFactory.getLogger(FundServiceImpl.class);

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private int recordsPerInsert = 5000;

    private int fundNetPerSelect = 150;

    @Resource
    private ProfitDao ProfitDao;
    @Resource
    private FundDao fundDao;
    @Resource
    private FundNetDao fundNetDao;
    @Resource
    private FundService fundService;

    @PostConstruct
    public void init() {
        // do something init
    }

    @PreDestroy
    public void destroy() {
        // do something destroy
    }

    @Override
    public void insertFundProfit(Profit profit) throws BusinessException {
        int i = ProfitDao.insertDailyProfit(profit);
        if (i == 0) {
            throw new BusinessException("insertFundProfit failed!");
        }
    }

    /**
     * 更新基金净值数据
     */
    @Override
    public void insertOrUpdateFundNetData() throws Exception {
        List<Fund> fundList = this.findFunds();
        while (fundList.size() > 0) {
            List<Fund> funds = fundList.subList(0, fundList.size() > fundNetPerSelect ? fundNetPerSelect : fundList.size());
            ExecutorService pool = Executors.newFixedThreadPool(20);
            List<Future<List<FundNet>>> futures = new ArrayList<>();
            // 获得到的净值数据
            List<FundNet> fetchedNetList = new ArrayList<>();
            for (Fund fund : funds) {
                Callable<List<FundNet>> callable = new GetThread(fund.getCode());
                Future<List<FundNet>> future = pool.submit(callable);
                futures.add(future);
            }
            for (int i = 0; i < futures.size(); i++) {
                Future<List<FundNet>> future = futures.get(i);
                if (future.get() != null) {
                    fetchedNetList.addAll(future.get());
                }
            }
            pool.shutdown();
            if (fetchedNetList == null || fetchedNetList.size() == 0) {
                return;
            }
            // 当前数据库中存在的净值数据
            List<FundNet> result = fundNetDao.findFundNetDateByCodes(funds);
            Set<FundNet> fundNets = new HashSet<>(fetchedNetList);
            for (int i = 0; i < result.size(); i++) {
                FundNet temp = result.get(i);
                if (fundNets.contains(temp)) {
                    fundNets.remove(temp);
                }
            }
            // too slow
//            fetchedNetList.sort(Comparator.comparing(FundNet::getCode).thenComparing(FundNet::getNetDate));
            // 只保留数据库中不存在的数据
//            Iterator<FundNet> it = fetchedNetList.iterator();
//            while (it.hasNext()) {
//                FundNet fundNet = it.next();
//                for (FundNet temp : result) {
//                    if (temp.getCode().equals(fundNet.getCode()) && df.format(temp.getNetDate()).equals(df.format(fundNet.getNetDate()))) {
//                        it.remove();
//                        break;
//                    }
//                }
//            }
            List<FundNet> fundNetList = new ArrayList<>(fundNets);
            if (fundNetList.size() > 0) {
                while (fundNetList.size() > 0) {
                    List<FundNet> subFundNetList = fundNetList.subList(0, fundNetList.size() > recordsPerInsert ? recordsPerInsert : fundNetList.size());
                    /* 自己注给自己，否则嵌套事务无法执行*/
                    fundService.batchInsertFundNetData(subFundNetList);
                    fundNetList.subList(0, fundNetList.size() > recordsPerInsert ? recordsPerInsert : fundNetList.size()).clear();
                }
            }
            fundList.subList(0, fundList.size() > fundNetPerSelect ? fundNetPerSelect : fundList.size()).clear();
        }
    }

    /**
     * 分批插入基金净值数据
     */
    @Override
    public void batchInsertFundNetData(List<FundNet> fundNetList) throws BusinessException {
        int i = fundNetDao.batchInsertFundNetData(fundNetList);
        if (i != fundNetList.size()) {
            throw new BusinessException("更新或插入基金净值数据失败！");
        }
    }

    /**
     * 更新或插入基金数据
     */
    @Override
    public void insertOrUpdateFundData() throws BusinessException {
        List<SinaFinanceFund> sinaFundList = this.fetchFundData();
        if (sinaFundList == null || sinaFundList.size() == 0) {
            return;
        }
        List<Fund> fundList = new ArrayList<>();
        for (SinaFinanceFund sinaFund : sinaFundList) {
            Fund fund = new Fund();
            fund.setCode(sinaFund.getSymbol());
            fund.setName(sinaFund.getName());
            fund.setCompanyName(sinaFund.getCompanyName());
            fund.setSubjectName(sinaFund.getSubjectName());
            fund.setEstablishDate(sinaFund.getClrq());
            fund.setFundScale(sinaFund.getJjgm());
            fund.setCxpj(sinaFund.getCxpj());
            fund.setYhpj(sinaFund.getYhpj3());
            fund.setHtpj(sinaFund.getHtpj());
            fund.setJajxpj(sinaFund.getJajxpj());
            fund.setZspj(sinaFund.getZspj());
            String type2Id = sinaFund.getType2Id();
            String type3Id = sinaFund.getType3Id();
            if (type2Id.equals("x2001")) {
                if ("x3006".equals(type3Id)) {
                    fund.setType1("激进混合型");
                } else if ("x3004".equals(type3Id)) {
                    fund.setType1("稳健混合型");
                } else if ("x3009".equals(type3Id)) {
                    fund.setType1("保本型");
                } else if ("x3008".equals(type3Id)) {
                    fund.setType1("指数型");
                } else if ("x3003".equals(type3Id)) {
                    fund.setType1("稳健债券型");
                } else if ("x3017".equals(type3Id)) {
                    fund.setType1("分级-激进债券型");
                } else if ("x3019".equals(type3Id)) {
                    fund.setType1("货币A");
                }
                fund.setType("混合型");
            } else if (type2Id.equals("x2003")) {
                if ("x3005".equals(type3Id)) {
                    fund.setType1("激进债券型");
                } else if ("x3012".equals(type3Id)) {
                    fund.setType1("纯债债券型");
                } else if ("x3003".equals(type3Id)) {
                    fund.setType1("稳健债券型");
                } else if ("x3021".equals(type3Id)) {
                    fund.setType1("短期理财");
                } else if ("x3006".equals(type3Id)) {
                    fund.setType1("激进混合型");
                }
                fund.setType("债券型");
            } else if (type2Id.equals("x2002")) {
                if ("x3008".equals(type3Id)) {
                    fund.setType1("指数型");
                } else if ("x3007".equals(type3Id)) {
                    fund.setType1("一般股票型");
                } else if ("x3014".equals(type3Id)) {
                    fund.setType1("分级-一般股票型");
                } else if ("x3006".equals(type3Id)) {
                    fund.setType1("激进混合型");
                }
                fund.setType("股票型");
            } else if (type2Id.equals("x2005")) {
                if ("x3019".equals(type3Id)) {
                    fund.setType1("货币A");
                } else if ("x3020".equals(type3Id)) {
                    fund.setType1("货币B");
                }
                fund.setType("货币型");
            } else if (type2Id.equals("x2006")) {
                if ("x3002".equals(type3Id)) {
                    fund.setType1("权益类");
                } else if ("x3018".equals(type3Id)) {
                    fund.setType1("固定收益类");
                } else if ("x3001".equals(type3Id)) {
                    fund.setType1("其他");
                } else if ("x3008".equals(type3Id)) {
                    fund.setType1("指数型");
                }
                fund.setType("QDII");
            } else if (type2Id.equals("x2010")) {
                if ("x3022".equals(type3Id)) {
                    fund.setType1("商品类");
                } else if ("x3023".equals(type3Id)) {
                    fund.setType1("其它资产");
                }
                fund.setType("另类资产");
            }
            fundList.add(fund);
        }

        List<Fund> existsFund = this.findFunds();
        List<Fund> fundToUpdate = new ArrayList<>();
        Iterator<Fund> fundIterator = fundList.iterator();
        while (fundIterator.hasNext()) {
            Fund newFund = fundIterator.next();
            for (Fund oldFund : existsFund) {
                if (newFund.getCode().equals(oldFund.getCode())) {
                    fundToUpdate.add(newFund);
                    fundIterator.remove();
                }
            }
        }
        // do  batch insert
        if (fundList.size() > 0) {
            fundDao.batchInsertFund(fundList);
        }
        // do batch update
        if (fundToUpdate.size() > 0) {
            fundDao.batchUpdateFund(fundToUpdate);
        }
    }

    /**
     * 查找所有基金
     */
    public List<Fund> findFunds() {
        return fundDao.findFunds();
    }

    /**
     * 查找基金净值数据
     *
     * @param code 基金代码
     */
    public List<FundNet> findFundNetByCode(String code) {
        return fundNetDao.findFundNetByCode(code);
    }

    /**
     * 获得基金净值数据
     *
     * @param fundCode 基金代码
     * @return 基金净值
     */
    private List<FundNet> fetchFundNetDataFromEasyMoney(String fundCode) {
        List<FundNet> fundNetList = new ArrayList<>();
        URI uri;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("fund.eastmoney.com/")
                    .setPath("/f10/F10DataApi.aspx")
                    .setParameter("type", "lsjz")
                    .setParameter("code", fundCode)
                    .setParameter("page", "1")
                    .setParameter("per", "20000")
                    .build();
            String strResult = HttpConnectionManager.executeHttpGet(uri, HttpClientContext.create());
            if (StringUtils.isEmpty(strResult)) {
                return null;
            }
            Document
                    doc = Jsoup.parse(strResult);
            Elements trs = doc.select("tbody").select("tr");
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                if (tds.size() == 7) {
                    FundNet fundNet = new FundNet();
                    fundNet.setCode(fundCode);
                    fundNet.setNetDate(df.parse(tds.get(0).text()));
                    fundNet.setUnitNetValue(StringUtils.isEmpty(tds.get(1).text()) ? 0 : Double.valueOf(tds.get(1).text()));
                    double accumulatedNetValue = Double.valueOf((StringUtils.isEmpty(tds.get(2).text().replace("%", "")) ? "0" : tds.get(2).text().replace("%", "")));
                    fundNet.setAccumulatedNetValue(accumulatedNetValue);
                    double dailyGrowthRate = Double.valueOf((StringUtils.isEmpty(tds.get(3).text().replace("%", "")) ? "0" : tds.get(3).text().replace("%", "")));
                    fundNet.setDailyGrowthRate(dailyGrowthRate);
                    fundNetList.add(fundNet);
                } else if (tds.size() == 6) {
                    logger.info("7日年化：" + fundCode);
                }
            }
        } catch (URISyntaxException e) {
            logger.debug(e.getMessage(), e);
        } catch (ParseException e) {
            logger.debug(e.getMessage(), e);
        }
        return fundNetList;
    }

    /**
     * 获得基金数据
     *
     * @return 基金
     */
    private List<SinaFinanceFund> fetchFundData() {
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("stock.finance.sina.com.cn/")
                    .setPath("fundfilter/api/openapi.php/MoneyFinanceFundFilterService.getFundFilterAll")
                    .setParameter("callback", "makeFilterData")
                    .setParameter("page", "1")
                    .setParameter("num", "200000")
                    .setParameter("dpc", "1")
                    .build();
        } catch (URISyntaxException e) {
            logger.debug(e.getMessage(), e);
        }
        String strResult = HttpConnectionManager.executeHttpGet(uri, HttpClientContext.create());
        if (StringUtils.isEmpty(strResult)) {
            return null;
        }
//                    Pattern pattern = Pattern.compile("\"data\"((?!data).)*]");
        Pattern pattern = Pattern.compile("\\[.*?]");
        Matcher matcher = pattern.matcher(strResult);
        while (matcher.find()) {
            strResult = matcher.group(0);
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<SinaFinanceFund> result = gson.fromJson(strResult, new TypeToken<List<SinaFinanceFund>>() {
        }.getType());
        return result;
    }

    @Override
    public void test() {
        try {
            String[] headers = {"1", "2", "3", "4"};
            ExportExcelUtil.exportBigDataExcel(Arrays.asList(headers), "test", fundNetDao);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    static class GetThread implements Callable<List<FundNet>> {

        private final HttpContext context;
        private final String fundCode;
        private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        public GetThread(String fundCode) {
            this.context = HttpClientContext.create();
            this.fundCode = fundCode;
        }

        @Override
        public List<FundNet> call() {
            List<FundNet> fundNetList = new ArrayList<>();
            URI uri;
            try {
                uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("fund.eastmoney.com/")
                        .setPath("/f10/F10DataApi.aspx")
                        .setParameter("type", "lsjz")
                        .setParameter("code", fundCode)
                        .setParameter("page", "1")
                        .setParameter("per", "200000")
                        .build();
                String strResult = HttpConnectionManager.executeHttpGet(uri, context);
                if (StringUtils.isEmpty(strResult)) {
                    return null;
                }
                Document
                        doc = Jsoup.parse(strResult);
                Elements trs = doc.select("tbody").select("tr");
                for (Element tr : trs) {
                    Elements tds = tr.select("td");
                    if (tds.size() == 7) {
                        FundNet fundNet = new FundNet();
                        fundNet.setCode(fundCode);
                        fundNet.setNetDate(df.parse(tds.get(0).text()));
                        fundNet.setUnitNetValue(StringUtils.isEmpty(tds.get(1).text()) ? 0 : Double.valueOf(tds.get(1).text()));
                        double accumulatedNetValue = Double.valueOf((StringUtils.isEmpty(tds.get(2).text().replace("%", "")) ? "0" : tds.get(2).text().replace("%", "")));
                        fundNet.setAccumulatedNetValue(accumulatedNetValue);
                        double dailyGrowthRate = Double.valueOf((StringUtils.isEmpty(tds.get(3).text().replace("%", "")) ? "0" : tds.get(3).text().replace("%", "")));
                        fundNet.setDailyGrowthRate(dailyGrowthRate);
                        fundNetList.add(fundNet);
                    } else if (tds.size() == 6) {
//                        logger.info("7日年化：" + fundCode);
                    }
                }
            } catch (URISyntaxException e) {
                logger.debug(e.getMessage(), e);
            } catch (ParseException e) {
                logger.debug(e.getMessage(), e);
            }
            return fundNetList;
        }
    }
}
