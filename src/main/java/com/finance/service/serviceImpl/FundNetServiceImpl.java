package com.finance.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.finance.dao.FundNetDao;
import com.finance.datasource.DynamicDataSourceContextHolder;
import com.finance.exception.BusinessException;
import com.finance.model.dto.SinaFinanceFundNetDTO;
import com.finance.model.pojo.FundDO;
import com.finance.model.pojo.FundNetDO;
import com.finance.service.FundNetService;
import com.finance.service.FundService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import static com.finance.util.myutil.BaseConstants.RECORDS_PER_INSERT;

@Service
public class FundNetServiceImpl implements FundNetService {

    private static final int FUND_NET_PER_SELECT = 300;
    private static final int THREAD_POOL_SIZE = 10;
    private static final Logger logger = LoggerFactory.getLogger(FundNetServiceImpl.class);

    @Resource
    private FundNetService fundNetService;

    @Resource
    private FundService fundService;

    @Resource
    private FundNetDao fundNetDao;

    /**
     * 更新基金净值数据
     */
    @Override
    public void insertOrUpdateFundNetData() throws Exception {
        List<FundDO> fundList = fundService.findFunds();
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        while (fundList.size() > 0) {
            List<FundDO> funds = fundList.subList(0, fundList.size() > FUND_NET_PER_SELECT ? FUND_NET_PER_SELECT : fundList.size());
            List<Future<List<FundNetDO>>> futures = new ArrayList<>();
            // 获得到的净值数据
            List<FundNetDO> fetchedNetList = new ArrayList<>();
            for (FundDO fund : funds) {
                Callable<List<FundNetDO>> callable = new FetchFundNetDataFromSinaThread(fund.getCode());
                Future<List<FundNetDO>> future = pool.submit(callable);
                futures.add(future);
            }
            for (int i = 0; i < futures.size(); i++) {
                Future<List<FundNetDO>> future = futures.get(i);
                if (future.get() != null) {
                    fetchedNetList.addAll(future.get());
                }
            }
            if (fetchedNetList == null || fetchedNetList.size() == 0) {
                return;
            }
            // 当前数据库中存在的净值数据
            List<FundNetDO> result = fundNetDao.findFundNetDateByCodes(funds);
            Set<FundNetDO> fundNets = new HashSet<>(fetchedNetList);
            for (int i = 0; i < result.size(); i++) {
                FundNetDO temp = result.get(i);
                if (fundNets.contains(temp)) {
                    fundNets.remove(temp);
                }
            }
            List<FundNetDO> fundNetList = new ArrayList<>(fundNets);
            fundNetList.sort(Comparator.comparing(FundNetDO::getNetDate));
            if (fundNetList.size() > 0) {
                while (fundNetList.size() > 0) {
                    List<FundNetDO> subFundNetList = fundNetList.subList(0, fundNetList.size() > RECORDS_PER_INSERT ? RECORDS_PER_INSERT : fundNetList.size());
                    /* 自己注给自己，否则嵌套事务无法执行*/
                    fundNetService.batchInsertFundNetData(subFundNetList);
                    fundNetList.subList(0, fundNetList.size() > RECORDS_PER_INSERT ? RECORDS_PER_INSERT : fundNetList.size()).clear();
                }
            }
            fundList.subList(0, fundList.size() > FUND_NET_PER_SELECT ? FUND_NET_PER_SELECT : fundList.size()).clear();
        }

        pool.shutdown();
    }

    /**
     * 分批插入基金净值数据
     */
    @Override
    public void batchInsertFundNetData(List<FundNetDO> fundNetList) throws BusinessException {
        int i = fundNetDao.batchInsertFundNetData(fundNetList);
        if (i != fundNetList.size()) {
            throw new BusinessException("更新或插入基金净值数据失败！");
        }
    }

    /**
     * 查找基金净值数据
     *
     * @param code 基金代码
     */
    public List<FundNetDO> findFundNetByCode(String code) {
        return fundNetDao.findFundNetByCode(code);
    }


