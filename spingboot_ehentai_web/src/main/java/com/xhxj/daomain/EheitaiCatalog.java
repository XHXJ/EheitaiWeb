package com.xhxj.daomain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "EheitaiCatalog")
public class EheitaiCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String url;
    private String imgUrl;

   // @Column(name = "div_Id")
    private Integer divId;
    private Integer complete = 0;

//    @OneToMany(targetEntity = EheitaiDetailPage.class)
//    @JoinColumn(name = "div_Id", referencedColumnName = "id")
//    Set<EheitaiDetailPage> eheitaiDetailPages = new HashSet<EheitaiDetailPage>();

}
