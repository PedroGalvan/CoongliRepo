package com.coongli.domain;

import com.coongli.security.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
    
    @NotNull
    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    private Collection<Messagefolder> messagefolders = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private Collection<Mesage> sentmesages = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    private Collection<Mesage> receivedmesages = new ArrayList<>();
    
    @NotNull
    @OneToOne
    @JsonIgnore
    private UserAccount useraccount;

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

    public Collection<Messagefolder> getMessagefolders() {
        return messagefolders;
    }

    public void setMessagefolders(Collection<Messagefolder> messagefolders) {
        this.messagefolders = messagefolders;
    }

    public Collection<Mesage> getSentmesages() {
        return sentmesages;
    }

    public void setSentmesages(Collection<Mesage> mesages) {
        this.sentmesages = mesages;
    }

    public Collection<Mesage> getReceivedmesages() {
        return receivedmesages;
    }

    public void setReceivedmesages(Collection<Mesage> mesages) {
        this.receivedmesages = mesages;
    }
    
    public UserAccount getUserAccount() {
        return useraccount;
    }

    public void setUserAccount(UserAccount useraccount) {
        this.useraccount = useraccount;
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
