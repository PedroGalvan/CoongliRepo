package com.coongli.forms;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.coongli.domain.Invitation;
import com.coongli.domain.Invoice;
import com.coongli.domain.Report;
import com.coongli.domain.User;


public class SessionForm {

	private int id;

	// Attributes ----------------------------------------------------------
	
	private int day;
	private int month;
	private int year;
	private int startMoment;
	private int endMoment;
	private Boolean periodica;
	private Boolean cancelled;
	private Boolean hidden;
	private Collection<Invitation> invitations;
	private Collection<User> users;
	private Invoice invoice;
	private Report report;
	
	@NotNull
	public Boolean getPeriodica() {
		return periodica;
	}
	public void setPeriodica(Boolean periodica) {
		this.periodica = periodica;
	}		
	
	@NotNull
	public Boolean getCancelled() {
		return cancelled;
	}
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}	
	
	@NotNull
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}	
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}	
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}	
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}	
	
	public int getStartMoment() {
		return startMoment;
	}

	public void setStartMoment(int startMoment) {
		this.startMoment = startMoment;
	}	
	
	public int getEndMoment() {
		return endMoment;
	}

	public void setEndMoment(int endMoment) {
		this.endMoment = endMoment;
	}
	
	@NotNull
	@Valid
	@OneToMany(mappedBy = "session")
	public Collection<Invitation> getInvitations() {
		return invitations;
	}
	public void setInvitations(Collection<Invitation> invitations) {
		this.invitations = invitations;
	}	
	
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
	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	@Valid
	@OneToOne(optional = true)
	public Report getReport() {
		return report;
	}
	public void setReport(Report report) {
		this.report = report;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
	
	
}
