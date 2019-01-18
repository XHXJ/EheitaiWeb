package com.xhxj.controller;

import com.xhxj.utils.GetProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.proxy.Proxy;

import java.util.List;

/**
 * 这个类用来处理web类的访问
 */
@RestController
@RequestMapping("/proxy")
public class LabelController {

    @Autowired
    GetProxy getProxy;

    @RequestMapping(method = RequestMethod.GET)
    public List<Proxy> getProxy(){
        //把获取到的连接池对象转成json给页面
        return getProxy.getHttpProxyDao();
    }

}
