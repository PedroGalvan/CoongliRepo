package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Invitation;
import com.coongli.repository.InvitationRepository;
import com.coongli.service.InvitationService;

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
 * Test class for the InvitationResource REST controller.
 *
 * @see InvitationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InvitationResourceIntTest {

    private static final String DEFAULT_BODY = "AAAAA";
    private static final String UPDATED_BODY = "BBBBB";
    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final Boolean DEFAULT_REJECTED = false;
    private static final Boolean UPDATED_REJECTED = true;

    private static final LocalDate DEFAULT_CREATIONMOMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATIONMOMENT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private InvitationRepository invitationRepository;

    @Inject
    private InvitationService invitationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInvitationMockMvc;

    private Invitation invitation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvitationResource invitationResource = new InvitationResource();
        ReflectionTestUtils.setField(invitationResource, "invitationService", invitationService);
        this.restInvitationMockMvc = MockMvcBuilders.standaloneSetup(invitationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        invitation = new Invitation();
        invitation.setBody(DEFAULT_BODY);
        invitation.setSubject(DEFAULT_SUBJECT);
        invitation.setAccepted(DEFAULT_ACCEPTED);
        invitation.setRejected(DEFAULT_REJECTED);
        invitation.setCreationmoment(DEFAULT_CREATIONMOMENT);
    }

    @Test
    @Transactional
    public void createInvitation() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeCreate + 1);
        Invitation testInvitation = invitations.get(invitations.size() - 1);
        assertThat(testInvitation.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testInvitation.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testInvitation.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testInvitation.getRejected()).isEqualTo(DEFAULT_REJECTED);
        assertThat(testInvitation.getCreationmoment()).isEqualTo(DEFAULT_CREATIONMOMENT);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setBody(null);

        // Create the Invitation, which fails.

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isBadRequest());

        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setSubject(null);

        // Create the Invitation, which fails.

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isBadRequest());

        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationmomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = invitationRepository.findAll().size();
        // set the field null
        invitation.setCreationmoment(null);

        // Create the Invitation, which fails.

        restInvitationMockMvc.perform(post("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isBadRequest());

        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvitations() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitations
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
                .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
                .andExpect(jsonPath("$.[*].rejected").value(hasItem(DEFAULT_REJECTED.booleanValue())))
                .andExpect(jsonPath("$.[*].creationmoment").value(hasItem(DEFAULT_CREATIONMOMENT.toString())));
    }

    @Test
    @Transactional
    public void getInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(invitation.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.rejected").value(DEFAULT_REJECTED.booleanValue()))
            .andExpect(jsonPath("$.creationmoment").value(DEFAULT_CREATIONMOMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvitation() throws Exception {
        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

		int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        invitation.setBody(UPDATED_BODY);
        invitation.setSubject(UPDATED_SUBJECT);
        invitation.setAccepted(UPDATED_ACCEPTED);
        invitation.setRejected(UPDATED_REJECTED);
        invitation.setCreationmoment(UPDATED_CREATIONMOMENT);

        restInvitationMockMvc.perform(put("/api/invitations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invitation)))
                .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitations.get(invitations.size() - 1);
        assertThat(testInvitation.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testInvitation.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testInvitation.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testInvitation.getRejected()).isEqualTo(UPDATED_REJECTED);
        assertThat(testInvitation.getCreationmoment()).isEqualTo(UPDATED_CREATIONMOMENT);
    }

    @Test
    @Transactional
    public void deleteInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

		int databaseSizeBeforeDelete = invitationRepository.findAll().size();

        // Get the invitation
        restInvitationMockMvc.perform(delete("/api/invitations/{id}", invitation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Invitation> invitations = invitationRepository.findAll();
        assertThat(invitations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
