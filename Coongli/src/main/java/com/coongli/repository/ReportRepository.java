package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer > {

	@Query("select distinct r from Report r  where (r.title like %?1% or r.description like %?1% " +
			"or r.resourceCategory.name like %?1% or r.resourceCategory.description like %?1% ) and r.resourceCategory.hidden = " +
			"FALSE and r.invoiceReport = true")
	Collection<Report> findAllByKeyWord(String kw);
	
	@Query("select us.report from User u join u.sessions us where us.report!=null and u.id=?1 order by us.report.creationMoment desc")
	Collection<Report> findAllByUser(long userId);
	
	@Query("select r from Report r where r.invoiceReport = true and r.id = ?1")
	Report findA(int reportId);
}