    /**
     * 获得基金净值数据
     *
     * @param fundCode 基金代码
     * @return 基金净值
     */
    @Deprecated
    private List<FundNetDO> fetchFundNetDataFromEasyMoney(String fundCode) {
        List<FundNetDO> fundNetList = new ArrayList<>();
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
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                if (tds.size() == 7) {
                    FundNetDO fundNet = new FundNetDO();
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


    @Override
    public void test() {
//        try {
//            String[] headers = {"1", "2", "3", "4"};
//            ExportExcelUtil.exportBigDataExcel(Arrays.asList(headers), "test", fundNetDao);
//        } catch (IOException e) {
//            logger.debug(e.getMessage(), e);
//        }

        DynamicDataSourceContextHolder.setCustomerType("keen");
        int i = fundNetDao.findFundNetCount();
    }

    static class FetchFundNetDataFromSinaThread implements Callable<List<FundNetDO>> {

        private final HttpContext context;
        private final String fundCode;

        public FetchFundNetDataFromSinaThread(String fundCode) {
            this.context = HttpClientContext.create();
            this.fundCode = fundCode;
        }

        private String getFundType() {
            String currency;
            URI uri = null;
            try {
                uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("finance.sina.com.cn/")
                        .setPath(String.format("fund/quotes/%s/bc.shtml", fundCode)).build();
            } catch (URISyntaxException e) {
                logger.debug(e.getMessage(), e);
            }
            String html = HttpConnectionManager.executeHttpGet(uri, context);
            if (StringUtils.isEmpty(html)) {
                return null;
            }
            Pattern pattern = Pattern.compile("\\$FConfig\\['isCurrency'].*;");
            Matcher matcher = pattern.matcher(html);
            while (matcher.find()) {
                html = matcher.group(0);
            }
            currency = html.replaceAll("\\$FConfig\\['isCurrency']|;|'|=", "").trim();
            return currency;
        }

        @Override
        public List<FundNetDO> call() {
            String currency = getFundType();
            // 万份收益
            if (currency.equals("1")) {
                return null;
            }
            List<FundNetDO> result = null;
            URI uri = null;
            // http://stock.finance.sina.com.cn/fundInfo/api/openapi.php/CaihuiFundInfoService.getNav?callback=fundnetcallback&symbol=160706&page=1
            // http://stock.finance.sina.com.cn/fundInfo/api/openapi.php/CaihuiFundInfoService.getNavcur"  // 万份收益
            try {
                uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("stock.finance.sina.com.cn/")
                        .setPath("fundInfo/api/openapi.php/CaihuiFundInfoService.getNav")
                        .setParameter("callback", "fundnetcallback")
                        .setParameter("symbol", fundCode)
                        .setParameter("page", "1")
                        .setParameter("num", "200000")
                        .build();
            } catch (URISyntaxException e) {
                logger.debug(e.getMessage(), e);
            }
            String strResult = HttpConnectionManager.executeHttpGet(uri, context);
            if (StringUtils.isEmpty(strResult)) {
                return null;
            }
            if (strResult.contains("\"data\":null") || strResult.contains("\"total_num\":\"0\"")) {
                return null;
            }
            Pattern pattern = Pattern.compile("\\[.*?]");
            Matcher matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                strResult = matcher.group(0);
            }
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            List<SinaFinanceFundNetDTO> fundNets = gson.fromJson(strResult, new TypeToken<List<SinaFinanceFundNetDTO>>() {
            }.getType());
            if (fundNets != null && fundNets.size() > 0) {
                result = new ArrayList<>();
                for (int i = 0; i < fundNets.size(); i++) {
                    // the last one is the fund's initial value 1
                    if (i == fundNets.size() - 1) {
                        break;
                    }
                    SinaFinanceFundNetDTO sinaFinanceFundNetDTO = fundNets.get(i);
                    FundNetDO fundNet = new FundNetDO();
                    fundNet.setCode(fundCode);
                    fundNet.setNetDate(sinaFinanceFundNetDTO.getFbrq());
                    fundNet.setUnitNetValue(sinaFinanceFundNetDTO.getJjjz() == null ? 0 : sinaFinanceFundNetDTO.getJjjz());
                    fundNet.setAccumulatedNetValue(sinaFinanceFundNetDTO.getLjjz() == null ? 0 : sinaFinanceFundNetDTO.getLjjz());
                    if (sinaFinanceFundNetDTO.getJjjz() != null && fundNets.get(i + 1).getJjjz() != null) {
                        DecimalFormat df = new DecimalFormat("#.00000");
                        String dailyGrowthRate = df.format((sinaFinanceFundNetDTO.getJjjz() / fundNets.get(i + 1).getJjjz() - 1) * 100);
                        fundNet.setDailyGrowthRate(Double.valueOf(dailyGrowthRate));
                    } else {
                        fundNet.setDailyGrowthRate(0);
                        logger.info(fundCode + "has null values!");
                    }

                    result.add(fundNet);
                }
            }
            return result;
        }
    }

}
