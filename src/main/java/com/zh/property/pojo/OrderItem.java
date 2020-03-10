package com.zh.property.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="orderItem")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class OrderItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="eid")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name="oid")
    private Workorder workorder;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public Equipment getEquipment(){
        return equipment;
    }

    public void setEquipment(Equipment equipment){
        this.equipment=equipment;
    }

    public Workorder getWorkorder(){
        return workorder;
    }

    public void setWorkorder(Workorder workorder){
        this.workorder=workorder;
    }

}
