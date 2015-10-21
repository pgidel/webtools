package com.bierocratie.model.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 01/11/14
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private BudgetYear year;

    @NotNull
    private String month;

    @NotNull
    private BigDecimal amount;

    private String description;

    private Tva tva;

    public Income() {
    }

    public Income(Tva tva, BudgetYear year) {
        this.tva = tva;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Income income = (Income) o;

        if (!amount.equals(income.amount)) return false;
        if (!month.equals(income.month)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = month.hashCode();
        result = 31 * result + amount.hashCode();
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tva getTva() {
        return tva;
    }

    public void setTva(Tva tva) {
        this.tva = tva;
    }
}
