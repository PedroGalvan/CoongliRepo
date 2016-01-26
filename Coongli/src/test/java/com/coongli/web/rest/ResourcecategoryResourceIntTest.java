package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Resourcecategory;
import com.coongli.repository.ResourcecategoryRepository;
import com.coongli.service.ResourcecategoryService;

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
 * Test class for the ResourcecategoryResource REST controller.
 *
 * @see ResourcecategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ResourcecategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    @Inject
    private ResourcecategoryRepository resourcecategoryRepository;

    @Inject
    private ResourcecategoryService resourcecategoryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restResourcecategoryMockMvc;

    private Resourcecategory resourcecategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResourcecategoryResource resourcecategoryResource = new ResourcecategoryResource();
        ReflectionTestUtils.setField(resourcecategoryResource, "resourcecategoryService", resourcecategoryService);
        this.restResourcecategoryMockMvc = MockMvcBuilders.standaloneSetup(resourcecategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        resourcecategory = new Resourcecategory();
        resourcecategory.setName(DEFAULT_NAME);
        resourcecategory.setDescription(DEFAULT_DESCRIPTION);
        resourcecategory.setHidden(DEFAULT_HIDDEN);
    }

    @Test
    @Transactional
    public void createResourcecategory() throws Exception {
        int databaseSizeBeforeCreate = resourcecategoryRepository.findAll().size();

        // Create the Resourcecategory

        restResourcecategoryMockMvc.perform(post("/api/resourcecategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcecategory)))
                .andExpect(status().isCreated());

        // Validate the Resourcecategory in the database
        List<Resourcecategory> resourcecategorys = resourcecategoryRepository.findAll();
        assertThat(resourcecategorys).hasSize(databaseSizeBeforeCreate + 1);
        Resourcecategory testResourcecategory = resourcecategorys.get(resourcecategorys.size() - 1);
        assertThat(testResourcecategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testResourcecategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testResourcecategory.getHidden()).isEqualTo(DEFAULT_HIDDEN);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcecategoryRepository.findAll().size();
        // set the field null
        resourcecategory.setName(null);

        // Create the Resourcecategory, which fails.

        restResourcecategoryMockMvc.perform(post("/api/resourcecategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcecategory)))
                .andExpect(status().isBadRequest());

        List<Resourcecategory> resourcecategorys = resourcecategoryRepository.findAll();
        assertThat(resourcecategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = resourcecategoryRepository.findAll().size();
        // set the field null
        resourcecategory.setDescription(null);

        // Create the Resourcecategory, which fails.

        restResourcecategoryMockMvc.perform(post("/api/resourcecategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcecategory)))
                .andExpect(status().isBadRequest());

        List<Resourcecategory> resourcecategorys = resourcecategoryRepository.findAll();
        assertThat(resourcecategorys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResourcecategorys() throws Exception {
        // Initialize the database
        resourcecategoryRepository.saveAndFlush(resourcecategory);

        // Get all the resourcecategorys
        restResourcecategoryMockMvc.perform(get("/api/resourcecategorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(resourcecategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())));
    }

    @Test
    @Transactional
    public void getResourcecategory() throws Exception {
        // Initialize the database
        resourcecategoryRepository.saveAndFlush(resourcecategory);

        // Get the resourcecategory
        restResourcecategoryMockMvc.perform(get("/api/resourcecategorys/{id}", resourcecategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(resourcecategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResourcecategory() throws Exception {
        // Get the resourcecategory
        restResourcecategoryMockMvc.perform(get("/api/resourcecategorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourcecategory() throws Exception {
        // Initialize the database
        resourcecategoryRepository.saveAndFlush(resourcecategory);

		int databaseSizeBeforeUpdate = resourcecategoryRepository.findAll().size();

        // Update the resourcecategory
        resourcecategory.setName(UPDATED_NAME);
        resourcecategory.setDescription(UPDATED_DESCRIPTION);
        resourcecategory.setHidden(UPDATED_HIDDEN);

        restResourcecategoryMockMvc.perform(put("/api/resourcecategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcecategory)))
                .andExpect(status().isOk());

        // Validate the Resourcecategory in the database
        List<Resourcecategory> resourcecategorys = resourcecategoryRepository.findAll();
        assertThat(resourcecategorys).hasSize(databaseSizeBeforeUpdate);
        Resourcecategory testResourcecategory = resourcecategorys.get(resourcecategorys.size() - 1);
        assertThat(testResourcecategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testResourcecategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testResourcecategory.getHidden()).isEqualTo(UPDATED_HIDDEN);
    }

    @Test
    @Transactional
    public void deleteResourcecategory() throws Exception {
        // Initialize the database
        resourcecategoryRepository.saveAndFlush(resourcecategory);

		int databaseSizeBeforeDelete = resourcecategoryRepository.findAll().size();

        // Get the resourcecategory
        restResourcecategoryMockMvc.perform(delete("/api/resourcecategorys/{id}", resourcecategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Resourcecategory> resourcecategorys = resourcecategoryRepository.findAll();
        assertThat(resourcecategorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
