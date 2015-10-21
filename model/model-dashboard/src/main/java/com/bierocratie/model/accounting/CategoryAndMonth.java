package com.bierocratie.model.accounting;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 31/10/14
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */
public class CategoryAndMonth {

    private Category category;

    private String month;

    private BigInteger amount;

    public CategoryAndMonth(Category category, String month, BigDecimal amount) {
        if (category != null) {
            this.category = category;
        } else {
            this.category = new Category();
        }
        this.month = month;
        if (amount != null) {
            this.amount = amount.toBigInteger();
        } else {
            this.amount = BigInteger.ZERO;
        }
    }

    public CategoryAndMonth(Category category, String month, double amount) {
        if (category != null) {
            this.category = category;
        } else {
            this.category = new Category();
        }
        this.month = month;
        this.amount = new BigDecimal(amount).toBigInteger();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryAndMonth)) return false;

        CategoryAndMonth that = (CategoryAndMonth) o;

        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (!month.equals(that.month)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + month.hashCode();
        return result;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
}
