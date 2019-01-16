package com.xhxj.service.impl;

import com.xhxj.controller.ActiveMqQueueProduce;
import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiDetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EheitaiDetailPageServiceImpl implements EheitaiDetailPageService {

    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;

    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;

    @Autowired
    ActiveMqQueueProduce activeMqQueueProduce;

    /**
     * 保存对象
     *
     * @param eheitaiDetailPage 下载详情页面
     */
    @Override
    public void save(EheitaiDetailPage eheitaiDetailPage) {
        eheitaiDetailPageDao.save(eheitaiDetailPage);
    }

    /**
     * 根据gid外键去保存对象
     * 并判断是否重复
     *
     * @param pageeheitaiDetailPage page页面传过来的参数
     * @param gid                   外键
     */
    @Override
    public void saveEheitaiDetailPage(EheitaiDetailPage pageeheitaiDetailPage, Integer gid) {

        if (!pageeheitaiDetailPage.getImgUrl().equals("https://exhentai.org/img/509.gif")) {


            Integer page = pageeheitaiDetailPage.getPage();
            //先去查询有没有这个页数了不然不保存
            List<EheitaiDetailPage> byImgUrlAndPagego = eheitaiDetailPageDao.findByImgUrlAndPage(gid, page);


            //这是查出来的数据
//        EheitaiDetailPage byImgUrlAndPage = new EheitaiDetailPage();


            if (byImgUrlAndPagego.size() == 0) {
                //保存图片
                saveImg(pageeheitaiDetailPage, gid);
            } else {
                System.out.println("图片下载页面爬取到重复数据!注意优化逻辑");
            }

        } else {
            System.out.println("警告!爬虫被封!返回地址为:https://exhentai.org/img/509.gif");
        }

    }


    /**
     * 保存图片
     *
     * @param pageeheitaiDetailPage 新的图片地址
     * @param gid                   更具主id查询
     */
    private void saveImg(EheitaiDetailPage pageeheitaiDetailPage, Integer gid) {
        //新建图片保存

        List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(gid);
        if (byGid.size() == 1) {


            EheitaiCatalog eheitaiCatalog = byGid.get(0);


            //设置为爬取成功
            pageeheitaiDetailPage.setCrawlComplete(1);

            eheitaiCatalog.getEheitaiDetailPages().add(pageeheitaiDetailPage);

            //保存
            //是时候重写保存语句了....
            eheitaiCatalogDao.save(eheitaiCatalog);

            try {
                activeMqQueueProduce.postMessage(eheitaiCatalog.getGid()+"@"+pageeheitaiDetailPage.getImgUrl()+"@"+pageeheitaiDetailPage.getFileLog());

            } catch (JMSException e) {
                System.out.println("发送消息失败,请检查ActiveMQ是否启动");
                e.printStackTrace();
            }


        } else {
            System.out.println(byGid);
            System.out.println("为什么没有数据,或者不是一个");
        }
    }

    @Override
    public List<String> findByImgUrl509() {

        return eheitaiDetailPageDao.findByImgUrl509();
    }

    /**
     * 删除重复的数据,删除正确的数据,留下错误的做测试
     *
     * @return
     */
    @Override
    public List<Integer> findByTest509() {
        return eheitaiDetailPageDao.findByTest509();
    }

    /**
     * 删除重复的509数据
     *
     * @param imgid 传入到的要删除的图片数据id
     */
    @Override
    public void deleteTest509(List<Integer> imgid) {

        System.out.println(imgid);
        for (Integer integer : imgid) {
            EheitaiDetailPage eheitaiDetailPage = new EheitaiDetailPage();
            eheitaiDetailPage.setId(integer);
            eheitaiDetailPageDao.delete(eheitaiDetailPage);
        }

    }


    @Override
    public void deleteTest509Demo01(Integer id) {

        eheitaiDetailPageDao.deleteById(id);
    }

    /**
     * 查询全部的图片url
     *
     * @return
     */
    @Override
    public List<String> findByUrl() {

        return eheitaiDetailPageDao.findByUrl();
    }

    /**
     * 根据传入的url去查询数据
     *
     * @param url 传入的url
     * @return
     */
    @Override
    public String findByUrl(String url) {
        return eheitaiDetailPageDao.findByUrl(url);
    }

    /**
     * #查询指定作品的page表下全部页面的总和
     *
     * @param id
     * @return
     */
    @Override
    public Integer findByUrlCount(Integer id) {
        return eheitaiDetailPageDao.findByUrlCount(id);
    }

    /**
     * 查询当前页数的总和
     *
     * @return
     */
    @Override
    public Integer findByUrlCountPage() {
        return eheitaiDetailPageDao.findByUrlCountPage();
    }

    @Override
    public List<String> findByUrlComplete() {
        List<String> byGidAndUrl = new ArrayList<>();
        //根据完成状态查询作品
        List<EheitaiCatalog> eheitaiCatalog = eheitaiCatalogDao.findByComplete(0);
        for (EheitaiCatalog catalog : eheitaiCatalog) {

            Integer gid = catalog.getGid();

            byGidAndUrl.addAll(eheitaiDetailPageDao.findByGidAndUrl(gid)) ;
        }

        return byGidAndUrl;
    }
}
