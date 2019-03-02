package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;

import com.mycompany.myapp.domain.VoiceCall;
import com.mycompany.myapp.repository.VoiceCallRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Voice;
/**
 * Test class for the VoiceCallResource REST controller.
 *
 * @see VoiceCallResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterApp.class)
public class VoiceCallResourceIntTest {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Voice DEFAULT_VOICE = Voice.JOANNA;
    private static final Voice UPDATED_VOICE = Voice.MATTHEW;

    private static final String DEFAULT_TWIML = "AAAAAAAAAA";
    private static final String UPDATED_TWIML = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private VoiceCallRepository voiceCallRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVoiceCallMockMvc;

    private VoiceCall voiceCall;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoiceCallResource voiceCallResource = new VoiceCallResource(voiceCallRepository);
        this.restVoiceCallMockMvc = MockMvcBuilders.standaloneSetup(voiceCallResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoiceCall createEntity(EntityManager em) {
        VoiceCall voiceCall = new VoiceCall()
            .number(DEFAULT_NUMBER)
            .message(DEFAULT_MESSAGE)
            .voice(DEFAULT_VOICE)
            .twiml(DEFAULT_TWIML)
            .date(DEFAULT_DATE);
        return voiceCall;
    }

    @Before
    public void initTest() {
        voiceCall = createEntity(em);
    }

    @Test
    @Transactional
    public void createVoiceCall() throws Exception {
        int databaseSizeBeforeCreate = voiceCallRepository.findAll().size();

        // Create the VoiceCall
        restVoiceCallMockMvc.perform(post("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceCall)))
            .andExpect(status().isCreated());

        // Validate the VoiceCall in the database
        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeCreate + 1);
        VoiceCall testVoiceCall = voiceCallList.get(voiceCallList.size() - 1);
        assertThat(testVoiceCall.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testVoiceCall.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testVoiceCall.getVoice()).isEqualTo(DEFAULT_VOICE);
        assertThat(testVoiceCall.getTwiml()).isEqualTo(DEFAULT_TWIML);
        assertThat(testVoiceCall.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createVoiceCallWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = voiceCallRepository.findAll().size();

        // Create the VoiceCall with an existing ID
        voiceCall.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoiceCallMockMvc.perform(post("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceCall)))
            .andExpect(status().isBadRequest());

        // Validate the VoiceCall in the database
        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = voiceCallRepository.findAll().size();
        // set the field null
        voiceCall.setNumber(null);

        // Create the VoiceCall, which fails.

        restVoiceCallMockMvc.perform(post("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceCall)))
            .andExpect(status().isBadRequest());

        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = voiceCallRepository.findAll().size();
        // set the field null
        voiceCall.setMessage(null);

        // Create the VoiceCall, which fails.

        restVoiceCallMockMvc.perform(post("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceCall)))
            .andExpect(status().isBadRequest());

        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = voiceCallRepository.findAll().size();
        // set the field null
        voiceCall.setVoice(null);

        // Create the VoiceCall, which fails.

        restVoiceCallMockMvc.perform(post("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceCall)))
            .andExpect(status().isBadRequest());

        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVoiceCalls() throws Exception {
        // Initialize the database
        voiceCallRepository.saveAndFlush(voiceCall);

        // Get all the voiceCallList
        restVoiceCallMockMvc.perform(get("/api/voice-calls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voiceCall.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].voice").value(hasItem(DEFAULT_VOICE.toString())))
            .andExpect(jsonPath("$.[*].twiml").value(hasItem(DEFAULT_TWIML.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getVoiceCall() throws Exception {
        // Initialize the database
        voiceCallRepository.saveAndFlush(voiceCall);

        // Get the voiceCall
        restVoiceCallMockMvc.perform(get("/api/voice-calls/{id}", voiceCall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(voiceCall.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.voice").value(DEFAULT_VOICE.toString()))
            .andExpect(jsonPath("$.twiml").value(DEFAULT_TWIML.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingVoiceCall() throws Exception {
        // Get the voiceCall
        restVoiceCallMockMvc.perform(get("/api/voice-calls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVoiceCall() throws Exception {
        // Initialize the database
        voiceCallRepository.saveAndFlush(voiceCall);

        int databaseSizeBeforeUpdate = voiceCallRepository.findAll().size();

        // Update the voiceCall
        VoiceCall updatedVoiceCall = voiceCallRepository.findById(voiceCall.getId()).get();
        // Disconnect from session so that the updates on updatedVoiceCall are not directly saved in db
        em.detach(updatedVoiceCall);
        updatedVoiceCall
            .number(UPDATED_NUMBER)
            .message(UPDATED_MESSAGE)
            .voice(UPDATED_VOICE)
            .twiml(UPDATED_TWIML)
            .date(UPDATED_DATE);

        restVoiceCallMockMvc.perform(put("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVoiceCall)))
            .andExpect(status().isOk());

        // Validate the VoiceCall in the database
        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeUpdate);
        VoiceCall testVoiceCall = voiceCallList.get(voiceCallList.size() - 1);
        assertThat(testVoiceCall.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testVoiceCall.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testVoiceCall.getVoice()).isEqualTo(UPDATED_VOICE);
        assertThat(testVoiceCall.getTwiml()).isEqualTo(UPDATED_TWIML);
        assertThat(testVoiceCall.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVoiceCall() throws Exception {
        int databaseSizeBeforeUpdate = voiceCallRepository.findAll().size();

        // Create the VoiceCall

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoiceCallMockMvc.perform(put("/api/voice-calls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voiceCall)))
            .andExpect(status().isBadRequest());

        // Validate the VoiceCall in the database
        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVoiceCall() throws Exception {
        // Initialize the database
        voiceCallRepository.saveAndFlush(voiceCall);

        int databaseSizeBeforeDelete = voiceCallRepository.findAll().size();

        // Delete the voiceCall
        restVoiceCallMockMvc.perform(delete("/api/voice-calls/{id}", voiceCall.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VoiceCall> voiceCallList = voiceCallRepository.findAll();
        assertThat(voiceCallList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VoiceCall.class);
        VoiceCall voiceCall1 = new VoiceCall();
        voiceCall1.setId(1L);
        VoiceCall voiceCall2 = new VoiceCall();
        voiceCall2.setId(voiceCall1.getId());
        assertThat(voiceCall1).isEqualTo(voiceCall2);
        voiceCall2.setId(2L);
        assertThat(voiceCall1).isNotEqualTo(voiceCall2);
        voiceCall1.setId(null);
        assertThat(voiceCall1).isNotEqualTo(voiceCall2);
    }
}
