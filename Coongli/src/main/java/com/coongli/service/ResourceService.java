package com.coongli.service;

import com.coongli.domain.Resource;
import com.coongli.repository.ResourceRepository;
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
 * Service Implementation for managing Resource.
 */
@Service
@Transactional
public class ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceService.class);
    
    @Inject
    private ResourceRepository resourceRepository;
    
    /**
     * Save a resource.
     * @return the persisted entity
     */
    public Resource save(Resource resource) {
        log.debug("Request to save Resource : {}", resource);
        Resource result = resourceRepository.save(resource);
        return result;
    }

    /**
     *  get all the resources.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Resource> findAll(Pageable pageable) {
        log.debug("Request to get all Resources");
        Page<Resource> result = resourceRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one resource by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Resource findOne(Long id) {
        log.debug("Request to get Resource : {}", id);
        Resource resource = resourceRepository.findOne(id);
        return resource;
    }

    /**
     *  delete the  resource by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resource : {}", id);
        resourceRepository.delete(id);
    }
}
