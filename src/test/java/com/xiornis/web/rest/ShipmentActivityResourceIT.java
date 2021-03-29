package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.ShipmentActivity;
import com.xiornis.repository.ShipmentActivityRepository;
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
 * Integration tests for the {@link ShipmentActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentActivityResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipment-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShipmentActivityRepository shipmentActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentActivityMockMvc;

    private ShipmentActivity shipmentActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentActivity createEntity(EntityManager em) {
        ShipmentActivity shipmentActivity = new ShipmentActivity()
            .status(DEFAULT_STATUS)
            .pincode(DEFAULT_PINCODE)
            .location(DEFAULT_LOCATION);
        return shipmentActivity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentActivity createUpdatedEntity(EntityManager em) {
        ShipmentActivity shipmentActivity = new ShipmentActivity()
            .status(UPDATED_STATUS)
            .pincode(UPDATED_PINCODE)
            .location(UPDATED_LOCATION);
        return shipmentActivity;
    }

    @BeforeEach
    public void initTest() {
        shipmentActivity = createEntity(em);
    }

    @Test
    @Transactional
    void createShipmentActivity() throws Exception {
        int databaseSizeBeforeCreate = shipmentActivityRepository.findAll().size();
        // Create the ShipmentActivity
        restShipmentActivityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isCreated());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeCreate + 1);
        ShipmentActivity testShipmentActivity = shipmentActivityList.get(shipmentActivityList.size() - 1);
        assertThat(testShipmentActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testShipmentActivity.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testShipmentActivity.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createShipmentActivityWithExistingId() throws Exception {
        // Create the ShipmentActivity with an existing ID
        shipmentActivity.setId(1L);

        int databaseSizeBeforeCreate = shipmentActivityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentActivityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShipmentActivities() throws Exception {
        // Initialize the database
        shipmentActivityRepository.saveAndFlush(shipmentActivity);

        // Get all the shipmentActivityList
        restShipmentActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getShipmentActivity() throws Exception {
        // Initialize the database
        shipmentActivityRepository.saveAndFlush(shipmentActivity);

        // Get the shipmentActivity
        restShipmentActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentActivity.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getNonExistingShipmentActivity() throws Exception {
        // Get the shipmentActivity
        restShipmentActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShipmentActivity() throws Exception {
        // Initialize the database
        shipmentActivityRepository.saveAndFlush(shipmentActivity);

        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();

        // Update the shipmentActivity
        ShipmentActivity updatedShipmentActivity = shipmentActivityRepository.findById(shipmentActivity.getId()).get();
        // Disconnect from session so that the updates on updatedShipmentActivity are not directly saved in db
        em.detach(updatedShipmentActivity);
        updatedShipmentActivity.status(UPDATED_STATUS).pincode(UPDATED_PINCODE).location(UPDATED_LOCATION);

        restShipmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShipmentActivity))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
        ShipmentActivity testShipmentActivity = shipmentActivityList.get(shipmentActivityList.size() - 1);
        assertThat(testShipmentActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testShipmentActivity.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testShipmentActivity.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingShipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();
        shipmentActivity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();
        shipmentActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();
        shipmentActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentActivityWithPatch() throws Exception {
        // Initialize the database
        shipmentActivityRepository.saveAndFlush(shipmentActivity);

        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();

        // Update the shipmentActivity using partial update
        ShipmentActivity partialUpdatedShipmentActivity = new ShipmentActivity();
        partialUpdatedShipmentActivity.setId(shipmentActivity.getId());

        partialUpdatedShipmentActivity.location(UPDATED_LOCATION);

        restShipmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipmentActivity))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
        ShipmentActivity testShipmentActivity = shipmentActivityList.get(shipmentActivityList.size() - 1);
        assertThat(testShipmentActivity.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testShipmentActivity.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testShipmentActivity.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateShipmentActivityWithPatch() throws Exception {
        // Initialize the database
        shipmentActivityRepository.saveAndFlush(shipmentActivity);

        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();

        // Update the shipmentActivity using partial update
        ShipmentActivity partialUpdatedShipmentActivity = new ShipmentActivity();
        partialUpdatedShipmentActivity.setId(shipmentActivity.getId());

        partialUpdatedShipmentActivity.status(UPDATED_STATUS).pincode(UPDATED_PINCODE).location(UPDATED_LOCATION);

        restShipmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipmentActivity))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
        ShipmentActivity testShipmentActivity = shipmentActivityList.get(shipmentActivityList.size() - 1);
        assertThat(testShipmentActivity.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testShipmentActivity.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testShipmentActivity.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingShipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();
        shipmentActivity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();
        shipmentActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentActivity() throws Exception {
        int databaseSizeBeforeUpdate = shipmentActivityRepository.findAll().size();
        shipmentActivity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipmentActivity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentActivity in the database
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentActivity() throws Exception {
        // Initialize the database
        shipmentActivityRepository.saveAndFlush(shipmentActivity);

        int databaseSizeBeforeDelete = shipmentActivityRepository.findAll().size();

        // Delete the shipmentActivity
        restShipmentActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShipmentActivity> shipmentActivityList = shipmentActivityRepository.findAll();
        assertThat(shipmentActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
