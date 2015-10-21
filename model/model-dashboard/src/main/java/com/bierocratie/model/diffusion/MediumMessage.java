package com.bierocratie.model.diffusion;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 18/10/14
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "mediummessage")
public class MediumMessage implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Message message;

    @NotNull
    @ManyToOne
    private Medium medium;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediumMessage)) return false;

        MediumMessage that = (MediumMessage) o;

        if (!date.equals(that.date)) return false;
        if (!medium.equals(that.medium)) return false;
        if (!message.equals(that.message)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + medium.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
