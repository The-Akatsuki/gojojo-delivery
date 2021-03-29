package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.PinCodes;
import com.xiornis.repository.PinCodesRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PinCodesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PinCodesResourceIT {

    private static final String DEFAULT_PIN = "AAAAAAAAAA";
    private static final String UPDATED_PIN = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pin-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PinCodesRepository pinCodesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPinCodesMockMvc;

    private PinCodes pinCodes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PinCodes createEntity(EntityManager em) {
        PinCodes pinCodes = new PinCodes().pin(DEFAULT_PIN).city(DEFAULT_CITY).state(DEFAULT_STATE).country(DEFAULT_COUNTRY);
        return pinCodes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PinCodes createUpdatedEntity(EntityManager em) {
        PinCodes pinCodes = new PinCodes().pin(UPDATED_PIN).city(UPDATED_CITY).state(UPDATED_STATE).country(UPDATED_COUNTRY);
        return pinCodes;
    }

    @BeforeEach
    public void initTest() {
        pinCodes = createEntity(em);
    }

    @Test
    @Transactional
    void createPinCodes() throws Exception {
        int databaseSizeBeforeCreate = pinCodesRepository.findAll().size();
        // Create the PinCodes
        restPinCodesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pinCodes)))
            .andExpect(status().isCreated());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeCreate + 1);
        PinCodes testPinCodes = pinCodesList.get(pinCodesList.size() - 1);
        assertThat(testPinCodes.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testPinCodes.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPinCodes.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPinCodes.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createPinCodesWithExistingId() throws Exception {
        // Create the PinCodes with an existing ID
        pinCodes.setId(1L);

        int databaseSizeBeforeCreate = pinCodesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPinCodesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pinCodes)))
            .andExpect(status().isBadRequest());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPinCodes() throws Exception {
        // Initialize the database
        pinCodesRepository.saveAndFlush(pinCodes);

        // Get all the pinCodesList
        restPinCodesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pinCodes.getId().intValue())))
            .andExpect(jsonPath("$.[*].pin").value(hasItem(DEFAULT_PIN)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getPinCodes() throws Exception {
        // Initialize the database
        pinCodesRepository.saveAndFlush(pinCodes);

        // Get the pinCodes
        restPinCodesMockMvc
            .perform(get(ENTITY_API_URL_ID, pinCodes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pinCodes.getId().intValue()))
            .andExpect(jsonPath("$.pin").value(DEFAULT_PIN))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingPinCodes() throws Exception {
        // Get the pinCodes
        restPinCodesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPinCodes() throws Exception {
        // Initialize the database
        pinCodesRepository.saveAndFlush(pinCodes);

        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();

        // Update the pinCodes
        PinCodes updatedPinCodes = pinCodesRepository.findById(pinCodes.getId()).get();
        // Disconnect from session so that the updates on updatedPinCodes are not directly saved in db
        em.detach(updatedPinCodes);
        updatedPinCodes.pin(UPDATED_PIN).city(UPDATED_CITY).state(UPDATED_STATE).country(UPDATED_COUNTRY);

        restPinCodesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPinCodes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPinCodes))
            )
            .andExpect(status().isOk());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
        PinCodes testPinCodes = pinCodesList.get(pinCodesList.size() - 1);
        assertThat(testPinCodes.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testPinCodes.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPinCodes.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPinCodes.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingPinCodes() throws Exception {
        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();
        pinCodes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPinCodesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pinCodes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pinCodes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPinCodes() throws Exception {
        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();
        pinCodes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPinCodesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pinCodes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPinCodes() throws Exception {
        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();
        pinCodes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPinCodesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pinCodes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePinCodesWithPatch() throws Exception {
        // Initialize the database
        pinCodesRepository.saveAndFlush(pinCodes);

        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();

        // Update the pinCodes using partial update
        PinCodes partialUpdatedPinCodes = new PinCodes();
        partialUpdatedPinCodes.setId(pinCodes.getId());

        partialUpdatedPinCodes.city(UPDATED_CITY).state(UPDATED_STATE).country(UPDATED_COUNTRY);

        restPinCodesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPinCodes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPinCodes))
            )
            .andExpect(status().isOk());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
        PinCodes testPinCodes = pinCodesList.get(pinCodesList.size() - 1);
        assertThat(testPinCodes.getPin()).isEqualTo(DEFAULT_PIN);
        assertThat(testPinCodes.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPinCodes.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPinCodes.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdatePinCodesWithPatch() throws Exception {
        // Initialize the database
        pinCodesRepository.saveAndFlush(pinCodes);

        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();

        // Update the pinCodes using partial update
        PinCodes partialUpdatedPinCodes = new PinCodes();
        partialUpdatedPinCodes.setId(pinCodes.getId());

        partialUpdatedPinCodes.pin(UPDATED_PIN).city(UPDATED_CITY).state(UPDATED_STATE).country(UPDATED_COUNTRY);

        restPinCodesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPinCodes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPinCodes))
            )
            .andExpect(status().isOk());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
        PinCodes testPinCodes = pinCodesList.get(pinCodesList.size() - 1);
        assertThat(testPinCodes.getPin()).isEqualTo(UPDATED_PIN);
        assertThat(testPinCodes.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPinCodes.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPinCodes.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingPinCodes() throws Exception {
        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();
        pinCodes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPinCodesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pinCodes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pinCodes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPinCodes() throws Exception {
        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();
        pinCodes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPinCodesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pinCodes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPinCodes() throws Exception {
        int databaseSizeBeforeUpdate = pinCodesRepository.findAll().size();
        pinCodes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPinCodesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pinCodes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PinCodes in the database
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePinCodes() throws Exception {
        // Initialize the database
        pinCodesRepository.saveAndFlush(pinCodes);

        int databaseSizeBeforeDelete = pinCodesRepository.findAll().size();

        // Delete the pinCodes
        restPinCodesMockMvc
            .perform(delete(ENTITY_API_URL_ID, pinCodes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PinCodes> pinCodesList = pinCodesRepository.findAll();
        assertThat(pinCodesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
