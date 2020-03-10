package com.zh.property.service;

import com.zh.property.dao.EquipmentDAO;
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

import java.util.List;

@Service
@CacheConfig(cacheNames="equipments")
public class EquipmentService {
    @Autowired EquipmentDAO equipmentDAO;
    @Autowired CustomerService customerService;

    @CacheEvict(allEntries=true)
    public void add(Equipment bean) {
        equipmentDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        equipmentDAO.delete(id);
    }

    @Cacheable(key="'equipments-one-'+#p0")
    public Equipment get(int id) {
        return equipmentDAO.findOne(id);
    }

    @CacheEvict(allEntries=true)
    public void update(Equipment bean) {
        equipmentDAO.save(bean);
    }

    @Cacheable(key="'equipments-cid-'+#p0+'-page-'+#p1+'-'+#p2")
    public Page4Navigator<Equipment> list(int cid, int start, int size, int navigatePages) {
        Customer customer = customerService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Equipment> pageFromJPA =equipmentDAO.findByCustomer(customer,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Cacheable(key="'equipments-cid-'+#p0.id")
    public List<Equipment> findByCustomer(Customer customer){
        return equipmentDAO.findByCustomerOrderByIdDesc(customer);
    }

}
