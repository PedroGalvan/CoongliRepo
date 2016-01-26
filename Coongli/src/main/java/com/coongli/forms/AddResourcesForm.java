package com.coongli.forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.coongli.domain.Resourcecategory;


public class AddResourcesForm {

	private int id;

	// Attributes ----------------------------------------------------------
	
	private Collection<Integer> resources;
	private Integer newr;
	private Resourcecategory resourceCategory;
	
	
	@NotNull

	@ManyToMany
	public Collection<Integer> getResources() {
		return resources;
	}
	public void setResources(Collection<Integer> resources) {
		this.resources = resources;
	}
	
	@OneToOne(optional = true)
	public Integer getNewr() {
		return newr;
	}
	public void setNewr(Integer newr) {
		this.newr = newr;
	}
	
	@Valid
	@OneToOne(optional = false)
	public Resourcecategory getResourceCategory() {
		return resourceCategory;
	}
	public void setResourceCategory(Resourcecategory resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
	
	
}
