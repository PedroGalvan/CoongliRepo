package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Adminis;
import com.coongli.repository.AdminisRepository;
import com.coongli.service.AdminisService;

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
 * Test class for the AdminisResource REST controller.
 *
 * @see AdminisResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdminisResourceIntTest {


    @Inject
    private AdminisRepository adminisRepository;

    @Inject
    private AdminisService adminisService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdminisMockMvc;

    private Adminis adminis;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdminisResource adminisResource = new AdminisResource();
        ReflectionTestUtils.setField(adminisResource, "adminisService", adminisService);
        this.restAdminisMockMvc = MockMvcBuilders.standaloneSetup(adminisResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        adminis = new Adminis();
    }

    @Test
    @Transactional
    public void createAdminis() throws Exception {
        int databaseSizeBeforeCreate = adminisRepository.findAll().size();

        // Create the Adminis

        restAdminisMockMvc.perform(post("/api/adminiss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adminis)))
                .andExpect(status().isCreated());

        // Validate the Adminis in the database
        List<Adminis> adminiss = adminisRepository.findAll();
        assertThat(adminiss).hasSize(databaseSizeBeforeCreate + 1);
        Adminis testAdminis = adminiss.get(adminiss.size() - 1);
    }

    @Test
    @Transactional
    public void getAllAdminiss() throws Exception {
        // Initialize the database
        adminisRepository.saveAndFlush(adminis);

        // Get all the adminiss
        restAdminisMockMvc.perform(get("/api/adminiss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adminis.getId().intValue())));
    }

    @Test
    @Transactional
    public void getAdminis() throws Exception {
        // Initialize the database
        adminisRepository.saveAndFlush(adminis);

        // Get the adminis
        restAdminisMockMvc.perform(get("/api/adminiss/{id}", adminis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adminis.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdminis() throws Exception {
        // Get the adminis
        restAdminisMockMvc.perform(get("/api/adminiss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdminis() throws Exception {
        // Initialize the database
        adminisRepository.saveAndFlush(adminis);

		int databaseSizeBeforeUpdate = adminisRepository.findAll().size();

        // Update the adminis

        restAdminisMockMvc.perform(put("/api/adminiss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adminis)))
                .andExpect(status().isOk());

        // Validate the Adminis in the database
        List<Adminis> adminiss = adminisRepository.findAll();
        assertThat(adminiss).hasSize(databaseSizeBeforeUpdate);
        Adminis testAdminis = adminiss.get(adminiss.size() - 1);
    }

    @Test
    @Transactional
    public void deleteAdminis() throws Exception {
        // Initialize the database
        adminisRepository.saveAndFlush(adminis);

		int databaseSizeBeforeDelete = adminisRepository.findAll().size();

        // Get the adminis
        restAdminisMockMvc.perform(delete("/api/adminiss/{id}", adminis.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Adminis> adminiss = adminisRepository.findAll();
        assertThat(adminiss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
