package com.bierocratie.model;

import com.bierocratie.model.catalog.Supplier;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 09/04/14
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "customerorder")
public class CustomerOrder implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Supplier supplier;

    @NotNull
    @ManyToOne
    private Customer customer;

    @Temporal(TemporalType.DATE)
    @Future
    private Date date;

    private String comment;

    @OneToMany
    private List<OrderBeer> beers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerOrder)) return false;

        CustomerOrder that = (CustomerOrder) o;

        if (!customer.equals(that.customer)) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (!supplier.equals(that.supplier)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = supplier.hashCode();
        result = 31 * result + customer.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Customer getCustomer() {
        return customer;
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

    public List<OrderBeer> getBeers() {
        return beers;
    }

    public void setBeers(List<OrderBeer> beers) {
        this.beers = beers;
    }
}
