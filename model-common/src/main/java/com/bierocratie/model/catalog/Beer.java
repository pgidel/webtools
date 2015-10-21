package com.bierocratie.model.catalog;

import com.bierocratie.model.accounting.Tva;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 24/10/14
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "beer")
public class Beer {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private String region;

    private String style;

    private double abv;

    @NotNull
    @ManyToOne
    private Supplier brewery;

    @ManyToOne
    private Supplier supplier;

    @NotNull
    private Capacity capacity;

    private boolean available;

    private BigDecimal costHT;

    private BigDecimal costingPriceTTC;

    private BigDecimal priceHT;

    private BigDecimal priceTTC;

    private Tva tva;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Beer)) return false;

        Beer beer = (Beer) o;

        if (!name.equals(beer.name)) return false;
        if (!brewery.equals(beer.brewery)) return false;
        if (!capacity.equals(beer.capacity)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = brewery.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + capacity.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Supplier getBrewery() {
        return brewery;
    }

    public void setBrewery(Supplier brewery) {
        this.brewery = brewery;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BigDecimal getCostHT() {
        return costHT;
    }

    public void setCostHT(BigDecimal costHT) {
        this.costHT = costHT;
    }

    public BigDecimal getCostingPriceTTC() {
        return costingPriceTTC;
    }

    public void setCostingPriceTTC(BigDecimal costingPriceTTC) {
        this.costingPriceTTC = costingPriceTTC;
    }

    public BigDecimal getPriceHT() {
        return priceHT;
    }

    public void setPriceHT(BigDecimal priceHT) {
        this.priceHT = priceHT;
    }

    public BigDecimal getPriceTTC() {
        return priceTTC;
    }

    public void setPriceTTC(BigDecimal priceTTC) {
        this.priceTTC = priceTTC;
    }

    public Tva getTva() {
        return tva;
    }

    public void setTva(Tva tva) {
        this.tva = tva;
    }

    public String getLabel() {
        if (name == null || name.isEmpty()) {
            return "";
        }
        if (brewery == null || brewery.getName() == null || brewery.getName().isEmpty()) {
            return name;
        }
        return name + " - " + brewery.getName();
    }

}
