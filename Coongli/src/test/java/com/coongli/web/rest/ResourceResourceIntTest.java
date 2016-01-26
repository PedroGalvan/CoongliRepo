package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Resource;
import com.coongli.repository.ResourceRepository;
import com.coongli.service.ResourceService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ResourceResource REST controller.
 *
 * @see ResourceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ResourceResourceIntTest {


    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private ResourceService resourceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restResourceMockMvc;

    private Resource resource;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResourceResource resourceResource = new ResourceResource();
        ReflectionTestUtils.setField(resourceResource, "resourceService", resourceService);
        this.restResourceMockMvc = MockMvcBuilders.standaloneSetup(resourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        resource = new Resource();
    }

    @Test
    @Transactional
    public void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // Create the Resource

        restResourceMockMvc.perform(post("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resources.get(resources.size() - 1);
    }

    @Test
    @Transactional
    public void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resources
        restResourceMockMvc.perform(get("/api/resources?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())));
    }

    @Test
    @Transactional
    public void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get("/api/resources/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

		int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource

        restResourceMockMvc.perform(put("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resource)))
                .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resources.get(resources.size() - 1);
    }

    @Test
    @Transactional
    public void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

		int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Get the resource
        restResourceMockMvc.perform(delete("/api/resources/{id}", resource.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Resource> resources = resourceRepository.findAll();
        assertThat(resources).hasSize(databaseSizeBeforeDelete - 1);
    }
}
