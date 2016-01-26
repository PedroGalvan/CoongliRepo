package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Session;
import com.coongli.service.SessionService;
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
 * REST controller for managing Session.
 */
@RestController
@RequestMapping("/api")
public class SessionResource {

    private final Logger log = LoggerFactory.getLogger(SessionResource.class);
        
    @Inject
    private SessionService sessionService;
    
    /**
     * POST  /sessions -> Create a new session.
     */
    @RequestMapping(value = "/sessions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Session> createSession(@Valid @RequestBody Session session) throws URISyntaxException {
        log.debug("REST request to save Session : {}", session);
        if (session.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("session", "idexists", "A new session cannot already have an ID")).body(null);
        }
        Session result = sessionService.save(session);
        return ResponseEntity.created(new URI("/api/sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("session", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sessions -> Updates an existing session.
     */
    @RequestMapping(value = "/sessions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Session> updateSession(@Valid @RequestBody Session session) throws URISyntaxException {
        log.debug("REST request to update Session : {}", session);
        if (session.getId() == null) {
            return createSession(session);
        }
        Session result = sessionService.save(session);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("session", session.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sessions -> get all the sessions.
     */
    @RequestMapping(value = "/sessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Session>> getAllSessions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Sessions");
        Page<Session> page = sessionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sessions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sessions/:id -> get the "id" session.
     */
    @RequestMapping(value = "/sessions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Session> getSession(@PathVariable Long id) {
        log.debug("REST request to get Session : {}", id);
        Session session = sessionService.findOne(id);
        return Optional.ofNullable(session)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sessions/:id -> delete the "id" session.
     */
    @RequestMapping(value = "/sessions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        log.debug("REST request to delete Session : {}", id);
        sessionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("session", id.toString())).build();
    }
}
