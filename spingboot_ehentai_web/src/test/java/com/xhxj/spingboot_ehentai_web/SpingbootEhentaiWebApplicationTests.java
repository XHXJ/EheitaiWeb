package com.xhxj.spingboot_ehentai_web;

import com.alibaba.fastjson.JSON;
import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.daomain.Proxies;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import com.xhxj.web.AnalysisUrl;
import com.xhxj.web.WebMagic;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import sun.net.www.http.HttpClient;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@PropertySource("classpath:Configuration.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpingbootEhentaiWebApplicationTests {

    @Autowired
    EheitaiDetailPageService eheitaiDetailPageDao;
    @Autowired
    EheitaiCatalogService eheitaiCatalogDao;

    @Test
    public void testdemo() {
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
    public void Test01() {
        analysisUrl.getHttp();
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

    }

    @Autowired
    WebMagic webMagic;

    @Test
    public void TestStart() {
        //把sql中没有爬的连接全部丢给爬虫
        //这里以后要改要有条件的查询

        List<EheitaiCatalog> all = eheitaiCatalogDao.findAll();
        List<String> urlall = new ArrayList<>();
        //如果sql中有数据就去爬
        if (all.size() != 0) {
            for (EheitaiCatalog eheitaiCatalog : all) {
                urlall.add(eheitaiCatalog.getUrl());
            }
            String[] urllist = urlall.toArray(new String[urlall.size()]);

            //获取代理对象
            Proxies httpProxy = getHttpProxy();
            //开始爬取
            webMagic.httpweb(urlall,httpProxy);


        }
    }

    @Value("${url}")
    private String url;




    /**
     * 获取网站上的代理服务器地址
     * @return
     */
    private Proxies getHttpProxy() {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet对象，设置url访问地址
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;

        Proxies proxies = null;
        try {
            //使用HttpClient发起请求，获取response
            response = httpClient.execute(httpGet);

            //解析响应
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "utf8");


                proxies  = JSON.parseObject(content, Proxies.class);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭response

            try {
                response.close();
                return  proxies;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }


    @Test
    public void TestQ509() {
        //查询sql中没有下载成功的509数据
        List<String> eheitaiDetailPage = eheitaiDetailPageDao.findByImgUrl509();

        //这些连接是需要重新下载的

        webMagic.httpweb(eheitaiDetailPage,getHttpProxy());
    }

    //删除重复的509图片数据再去测试爬取
    @Test
    public void TestDelete() {
        List<Integer> imgid = eheitaiDetailPageDao.findByTest509();

        eheitaiDetailPageDao.deleteTest509(imgid);

    }

    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao2;
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao2;

    @Test
    public void TestDeleteGO() {

        /**
         * 别问我,我也忘了怎么写的
         */
        List<EheitaiCatalog> all = eheitaiCatalogDao2.findAll();

        for (EheitaiCatalog eheitaiCatalog : all) {
            Set<EheitaiDetailPage> eheitaiDetailPages = eheitaiCatalog.getEheitaiDetailPages();
            if (eheitaiDetailPages.size() > 1) {

                for (EheitaiDetailPage eheitaiDetailPage : eheitaiDetailPages) {
                    eheitaiDetailPageDao2.delete(eheitaiDetailPage);

                }
            }
        }


    }

    @Test
    public void TestLength() {
        String lengTh = "Your IP address has been temporarily banned for excessive pageloads which indicates that you are using automated mirroring/harvesting software. The ban expires in 8 hours and 9 minutes";

        System.out.println(lengTh.length());


    }


}

