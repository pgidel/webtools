package com.bierocratie.model;

import com.bierocratie.model.catalog.Supplier;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 09/04/14
 * Time: 22:31
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "order_")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @NotNull
    private Supplier supplier;

    @ManyToOne
    private Customer customer;

    @Temporal(TemporalType.DATE)
    @Future
    private Date date;

    private String comment;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (date != null ? !date.equals(order.date) : order.date != null) return false;
        if (supplier != null ? !supplier.equals(order.supplier) : order.supplier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = supplier != null ? supplier.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
