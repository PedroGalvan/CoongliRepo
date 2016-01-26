package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.security.Authority;
import com.coongli.domain.Invoice;
import com.coongli.domain.Resourcecategory;
import com.coongli.domain.User;
import com.coongli.repository.InvoiceRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;

@Service
@Transactional
public class InvoiceService {

	// Managed repository -------------------------------------------------------
	@Inject		
	private InvoiceRepository invoiceRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Inject
	private UserService userService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Invoice create(){
		Invoice result;

		result= new Invoice();
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setInvoicereport(true);
		result.setDoctype("PDF");
		
		return result;
	}
	
	public Invoice save(Invoice invoice){
		Assert.notNull(invoice);
		//checkIsAdmin();
		Invoice result;
			
		invoice.setCreationmoment(new Date(System.currentTimeMillis() -1));
		
		result = invoiceRepository.save(invoice);
		
		return result;
	}
	
	public Invoice findOne(int invoiceId){
		Invoice result;
		
		result  = invoiceRepository.findOne(invoiceId);
		
		return result;
	}
	
	public Collection<Invoice> findAll(){
		Collection<Invoice> result;
		
		result  = invoiceRepository.findAll();
		
		return result;
	}
	
	public void delete(Invoice invoice){
		Assert.notNull(invoice);
		//checkIsAdmin();	
		
		invoiceRepository.delete(invoice);
	}
	
	//Other business methods ------------------------------------------------
	
	public Invoice findA(int invoiceId){
		Invoice result;

		result  = invoiceRepository.findA(invoiceId);
		
		return result;
	}
	
	public Collection<Invoice> findAllByUser(){
		Collection<Invoice> result;
		User user;
		
		user = userService.findOneByPrincipal();
		result = invoiceRepository.findAllByUser(user.getId());
		
		return result;
	}
	
	public Collection<Invoice> findAllInvoicesByCategory(Resourcecategory resourceCategory){
		Collection<Invoice> result;
		
		result  = invoiceRepository.findAllByCategory(resourceCategory.getId());
		
		return result;
	}
	
	public Collection<Invoice> findAllInvoicesByUser(){
		Collection<Invoice> result;
		User user;
		
		user = userService.findOneByPrincipal();
		
		result  = invoiceRepository.findAllByUser(user.getId());
		
		return result;
	}
	
	public Collection<Invoice> findAllByKeyWord(String kw){
		Assert.notNull(kw);
		UserAccount principal;
		Authority a1,a2;
		Collection<Invoice> result,aux;
		
		a1 = new Authority();
		a1.setAuthority("ADMIN");
		
		a2 = new Authority();
		a2.setAuthority("USER");
		principal = LoginService.getPrincipal();
		if(principal.getAuthorities().contains(a2)){
			if(kw.equals("")){
				result = findAllInvoicesByUser();
			}else{
				result = invoiceRepository.findAllByKeyWord(kw);
			}
			aux = findAllInvoicesByUser();
			aux.removeAll(result);
			result.removeAll(aux);
		}else if(principal.getAuthorities().contains(a1)){
			if(kw.equals("")){
				result = findAll();
			}else{
				result = invoiceRepository.findAllByKeyWord(kw);
			}
		}else{
			result = new ArrayList<Invoice>();
		}
		
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