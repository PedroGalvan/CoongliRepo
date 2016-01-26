package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Actor;
import com.coongli.domain.Mesage;
import com.coongli.domain.Messagefolder;
import com.coongli.repository.MessagefolderRepository;



@Service
@Transactional
public class MessagefolderService {
	
	//Managed repository -----------------------------------------------------
	
	@Inject
	private MessagefolderRepository messageFolderRepository;
	
	// Supporting services -----------------------------------------------------

	// Constructor -----------------------------------------------------
	
	public MessagefolderService(){
		super();
	}
		
	// Simple CRUD methods -----------------------------------------------------
	
	public Messagefolder findOne(int messageFolderId){
		Assert.notNull(messageFolderId);
		Messagefolder result;
		
		result = messageFolderRepository.findOne(messageFolderId);
		
		return result;
	}
	
	public Messagefolder create(String name){
		Messagefolder result;
		Collection<Mesage> mesages;
		
		mesages = new ArrayList<Mesage>();
		result = new Messagefolder();
		result.setName(name);
		result.setMesages(mesages);
				
		return result;
	}
	
	public Messagefolder save(Messagefolder messageFolder){
		Assert.notNull(messageFolder);
		Messagefolder result;

		result = messageFolderRepository.save(messageFolder);
		
		return result;
	}
	
	// Other business methods -----------------------------------------------------

	public Messagefolder getTrashbox(Actor actor){
		Messagefolder result;
		
		result = null;
		for(Messagefolder messageFolder: actor.getMessagefolders()){
			if(messageFolder.getName().equals("Trashbox")){
				result = messageFolder;
				break;
			}
			
		}
		Assert.notNull(result);
		return result;
	}
	
	public Messagefolder findOneByName(String name,Actor a){
		Assert.notNull(name);
		Messagefolder result;
		
		result = null;
		for(Messagefolder mf:a.getMessagefolders()){
			if(mf.getName().equals(name)){
				result = mf;
				break;
			}
			
		}
		Assert.notNull(result);
		return result;
	}
}
