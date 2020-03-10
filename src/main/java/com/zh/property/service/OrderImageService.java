package com.zh.property.service;

import com.zh.property.dao.OrderImageDAO;
import com.zh.property.pojo.OrderImage;
import com.zh.property.pojo.Workorder;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames="orderImages")
public class OrderImageService {
    public static final String type_condition="condition";
    public static final String type_fix="fix";

    @Autowired OrderImageDAO orderImageDAO;
    @Autowired WorkorderService workorderService;

    @CacheEvict(allEntries = true)
    public void add(OrderImage bean){
        orderImageDAO.save(bean);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id){
        orderImageDAO.delete(id);
    }

    @Cacheable(key="'orderImages-one-'+#p0")
    public OrderImage get(int id){
        return orderImageDAO.findOne(id);
    }

    @Cacheable(key="'orderImages-condition-oid-'+#p0.id")
    public List<OrderImage> listConditionOrderImage(Workorder workorder){
        return orderImageDAO.findByWorkorderAndTypeOrderByIdDesc(workorder,type_condition);
    }

    @Cacheable(key="'orderImages-fix-oid-'+#p0.id")
    public List<OrderImage> listFixOrderImage(Workorder workorder){
        return orderImageDAO.findByWorkorderAndTypeOrderByIdDesc(workorder,type_fix);
    }
}
