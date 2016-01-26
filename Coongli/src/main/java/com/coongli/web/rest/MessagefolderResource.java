package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Messagefolder;
import com.coongli.service.MessagefolderService;
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
 * REST controller for managing Messagefolder.
 */
@RestController
@RequestMapping("/api")
public class MessagefolderResource {

    private final Logger log = LoggerFactory.getLogger(MessagefolderResource.class);
        
    @Inject
    private MessagefolderService messagefolderService;
    
    /**
     * POST  /messagefolders -> Create a new messagefolder.
     */
    @RequestMapping(value = "/messagefolders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messagefolder> createMessagefolder(@Valid @RequestBody Messagefolder messagefolder) throws URISyntaxException {
        log.debug("REST request to save Messagefolder : {}", messagefolder);
        if (messagefolder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("messagefolder", "idexists", "A new messagefolder cannot already have an ID")).body(null);
        }
        Messagefolder result = messagefolderService.save(messagefolder);
        return ResponseEntity.created(new URI("/api/messagefolders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("messagefolder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messagefolders -> Updates an existing messagefolder.
     */
    @RequestMapping(value = "/messagefolders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messagefolder> updateMessagefolder(@Valid @RequestBody Messagefolder messagefolder) throws URISyntaxException {
        log.debug("REST request to update Messagefolder : {}", messagefolder);
        if (messagefolder.getId() == null) {
            return createMessagefolder(messagefolder);
        }
        Messagefolder result = messagefolderService.save(messagefolder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("messagefolder", messagefolder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messagefolders -> get all the messagefolders.
     */
    @RequestMapping(value = "/messagefolders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Messagefolder>> getAllMessagefolders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Messagefolders");
        Page<Messagefolder> page = messagefolderService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/messagefolders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /messagefolders/:id -> get the "id" messagefolder.
     */
    @RequestMapping(value = "/messagefolders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Messagefolder> getMessagefolder(@PathVariable Long id) {
        log.debug("REST request to get Messagefolder : {}", id);
        Messagefolder messagefolder = messagefolderService.findOne(id);
        return Optional.ofNullable(messagefolder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messagefolders/:id -> delete the "id" messagefolder.
     */
    @RequestMapping(value = "/messagefolders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMessagefolder(@PathVariable Long id) {
        log.debug("REST request to delete Messagefolder : {}", id);
        messagefolderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("messagefolder", id.toString())).build();
    }
}
