package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.ReturnLabel;
import com.xiornis.repository.ReturnLabelRepository;
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
 * Integration tests for the {@link ReturnLabelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReturnLabelResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/return-labels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReturnLabelRepository returnLabelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnLabelMockMvc;

    private ReturnLabel returnLabel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnLabel createEntity(EntityManager em) {
        ReturnLabel returnLabel = new ReturnLabel().title(DEFAULT_TITLE);
        return returnLabel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnLabel createUpdatedEntity(EntityManager em) {
        ReturnLabel returnLabel = new ReturnLabel().title(UPDATED_TITLE);
        return returnLabel;
    }

    @BeforeEach
    public void initTest() {
        returnLabel = createEntity(em);
    }

    @Test
    @Transactional
    void createReturnLabel() throws Exception {
        int databaseSizeBeforeCreate = returnLabelRepository.findAll().size();
        // Create the ReturnLabel
        restReturnLabelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnLabel)))
            .andExpect(status().isCreated());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeCreate + 1);
        ReturnLabel testReturnLabel = returnLabelList.get(returnLabelList.size() - 1);
        assertThat(testReturnLabel.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createReturnLabelWithExistingId() throws Exception {
        // Create the ReturnLabel with an existing ID
        returnLabel.setId(1L);

        int databaseSizeBeforeCreate = returnLabelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnLabelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnLabel)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReturnLabels() throws Exception {
        // Initialize the database
        returnLabelRepository.saveAndFlush(returnLabel);

        // Get all the returnLabelList
        restReturnLabelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnLabel.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getReturnLabel() throws Exception {
        // Initialize the database
        returnLabelRepository.saveAndFlush(returnLabel);

        // Get the returnLabel
        restReturnLabelMockMvc
            .perform(get(ENTITY_API_URL_ID, returnLabel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnLabel.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingReturnLabel() throws Exception {
        // Get the returnLabel
        restReturnLabelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReturnLabel() throws Exception {
        // Initialize the database
        returnLabelRepository.saveAndFlush(returnLabel);

        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();

        // Update the returnLabel
        ReturnLabel updatedReturnLabel = returnLabelRepository.findById(returnLabel.getId()).get();
        // Disconnect from session so that the updates on updatedReturnLabel are not directly saved in db
        em.detach(updatedReturnLabel);
        updatedReturnLabel.title(UPDATED_TITLE);

        restReturnLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReturnLabel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReturnLabel))
            )
            .andExpect(status().isOk());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
        ReturnLabel testReturnLabel = returnLabelList.get(returnLabelList.size() - 1);
        assertThat(testReturnLabel.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingReturnLabel() throws Exception {
        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();
        returnLabel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, returnLabel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(returnLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReturnLabel() throws Exception {
        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();
        returnLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnLabelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(returnLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReturnLabel() throws Exception {
        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();
        returnLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnLabelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(returnLabel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReturnLabelWithPatch() throws Exception {
        // Initialize the database
        returnLabelRepository.saveAndFlush(returnLabel);

        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();

        // Update the returnLabel using partial update
        ReturnLabel partialUpdatedReturnLabel = new ReturnLabel();
        partialUpdatedReturnLabel.setId(returnLabel.getId());

        restReturnLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReturnLabel))
            )
            .andExpect(status().isOk());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
        ReturnLabel testReturnLabel = returnLabelList.get(returnLabelList.size() - 1);
        assertThat(testReturnLabel.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateReturnLabelWithPatch() throws Exception {
        // Initialize the database
        returnLabelRepository.saveAndFlush(returnLabel);

        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();

        // Update the returnLabel using partial update
        ReturnLabel partialUpdatedReturnLabel = new ReturnLabel();
        partialUpdatedReturnLabel.setId(returnLabel.getId());

        partialUpdatedReturnLabel.title(UPDATED_TITLE);

        restReturnLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReturnLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReturnLabel))
            )
            .andExpect(status().isOk());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
        ReturnLabel testReturnLabel = returnLabelList.get(returnLabelList.size() - 1);
        assertThat(testReturnLabel.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingReturnLabel() throws Exception {
        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();
        returnLabel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, returnLabel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReturnLabel() throws Exception {
        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();
        returnLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnLabelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(returnLabel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReturnLabel() throws Exception {
        int databaseSizeBeforeUpdate = returnLabelRepository.findAll().size();
        returnLabel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReturnLabelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(returnLabel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReturnLabel in the database
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReturnLabel() throws Exception {
        // Initialize the database
        returnLabelRepository.saveAndFlush(returnLabel);

        int databaseSizeBeforeDelete = returnLabelRepository.findAll().size();

        // Delete the returnLabel
        restReturnLabelMockMvc
            .perform(delete(ENTITY_API_URL_ID, returnLabel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReturnLabel> returnLabelList = returnLabelRepository.findAll();
        assertThat(returnLabelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
