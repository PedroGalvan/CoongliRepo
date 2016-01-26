package com.coongli.service;

import com.coongli.domain.Actionplan;
import com.coongli.repository.ActionplanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Actionplan.
 */
@Service
@Transactional
public class ActionplanService {

    private final Logger log = LoggerFactory.getLogger(ActionplanService.class);
    
    @Inject
    private ActionplanRepository actionplanRepository;
    
    /**
     * Save a actionplan.
     * @return the persisted entity
     */
    public Actionplan save(Actionplan actionplan) {
        log.debug("Request to save Actionplan : {}", actionplan);
        Actionplan result = actionplanRepository.save(actionplan);
        return result;
    }

    /**
     *  get all the actionplans.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Actionplan> findAll(Pageable pageable) {
        log.debug("Request to get all Actionplans");
        Page<Actionplan> result = actionplanRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one actionplan by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Actionplan findOne(Long id) {
        log.debug("Request to get Actionplan : {}", id);
        Actionplan actionplan = actionplanRepository.findOne(id);
        return actionplan;
    }

    /**
     *  delete the  actionplan by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Actionplan : {}", id);
        actionplanRepository.delete(id);
    }
}
