package com.coongli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
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
    private LocalDate startmoment;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "endmoment", nullable = false)
    private LocalDate endmoment;
    
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
    private Set<User> users = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private Set<Invitation> invitations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartmoment() {
        return startmoment;
    }
    
    public void setStartmoment(LocalDate startmoment) {
        this.startmoment = startmoment;
    }

    public LocalDate getEndmoment() {
        return endmoment;
    }
    
    public void setEndmoment(LocalDate endmoment) {
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

    public Set<User> getUserss() {
        return users;
    }

    public void setUserss(Set<User> users) {
        this.users = users;
    }

    public Set<Invitation> getInvitationss() {
        return invitations;
    }

    public void setInvitationss(Set<Invitation> invitations) {
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
