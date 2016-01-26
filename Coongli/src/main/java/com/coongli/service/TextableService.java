package com.coongli.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Textable;
import com.coongli.repository.TextableRepository;


@Service
@Transactional
public class TextableService {
	
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private TextableRepository textableRepository;
	
	// Supporting services -----------------------------------------------------
	
	// Constructor -----------------------------------------------------
	
	public TextableService(){
		super();
	}
	
	// Simple CRUD methods -----------------------------------------------------
	
	public Collection<Textable> findAll(){
		Collection<Textable> result;
		
		result = textableRepository.findAll();
		
		return result;
	}
	
	public Textable findOne(int Textableid){
		Assert.notNull(Textableid);
		Textable result;
		
		result = textableRepository.findOne(Textableid);
		
		return result;
	}
	

	
	// Other business methods -----------------------------------------------------
	
	
}
