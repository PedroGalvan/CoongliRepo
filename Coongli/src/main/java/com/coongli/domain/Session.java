package com.coongli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Session.
 */
@Entity
@Table(name = "session")
public class Session implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "startmoment", nullable = false)
    private Date startmoment;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "endmoment", nullable = false)
    private Date endmoment;
    
    @Column(name = "periodica")
    private Boolean periodica;
    
    @Column(name = "hidden")
    private Boolean hidden;
    
    @Column(name = "accepted")
    private Boolean accepted;
    
    @Column(name = "cancelled")
    private Boolean cancelled;
    
    @OneToOne
    private Report report;
    
    @NotNull
    @ManyToMany(mappedBy = "sessions")
    @JsonIgnore
    private Collection<User> users = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private Collection<Invitation> invitations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartmoment() {
        return startmoment;
    }
    
    public void setStartmoment(Date startmoment) {
        this.startmoment = startmoment;
    }

    public Date getEndmoment() {
        return endmoment;
    }
    
    public void setEndmoment(Date endmoment) {
        this.endmoment = endmoment;
    }

    public Boolean getPeriodica() {
        return periodica;
    }
    
    public void setPeriodica(Boolean periodica) {
        this.periodica = periodica;
    }

    public Boolean getHidden() {
        return hidden;
    }
    
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getAccepted() {
        return accepted;
    }
    
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Boolean getCancelled() {
        return cancelled;
    }
    
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(Collection<Invitation> invitations) {
        this.invitations = invitations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        if(session.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Session{" +
            "id=" + id +
            ", startmoment='" + startmoment + "'" +
            ", endmoment='" + endmoment + "'" +
            ", periodica='" + periodica + "'" +
            ", hidden='" + hidden + "'" +
            ", accepted='" + accepted + "'" +
            ", cancelled='" + cancelled + "'" +
            '}';
    }
}
