package com.coongli.service;

import com.coongli.domain.Invoice;
import com.coongli.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);
    
    @Inject
    private InvoiceRepository invoiceRepository;
    
    /**
     * Save a invoice.
     * @return the persisted entity
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        Invoice result = invoiceRepository.save(invoice);
        return result;
    }

    /**
     *  get all the invoices.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Invoice> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        Page<Invoice> result = invoiceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one invoice by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Invoice findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        Invoice invoice = invoiceRepository.findOne(id);
        return invoice;
    }

    /**
     *  delete the  invoice by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.delete(id);
    }
}
