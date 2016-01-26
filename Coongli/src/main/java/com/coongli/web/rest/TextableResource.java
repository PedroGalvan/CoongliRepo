package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Textable;
import com.coongli.service.TextableService;
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
 * REST controller for managing Textable.
 */
@RestController
@RequestMapping("/api")
public class TextableResource {

    private final Logger log = LoggerFactory.getLogger(TextableResource.class);
        
    @Inject
    private TextableService textableService;
    
    /**
     * POST  /textables -> Create a new textable.
     */
    @RequestMapping(value = "/textables",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Textable> createTextable(@RequestBody Textable textable) throws URISyntaxException {
        log.debug("REST request to save Textable : {}", textable);
        if (textable.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("textable", "idexists", "A new textable cannot already have an ID")).body(null);
        }
        Textable result = textableService.save(textable);
        return ResponseEntity.created(new URI("/api/textables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("textable", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /textables -> Updates an existing textable.
     */
    @RequestMapping(value = "/textables",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Textable> updateTextable(@RequestBody Textable textable) throws URISyntaxException {
        log.debug("REST request to update Textable : {}", textable);
        if (textable.getId() == null) {
            return createTextable(textable);
        }
        Textable result = textableService.save(textable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("textable", textable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /textables -> get all the textables.
     */
    @RequestMapping(value = "/textables",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Textable>> getAllTextables(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Textables");
        Page<Textable> page = textableService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/textables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /textables/:id -> get the "id" textable.
     */
    @RequestMapping(value = "/textables/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Textable> getTextable(@PathVariable Long id) {
        log.debug("REST request to get Textable : {}", id);
        Textable textable = textableService.findOne(id);
        return Optional.ofNullable(textable)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /textables/:id -> delete the "id" textable.
     */
    @RequestMapping(value = "/textables/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTextable(@PathVariable Long id) {
        log.debug("REST request to delete Textable : {}", id);
        textableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("textable", id.toString())).build();
    }
}
