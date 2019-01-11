package com.xhxj.web;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.service.EheitaiDetailPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

/**
 * 用来接受page传过来的对象进一步处理
 */
@Component
public class WebMagicDate implements Pipeline {

    @Autowired
    EheitaiCatalogService eheitaiCatalogService;

    @Autowired
    EheitaiDetailPageService eheitaiDetailPageService;



    @Override
    public void process(ResultItems resultItems, Task task) {
//            List<String> a = resultItems.get("jobInfo");
        //接受page传过来的对象
        EheitaiCatalog pageEheitaiCatalog = resultItems.get("eheitaiCatalog");
        EheitaiDetailPage pageeheitaiDetailPage = resultItems.get("eheitaiDetailPage");
        //这里接收到的是gid
        String complete = resultItems.get("complete");
        if (StringUtils.isNotEmpty(complete)){
            //作品爬取完毕,通知下载器下载
            System.out.println("@@@@@@@@@@@@@@@@@@作品:"+complete+"爬取完毕通知下载");

        }
        //接收eheitaiCatalog参数,否则放过
        if (pageEheitaiCatalog != null) {

            //更新作品的详细信息

//            System.out.println("eheitaiCatalog="+eheitaiCatalog);

            //查询从网页获取到的数据,sql中是否有这个对象
            Integer gid = pageEheitaiCatalog.getGid();
            List<EheitaiCatalog> byGid = eheitaiCatalogService.findByGid(gid);
            if (byGid.size() != 0) {
                //操作作品对象
                EheitaiCatalog eheitaiCatalog = byGid.get(0);
                //要没有参数才去更新
                if (eheitaiCatalog.getToken().equals("")||eheitaiCatalog.getToken()==null){
                    //更新数据
                    System.out.println("page更新了数据库中的数据");
                    eheitaiCatalogService.updatePageEheitaiCatalog(pageEheitaiCatalog);
                }

            } else {
                System.out.println("从网页中获取到的数据,在sql中不存在,一般不会出现,出现就可能是爬了不该爬的数据,或者之前sql中的数据不完整");
            }
        } else {
            //图片下载地址
            if (pageeheitaiDetailPage!=null){
                //如果传入的是pageeheitaiDetailPage对象
                Integer gid = resultItems.get("gid");

                //保存下载连接
                eheitaiDetailPageService.saveEheitaiDetailPage(pageeheitaiDetailPage,gid);
                System.out.println("在page获取到的为:"+pageeheitaiDetailPage+"\ngid="+gid);


            }
            System.out.println("页面没有传page参数过来");
        }
    }
}
