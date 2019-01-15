package com.xhxj.controller;

import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.*;

/**
 * 用来接受page传过来的对象进一步处理
 */
@Component
public class WebMagicDate implements Pipeline {

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;

    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;

    public WebMagicDate() {
    }

    public WebMagicDate(EheitaiDetailPageService eheitaiDetailPageService) {
        this.eheitaiDetailPageService = eheitaiDetailPageService;
    }
    @Override
    public void process(ResultItems resultItems, Task task) {
//            List<String> a = resultItems.get("jobInfo");
        //接受page传过来的对象
        EheitaiCatalog pageEheitaiCatalog = resultItems.get("eheitaiCatalog");
        //如果接收到的是page那就是图片下载链接
        EheitaiDetailPage pageeheitaiDetailPage = resultItems.get("eheitaiDetailPage");
        //如果传入的是pageeheitaiDetailPage对象
        Integer gid = resultItems.get("gid");
        //这里接收到的是gid
        String complete = resultItems.get("complete");


        int result = 0;

        if (pageEheitaiCatalog != null) {
            result = 1;
        }

        if (pageeheitaiDetailPage != null) {
            result = 2;
        }


//        if (null != gid ){
//            result = 4;
//        }

        switch (result) {
            case 1:
                //pageEheitaiCatalog 首页对象操作
                //更新作品的详细信息
                //查询从网页获取到的数据,sql中是否有这个对象
                Integer loggid = pageEheitaiCatalog.getGid();
                List<EheitaiCatalog> byGid = eheitaiCatalogService.findByGid(loggid);
                if (byGid.size() != 0) {
                    //操作作品对象
                    EheitaiCatalog eheitaiCatalog = byGid.get(0);
                    //要没有参数才去更新
                    if (eheitaiCatalog.getToken().equals("") || eheitaiCatalog.getToken() == null) {
                        //更新数据
                        System.out.println("page更新了数据库中的数据");
                        eheitaiCatalogService.updatePageEheitaiCatalog(pageEheitaiCatalog);
                    }

                } else {
                    System.out.println("从网页中获取到的数据,在sql中不存在,一般不会出现,出现就可能是爬了不该爬的数据,或者之前sql中的数据不完整");
                }
                break;
            case 2:
                //pageeheitaiDetailPage 图片页面操作
                //保存下载连接
                //在这里判断爬取的数据是否509
                eheitaiDetailPageService.saveEheitaiDetailPage(pageeheitaiDetailPage, gid);

                if (StringUtils.isNotEmpty(complete)) {
//                    System.out.println("@@@@@@@@@@@@@@@@@@作品:" + complete + "爬取完毕通知下载");
                    //作品已经要完成下载了
                    eheitaiCatalogService.saveComplete(gid);
                }
                break;
        }
    }



}
