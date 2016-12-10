package com.finance.service.serviceImpl;

import com.finance.dao.FundNetDao;
import com.finance.exception.BusinessException;
import com.finance.model.pojo.Fund;
import com.finance.model.pojo.FundNet;
import com.finance.service.FundNetService;
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
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import static com.finance.util.myutil.BaseConstants.RECORDS_PER_INSERT;

@Service
public class FundNetServiceImpl implements FundNetService {

    private static final int FUND_NET_PER_SELECT = 500;
    private static final int THREAD_POOL_SIZE = 10;
    private static Logger logger = LoggerFactory.getLogger(FundNetServiceImpl.class);
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
        List<Fund> fundList = fundService.findFunds();
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        while (fundList.size() > 0) {
            List<Fund> funds = fundList.subList(0, fundList.size() > FUND_NET_PER_SELECT ? FUND_NET_PER_SELECT : fundList.size());
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
//            fetchedNetList.sort(Comparator.comparing(FundNet::getCode).thenComparing(FundNet::getNetDate));
            List<FundNet> fundNetList = new ArrayList<>(fundNets);
            if (fundNetList.size() > 0) {
                while (fundNetList.size() > 0) {
                    List<FundNet> subFundNetList = fundNetList.subList(0, fundNetList.size() > RECORDS_PER_INSERT ? RECORDS_PER_INSERT : fundNetList.size());
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
    public void batchInsertFundNetData(List<FundNet> fundNetList) throws BusinessException {
        int i = fundNetDao.batchInsertFundNetData(fundNetList);
        if (i != fundNetList.size()) {
            throw new BusinessException("更新或插入基金净值数据失败！");
        }
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
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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

    static class GetThread implements Callable<List<FundNet>> {

        private final HttpContext context;
        private final String fundCode;

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
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
