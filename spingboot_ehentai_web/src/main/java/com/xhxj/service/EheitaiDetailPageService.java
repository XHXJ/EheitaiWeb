package com.xhxj.service;

import com.xhxj.daomain.EheitaiDetailPage;

public interface EheitaiDetailPageService {

    //保存对象
    void save(EheitaiDetailPage eheitaiDetailPage);

    /**
     * 根据gid外键去保存对象
     * @param pageeheitaiDetailPage page页面传过来的参数
     * @param gid 外键
     */
    void saveEheitaiDetailPage(EheitaiDetailPage pageeheitaiDetailPage, Integer gid);


    /**
     * 查询下载失败509的数据
     * @return
     */
    EheitaiDetailPage findByImgUrl509();
}
