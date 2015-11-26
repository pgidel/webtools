package com.bierocratie.model.catalog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 23/10/15
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "coeffandmargin")
public class CoeffAndMargin {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String code;

    private String description;

    private BigDecimal value;

    private int roundingScale;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoeffAndMargin that = (CoeffAndMargin) o;

        if (!code.equals(that.code)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getRoundingScale() {
        return roundingScale;
    }

    public void setRoundingScale(int roundingScale) {
        this.roundingScale = roundingScale;
    }
}
