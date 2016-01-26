package com.coongli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
public class Actor extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @SafeHtml
    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;
    
    @SafeHtml
    @NotNull
    @Email
    @Column(name = "email", nullable = false)
    private String email;
    
    @SafeHtml
    @NotNull
    @Pattern(regexp = "[0-9]{9}")
    @Column(name = "phone", nullable = false)
    private String phone;
    
    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    private Set<Messagefolder> messagefolders = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private Set<Mesage> sentMesages = new HashSet<>();

    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    private Set<Mesage> receivedMesages = new HashSet<>();

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

    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Messagefolder> getMessagefolderss() {
        return messagefolders;
    }

    public void setMessagefolderss(Set<Messagefolder> messagefolders) {
        this.messagefolders = messagefolders;
    }

    public Set<Mesage> getSentMesagess() {
        return sentMesages;
    }

    public void setSentMesagess(Set<Mesage> mesages) {
        this.sentMesages = mesages;
    }

    public Set<Mesage> getReceivedMesagess() {
        return receivedMesages;
    }

    public void setReceivedMesagess(Set<Mesage> mesages) {
        this.receivedMesages = mesages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Actor actor = (Actor) o;
        if(actor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Actor{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", surname='" + surname + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            '}';
    }
}
