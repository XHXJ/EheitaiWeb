package com.xhxj.Component;


import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * 在这里开启异步多线程去下载需要的图片
 * 所有的bean应该从这里开始
 */
@Component
public class Download {

    private static final Logger logger = LogManager.getLogger(Download.class);
    //多线程传参过来赋值
    EheitaiCatalogService eheitaiCatalogService;
    EheitaiDetailPageService eheitaiDetailPageService;

    //使用连接池
    public static PoolingHttpClientConnectionManager cm = null;


    public Download(EheitaiCatalogService eheitaiCatalogService, EheitaiDetailPageService eheitaiDetailPageService) {
        this.eheitaiCatalogService = eheitaiCatalogService;
        this.eheitaiDetailPageService = eheitaiDetailPageService;
        cm = new PoolingHttpClientConnectionManager();
        //设置连接池的最大连接数
        cm.setMaxTotal(100);

    }


    @Async
    public void download(Integer i, EheitaiCatalogService eheitaiCatalogService, EheitaiDetailPageService eheitaiDetailPageService, Download download) {

        logger.info("这里是接收到的作品编号:" + i);

        //去查询当前作品的所有下载连接
        List<EheitaiDetailPage> byGidAndUrl = eheitaiDetailPageService.findByGid(i);


        //去查询作品的名字
        String name = eheitaiCatalogService.findGetName(i);


        for (EheitaiDetailPage s : byGidAndUrl) {
            download.downloadImgurl(s, name);
        }


    }


    @Async
    public void downloadImgurl(EheitaiDetailPage s, String name) {
        logger.info("图片开始下载:" + s + "作品名字:" + name);



        try {
            URL url = new URL(s.getImgUrl());

            CookieStore cookieStore = new BasicCookieStore();
//        System.out.println("传入cookieStore前的数据:" + cookieStore.toString());
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm)
                    //设置cookieStore,传入
                    .setDefaultCookieStore(cookieStore)
                    .build();


            URIBuilder uriBuilder = null;
            HttpGet httpGet = null;


                /**
                 * 这里需要修改为对象传参
                 */
                uriBuilder = new URIBuilder(s.getImgUrl());


                httpGet = new HttpGet(uriBuilder.build());

                //设置获取时的等待参数
                httpGet.setConfig(setConfig());
                //设置请求头
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3664.3 Safari/537.36");

                httpGet.setHeader("Cookie", "igneous=6c63cbdc0; ipb_member_id=805259; ipb_pass_hash=1a0592e854f1b08bcb9c2eb40b6455de; yay=0; lv=1546944712-1546960410");

                System.out.println("发起请求的信息：" + httpGet);



            CloseableHttpResponse response = null;

            try {
                response = client.execute(httpGet);
                if(response.getStatusLine().getStatusCode()==200) {
                    HttpEntity httpEntity = response.getEntity();
                    // 文件的后缀(.jpg)

                    File files = new File("D:/imgUrl/" + name + "/");
                    if (!files.exists()) {
                        files.mkdirs();
                    }
                    OutputStream outputStream = new FileOutputStream("D:/imgUrl/" + s.getFileLog());
                    httpEntity.writeTo(outputStream);

                }


//                FileOutputStream out = new FileOutputStream(file);
//                int index;
//                byte[] bytes = new byte[1024];
//                while ((index = inputStream.read(bytes)) != -1) {
//                    out.write(bytes, 0, index);
//                    out.flush();
//                }
//
//                inputStream.close();
//                out.close();






            } catch (IOException e) {
                System.out.println("'访问网站失败");
                    e.printStackTrace();
                }





        }catch (Exception e){
            System.out.println("????");
            e.fillInStackTrace();
        }
    }


    //设置获取时的等待参数
    public RequestConfig setConfig() {
        //设置代理服务器
        HttpHost proxy = new HttpHost("127.0.0.1", 1081, "http");
        System.out.println(proxy);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000*100)//创建连接的超时时间,单位毫秒.
                .setConnectionRequestTimeout(300)//从连接池中创建连接的超时时间,单位毫秒
                .setSocketTimeout(100 * 1000)//数据的传输超时时间
                .setProxy(proxy)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        return requestConfig;
    }

}

