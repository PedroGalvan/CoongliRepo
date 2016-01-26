package com.coongli.service;

import com.coongli.domain.Textable;
import com.coongli.repository.TextableRepository;
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
 * Service Implementation for managing Textable.
 */
@Service
@Transactional
public class TextableService {

    private final Logger log = LoggerFactory.getLogger(TextableService.class);
    
    @Inject
    private TextableRepository textableRepository;
    
    /**
     * Save a textable.
     * @return the persisted entity
     */
    public Textable save(Textable textable) {
        log.debug("Request to save Textable : {}", textable);
        Textable result = textableRepository.save(textable);
        return result;
    }

    /**
     *  get all the textables.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Textable> findAll(Pageable pageable) {
        log.debug("Request to get all Textables");
        Page<Textable> result = textableRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one textable by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Textable findOne(Long id) {
        log.debug("Request to get Textable : {}", id);
        Textable textable = textableRepository.findOne(id);
        return textable;
    }

    /**
     *  delete the  textable by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Textable : {}", id);
        textableRepository.delete(id);
    }
}
