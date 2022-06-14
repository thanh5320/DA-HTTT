//package org.logstashplugins.utils.http;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//
//public class Http {
//    private static final Integer CONNECTION_TIMEOUT = 1000;
//    private static final Integer CONNECTION_REQUEST_TIMEOUT = 500;
//    private static final Integer SOCKET_TIMEOUT = 10000;
//    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
//            .setConnectTimeout(CONNECTION_TIMEOUT)
//            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
//            .setSocketTimeout(SOCKET_TIMEOUT)
//            .build();
//
//    public static CloseableHttpClient newPoolingHttpClient(int maxPerRoute, int totalMax) {
//        PoolingHttpClientConnectionManager connManager
//                = new PoolingHttpClientConnectionManager();
//        connManager.setDefaultMaxPerRoute(maxPerRoute);
//        connManager.setMaxTotal(totalMax);
//        return HttpClients.custom()
//                .setConnectionManager(connManager)
//                .build();
//    }
//
//  /*   * connTimeout: time to establish conn
//     * connReqTimeout:
//     * socketTimeout: time waiting for data after establish
//     *
//*/
//    public static RequestConfig newRequestConfig(int connTimeout, int connReqTimeout, int socketTimeout) {
//        return RequestConfig.custom()
//                .setConnectTimeout(connTimeout)
//                .setConnectionRequestTimeout(connReqTimeout)
//                .setSocketTimeout(socketTimeout)
//                .build();
//    }
//
//    public static RequestConfig newDefaultRequestConfig() {
//        return REQUEST_CONFIG;
//    }
//}
