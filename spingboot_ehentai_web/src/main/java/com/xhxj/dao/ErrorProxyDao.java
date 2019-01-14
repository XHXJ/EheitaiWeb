package com.xhxj.dao;

import com.xhxj.daomain.ErrorProxy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorProxyDao extends JpaRepository<ErrorProxy,Integer>{
    //更具host查询
    ErrorProxy findByHost(String s);


}
