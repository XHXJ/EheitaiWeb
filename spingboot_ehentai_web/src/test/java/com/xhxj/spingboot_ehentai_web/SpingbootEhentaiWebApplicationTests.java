package com.xhxj.spingboot_ehentai_web;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import com.xhxj.web.AnalysisUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpingbootEhentaiWebApplicationTests {

    @Autowired
    EheitaiDetailPageService eheitaiDetailPageDao;
    @Autowired
    EheitaiCatalogService eheitaiCatalogDao;

    @Test
    public void testdemo(){
        List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(1329034);
        for (EheitaiCatalog eheitaiCatalog : byGid) {
            System.out.println(eheitaiCatalog);
        }
    }

    @Test
    @Transactional
    @Rollback(false) //不自动回滚
    public void contextLoads() {

        EheitaiCatalog eheitaiCatalog = new EheitaiCatalog();
        eheitaiCatalog.setGid(123);
        eheitaiCatalog.setTitle("好的一个标题");
        EheitaiDetailPage eheitaiDetailPage = new EheitaiDetailPage();
        EheitaiDetailPage eheitaiDetailPage1 = new EheitaiDetailPage();
        eheitaiCatalog.getEheitaiDetailPages().add(eheitaiDetailPage);
        eheitaiCatalog.getEheitaiDetailPages().add(eheitaiDetailPage1);

        eheitaiCatalogDao.save(eheitaiCatalog);
        eheitaiDetailPageDao.save(eheitaiDetailPage);
        eheitaiDetailPageDao.save(eheitaiDetailPage1);


    }

    @Autowired
    AnalysisUrl analysisUrl;


    @Test
    @Transactional
    public void Test01(){
        analysisUrl.getHttp();
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

    }


}

