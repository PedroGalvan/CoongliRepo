package com.coongli.service;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Actionplan;
import com.coongli.security.Authority;
import com.coongli.domain.Resourcecategory;
import com.coongli.domain.User;
import com.coongli.repository.ActionplanRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;



@Service
@Transactional
public class ActionplanService {

	// Managed repository -------------------------------------------------------
	@Autowired		
	private ActionplanRepository actionPlanRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Autowired		
	private UserService userService;
	
	@Autowired		
	private ResourcecategoryService resourceCategoryService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Actionplan create(){
		Actionplan result;
		User user;
		Resourcecategory resourceCategory;
		
		user = userService.findOneByPrincipal();
		result= new Actionplan();
		resourceCategory = resourceCategoryService.findPlanes();
		result.setResourcecategory(resourceCategory);
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setInvoicereport(true);
		result.setOwner(user);
		return result;
	}
	
	public Actionplan save(Actionplan actionPlan){
		Assert.notNull(actionPlan);
		Actionplan result;
		User user;
		
		user = userService.findOneByPrincipal();
		Assert.isTrue(actionPlan.getOwner().equals(user));
		actionPlan.setCreationmoment(new Date(System.currentTimeMillis() -1));
		
		result = actionPlanRepository.save(actionPlan);
		deleteRest(result);
		return result;
	}
	
	public Actionplan findOne(int actionPlanId){
		Actionplan result;
		
		result  = actionPlanRepository.findOne(actionPlanId);
		
		return result;
	}
	
	public Collection<Actionplan> findAll(){
		Collection<Actionplan> result;
		
		result  = actionPlanRepository.findAll();
		
		return result;
	}
	
	public void delete(Actionplan actionPlan){
		Assert.notNull(actionPlan);
		actionPlanRepository.delete(actionPlan);
	}
	
	//Other business methods ------------------------------------------------
	
	public void deleteRest(Actionplan actionPlan){
		Assert.notNull(actionPlan);
		Collection<Actionplan> plans;
		User user;
		
		user = userService.findOneByPrincipal();
		plans  = actionPlanRepository.findAllPlansByUser(user.getId());
		for(Actionplan ap:plans){
			if(!ap.equals(actionPlan))
				delete(ap);
		}
		
	}
	
	public Actionplan findPlanByUser(){
		Actionplan result;
		User user;
		
		user = userService.findOneByPrincipal();
		result = actionPlanRepository.findPlanByUser(user.getId());
		
		return result;
	}
	
	public Actionplan findPlanByUserId(int userId){
		Actionplan result;
		
		result = actionPlanRepository.findPlanByUser(userId);
		
		return result;
	}
	
	public void checkIsAdmin(){
		UserAccount principal;
		Authority a;
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();			
		
		Assert.isTrue(principal.getAuthorities().contains(a));
	}

}