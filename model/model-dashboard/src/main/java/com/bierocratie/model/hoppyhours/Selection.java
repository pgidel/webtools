package com.bierocratie.model.hoppyhours;

import com.bierocratie.model.catalog.Beer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 21/10/14
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "hoppyhours")
public class Selection implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1339179194834952847L;

	@Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    @ManyToOne
    private Beer beer;

    private Integer quantity;

    @Temporal(TemporalType.DATE)
    private Date billingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Selection)) return false;

        Selection selection = (Selection) o;

        if (!beer.equals(selection.beer)) return false;
        if (date != null ? !date.equals(selection.date) : selection.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + beer.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return date + " - " + beer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }
}
