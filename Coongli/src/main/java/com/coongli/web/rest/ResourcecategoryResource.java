package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Resourcecategory;
import com.coongli.service.ResourcecategoryService;
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
 * REST controller for managing Resourcecategory.
 */
@RestController
@RequestMapping("/api")
public class ResourcecategoryResource {

    private final Logger log = LoggerFactory.getLogger(ResourcecategoryResource.class);
        
    @Inject
    private ResourcecategoryService resourcecategoryService;
    
    /**
     * POST  /resourcecategorys -> Create a new resourcecategory.
     */
    @RequestMapping(value = "/resourcecategorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resourcecategory> createResourcecategory(@Valid @RequestBody Resourcecategory resourcecategory) throws URISyntaxException {
        log.debug("REST request to save Resourcecategory : {}", resourcecategory);
        if (resourcecategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("resourcecategory", "idexists", "A new resourcecategory cannot already have an ID")).body(null);
        }
        Resourcecategory result = resourcecategoryService.save(resourcecategory);
        return ResponseEntity.created(new URI("/api/resourcecategorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("resourcecategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resourcecategorys -> Updates an existing resourcecategory.
     */
    @RequestMapping(value = "/resourcecategorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resourcecategory> updateResourcecategory(@Valid @RequestBody Resourcecategory resourcecategory) throws URISyntaxException {
        log.debug("REST request to update Resourcecategory : {}", resourcecategory);
        if (resourcecategory.getId() == null) {
            return createResourcecategory(resourcecategory);
        }
        Resourcecategory result = resourcecategoryService.save(resourcecategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("resourcecategory", resourcecategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resourcecategorys -> get all the resourcecategorys.
     */
    @RequestMapping(value = "/resourcecategorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Resourcecategory>> getAllResourcecategorys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Resourcecategorys");
        Page<Resourcecategory> page = resourcecategoryService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/resourcecategorys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /resourcecategorys/:id -> get the "id" resourcecategory.
     */
    @RequestMapping(value = "/resourcecategorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Resourcecategory> getResourcecategory(@PathVariable Long id) {
        log.debug("REST request to get Resourcecategory : {}", id);
        Resourcecategory resourcecategory = resourcecategoryService.findOne(id);
        return Optional.ofNullable(resourcecategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /resourcecategorys/:id -> delete the "id" resourcecategory.
     */
    @RequestMapping(value = "/resourcecategorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteResourcecategory(@PathVariable Long id) {
        log.debug("REST request to delete Resourcecategory : {}", id);
        resourcecategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("resourcecategory", id.toString())).build();
    }
}
