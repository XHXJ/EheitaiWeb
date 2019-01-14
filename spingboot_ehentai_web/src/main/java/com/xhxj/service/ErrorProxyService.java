package com.xhxj.service;

import com.xhxj.daomain.ErrorProxy;

public interface ErrorProxyService {
    //保存
    void save(ErrorProxy errorProxy);

    //更具ip地址去查询
    ErrorProxy finByHost(String s);


}
