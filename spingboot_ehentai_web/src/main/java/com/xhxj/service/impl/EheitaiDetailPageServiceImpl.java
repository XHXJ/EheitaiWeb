package com.xhxj.service.impl;

import com.xhxj.dao.EheitaiDetailPageDao;
import com.xhxj.daomain.EheitaiDetailPage;
import com.xhxj.service.EheitaiDetailPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EheitaiDetailPageServiceImpl implements EheitaiDetailPageService {

    @Autowired
    EheitaiDetailPageDao eheitaiDetailPageDao;

    /**
     * 保存对象
     * @param eheitaiDetailPage 下载详情页面
     */
    @Override
    public void save(EheitaiDetailPage eheitaiDetailPage) {
        eheitaiDetailPageDao.save(eheitaiDetailPage);
    }
}
