package com.bierocratie.model.diffusion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pir
 * Date: 18/10/14
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6386991728380354155L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String title;

    private List<MediumMessage> media;

    private boolean planned;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (!title.equals(message.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public String toString() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MediumMessage> getMedia() {
        return media;
    }

    public void setMedia(List<MediumMessage> media) {
        this.media = media;
    }

    public Boolean isPlanned() {
        return planned;
    }

    public void setPlanned(Boolean planned) {
        this.planned = planned;
    }
}
