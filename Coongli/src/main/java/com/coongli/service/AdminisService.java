package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Adminis;
import com.coongli.security.Authority;
import com.coongli.security.LoginService;
import com.coongli.domain.Mesage;
import com.coongli.domain.Messagefolder;
import com.coongli.repository.AdminisRepository;
import com.coongli.security.UserAccount;



@Service
@Transactional
public class AdminisService {

	//Managed repository -----------------------------------------------------
	
	@Autowired
	private AdminisRepository adminRepository;
	
	// Supporting services -----------------------------------------------------

	@Autowired		
	private MessagefolderService messageFolderService;
	// Constructor -----------------------------------------------------
	
	public AdminisService(){
		super();
	}
	
	// Simple CRUD methods -----------------------------------------------------
	
	public Adminis create(){
		Adminis result;
	    UserAccount userAccount;
	    Authority authority;
	    Collection<Authority> authorities;
	    Collection<Messagefolder> messageFolders;
		Collection<Mesage> receivedMesages;
		Collection<Mesage> sentMesages;
		
		messageFolders = new ArrayList<Messagefolder>();
		receivedMesages = new ArrayList<Mesage>();
		sentMesages = new ArrayList<Mesage>();
	    authority = new Authority();
		authorities = new ArrayList<Authority>();
		userAccount = new UserAccount();
		result = new Adminis();
		
		authority.setAuthority(Authority.ADMIN);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);
		result.setUserAccount(userAccount);
		result.setMessagefolders(messageFolders);
		result.setSentmesages(sentMesages);
		result.setReceivedmesages(receivedMesages);

		return result;
	}
	
	public Adminis save(Adminis admin){
		Assert.notNull(admin);
		
		Authority authority;
		authority = new Authority();
		authority.setAuthority("ADMIN");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(authority));
		
		Adminis result;
		Md5PasswordEncoder encoder;
		String password;
		String repeatPassword;
		UserAccount userAccount;
		Collection<Messagefolder> messageFolders;
		Messagefolder inbox, outbox, trashbox;
		
	    
		messageFolders = new ArrayList<Messagefolder>();
		inbox = messageFolderService.create("Inbox");
		outbox = messageFolderService.create("Outbox");
		trashbox = messageFolderService.create("Trashbox");
		messageFolders.add(inbox);
		messageFolders.add(outbox);
		messageFolders.add(trashbox);
		admin.setMessagefolders(messageFolders);
		
		userAccount = admin.getUserAccount();
		encoder = new Md5PasswordEncoder();
		password = encoder.encodePassword(userAccount.getPassword(), null);				
		repeatPassword = encoder.encodePassword(userAccount.getRepeatPassword(), null);
		
		Assert.isTrue(password.equals(repeatPassword)); 
		
		userAccount.setPassword(password);
		userAccount.setRepeatPassword(repeatPassword);
				
		admin.setUserAccount(userAccount);	
		
		result = adminRepository.save(admin);
		inbox.setActor(result);
		outbox.setActor(result);
		trashbox.setActor(result);
		messageFolderService.save(inbox);
		messageFolderService.save(outbox);
		messageFolderService.save(trashbox);
		
		return result;
	}

	public Adminis findOne(Integer adminId) {
		Assert.notNull(adminId);
		Adminis result;
		
		result = adminRepository.findOne(adminId);
		
		return result;
	}
	
	public Collection<Adminis> findAll(){
		Collection<Adminis> result;
		
		result = adminRepository.findAll();
		
		return result;
	}
	// Other business methods -----------------------------------------------------
/*	
	public Adminis findOneByPrincipal(){
		Adminis result;
		
		result = adminRepository.findOneByPrincipal(LoginService.getPrincipal().getId());
				
		return result;
	}
*/
}
