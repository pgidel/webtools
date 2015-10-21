package com.bierocratie.bean;

import com.bierocratie.model.accounting.Tva;
import com.bierocratie.model.catalog.Beer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 16/09/15
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class BeerBean {

    private Beer beer;

    private BigDecimal priceCostingTTC;

    private BigDecimal priceHT;

    private BigDecimal priceTTC;

    public BigDecimal getPriceCostingTTC() {
        return priceCostingTTC;
    }

    public void setPriceCostingTTC(BigDecimal priceCostingTTC) {
        this.priceCostingTTC = priceCostingTTC;
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

}
