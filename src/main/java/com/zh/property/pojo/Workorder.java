package com.zh.property.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zh.property.service.WorkorderService;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="workorder")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Document(indexName = "property_system",type="workorder")
public class Workorder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    private String orderCode;
    private String errordescription;
    private String fixdescription;
    private String status;
    private Date createDate;
    private Date receiveDate;
    private Date fixDate;
    private Date finishDate;

    @Transient
    private List<OrderItem> orderItems;
    @Transient
    private String statusDesc;

    public List<OrderItem> getOrderItems(){
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems){
        this.orderItems=orderItems;
    }

    public void setStatusDesc(String statusDesc){
        this.statusDesc=statusDesc;
    }

    public String getStatusDesc(){
        if(null!=statusDesc) return statusDesc;
        String desc="未知";
        switch(status){
            case WorkorderService.waitReceive:
                desc="等待接收";
                break;
            case WorkorderService.waitFix:
                desc="等待处理";
                break;
            case WorkorderService.waitFinish:
                desc="正在处理";
                break;
            case WorkorderService.finish:
                desc="已完成";
                break;
            default:
                desc="未知";
        }
        statusDesc=desc;
        return statusDesc;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getOrderCode(){
        return orderCode;
    }

    public void setOrderCode(String orderCode){
        this.orderCode=orderCode;
    }

    public String getErrordescription(){
        return errordescription;
    }

    public void setErrordescription(String errordescription){
        this.errordescription=errordescription;
    }

    public String getFixdescription(){
        return fixdescription;
    }

    public void setFixdescription(String fixdescription){
        this.fixdescription=fixdescription;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public Date getCreateDate(){
        return createDate;
    }

    public void setCreateDate(Date createDate){
        this.createDate=createDate;
    }

    public Date getReceiveDate(){
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate){
        this.receiveDate=receiveDate;
    }

    public Date getFixDate(){
        return fixDate;
    }

    public void setFixDate(Date fixDate){
        this.fixDate=fixDate;
    }

    public Date getFinishDate(){
        return finishDate;
    }

    public void setFinishDate(Date finishDate){
        this.finishDate=finishDate;
    }

    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(Customer customer){
        this.customer=customer;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user=user;
    }





}
