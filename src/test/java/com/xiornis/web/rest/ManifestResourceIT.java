package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.Manifest;
import com.xiornis.repository.ManifestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ManifestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManifestResourceIT {

    private static final String DEFAULT_MANIFEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_MANIFEST_ID = "BBBBBBBBBB";

    private static final String DEFAULT_AWB = "AAAAAAAAAA";
    private static final String UPDATED_AWB = "BBBBBBBBBB";

    private static final Instant DEFAULT_AWB_ASSIGNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AWB_ASSIGNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PICKUP_EXCEPTION = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_EXCEPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_PICKUP_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/manifests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ManifestRepository manifestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManifestMockMvc;

    private Manifest manifest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manifest createEntity(EntityManager em) {
        Manifest manifest = new Manifest()
            .manifestId(DEFAULT_MANIFEST_ID)
            .awb(DEFAULT_AWB)
            .awbAssignedDate(DEFAULT_AWB_ASSIGNED_DATE)
            .pickupException(DEFAULT_PICKUP_EXCEPTION)
            .remarks(DEFAULT_REMARKS)
            .pickupReferenceNumber(DEFAULT_PICKUP_REFERENCE_NUMBER)
            .status(DEFAULT_STATUS);
        return manifest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manifest createUpdatedEntity(EntityManager em) {
        Manifest manifest = new Manifest()
            .manifestId(UPDATED_MANIFEST_ID)
            .awb(UPDATED_AWB)
            .awbAssignedDate(UPDATED_AWB_ASSIGNED_DATE)
            .pickupException(UPDATED_PICKUP_EXCEPTION)
            .remarks(UPDATED_REMARKS)
            .pickupReferenceNumber(UPDATED_PICKUP_REFERENCE_NUMBER)
            .status(UPDATED_STATUS);
        return manifest;
    }

    @BeforeEach
    public void initTest() {
        manifest = createEntity(em);
    }

    @Test
    @Transactional
    void createManifest() throws Exception {
        int databaseSizeBeforeCreate = manifestRepository.findAll().size();
        // Create the Manifest
        restManifestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manifest)))
            .andExpect(status().isCreated());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeCreate + 1);
        Manifest testManifest = manifestList.get(manifestList.size() - 1);
        assertThat(testManifest.getManifestId()).isEqualTo(DEFAULT_MANIFEST_ID);
        assertThat(testManifest.getAwb()).isEqualTo(DEFAULT_AWB);
        assertThat(testManifest.getAwbAssignedDate()).isEqualTo(DEFAULT_AWB_ASSIGNED_DATE);
        assertThat(testManifest.getPickupException()).isEqualTo(DEFAULT_PICKUP_EXCEPTION);
        assertThat(testManifest.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testManifest.getPickupReferenceNumber()).isEqualTo(DEFAULT_PICKUP_REFERENCE_NUMBER);
        assertThat(testManifest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createManifestWithExistingId() throws Exception {
        // Create the Manifest with an existing ID
        manifest.setId(1L);

        int databaseSizeBeforeCreate = manifestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManifestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manifest)))
            .andExpect(status().isBadRequest());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllManifests() throws Exception {
        // Initialize the database
        manifestRepository.saveAndFlush(manifest);

        // Get all the manifestList
        restManifestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manifest.getId().intValue())))
            .andExpect(jsonPath("$.[*].manifestId").value(hasItem(DEFAULT_MANIFEST_ID)))
            .andExpect(jsonPath("$.[*].awb").value(hasItem(DEFAULT_AWB)))
            .andExpect(jsonPath("$.[*].awbAssignedDate").value(hasItem(DEFAULT_AWB_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].pickupException").value(hasItem(DEFAULT_PICKUP_EXCEPTION)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)))
            .andExpect(jsonPath("$.[*].pickupReferenceNumber").value(hasItem(DEFAULT_PICKUP_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getManifest() throws Exception {
        // Initialize the database
        manifestRepository.saveAndFlush(manifest);

        // Get the manifest
        restManifestMockMvc
            .perform(get(ENTITY_API_URL_ID, manifest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manifest.getId().intValue()))
            .andExpect(jsonPath("$.manifestId").value(DEFAULT_MANIFEST_ID))
            .andExpect(jsonPath("$.awb").value(DEFAULT_AWB))
            .andExpect(jsonPath("$.awbAssignedDate").value(DEFAULT_AWB_ASSIGNED_DATE.toString()))
            .andExpect(jsonPath("$.pickupException").value(DEFAULT_PICKUP_EXCEPTION))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS))
            .andExpect(jsonPath("$.pickupReferenceNumber").value(DEFAULT_PICKUP_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingManifest() throws Exception {
        // Get the manifest
        restManifestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewManifest() throws Exception {
        // Initialize the database
        manifestRepository.saveAndFlush(manifest);

        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();

        // Update the manifest
        Manifest updatedManifest = manifestRepository.findById(manifest.getId()).get();
        // Disconnect from session so that the updates on updatedManifest are not directly saved in db
        em.detach(updatedManifest);
        updatedManifest
            .manifestId(UPDATED_MANIFEST_ID)
            .awb(UPDATED_AWB)
            .awbAssignedDate(UPDATED_AWB_ASSIGNED_DATE)
            .pickupException(UPDATED_PICKUP_EXCEPTION)
            .remarks(UPDATED_REMARKS)
            .pickupReferenceNumber(UPDATED_PICKUP_REFERENCE_NUMBER)
            .status(UPDATED_STATUS);

        restManifestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedManifest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedManifest))
            )
            .andExpect(status().isOk());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
        Manifest testManifest = manifestList.get(manifestList.size() - 1);
        assertThat(testManifest.getManifestId()).isEqualTo(UPDATED_MANIFEST_ID);
        assertThat(testManifest.getAwb()).isEqualTo(UPDATED_AWB);
        assertThat(testManifest.getAwbAssignedDate()).isEqualTo(UPDATED_AWB_ASSIGNED_DATE);
        assertThat(testManifest.getPickupException()).isEqualTo(UPDATED_PICKUP_EXCEPTION);
        assertThat(testManifest.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testManifest.getPickupReferenceNumber()).isEqualTo(UPDATED_PICKUP_REFERENCE_NUMBER);
        assertThat(testManifest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingManifest() throws Exception {
        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();
        manifest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManifestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manifest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manifest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManifest() throws Exception {
        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();
        manifest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManifestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(manifest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManifest() throws Exception {
        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();
        manifest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManifestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(manifest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManifestWithPatch() throws Exception {
        // Initialize the database
        manifestRepository.saveAndFlush(manifest);

        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();

        // Update the manifest using partial update
        Manifest partialUpdatedManifest = new Manifest();
        partialUpdatedManifest.setId(manifest.getId());

        partialUpdatedManifest
            .manifestId(UPDATED_MANIFEST_ID)
            .awb(UPDATED_AWB)
            .awbAssignedDate(UPDATED_AWB_ASSIGNED_DATE)
            .pickupException(UPDATED_PICKUP_EXCEPTION)
            .remarks(UPDATED_REMARKS);

        restManifestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManifest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManifest))
            )
            .andExpect(status().isOk());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
        Manifest testManifest = manifestList.get(manifestList.size() - 1);
        assertThat(testManifest.getManifestId()).isEqualTo(UPDATED_MANIFEST_ID);
        assertThat(testManifest.getAwb()).isEqualTo(UPDATED_AWB);
        assertThat(testManifest.getAwbAssignedDate()).isEqualTo(UPDATED_AWB_ASSIGNED_DATE);
        assertThat(testManifest.getPickupException()).isEqualTo(UPDATED_PICKUP_EXCEPTION);
        assertThat(testManifest.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testManifest.getPickupReferenceNumber()).isEqualTo(DEFAULT_PICKUP_REFERENCE_NUMBER);
        assertThat(testManifest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateManifestWithPatch() throws Exception {
        // Initialize the database
        manifestRepository.saveAndFlush(manifest);

        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();

        // Update the manifest using partial update
        Manifest partialUpdatedManifest = new Manifest();
        partialUpdatedManifest.setId(manifest.getId());

        partialUpdatedManifest
            .manifestId(UPDATED_MANIFEST_ID)
            .awb(UPDATED_AWB)
            .awbAssignedDate(UPDATED_AWB_ASSIGNED_DATE)
            .pickupException(UPDATED_PICKUP_EXCEPTION)
            .remarks(UPDATED_REMARKS)
            .pickupReferenceNumber(UPDATED_PICKUP_REFERENCE_NUMBER)
            .status(UPDATED_STATUS);

        restManifestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManifest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedManifest))
            )
            .andExpect(status().isOk());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
        Manifest testManifest = manifestList.get(manifestList.size() - 1);
        assertThat(testManifest.getManifestId()).isEqualTo(UPDATED_MANIFEST_ID);
        assertThat(testManifest.getAwb()).isEqualTo(UPDATED_AWB);
        assertThat(testManifest.getAwbAssignedDate()).isEqualTo(UPDATED_AWB_ASSIGNED_DATE);
        assertThat(testManifest.getPickupException()).isEqualTo(UPDATED_PICKUP_EXCEPTION);
        assertThat(testManifest.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testManifest.getPickupReferenceNumber()).isEqualTo(UPDATED_PICKUP_REFERENCE_NUMBER);
        assertThat(testManifest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingManifest() throws Exception {
        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();
        manifest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManifestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manifest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manifest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManifest() throws Exception {
        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();
        manifest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManifestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(manifest))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManifest() throws Exception {
        int databaseSizeBeforeUpdate = manifestRepository.findAll().size();
        manifest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManifestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(manifest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manifest in the database
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManifest() throws Exception {
        // Initialize the database
        manifestRepository.saveAndFlush(manifest);

        int databaseSizeBeforeDelete = manifestRepository.findAll().size();

        // Delete the manifest
        restManifestMockMvc
            .perform(delete(ENTITY_API_URL_ID, manifest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Manifest> manifestList = manifestRepository.findAll();
        assertThat(manifestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
