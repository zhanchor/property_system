package com.zh.property.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "equipment")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer"})
@Document(indexName = "property_system",type = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name="cid")
    private Customer customer;

    private String name;
    private String location;
    private String type;
    private String status;
    private String equipman;
    private Date equipDate;
    private Date useDate;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public Customer getCustomer(){
        return customer;
    }
    public void setCustomer(Customer bean){
        this.customer=bean;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public String getEquipman(){
        return equipman;
    }
    public void setEquipman(String equipman){
        this.equipman=equipman;
    }
    public Date getEquipDate(){
        return equipDate;
    }
    public void setEquipDate(Date equipDate){
        this.equipDate=equipDate;
    }
    public Date getUseDate(){
        return useDate;
    }
    public void setUseDate(Date useDate){
        this.useDate=useDate;
    }


}
