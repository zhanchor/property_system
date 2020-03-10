package com.zh.property.controller;

import com.zh.property.pojo.OrderItem;
import com.zh.property.pojo.User;
import com.zh.property.pojo.Workorder;
import com.zh.property.service.OrderItemService;
import com.zh.property.service.UserService;
import com.zh.property.service.WorkorderService;
import com.zh.property.util.Result;
import javafx.scene.control.TableView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class ForeRESTController {
    @Autowired UserService userService;
    @Autowired WorkorderService workorderService;
    @Autowired OrderItemService orderItemService;

    @PostMapping("/foreregister")
    public Object register(@RequestBody User user) {
        String name =  user.getName();
        String password = user.getPassword();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);

        boolean exist = userService.isExist(name);

        if(exist){
            String message ="用户名已经被使用,不能使用";
            return Result.fail(message);
        }

        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String algorithmName = "md5";

        String encodedPassword = new SimpleHash(algorithmName, password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);
        user.setAdmin((short) 0);
        userService.add(user);

        return Result.success();
    }

    @PostMapping("/forelogin")
    public Object login(@RequestBody User userParam, HttpSession session) {
        String name =  userParam.getName();
        name = HtmlUtils.htmlEscape(name);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, userParam.getPassword());
        try {
            subject.login(token);
            User user = userService.getByName(name);
//          subject.getSession().setAttribute("user", user);
            session.setAttribute("user", user);
            return Result.success();
        } catch (AuthenticationException e) {
            String message ="账号密码错误";
            return Result.fail(message);
        }
    }

    @GetMapping("/foreworkorders")
    public Object workorders(HttpSession session){
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        List<Workorder> workorders=workorderService.listByUser(user);
        return workorders;
    }

    @GetMapping("/forereceived")
    public Object reveiced(int oid,HttpSession session){
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        Workorder workorder=workorderService.get(oid);
        if(workorder.getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        if(!WorkorderService.waitReceive.equals(workorder.getStatus())){
            return Result.fail("该工单状态为"+workorder.getStatusDesc()+"无法进行该操作");
        }
        workorder.setStatus(WorkorderService.waitFix);
        workorder.setReceiveDate(new Date());
        workorderService.update(workorder);
        return Result.success(workorder);
    }

    @GetMapping("/forefixing")
    public Object fixing(int oid,HttpSession session){
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        Workorder workorder=workorderService.get(oid);
        if(workorder.getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        if(!WorkorderService.waitFix.equals(workorder.getStatus())){
            return Result.fail("该工单状态为"+workorder.getStatusDesc()+"，无法进行该操作");
        }
        workorder.setStatus(WorkorderService.waitFinish);
        workorder.setFixDate(new Date());
        workorderService.update(workorder);
        return Result.success(workorder);
    }

    @GetMapping("/foreorderItems")
    public Object orderItems(int oid){
        Workorder workorder=workorderService.get(oid);
        List<OrderItem> orderItems=orderItemService.listByWorkorder(workorder);
        return Result.success(orderItems);
    }

    @DeleteMapping("/orderItems/{id}")
    public Object deleteOrderItem(@PathVariable("id")int id,HttpSession session)throws Exception{
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        OrderItem orderItem=orderItemService.get(id);
        if(orderItem.getWorkorder().getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        orderItemService.delete(id);
        return Result.success();
    }

    @PostMapping("/orderItems")
    public Object addOrderItem(@RequestBody OrderItem orderItem,HttpSession session)throws Exception{
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        Workorder workorder=workorderService.get(orderItem.getWorkorder().getId());
        if(workorder.getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        OrderItem orderItemfound=orderItemService.getByWorkorderAndEquipment(orderItem.getWorkorder(),orderItem.getEquipment());
        if(null==orderItemfound){
            orderItemService.add(orderItem);
            return Result.success();
        }
        else{
            return Result.fail("该设备已添加");
        }
    }

    @PutMapping("/forefinish")
    public Object finish(@RequestBody Workorder workorder,HttpSession session)throws Exception{
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        if(workorder.getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        if(!WorkorderService.waitFinish.equals(workorder.getStatus())){
            return Result.fail("该工单状态为"+workorder.getStatusDesc()+",无法进行该操作");
        }
        workorder.setFinishDate(new Date());
        workorder.setStatus(WorkorderService.finish);
        workorderService.update(workorder);
        return Result.success(workorder);
    }

    @PostMapping("/foresearch")
    public Object search(String keyword){
        if(null==keyword)
            keyword="";
        List<Workorder> workorders=workorderService.search(keyword,0,20);
        return workorders;
    }
}
