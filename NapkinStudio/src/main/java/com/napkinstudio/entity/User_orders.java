package com.napkinstudio.entity;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REMOVE;


@Entity

@Table(name = "users_orders")
public class User_orders{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    private Date lastLook;


    @ManyToOne(fetch = FetchType.LAZY,cascade = {MERGE,REMOVE})
    private Order order;
    public Order getOrder() {
        return this.order;	}
    public void setOrder(Order order) {
        this.order = order;	}

    @ManyToOne(fetch = FetchType.LAZY,cascade = {MERGE,REMOVE})
    private User user;
    public User getUser() {
        return this.user;	}
    public void setUser(User user) {
        this.user = user;	}


    public Date getLastLook() {
        return this.lastLook;
    }
    public void setLastLook(Date lastLook) {
        this.lastLook = lastLook;
    }






}