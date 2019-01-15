package com.xhxj.utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.IOException;
import java.util.List;

@Component
public class GetProxy {

    @Value("http://127.0.0.1:8080/proxy")
    private String url;



    /**
     * 获取网站上的代理服务器地址
     *
     * @return
     */
    public Proxy getHttpProxys() {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;


        Proxy objects = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");

                List<Proxy> proxies = JSONArray.parseArray(content,Proxy.class);

                System.out.println(proxies);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response

            try {
                response.close();

                return objects;

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
