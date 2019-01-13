package com.xhxj.service.impl;

import com.xhxj.dao.EheitaiCatalogDao;
import com.xhxj.daomain.EheitaiCatalog;
import com.xhxj.service.EheitaiCatalogService;
import com.xhxj.web.ActiveMqQueueProduce;
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
    ActiveMqQueueProduce activeMqQueueProduce;


    /**
     * 保存对象
     * @param eheitaiCatalog 作品对象
     */
    @Override
    public void save(EheitaiCatalog eheitaiCatalog) {
        eheitaiCatalogDao.save(eheitaiCatalog);
    }

    /**
     * 根据gid去查询对象
     * @param gid 作品id
     * @return
     */
    @Override
    public List<EheitaiCatalog> findByGid(Integer gid) {
        return eheitaiCatalogDao.findByGid(gid);
    }

    /**
     * 更新旧的数据
     * @param pageEheitaiCatalog 解析页面page获取到的数据
     */
    @Override
    public void updatePageEheitaiCatalog(EheitaiCatalog pageEheitaiCatalog) {
        List<EheitaiCatalog> byGid = eheitaiCatalogDao.findByGid(pageEheitaiCatalog.getGid());
        EheitaiCatalog eheitaiCatalog = byGid.get(0);

        //把新传入的数据拿出来存到旧的里面
        eheitaiCatalog.setFileSize(pageEheitaiCatalog.getFileSize());
        eheitaiCatalog.setLanguage(pageEheitaiCatalog.getLanguage());
        eheitaiCatalog.setLength(pageEheitaiCatalog.getLength());
        eheitaiCatalog.setParent(pageEheitaiCatalog.getParent());
        eheitaiCatalog.setPostedDate(pageEheitaiCatalog.getPostedDate());
        eheitaiCatalog.setToken(pageEheitaiCatalog.getToken());

        eheitaiCatalogDao.save(eheitaiCatalog);
    }

    /**
     * 查询全部
     * @return
     */
    @Override
    public List<EheitaiCatalog> findAll() {
        return eheitaiCatalogDao.findAll();
    }

    /**
     * 作品即将爬完,准备去查询更新
     * @param gid 即将完成的作品id
     */
    @Override
    public void saveComplete(Integer gid) {

        List<EheitaiCatalog> all = eheitaiCatalogDao.findByGid(gid);

        if (all.get(0).getEheitaiDetailPages().size()==all.get(0).getLength()){
            //如果当前作品eheitaiCatalog记录的页数相等于他对应的eheitaiDetailPages的总数,那作品就下载完成
            try {
                activeMqQueueProduce.postMessage(gid);

            } catch (JMSException e) {
                System.out.println("发送消息失败,请检查ActiveMQ是否启动");
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询全部的作品url
     * @return
     */
    @Override
    public List<String> findByUrl() {

        return eheitaiCatalogDao.findByUrl();
    }

    @Override
    public EheitaiCatalog findByUrl(String s) {

        return eheitaiCatalogDao.findByUrl(s);
    }


}
