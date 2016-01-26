package com.coongli.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Textable.
 */
@Entity
@Table(name = "textable")
public class Textable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "text", nullable = false)
    private String text;
    
    @Column(name = "creationmoment")
    @NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date creationmoment;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationmoment() {
        return creationmoment;
    }
    
    public void setCreationmoment(Date creationmoment) {
        this.creationmoment = creationmoment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Textable textable = (Textable) o;
        if(textable.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, textable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Textable{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", creationMoment='" + creationmoment + "'" +
            '}';
    }
}
