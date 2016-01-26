package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Mesage;
import com.coongli.service.MesageService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Mesage.
 */
@RestController
@RequestMapping("/api")
public class MesageResource {

    private final Logger log = LoggerFactory.getLogger(MesageResource.class);
        
    @Inject
    private MesageService mesageService;
    
    /**
     * POST  /mesages -> Create a new mesage.
     */
    @RequestMapping(value = "/mesages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mesage> createMesage(@Valid @RequestBody Mesage mesage) throws URISyntaxException {
        log.debug("REST request to save Mesage : {}", mesage);
        if (mesage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mesage", "idexists", "A new mesage cannot already have an ID")).body(null);
        }
        Mesage result = mesageService.save(mesage);
        return ResponseEntity.created(new URI("/api/mesages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mesage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mesages -> Updates an existing mesage.
     */
    @RequestMapping(value = "/mesages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mesage> updateMesage(@Valid @RequestBody Mesage mesage) throws URISyntaxException {
        log.debug("REST request to update Mesage : {}", mesage);
        if (mesage.getId() == null) {
            return createMesage(mesage);
        }
        Mesage result = mesageService.save(mesage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mesage", mesage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mesages -> get all the mesages.
     */
    @RequestMapping(value = "/mesages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Mesage>> getAllMesages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Mesages");
        Page<Mesage> page = mesageService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mesages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /mesages/:id -> get the "id" mesage.
     */
    @RequestMapping(value = "/mesages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Mesage> getMesage(@PathVariable Long id) {
        log.debug("REST request to get Mesage : {}", id);
        Mesage mesage = mesageService.findOne(id);
        return Optional.ofNullable(mesage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mesages/:id -> delete the "id" mesage.
     */
    @RequestMapping(value = "/mesages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMesage(@PathVariable Long id) {
        log.debug("REST request to delete Mesage : {}", id);
        mesageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mesage", id.toString())).build();
    }
}
