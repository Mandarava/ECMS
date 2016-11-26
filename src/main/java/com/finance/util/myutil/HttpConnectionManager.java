package com.finance.util.myutil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLException;

/**
 * Created by zt on 2016/11/19.
 */
@Component
public class HttpConnectionManager {

    private static final int MAX_TOTAL = 200;
    private static final int DEFAULT_MAX_PER_ROUTE = 25;
    private static final int CONNECTION_REQUEST_TIMEOUT = 2500;
    private static final int CONNECT_TIMEOUT = 2500;
    private static final int SOCKET_TIMEOUT = 2500;
    private static final long MAX_IDLE_TIME = 3L;
    private static CloseableHttpClient httpClient;
    private static Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);

    public static String executeHttpGet(URI uri, HttpContext context) {
        if (context == null) {
            context = HttpClientContext.create();
        }
        String result = null;
        CloseableHttpResponse response = null;
        HttpGet httpget = new HttpGet(uri);
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        httpget.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpget, context);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                    EntityUtils.consume(entity);
                } else {
                    throw new ClientProtocolException("Response contains no content");
                }
            } else {
                throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        } finally {
            try {
                httpget.releaseConnection();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.debug(e.getMessage(), e);
            }
        }
        return result;
    }

    @PostConstruct
    public void init() {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", new SSLConnectionSocketFactory(SSLContexts.createSystemDefault()))
                .register("http", new PlainConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(MAX_TOTAL);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        // 将目标主机的最大连接数增加到50
//        HttpHost localhost = new HttpHost("http://blog.csdn.net",80);
//        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= 5) {
                // Do not retry if over max retry count
                return false;
            }
            if (exception instanceof InterruptedIOException) {
                // Timeout
                return false;
            }
            if (exception instanceof UnknownHostException) {
                // Unknown host
                return false;
            }
            if (exception instanceof ConnectTimeoutException) {
                // Connection refused
                return false;
            }
            if (exception instanceof SSLException) {
                // SSL handshake exception
                return false;
            }
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
            if (idempotent) {
                // Retry if the request is considered idempotent
                return true;
            }
            return false;
        };

        // 客户端级别请求的超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        /* CloseableHttpClient httpClient = HttpClients.createDefault();*/
        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(MAX_IDLE_TIME, TimeUnit.SECONDS)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .build();

        logger.info("httpclient 初始化完成！");
    }

    @PreDestroy
    public void destory() {
        try {
            httpClient.close();
            logger.info("httpclient 成功关闭！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


