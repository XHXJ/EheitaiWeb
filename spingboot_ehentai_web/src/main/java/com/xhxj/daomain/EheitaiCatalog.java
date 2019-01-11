package com.xhxj.daomain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@ToString(exclude = {"eheitaiDetailPages"})
public class EheitaiCatalog implements Serializable {
    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    //标题
//    @Column(name = "title",nullable = false, length = 100)
//    private String title = "";
//    //作品连接
//    @Column(nullable = false)
//    private String url = "";
//    //预览图标
//    @Column(nullable = false)
//    private String imgUrl = "";
//    //时间转换
//    @Column(nullable = false)
//    private Date PostedDate = new Date(0);
//    //总页数
//    @Column(nullable = false)
//    private Integer Length = 0;
//    //作品语言
//    @Column(name = "language",nullable = false, length = 20)
//    private String Language = "";
//    //文件大小
//    @Column(name = "file_size",nullable = false, length = 20)
//    private String FileSize = "";
//    //他的父对象,我不知道有啥用,应该是个历史记录的id
//    @Column(nullable = false)
//    private Integer Parent = 0;
//    //这个地址和id相加就是网站链接
//    @Column(nullable = false)
//    private String token = "";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //标题
    private String title ;
    //作品连接
    private String url ;
    //预览图标
    private String imgUrl ;
    //时间转换
    private Date PostedDate ;
    //总页数
    private Integer Length ;
    //作品语言
    private String Language ;
    //文件大小
    private String FileSize ;
    //他的父对象,我不知道有啥用,应该是个历史记录的id
    private Integer Parent ;
    //这个地址和id相加就是网站链接
    private String token ;

    /**
     * Posted:	2018-12-13 10:03 时间
     * Parent:	1328175 作品编号
     * Visible:	No (Replaced)
     * Language:	Chinese  TR 语言
     * File Size:	113.9 MB 大小
     * Length:	46 pages 总页数
     * Favorited:	1445 times
     */
    //是否成功下载
    @Column(nullable = false)
    private Integer complete = 0;


    //外键,也是g站整个作品的id
    @Column(name = "gid", nullable = false)
    private Integer gid;


    @OneToMany(targetEntity = EheitaiDetailPage.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "eheitai_id", referencedColumnName = "gid")
    private Set<EheitaiDetailPage> eheitaiDetailPages = new HashSet<EheitaiDetailPage>();

}
