//package org.logstashplugins.utils.http;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.HttpHeaders;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.util.EntityUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class HttpClient {
//    public static TypeReference<HashMap<String,String>> typeRef
//            = new TypeReference<>() {
//    };
//    private final CloseableHttpClient client;
//    private final ObjectMapper mapper = new ObjectMapper();
//    public HttpClient(int maxConn) {
//        this.client = Http.newPoolingHttpClient(maxConn, maxConn);
//    }
//
//    public Map<String,String> doPost(String url, Map<String,String> data) throws Exception {
//
//
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(Http.newDefaultRequestConfig());
//        if (data != null) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String dataStr = objectMapper.writeValueAsString(data);
//            StringEntity encodedFormEntity = new StringEntity(dataStr, "UTF-8");
//            httpPost.setEntity(encodedFormEntity);
//            httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
//            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//        }
//
//        CloseableHttpResponse response = this.client.execute(httpPost);
//        if (response.getStatusLine().getStatusCode() == 200) {
//            String body = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//            return mapper.readValue(body, typeRef);
//        }
//
//        httpPost.releaseConnection();
//        return null;
//    }
//}
