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
        EheitaiDetailPage byImgUrlAndPage = eheitaiDetailPageDao.findByImgUrlAndPage(gid, page);

        //如果之前都没有这if (byImgUrlAndPage == null||!byImgUrlAndPage.getImgUrl().equals("https://exhentai.org/img/509.gif")) {
        //保存图片
        saveImg(pageeheitaiDetailPage, gid);

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
}
