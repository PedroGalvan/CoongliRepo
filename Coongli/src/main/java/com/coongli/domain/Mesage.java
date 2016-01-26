package com.coongli.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Mesage.
 */
@Entity
@Table(name = "mesage")
public class Mesage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;
    
    @SafeHtml
    @NotNull
    @Column(name = "body", nullable = false)
    private String body;
    
    
    @NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "sentmoment", nullable = false)
    private Date sentmoment;
    
    @Column(name = "saw")
    private Boolean saw;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Actor sender;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Actor recipient;

    @ManyToOne
    @JoinColumn(name = "messagefolder_id")
    private Messagefolder messagefolder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }

    public Date getSentmoment() {
        return sentmoment;
    }
    
    public void setSentmoment(Date sentmoment) {
        this.sentmoment = sentmoment;
    }

    public Boolean getSaw() {
        return saw;
    }
    
    public void setSaw(Boolean saw) {
        this.saw = saw;
    }

    public Actor getSender() {
        return sender;
    }

    public void setSender(Actor actor) {
        this.sender = actor;
    }

    public Actor getRecipient() {
        return recipient;
    }

    public void setRecipient(Actor actor) {
        this.recipient = actor;
    }

    public Messagefolder getMessagefolder() {
        return messagefolder;
    }

    public void setMessagefolder(Messagefolder messagefolder) {
        this.messagefolder = messagefolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mesage mesage = (Mesage) o;
        if(mesage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mesage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mesage{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", body='" + body + "'" +
            ", sentmoment='" + sentmoment + "'" +
            ", saw='" + saw + "'" +
            '}';
    }
}
