package com.coongli.service;

import com.coongli.domain.Action;
import com.coongli.repository.ActionRepository;
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
 * Service Implementation for managing Action.
 */
@Service
@Transactional
public class ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionService.class);
    
    @Inject
    private ActionRepository actionRepository;
    
    /**
     * Save a action.
     * @return the persisted entity
     */
    public Action save(Action action) {
        log.debug("Request to save Action : {}", action);
        Action result = actionRepository.save(action);
        return result;
    }

    /**
     *  get all the actions.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Action> findAll(Pageable pageable) {
        log.debug("Request to get all Actions");
        Page<Action> result = actionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one action by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Action findOne(Long id) {
        log.debug("Request to get Action : {}", id);
        Action action = actionRepository.findOne(id);
        return action;
    }

    /**
     *  delete the  action by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Action : {}", id);
        actionRepository.delete(id);
    }
}
