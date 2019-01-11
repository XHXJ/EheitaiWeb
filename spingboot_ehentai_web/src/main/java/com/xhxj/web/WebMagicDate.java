package com.xhxj.web;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
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

        @Override
        public void process(ResultItems resultItems, Task task) {
//            List<String> a = resultItems.get("jobInfo");
            //接受page传过来的对象
            EheitaiCatalog pageEheitaiCatalog = resultItems.get("eheitaiCatalog");
            System.out.println("///////////////////////////////////////////////////////");
            //接收eheitaiCatalog参数,否则放过
            if (pageEheitaiCatalog!=null){


//            System.out.println("eheitaiCatalog="+eheitaiCatalog);

            //查询从网页获取到的数据,sql中是否有这个对象
            Integer gid = pageEheitaiCatalog.getGid();
//            List<EheitaiCatalog> byGid =
                List<EheitaiCatalog> byGid = eheitaiCatalogService.findByGid(gid);
            if (byGid.size()!=0){
                //操作作品对象
                EheitaiCatalog eheitaiCatalog = byGid.get(0);
                System.out.println("---------------"+eheitaiCatalog);
                System.out.println("+++++++++++++++"+pageEheitaiCatalog);

                //更新数据
                eheitaiCatalogService.updatePageEheitaiCatalog(pageEheitaiCatalog);

            }else {
                System.out.println("从网页中获取到的数据,在sql中不存在,一般不会出现,出现就可能是爬了不该爬的数据,或者之前sql中的数据不完整");
            }
            }else {
                System.out.println("页面没有传page参数过来");
            }
        }
}
