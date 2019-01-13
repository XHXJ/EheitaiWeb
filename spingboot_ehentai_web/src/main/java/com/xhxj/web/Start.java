package com.xhxj.web;

import com.alibaba.fastjson.JSON;
import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.Proxies;
import com.xhxj.service.EheitaiCatalogService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:Configuration.properties")
@Component
public class Start {
    @Autowired
    AnalysisUrl analysisUrl;

    @Autowired
    WebMagic webMagic;

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;

    @Value("${url}")
    private String url;

    /**
     * 开始爬虫
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 1 * 60 * 60 * 1000)
    public void Start() {
        //把sql中没有爬的连接全部丢给爬虫
        //这里以后要改要有条件的查询

        List<EheitaiCatalog> all = eheitaiCatalogService.findAll();
        List<String> urlall = new ArrayList<>();
        //如果sql中有数据就去爬
        if (all.size() != 0) {
            for (EheitaiCatalog eheitaiCatalog : all) {
                urlall.add(eheitaiCatalog.getUrl());
            }
            //去看看有没有之前报错的数据也一并给他丢进去了



            //这里是需要爬取的对象
            String[] urllist = urlall.toArray(new String[urlall.size()]);


            //获取代理对象
            Proxies httpProxy = getHttpProxy();
            //开始爬取
            webMagic.httpweb(urlall,httpProxy);


        }
    }

    public void Test01() {
        analysisUrl.getHttp();
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

    }


    /**
     * 获取网站上的代理服务器地址
     * @return
     */
    private Proxies getHttpProxy() {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;

        Proxies proxies = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");


                proxies  = JSON.parseObject(content, Proxies.class);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response

            try {
                response.close();
                return  proxies;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }


}
