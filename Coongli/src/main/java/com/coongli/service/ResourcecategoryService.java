package com.coongli.service;

import com.coongli.domain.Resourcecategory;
import com.coongli.repository.ResourcecategoryRepository;
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
 * Service Implementation for managing Resourcecategory.
 */
@Service
@Transactional
public class ResourcecategoryService {

    private final Logger log = LoggerFactory.getLogger(ResourcecategoryService.class);
    
    @Inject
    private ResourcecategoryRepository resourcecategoryRepository;
    
    /**
     * Save a resourcecategory.
     * @return the persisted entity
     */
    public Resourcecategory save(Resourcecategory resourcecategory) {
        log.debug("Request to save Resourcecategory : {}", resourcecategory);
        Resourcecategory result = resourcecategoryRepository.save(resourcecategory);
        return result;
    }

    /**
     *  get all the resourcecategorys.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Resourcecategory> findAll(Pageable pageable) {
        log.debug("Request to get all Resourcecategorys");
        Page<Resourcecategory> result = resourcecategoryRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one resourcecategory by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Resourcecategory findOne(Long id) {
        log.debug("Request to get Resourcecategory : {}", id);
        Resourcecategory resourcecategory = resourcecategoryRepository.findOneWithEagerRelationships(id);
        return resourcecategory;
    }

    /**
     *  delete the  resourcecategory by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Resourcecategory : {}", id);
        resourcecategoryRepository.delete(id);
    }
}
