package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Messagefolder;
import com.coongli.repository.MessagefolderRepository;
import com.coongli.service.MessagefolderService;

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
 * Test class for the MessagefolderResource REST controller.
 *
 * @see MessagefolderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessagefolderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private MessagefolderRepository messagefolderRepository;

    @Inject
    private MessagefolderService messagefolderService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMessagefolderMockMvc;

    private Messagefolder messagefolder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessagefolderResource messagefolderResource = new MessagefolderResource();
        ReflectionTestUtils.setField(messagefolderResource, "messagefolderService", messagefolderService);
        this.restMessagefolderMockMvc = MockMvcBuilders.standaloneSetup(messagefolderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        messagefolder = new Messagefolder();
        messagefolder.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMessagefolder() throws Exception {
        int databaseSizeBeforeCreate = messagefolderRepository.findAll().size();

        // Create the Messagefolder

        restMessagefolderMockMvc.perform(post("/api/messagefolders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagefolder)))
                .andExpect(status().isCreated());

        // Validate the Messagefolder in the database
        List<Messagefolder> messagefolders = messagefolderRepository.findAll();
        assertThat(messagefolders).hasSize(databaseSizeBeforeCreate + 1);
        Messagefolder testMessagefolder = messagefolders.get(messagefolders.size() - 1);
        assertThat(testMessagefolder.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = messagefolderRepository.findAll().size();
        // set the field null
        messagefolder.setName(null);

        // Create the Messagefolder, which fails.

        restMessagefolderMockMvc.perform(post("/api/messagefolders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagefolder)))
                .andExpect(status().isBadRequest());

        List<Messagefolder> messagefolders = messagefolderRepository.findAll();
        assertThat(messagefolders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessagefolders() throws Exception {
        // Initialize the database
        messagefolderRepository.saveAndFlush(messagefolder);

        // Get all the messagefolders
        restMessagefolderMockMvc.perform(get("/api/messagefolders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(messagefolder.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMessagefolder() throws Exception {
        // Initialize the database
        messagefolderRepository.saveAndFlush(messagefolder);

        // Get the messagefolder
        restMessagefolderMockMvc.perform(get("/api/messagefolders/{id}", messagefolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(messagefolder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMessagefolder() throws Exception {
        // Get the messagefolder
        restMessagefolderMockMvc.perform(get("/api/messagefolders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessagefolder() throws Exception {
        // Initialize the database
        messagefolderRepository.saveAndFlush(messagefolder);

		int databaseSizeBeforeUpdate = messagefolderRepository.findAll().size();

        // Update the messagefolder
        messagefolder.setName(UPDATED_NAME);

        restMessagefolderMockMvc.perform(put("/api/messagefolders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(messagefolder)))
                .andExpect(status().isOk());

        // Validate the Messagefolder in the database
        List<Messagefolder> messagefolders = messagefolderRepository.findAll();
        assertThat(messagefolders).hasSize(databaseSizeBeforeUpdate);
        Messagefolder testMessagefolder = messagefolders.get(messagefolders.size() - 1);
        assertThat(testMessagefolder.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteMessagefolder() throws Exception {
        // Initialize the database
        messagefolderRepository.saveAndFlush(messagefolder);

		int databaseSizeBeforeDelete = messagefolderRepository.findAll().size();

        // Get the messagefolder
        restMessagefolderMockMvc.perform(delete("/api/messagefolders/{id}", messagefolder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Messagefolder> messagefolders = messagefolderRepository.findAll();
        assertThat(messagefolders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
