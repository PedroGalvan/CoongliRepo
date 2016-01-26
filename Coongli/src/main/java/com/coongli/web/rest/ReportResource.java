package com.coongli.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.coongli.domain.Report;
import com.coongli.service.ReportService;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);
        
    @Inject
    private ReportService reportService;
    
    /**
     * POST  /reports -> Create a new report.
     */
    @RequestMapping(value = "/reports",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Report> createReport(@RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to save Report : {}", report);
        if (report.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("report", "idexists", "A new report cannot already have an ID")).body(null);
        }
        Report result = reportService.save(report);
        return ResponseEntity.created(new URI("/api/reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("report", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reports -> Updates an existing report.
     */
    @RequestMapping(value = "/reports",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Report> updateReport(@RequestBody Report report) throws URISyntaxException {
        log.debug("REST request to update Report : {}", report);
        if (report.getId() == null) {
            return createReport(report);
        }
        Report result = reportService.save(report);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("report", report.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reports -> get all the reports.
     */
    @RequestMapping(value = "/reports",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Report>> getAllReports(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("session-is-null".equals(filter)) {
            log.debug("REST request to get all Reports where session is null");
            return new ResponseEntity<>(reportService.findAllWhereSessionIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Reports");
        Page<Report> page = reportService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reports");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reports/:id -> get the "id" report.
     */
    @RequestMapping(value = "/reports/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        log.debug("REST request to get Report : {}", id);
        Report report = reportService.findOne(id);
        return Optional.ofNullable(report)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /reports/:id -> delete the "id" report.
     */
    @RequestMapping(value = "/reports/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.debug("REST request to delete Report : {}", id);
        reportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("report", id.toString())).build();
    }
}
