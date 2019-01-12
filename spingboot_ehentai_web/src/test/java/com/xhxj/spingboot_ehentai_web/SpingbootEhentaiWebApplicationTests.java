package com.xhxj.spingboot_ehentai_web;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import com.xhxj.web.AnalysisUrl;
import com.xhxj.web.WebMagic;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.List;
import java.util.Set;

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

    @Autowired
    WebMagic webMagic;

    @Test
    public void TestQ509(){
        //查询sql中没有下载成功的509数据
        List<String> eheitaiDetailPage = eheitaiDetailPageDao.findByImgUrl509();

        //这些连接是需要重新下载的

        webMagic.httpweb(eheitaiDetailPage);
    }

    //删除重复的509图片数据再去测试爬取
    @Test
    public void TestDelete(){
         List<Integer> imgid= eheitaiDetailPageDao.findByTest509();

         eheitaiDetailPageDao.deleteTest509(imgid);

    }

    @Autowired
     EheitaiDetailPageDao eheitaiDetailPageDao2;
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao2;
    @Test
    public void  TestDeleteGO(){

        /**
         * 别问我,我也忘了怎么写的
         */
        List<EheitaiCatalog> all = eheitaiCatalogDao2.findAll();

        for (EheitaiCatalog eheitaiCatalog : all) {
            Set<EheitaiDetailPage> eheitaiDetailPages = eheitaiCatalog.getEheitaiDetailPages();
            if (eheitaiDetailPages.size()>1){

                for (EheitaiDetailPage eheitaiDetailPage : eheitaiDetailPages) {
                    eheitaiDetailPageDao2.delete(eheitaiDetailPage);

                }
            }
        }



    }


}

