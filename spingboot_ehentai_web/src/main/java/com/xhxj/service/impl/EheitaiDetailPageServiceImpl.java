package com.xhxj.service.impl;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiDetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EheitaiDetailPageServiceImpl implements EheitaiDetailPageService {

    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;

    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;

    /**
     * 保存对象
     *
     * @param eheitaiDetailPage 下载详情页面
     */
    @Override
    public void save(EheitaiDetailPage eheitaiDetailPage) {
        eheitaiDetailPageDao.save(eheitaiDetailPage);
    }

    /**
     * 根据gid外键去保存对象
     *
     * @param pageeheitaiDetailPage page页面传过来的参数
     * @param gid                   外键
     */
    @Override
    public void saveEheitaiDetailPage(EheitaiDetailPage pageeheitaiDetailPage, Integer gid) {

        Integer page = pageeheitaiDetailPage.getPage();
        //先去查询有没有这个页数了不然不保存
        List<EheitaiDetailPage> byImgUrlAndPage = eheitaiDetailPageDao.findByImgUrlAndPage(gid, page);

        if (byImgUrlAndPage.size() == 0) {
            //如果是空才去保存,不重复抓取数据

            List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(gid);
            EheitaiCatalog eheitaiCatalog = byGid.get(0);


            eheitaiCatalog.getEheitaiDetailPages().add(pageeheitaiDetailPage);

            //保存
            eheitaiCatalogDao.save(eheitaiCatalog);
        }
//        else {
//            System.out.println("已有爬取的下载页面存在");
//        }

    }
}
