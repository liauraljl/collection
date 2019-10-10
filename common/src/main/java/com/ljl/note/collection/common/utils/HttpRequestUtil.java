package com.ljl.note.collection.common.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestUtil {

    private static final int CONNECT_TIMEOUT = 500;
    private static final int SOCKET_TIMEOUT = 2000;

    private static final String URL_REQ_TYPE = "ConsumerURL";
    private static CloseableHttpClient HTTP_CLIENT;

    /**
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params) throws Exception {
        return get(url, params, SOCKET_TIMEOUT);
    }

    /**
     * @param url
     * @param timeout 读取超时时间
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params, int timeout) throws Exception {
        if (params != null && !params.isEmpty()) {
            String pstr = Joiner.on("&").withKeyValueSeparator("=").useForNull("null").join(params);
            StringBuilder sb = new StringBuilder(url);
            char c = url.indexOf('?') < 0 ? '?' : '&';
            sb.append(c);
            sb.append(pstr);
            url = sb.toString();
        }
        return get(url, timeout);
    }

    public static String get(String url) throws Exception {
        return get(url, SOCKET_TIMEOUT);
    }

    /**
     * @param url
     * @param timeout 读取超时时间
     * @return
     * @throws Exception
     */
    public static String get(String url, int timeout) throws Exception {
        /*if (log.isDebugEnabled()) {
            log.debug("m : {}, u : {}", "GET", url);
        }*/
        return request(new HttpGet(url), null, null, timeout);
    }

    /**
     * @param url
     * @param params
     * @return resp
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params) throws Exception {
        return post(url, params, SOCKET_TIMEOUT);
    }

    /**
     * @param url
     * @param params
     * @param timeout 读取超时时间
     * @return resp
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params, int timeout) throws Exception {
        /*if (log.isDebugEnabled()) {
            log.debug("m : {}, u : {}, p : {}", "POST", url, JSON.toJSONString(params));
        }*/
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> list = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(list));
        return request(httpPost, null, null, timeout);
    }

    /**
     * @param url
     * @param obj
     * @return
     * @throws Exception
     */
    public static String postJson(String url, Object obj) throws Exception {
        return postJson(url, obj, SOCKET_TIMEOUT);
    }

    /**
     * @param url
     * @param obj
     * @param timeout 读取超时时间
     * @return
     * @throws Exception
     */
    public static String postJson(String url, Object obj, int timeout) throws Exception {
        /*if (log.isDebugEnabled()) {
            log.debug("m : {}, u : {}, p : {}", "POST", url, obj != null ? JSON.toJSONString(obj) : null);
        }*/
        HttpPost httpPost = new HttpPost(url);
        Map<String, String> heads = new HashMap<>();
        heads.put(HTTP.CONTENT_TYPE, "application/json");
        String input;
        if (obj instanceof String) {
            input = String.valueOf(obj);
        } else {
            input = JSON.toJSONString(obj);
        }
        StringEntity stringEntity = new StringEntity(input, "UTF-8");
        httpPost.setEntity(stringEntity);
        return request(httpPost, heads, null, timeout);
    }

    /**
     * 带HTTP header的POST请求
     * @param url
     * @return headers
     * @throws Exception
     */
    public static String postJsonWithHeader(String url,Map<String, String> headers) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        return request(httpPost, headers, null, SOCKET_TIMEOUT);
    }

    /**
     * @param requestBase
     * @param heads
     * @param charset
     * @param timeout     读取超时时间
     * @return
     * @throws Exception
     */
    private static String request(final HttpRequestBase requestBase, Map<String, String> heads, final Charset charset,
                                  int timeout) throws Exception {
        String urlPath = requestBase.getURI().getPath();
        if ("/".equals(urlPath) || StringUtils.isEmpty(urlPath)) {
            urlPath = requestBase.getURI().getHost();
        }

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(CONNECT_TIMEOUT).build();
        if (heads != null && !heads.isEmpty()) {
            for (Map.Entry<String, String> entry : heads.entrySet()) {
                requestBase.addHeader(entry.getKey(), entry.getValue());
            }
        }
        requestBase.setConfig(requestConfig);
        HttpClient httpClient = getClient();
        final long startTime = System.currentTimeMillis();
        final String catName = urlPath;
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response) throws IOException {
                int status = response.getStatusLine().getStatusCode();
                long elapsed = System.currentTimeMillis() - startTime;
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    try {
                        String resp = entity != null ? EntityUtils.toString(entity, charset) : null;
                        try {
                            //Cat.logTransaction(URL_REQ_TYPE, catName, startTime, elapsed, Message.SUCCESS);
                        } catch (Exception e) {
                            /*log.warn("cat exception", e);*/
                        }
                        /*if (log.isDebugEnabled()) {
                            log.debug("used time {}ms", elapsed);
                            log.debug("--------------------Response content--------------------");
                            log.debug(resp);
                            log.debug("--------------------Response content--------------------");
                        }*/
                        return resp;
                    } catch (ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    try {
                        //Cat.logTransaction(URL_REQ_TYPE, catName, startTime, elapsed, Message.FAIL);
                    } catch (Exception e) {
                        //log.warn("cat exception", e);
                    }
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };

        return httpClient.execute(requestBase, responseHandler);
    }

    private static synchronized HttpClient getClient() {
        if (HTTP_CLIENT == null) {
            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
            // 每个路由最大连接数
            manager.setDefaultMaxPerRoute(70);
            // 整体最大连接数
            manager.setMaxTotal(200);
            HTTP_CLIENT = HttpClients.createMinimal(manager);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        HTTP_CLIENT.close();
                    } catch (IOException e) {
                    }
                }
            });
        }
        return HTTP_CLIENT;
    }
}
