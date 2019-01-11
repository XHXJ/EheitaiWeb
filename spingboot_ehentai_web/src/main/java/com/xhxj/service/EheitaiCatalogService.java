package com.xhxj.service;

import com.xhxj.daomain.EheitaiCatalog;

import java.util.List;

public interface EheitaiCatalogService {
    //保存
    public void save(EheitaiCatalog eheitaiCatalog);

    //更新gid去查询返回对象
    public List<EheitaiCatalog> findByGid(Integer gid);

    /**
     * 更新旧的EheitaiCatalog为新的page传来的数据
     * @param pageEheitaiCatalog 解析页面page获取到的数据
     */
    void updatePageEheitaiCatalog(EheitaiCatalog pageEheitaiCatalog);

    /**
     * 查询全部
     * @return
     */
    List<EheitaiCatalog> findAll();
}
