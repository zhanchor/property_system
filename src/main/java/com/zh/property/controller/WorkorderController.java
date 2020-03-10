package com.zh.property.controller;

import com.zh.property.pojo.Workorder;
import com.zh.property.service.OrderItemService;
import com.zh.property.service.WorkorderService;
import com.zh.property.util.Page4Navigator;
import com.zh.property.util.Result;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class WorkorderController {
    @Autowired WorkorderService workorderService;
    @Autowired OrderItemService orderItemService;

    @GetMapping("/workorders")
    public Page4Navigator<Workorder> list(@RequestParam(value="start",defaultValue = "0")int start,
                                          @RequestParam(value="size",defaultValue = "5")int size) throws  Exception{
        start=start<0?0:start;
        Page4Navigator<Workorder> page=workorderService.list(start,size,5);
        orderItemService.fill(page.getContent());
        workorderService.removeWorkorderFromOrderItem(page.getContent());
        return page;
    }

    @PostMapping("/workorders")
    public Object add(@RequestBody Workorder bean){
        String orderCode=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(10000);
        bean.setOrderCode(orderCode);
        bean.setCreateDate(new Date());
        bean.setStatus(WorkorderService.waitReceive);
        workorderService.add(bean);
        return Result.success("创建工单成功");
    }

    @GetMapping("/workorders/{id}")
    public Object get(@PathVariable("id")int id)throws Exception{
        Workorder workorder=workorderService.get(id);
        orderItemService.fill(workorder);
        workorderService.removeWorkorderFromOrderItem(workorder);
        return workorder;
    }

}
