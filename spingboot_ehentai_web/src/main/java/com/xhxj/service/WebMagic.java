package com.xhxj.service;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.selector.Selectable;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import java.util.UUID;

@Component
public class WebMagic implements PageProcessor {

    @Autowired
    AnalysisUrl analysisUrl;
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;
    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;
    @Autowired
    WebMagicDate webMagicDate;

    //写出的测试网页
    private String catalog = "e://2.html";
    //网页的编码格式
    private String httpCharset = "";
    //设置一个变量储存当前图片地址用来对比是否到了最后一页
    private String imgSrc = "";

    @Override
    public void process(Page page) {


        //在这里处理获取到的页面
        String string = page.getHtml().css("div.gdtm a").links().toString();
        if (string == null) {
            //如果空了就说明当前是在图片页面
            System.out.println("这就是第二次访问了:");

            //写出文件
            writeFile(page);

            //判断当前在第几页,还有几页到最后


        } else {
            //没空就说明还在首页
            System.out.println("第一次访问" + string);
            //把这个页面丢给爬虫去访问
            page.addTargetRequest(string);
        }


//        page.putField("jobInfo",all);

    }

    public void writeFile(Page page) {
        //把文件写出去看看
        byte[] bytes1 = page.getHtml().toString().getBytes();
        //写出文件看看乍回事
        InputStream content = new ByteArrayInputStream(bytes1);
        try {

            FileOutputStream downloadFile = new FileOutputStream(catalog);
            int index;
            byte[] bytes = new byte[1024];
            while ((index = content.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }

            downloadFile.close();
        } catch (IOException e) {
            System.out.println("写出文件时报错");
            e.printStackTrace();
        }
    }


    @PostConstruct
    public void httpweb() {
        //抓取页面

        //自己蛋疼写的轮子,至少能用
/*
        httpCharset = analysisUrl.getHttp();
        System.out.println("网站的编码格式为:" + httpCharset);
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
*/

        //下载页面
        //获取下载链接

        //应该写service层的..
        String url = "";
        String title = "";
        //要爬取得数据的divid
        List<EheitaiCatalog> byDivId = eheitaiCatalogDao.findByDivId(1329034);
        for (EheitaiCatalog eheitaiCatalog : byDivId) {
            url = eheitaiCatalog.getUrl();
            title = eheitaiCatalog.getTitle();
        }
        System.out.println("e://" + title + "/title/" + UUID.randomUUID() + ".html");

        System.out.println("要爬的网站路径~~~~~~" + url);
        Spider spider = Spider.create(new WebMagic())
                .addUrl(url)
                .addPipeline(webMagicDate)
                .thread(1);

        //设置爬虫代理
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1", 1081)));
        spider.setDownloader(httpClientDownloader);
        spider.run();
    }
    // 用来设置参数
    /**
     * Site.me()可以对爬虫进行一些参数配置，包括编码、超时时间、重试时间、重试次数等。在这里我们先简单设置一下：重试次数为3次，重试时间为3秒。
     */
    Site site = Site
            .me()
            .setTimeOut(10 * 1000) // 设置超时时间，10秒
            .setRetrySleepTime(3 * 1000) // 设置重试时间（如果访问一个网站的时候失败了，Webmagic启动的过程中，会每3秒重复再次执行访问）
            .setRetryTimes(3) // 设置重试次数
            .setCharset("UTF-8") // 获取UTF-8网站的数据
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3664.3 Safari/537.36")
            .addHeader("Cookie", "igneous=6c63cbdc0; ipb_member_id=805259; ipb_pass_hash=1a0592e854f1b08bcb9c2eb40b6455de; yay=0; lv=1546944712-1546960410");


    @Override
    public Site getSite() {
        return site;
    }


}
