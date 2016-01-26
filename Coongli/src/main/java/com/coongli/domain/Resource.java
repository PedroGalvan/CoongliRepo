package com.coongli.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SafeHtml
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;
    
    @SafeHtml
    @Column(name = "description", nullable = true)
    private String description;
    
    @SafeHtml
    @Column(name = "link", nullable = true)
    private String link;
    
    @SafeHtml
    @NotNull
    @Column(name = "doctype", nullable = false)
    private String doctype;
    
    @NotNull
   	@Past
   	@Temporal(TemporalType.TIMESTAMP)
   	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "creationmoment", nullable = false)
    private Date creationmoment;
    
    @Column(name = "invoicereport")
    private Boolean invoicereport;
    
    @Lob
    @Column(name = "file")
    private byte[] file;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "resourcecategory_id")
    private Resourcecategory resourcecategory;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getDoctype() {
        return doctype;
    }
    
    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }
    
    public Date getCreationmoment() {
        return creationmoment;
    }
    
    public void setCreationmoment(Date creationmoment) {
        this.creationmoment = creationmoment;
    }

    public Boolean getInvoicereport() {
        return invoicereport;
    }
    
    public void setInvoicereport(Boolean invoicereport) {
        this.invoicereport = invoicereport;
    }
    
    public byte[] getFile() {
        return file;
    }
    
    public void setFile(byte[] file) {
        this.file = file;
    }
    
    public Resourcecategory getResourcecategory() {
        return resourcecategory;
    }

    public void setResourcecategory(Resourcecategory resourcecategory) {
        this.resourcecategory = resourcecategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resource resource = (Resource) o;
        if(resource.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resource{" +
            "id=" + id +
            '}';
    }
}
