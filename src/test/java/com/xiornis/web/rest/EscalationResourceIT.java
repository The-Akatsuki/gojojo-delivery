package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.Escalation;
import com.xiornis.repository.EscalationRepository;
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
 * Integration tests for the {@link EscalationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EscalationResourceIT {

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/escalations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EscalationRepository escalationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEscalationMockMvc;

    private Escalation escalation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Escalation createEntity(EntityManager em) {
        Escalation escalation = new Escalation().remark(DEFAULT_REMARK);
        return escalation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Escalation createUpdatedEntity(EntityManager em) {
        Escalation escalation = new Escalation().remark(UPDATED_REMARK);
        return escalation;
    }

    @BeforeEach
    public void initTest() {
        escalation = createEntity(em);
    }

    @Test
    @Transactional
    void createEscalation() throws Exception {
        int databaseSizeBeforeCreate = escalationRepository.findAll().size();
        // Create the Escalation
        restEscalationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(escalation)))
            .andExpect(status().isCreated());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeCreate + 1);
        Escalation testEscalation = escalationList.get(escalationList.size() - 1);
        assertThat(testEscalation.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    void createEscalationWithExistingId() throws Exception {
        // Create the Escalation with an existing ID
        escalation.setId(1L);

        int databaseSizeBeforeCreate = escalationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEscalationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(escalation)))
            .andExpect(status().isBadRequest());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEscalations() throws Exception {
        // Initialize the database
        escalationRepository.saveAndFlush(escalation);

        // Get all the escalationList
        restEscalationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(escalation.getId().intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)));
    }

    @Test
    @Transactional
    void getEscalation() throws Exception {
        // Initialize the database
        escalationRepository.saveAndFlush(escalation);

        // Get the escalation
        restEscalationMockMvc
            .perform(get(ENTITY_API_URL_ID, escalation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(escalation.getId().intValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK));
    }

    @Test
    @Transactional
    void getNonExistingEscalation() throws Exception {
        // Get the escalation
        restEscalationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEscalation() throws Exception {
        // Initialize the database
        escalationRepository.saveAndFlush(escalation);

        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();

        // Update the escalation
        Escalation updatedEscalation = escalationRepository.findById(escalation.getId()).get();
        // Disconnect from session so that the updates on updatedEscalation are not directly saved in db
        em.detach(updatedEscalation);
        updatedEscalation.remark(UPDATED_REMARK);

        restEscalationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEscalation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEscalation))
            )
            .andExpect(status().isOk());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
        Escalation testEscalation = escalationList.get(escalationList.size() - 1);
        assertThat(testEscalation.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void putNonExistingEscalation() throws Exception {
        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();
        escalation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEscalationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, escalation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(escalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEscalation() throws Exception {
        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();
        escalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscalationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(escalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEscalation() throws Exception {
        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();
        escalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscalationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(escalation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEscalationWithPatch() throws Exception {
        // Initialize the database
        escalationRepository.saveAndFlush(escalation);

        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();

        // Update the escalation using partial update
        Escalation partialUpdatedEscalation = new Escalation();
        partialUpdatedEscalation.setId(escalation.getId());

        partialUpdatedEscalation.remark(UPDATED_REMARK);

        restEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEscalation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEscalation))
            )
            .andExpect(status().isOk());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
        Escalation testEscalation = escalationList.get(escalationList.size() - 1);
        assertThat(testEscalation.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void fullUpdateEscalationWithPatch() throws Exception {
        // Initialize the database
        escalationRepository.saveAndFlush(escalation);

        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();

        // Update the escalation using partial update
        Escalation partialUpdatedEscalation = new Escalation();
        partialUpdatedEscalation.setId(escalation.getId());

        partialUpdatedEscalation.remark(UPDATED_REMARK);

        restEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEscalation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEscalation))
            )
            .andExpect(status().isOk());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
        Escalation testEscalation = escalationList.get(escalationList.size() - 1);
        assertThat(testEscalation.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    void patchNonExistingEscalation() throws Exception {
        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();
        escalation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, escalation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(escalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEscalation() throws Exception {
        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();
        escalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(escalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEscalation() throws Exception {
        int databaseSizeBeforeUpdate = escalationRepository.findAll().size();
        escalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(escalation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Escalation in the database
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEscalation() throws Exception {
        // Initialize the database
        escalationRepository.saveAndFlush(escalation);

        int databaseSizeBeforeDelete = escalationRepository.findAll().size();

        // Delete the escalation
        restEscalationMockMvc
            .perform(delete(ENTITY_API_URL_ID, escalation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Escalation> escalationList = escalationRepository.findAll();
        assertThat(escalationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
