package com.xhxj.service;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;
@Component
public class WebMagicDate implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> a = resultItems.get("jobInfo");
        System.out.println("WebMagicDate="+a);
    }
}
