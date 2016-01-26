package com.coongli.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coongli.domain.Actor;
import com.coongli.domain.Adminis;
import com.coongli.security.Authority;
import com.coongli.security.LoginService;
import com.coongli.security.UserAccount;
import com.coongli.domain.Invitation;
import com.coongli.domain.Session;
import com.coongli.domain.User;
import com.coongli.forms.SessionMomentForm;
import com.coongli.repository.SessionRepository;


@Service
@Transactional
public class SessionService {

	// Managed repository -------------------------------------------------------
	@Autowired		
	private SessionRepository sessionRepository;
	
	// Supporting services ------------------------------------------------------
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private AdminisService adminService;
	
	@Autowired
	private MesageService mesageService;
	
	// Simpled CRUD methods -----------------------------------------------------

	public Session create(){
		Session result;
		Collection<User> users;
		Collection<Invitation> invitations;
		Actor actor;
		Authority a;
		User creator;
		
		a = new Authority();
		a.setAuthority("USER");
		actor = actorService.findOneByPrincipal();
		users = new ArrayList<User>();
		invitations = new ArrayList<Invitation>();
		
		if(actor.getUserAccount().getAuthorities().contains(a)){
			creator = userService.findOneByPrincipal();
			users.add(creator);
		}
		
		result= new Session();
		result.setInvitations(invitations);
		result.setUsers(users);
		result.setPeriodica(false);
		result.setHidden(false);
		result.setCancelled(false);
		result.setAccepted(false);
		
		return result;
	}

	public Session save(Session session){
		Assert.notNull(session);
		Session result;
			
		result = sessionRepository.save(session);
		
		return result;
	}
	
	public Session findOne(long sessionId){
		Session result;
		
		result  = sessionRepository.findA(sessionId);
		
		return result;
	}
	
	public Collection<Session> findAll(){
		Collection<Session> result;
		
		result  = sessionRepository.findAll();
		
		return result;
	}
	
	public void delete(Session session){
		Assert.notNull(session);
		Assert.isTrue(session.getCancelled());
		//checkIsAdmin();	
		
		sessionRepository.delete(session);
	}
	
	//Other business methods ------------------------------------------------

	public void accept(Session session){
		Assert.notNull(session);
		UserAccount principal;
		Authority a1;
		
		a1 = new Authority();
		a1.setAuthority("ADMIN");
		
		principal = LoginService.getPrincipal();
	
		Assert.isTrue(principal.getAuthorities().contains(a1));
		session.setAccepted(true);
		sessionRepository.save(session);
		List<User> usuarios = new ArrayList<User>(session.getUsers());
		mesageService.sendAcceptInfo(usuarios.get(0),session.getStartmoment());
	}
	
	
	public void cancel(Session session){
		Assert.notNull(session);
		UserAccount principal;
		Authority a1,a2;
		User user;
		Collection<Adminis> admins;
		Collection<User> users;
		
		a1 = new Authority();
		a1.setAuthority("ADMIN");
		a2 = new Authority();
		a2.setAuthority("USER");
		
		principal = LoginService.getPrincipal();
		admins = adminService.findAll();
		users = session.getUsers();
		if(principal.getAuthorities().contains(a2)){
			user = userService.findOneByPrincipal();
			Assert.isTrue(session.getUsers().contains(user));
		}else{
			Assert.isTrue(principal.getAuthorities().contains(a1));
		}
		session.setCancelled(true);
		sessionRepository.save(session);
		for(Adminis a:admins){
			if(a.getUserAccount().getId()!=principal.getId()){
				mesageService.sendCancelInfo(a,session.getStartmoment(),session.getEndmoment());
			}
		}
		for(User u:users){
			if(u.getUserAccount().getId()!=principal.getId()){
			mesageService.sendCancelInfo(u,session.getStartmoment(),session.getEndmoment());
			}
		}
	}
	
	public void cancelDays(Date date1, Date date2){
		Assert.notNull(date1);
		Assert.notNull(date2);
		UserAccount principal;
		Authority a;
		Collection<Session> sessions;
		Collection<User> users;
		
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();
		Assert.isTrue(principal.getAuthorities().contains(a));
		sessions = sessionRepository.sessionsBetween(date1,date2);
		users = sessionRepository.usersSessionsBetween(date1,date2);
		for(Session s:sessions){
			s.setCancelled(true);
			sessionRepository.save(s);
			
		}
		for(User u:users){
			mesageService.sendCancelInfo(u,date1,date2);
		}
		
	}
	
	public void addUser(Session session, User user){
		Assert.notNull(session);
		Assert.notNull(user);
		Assert.isTrue(!session.getUsers().contains(user));
		Collection<User> users;
		Collection<Session> sessions;
		
		users = session.getUsers();
		users.add(user);
		session.setUsers(users);
		
		sessions = user.getSessions();
		sessions.add(session);
		user.setSessions(sessions);
		
		sessionRepository.save(session);
		
	}
	
