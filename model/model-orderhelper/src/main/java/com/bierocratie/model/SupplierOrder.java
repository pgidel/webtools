package com.bierocratie.model;

import com.bierocratie.model.catalog.Supplier;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 09/04/14
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "supplierorder")
public class SupplierOrder implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Supplier supplier;

    @Temporal(TemporalType.DATE)
    @Future
    private Date date;

    @OneToMany
    private List<CustomerOrder> orders = new ArrayList<>();

    @ManyToMany
    private Set<Customer> customers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierOrder)) return false;

        SupplierOrder that = (SupplierOrder) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (!supplier.equals(that.supplier)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = supplier.hashCode();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<CustomerOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CustomerOrder> orders) {
        this.orders = orders;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
}
