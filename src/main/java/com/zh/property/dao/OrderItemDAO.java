package com.zh.property.dao;

import com.zh.property.pojo.Equipment;
import com.zh.property.pojo.OrderItem;
import com.zh.property.pojo.User;
import com.zh.property.pojo.Workorder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByWorkorderOrderByIdDesc(Workorder workorder);
    List<OrderItem> findByEquipmentOrderByIdDesc(Equipment equipment);
    OrderItem getByWorkorderAndEquipment(Workorder workorder, Equipment equipment);
}
