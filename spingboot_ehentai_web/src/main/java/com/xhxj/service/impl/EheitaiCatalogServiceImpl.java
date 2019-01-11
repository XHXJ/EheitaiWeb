package com.xhxj.service.impl;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EheitaiCatalogServiceImpl implements EheitaiCatalogService {
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;


    /**
     * 保存对象
     * @param eheitaiCatalog 作品对象
     */
    @Override
    public void save(EheitaiCatalog eheitaiCatalog) {
        eheitaiCatalogDao.save(eheitaiCatalog);
    }

    /**
     * 根据gid去查询对象
     * @param gid 作品id
     * @return
     */
    @Override
    public List<EheitaiCatalog> findByGid(Integer gid) {
        return eheitaiCatalogDao.findByGid(gid);
    }
}
