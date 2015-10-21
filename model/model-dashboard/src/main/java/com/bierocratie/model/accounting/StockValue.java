package com.bierocratie.model.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 01/11/14
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "stockvalue")
public class StockValue {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private BudgetYear year;

    @NotNull
    private double amount;

    public StockValue() {
    }

    public StockValue(BudgetYear year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockValue that = (StockValue) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        if (!year.equals(that.year)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = year.hashCode();
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BudgetYear getYear() {
        return year;
    }

    public void setYear(BudgetYear year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
