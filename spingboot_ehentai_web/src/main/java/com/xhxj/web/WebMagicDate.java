package com.xhxj.web;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

@Component
public class WebMagicDate implements Pipeline {
    @Autowired
     EheitaiCatalogService eheitaiCatalogDao;

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> a = resultItems.get("jobInfo");
        EheitaiCatalog eheitaiCatalog = resultItems.get("eheitaiCatalog");
        System.out.println("///////////////////////////////////////////////////////");
        System.out.println("eheitaiCatalog="+eheitaiCatalog);


    }
}
