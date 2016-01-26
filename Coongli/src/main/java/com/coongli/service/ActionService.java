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

import com.coongli.domain.Action;
import com.coongli.domain.User;
import com.coongli.repository.ActionRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;


@Service
@Transactional
public class ActionService {

	// Managed repository -------------------------------------------------------
	
	@Inject		
	private ActionRepository actionRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Inject
	private UserService userService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Action create(){
		Action result;
		User creator;

		result= new Action();
		creator = userService.findOneByPrincipal();
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setUser(creator);
		result.setCompleted(false);
		
		return result;
	}
	
	public Action save(Action action){
		Assert.notNull(action);
		Action result;
		Collection<Action> actions;
		User creator;
		
		creator = userService.findOneByPrincipal();
		action.setCreationmoment(new Date(System.currentTimeMillis() -1));
		
		actions = creator.getActions();
		actions.add(action);
		creator.setActions(actions);
		
		result = actionRepository.save(action);
		
		return result;
	}
	
	public Action findOne(int actionId){
		Action result;
		
		result  = actionRepository.findOne(actionId);
		
		return result;
	}
	
	public Collection<Action> findAll(){
		Collection<Action> result;
		
		result  = actionRepository.findAll();
		
		return result;
	}
	
	public void complete(Action action){
		Assert.notNull(action);
		if(action.getCompleted()){
			action.setCompleted(false);
		}else{
			action.setCompleted(true);
		}
		actionRepository.save(action);
	}
	
	public void delete(Action action){
		Assert.notNull(action);
		//checkIsOwner(action);	
		
		actionRepository.delete(action);
	}
	
	//Other business methods ------------------------------------------------
	
	public Collection<Action> findAllByUser(){
		Collection<Action> result;
		User user;
		
		user = userService.findOneByPrincipal();
		result  = actionRepository.findByOrder(user.getId());
		
		return result;
	}
	
	public Collection<Action> findThree(){
		List<Action> result,aux;
		User user;
		int a;
		
		user = userService.findOneByPrincipal();
		aux  = new ArrayList<Action>(actionRepository.findByOrder(user.getId()));
		result = new ArrayList<Action>();
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
	
	public void checkIsOwner(Action action){
		UserAccount principal;
		UserAccount owner;
		
		principal = LoginService.getPrincipal();			
		owner = action.getUser().getUserAccount();
		
		Assert.isTrue(principal.equals(owner));
	}

}