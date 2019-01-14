package com.xhxj.service;

import com.xhxj.daomain.ErrorProxy;

public interface ErrorProxyService {
    //保存
    void save(ErrorProxy errorProxy);

    //更具ip地址去查询
    ErrorProxy finByHost(String s);


    /**
     * 定期删除状态为error,总报错次数不超过几次的数据
     * @param i
     */
    void deleteByStateAndCounter(int i);
}
