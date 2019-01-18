package com.xhxj.controller;


import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import com.xhxj.service.ErrorProxyService;
import com.xhxj.utils.ErrorProxyUtils;
import com.xhxj.utils.GetProxy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.proxy.Proxy;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:Configuration.properties")
@Component
@EnableAsync
public class Start {

    /**
     * 所有的bean对象应该从这里开始
     */

    @Autowired
    AnalysisUrl analysisUrl;

    @Autowired
    WebMagic webMagic;

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;

    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;

    @Autowired
    WebMagicScheduler webMagicScheduler;

    @Autowired
    ErrorProxyUtils errorProxyUtils;

    @Autowired
    ErrorProxyService errorProxyService;


    /**
     * 开始爬虫
     */
    @Async
    @Scheduled(initialDelay = 1000, fixedDelay = 1 * 60 * 60 * 1000)
    public void Start() {


        //获取最初要开始爬取的数据

        //解析想要获取的数据
        analysisUrl.getHttp();
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

        while (true) {
            //把sql中没有爬的连接全部丢给爬虫
            //这里以后要改要有条件的查询


            List<EheitaiCatalog> all = eheitaiCatalogService.findByComplete(0);
            List<String> urlall = new ArrayList<>();

            //添加完成查询方法,如果完成的不需要爬取

            for (EheitaiCatalog eheitaiCatalog : all) {
                urlall.add(eheitaiCatalog.getUrl());
            }


            //如果sql中有数据就去爬
            if (all.size() != 0) {

                //去看看有没有之前报错的数据也一并给他丢进去了


                //第一次爬虫开始

                //这里的逻辑需要优化,之前作品应该完成
                //这是第一次爬取所有报错的连接
                //如果有错误地址就一直重复爬取
                //在重复爬取几次后,就只爬取图片页面的数据

                List<String> list = readErrorUrl();

                urlall.addAll(list);

                webMagic.httpWebStart(urlall, getHttpProxy(), eheitaiCatalogService, eheitaiDetailPageService);

                Integer integer = eheitaiDetailPageService.findByUrlCountPage();

                Integer integer1 = eheitaiCatalogService.findByUrlCountPage();


                System.out.println("已爬取:" + integer1 + "页@剩:" + integer + "页");
                if (integer.equals(integer1)) {
                    System.out.println("爬取完毕!!!");

                    //报错得url错误文件数据...因为已经没啥用了避免下次抓取新页面还在
                    newErrorUrl();


                    //结束爬虫罪恶得一生
                    System.exit(0);
                }


            } else {
                System.out.println("所有数据已爬取成功~~~~~~~~~~~~~~~~~~~~~~~~");
            }
        }
    }

    private void newErrorUrl() {

        //读取完毕之后就清除之前的

        try {
            FileWriter fileWriter = new FileWriter("./errorUrl.txt");

            fileWriter.write("");
            fileWriter.close();

            //读取被ban的url
            FileWriter banUrl = new FileWriter("./banUrl.txt");
            banUrl.write("");
            banUrl.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    GetProxy getProxy;

    /**
     * 获取多个代理对象
     * 使其不重复
     *
     * @return
     */
    private List<Proxy> getHttpProxy() {

        //获取大佬的代理池
        return getProxy.getHttpProxyDao();

        //获取无忧代理
//        return getProxy.getProxiesWuYou();
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

//            List<String> all = new ArrayList<>();
//
//            for (String url : list) {
//                //再进行二次去重
//                String byUrl = eheitaiDetailPageService.findByUrl(url);
//                if (!StringUtils.isNotEmpty(byUrl)){
//                    all.add(url);
//                }
//
//            }

            return list;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();

    }


    /**
     * 使用多线程去异步执行下面的子线程
     */
    @Async
    @Scheduled(initialDelay = 1 * 1000, fixedDelay = 15 * 1000)
    public void stop() {

        System.out.println("-------------------------------" + "\n" + "处理被ban或报错的代理服务器");
        //执行关闭
//        webMagic.stop();


        //把报错的id和被封的id存入sql中;
        errorProxyUtils.analysis();

        //要去把代理的连接池爬出来,重新替换代理


        //定期删除状态为error,总报错次数不超过几次的数据

        errorProxyService.deleteByStateAndCounter(20);

    }


    @Async
    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 15 * 1000)
    public void Remove() {

        System.out.println("查询了数据库中的全部图片url,用于去重");

        webMagicScheduler.remove();


    }


}
