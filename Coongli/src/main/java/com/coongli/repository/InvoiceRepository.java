package com.coongli.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coongli.domain.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer > {

	@Query("select distinct r from Invoice r  where (r.title like %?1% or r.description like %?1% " +
			"or r.resourceCategory.name like %?1% or r.resourceCategory.description like %?1% ) and r.resourceCategory.hidden = " +
			"FALSE and r.invoiceReport = true")
	Collection<Invoice> findAllByKeyWord(String kw);
	
	@Query("select i from User u join u.invoices i where u.id=?1 order by i.creationMoment desc")
	Collection<Invoice> findAllByUser(long userId);
	
	@Query("select distinct i from Invoice i where i.resourceCategory.id = ?1")
	Collection<Invoice> findAllByCategory(long categoryId);

	@Query("select i from Invoice i where i.invoiceReport = true and i.id = ?1")
	Invoice findA(int invoiceId);
}