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
import java.util.ArrayList;
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
        cm.setMaxTotal(1000);

    }


    @Async
    public void download(String[] i, EheitaiCatalogService eheitaiCatalogService, EheitaiDetailPageService eheitaiDetailPageService, Download download) {

        logger.info("这里是接收到的作品编号:" + i[0]);




            download.downloadImgurl(i);



    }


    @Async
    public void downloadImgurl(String[] strings) {
        logger.info("图片开始下载:" + strings[1] + "作品名字:" + strings[0]);





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
        try {
            uriBuilder = new URIBuilder(strings[1]);


            httpGet = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {

            System.out.println("设置连接出错");

            e.printStackTrace();
        }

        //设置获取时的等待参数
                httpGet.setConfig(setConfig());
                //设置请求头
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3664.3 Safari/537.36");

                httpGet.setHeader("Cookie", "igneous=6c63cbdc0; ipb_member_id=805259; ipb_pass_hash=1a0592e854f1b08bcb9c2eb40b6455de; yay=0; lv=1546944712-1546960410");

                System.out.println("发起请求的信息：" + httpGet);



            CloseableHttpResponse response = null;



                try {
                    response = client.execute(httpGet);
                } catch (IOException e) {
                    //把访问失败的网址加入到之前的
                    /**
                     * 待处理一些逻辑
                     */


                    e.printStackTrace();
                }



                if(response.getStatusLine().getStatusCode()==200) {
                    HttpEntity httpEntity = response.getEntity();
                    // 文件的后缀(.jpg)

                    File files = new File("D:/imgUrl/" + strings[0] + "/");
                    if (!files.exists()) {
                        files.mkdirs();
                    }
                    try {

                        OutputStream outputStream = new FileOutputStream("D:/imgUrl/" + strings[0] + "/"+strings[2]);
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                        httpEntity.writeTo(bufferedOutputStream);
                        //写完关闭
                        outputStream.close();
                        bufferedOutputStream.close();

                    } catch (IOException e) {

                        System.out.println("写出文件报错");
                    }

                }else {

                    System.out.println("访问值不是200");
                }











    }

    //假装写个连接池
    private static List<HttpHost> allHttpHost = new ArrayList<>();


    //设置获取时的等待参数
    public RequestConfig setConfig() {
        //设置代理服务器
        //这个地址是自己的代理池,每次访问会给一个新的代理
        HttpHost proxy = new HttpHost("192.168.211.131", 8081, "http");
        System.out.println(proxy);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000*100)//创建连接的超时时间,单位毫秒.
                .setConnectionRequestTimeout(500)//从连接池中创建连接的超时时间,单位毫秒
                .setSocketTimeout(60 * 1000)//数据的传输超时时间
                .setProxy(proxy)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        return requestConfig;
    }

}

