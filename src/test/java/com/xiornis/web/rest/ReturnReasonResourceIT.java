package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.ReturnReason;
import com.xiornis.repository.ReturnReasonRepository;
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
 * Integration tests for the {@link ReturnReasonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReturnReasonResourceIT {

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/return-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReturnReasonRepository returnReasonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnReasonMockMvc;

    private ReturnReason returnReason;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnReason createEntity(EntityManager em) {
        ReturnReason returnReason = new ReturnReason().comment(DEFAULT_COMMENT).image(DEFAULT_IMAGE);
        return returnReason;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnReason createUpdatedEntity(EntityManager em) {
        ReturnReason returnReason = new ReturnReason().comment(UPDATED_COMMENT).image(UPDATED_IMAGE);
        return returnReason;
    }

    @BeforeEach
    public void initTest() {
        returnReason = createEntity(em);
    }

    @Test
    @Transactional
    void createReturnReason() throws Exception {
        int databaseSizeBeforeCreate = returnReasonRepository.findAll().size();
        // Create the ReturnReason
        restReturnReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnReason)))
            .andExpect(status().isCreated());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeCreate + 1);
        ReturnReason testReturnReason = returnReasonList.get(returnReasonList.size() - 1);
        assertThat(testReturnReason.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testReturnReason.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void createReturnReasonWithExistingId() throws Exception {
        // Create the ReturnReason with an existing ID
        returnReason.setId(1L);

        int databaseSizeBeforeCreate = returnReasonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnReasonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnReason)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReturnReasons() throws Exception {
        // Initialize the database
        returnReasonRepository.saveAndFlush(returnReason);

        // Get all the returnReasonList
        restReturnReasonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnReason.getId().intValue())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getReturnReason() throws Exception {
        // Initialize the database
        returnReasonRepository.saveAndFlush(returnReason);

        // Get the returnReason
        restReturnReasonMockMvc
            .perform(get(ENTITY_API_URL_ID, returnReason.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnReason.getId().intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingReturnReason() throws Exception {
        // Get the returnReason
        restReturnReasonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReturnReason() throws Exception {
        // Initialize the database
        returnReasonRepository.saveAndFlush(returnReason);

        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();

        // Update the returnReason
        ReturnReason updatedReturnReason = returnReasonRepository.findById(returnReason.getId()).get();
        // Disconnect from session so that the updates on updatedReturnReason are not directly saved in db
        em.detach(updatedReturnReason);
        updatedReturnReason.comment(UPDATED_COMMENT).image(UPDATED_IMAGE);

        restReturnReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnReason.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReturnReason))
            )
            .andExpect(status().isOk());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
        ReturnReason testReturnReason = returnReasonList.get(returnReasonList.size() - 1);
        assertThat(testReturnReason.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testReturnReason.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingReturnReason() throws Exception {
        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();
        returnReason.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnReason.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(returnReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnReason() throws Exception {
        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();
        returnReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnReasonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(returnReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnReason() throws Exception {
        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();
        returnReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnReasonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnReason)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnReasonWithPatch() throws Exception {
        // Initialize the database
        returnReasonRepository.saveAndFlush(returnReason);

        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();

        // Update the returnReason using partial update
        ReturnReason partialUpdatedReturnReason = new ReturnReason();
        partialUpdatedReturnReason.setId(returnReason.getId());

        restReturnReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReturnReason))
            )
            .andExpect(status().isOk());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
        ReturnReason testReturnReason = returnReasonList.get(returnReasonList.size() - 1);
        assertThat(testReturnReason.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testReturnReason.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateReturnReasonWithPatch() throws Exception {
        // Initialize the database
        returnReasonRepository.saveAndFlush(returnReason);

        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();

        // Update the returnReason using partial update
        ReturnReason partialUpdatedReturnReason = new ReturnReason();
        partialUpdatedReturnReason.setId(returnReason.getId());

        partialUpdatedReturnReason.comment(UPDATED_COMMENT).image(UPDATED_IMAGE);

        restReturnReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReturnReason))
            )
            .andExpect(status().isOk());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
        ReturnReason testReturnReason = returnReasonList.get(returnReasonList.size() - 1);
        assertThat(testReturnReason.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testReturnReason.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingReturnReason() throws Exception {
        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();
        returnReason.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnReason.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnReason() throws Exception {
        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();
        returnReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnReasonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnReason))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnReason() throws Exception {
        int databaseSizeBeforeUpdate = returnReasonRepository.findAll().size();
        returnReason.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnReasonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(returnReason))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnReason in the database
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnReason() throws Exception {
        // Initialize the database
        returnReasonRepository.saveAndFlush(returnReason);

        int databaseSizeBeforeDelete = returnReasonRepository.findAll().size();

        // Delete the returnReason
        restReturnReasonMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnReason.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReturnReason> returnReasonList = returnReasonRepository.findAll();
        assertThat(returnReasonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
