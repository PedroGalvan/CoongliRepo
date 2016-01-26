package com.coongli.forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.coongli.domain.Resourcecategory;
import com.coongli.domain.User;

public class AddUsersForm {

	private int id;

	// Attributes ----------------------------------------------------------
	
	private Collection<User> users;
	private User newUser;
	private Resourcecategory resourceCategory;
	
	
	@NotNull
	@Valid
	@ManyToMany
	public Collection<User> getUsers() {
		return users;
	}
	public void setUsers(Collection<User> users) {
		this.users = users;
	}
	
	@Valid
	@OneToOne(optional = true)
	public User getNewUser() {
		return newUser;
	}
	public void setNewUser(User newUser) {
		this.newUser = newUser;
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
