package com.coongli.forms;

import org.hibernate.validator.constraints.SafeHtml;

public class SearchForm {
	
	private int id;
	private int parametro; 
	private String valor;

	public int getParametro() {
		return parametro;
	}

	public void setParametro(int parametro) {
		this.parametro = parametro;
	}
	
	@SafeHtml
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
	
	
}
