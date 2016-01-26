package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Mesage;
import com.coongli.repository.MesageRepository;
import com.coongli.service.MesageService;

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
 * Test class for the MesageResource REST controller.
 *
 * @see MesageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MesageResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";
    private static final String DEFAULT_BODY = "AAAAA";
    private static final String UPDATED_BODY = "BBBBB";

    private static final LocalDate DEFAULT_SENTMOMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SENTMOMENT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_SAW = false;
    private static final Boolean UPDATED_SAW = true;

    @Inject
    private MesageRepository mesageRepository;

    @Inject
    private MesageService mesageService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMesageMockMvc;

    private Mesage mesage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MesageResource mesageResource = new MesageResource();
        ReflectionTestUtils.setField(mesageResource, "mesageService", mesageService);
        this.restMesageMockMvc = MockMvcBuilders.standaloneSetup(mesageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mesage = new Mesage();
        mesage.setSubject(DEFAULT_SUBJECT);
        mesage.setBody(DEFAULT_BODY);
        mesage.setSentmoment(DEFAULT_SENTMOMENT);
        mesage.setSaw(DEFAULT_SAW);
    }

    @Test
    @Transactional
    public void createMesage() throws Exception {
        int databaseSizeBeforeCreate = mesageRepository.findAll().size();

        // Create the Mesage

        restMesageMockMvc.perform(post("/api/mesages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mesage)))
                .andExpect(status().isCreated());

        // Validate the Mesage in the database
        List<Mesage> mesages = mesageRepository.findAll();
        assertThat(mesages).hasSize(databaseSizeBeforeCreate + 1);
        Mesage testMesage = mesages.get(mesages.size() - 1);
        assertThat(testMesage.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testMesage.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testMesage.getSentmoment()).isEqualTo(DEFAULT_SENTMOMENT);
        assertThat(testMesage.getSaw()).isEqualTo(DEFAULT_SAW);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesageRepository.findAll().size();
        // set the field null
        mesage.setSubject(null);

        // Create the Mesage, which fails.

        restMesageMockMvc.perform(post("/api/mesages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mesage)))
                .andExpect(status().isBadRequest());

        List<Mesage> mesages = mesageRepository.findAll();
        assertThat(mesages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesageRepository.findAll().size();
        // set the field null
        mesage.setBody(null);

        // Create the Mesage, which fails.

        restMesageMockMvc.perform(post("/api/mesages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mesage)))
                .andExpect(status().isBadRequest());

        List<Mesage> mesages = mesageRepository.findAll();
        assertThat(mesages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSentmomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = mesageRepository.findAll().size();
        // set the field null
        mesage.setSentmoment(null);

        // Create the Mesage, which fails.

        restMesageMockMvc.perform(post("/api/mesages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mesage)))
                .andExpect(status().isBadRequest());

        List<Mesage> mesages = mesageRepository.findAll();
        assertThat(mesages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMesages() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);

        // Get all the mesages
        restMesageMockMvc.perform(get("/api/mesages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mesage.getId().intValue())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
                .andExpect(jsonPath("$.[*].sentmoment").value(hasItem(DEFAULT_SENTMOMENT.toString())))
                .andExpect(jsonPath("$.[*].saw").value(hasItem(DEFAULT_SAW.booleanValue())));
    }

    @Test
    @Transactional
    public void getMesage() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);

        // Get the mesage
        restMesageMockMvc.perform(get("/api/mesages/{id}", mesage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mesage.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.sentmoment").value(DEFAULT_SENTMOMENT.toString()))
            .andExpect(jsonPath("$.saw").value(DEFAULT_SAW.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMesage() throws Exception {
        // Get the mesage
        restMesageMockMvc.perform(get("/api/mesages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMesage() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);

		int databaseSizeBeforeUpdate = mesageRepository.findAll().size();

        // Update the mesage
        mesage.setSubject(UPDATED_SUBJECT);
        mesage.setBody(UPDATED_BODY);
        mesage.setSentmoment(UPDATED_SENTMOMENT);
        mesage.setSaw(UPDATED_SAW);

        restMesageMockMvc.perform(put("/api/mesages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mesage)))
                .andExpect(status().isOk());

        // Validate the Mesage in the database
        List<Mesage> mesages = mesageRepository.findAll();
        assertThat(mesages).hasSize(databaseSizeBeforeUpdate);
        Mesage testMesage = mesages.get(mesages.size() - 1);
        assertThat(testMesage.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testMesage.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testMesage.getSentmoment()).isEqualTo(UPDATED_SENTMOMENT);
        assertThat(testMesage.getSaw()).isEqualTo(UPDATED_SAW);
    }

    @Test
    @Transactional
    public void deleteMesage() throws Exception {
        // Initialize the database
        mesageRepository.saveAndFlush(mesage);

		int databaseSizeBeforeDelete = mesageRepository.findAll().size();

        // Get the mesage
        restMesageMockMvc.perform(delete("/api/mesages/{id}", mesage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mesage> mesages = mesageRepository.findAll();
        assertThat(mesages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
