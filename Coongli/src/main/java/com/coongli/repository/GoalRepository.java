package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Goal;


@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer>{
	
	@Query("select g from Goal g where g.user.id=?1 order by g.creationMoment desc")
	Collection<Goal> findByOrder(long userId);
	
}
