package com.zh.property.dao;

import com.zh.property.pojo.Customer;
import com.zh.property.pojo.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentDAO  extends JpaRepository<Equipment,Integer> {
    Page<Equipment> findByCustomer(Customer customer, Pageable pageable);
    List<Equipment> findByCustomerOrderByIdDesc(Customer customer);
}
