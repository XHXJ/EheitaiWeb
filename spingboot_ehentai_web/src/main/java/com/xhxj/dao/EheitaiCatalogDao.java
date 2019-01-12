package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EheitaiCatalogDao extends JpaRepository<EheitaiCatalog,Integer>, JpaSpecificationExecutor<EheitaiCatalog> {

    //根据gid查询对象
    List<EheitaiCatalog> findByGid(Integer divId);


}

