package com.coongli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.validator.constraints.SafeHtml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
               inverseJoinColumns = @JoinColumn(name="users_id", referencedColumnName="ID"))
    private Collection<User> users = new ArrayList<>();

    @NotNull
    @OneToMany(mappedBy = "resourcecategory")
    @JsonIgnore
    private Collection<Resource> resources = new ArrayList<>();

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

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Resource> getResources() {
        return resources;
    }

    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
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
