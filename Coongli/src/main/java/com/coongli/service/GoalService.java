package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Goal;
import com.coongli.domain.User;
import com.coongli.repository.GoalRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;


@Service
@Transactional
public class GoalService {

	// Managed repository -------------------------------------------------------
	
	@Inject		
	private GoalRepository goalRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Inject
	private UserService userService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Goal create(){
		Goal result;
		User creator;

		result= new Goal();
		creator = userService.findOneByPrincipal();
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setUser(creator);
		result.setCompleted(false);
		
		return result;
	}
	
	public Goal save(Goal goal){
		Assert.notNull(goal);
		Goal result;
		Collection<Goal> goals;
		User creator;
		
		creator = userService.findOneByPrincipal();
		goal.setCreationmoment(new Date(System.currentTimeMillis() -1));
		
		goals = creator.getGoals();
		goals.add(goal);
		creator.setGoals(goals);
		
		result = goalRepository.save(goal);
		
		return result;
	}
	
	public Goal findOne(int goalId){
		Goal result;
		
		result  = goalRepository.findOne(goalId);
		
		return result;
	}
	
	public Collection<Goal> findAll(){
		Collection<Goal> result;
		
		result  = goalRepository.findAll();
		
		return result;
	}
	
	public void complete(Goal goal){
		Assert.notNull(goal);
		if(goal.getCompleted()){
			goal.setCompleted(false);
		}else{
			goal.setCompleted(true);
		}
		goalRepository.save(goal);
	}
	
	public void delete(Goal goal){
		Assert.notNull(goal);
		//checkIsOwner(goal);	
		
		goalRepository.delete(goal);
	}
	
	//Other business methods ------------------------------------------------
	
	public Collection<Goal> findAllByUser(){
		Collection<Goal> result;
		User user;
		
		user = userService.findOneByPrincipal();
		result  = goalRepository.findByOrder(user.getId());
		
		return result;
	}
	
	public Collection<Goal> findThree(){
		List<Goal> result,aux;
		User user;
		int a;
		
		user = userService.findOneByPrincipal();
		aux  = new ArrayList<Goal>(goalRepository.findByOrder(user.getId()));
		result = new ArrayList<Goal>();
		if(aux.size()>0 && aux.size()<3){
			a = aux.size();
		}else{
			a = 3;
		}
		if(aux.size()>0){
			for(int i=0;i<a;i++){
				result.add(aux.get(i));
			}
		}
		return result;
	}

	public void checkIsOwner(Goal goal){
		UserAccount principal;
		UserAccount owner;
		
		principal = LoginService.getPrincipal();			
		owner = goal.getUser().getUserAccount();
		
		Assert.isTrue(principal.equals(owner));
	}

}