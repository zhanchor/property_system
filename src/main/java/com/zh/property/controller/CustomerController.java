package com.zh.property.controller;

import com.zh.property.pojo.Customer;
import com.zh.property.service.CustomerService;
import com.zh.property.util.ImageUtil;
import com.zh.property.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class CustomerController {
    @Autowired CustomerService customerService;
    @Value("${web.upload-path}")
    private String filePath;  //获取上传地址

    @GetMapping("/customers")
    public Page4Navigator<Customer> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Customer> page =customerService.list(start, size, 5);  //5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
        return page;
    }

    @GetMapping("/customers/list")
    public List<Customer> list() throws Exception {
        List<Customer> customers=customerService.list();
        return customers;
    }

    @PostMapping("/customers")
    public Object add(Customer bean, MultipartFile image, HttpServletRequest request) throws Exception {
        customerService.add(bean);
        saveOrUpdateImageFile(bean, image, request);
        return bean;
    }

    public void saveOrUpdateImageFile(Customer bean, MultipartFile image, HttpServletRequest request)
            throws IOException {
//        File imageFolder= new File(request.getServletContext().getRealPath("img/customer"));
        File file = new File(filePath+"customer/",bean.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @DeleteMapping("/customers/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request)  throws Exception {
        customerService.delete(id);
//        File  imageFolder= new File(request.getServletContext().getRealPath("img/customer"));
        File file = new File(filePath+"customer/",id+".jpg");
        file.delete();
        return null;
    }

    @GetMapping("/customers/{id}")
    public Customer get(@PathVariable("id") int id) throws Exception {
        Customer bean=customerService.get(id);
        return bean;
    }

    @PutMapping("/customers/{id}")
    public Object update(Customer bean, MultipartFile image,HttpServletRequest request) throws Exception {
        String name = request.getParameter("name");
        String contact = request.getParameter("contact");
        String address = request.getParameter("address");
        bean.setName(name);
        bean.setContact(contact);
        bean.setAddress(address);
        customerService.update(bean);

        if(image!=null) {
            saveOrUpdateImageFile(bean, image, request);
        }
        return bean;
    }
}
