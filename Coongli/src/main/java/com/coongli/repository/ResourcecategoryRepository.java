package com.coongli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Resourcecategory;


@Repository
public interface ResourcecategoryRepository extends JpaRepository<Resourcecategory, Integer>{

	@Query("select distinct rc from Resourcecategory rc where rc.name = 'Invoices'")
	Resourcecategory findInvoice();
	
	@Query("select distinct rc from Resourcecategory rc where rc.name = 'ActionPlans'")
	Resourcecategory findPlanes();
	
	@Query("select distinct rc from Resourcecategory rc where rc.name = 'Reports'")
	Resourcecategory findReport();
}
