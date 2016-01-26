package com.coongli.domain;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Invitation.
 */
@Entity
@Table(name = "invitation")
public class Invitation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "body", nullable = false)
    private String body;
    
    @SafeHtml
    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;
    
    @Column(name = "accepted")
    private Boolean accepted;
    
    @Column(name = "rejected")
    private Boolean rejected;
    
    @NotNull
	@Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "creationmoment", nullable = false)
    private LocalDate creationmoment;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getAccepted() {
        return accepted;
    }
    
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getRejected() {
        return rejected;
    }
    
    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public LocalDate getCreationmoment() {
        return creationmoment;
    }
    
    public void setCreationmoment(LocalDate creationmoment) {
        this.creationmoment = creationmoment;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User user) {
        this.sender = user;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User user) {
        this.recipient = user;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invitation invitation = (Invitation) o;
        if(invitation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, invitation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Invitation{" +
            "id=" + id +
            ", body='" + body + "'" +
            ", subject='" + subject + "'" +
            ", accepted='" + accepted + "'" +
            ", rejected='" + rejected + "'" +
            ", creationmoment='" + creationmoment + "'" +
            '}';
    }
}
