package com.zh.property.dao;

import com.zh.property.pojo.User;
import com.zh.property.pojo.Workorder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkorderDAO extends JpaRepository<Workorder,Integer> {
    public List<Workorder> findByUserOrderByIdDesc(User user);
    public List<Workorder> findByOrderCodeLike(String keyword, Pageable pageable);
}
