package com.bierocratie.model.accounting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 23/10/14
 * Time: 11:37
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8942925518737979101L;

	public static final String DEFAULT_MONTH = "-";

    @Id
    @GeneratedValue
    private Long id;

    // TODO importer la d en java.util.Date
    //@Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "d")
    private String d;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String month;

    @NotNull
    private String supplier;

    @NotNull
    private BigDecimal amount;

    private Category category;

    private Tva tva;

    private boolean imported;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");

    public Invoice() {
    }

    public Invoice(String date, String supplier, String amount, String month, boolean imported) {
        try {
            this.d = date;
            this.date = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            this.date = null;
        }
        this.supplier = supplier;
        this.amount = new BigDecimal(amount.split("=")[0].replace(",", "."));
        //this.amount = Double.parseDouble(amount.split("=")[0].replace(",", "."));
        this.month = month;
        this.imported = imported;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "date=" + date +
                ", supplier='" + supplier + '\'' +
                ", amount=" + amount +
                ", month='" + month + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;

        if (!amount.equals(invoice.amount)) return false;
        if (!date.equals(invoice.date)) return false;
        if (!supplier.equals(invoice.supplier)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + supplier.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
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
        if (date != null) {
            this.d = simpleDateFormat.format(date);
            this.date = date;
        }
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
        if (this.date == null) {
            try {
                this.date = simpleDateFormat.parse(d);
            } catch (ParseException e) {
                this.date = null;
            }
        }
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Tva getTva() {
        return tva;
    }

    public void setTva(Tva tva) {
        this.tva = tva;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

}
