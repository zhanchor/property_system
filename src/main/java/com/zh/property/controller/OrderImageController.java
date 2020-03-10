package com.zh.property.controller;

import com.zh.property.pojo.OrderImage;
import com.zh.property.pojo.User;
import com.zh.property.pojo.Workorder;
import com.zh.property.service.OrderImageService;
import com.zh.property.service.WorkorderService;
import com.zh.property.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.zh.property.util.ImageUtil;

@RestController
public class OrderImageController {
    @Autowired OrderImageService orderImageService;
    @Autowired WorkorderService workorderService;
    @Value("${web.upload-path}")
    private String filePath;

    @GetMapping("/workorders/{oid}/orderImages")
    public List<OrderImage> list(@RequestParam("type") String type, @PathVariable("oid")int oid)throws Exception{
        Workorder workorder=workorderService.get(oid);
        if(OrderImageService.type_condition.equals(type)){
            return orderImageService.listConditionOrderImage(workorder);
        }
        else if(OrderImageService.type_fix.equals(type)){
            return orderImageService.listFixOrderImage(workorder);
        }
        else{
            return new ArrayList<>();
        }
    }

    @PostMapping("/orderImages")
    public Object add(@RequestParam("oid")int oid, @RequestParam("type")String type,
                      MultipartFile image, HttpServletRequest request, HttpSession session)throws Exception{
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        OrderImage bean=new OrderImage();
        Workorder workorder=workorderService.get(oid);
        if(workorder.getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        bean.setWorkorder(workorder);
        bean.setType(type);

        orderImageService.add(bean);
//        String folder="img/";
        String folder=filePath;
        if(OrderImageService.type_condition.equals(bean.getType())){
            folder+="orderCondition/";
        }
        else{
            folder+="orderFix/";
        }
//        File imageFolder=new File(request.getServletContext().getRealPath(folder));
        File file=new File(folder,bean.getId()+".jpg");
        String fileName=file.getName();
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try{
            image.transferTo(file);
            BufferedImage img=ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }catch(IOException e){
            e.printStackTrace();
        }

        return Result.success(bean);
    }

    @DeleteMapping("/orderImages/{id}")
    public Object delete(@PathVariable("id")int id,HttpServletRequest request,HttpSession session)throws Exception{
        OrderImage bean=orderImageService.get(id);
        User user=(User)session.getAttribute("user");
        if(null==user){
            return Result.fail("未登录");
        }
        if(bean.getWorkorder().getUser().getId()!=user.getId()){
            return Result.fail("无权操作其他用户工单");
        }
        orderImageService.delete(id);

//        String folder="img/";
        String folder=filePath;
        System.out.println(filePath);
        if(OrderImageService.type_condition.equals(bean.getType())){
            folder+="orderCondition/";
        }
        else{
            folder+="orderFix/";
        }

//        File imageFolder=new File(request.getServletContext().getRealPath(folder));
        File file=new File(folder,bean.getId()+".jpg");
        String name=file.getName();
        file.delete();

        return Result.success();
    }

}
