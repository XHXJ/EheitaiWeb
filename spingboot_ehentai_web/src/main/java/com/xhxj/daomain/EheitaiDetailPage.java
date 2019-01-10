package com.xhxj.daomain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class EheitaiDetailPage {
    @Id
    @Column(name = "id")
    private Integer id;
    private String url;
    private Integer page;
    private String pageCount;
    private Data Posted;
    private String Language;

    @ManyToOne(targetEntity = EheitaiCatalog.class)
    @JoinColumn(name =  "divId", referencedColumnName = "id")
    private EheitaiCatalog eheitaiCatalog;
}
