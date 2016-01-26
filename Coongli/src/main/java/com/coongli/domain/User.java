package com.coongli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.time.ZonedDateTime;

/**
 * A user.
 */
@Entity
@Table(name = "jhi_user")
public class User extends Actor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60) 
    @Column(name = "password_hash",length = 60)
    private String password;

    @SafeHtml
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;
    
    @SafeHtml
    @NotNull
    @Column(name = "city", nullable = false)
    private String city;
    
    @SafeHtml
    @NotNull
    @Column(name = "nationality", nullable = false)
    private String nationality;
    
    @SafeHtml
    @NotNull
    @Pattern(regexp = "[0-9A-Z][0-9]{7}[A-Z]")
    @Column(name = "dni", nullable = false)
    private String dni;
    
    @NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "birthdate", nullable = false)
    private Date birthdate;
    
    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    @Column(name = "reset_date", nullable = true)
    private ZonedDateTime resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<PersistentToken> persistentTokens = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Invoice> invoices = new ArrayList<>();
    
    @NotNull
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Action> actions = new ArrayList<>();
    
    @NotNull
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Collection<Goal> goals = new ArrayList<>();
    
    @NotNull
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Session> sessions = new ArrayList<>();
    
    @NotNull
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private Collection<Invitation> sentinvitations = new ArrayList<>();
    
    @NotNull
    @OneToMany(mappedBy = "recipient")
    @JsonIgnore
    private Collection<Invitation> receivedinvitations = new ArrayList<>();
    
    @NotNull
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Resourcecategory> resourcecategories = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public Date getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public ZonedDateTime getResetDate() {
       return resetDate;
    }

    public void setResetDate(ZonedDateTime resetDate) {
       this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<PersistentToken> getPersistentTokens() {
        return persistentTokens;
    }

    public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
        this.persistentTokens = persistentTokens;
    }
    
    public Collection<Action> getActions() {
        return actions;
    }

    public void setActions(Collection<Action> actions) {
        this.actions = actions;
    }
    
    public Collection<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Collection<Goal> goals) {
        this.goals = goals;
    }
    
    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        this.invoices = invoices;
    }
    
    public Collection<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Collection<Session> sessions) {
        this.sessions = sessions;
    }
    
    public Collection<Invitation> getSentinvitations() {
        return sentinvitations;
    }

    public void setSentinvitations(Collection<Invitation> sentinvitations) {
        this.sentinvitations = sentinvitations;
    }
    
    public Collection<Invitation> getReceivedinvitations() {
        return receivedinvitations;
    }

    public void setReceivedinvitations(Collection<Invitation> receivedinvitations) {
        this.receivedinvitations = receivedinvitations;
    }
    
    public Collection<Resourcecategory> getResourcecategories() {
        return resourcecategories;
    }

    public void setResourcecategories(Collection<Resourcecategory> resourcecategories) {
        this.resourcecategories = resourcecategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!login.equals(user.login)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
