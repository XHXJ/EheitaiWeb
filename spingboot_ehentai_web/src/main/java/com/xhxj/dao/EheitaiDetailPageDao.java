package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EheitaiDetailPageDao extends JpaRepository<EheitaiDetailPage, Integer>, JpaSpecificationExecutor<EheitaiDetailPage> {
}

