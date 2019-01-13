package com.xhxj.web;

import com.xhxj.service.EheitaiDetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/**
 * 为了解决在webmagic中不能调用dao的问题
 */
@Component
public class WebMagicScheduler extends DuplicateRemovedScheduler {


    @Override
    public Request poll(Task task) {
        return null;
    }
}
