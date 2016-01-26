package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Resource;


@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer>{

	@Query("select distinct r from Resource r  where (r.title like %?1% or r.description like %?1% " +
			"or r.resourceCategory.name like %?1% or r.resourceCategory.description like %?1% ) and r.resourceCategory.hidden = " +
			"FALSE" )
	Collection<Resource> findAllByKeyWord(String kw);
	
	@Query("select distinct urr from User u join u.resourceCategories ur join ur.resources urr where u.id = ?1")
	Collection<Resource> findAllByUser(long userId);
	
	@Query("select r from Resource r where r.id = ?1")
	Resource findA(int resourceId);
	
	@Query("select distinct r from Resource r where r.invoiceReport = false")
	Collection<Resource> findAllOnlyResources();
	
	@Query("select distinct r from Resource r where r.resourceCategory.id = ?1")
	Collection<Resource> findAllByCategory(long categoryId);
	
	@Query("select distinct r.id from Resource r where r.resourceCategory.id = ?1")
	Collection<Integer> findAllIntsByCategory(long categoryId);
	
}
