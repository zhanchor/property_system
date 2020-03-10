package com.zh.property.dao;

import com.zh.property.pojo.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDAO extends JpaRepository<Customer,Integer> {
}
