package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Invitation;
import com.coongli.domain.Session;
import com.coongli.domain.User;
import com.coongli.repository.InvitationRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;



@Service
@Transactional
public class InvitationService {

	// Managed repository -------------------------------------------------------
	@Autowired		
	private InvitationRepository invitationRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionService sessionService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Invitation create(Session session){
		Invitation result;
		User creator;

		result= new Invitation();
		creator = userService.findOneByPrincipal();
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setSender(creator);
		result.setAccepted(false);
		result.setRejected(false);
		result.setSession(session);
		
		return result;
	}
	
	public Invitation save(Invitation invitation){
		Assert.notNull(invitation);
		Invitation result;
		Session session;
		Collection<Invitation> sentInvitations,receivedInvitations,invitations;
		User creator, recipient;
		
		session = sessionService.findOne(invitation.getSession().getId());
		creator = userService.findOneByPrincipal();
		recipient = invitation.getRecipient();
		Assert.isTrue(noMoreInvitations(recipient,session));
		invitation.setCreationmoment(new Date(System.currentTimeMillis() -10));
		
		sentInvitations = creator.getSentinvitations();
		sentInvitations.add(invitation);
		creator.setSentinvitations(sentInvitations);

		receivedInvitations = recipient.getReceivedinvitations();
		receivedInvitations.add(invitation);
		recipient.setReceivedinvitations(receivedInvitations);
		
		invitations = session.getInvitations();
		invitations.add(invitation);
		session.setInvitations(invitations);
		
		session = sessionService.save(session);
		
		invitation.setSender(creator);
		invitation.setRecipient(recipient);
		invitation.setSession(session);
		result = invitationRepository.save(invitation);
		
		return result;
	}
	
	public Invitation findOne(int invitationId){
		Invitation result;
		
		result  = invitationRepository.findOne(invitationId);
		
		return result;
	}
	
	public Collection<Invitation> findAll(){
		Collection<Invitation> result;
		
		result  = invitationRepository.findAll();
		
		return result;
	}
	
	public void delete(Invitation invitation){
		Assert.notNull(invitation);
		Assert.isTrue(invitation.getAccepted() || invitation.getRejected());
		//checkIsParticipant(invitation);	
		
		invitationRepository.delete(invitation);
	}
	
	//Other business methods ------------------------------------------------
	
	public void accept(Invitation invitation){
		Assert.notNull(invitation);
		//checkIsRecipient(invitation);	
		
		invitation.setAccepted(true);
		sessionService.addUser(invitation.getSession(),invitation.getRecipient());
		invitationRepository.save(invitation);
	}
	
	public void reject(Invitation invitation){
		Assert.notNull(invitation);
		//checkIsRecipient(invitation);	
		
		invitation.setRejected(true);
		invitationRepository.save(invitation);
	}
	
	public Collection<Invitation> findPending(){
		User user;
		Collection<Invitation> result;
		
		user = userService.findOneByPrincipal();
		result = invitationRepository.findPendingByUser(user.getId());

		if(result==null){
			result = new ArrayList<Invitation>();
		}
		
		return result;
	}
	
	public Collection<Invitation> findSent(){
		User user;
		Collection<Invitation> result;
		
		user = userService.findOneByPrincipal();
		result = invitationRepository.findSentByUser(user.getId());

		if(result==null){
			result = new ArrayList<Invitation>();
		}
		
		return result;
	}
	
	public Collection<Invitation> findAnswered(){
		User user;
		Collection<Invitation> result;
		
		user = userService.findOneByPrincipal();
		result = invitationRepository.findAnsweredByUser(user.getId());

		if(result==null){
			result = new ArrayList<Invitation>();
		}
		
		return result;
	}
	
	public void checkIsOwner(Invitation invitation){
		UserAccount principal;
		UserAccount owner;
		
		principal = LoginService.getPrincipal();			
		owner = invitation.getSender().getUserAccount();
		
		Assert.isTrue(principal.equals(owner));
	}
	
	public void checkIsParticipant(Invitation invitation){
		UserAccount principal;
		UserAccount owner,recipient;
		
		principal = LoginService.getPrincipal();			
		owner = invitation.getSender().getUserAccount();
		recipient = invitation.getRecipient().getUserAccount();
		
		Assert.isTrue(principal.equals(owner) || principal.equals(recipient));
	}

	public boolean noMoreInvitations(User user, Session session){
		Assert.notNull(user);
		Assert.notNull(session);
		boolean result;
		Collection<Invitation> invitations;
		
		invitations = invitationRepository.findInvitationsToSession(user.getId(), session.getId());
		
		result= invitations.isEmpty();
		
		return result;
	}
	
	public void checkIsRecipient(Invitation invitation){
		UserAccount principal;
		UserAccount recipient;
		
		principal = LoginService.getPrincipal();			
		recipient = invitation.getRecipient().getUserAccount();
		
		Assert.isTrue(principal.equals(recipient));
	}

}