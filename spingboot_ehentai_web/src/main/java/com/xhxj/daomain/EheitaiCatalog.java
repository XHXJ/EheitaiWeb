package com.xhxj.daomain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@ToString(exclude = {"eheitaiDetailPages"})
public class EheitaiCatalog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String url;
    private String imgUrl;

    @Column(name = "div_id")
    private Integer divId;
    private Integer complete = 0;

    @OneToMany(targetEntity = EheitaiDetailPage.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "eheitai_id", referencedColumnName = "div_id")
    private Set<EheitaiDetailPage> eheitaiDetailPages = new HashSet<EheitaiDetailPage>();

}
