package com.bierocratie.model.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 03/11/14
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "budgetyear")
public class BudgetYear {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String year;

    @NotNull
    private String firstMonth;

    @NotNull
    private String lastMonth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetYear)) return false;

        BudgetYear that = (BudgetYear) o;

        if (!year.equals(that.year)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return year.hashCode();
    }

    private static final SimpleDateFormat monthFormatter = new SimpleDateFormat("yyMM");

    public static String getCurrentMonth() {
        return monthFormatter.format(new Date());
    }

    public boolean isCurrentYear() {
        String currentMonth = getCurrentMonth();
        return currentMonth.compareTo(firstMonth) >= 0 && currentMonth.compareTo(lastMonth) < 0;
    }

    @Override
    public String toString() {
        return year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFirstMonth() {
        return firstMonth;
    }

    public void setFirstMonth(String firstMonth) {
        this.firstMonth = firstMonth;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }
}
