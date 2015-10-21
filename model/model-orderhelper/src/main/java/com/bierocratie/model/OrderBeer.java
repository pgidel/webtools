package com.bierocratie.model;


import com.bierocratie.model.catalog.Beer;
import com.bierocratie.model.catalog.Capacity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 25/05/14
 * Time: 23:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "orderbeer")
public class OrderBeer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Beer beer;

    @ManyToOne
    private Capacity capacity;

    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderBeer)) return false;

        OrderBeer orderBeer = (OrderBeer) o;

        if (!beer.equals(orderBeer.beer)) return false;
        if (!capacity.equals(orderBeer.capacity)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = beer.hashCode();
        result = 31 * result + capacity.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
