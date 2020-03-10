package com.zh.property.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    @GetMapping(value="/admin")
    public String admin(){
        return "redirect:admin_customer_list";
    }

    @GetMapping(value="/admin_customer_list")
    public String listCustomer(){
        return "admin/listCustomer";
    }

    @GetMapping(value="/admin_customer_edit")
    public String editCustomer(){
        return "admin/editCustomer";
    }

    @GetMapping(value="/admin_equipment_list")
    public String listEquipment(){
        return "admin/listEquipment";
    }

    @GetMapping(value="/admin_equipment_edit")
    public String editEquipment(){
        return "admin/editEquipment";
    }

    @GetMapping(value="/admin_user_list")
    public String listUser(){
        return "admin/listUser";
    }

    @GetMapping(value="/admin_workorder_list")
    public String listWorkorder(){
        return "admin/listWorkorder";
    }
}
