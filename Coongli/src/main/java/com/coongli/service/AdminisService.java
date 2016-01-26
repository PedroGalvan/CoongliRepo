package com.coongli.service;

import com.coongli.domain.Adminis;
import com.coongli.repository.AdminisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Adminis.
 */
@Service
@Transactional
public class AdminisService {

    private final Logger log = LoggerFactory.getLogger(AdminisService.class);
    
    @Inject
    private AdminisRepository adminisRepository;
    
    /**
     * Save a adminis.
     * @return the persisted entity
     */
    public Adminis save(Adminis adminis) {
        log.debug("Request to save Adminis : {}", adminis);
        Adminis result = adminisRepository.save(adminis);
        return result;
    }

    /**
     *  get all the adminiss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Adminis> findAll() {
        log.debug("Request to get all Adminiss");
        List<Adminis> result = adminisRepository.findAll();
        return result;
    }

    /**
     *  get one adminis by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Adminis findOne(Long id) {
        log.debug("Request to get Adminis : {}", id);
        Adminis adminis = adminisRepository.findOne(id);
        return adminis;
    }

    /**
     *  delete the  adminis by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Adminis : {}", id);
        adminisRepository.delete(id);
    }
}
