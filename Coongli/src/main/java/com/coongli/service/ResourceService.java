package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.security.Authority;
import com.coongli.domain.Resource;
import com.coongli.domain.Resourcecategory;
import com.coongli.domain.User;
import com.coongli.repository.ResourceRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;


@Service
@Transactional
public class ResourceService {

	// Managed repository -------------------------------------------------------
	@Autowired		
	private ResourceRepository resourceRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Autowired		
	private UserService userService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Resource create(Resourcecategory resourceCategory){
		Resource result;

		result= new Resource();
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setResourcecategory(resourceCategory);
		result.setInvoicereport(false);
		result.setDoctype("-");
		
		return result;
	}
	
	public Resource save(Resource resource){
		Assert.notNull(resource);
		Resource result;

		resource.setCreationmoment(new Date(System.currentTimeMillis() -1));
		result = resourceRepository.save(resource);
		
		return result;
		
	}
	
	public String standardUrl(String link){
		String result;
		if(link.startsWith("http")){
			result = link;
		}else if(link.startsWith("www")){
			result = "http://"+link;
		}else{
			result = "http://www."+link;
		}
		return result;
	}
	
	public Resource findOne(int resourceId){
		Resource result;
		
		result  = resourceRepository.findOne(resourceId);
		
		return result;
	}
	
	public Collection<Resource> findAll(){
		Collection<Resource> result;
		
		result  = resourceRepository.findAll();
		
		return result;
	}
	
	public void delete(Resource resource){
		Assert.notNull(resource);
		//checkIsAdmin();	
		
		resourceRepository.delete(resource);
	}
	
	//Other business methods ------------------------------------------------
	
	public Collection<Resource> findAllResourcesByUser(){
		Collection<Resource> result;
		User user;
		
		user = userService.findOneByPrincipal();
		
		result  = resourceRepository.findAllByUser(user.getId());
		
		return result;
	}
	
	public Collection<Resource> findAllOnlyResources(){
		Collection<Resource> result;
		
		result  = resourceRepository.findAllOnlyResources();
		
		return result;
	}
	
	public Resource findA(int resourceId){
		Resource result;

		result  = resourceRepository.findA(resourceId);
		
		return result;
	}
	
	public Collection<Resource> findAllResourcesByCategory(Resourcecategory resourceCategory){
		Collection<Resource> result;
		
		result  = resourceRepository.findAllByCategory(resourceCategory.getId());
		
		return result;
	}
	
	public Collection<Integer> findAllResourcesIntsByCategory(Resourcecategory resourceCategory){
		Collection<Integer> result;
		
		result  = resourceRepository.findAllIntsByCategory(resourceCategory.getId());
		
		return result;
	}
	
	public void moveResource(Resource resource,Resourcecategory resourceCategoryNew){
		
		resource.setResourcecategory(resourceCategoryNew);
		resourceRepository.save(resource);
		
	}
	
	public void checkIsAdmin(){
		UserAccount principal;
		Authority a;
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();			
		
		Assert.isTrue(principal.getAuthorities().contains(a));
	}
	
	public Collection<Resource> findAllByKeyWord(String kw){
		Assert.notNull(kw);
		UserAccount principal;
		Authority a1,a2;
		Collection<Resource> result,aux;
		
		a1 = new Authority();
		a1.setAuthority("ADMIN");
		
		a2 = new Authority();
		a2.setAuthority("USER");
		principal = LoginService.getPrincipal();
		if(principal.getAuthorities().contains(a2)){
			if(kw.equals("")){
				result = findAllResourcesByUser();
			}else{
				result = resourceRepository.findAllByKeyWord(kw);
			}
			aux = findAllResourcesByUser();
			aux.removeAll(result);
			result.removeAll(aux);
		}else if(principal.getAuthorities().contains(a1)){
			if(kw.equals("")){
				result = findAll();
			}else{
				result = resourceRepository.findAllByKeyWord(kw);
			}
		}else{
			result = new ArrayList<Resource>();
		}
		
		return result;
	}
	
	public boolean check(Resource resource){
		boolean result;
		result = true;
		
		if(resource.getResourcecategory().equals(null)){
			result = false;
		}
		if(resource.getCreationmoment().equals(null)){
			result = false;
		}
		if(resource.getInvoicereport().equals(null)){
			result = false;
		}
		if(resource.getTitle().equals(null) || resource.getTitle().equals("")){
			result = false;
		}
		if(!resource.getFile().equals(null)){
			if(resource.getDoctype().equals(null) || resource.getDoctype().equals("")){
				result = false;
			}
		}
		
		
		return result;
	}
	
}