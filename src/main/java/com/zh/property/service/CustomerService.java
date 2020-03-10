package com.zh.property.service;

import java.util.List;

import com.zh.property.dao.CustomerDAO;
import com.zh.property.pojo.Customer;
import com.zh.property.pojo.Equipment;
import com.zh.property.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@CacheConfig(cacheNames="customers")
public class CustomerService {
    @Autowired CustomerDAO customerDAO;

    @Cacheable(key="'customers-page-'+#p0+ '-' + #p1")
    public Page4Navigator<Customer> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =customerDAO.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Cacheable(key="'customers-all'")
    public List<Customer> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return customerDAO.findAll(sort);
    }

    @CacheEvict(allEntries=true)
    public void add(Customer bean) {
        customerDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        customerDAO.delete(id);
    }

    @Cacheable(key="'customers-one-'+ #p0")
    public Customer get(int id) {
        Customer c= customerDAO.findOne(id);
        return c;
    }

    @CacheEvict(allEntries=true)
    public void update(Customer bean) {
        customerDAO.save(bean);
    }

}