package com.coongli.service;

import com.coongli.domain.Messagefolder;
import com.coongli.repository.MessagefolderRepository;
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
 * Service Implementation for managing Messagefolder.
 */
@Service
@Transactional
public class MessagefolderService {

    private final Logger log = LoggerFactory.getLogger(MessagefolderService.class);
    
    @Inject
    private MessagefolderRepository messagefolderRepository;
    
    /**
     * Save a messagefolder.
     * @return the persisted entity
     */
    public Messagefolder save(Messagefolder messagefolder) {
        log.debug("Request to save Messagefolder : {}", messagefolder);
        Messagefolder result = messagefolderRepository.save(messagefolder);
        return result;
    }

    /**
     *  get all the messagefolders.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Messagefolder> findAll(Pageable pageable) {
        log.debug("Request to get all Messagefolders");
        Page<Messagefolder> result = messagefolderRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one messagefolder by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Messagefolder findOne(Long id) {
        log.debug("Request to get Messagefolder : {}", id);
        Messagefolder messagefolder = messagefolderRepository.findOne(id);
        return messagefolder;
    }

    /**
     *  delete the  messagefolder by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Messagefolder : {}", id);
        messagefolderRepository.delete(id);
    }
}
