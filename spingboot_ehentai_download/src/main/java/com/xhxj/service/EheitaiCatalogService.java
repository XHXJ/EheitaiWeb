package com.xhxj.service;

import com.xhxj.daomain.EheitaiCatalog;

import java.util.List;

public interface EheitaiCatalogService {
    //保存
    public void save(EheitaiCatalog eheitaiCatalog);


    /**
     * 查询全部
     * @return
     */
    List<EheitaiCatalog> findAll();



    List<String> findByUrl();

    /**
     * 根据url查询作品
     * @param s 作品的url连接
     */
    EheitaiCatalog findByUrl(String s);


    //查看作品页面加起来的总和
    Integer findByUrlCountPage();

    //只查询指定下载状态的作品
    List<EheitaiCatalog> findByComplete(Integer integer);

    //根据gid查询作品的名字
    String findGetName(Integer i);
}
