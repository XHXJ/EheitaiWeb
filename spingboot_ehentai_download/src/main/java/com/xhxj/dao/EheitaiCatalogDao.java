package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EheitaiCatalogDao extends JpaRepository<EheitaiCatalog,Integer>, JpaSpecificationExecutor<EheitaiCatalog> {

    //根据gid查询对象
    List<EheitaiCatalog> findByGid(Integer divId);


    //查询全部的url
    @Query(value = "SELECT url FROM eheitai_catalog",nativeQuery = true)
    List<String> findByUrl();

    //根据传入的url去查询作品
    @Query(value = "SELECT * FROM eheitai_catalog WHERE url = ? ",nativeQuery = true)
    EheitaiCatalog findByUrl(String s);


    //查看作品页面总和
    @Query(value = "SELECT COUNT(page)  FROM eheitai_detail_page",nativeQuery = true)
    Integer findByUrlCountPage();


    //查询指定id下的总页数
    @Query(value = "SELECT SUM(eheitai_catalog.length) FROM eheitai_catalog WHERE gid = ? ",nativeQuery = true)
    Integer findByGidWhereLanguage(Integer integer);

    //查询全部的gid
    @Query(value = "SELECT gid FROM eheitai_catalog",nativeQuery = true)
    List<Integer> findGidAll();


    //根据完成情况查询作品
    List<EheitaiCatalog> findByComplete(Integer integer);


    //根据作品gid去返回作品记录的总页数
    @Query(value = "SELECT length FROM eheitai_catalog WHERE gid = ?" ,nativeQuery = true)
    Integer findByGidGetLength(Integer gid);

    //根据作品的gid去查询作品的名字
    @Query(value = "SELECT title FROM eheitai_catalog WHERE gid = ?",nativeQuery = true)
    String findGetName(Integer i);
}

