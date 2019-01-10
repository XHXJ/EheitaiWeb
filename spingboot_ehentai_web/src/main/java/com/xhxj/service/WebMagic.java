package com.xhxj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;

@Controller
public class WebMagic implements PageProcessor {

    @Autowired
    AnalysisUrl analysisUrl;

    private String httpCharset = "";

    @Override
    public void process(Page page) {

    }
    // 用来设置参数
    /**
     * Site.me()可以对爬虫进行一些参数配置，包括编码、超时时间、重试时间、重试次数等。在这里我们先简单设置一下：重试次数为3次，重试时间为3秒。
     */
    Site site = Site.me()
            .setTimeOut(10 * 1000) // 设置超时时间，10秒
            .setRetrySleepTime(3 * 1000) // 设置重试时间（如果访问一个网站的时候失败了，Webmagic启动的过程中，会每3秒重复再次执行访问）
            .setRetryTimes(3) // 设置重试次数
            .setCharset(httpCharset) // 获取UTF-8网站的数据
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2595.400 QQBrowser/9.6.10872.400");
    ;

    @Override
    public Site getSite() {
        return null;
    }

    @PostConstruct
    public void httpweb() {
        //抓取页面
//     httpCharset= analysisUrl.getHttp();
//    System.out.println("网站的编码格式为:" + httpCharset);
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

    }

    ;


}
