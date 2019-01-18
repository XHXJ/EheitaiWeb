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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~报错");
//            this.downloadImgurl(strings);

        }


        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity httpEntity = response.getEntity();
            // 文件的后缀(.jpg)

            File files = new File("D:/imgUrl/" + strings[0] + "/");
            if (!files.exists()) {
                files.mkdirs();
            }
            try {
                //加个同步锁,省得高并发出问题
                synchronized(this){


                OutputStream outputStream = new FileOutputStream("D:/imgUrl/" + strings[0] + "/" + strings[2]);
                BufferedOutputStream bos = new BufferedOutputStream(outputStream);
                InputStream content = httpEntity.getContent();
                BufferedInputStream bis = new BufferedInputStream(content);


                // 读写数据
                int len;
                byte[] bytes = new byte[8 * 1024];
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);
                }


                bis.close();
                bos.close();
                }
            } catch (IOException e) {

                System.out.println("写出文件报错");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~报错");
//                this.downloadImgurl(strings);
            }

        } else {
            System.out.println("访问值不是200");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~报错");
//            this.downloadImgurl(strings);
        }


    }


    //    1.定义map, key-ip,value-weight
    static Map<String, Integer> ipMap = new HashMap<>();

    static {
        ipMap.put("127.0.0.1:1082", 1);
        ipMap.put("127.0.0.1:1083", 2);

    }

    //    Integer sum=0;
    Integer pos = 0;

    public String RoundRobin() {
        Map<String, Integer> ipServerMap = new ConcurrentHashMap<>();
        ipServerMap.putAll(ipMap);

        //    2.取出来key,放到set中
        Set<String> ipset = ipServerMap.keySet();

        //    3.set放到list，要循环list取出
        ArrayList<String> iplist = new ArrayList<String>();
        iplist.addAll(ipset);

        String serverName = null;

        //    4.定义一个循环的值，如果大于set就从0开始
        synchronized (pos) {
            if (pos >= ipset.size()) {
                pos = 0;
            }
            serverName = iplist.get(pos);
            //轮询+1
            pos++;
        }
        return serverName;

    }


    //设置获取时的等待参数
    public RequestConfig setConfig() {




        String[] split1 = RoundRobin().split(":");

        HttpHost proxy = new HttpHost(split1[0], Integer.valueOf(split1[1]));
//
        //嗯....又封了我两个ip,看来固定主机爬取是不可取的








        //设置代理服务器
        //这个地址是自己的代理池,每次访问会给一个新的代理
//        HttpHost proxy = new HttpHost("192.168.211.131", 8081, "http");


        //无法解决统一代理服务器去下载图片,图片会下载失败的问题
        /**
         * 方案1:
         * 用自己稳定的服务器去下载图片,
         * 这样大量图片都能稳定爬取,但是依然存在某些图片无法下载的情况,
         * 而且并发处理量不能太大,不然把自己服务器压垮了
         * 方案2:
         * 在爬虫爬取页面的时候就去下载图片,但是因为爬虫框架的原因,
         * 无法让同一个代理服务器去爬去同一个图片,要实现必然大改框架
         * 而且这样会异步处理变得没有意义
         * 方案3:
         * 用自己的服务器搭个代理连接池,在下载完毕一遍后去查sql,所有完成的图片将被标记,
         * 没下载图片可以重新爬去下载,但是重复逻辑太多.
         *
         *
         * .........下载问题待解决
         *
         *
         */


        System.out.println(proxy);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)//创建连接的超时时间,单位毫秒.
                .setConnectionRequestTimeout(1000)//从连接池中创建连接的超时时间,单位毫秒
                .setSocketTimeout(15000)//数据的传输超时时间
                .setProxy(proxy)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();
        return requestConfig;
    }

}

