package com.xhxj.daomain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@ToString(exclude = {"eheitaiCatalog"})
@EqualsAndHashCode(exclude = {"eheitaiCatalog"})
public class EheitaiDetailPage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //当前页数
    private Integer page;
    //当前页面地址
    private String url;
    //文件信息
    private String FileLog;
    //图片分辨率
    private String Resolution;
    //图片大小
    private String FileSize;
    //设置当前图片下载地址
    private String ImgUrl;
    //爬取成功
    @Column(nullable = false)
    private Integer CrawlComplete = 0;

    //是否成功下载
    @Column(nullable = false)
    private Integer complete = 0;


    @ManyToOne(targetEntity = EheitaiCatalog.class)
    @JoinColumn(name = "eheitai_id", referencedColumnName = "gid")
    private EheitaiCatalog eheitaiCatalog;


}
