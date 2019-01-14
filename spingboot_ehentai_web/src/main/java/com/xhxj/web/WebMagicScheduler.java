package com.xhxj.web;

import com.xhxj.service.EheitaiDetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 为了解决在webmagic中不能调用dao的问题
 */
@Component
public class WebMagicScheduler  implements DuplicateRemover {
    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;
    public WebMagicScheduler() {
    }

    /**
     * 解决多线程中的传参问题
     * @param eheitaiDetailPageService
     */
    public WebMagicScheduler(EheitaiDetailPageService eheitaiDetailPageService) {
        this.eheitaiDetailPageService = eheitaiDetailPageService;
    }


    //@Autowired
    //EheitaiDetailPageService eheitaiDetailPageService;

    @PostConstruct
    private void test(){
        //List<String> byUrl = eheitaiDetailPageService.findByUrl();


       // System.out.println();
    }


    private static Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    @Override
    public boolean isDuplicate(Request request, Task task) {
        List<String> byUrl =  eheitaiDetailPageService.findByUrl();
        System.out.println();

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
