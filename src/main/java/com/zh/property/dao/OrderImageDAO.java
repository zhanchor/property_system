package com.zh.property.dao;

import com.zh.property.pojo.OrderImage;
import com.zh.property.pojo.Workorder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderImageDAO extends JpaRepository<OrderImage,Integer> {
    public List<OrderImage> findByWorkorderAndTypeOrderByIdDesc(Workorder workorder,String type);
}
