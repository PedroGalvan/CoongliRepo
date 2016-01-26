package com.coongli.repository;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Actionplan;


@Repository
public interface ActionplanRepository extends JpaRepository<Actionplan, Integer > {

	@Query("select distinct r from Actionplan r  where (r.title like %?1% or r.description like %?1% " +
			"or r.resourceCategory.name like %?1% or r.resourceCategory.description like %?1% ) and r.resourceCategory.hidden = " +
			"FALSE and r.invoiceReport = true")
	Collection<Actionplan> findAllByKeyWord(String kw);
	
	@Query("select p from Actionplan p where p.owner.id=?1")
	Actionplan findPlanByUser(long userId);
	
	@Query("select p from Actionplan p where p.owner.id=?1")
	Collection<Actionplan> findAllPlansByUser(long userId);
	
	@Query("delete from Actionplan where owner.id=?1 and id!=?2")
	Actionplan deleteRest(int userId,int planId);
}