	public void goOut(Session session){
		Assert.notNull(session);
		User user;
		Collection<User> users;
		Collection<Session> sessions;
		
		user = userService.findOneByPrincipal();
		Assert.isTrue(session.getUsers().contains(user) && session.getUsers().size()>1);
		users = session.getUsers();
		users.remove(user);
		session.setUsers(users);
		
		sessions = user.getSessions();
		sessions.remove(session);
		user.setSessions(sessions);
		
		sessionRepository.save(session);
		
	}
	
	public Collection<Session> findPastByUser(){
		Collection<Session> result,aux;
		User user;
		
		user = userService.findOneByPrincipal();
		if(sessionRepository.findPast()==null){
			result = new ArrayList<Session>();
		}else{
			result = user.getSessions();
			aux = new ArrayList<Session>(sessionRepository.findPast());
			aux.removeAll(result);
			result = new ArrayList<Session>(sessionRepository.findPast());
			result.removeAll(aux);
		}
		
		return result;
	}
	
	public Collection<Session> findAllPast(){
		Collection<Session> result;
		UserAccount principal;
		Authority a;
		
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();
		Assert.isTrue(principal.getAuthorities().contains(a));
		
		if(sessionRepository.findPast()==null){
			result = new ArrayList<Session>();
		}else{
			result = sessionRepository.findPast();
		}
		
		return result;
	}
	
	public Collection<Session> findFutureByUser(){
		Collection<Session> result,aux;
		User user;
		
		user = userService.findOneByPrincipal();
		if(sessionRepository.findFuture()==null){
			result = new ArrayList<Session>();
		}else{
			result = user.getSessions();
			aux = new ArrayList<Session>(sessionRepository.findFuture());
			aux.removeAll(result);
			result = new ArrayList<Session>(sessionRepository.findFuture());
			result.removeAll(aux);
		}
		
		return result;
	}
	
	public Collection<Session> findFutureByUserId(int userId){
		Collection<Session> result,aux;
		User user;
		
		user = userService.findOne(userId);
		if(sessionRepository.findFuture()==null){
			result = new ArrayList<Session>();
		}else{
			result = user.getSessions();
			aux = new ArrayList<Session>(sessionRepository.findFuture());
			aux.removeAll(result);
			result = new ArrayList<Session>(sessionRepository.findFuture());
			result.removeAll(aux);
			aux = sessionRepository.findCancelled();
			result.removeAll(aux);
		}
		
		return result;
	}
/*	
	public Collection<Session> findAllFuture(){
		Collection<Session> result;
		UserAccount principal;
		Authority a;
		
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();
		Assert.isTrue(principal.getAuthorities().contains(a));
		
		if(sessionRepository.findFuture()==null){
			result = new ArrayList<Session>();
		}else{
			result = sessionRepository.findFuture();
		}
		
		return result;
	}
	
	public Collection<Session> findAllNotAccepted(){
		Collection<Session> result;
		UserAccount principal;
		Authority a;
		
		a = new Authority();
		a.setAuthority("ADMIN");
		principal = LoginService.getPrincipal();
		Assert.isTrue(principal.getAuthorities().contains(a));
		
		if(sessionRepository.findNotAccepted()==null){
			result = new ArrayList<Session>();
		}else{
			result = sessionRepository.findNotAccepted();
		}
		
		return result;
	}
*/	
	public Collection<SessionMomentForm> momentsPerDay(Date d1){
		Assert.notNull(d1);
		int a,b;
		List<SessionMomentForm> result;
		Date aux1,aux2;
		boolean available;
		
		result = new ArrayList<SessionMomentForm>();
		aux1 = d1;
		aux2 = d1;
		for(int i=0;i<16;i++){
			SessionMomentForm rmfAux = new SessionMomentForm();
			a=8+i;
			b=9+i;
			aux1 = new Date(d1.getYear(),d1.getMonth(),d1.getDate(),a,0);
			aux2 = new Date(d1.getYear(),d1.getMonth(),d1.getDate(),b,0);
			rmfAux.setStartMoment(aux1);
			rmfAux.setEndMoment(aux2);
			available = fechaDisponible(aux1,aux2) && aux1.after(new Date());
			rmfAux.setAvailable(available);
			result.add(rmfAux);
		}
		
		return result;
	}
	
	public boolean fechaDisponible(Date d1, Date d2){
		Assert.notNull(d1);
		Assert.notNull(d2);
		Assert.isTrue(d1.before(d2));
		boolean result;
		Collection<Session> sessions;
		
		sessions = sessionRepository.sessionsBetween(d1, d2);
		result = sessions.isEmpty();
		
		return result;
	}
	
	public boolean cancel24h(Session session){
		Assert.notNull(session);
		boolean result;
		long diference;
		long hoursDiff;
		long minsDiff;
		Date dSession = session.getStartmoment();
		Date today = new Date();
		diference = dSession.getTime()-today.getTime();
		hoursDiff = TimeUnit.MILLISECONDS.toHours(diference);
		result = hoursDiff >= 24;
		
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