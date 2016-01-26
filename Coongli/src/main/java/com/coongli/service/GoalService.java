package com.coongli.service;

import com.coongli.domain.Goal;
import com.coongli.repository.GoalRepository;
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
 * Service Implementation for managing Goal.
 */
@Service
@Transactional
public class GoalService {

    private final Logger log = LoggerFactory.getLogger(GoalService.class);
    
    @Inject
    private GoalRepository goalRepository;
    
    /**
     * Save a goal.
     * @return the persisted entity
     */
    public Goal save(Goal goal) {
        log.debug("Request to save Goal : {}", goal);
        Goal result = goalRepository.save(goal);
        return result;
    }

    /**
     *  get all the goals.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Goal> findAll(Pageable pageable) {
        log.debug("Request to get all Goals");
        Page<Goal> result = goalRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one goal by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Goal findOne(Long id) {
        log.debug("Request to get Goal : {}", id);
        Goal goal = goalRepository.findOne(id);
        return goal;
    }

    /**
     *  delete the  goal by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Goal : {}", id);
        goalRepository.delete(id);
    }
}
