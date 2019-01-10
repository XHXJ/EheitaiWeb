package com.xhxj.dao;

import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.daomain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EheitaiDetailPageDao extends JpaRepository<EheitaiDetailPage,Integer> , JpaSpecificationExecutor<EheitaiDetailPage> {
     List<EheitaiDetailPage> findByDivId(Integer divId);
}

