package com.coongli.web.rest;

import com.coongli.Application;
import com.coongli.domain.Textable;
import com.coongli.repository.TextableRepository;
import com.coongli.service.TextableService;

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
 * Test class for the TextableResource REST controller.
 *
 * @see TextableResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TextableResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    private static final LocalDate DEFAULT_CREATION_MOMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_MOMENT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private TextableRepository textableRepository;

    @Inject
    private TextableService textableService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTextableMockMvc;

    private Textable textable;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TextableResource textableResource = new TextableResource();
        ReflectionTestUtils.setField(textableResource, "textableService", textableService);
        this.restTextableMockMvc = MockMvcBuilders.standaloneSetup(textableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        textable = new Textable();
        textable.setText(DEFAULT_TEXT);
        textable.setCreationMoment(DEFAULT_CREATION_MOMENT);
    }

    @Test
    @Transactional
    public void createTextable() throws Exception {
        int databaseSizeBeforeCreate = textableRepository.findAll().size();

        // Create the Textable

        restTextableMockMvc.perform(post("/api/textables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(textable)))
                .andExpect(status().isCreated());

        // Validate the Textable in the database
        List<Textable> textables = textableRepository.findAll();
        assertThat(textables).hasSize(databaseSizeBeforeCreate + 1);
        Textable testTextable = textables.get(textables.size() - 1);
        assertThat(testTextable.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testTextable.getCreationMoment()).isEqualTo(DEFAULT_CREATION_MOMENT);
    }

    @Test
    @Transactional
    public void getAllTextables() throws Exception {
        // Initialize the database
        textableRepository.saveAndFlush(textable);

        // Get all the textables
        restTextableMockMvc.perform(get("/api/textables?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(textable.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].creationMoment").value(hasItem(DEFAULT_CREATION_MOMENT.toString())));
    }

    @Test
    @Transactional
    public void getTextable() throws Exception {
        // Initialize the database
        textableRepository.saveAndFlush(textable);

        // Get the textable
        restTextableMockMvc.perform(get("/api/textables/{id}", textable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(textable.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.creationMoment").value(DEFAULT_CREATION_MOMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTextable() throws Exception {
        // Get the textable
        restTextableMockMvc.perform(get("/api/textables/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTextable() throws Exception {
        // Initialize the database
        textableRepository.saveAndFlush(textable);

		int databaseSizeBeforeUpdate = textableRepository.findAll().size();

        // Update the textable
        textable.setText(UPDATED_TEXT);
        textable.setCreationMoment(UPDATED_CREATION_MOMENT);

        restTextableMockMvc.perform(put("/api/textables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(textable)))
                .andExpect(status().isOk());

        // Validate the Textable in the database
        List<Textable> textables = textableRepository.findAll();
        assertThat(textables).hasSize(databaseSizeBeforeUpdate);
        Textable testTextable = textables.get(textables.size() - 1);
        assertThat(testTextable.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testTextable.getCreationMoment()).isEqualTo(UPDATED_CREATION_MOMENT);
    }

    @Test
    @Transactional
    public void deleteTextable() throws Exception {
        // Initialize the database
        textableRepository.saveAndFlush(textable);

		int databaseSizeBeforeDelete = textableRepository.findAll().size();

        // Get the textable
        restTextableMockMvc.perform(delete("/api/textables/{id}", textable.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Textable> textables = textableRepository.findAll();
        assertThat(textables).hasSize(databaseSizeBeforeDelete - 1);
    }
}
