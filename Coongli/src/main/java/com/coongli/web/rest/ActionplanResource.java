package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Actionplan;
import com.coongli.service.ActionplanService;
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
 * REST controller for managing Actionplan.
 */
@RestController
@RequestMapping("/api")
public class ActionplanResource {

    private final Logger log = LoggerFactory.getLogger(ActionplanResource.class);
        
    @Inject
    private ActionplanService actionplanService;
    
    /**
     * POST  /actionplans -> Create a new actionplan.
     */
    @RequestMapping(value = "/actionplans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actionplan> createActionplan(@RequestBody Actionplan actionplan) throws URISyntaxException {
        log.debug("REST request to save Actionplan : {}", actionplan);
        if (actionplan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("actionplan", "idexists", "A new actionplan cannot already have an ID")).body(null);
        }
        Actionplan result = actionplanService.save(actionplan);
        return ResponseEntity.created(new URI("/api/actionplans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("actionplan", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actionplans -> Updates an existing actionplan.
     */
    @RequestMapping(value = "/actionplans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actionplan> updateActionplan(@RequestBody Actionplan actionplan) throws URISyntaxException {
        log.debug("REST request to update Actionplan : {}", actionplan);
        if (actionplan.getId() == null) {
            return createActionplan(actionplan);
        }
        Actionplan result = actionplanService.save(actionplan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("actionplan", actionplan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actionplans -> get all the actionplans.
     */
    @RequestMapping(value = "/actionplans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Actionplan>> getAllActionplans(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Actionplans");
        Page<Actionplan> page = actionplanService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/actionplans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /actionplans/:id -> get the "id" actionplan.
     */
    @RequestMapping(value = "/actionplans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Actionplan> getActionplan(@PathVariable Long id) {
        log.debug("REST request to get Actionplan : {}", id);
        Actionplan actionplan = actionplanService.findOne(id);
        return Optional.ofNullable(actionplan)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /actionplans/:id -> delete the "id" actionplan.
     */
    @RequestMapping(value = "/actionplans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActionplan(@PathVariable Long id) {
        log.debug("REST request to delete Actionplan : {}", id);
        actionplanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("actionplan", id.toString())).build();
    }
}
