package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EheitaiDetailPageDao extends JpaRepository<EheitaiDetailPage, Integer>, JpaSpecificationExecutor<EheitaiDetailPage> {
    @Query(value = "SELECT * FROM eheitai_detail_page WHERE eheitai_id = ? AND page = ?",nativeQuery =true)
    List<EheitaiDetailPage> findByImgUrlAndPage(Integer gid, Integer page);
}

