package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Goal;
import com.coongli.service.GoalService;
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
 * REST controller for managing Goal.
 */
@RestController
@RequestMapping("/api")
public class GoalResource {

    private final Logger log = LoggerFactory.getLogger(GoalResource.class);
        
    @Inject
    private GoalService goalService;
    
    /**
     * POST  /goals -> Create a new goal.
     */
    @RequestMapping(value = "/goals",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goal> createGoal(@Valid @RequestBody Goal goal) throws URISyntaxException {
        log.debug("REST request to save Goal : {}", goal);
        if (goal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("goal", "idexists", "A new goal cannot already have an ID")).body(null);
        }
        Goal result = goalService.save(goal);
        return ResponseEntity.created(new URI("/api/goals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("goal", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /goals -> Updates an existing goal.
     */
    @RequestMapping(value = "/goals",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goal> updateGoal(@Valid @RequestBody Goal goal) throws URISyntaxException {
        log.debug("REST request to update Goal : {}", goal);
        if (goal.getId() == null) {
            return createGoal(goal);
        }
        Goal result = goalService.save(goal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("goal", goal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /goals -> get all the goals.
     */
    @RequestMapping(value = "/goals",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Goal>> getAllGoals(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Goals");
        Page<Goal> page = goalService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /goals/:id -> get the "id" goal.
     */
    @RequestMapping(value = "/goals/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Goal> getGoal(@PathVariable Long id) {
        log.debug("REST request to get Goal : {}", id);
        Goal goal = goalService.findOne(id);
        return Optional.ofNullable(goal)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /goals/:id -> delete the "id" goal.
     */
    @RequestMapping(value = "/goals/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        log.debug("REST request to delete Goal : {}", id);
        goalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("goal", id.toString())).build();
    }
}
