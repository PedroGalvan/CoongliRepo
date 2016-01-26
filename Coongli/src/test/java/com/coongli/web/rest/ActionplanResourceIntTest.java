package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Actionplan;
import com.coongli.repository.ActionplanRepository;
import com.coongli.service.ActionplanService;

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
 * Test class for the ActionplanResource REST controller.
 *
 * @see ActionplanResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ActionplanResourceIntTest {


    @Inject
    private ActionplanRepository actionplanRepository;

    @Inject
    private ActionplanService actionplanService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActionplanMockMvc;

    private Actionplan actionplan;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActionplanResource actionplanResource = new ActionplanResource();
        ReflectionTestUtils.setField(actionplanResource, "actionplanService", actionplanService);
        this.restActionplanMockMvc = MockMvcBuilders.standaloneSetup(actionplanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        actionplan = new Actionplan();
    }

    @Test
    @Transactional
    public void createActionplan() throws Exception {
        int databaseSizeBeforeCreate = actionplanRepository.findAll().size();

        // Create the Actionplan

        restActionplanMockMvc.perform(post("/api/actionplans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actionplan)))
                .andExpect(status().isCreated());

        // Validate the Actionplan in the database
        List<Actionplan> actionplans = actionplanRepository.findAll();
        assertThat(actionplans).hasSize(databaseSizeBeforeCreate + 1);
        Actionplan testActionplan = actionplans.get(actionplans.size() - 1);
    }

    @Test
    @Transactional
    public void getAllActionplans() throws Exception {
        // Initialize the database
        actionplanRepository.saveAndFlush(actionplan);

        // Get all the actionplans
        restActionplanMockMvc.perform(get("/api/actionplans?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(actionplan.getId().intValue())));
    }

    @Test
    @Transactional
    public void getActionplan() throws Exception {
        // Initialize the database
        actionplanRepository.saveAndFlush(actionplan);

        // Get the actionplan
        restActionplanMockMvc.perform(get("/api/actionplans/{id}", actionplan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(actionplan.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingActionplan() throws Exception {
        // Get the actionplan
        restActionplanMockMvc.perform(get("/api/actionplans/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActionplan() throws Exception {
        // Initialize the database
        actionplanRepository.saveAndFlush(actionplan);

		int databaseSizeBeforeUpdate = actionplanRepository.findAll().size();

        // Update the actionplan

        restActionplanMockMvc.perform(put("/api/actionplans")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actionplan)))
                .andExpect(status().isOk());

        // Validate the Actionplan in the database
        List<Actionplan> actionplans = actionplanRepository.findAll();
        assertThat(actionplans).hasSize(databaseSizeBeforeUpdate);
        Actionplan testActionplan = actionplans.get(actionplans.size() - 1);
    }

    @Test
    @Transactional
    public void deleteActionplan() throws Exception {
        // Initialize the database
        actionplanRepository.saveAndFlush(actionplan);

		int databaseSizeBeforeDelete = actionplanRepository.findAll().size();

        // Get the actionplan
        restActionplanMockMvc.perform(delete("/api/actionplans/{id}", actionplan.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Actionplan> actionplans = actionplanRepository.findAll();
        assertThat(actionplans).hasSize(databaseSizeBeforeDelete - 1);
    }
}
