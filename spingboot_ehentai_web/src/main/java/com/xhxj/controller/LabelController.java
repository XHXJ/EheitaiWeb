package com.xhxj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xhxj.utils.GetProxy;
import com.xhxj.utils.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.proxy.Proxy;

import javax.xml.transform.Result;
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
    public String getProxy(){
        //把获取到的连接池对象转成json给页面


        List<Proxy> httpProxyDao = getProxy.getHttpProxyDao();


        String string = JSONArray.toJSONString(httpProxyDao);

        System.out.println(string);


        return string;
    }

}
