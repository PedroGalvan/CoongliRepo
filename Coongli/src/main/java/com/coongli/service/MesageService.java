package com.coongli.service;

import com.coongli.domain.Mesage;
import com.coongli.repository.MesageRepository;
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
 * Service Implementation for managing Mesage.
 */
@Service
@Transactional
public class MesageService {

    private final Logger log = LoggerFactory.getLogger(MesageService.class);
    
    @Inject
    private MesageRepository mesageRepository;
    
    /**
     * Save a mesage.
     * @return the persisted entity
     */
    public Mesage save(Mesage mesage) {
        log.debug("Request to save Mesage : {}", mesage);
        Mesage result = mesageRepository.save(mesage);
        return result;
    }

    /**
     *  get all the mesages.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Mesage> findAll(Pageable pageable) {
        log.debug("Request to get all Mesages");
        Page<Mesage> result = mesageRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one mesage by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Mesage findOne(Long id) {
        log.debug("Request to get Mesage : {}", id);
        Mesage mesage = mesageRepository.findOne(id);
        return mesage;
    }

    /**
     *  delete the  mesage by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Mesage : {}", id);
        mesageRepository.delete(id);
    }
}
