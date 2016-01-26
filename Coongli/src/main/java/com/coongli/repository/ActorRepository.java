package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Actor;


@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer > {

	@Query("select a from Actor a where a.userAccount.id=?1")
	Actor findOneByPrincipal(int id);
	
	@Query("select a from Actor a where a.userAccount.id!=?1")
	Collection<Actor> findAllExceptMe(int userAccountId);
}