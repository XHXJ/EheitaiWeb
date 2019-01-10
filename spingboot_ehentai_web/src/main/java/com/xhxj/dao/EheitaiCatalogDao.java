package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EheitaiCatalogDao extends JpaRepository<EheitaiCatalog,Integer> {
     List<EheitaiCatalog> findByDivId(Integer divId);
}

