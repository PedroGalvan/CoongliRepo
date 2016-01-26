package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Invoice;
import com.coongli.service.InvoiceService;
import com.coongli.web.rest.util.HeaderUtil;
import com.coongli.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);
        
    @Inject
    private InvoiceService invoiceService;
    
    /**
     * POST  /invoices -> Create a new invoice.
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoice);
        if (invoice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invoice", "idexists", "A new invoice cannot already have an ID")).body(null);
        }
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity.created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invoice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoices -> Updates an existing invoice.
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}", invoice);
        if (invoice.getId() == null) {
            return createInvoice(invoice);
        }
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invoice", invoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoices -> get all the invoices.
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Invoice>> getAllInvoices(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Invoices");
        Page<Invoice> page = invoiceService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoices/:id -> get the "id" invoice.
     */
    @RequestMapping(value = "/invoices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Invoice invoice = invoiceService.findOne(id);
        return Optional.ofNullable(invoice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invoices/:id -> delete the "id" invoice.
     */
    @RequestMapping(value = "/invoices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invoice", id.toString())).build();
    }
}
