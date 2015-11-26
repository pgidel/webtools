package com.bierocratie.model.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 23/10/14
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "category")
public class Category implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3768194549364378014L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name = "-";

    private double percentageDueByTheCompany = 1;

    private Tva defaultTva;

    public Category() {
    }

    public Category(String name, Tva tva) {
        this.name = name;
        this.defaultTva = tva;
    }

    public Category(Tva tva) {
        this.defaultTva = tva;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (!name.equals(category.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentageDueByTheCompany() {
        return percentageDueByTheCompany;
    }

    public void setPercentageDueByTheCompany(double percentageDueByTheCompany) {
        this.percentageDueByTheCompany = percentageDueByTheCompany;
    }

    public Tva getDefaultTva() {
        return defaultTva;
    }

    public void setDefaultTva(Tva defaultTva) {
        this.defaultTva = defaultTva;
    }
}
