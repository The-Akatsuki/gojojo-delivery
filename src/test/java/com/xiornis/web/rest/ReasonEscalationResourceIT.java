package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.ReasonEscalation;
import com.xiornis.repository.ReasonEscalationRepository;
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
 * Integration tests for the {@link ReasonEscalationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReasonEscalationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reason-escalations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReasonEscalationRepository reasonEscalationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReasonEscalationMockMvc;

    private ReasonEscalation reasonEscalation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReasonEscalation createEntity(EntityManager em) {
        ReasonEscalation reasonEscalation = new ReasonEscalation().title(DEFAULT_TITLE);
        return reasonEscalation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReasonEscalation createUpdatedEntity(EntityManager em) {
        ReasonEscalation reasonEscalation = new ReasonEscalation().title(UPDATED_TITLE);
        return reasonEscalation;
    }

    @BeforeEach
    public void initTest() {
        reasonEscalation = createEntity(em);
    }

    @Test
    @Transactional
    void createReasonEscalation() throws Exception {
        int databaseSizeBeforeCreate = reasonEscalationRepository.findAll().size();
        // Create the ReasonEscalation
        restReasonEscalationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isCreated());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeCreate + 1);
        ReasonEscalation testReasonEscalation = reasonEscalationList.get(reasonEscalationList.size() - 1);
        assertThat(testReasonEscalation.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createReasonEscalationWithExistingId() throws Exception {
        // Create the ReasonEscalation with an existing ID
        reasonEscalation.setId(1L);

        int databaseSizeBeforeCreate = reasonEscalationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReasonEscalationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReasonEscalations() throws Exception {
        // Initialize the database
        reasonEscalationRepository.saveAndFlush(reasonEscalation);

        // Get all the reasonEscalationList
        restReasonEscalationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reasonEscalation.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getReasonEscalation() throws Exception {
        // Initialize the database
        reasonEscalationRepository.saveAndFlush(reasonEscalation);

        // Get the reasonEscalation
        restReasonEscalationMockMvc
            .perform(get(ENTITY_API_URL_ID, reasonEscalation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reasonEscalation.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingReasonEscalation() throws Exception {
        // Get the reasonEscalation
        restReasonEscalationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReasonEscalation() throws Exception {
        // Initialize the database
        reasonEscalationRepository.saveAndFlush(reasonEscalation);

        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();

        // Update the reasonEscalation
        ReasonEscalation updatedReasonEscalation = reasonEscalationRepository.findById(reasonEscalation.getId()).get();
        // Disconnect from session so that the updates on updatedReasonEscalation are not directly saved in db
        em.detach(updatedReasonEscalation);
        updatedReasonEscalation.title(UPDATED_TITLE);

        restReasonEscalationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReasonEscalation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReasonEscalation))
            )
            .andExpect(status().isOk());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
        ReasonEscalation testReasonEscalation = reasonEscalationList.get(reasonEscalationList.size() - 1);
        assertThat(testReasonEscalation.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingReasonEscalation() throws Exception {
        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();
        reasonEscalation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReasonEscalationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reasonEscalation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReasonEscalation() throws Exception {
        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();
        reasonEscalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonEscalationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReasonEscalation() throws Exception {
        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();
        reasonEscalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonEscalationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReasonEscalationWithPatch() throws Exception {
        // Initialize the database
        reasonEscalationRepository.saveAndFlush(reasonEscalation);

        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();

        // Update the reasonEscalation using partial update
        ReasonEscalation partialUpdatedReasonEscalation = new ReasonEscalation();
        partialUpdatedReasonEscalation.setId(reasonEscalation.getId());

        restReasonEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReasonEscalation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReasonEscalation))
            )
            .andExpect(status().isOk());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
        ReasonEscalation testReasonEscalation = reasonEscalationList.get(reasonEscalationList.size() - 1);
        assertThat(testReasonEscalation.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateReasonEscalationWithPatch() throws Exception {
        // Initialize the database
        reasonEscalationRepository.saveAndFlush(reasonEscalation);

        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();

        // Update the reasonEscalation using partial update
        ReasonEscalation partialUpdatedReasonEscalation = new ReasonEscalation();
        partialUpdatedReasonEscalation.setId(reasonEscalation.getId());

        partialUpdatedReasonEscalation.title(UPDATED_TITLE);

        restReasonEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReasonEscalation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReasonEscalation))
            )
            .andExpect(status().isOk());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
        ReasonEscalation testReasonEscalation = reasonEscalationList.get(reasonEscalationList.size() - 1);
        assertThat(testReasonEscalation.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingReasonEscalation() throws Exception {
        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();
        reasonEscalation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReasonEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reasonEscalation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReasonEscalation() throws Exception {
        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();
        reasonEscalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReasonEscalation() throws Exception {
        int databaseSizeBeforeUpdate = reasonEscalationRepository.findAll().size();
        reasonEscalation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonEscalationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonEscalation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReasonEscalation in the database
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReasonEscalation() throws Exception {
        // Initialize the database
        reasonEscalationRepository.saveAndFlush(reasonEscalation);

        int databaseSizeBeforeDelete = reasonEscalationRepository.findAll().size();

        // Delete the reasonEscalation
        restReasonEscalationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reasonEscalation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReasonEscalation> reasonEscalationList = reasonEscalationRepository.findAll();
        assertThat(reasonEscalationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
