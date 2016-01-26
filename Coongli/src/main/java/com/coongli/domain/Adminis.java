package com.coongli.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Adminis.
 */
@Entity
@Table(name = "adminis")
public class Adminis extends Actor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Adminis adminis = (Adminis) o;
        if(adminis.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adminis.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Adminis{" +
            "id=" + id +
            '}';
    }
}
