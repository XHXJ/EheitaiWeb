package com.xhxj.dao;

import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EheitaiCatalogDao extends JpaRepository<EheitaiCatalog,Integer>, JpaSpecificationExecutor<EheitaiCatalog> {
     List<EheitaiCatalog> findByGid(Integer divId);

//查询509的图片
    @Query(value = "SELECT count(*) FROM eheitai_detail_page WHERE eheitai_detail_page.img_url = 'https://exhentai.org/img/509.gif'",nativeQuery = true)
    EheitaiDetailPage findByImgUrl509();
}

