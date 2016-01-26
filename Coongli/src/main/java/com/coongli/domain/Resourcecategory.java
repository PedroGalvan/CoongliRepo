package com.coongli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Resourcecategory.
 */
@Entity
@Table(name = "resourcecategory")
public class Resourcecategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @SafeHtml
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "hidden")
    private Boolean hidden;
    
    @NotNull
    @ManyToMany
    @JoinTable(name = "resourcecategory_users",
               joinColumns = @JoinColumn(name="resourcecategorys_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="userss_id", referencedColumnName="ID"))
    private Set<User> userss = new HashSet<>();

    @NotNull
    @OneToMany(mappedBy = "resourcecategory")
    @JsonIgnore
    private Set<Resource> resourcess = new HashSet<>();

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

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHidden() {
        return hidden;
    }
    
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Set<User> getUserss() {
        return userss;
    }

    public void setUserss(Set<User> users) {
        this.userss = users;
    }

    public Set<Resource> getResourcess() {
        return resourcess;
    }

    public void setResourcess(Set<Resource> resources) {
        this.resourcess = resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resourcecategory resourcecategory = (Resourcecategory) o;
        if(resourcecategory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, resourcecategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resourcecategory{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", hidden='" + hidden + "'" +
            '}';
    }
}
