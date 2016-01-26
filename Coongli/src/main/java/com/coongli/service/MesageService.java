package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Actor;
import com.coongli.domain.Mesage;
import com.coongli.domain.Messagefolder;
import com.coongli.repository.MesageRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;


@Service
@Transactional
public class MesageService {

	// Managed repository -------------------------------------------------------
	@Inject		
	private MesageRepository mesageRepository;
	
	// Supporting services ------------------------------------------------------
	@Inject
	private ActorService actorService;

	@Inject
	private MessagefolderService messageFolderService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Mesage create(){
		Mesage result;
		Actor a,recipient;
		Messagefolder outbox;

		result= new Mesage();
		a = actorService.findOneByPrincipal();
		recipient = null;
		outbox = messageFolderService.findOneByName("Outbox",a);
		result.setSentmoment(new Date(System.currentTimeMillis()-1));
		result.setMessagefolder(outbox);
		result.setSender(a);
		result.setBody("");
		result.setRecipient(recipient);
		result.setSubject("");
		result.setSaw(true);
		
		return result;
	}
	
	public Mesage save(Mesage m){
		Assert.notNull(m);
		Mesage result;
		Collection<Mesage> mesages;
		Actor aSender,aRecipient;
		Messagefolder outbox,inbox;
		
		
		if(m.getId()==0){
			
		aSender = actorService.findOneByPrincipal();
		aRecipient = m.getRecipient();
		outbox = messageFolderService.findOneByName("Outbox",aSender);
		inbox = messageFolderService.findOneByName("Inbox",aRecipient);
		m.setSentmoment(new Date(System.currentTimeMillis() -1));
		
		mesages = aSender.getSentmesages();
		mesages.add(m);
		aSender.setSentmesages(mesages);

		mesages = aRecipient.getReceivedmesages();
		mesages.add(m);
		aRecipient.setReceivedmesages(mesages);
		
		mesages = outbox.getMesages();
		mesages.add(m);
		outbox.setMesages(mesages);
		
		mesages = inbox.getMesages();
		mesages.add(m);
		inbox.setMesages(mesages);
		
		Assert.isTrue(!m.getSender().equals(m.getRecipient()));
		
		messageFolderService.save(outbox);
		messageFolderService.save(inbox);
		result = mesageRepository.save(m);
		
		m.setMessagefolder(inbox);
		m.setSaw(false);
		result = mesageRepository.save(m);
		
		}else{
			
			result = mesageRepository.save(m);
		
		}
		return result;
	}
	
	public Mesage findOne(int mesId){
		Mesage result;
		
		result  = mesageRepository.findOne(mesId);
		
		return result;
	}
	
	public Collection<Mesage> findAll(){
		Collection<Mesage> result;
		
		result  = mesageRepository.findAll();
		
		return result;
	}
	
	public void delete(Mesage mesage){
		Assert.notNull(mesage);
		//checkIsOwner(mesage);	
		Assert.isTrue(mesage.getMessagefolder().getName().equals("Trashbox"));
		
		mesageRepository.delete(mesage);
	}
	
	//Other business methods ------------------------------------------------
	
	public void changeSaw(Mesage m){
		Assert.notNull(m);
		
		if(!m.getSaw()){
			m.setSaw(true);
			//save(m);
		}
		
	}
	
	public void sendAcceptInfo(Actor actor, Date date1){
		Mesage mesage;
		Messagefolder inbox;
		Actor sender;
		Collection<Mesage> mesages;
		String minuts;
		
		sender = actorService.findOneByPrincipal();
		inbox = messageFolderService.findOneByName("Inbox",actor);
		mesage = new Mesage();
		mesage.setRecipient(actor);
		if(date1.getMinutes()<10){
			minuts = "0" + date1.getMinutes();
		}else{
			minuts = ""+date1.getMinutes();
		}
		mesage.setBody("Your session on "+date1.getDate()+"/"+(date1.getMonth()+1)+"/"+(date1.getYear()+1900)+" "+date1.getHours()+":"+minuts+" has been confirmed.");
		mesage.setSubject("Session "+date1.getDate()+"/"+(date1.getMonth()+1)+"/"+(date1.getYear()+1900)+" Accepted");
		mesage.setSender(sender);
		mesage.setSentmoment(new Date(System.currentTimeMillis()-1));
		mesages = actor.getReceivedmesages();
		mesages.add(mesage);
		actor.setReceivedmesages(mesages);
		
		mesages = inbox.getMesages();
		mesages.add(mesage);
		inbox.setMesages(mesages);
		
		messageFolderService.save(inbox);
		mesage.setMessagefolder(inbox);
		mesageRepository.save(mesage);
		
	}
	public void sendCancelInfo(Actor actor, Date date1, Date date2){
		Mesage mesage;
		Messagefolder inbox;
		Actor sender;
		Collection<Mesage> mesages;
		
		sender = actorService.findOneByPrincipal();
		inbox = messageFolderService.findOneByName("Inbox",actor);
		mesage = new Mesage();
		mesage.setRecipient(actor);
		mesage.setBody("A future session programmed between the days "+ date1.getDate()+"/"+(date1.getMonth()+1)+"/"+(date1.getYear()+1900)+
				" and "+ date2.getDate()+"/"+(date2.getMonth()+1)+"/"+(date2.getYear()+1900)+" has been cancelled.");
		mesage.setSubject("Future Sessions Cancelled");
		mesage.setSender(sender);
		mesage.setSentmoment(new Date(System.currentTimeMillis()-1));
		mesages = actor.getReceivedmesages();
		mesages.add(mesage);
		actor.setReceivedmesages(mesages);
		
		mesages = inbox.getMesages();
		mesages.add(mesage);
		inbox.setMesages(mesages);
		
		messageFolderService.save(inbox);
		mesage.setMessagefolder(inbox);
		mesageRepository.save(mesage);
		
	}
	
	public Collection<Mesage> findMesageByMFOrder(int messageFolderId){
		Assert.notNull(messageFolderId);
		Collection<Mesage> result;
		
		result = mesageRepository.findMesageByMFOrder(messageFolderId);
		if(result==null){
			result = new ArrayList<Mesage>();
		}
		return result;
	}
	
	public void moveToTrashbox(Mesage mesage) {
		Assert.notNull(mesage);
		Collection<Mesage> mesages;
		Messagefolder messageFolder;
		
		messageFolder = messageFolderService.getTrashbox(mesage.getMessagefolder().getActor());
		mesages = messageFolder.getMesages();
		mesage.setMessagefolder(messageFolder);
		mesages.add(mesage);
		messageFolder.setMesages(mesages);
		
		//save(mesage);
		
	}

	public Mesage replyMesage(Mesage aEnviar, Mesage aResponder){
		Assert.notNull(aResponder);
		Assert.notNull(aEnviar);
		Assert.isTrue(!aEnviar.getSender().equals(aResponder.getSender()));
		Mesage result;
		Actor ac;
		String subject;
		
		result = aEnviar;
		ac = aResponder.getSender();
		
		if(aResponder.getSubject().contains("RES[")){
			subject = aResponder.getSubject();
		}else{
		subject = "RES["+aResponder.getSubject()+"]";
		}
		
		result.setRecipient(ac);
		result.setSubject(subject);
		return result;
	}

	public void checkIsOwner(Mesage mesage){
		UserAccount principal;
		UserAccount owner;
		
		principal = LoginService.getPrincipal();			
		owner = mesage.getMessagefolder().getActor().getUserAccount();
		
		Assert.isTrue(principal.equals(owner));
	}

}