package com.coongli.forms;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.coongli.domain.Resourcecategory;


public class ResourceForm {
	
	private int id;
	private String title;
	private String description;
	private String link;
	private CommonsMultipartFile file;
	private Resourcecategory resourceCategory;

	@SafeHtml
	@NotBlank
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title=title;
	}
	
	@SafeHtml
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}

	@SafeHtml
	public String getLink(){
		return link;
	}
	public void setLink(String link){
		this.link=link;
	}
	

	public CommonsMultipartFile getFile(){
		return file;
	}
	public void setFile(CommonsMultipartFile file){
		this.file=file;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional=false)
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
