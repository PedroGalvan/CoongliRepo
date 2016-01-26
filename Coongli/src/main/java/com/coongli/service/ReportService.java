package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.security.Authority;
import com.coongli.domain.Report;
import com.coongli.domain.Session;
import com.coongli.domain.User;
import com.coongli.repository.ReportRepository;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;


@Service
@Transactional
public class ReportService {

	// Managed repository -------------------------------------------------------
	@Autowired		
	private ReportRepository reportRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Autowired		
	private UserService userService;
	
	// Simpled CRUD methods -----------------------------------------------------
	
	public Report create(Session session){
		Report result;

		result= new Report();
		result.setCreationmoment(new Date(System.currentTimeMillis()-1));
		result.setInvoicereport(true);
		result.setSession(session);
		return result;
	}
	
	public Report save(Report report){
		Assert.notNull(report);
		//checkIsAdmin();
		Report result;
			
		report.setCreationmoment(new Date(System.currentTimeMillis() -1));
		
		result = reportRepository.save(report);
		
		return result;
	}
	
	public Report findOne(int reportId){
		Report result;
		
		result  = reportRepository.findOne(reportId);
		
		return result;
	}
	
	public Collection<Report> findAll(){
		Collection<Report> result;
		
		result  = reportRepository.findAll();
		
		return result;
	}
	
	//Other business methods ------------------------------------------------
	
	public Report findA(int reportId){
		Report result;

		result  = reportRepository.findA(reportId);
		
		return result;
	}
	
	public Collection<Report> findAllByUser(){
		Collection<Report> result;
		User user;
		
		user = userService.findOneByPrincipal();
		result = reportRepository.findAllByUser(user.getId());
		
		return result;
	}
	
	
	public Collection<Report> findAllByKeyWord(String kw){
		Assert.notNull(kw);
		UserAccount principal;
		Authority a1,a2;
		Collection<Report> result,aux;
		
		a1 = new Authority();
		a1.setAuthority("ADMIN");
		
		a2 = new Authority();
		a2.setAuthority("USER");
		principal = LoginService.getPrincipal();
		if(principal.getAuthorities().contains(a2)){
			if(kw.equals("")){
				result = findAllByUser();
			}else{
				result = reportRepository.findAllByKeyWord(kw);
			}
			aux = findAllByUser();
			aux.removeAll(result);
			result.removeAll(aux);
		}else if(principal.getAuthorities().contains(a1)){
			if(kw.equals("")){
				result = findAll();
			}else{
				result = reportRepository.findAllByKeyWord(kw);
			}
		}else{
			result = new ArrayList<Report>();
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