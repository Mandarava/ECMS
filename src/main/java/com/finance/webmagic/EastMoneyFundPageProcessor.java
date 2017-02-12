package com.finance.webmagic;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 * Created by zt on 2016/10/1.
 */
public class EastMoneyFundPageProcessor implements PageProcessor {

    private static final String LIST_URL = "http://angularjs\\.cn/api/article/latest.*" ;

    /* @Override
     public void process(PageDTO page) {
         page.addTargetRequests(page.getHtml().links().regex("http://fund.eastmoney.com/f10/jjjz_160706.html").all());
         page.putField("fund", page.getHtml().$("#jztable"));
         if (page.getResultItems().get("fund") == null) {
             page.setSkip(true);
         }

     }*/
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static void main(String[] args) {
        Spider.create(new EastMoneyFundPageProcessor())
                .addUrl("http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=160706&page=1&per=2000")
                .thread(5)
                .run();
    }

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(LIST_URL).match()) {
            List<String> ids = new JsonPathSelector("$.data[*]._id").selectList(page.getRawText());
            if (CollectionUtils.isNotEmpty(ids)) {
                for (String id : ids) {
                    page.addTargetRequest("http://angularjs.cn/api/article/" + id);
                }
            }
        } else {
            page.putField("title", new JsonPathSelector("$.data.title").select(page.getRawText()));
            page.putField("content", new JsonPathSelector("$.data.content").select(page.getRawText()));
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
