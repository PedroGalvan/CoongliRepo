package com.coongli.repository;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Session;
import com.coongli.domain.User;


@Repository
public interface SessionRepository extends JpaRepository<Session, Integer > {
	
	@Query("select s from Session s where s.startMoment < (current_timestamp) and s.hidden=false and s.accepted=true order by s.startMoment desc")
	Collection<Session> findPast();
	
	@Query("select s from Session s where s.id=?1")
	Session findA(long sessionId);
	
	@Query("select s from Session s where s.startMoment >= (current_timestamp) and s.hidden=false and s.accepted=true order by s.startMoment desc")
	Collection<Session> findFuture();
	
	@Query("select s from Session s where s.cancelled=true order by s.startMoment desc")
	Collection<Session> findCancelled();
	
	@Query("select s from Session s where s.accepted=false and s.cancelled=false order by s.startMoment desc")
	Collection<Session> findNotAccepted();
	
	@Query("select distinct s from Session s where (s.startMoment between ?1 and ?2) or (s.endMoment between ?1 and ?2)" +
			"or (s.startMoment < ?1 and s.endMoment > ?2) ")
	Collection<Session> sessionsBetween(Date d1, Date d2);
	
	@Query("select distinct sus from Session s join s.users sus where (s.startMoment between ?1 and ?2) or (s.endMoment between ?1 and ?2)" +
			"or (s.startMoment < ?1 and s.endMoment > ?2) ")
	Collection<User> usersSessionsBetween(Date d1, Date d2);
	
}