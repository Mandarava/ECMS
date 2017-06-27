package com.finance.util.myutil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

/**
 * Created by zt
 * 2017/6/27 10:33
 */
@Component
@Singleton
public class HttpAsyncConnectionManager {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36";
    private static final Logger logger = LoggerFactory.getLogger(HttpAsyncConnectionManager.class);
    private static final int MAX_TOTAL = 100;
    private static final int DEFAULT_MAX_PER_ROUTE = 50;
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;
    private static final int CONNECT_TIMEOUT = 2500;
    private static final int SOCKET_TIMEOUT = 2000;
    private static CloseableHttpAsyncClient asyncHttpClient;
    private static IdleConnectionEvictor connEvictor;

    public static RequestConfig createDefaultRequestConfig() {
        // 配置请求的超时设置
        return RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
//                .setProxy(new HttpHost("127.0.0.1", 8888)).setAuthenticationEnabled(true) // for fiddler debug
                .build();
    }

    public void executeAsyncGet(URI uri, HttpContext context) {
        if (context == null) {
            context = HttpClientContext.create();
        }
        try {
            final CountDownLatch latch = new CountDownLatch(1);
            final HttpGet request = new HttpGet(uri);
            request.setHeader("User-Agent", USER_AGENT);
            request.setConfig(createDefaultRequestConfig());

            asyncHttpClient.execute(request, context, new FutureCallback<HttpResponse>() {

                public void completed(final HttpResponse response) {
                    latch.countDown();
                    logger.info(request.getRequestLine() + "->" + response.getStatusLine());
                    try {
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                String result = EntityUtils.toString(entity, "UTF-8");
                                logger.info(result);
                            }
                        }
                    } catch (IOException e) {
                        logger.info(e.getMessage(), e);
                    } finally {
                        HttpClientUtils.closeQuietly(response);
                    }
                }

                public void failed(final Exception ex) {
                    latch.countDown();
                    logger.info(request.getRequestLine() + "->" + ex);
                }

                public void cancelled() {
                    latch.countDown();
                    logger.info(request.getRequestLine() + " cancelled");
                }
            });
            latch.await();
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        }
    }

    @PostConstruct
    public void init() {
        Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry = RegistryBuilder.<SchemeIOSessionStrategy>create()
                .register("https", new SSLIOSessionStrategy(SSLContexts.createSystemDefault()))
                .register("http", new NoopIOSessionStrategy())
                .build();
        ConnectingIOReactor ioReactor;
        try {
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                    .build();
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (final IOReactorException ex) {
            throw new IllegalStateException(ex);
        }
        PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(ioReactor, iosessionFactoryRegistry);
        cm.setMaxTotal(MAX_TOTAL);
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);

        connEvictor = new IdleConnectionEvictor(cm);
        connEvictor.start();

        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(
                    HttpResponse response,
                    HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    keepAlive = 3000;
                }
                return keepAlive;
            }
        };

        // 客户端级别请求的超时配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        asyncHttpClient = HttpAsyncClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .setKeepAliveStrategy(keepAliveStrategy)
                .build();
        asyncHttpClient.start();

        logger.info("asyncHttpclient 初始化完成！");
    }

    @PreDestroy
    public void destory() {
        try {
            asyncHttpClient.close();
            // Shut down the evictor thread
            connEvictor.shutdown();
            logger.info("asyncHttpclient 成功关闭！");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static class IdleConnectionEvictor extends Thread {

        private final NHttpClientConnectionManager connMgr;

        private volatile boolean shutdown;

        public IdleConnectionEvictor(NHttpClientConnectionManager connMgr) {
            super();
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 5 sec
                        connMgr.closeIdleConnections(5, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                logger.info(ex.getMessage(), ex);
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }

    }

}
