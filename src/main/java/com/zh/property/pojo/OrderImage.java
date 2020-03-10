package com.zh.property.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="orderimage")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class OrderImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="oid")
    @JsonBackReference
    private Workorder workorder;

    private String type;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public Workorder getWorkorder(){
        return workorder;
    }

    public void setWorkorder(Workorder workorder){
        this.workorder=workorder;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }
}
