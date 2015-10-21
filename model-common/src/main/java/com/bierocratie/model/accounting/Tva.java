package com.bierocratie.model.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 23/10/14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "tva")
public class Tva {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private BigDecimal rate;

    public Tva() {
    }

    public Tva(BigDecimal rate) {
        this.rate = rate;
    }

    private static final DecimalFormat decimalFormatter = new DecimalFormat("#.##%");

    @Override
    public String toString() {
        return decimalFormatter.format(rate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tva tva = (Tva) o;

        if (!rate.equals(tva.rate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return rate.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRate2String() {
        return toString();
    }

}
