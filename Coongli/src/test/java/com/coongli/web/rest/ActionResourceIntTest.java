package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Action;
import com.coongli.repository.ActionRepository;
import com.coongli.service.ActionService;

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
 * Test class for the ActionResource REST controller.
 *
 * @see ActionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ActionResourceIntTest {


    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    @Inject
    private ActionRepository actionRepository;

    @Inject
    private ActionService actionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActionMockMvc;

    private Action action;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActionResource actionResource = new ActionResource();
        ReflectionTestUtils.setField(actionResource, "actionService", actionService);
        this.restActionMockMvc = MockMvcBuilders.standaloneSetup(actionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        action = new Action();
        action.setCompleted(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void createAction() throws Exception {
        int databaseSizeBeforeCreate = actionRepository.findAll().size();

        // Create the Action

        restActionMockMvc.perform(post("/api/actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(action)))
                .andExpect(status().isCreated());

        // Validate the Action in the database
        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeCreate + 1);
        Action testAction = actions.get(actions.size() - 1);
        assertThat(testAction.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    public void checkCompletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = actionRepository.findAll().size();
        // set the field null
        action.setCompleted(null);

        // Create the Action, which fails.

        restActionMockMvc.perform(post("/api/actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(action)))
                .andExpect(status().isBadRequest());

        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActions() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get all the actions
        restActionMockMvc.perform(get("/api/actions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(action.getId().intValue())))
                .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

        // Get the action
        restActionMockMvc.perform(get("/api/actions/{id}", action.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(action.getId().intValue()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAction() throws Exception {
        // Get the action
        restActionMockMvc.perform(get("/api/actions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

		int databaseSizeBeforeUpdate = actionRepository.findAll().size();

        // Update the action
        action.setCompleted(UPDATED_COMPLETED);

        restActionMockMvc.perform(put("/api/actions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(action)))
                .andExpect(status().isOk());

        // Validate the Action in the database
        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeUpdate);
        Action testAction = actions.get(actions.size() - 1);
        assertThat(testAction.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    public void deleteAction() throws Exception {
        // Initialize the database
        actionRepository.saveAndFlush(action);

		int databaseSizeBeforeDelete = actionRepository.findAll().size();

        // Get the action
        restActionMockMvc.perform(delete("/api/actions/{id}", action.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Action> actions = actionRepository.findAll();
        assertThat(actions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
