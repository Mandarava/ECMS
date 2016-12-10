package com.finance.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import com.finance.dao.FundDao;
import com.finance.exception.BusinessException;
import com.finance.model.JavaBean.SinaFinanceFund;
import com.finance.model.pojo.Fund;
import com.finance.service.FundService;
import com.finance.util.myutil.HttpConnectionManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Service
public class FundServiceImpl implements FundService {

    private static Logger logger = LoggerFactory.getLogger(FundServiceImpl.class);

    @Resource
    private FundDao fundDao;

    @PostConstruct
    public void init() {
        // do something init
    }

    @PreDestroy
    public void destroy() {
        // do something destroy
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
    @Override
    public List<Fund> findFunds() {
        return fundDao.findFunds();
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

}
