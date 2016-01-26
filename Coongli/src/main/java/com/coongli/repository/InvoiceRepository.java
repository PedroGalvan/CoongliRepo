package com.coongli.repository;

import com.coongli.domain.Invoice;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("select invoice from Invoice invoice where invoice.user.login = ?#{principal.username}")
    List<Invoice> findByUserIsCurrentUser();

}
