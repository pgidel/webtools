package com.bierocratie.model.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 29/10/14
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "stock")
public class Stock implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String beer;

    @NotNull
    private int quantity;

    @NotNull
    private String supplierCode;

    @Override
    public String toString() {
        return beer + "[" + quantity + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;

        Stock stock = (Stock) o;

        if (!code.equals(stock.code)) return false;
        if (!supplierCode.equals(stock.supplierCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + supplierCode.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeer() {
        return beer;
    }

    public void setBeer(String beer) {
        this.beer = beer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
