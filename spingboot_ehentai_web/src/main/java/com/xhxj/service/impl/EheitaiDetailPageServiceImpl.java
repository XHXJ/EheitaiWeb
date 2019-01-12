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
     * 并判断是否重复
     *
     * @param pageeheitaiDetailPage page页面传过来的参数
     * @param gid                   外键
     */
    @Override
    public void saveEheitaiDetailPage(EheitaiDetailPage pageeheitaiDetailPage, Integer gid) {

        Integer page = pageeheitaiDetailPage.getPage();
        //先去查询有没有这个页数了不然不保存
        List<EheitaiDetailPage> byImgUrlAndPagego = eheitaiDetailPageDao.findByImgUrlAndPage(gid, page);

        EheitaiDetailPage byImgUrlAndPage = byImgUrlAndPagego.get(0);


        if (byImgUrlAndPage==null){

            //保存图片
            saveImg(byImgUrlAndPage, gid);
        }else {
            //覆盖原来的,大概是这样吧
            byImgUrlAndPage.setPage(pageeheitaiDetailPage.getPage());
            byImgUrlAndPage.setUrl(pageeheitaiDetailPage.getUrl());
            byImgUrlAndPage.setFileLog(pageeheitaiDetailPage.getFileLog());
            byImgUrlAndPage.setResolution(pageeheitaiDetailPage.getResolution());
            byImgUrlAndPage.setFileSize(pageeheitaiDetailPage.getFileSize());
            byImgUrlAndPage.setImgUrl(pageeheitaiDetailPage.getImgUrl());

            eheitaiDetailPageDao.save(byImgUrlAndPage);
        }

    }


    /**
     * 保存图片
     *
     * @param pageeheitaiDetailPage 新的图片地址
     * @param gid                   更具主id查询
     */
    private void saveImg(EheitaiDetailPage pageeheitaiDetailPage, Integer gid) {
        //新建图片保存

        List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(gid);
        EheitaiCatalog eheitaiCatalog = byGid.get(0);


        eheitaiCatalog.getEheitaiDetailPages().add(pageeheitaiDetailPage);

        //保存
        eheitaiCatalogDao.save(eheitaiCatalog);
    }

    @Override
    public List<String> findByImgUrl509() {

        return eheitaiDetailPageDao.findByImgUrl509();
    }

    /**
     * 删除重复的数据,删除正确的数据,留下错误的做测试
     *
     * @return
     */
    @Override
    public List<Integer> findByTest509() {
        return eheitaiDetailPageDao.findByTest509();
    }

    /**
     * 删除重复的509数据
     *
     * @param imgid 传入到的要删除的图片数据id
     */
    @Override
    public void deleteTest509(List<Integer> imgid) {

        System.out.println(imgid);
        for (Integer integer : imgid) {
            EheitaiDetailPage eheitaiDetailPage = new EheitaiDetailPage();
            eheitaiDetailPage.setId(integer);
            eheitaiDetailPageDao.delete(eheitaiDetailPage);
        }

    }



    @Override
    public void deleteTest509Demo01(Integer id) {

        eheitaiDetailPageDao.deleteById(id);
    }
}
