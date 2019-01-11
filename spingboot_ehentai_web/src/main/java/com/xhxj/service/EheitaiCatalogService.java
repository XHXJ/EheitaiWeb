package com.xhxj.service;

import com.xhxj.daomain.EheitaiCatalog;

import java.util.List;

public interface EheitaiCatalogService {
    //保存
    public void save(EheitaiCatalog eheitaiCatalog);

    //更具gid去查询返回对象
    public List<EheitaiCatalog> findByGid(Integer gid);
}
