package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Adminis;
import com.coongli.service.AdminisService;
import com.coongli.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Adminis.
 */
@RestController
@RequestMapping("/api")
public class AdminisResource {

    private final Logger log = LoggerFactory.getLogger(AdminisResource.class);
        
    @Inject
    private AdminisService adminisService;
    
    /**
     * POST  /adminiss -> Create a new adminis.
     */
    @RequestMapping(value = "/adminiss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adminis> createAdminis(@RequestBody Adminis adminis) throws URISyntaxException {
        log.debug("REST request to save Adminis : {}", adminis);
        if (adminis.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("adminis", "idexists", "A new adminis cannot already have an ID")).body(null);
        }
        Adminis result = adminisService.save(adminis);
        return ResponseEntity.created(new URI("/api/adminiss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("adminis", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /adminiss -> Updates an existing adminis.
     */
    @RequestMapping(value = "/adminiss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adminis> updateAdminis(@RequestBody Adminis adminis) throws URISyntaxException {
        log.debug("REST request to update Adminis : {}", adminis);
        if (adminis.getId() == null) {
            return createAdminis(adminis);
        }
        Adminis result = adminisService.save(adminis);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("adminis", adminis.getId().toString()))
            .body(result);
    }

    /**
     * GET  /adminiss -> get all the adminiss.
     */
    @RequestMapping(value = "/adminiss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Adminis> getAllAdminiss() {
        log.debug("REST request to get all Adminiss");
        return adminisService.findAll();
            }

    /**
     * GET  /adminiss/:id -> get the "id" adminis.
     */
    @RequestMapping(value = "/adminiss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adminis> getAdminis(@PathVariable Long id) {
        log.debug("REST request to get Adminis : {}", id);
        Adminis adminis = adminisService.findOne(id);
        return Optional.ofNullable(adminis)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adminiss/:id -> delete the "id" adminis.
     */
    @RequestMapping(value = "/adminiss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAdminis(@PathVariable Long id) {
        log.debug("REST request to delete Adminis : {}", id);
        adminisService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("adminis", id.toString())).build();
    }
}
