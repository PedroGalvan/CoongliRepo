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
 * A Messagefolder.
 */
@Entity
@Table(name = "messagefolder")
public class Messagefolder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @NotNull
    @OneToMany(mappedBy = "messagefolder")
    @JsonIgnore
    private Set<Mesage> mesages = new HashSet<>();

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

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Set<Mesage> getMesagess() {
        return mesages;
    }

    public void setMessages(Set<Mesage> mesages) {
        this.mesages = mesages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Messagefolder messagefolder = (Messagefolder) o;
        if(messagefolder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, messagefolder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Messagefolder{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
