/* Credentials.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package com.coongli.security;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.SafeHtml;

public class Credentials {

	// Constructors -----------------------------------------------------------

	public Credentials() {
		super();
	}

	// Attributes -------------------------------------------------------------

	private String username;
	private String password;
	private String repeatPassword;

	@SafeHtml
	@Size(min = 5, max = 32)
	public String getUsername() {
		return username;
	}

	public void setJ_username(String username) {
		this.username = username;
	}

	@SafeHtml
	@Size(min = 5, max = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@SafeHtml
	@Size(min = 5, max = 32)
	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

}
