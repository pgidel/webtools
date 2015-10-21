package com.bierocratie.model.order;

import com.bierocratie.model.catalog.Supplier;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 00:02
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "order_")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Supplier supplier;

    @Lob
    private String list;

    private boolean ordered;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (!supplier.equals(order.supplier)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return supplier.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(Boolean ordered) {
        this.ordered = ordered;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
