package com.xhxj.dao;

import com.xhxj.daomain.ErrorProxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ErrorProxyDao extends JpaRepository<ErrorProxy,Integer>{
    //更具host查询
    ErrorProxy findByHost(String s);

    /**
     * 定期删除状态为error,总报错次数不超过几次的数据
     */
    @Query(value = "DELETE FROM error_proxy WHERE state = 'error' AND counter < ?",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByStateAndCounter(Integer integer);
}
