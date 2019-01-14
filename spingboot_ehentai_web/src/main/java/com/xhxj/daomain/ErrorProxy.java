package com.xhxj.daomain;

import lombok.Data;
import us.codecraft.webmagic.proxy.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class ErrorProxy {
    /**
     * 接受错误的id对象
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //ip
    private String host;
    //端口号
    private int port;
    //返回的文本
    private String txt;
    //错误时间
    private Date Date;
    //错误统计
    private Integer counter;
    //搞个状态
    private String state;

}
