package com.coongli.service;

import com.coongli.domain.Invitation;
import com.coongli.repository.InvitationRepository;
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
 * Service Implementation for managing Invitation.
 */
@Service
@Transactional
public class InvitationService {

    private final Logger log = LoggerFactory.getLogger(InvitationService.class);
    
    @Inject
    private InvitationRepository invitationRepository;
    
    /**
     * Save a invitation.
     * @return the persisted entity
     */
    public Invitation save(Invitation invitation) {
        log.debug("Request to save Invitation : {}", invitation);
        Invitation result = invitationRepository.save(invitation);
        return result;
    }

    /**
     *  get all the invitations.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Invitation> findAll(Pageable pageable) {
        log.debug("Request to get all Invitations");
        Page<Invitation> result = invitationRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one invitation by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Invitation findOne(Long id) {
        log.debug("Request to get Invitation : {}", id);
        Invitation invitation = invitationRepository.findOne(id);
        return invitation;
    }

    /**
     *  delete the  invitation by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Invitation : {}", id);
        invitationRepository.delete(id);
    }
}
