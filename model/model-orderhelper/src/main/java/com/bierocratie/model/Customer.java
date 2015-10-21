package com.bierocratie.model;

import com.bierocratie.model.security.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 09/04/14
 * Time: 22:33
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "customer")
public class Customer extends Account implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    private String address;

    private String telephone;

    private boolean receiveDelivery;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    private Set<Order> orders = new HashSet<>();

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Boolean getReceiveDelivery() {
        return receiveDelivery;
    }

    public void setReceiveDelivery(Boolean receiveDelivery) {
        this.receiveDelivery = receiveDelivery;
    }

    @Override
    public String toString() {
        return name;
    }

}
