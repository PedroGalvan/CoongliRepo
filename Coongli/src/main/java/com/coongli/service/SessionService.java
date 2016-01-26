package com.coongli.service;

import com.coongli.domain.Session;
import com.coongli.repository.SessionRepository;
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
 * Service Implementation for managing Session.
 */
@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);
    
    @Inject
    private SessionRepository sessionRepository;
    
    /**
     * Save a session.
     * @return the persisted entity
     */
    public Session save(Session session) {
        log.debug("Request to save Session : {}", session);
        Session result = sessionRepository.save(session);
        return result;
    }

    /**
     *  get all the sessions.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Session> findAll(Pageable pageable) {
        log.debug("Request to get all Sessions");
        Page<Session> result = sessionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one session by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Session findOne(Long id) {
        log.debug("Request to get Session : {}", id);
        Session session = sessionRepository.findOne(id);
        return session;
    }

    /**
     *  delete the  session by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.delete(id);
    }
}
