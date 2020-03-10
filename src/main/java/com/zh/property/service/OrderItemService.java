package com.zh.property.service;

import com.zh.property.dao.OrderItemDAO;
import com.zh.property.pojo.Equipment;
import com.zh.property.pojo.OrderItem;
import com.zh.property.pojo.Workorder;
import com.zh.property.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "orderItems")
public class OrderItemService {
    @Autowired OrderItemDAO orderItemDAO;


    public void fill(List<Workorder> workorders){
        for(Workorder workorder:workorders){
            fill(workorder);
        }
    }

    public void fill(Workorder workorder){
        OrderItemService orderItemService= SpringContextUtil.getBean(OrderItemService.class);
        List<OrderItem> orderItems=orderItemService.listByWorkorder(workorder);
        workorder.setOrderItems(orderItems);
    }

    @Cacheable(key="'orderItems-oid-'+#p0.id")
    public List<OrderItem> listByWorkorder(Workorder workorder){
        return orderItemDAO.findByWorkorderOrderByIdDesc(workorder);
    }

    @CacheEvict(allEntries=true)
    public void add(OrderItem bean){
        orderItemDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id){
        orderItemDAO.delete(id);
    }

    @Cacheable(key="'orderItems-oid-'+#p0.id+'-eid-'+#p1.id")
    public OrderItem getByWorkorderAndEquipment(Workorder workorder, Equipment equipment){
        return orderItemDAO.getByWorkorderAndEquipment(workorder,equipment);
    }

    @Cacheable(key="'orderItems-one-'+#p0")
    public OrderItem get(int id){
        return orderItemDAO.findOne(id);
    }

}
