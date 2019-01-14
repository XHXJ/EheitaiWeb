package com.xhxj.web;

import com.alibaba.fastjson.JSON;
import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.daomain.Proxies;
import com.xhxj.daomain.ProxiesBean;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;

import java.awt.print.Pageable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PropertySource("classpath:Configuration.properties")
@Component
@EnableAsync
public class Start {
    @Autowired
    AnalysisUrl analysisUrl;

    @Autowired
    WebMagic webMagic;

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;

    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;

    @Value("${url}")
    private String url;

    /**
     * 开始爬虫
     */
    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = 1 * 60 * 60 * 1000)
    public void Start() {


//        analysisUrl.getHttp();
//        //获取解析结果存入sql
//        analysisUrl.analysisHtml();
//        //下载页面


        //把sql中没有爬的连接全部丢给爬虫
        //这里以后要改要有条件的查询


        List<EheitaiCatalog> all = eheitaiCatalogService.findAll();
        List<String> urlall = new ArrayList<>();

        //添加完成查询方法,如果完成的不需要爬取


        //如果sql中有数据就去爬
        if (all.size() != 0) {
            for (EheitaiCatalog eheitaiCatalog : all) {
                urlall.add(eheitaiCatalog.getUrl());
            }
            //去看看有没有之前报错的数据也一并给他丢进去了
            urlall.addAll(readErrorUrl());

            //这里是需要爬取的对象
            String[] urllist = urlall.toArray(new String[urlall.size()]);


            //获取代理对象
            /**
             * 替换爬虫了!!!!
             *
             *
             */
            //webMagic.httpweb(urlall, getHttpProxy());
            webMagic.httpweb2(urlall,getHttpProxy(),eheitaiDetailPageService);


            //这里的逻辑需要优化,之前作品应该完成

            //这是第一次爬取所有报错的连接
            List<String> errorUrl = readErrorUrl();


            //如果有错误地址就一直重复爬取
            //在重复爬取几次后,就只爬取图片页面的数据
            int count = 2;
            int i = 0;
            while (errorUrl.size() != 0) {
                List<String> errorUrlCount = new ArrayList<>();
                //重复几次后只爬取图片页面
                if (i == count) {
                    for (String s : errorUrl) {
                        String[] split = s.split("=");
                        if (split.length == 1) {
                            errorUrlCount.add(s);
                        }

                    }
                    if (errorUrlCount.size() == 0) {
                        //如果都没有图片了那就跳出循环结束爬虫
                        break;
                    }

                    webMagic.httpweb(errorUrlCount, getHttpProxy());
                }


                //获取新的连接池
                webMagic.httpweb(errorUrl, getHttpProxy());
                i++;
            }

        }
    }

    /**
     * 获取多个代理对象
     * 使其不重复
     *
     * @return
     */
    private List<Proxy> getHttpProxy() {
        List<Proxy> proxies = new ArrayList<>();

        Proxy httpProxyOne = getHttpProxyOne();
        //记录耗时
        Date date = new Date();

        int sum = 3;

        while (proxies.size() < sum) {

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (httpProxyOne != null) {
                if (proxies.size() > 0) {
                    //如果不是第一个就去对比
                    for (Proxy proxy : proxies) {
                        String host = proxy.getHost();

                        if (!httpProxyOne.getHost().equals(host)) {
                            proxies.add(httpProxyOne);
                            //如果当前获取到的代理服务器已经大于三个了,就返回
                            break;

                        }
                        //别忘了重新访问赋值
                        httpProxyOne = getHttpProxyOne();

                    }
                } else {
                    //如果是第一个就添加一个代理服务器
                    proxies.add(httpProxyOne);
                }

            } else {
                //重新去调用获取代理服务器
                System.out.println("获取代理服务器失败检查连接池获取");
                getHttpProxy();
            }
        }

        Date date1 = new Date();

        System.out.println("获取连接池["+sum+"]耗时:"+(date1.hashCode()-date.hashCode())/1000+"秒");

        return proxies;
    }

    /**
     * @return
     */
    private List<String> readErrorUrl() {

        try {

            List<String> list = Files.readAllLines(Paths.get("./errorUrl.txt"), StandardCharsets.UTF_8);
            //读取完毕之后就清除之前的
            FileWriter fileWriter = new FileWriter("./errorUrl.txt");
            fileWriter.write("");
            fileWriter.close();

            //读取被ban的url
            List<String> banlist = Files.readAllLines(Paths.get("./banUrl.txt"), StandardCharsets.UTF_8);
            FileWriter banUrl = new FileWriter("./banUrl.txt");
            banUrl.write("");
            banUrl.close();


            //合并两个读取的连接
            list.addAll(banlist);

            List<String> listAll = new ArrayList<>();
            //处理重复数据
            for (String s : list) {


                //如果作品已完成,就不要添加连接
                //更具url
                EheitaiCatalog eheitaiCatalog = eheitaiCatalogService.findByUrl(s);
                //有才去执行
                if (eheitaiCatalog != null) {


                    Integer id = eheitaiCatalog.getId();
                    //更具获取到的id去查询作品是否完成
                    //先查询总下载页数
                    Integer count = eheitaiDetailPageService.findByUrlCount(id);
                    //如果作品本身的总页数,不等于sql中的总页数,继续爬取
                    if (eheitaiCatalog.getLength() != count) {
                        listAll.add(s);
                    }

                }
                //s为连接
                //如果sql中已经存在该页面就不需要添加
                String byUrl = eheitaiDetailPageService.findByUrl(s);
                if (byUrl == null) {
                    listAll.add(s);
                }

            }


            return listAll;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();

    }


    /**
     * 获取网站上的代理服务器地址
     *
     * @return
     */
    private Proxy getHttpProxyOne() {
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


//                proxies = JSON.parseObject(content, Proxies.class);


                String[] split = content.split("\n");
                for (String s : split) {
                    String[] split1 = s.split(":");
                    objects = new Proxy(split1[0], Integer.valueOf(split1[1]));
                }


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

    @Async
    @Scheduled(initialDelay = 60 * 1000, fixedDelay =  60 * 1000)
    public void stop(){

        System.out.println("-------------------------------"+"\n"+"每60秒执行一次");
        //执行关闭
        webMagic.stop();

    }
}
