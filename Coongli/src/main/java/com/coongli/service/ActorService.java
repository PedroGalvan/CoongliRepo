package com.coongli.service;

import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Actor;
import com.coongli.repository.ActorRepository;
import com.coongli.security.LoginService;

@Service
@Transactional
public class ActorService {
	
	//Managed repository -----------------------------------------------------
	
	@Inject
	private ActorRepository actorRepository;
	
	// Supporting services -----------------------------------------------------
	
	// Constructor -----------------------------------------------------
	
	public ActorService(){
		super();
	}
	
	// Simple CRUD methods -----------------------------------------------------
	
	public Collection<Actor> findAll(){
		Collection<Actor> result;
		
		result = actorRepository.findAll();
		
		return result;
	}
	
	public Actor findOne(int actorid){
		Assert.notNull(actorid);
		Actor result;
		
		result = actorRepository.findOne(actorid);
		
		return result;
	}

	public Actor findOneByPrincipal(){
		Actor result;
		
		result = actorRepository.findOneByPrincipal(LoginService.getPrincipal().getId());
		
		return result;
	}

	public Collection<Actor> findAllExceptMe() {
		Collection<Actor> result;
		
		result = actorRepository.findAllExceptMe(LoginService.getPrincipal().getId());
		
		return result;
	}

	// Other business methods -----------------------------------------------------
	
	
}
