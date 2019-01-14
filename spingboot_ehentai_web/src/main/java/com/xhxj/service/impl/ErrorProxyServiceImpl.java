package com.xhxj.service.impl;

import com.xhxj.dao.ErrorProxyDao;
import com.xhxj.daomain.ErrorProxy;
import com.xhxj.service.ErrorProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorProxyServiceImpl  implements ErrorProxyService {

    @Autowired
    ErrorProxyDao errorProxyDao;

    @Override
    public void save(ErrorProxy errorProxy) {
        errorProxyDao.save(errorProxy);
    }

    @Override
    public ErrorProxy finByHost(String s) {
        return errorProxyDao.findByHost(s);
    }
}
