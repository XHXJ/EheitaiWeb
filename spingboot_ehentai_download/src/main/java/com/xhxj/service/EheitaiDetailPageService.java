package com.xhxj.service;

import com.xhxj.daomain.EheitaiDetailPage;

import java.util.List;

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
    List<String> findByImgUrl509();


    /**
     * 删除重复的数据,删除正确的数据,留下错误的做测试
     * @return
     */
    List<Integer> findByTest509();

    //批量删除
    void deleteTest509(List<Integer> imgid);


    //根据一个id删除
    void deleteTest509Demo01(Integer id);

    /**
     * 查询全部的图片获取地址
     * @return
     */

    List<String> findByUrl();


    /**
     * 根据url去查询
     * @param url 传入的url
     * @return
     */
    String findByUrl(String url);

    /**
     * #查询指定作品的page表下全部页面的总和
     * @param id
     * @return
     */
    Integer findByUrlCount(Integer id);

    //查询当前所有页数的总和
    Integer findByUrlCountPage();

    /**
     * 查询排除已完成的数据之后的全部url
     * @return
     */
    List<String> findByUrlComplete();


    /**
     * 根据作品连接去查询下载连接
     * @param gid 作品id
     * @return
     */
    List<String> findByGidAndImgurl(Integer gid);

    /**
     *根据作品连接去查询下载连接和文件名称
     * @param gid 作品id
     * @return
     */
    List<EheitaiDetailPage> findByGidAndImgurlGetName(Integer gid);

    List<EheitaiDetailPage> findByGid(Integer i);
}
