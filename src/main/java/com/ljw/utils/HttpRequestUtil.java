package com.ljw.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author 林杰炜 linjiewei
 * @Title TODO 类描述
 * @Description TODO 详细描述
 * @date 2018/10/5 9:58
 */
public class HttpRequestUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtil.class);

    public static GetBuilder withGetBuilder() {
        return new GetBuilder();
    }

    public static PostBuilder withPostBuilder() {
        return new PostBuilder();
    }

    public static class GetBuilder {
        private String url;
        private Map<String, String> headers = Maps.newHashMap();
        private Map<String, String> body = Maps.newHashMap();
        private int connectionTimeout = 240000;
        private int socketTimeout = 240000;
        private int connectionRequestTimeout = 240000;

        public GetBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public GetBuilder withHeader(String key, String val) {
            this.headers.put(key, val);
            return this;
        }

        public GetBuilder withParam(String key, String val) {
            this.body.put(key, val);
            return this;
        }

        public GetBuilder withConnectionTimeout(int value) {
            this.connectionTimeout = value;
            return this;
        }

        public GetBuilder withSocketTimeout(int value) {
            this.socketTimeout = value;
            return this;
        }

        public GetBuilder withConnectionRequestTimeout(int value) {
            this.connectionRequestTimeout = value;
            return this;
        }

        public Map<String, String> buildAndPrint() throws IOException {
            Map<String, String> result = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            result = doGet(httpClient, url, headers, body, connectionRequestTimeout, connectionTimeout, socketTimeout);
            httpClient.close();
            return result;
        }

        private Map<String, String> doGet(CloseableHttpClient httpClient, String url, Map<String, String> headers, Map<String, String> body, int connectionRequestTimeout, int connectionTimeout, int socketTimeout) throws IOException {
            String param = "";
            Map<String, String> result = Maps.newHashMap();
            StringBuffer sb = new StringBuffer();
            String value;
            if (body.size() != 0) {
                sb.append("?");
                for (String key : body.keySet()) {
                    value = body.get(key);
                    value = URLEncoder.encode(value, "utf-8");
                    sb.append(key + "=" + value + "&");
                }
                param = sb.toString().substring(0, sb.toString().length() - 1);
            }
            HttpGet httpGet = new HttpGet(url + param);
            if (headers.size() != 0) {
                for (String key : headers.keySet()) {
                    String headerName = key;
                    String headerValue = headers.get(key);
                    httpGet.addHeader(headerName, headerValue);
                }
            }
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
            httpGet.setConfig(config);
            CloseableHttpResponse response = null;
            response = httpClient.execute(httpGet);
            Header[] h = response.getAllHeaders();
            for (int i = 0; i < h.length; i++) {
                Header header = h[i];
                result.put(header.getName(), header.getValue());
            }
            HttpEntity entity = response.getEntity();

            String content = EntityUtils.toString(entity);
            result.put("Response", content);

            log.debug(String.valueOf(result));
            EntityUtils.consume(entity);

            response.close();
            httpClient.close();

            return result;
        }
    }

    public static class PostBuilder {
        private String url;
        private List<NameValuePair> nvps = Lists.newArrayList();
        private Map<String, String> headers = Maps.newHashMap();
        private int connectionTimeout = 240000;
        private int socketTimeout = 240000;
        private int connectionRequestTimeout = 240000;
        private String jsonStr = "";

        public PostBuilder withUrl(String url) {
            this.url = url;
            return this;
        }

        public PostBuilder withHeader(String key, String val) {
            this.headers.put(key, val);
            return this;
        }

        public PostBuilder withJsonStr(String jsonStr) {
            this.jsonStr = jsonStr;
            return this;
        }

        public PostBuilder withParam(String key, String val) {
            this.nvps.add(new BasicNameValuePair(key, val));
            return this;
        }

        public PostBuilder withConnectionTimeout(int value) {
            this.connectionTimeout = value;
            return this;
        }

        public PostBuilder withSocketTimeout(int value) {
            this.socketTimeout = value;
            return this;
        }

        public PostBuilder withConnectionRequestTimeout(int value) {
            this.connectionRequestTimeout = value;
            return this;
        }

        public Map<String, String> buildAndPrint() throws IOException {
            Map<String, String> result = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            result = doPost(httpClient, this.url, this.nvps, this.headers, this.connectionRequestTimeout, this.connectionTimeout, this.socketTimeout);
            httpClient.close();
            return result;
        }

        private Map<String, String> doPost(CloseableHttpClient httpClient, String url, List<NameValuePair> nvps, Map<String, String> headers, int connectionRequestTimeout, int connectionTimeout, int socketTimeout) throws IOException {
            Map<String, String> result = Maps.newHashMap();
            HttpPost httpPost = new HttpPost(url);
            if (headers.size() != 0) {
                for (String key : headers.keySet()) {
                    String value = headers.get(key);
                    httpPost.addHeader(key, value);
                }
            }
            if (this.jsonStr.isEmpty()) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } else {
                StringEntity entity = new StringEntity(this.jsonStr, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                    .setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
            httpPost.setConfig(config);

            CloseableHttpResponse response = null;
            response = httpClient.execute(httpPost);
            Header[] h = response.getAllHeaders();
            for (int i = 0; i < h.length; i++) {
                Header header = h[i];
                result.put(header.getName(), header.getValue());
            }
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            result.put("Response", content);
            response.close();
            return result;
        }
    }
}
