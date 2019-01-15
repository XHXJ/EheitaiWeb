package com.xhxj.service.impl;

import com.xhxj.dao.ErrorProxyDao;
import com.xhxj.daomain.ErrorProxy;
import com.xhxj.service.ErrorProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ErrorProxyServiceImpl  implements ErrorProxyService {

    @Autowired
    ErrorProxyDao errorProxyDao;



    @Override
    public void save(ErrorProxy errorProxy) {
        //判断一下有没有重复的重复的就更新
        ErrorProxy byHost = errorProxyDao.findByHost(errorProxy.getHost());
        if (byHost==null){
            errorProxyDao.save(errorProxy);
        }else {
            //如果有了就保存一下之前的数据
            byHost.setDate(new Date());
            byHost.setCounter(byHost.getCounter()+errorProxy.getCounter());
            byHost.setPort(errorProxy.getPort());
            byHost.setTxt(errorProxy.getTxt());
            errorProxyDao.save(byHost);
        }




    }

    @Override
    public ErrorProxy finByHost(String s) {




        return errorProxyDao.findByHost(s);
    }

    /**
     * 定期删除状态为error,总报错次数不超过几次的数据
     * @param i
     */
    @Override
    public void deleteByStateAndCounter(int i) {
        errorProxyDao.deleteByStateAndCounter(i);
    }
}
