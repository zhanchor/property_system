package com.zh.property.controller;

import com.zh.property.pojo.Customer;
import com.zh.property.pojo.Equipment;
import com.zh.property.service.CustomerService;
import com.zh.property.service.EquipmentService;
import com.zh.property.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class EquipmentController {
    @Autowired EquipmentService equipmentService;
    @Autowired CustomerService customerService;

    @GetMapping("/customers/{cid}/equipments")
    public Page4Navigator<Equipment> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Equipment> page =equipmentService.list(cid, start, size,5 );

        return page;
    }

    @GetMapping("/customers/{cid}/allequipments")
    public List<Equipment> findByCustomer(@PathVariable("cid")int cid)throws Exception{
        Customer customer=customerService.get(cid);
        List<Equipment> equipments=equipmentService.findByCustomer(customer);
        return equipments;
    }

    @GetMapping("/equipments/{id}")
    public Equipment get(@PathVariable("id") int id) throws Exception {
        Equipment bean=equipmentService.get(id);
        return bean;
    }

    @PostMapping("/equipments")
    public Object add(@RequestBody Equipment bean) throws Exception {
        equipmentService.add(bean);
        return bean;
    }

    @DeleteMapping("/equipments/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        equipmentService.delete(id);
        return null;
    }

    @PutMapping("/equipments")
    public Object update(@RequestBody Equipment bean) throws Exception {
        equipmentService.update(bean);
        return bean;
    }
}
