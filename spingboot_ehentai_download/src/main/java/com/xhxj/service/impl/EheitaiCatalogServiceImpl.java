package com.xhxj.service.impl;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.util.List;

@Service
@Transactional
public class EheitaiCatalogServiceImpl implements EheitaiCatalogService {
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;
    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;


    /**
     * 保存对象
     *
     * @param eheitaiCatalog 作品对象
     */
    @Override
    public void save(EheitaiCatalog eheitaiCatalog) {
        eheitaiCatalogDao.save(eheitaiCatalog);
    }

    /**
     * 根据gid去查询对象
     *
     * @param gid 作品id
     * @return
     */
    public List<EheitaiCatalog> findByGid(Integer gid) {
        return eheitaiCatalogDao.findByGid(gid);
    }



    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<EheitaiCatalog> findAll() {
        return eheitaiCatalogDao.findAll();
    }



    /**
     * 查询全部的作品url
     *
     * @return
     */
    @Override
    public List<String> findByUrl() {

        return eheitaiCatalogDao.findByUrl();
    }

    /**
     * 根据作品url连接去查询作品
     *
     * @param s 作品的url连接
     * @return
     */
    @Override
    public EheitaiCatalog findByUrl(String s) {

        return eheitaiCatalogDao.findByUrl(s);
    }

    /**
     * 查看作品页面总和
     *
     * @return
     */
    @Override
    public Integer findByUrlCountPage() {
        return eheitaiCatalogDao.findByUrlCountPage();
    }

    /**
     * 只查询指定下载状态的作品
     *
     * @return
     */
    public List<EheitaiCatalog> findByComplete(Integer integer) {
        return eheitaiCatalogDao.findByComplete(integer);
    }

    @Override
    public String findGetName(Integer i) {
        return eheitaiCatalogDao.findGetName(i);
    }


}
