package com.xhxj.spingboot_ehentai_web;

import com.xhxj.service.AnalysisUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpingbootEhentaiWebApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    AnalysisUrl analysisUrl;


    @Test
    public void Test01(){
        //抓取页面
        analysisUrl.getHttp();
        //获取解析结果存入sql
        analysisUrl.analysisHtml();
        //下载页面

    }


}

