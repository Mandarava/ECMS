package com.finance.util.myutil;

import com.finance.model.dto.ProxyIPDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zt on 2017/2/9.
 */
public class FetchProxyIP {

    private static final Logger logger = LoggerFactory.getLogger(FetchProxyIP.class);

    public List<ProxyIPDTO> fetchProxyIPInfo() {
        List<ProxyIPDTO> result = new ArrayList<>();
        URI uri = null;
        for (int i = 0; i < 50; i++) {
            try {
                uri = new URIBuilder()
                        .setScheme("http")
                        .setHost("www.kuaidaili.com/")
                        .setPath("free/inha/" + (i + 1))
                        .build();
            } catch (URISyntaxException e) {
                logger.debug(e.getMessage(), e);
            }
            String strResult = HttpConnectionManager.executeHttpGet(uri, HttpClientContext.create());
            if (!StringUtils.isEmpty(strResult)) {
                Document
                        doc = Jsoup.parse(strResult);
                Elements trs = doc.select("table").select("tbody").select("tr");
                for (Element tr : trs) {
                    Elements tds = tr.select("td");
                    System.out.println(tds.get(0).text() + ":" + tds.get(1).text());
                }

            }
        }
        return result;
    }
}
