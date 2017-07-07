package com.finance.util.myutil;

import com.finance.model.dto.HttpClientResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
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
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.AIMDBackoffManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultBackoffStrategy;
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
import javax.inject.Singleton;
import javax.net.ssl.SSLException;

/**
 * Created by zt on 2016/11/19.
 */
@Component
@Singleton
public class HttpConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);
    private static final int MAX_TOTAL = 100;
    private static final int DEFAULT_MAX_PER_ROUTE = 50;
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000; // 当连接池里没有可用连接时，等待的超时时间，这个值一定要设置，且不能太长，不然会出现大量请求等待。
    private static final int CONNECT_TIMEOUT = 2500;
    private static final int SOCKET_TIMEOUT = 2000;
    private static final long MAX_IDLE_TIME = 3L;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36";
    private static CloseableHttpClient httpClient;

    public static HttpClientResponse executeHttpGet(URI uri) {
        return executeHttpGet(uri, HttpClientContext.create());
    }

    public static HttpClientResponse executeHttpGet(URI uri, HttpContext context) {
        HttpClientResponse httpClientResponse = new HttpClientResponse();
        if (context == null) {
            context = HttpClientContext.create();
        }
        CloseableHttpResponse response = null;
        HttpGet httpget = new HttpGet(uri);
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
//                .setProxy(new HttpHost("127.0.0.1", 8888)).setAuthenticationEnabled(true) // for fiddler debug
//                .setProxy(new HttpHost("221.229.45.124", 808)) // 高匿代理
                .build();
        httpget.setHeader("User-Agent", USER_AGENT);
        httpget.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpget, context);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                httpClientResponse.setCode(HttpStatus.SC_OK);
                httpClientResponse.setMessage("请求成功！");
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    httpClientResponse.setData(result);
                    EntityUtils.consume(entity);
                } else {
                    throw new ClientProtocolException("Response contains no content");
                }
            } else {
                throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            httpClientResponse.setCode(response == null ? -1 : response.getStatusLine().getStatusCode());
            httpClientResponse.setMessage("网络连接失败，请检查网络！");
        } finally {
            try {
                httpget.releaseConnection();
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return httpClientResponse;
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

        AIMDBackoffManager backoffManager = new AIMDBackoffManager(cm);
        backoffManager.setPerHostConnectionCap(10);

        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(getHttpRequestRetryHandler())
                .setDefaultRequestConfig(getRequestConfig())
                .evictExpiredConnections()
                .evictIdleConnections(MAX_IDLE_TIME, TimeUnit.SECONDS)
                .setKeepAliveStrategy(getKeepAliveStrategy())
                .setBackoffManager(backoffManager)
                .setConnectionBackoffStrategy(new DefaultBackoffStrategy())
                .build();

        logger.info("httpclient 初始化完成！");
    }

    @PreDestroy
    public void destory() {
        try {
            httpClient.close();
            logger.info("httpclient 成功关闭！");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private ConnectionKeepAliveStrategy getKeepAliveStrategy() {
        return new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(
                    HttpResponse response,
                    HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    // Keep connections alive 5 seconds if a keep-alive value
                    // has not be explicitly set by the server
                    keepAlive = 3000;
                }
                return keepAlive;
            }
        };
    }

    private HttpRequestRetryHandler getHttpRequestRetryHandler() {
        // 请求重试处理
        return (exception, executionCount, context) -> {
            if (executionCount >= 3) {
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
            return !(request instanceof HttpEntityEnclosingRequest);
        };
    }

    private RequestConfig getRequestConfig() {
        // 客户端级别请求的超时配置
        return RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
    }

}


