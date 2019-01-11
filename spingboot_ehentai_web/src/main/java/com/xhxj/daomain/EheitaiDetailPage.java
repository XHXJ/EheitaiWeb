package com.xhxj.daomain;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@ToString(exclude = {"eheitaiCatalog"})
@EqualsAndHashCode(exclude = {"eheitaiCatalog"})
public class EheitaiDetailPage  implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String language;
  private String posted;
  private Integer page;
  private String pageCount;
  private String url;

  @ManyToOne(targetEntity = EheitaiCatalog.class)
  @JoinColumn(name = "eheitai_id", referencedColumnName = "gid")
  private EheitaiCatalog eheitaiCatalog;



}
