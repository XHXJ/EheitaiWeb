package com.xhxj.service.impl;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.controller.ActiveMqQueueProduce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.util.List;

@Service
@Transactional
public class EheitaiCatalogServiceImpl implements EheitaiCatalogService {
    @Autowired
    EheitaiCatalogDao eheitaiCatalogDao;
    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;
    @Autowired
    ActiveMqQueueProduce activeMqQueueProduce;


    /**
     * 保存对象
     *
     * @param eheitaiCatalog 作品对象
     */
    @Override
    public void save(EheitaiCatalog eheitaiCatalog) {
        eheitaiCatalogDao.save(eheitaiCatalog);
    }

    /**
     * 根据gid去查询对象
     *
     * @param gid 作品id
     * @return
     */
    @Override
    public List<EheitaiCatalog> findByGid(Integer gid) {
        return eheitaiCatalogDao.findByGid(gid);
    }

    /**
     * 更新旧的数据
     *
     * @param pageEheitaiCatalog 解析页面page获取到的数据
     */
    @Override
    public void updatePageEheitaiCatalog(EheitaiCatalog pageEheitaiCatalog) {

        List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(pageEheitaiCatalog.getGid());
        if (byGid.size() == 1) {


            EheitaiCatalog eheitaiCatalog = byGid.get(0);

            //把新传入的数据拿出来存到旧的里面
            eheitaiCatalog.setFileSize(pageEheitaiCatalog.getFileSize());
            eheitaiCatalog.setLanguage(pageEheitaiCatalog.getLanguage());
            eheitaiCatalog.setLength(pageEheitaiCatalog.getLength());
            eheitaiCatalog.setParent(pageEheitaiCatalog.getParent());
            eheitaiCatalog.setPostedDate(pageEheitaiCatalog.getPostedDate());
            eheitaiCatalog.setToken(pageEheitaiCatalog.getToken());

            eheitaiCatalogDao.save(eheitaiCatalog);
        } else {
            System.out.println("为什么会不是1????,或者没有数据?");
        }
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<EheitaiCatalog> findAll() {
        return eheitaiCatalogDao.findAll();
    }

    /**
     * 作品即将爬完,准备去查询更新
     *
     * @param gid 即将完成的作品id
     */
    @Override
    public void saveComplete(Integer gid) {

//        List<EheitaiCatalog> all = eheitaiCatalogDao.findByGid(gid);

        //查看gid作品的总页数
        //判断这个作品作品页面获取了没
        Integer length =  eheitaiCatalogDao.findByGidGetLength(gid);

        if (length!=null) {
            //如果还是0说明之前详情页还没爬下来呢
            if (length != 0) {


                Integer byGidWhereLanguage = eheitaiCatalogDao.findByGidWhereLanguage(gid);
                //查看作品的实际下载页数
                Integer byGidWherePage = eheitaiDetailPageDao.findByGidWherePage(gid);

                if (byGidWhereLanguage.equals(byGidWherePage) ) {
                    //如果当前作品eheitaiCatalog记录的页数相等于他对应的eheitaiDetailPages的总数,那作品就下载完成
                    //更新作品状态
                    List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(gid);
                    if (byGid.size() == 1) {
                        //确保作品数据唯一

                        EheitaiCatalog eheitaiCatalog = byGid.get(0);
                        eheitaiCatalog.setComplete(1);
                        //作品设置下载值为1,表示该作品抓取完成
                        eheitaiCatalogDao.save(eheitaiCatalog);

                    } else if (byGid.size() > 1) {
                        System.out.println("有重复gid的作品");
                    }


                    try {
                        activeMqQueueProduce.postMessage(gid);

                    } catch (JMSException e) {
                        System.out.println("发送消息失败,请检查ActiveMQ是否启动");
                        e.printStackTrace();
                    }
                } else if (byGidWherePage > byGidWhereLanguage) {
                    //判断实际下载页数是否大于作品总页数
                    System.out.println("GID:" + gid + "  一般你看到这个消息的时候,说明实际下载页数,比作品总数多,sql中有重复数据,除非是他网站数据出问题,不然这个问题就很麻烦了");

                }
            }
        }
    }

    /**
     * 查询全部的作品url
     *
     * @return
     */
    @Override
    public List<String> findByUrl() {

        return eheitaiCatalogDao.findByUrl();
    }

    /**
     * 根据作品url连接去查询作品
     *
     * @param s 作品的url连接
     * @return
     */
    @Override
    public EheitaiCatalog findByUrl(String s) {

        return eheitaiCatalogDao.findByUrl(s);
    }

    /**
     * 查看作品页面总和
     *
     * @return
     */
    @Override
    public Integer findByUrlCountPage() {
        return eheitaiCatalogDao.findByUrlCountPage();
    }

    /**
     * 只查询指定下载状态的作品
     *
     * @return
     */
    public List<EheitaiCatalog> findByComplete(Integer integer) {
        return eheitaiCatalogDao.findByComplete(integer);
    }


}
