package com.bierocratie.model.catalog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 28/10/14
 * Time: 09:40
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "suppliertype")
public class SupplierType implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -769844805970423984L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    public SupplierType() {
    }

    public SupplierType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SupplierType)) return false;

        SupplierType that = (SupplierType) o;

        if (!name.equals(that.name)) return false;

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
}
