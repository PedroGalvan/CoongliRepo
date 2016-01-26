package com.coongli.service;

import com.coongli.domain.Report;
import com.coongli.repository.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Report.
 */
@Service
@Transactional
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(ReportService.class);
    
    @Inject
    private ReportRepository reportRepository;
    
    /**
     * Save a report.
     * @return the persisted entity
     */
    public Report save(Report report) {
        log.debug("Request to save Report : {}", report);
        Report result = reportRepository.save(report);
        return result;
    }

    /**
     *  get all the reports.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Report> findAll(Pageable pageable) {
        log.debug("Request to get all Reports");
        Page<Report> result = reportRepository.findAll(pageable); 
        return result;
    }


    /**
     *  get all the reports where Session is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Report> findAllWhereSessionIsNull() {
        log.debug("Request to get all reports where Session is null");
        return StreamSupport
            .stream(reportRepository.findAll().spliterator(), false)
            .filter(report -> report.getSession() == null)
            .collect(Collectors.toList());
    }

    /**
     *  get one report by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Report findOne(Long id) {
        log.debug("Request to get Report : {}", id);
        Report report = reportRepository.findOne(id);
        return report;
    }

    /**
     *  delete the  report by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Report : {}", id);
        reportRepository.delete(id);
    }
}
