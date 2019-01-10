package com.xhxj.dao;

import com.xhxj.daomain.EheitaiDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EheitaiDetailPageDao extends JpaRepository<EheitaiDetailPage,Integer> {
     List<EheitaiDetailPage> findByDivId(Integer divId);
}

