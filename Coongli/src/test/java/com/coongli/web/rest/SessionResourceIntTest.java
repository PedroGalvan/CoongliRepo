package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Session;
import com.coongli.repository.SessionRepository;
import com.coongli.service.SessionService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SessionResource REST controller.
 *
 * @see SessionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SessionResourceIntTest {


    private static final LocalDate DEFAULT_STARTMOMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTMOMENT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDMOMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDMOMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_PERIODICA = false;
    private static final Boolean UPDATED_PERIODICA = true;

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final Boolean DEFAULT_CANCELLED = false;
    private static final Boolean UPDATED_CANCELLED = true;

    @Inject
    private SessionRepository sessionRepository;

    @Inject
    private SessionService sessionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSessionMockMvc;

    private Session session;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SessionResource sessionResource = new SessionResource();
        ReflectionTestUtils.setField(sessionResource, "sessionService", sessionService);
        this.restSessionMockMvc = MockMvcBuilders.standaloneSetup(sessionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        session = new Session();
        session.setStartmoment(DEFAULT_STARTMOMENT);
        session.setEndmoment(DEFAULT_ENDMOMENT);
        session.setPeriodica(DEFAULT_PERIODICA);
        session.setHidden(DEFAULT_HIDDEN);
        session.setAccepted(DEFAULT_ACCEPTED);
        session.setCancelled(DEFAULT_CANCELLED);
    }

    @Test
    @Transactional
    public void createSession() throws Exception {
        int databaseSizeBeforeCreate = sessionRepository.findAll().size();

        // Create the Session

        restSessionMockMvc.perform(post("/api/sessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(session)))
                .andExpect(status().isCreated());

        // Validate the Session in the database
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions).hasSize(databaseSizeBeforeCreate + 1);
        Session testSession = sessions.get(sessions.size() - 1);
        assertThat(testSession.getStartmoment()).isEqualTo(DEFAULT_STARTMOMENT);
        assertThat(testSession.getEndmoment()).isEqualTo(DEFAULT_ENDMOMENT);
        assertThat(testSession.getPeriodica()).isEqualTo(DEFAULT_PERIODICA);
        assertThat(testSession.getHidden()).isEqualTo(DEFAULT_HIDDEN);
        assertThat(testSession.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testSession.getCancelled()).isEqualTo(DEFAULT_CANCELLED);
    }

    @Test
    @Transactional
    public void checkStartmomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setStartmoment(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(session)))
                .andExpect(status().isBadRequest());

        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndmomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = sessionRepository.findAll().size();
        // set the field null
        session.setEndmoment(null);

        // Create the Session, which fails.

        restSessionMockMvc.perform(post("/api/sessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(session)))
                .andExpect(status().isBadRequest());

        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSessions() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get all the sessions
        restSessionMockMvc.perform(get("/api/sessions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(session.getId().intValue())))
                .andExpect(jsonPath("$.[*].startmoment").value(hasItem(DEFAULT_STARTMOMENT.toString())))
                .andExpect(jsonPath("$.[*].endmoment").value(hasItem(DEFAULT_ENDMOMENT.toString())))
                .andExpect(jsonPath("$.[*].periodica").value(hasItem(DEFAULT_PERIODICA.booleanValue())))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())))
                .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
                .andExpect(jsonPath("$.[*].cancelled").value(hasItem(DEFAULT_CANCELLED.booleanValue())));
    }

    @Test
    @Transactional
    public void getSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", session.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(session.getId().intValue()))
            .andExpect(jsonPath("$.startmoment").value(DEFAULT_STARTMOMENT.toString()))
            .andExpect(jsonPath("$.endmoment").value(DEFAULT_ENDMOMENT.toString()))
            .andExpect(jsonPath("$.periodica").value(DEFAULT_PERIODICA.booleanValue()))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.cancelled").value(DEFAULT_CANCELLED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSession() throws Exception {
        // Get the session
        restSessionMockMvc.perform(get("/api/sessions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

		int databaseSizeBeforeUpdate = sessionRepository.findAll().size();

        // Update the session
        session.setStartmoment(UPDATED_STARTMOMENT);
        session.setEndmoment(UPDATED_ENDMOMENT);
        session.setPeriodica(UPDATED_PERIODICA);
        session.setHidden(UPDATED_HIDDEN);
        session.setAccepted(UPDATED_ACCEPTED);
        session.setCancelled(UPDATED_CANCELLED);

        restSessionMockMvc.perform(put("/api/sessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(session)))
                .andExpect(status().isOk());

        // Validate the Session in the database
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions).hasSize(databaseSizeBeforeUpdate);
        Session testSession = sessions.get(sessions.size() - 1);
        assertThat(testSession.getStartmoment()).isEqualTo(UPDATED_STARTMOMENT);
        assertThat(testSession.getEndmoment()).isEqualTo(UPDATED_ENDMOMENT);
        assertThat(testSession.getPeriodica()).isEqualTo(UPDATED_PERIODICA);
        assertThat(testSession.getHidden()).isEqualTo(UPDATED_HIDDEN);
        assertThat(testSession.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testSession.getCancelled()).isEqualTo(UPDATED_CANCELLED);
    }

    @Test
    @Transactional
    public void deleteSession() throws Exception {
        // Initialize the database
        sessionRepository.saveAndFlush(session);

		int databaseSizeBeforeDelete = sessionRepository.findAll().size();

        // Get the session
        restSessionMockMvc.perform(delete("/api/sessions/{id}", session.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Session> sessions = sessionRepository.findAll();
        assertThat(sessions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
