package com.bierocratie.model.diffusion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 18/10/14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "medium")
public class Medium implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6478933814353104125L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    public Medium() {
    }

    public Medium(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medium)) return false;

        Medium medium = (Medium) o;

        if (!name.equals(medium.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
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
