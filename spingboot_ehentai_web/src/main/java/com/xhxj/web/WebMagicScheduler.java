package com.xhxj.web;

import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 为了解决在webmagic中不能调用dao的问题
 */
@Component
public class WebMagicScheduler implements DuplicateRemover {
    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;

    public WebMagicScheduler() {
    }

    /**
     * 解决多线程中的传参问题
     *
     * @param eheitaiDetailPageService
     */
    public WebMagicScheduler(EheitaiCatalogService eheitaiCatalogService, EheitaiDetailPageService eheitaiDetailPageService) {
        this.eheitaiCatalogService = eheitaiCatalogService;
        this.eheitaiDetailPageService = eheitaiDetailPageService;
    }


    //@Autowired
    //EheitaiDetailPageService eheitaiDetailPageService;


    public void remove() {
        byUrl = eheitaiDetailPageService.findByUrl();
    }


    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    //数据库中所有的图片url连接用于去重复
    private static List<String> byUrl = new ArrayList<>();

    @Override
    public boolean isDuplicate(Request request, Task task) {
        List<String> all = byUrl;
        for (String s : all) {
            urls.add(s);
        }

        return !urls.add(getUrl(request));
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }
}
