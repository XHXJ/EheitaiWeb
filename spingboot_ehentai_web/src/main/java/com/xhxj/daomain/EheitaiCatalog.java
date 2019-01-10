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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //标题
    private String title;
    //作品连接
    private String url;
    //预览图标
    private String imgUrl;
    //时间转换
    private Date Posted;
    //总页数
    private String Language;
    //文件大小
    private String FileSize;
    //

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
    private Integer complete = 0;


//外键
    @Column(name = "div_id")
    private Integer divId;


    @OneToMany(targetEntity = EheitaiDetailPage.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "eheitai_id", referencedColumnName = "div_id")
    private Set<EheitaiDetailPage> eheitaiDetailPages = new HashSet<EheitaiDetailPage>();

}
