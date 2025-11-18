package com.chder.gasheatsupply_cloud.utils;

import com.chder.gasheatsupply_cloud.config.GasheatSupplyProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RefreshScope
public class HttpClientUtil {

    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private GasheatSupplyProperties gasheatSupplyProperties;

    /**
     * get请求
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url){
        Map<String, String> headers = new HashMap<>();
        headers.put("token", gasheatSupplyProperties.getToken());
        Request.Builder builder = new Request.Builder().url(url).get();
        headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * post请求
     * @param url
     * @param data
     * @return
     * @throws IOException
     */
    public String postJson(String url, Object data) {
        // 将参数对象转为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);

        Map<String, String> headers = new HashMap<>();
        headers.put("token", gasheatSupplyProperties.getToken());
        Request.Builder builder = new Request.Builder().url(url).post(body);
        headers.forEach(builder::addHeader);
        Request request = builder.build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.body() != null) {
                try (ResponseBody responseBody = response.body()) {
                    return responseBody.string();
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            // 记录日志并抛出更合适的异常
            throw new RuntimeException("Failed to execute HTTP request", e);
        }
    }
}
