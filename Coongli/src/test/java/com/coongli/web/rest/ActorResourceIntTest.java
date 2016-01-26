package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Actor;
import com.coongli.repository.ActorRepository;
import com.coongli.service.ActorService;

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
 * Test class for the ActorResource REST controller.
 *
 * @see ActorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ActorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SURNAME = "AAAAA";
    private static final String UPDATED_SURNAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    @Inject
    private ActorRepository actorRepository;

    @Inject
    private ActorService actorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActorMockMvc;

    private Actor actor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActorResource actorResource = new ActorResource();
        ReflectionTestUtils.setField(actorResource, "actorService", actorService);
        this.restActorMockMvc = MockMvcBuilders.standaloneSetup(actorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        actor = new Actor();
        actor.setName(DEFAULT_NAME);
        actor.setSurname(DEFAULT_SURNAME);
        actor.setEmail(DEFAULT_EMAIL);
        actor.setPhone(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actors.get(actors.size() - 1);
        assertThat(testActor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActor.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testActor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testActor.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setName(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isBadRequest());

        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setSurname(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isBadRequest());

        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setEmail(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isBadRequest());

        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setPhone(null);

        // Create the Actor, which fails.

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isBadRequest());

        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actors
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

		int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        actor.setName(UPDATED_NAME);
        actor.setSurname(UPDATED_SURNAME);
        actor.setEmail(UPDATED_EMAIL);
        actor.setPhone(UPDATED_PHONE);

        restActorMockMvc.perform(put("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actor)))
                .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actors.get(actors.size() - 1);
        assertThat(testActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActor.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testActor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testActor.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

		int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Get the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
