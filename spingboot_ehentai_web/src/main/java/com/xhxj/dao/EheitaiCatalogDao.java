package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EheitaiCatalogDao extends JpaRepository<EheitaiCatalog,Integer>, JpaSpecificationExecutor<EheitaiCatalog> {
     List<EheitaiCatalog> findByGid(Integer divId);


}

