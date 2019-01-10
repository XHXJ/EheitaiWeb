package com.xhxj.spingboot_ehentai_web;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.AnalysisUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Transient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpingbootEhentaiWebApplicationTests {

    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;

    @Test
    @Transactional
    @Rollback(false) //不自动回滚
    public void contextLoads() {

        EheitaiCatalog eheitaiCatalog = new EheitaiCatalog();
        eheitaiCatalog.setDivId(123);
        eheitaiCatalog.setTitle("好的一个标题");
        EheitaiDetailPage eheitaiDetailPage = new EheitaiDetailPage();
        eheitaiDetailPage.setLanguage("中文");
        EheitaiDetailPage eheitaiDetailPage1 = new EheitaiDetailPage();
        eheitaiDetailPage1.setLanguage("英文");
        eheitaiCatalog.getEheitaiDetailPages().add(eheitaiDetailPage);
        eheitaiCatalog.getEheitaiDetailPages().add(eheitaiDetailPage1);

        eheitaiCatalogDao.save(eheitaiCatalog);
        eheitaiDetailPageDao.save(eheitaiDetailPage);
        eheitaiDetailPageDao.save(eheitaiDetailPage1);


    }

    @Autowired
    AnalysisUrl analysisUrl;


    @Test
    public void Test01(){
        analysisUrl.getHttp();
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

    }


}

