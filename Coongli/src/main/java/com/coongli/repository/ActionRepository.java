package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Action;


@Repository
public interface ActionRepository extends JpaRepository<Action, Integer > {

	@Query("select a from Action a where a.user.id=?1 order by a.creationMoment desc")
	Collection<Action> findByOrder(long userId);
	
}