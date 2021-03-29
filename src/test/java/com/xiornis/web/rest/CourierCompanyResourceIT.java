package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.CourierCompany;
import com.xiornis.repository.CourierCompanyRepository;
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
 * Integration tests for the {@link CourierCompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourierCompanyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_ALTERNATE_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATE_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSLINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSLINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSLINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSLINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/courier-companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourierCompanyRepository courierCompanyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourierCompanyMockMvc;

    private CourierCompany courierCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourierCompany createEntity(EntityManager em) {
        CourierCompany courierCompany = new CourierCompany()
            .name(DEFAULT_NAME)
            .mobile(DEFAULT_MOBILE)
            .alternateMobile(DEFAULT_ALTERNATE_MOBILE)
            .email(DEFAULT_EMAIL)
            .addressline1(DEFAULT_ADDRESSLINE_1)
            .addressline2(DEFAULT_ADDRESSLINE_2)
            .pincode(DEFAULT_PINCODE)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .image(DEFAULT_IMAGE);
        return courierCompany;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourierCompany createUpdatedEntity(EntityManager em) {
        CourierCompany courierCompany = new CourierCompany()
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .addressline2(UPDATED_ADDRESSLINE_2)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE);
        return courierCompany;
    }

    @BeforeEach
    public void initTest() {
        courierCompany = createEntity(em);
    }

    @Test
    @Transactional
    void createCourierCompany() throws Exception {
        int databaseSizeBeforeCreate = courierCompanyRepository.findAll().size();
        // Create the CourierCompany
        restCourierCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isCreated());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        CourierCompany testCourierCompany = courierCompanyList.get(courierCompanyList.size() - 1);
        assertThat(testCourierCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourierCompany.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testCourierCompany.getAlternateMobile()).isEqualTo(DEFAULT_ALTERNATE_MOBILE);
        assertThat(testCourierCompany.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCourierCompany.getAddressline1()).isEqualTo(DEFAULT_ADDRESSLINE_1);
        assertThat(testCourierCompany.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testCourierCompany.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testCourierCompany.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCourierCompany.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCourierCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCourierCompany.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void createCourierCompanyWithExistingId() throws Exception {
        // Create the CourierCompany with an existing ID
        courierCompany.setId(1L);

        int databaseSizeBeforeCreate = courierCompanyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourierCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCourierCompanies() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        // Get all the courierCompanyList
        restCourierCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courierCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].alternateMobile").value(hasItem(DEFAULT_ALTERNATE_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].addressline1").value(hasItem(DEFAULT_ADDRESSLINE_1)))
            .andExpect(jsonPath("$.[*].addressline2").value(hasItem(DEFAULT_ADDRESSLINE_2)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getCourierCompany() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        // Get the courierCompany
        restCourierCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, courierCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courierCompany.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.alternateMobile").value(DEFAULT_ALTERNATE_MOBILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.addressline1").value(DEFAULT_ADDRESSLINE_1))
            .andExpect(jsonPath("$.addressline2").value(DEFAULT_ADDRESSLINE_2))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingCourierCompany() throws Exception {
        // Get the courierCompany
        restCourierCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourierCompany() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();

        // Update the courierCompany
        CourierCompany updatedCourierCompany = courierCompanyRepository.findById(courierCompany.getId()).get();
        // Disconnect from session so that the updates on updatedCourierCompany are not directly saved in db
        em.detach(updatedCourierCompany);
        updatedCourierCompany
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .addressline2(UPDATED_ADDRESSLINE_2)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE);

        restCourierCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourierCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourierCompany))
            )
            .andExpect(status().isOk());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
        CourierCompany testCourierCompany = courierCompanyList.get(courierCompanyList.size() - 1);
        assertThat(testCourierCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourierCompany.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testCourierCompany.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testCourierCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCourierCompany.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testCourierCompany.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testCourierCompany.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testCourierCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCourierCompany.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCourierCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCourierCompany.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();
        courierCompany.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courierCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();
        courierCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();
        courierCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierCompany)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourierCompanyWithPatch() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();

        // Update the courierCompany using partial update
        CourierCompany partialUpdatedCourierCompany = new CourierCompany();
        partialUpdatedCourierCompany.setId(courierCompany.getId());

        partialUpdatedCourierCompany
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY);

        restCourierCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourierCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourierCompany))
            )
            .andExpect(status().isOk());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
        CourierCompany testCourierCompany = courierCompanyList.get(courierCompanyList.size() - 1);
        assertThat(testCourierCompany.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourierCompany.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testCourierCompany.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testCourierCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCourierCompany.getAddressline1()).isEqualTo(DEFAULT_ADDRESSLINE_1);
        assertThat(testCourierCompany.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testCourierCompany.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testCourierCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCourierCompany.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCourierCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCourierCompany.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateCourierCompanyWithPatch() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();

        // Update the courierCompany using partial update
        CourierCompany partialUpdatedCourierCompany = new CourierCompany();
        partialUpdatedCourierCompany.setId(courierCompany.getId());

        partialUpdatedCourierCompany
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .addressline2(UPDATED_ADDRESSLINE_2)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .image(UPDATED_IMAGE);

        restCourierCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourierCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourierCompany))
            )
            .andExpect(status().isOk());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
        CourierCompany testCourierCompany = courierCompanyList.get(courierCompanyList.size() - 1);
        assertThat(testCourierCompany.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourierCompany.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testCourierCompany.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testCourierCompany.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCourierCompany.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testCourierCompany.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testCourierCompany.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testCourierCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCourierCompany.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCourierCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCourierCompany.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();
        courierCompany.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courierCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();
        courierCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourierCompany() throws Exception {
        int databaseSizeBeforeUpdate = courierCompanyRepository.findAll().size();
        courierCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courierCompany))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourierCompany in the database
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourierCompany() throws Exception {
        // Initialize the database
        courierCompanyRepository.saveAndFlush(courierCompany);

        int databaseSizeBeforeDelete = courierCompanyRepository.findAll().size();

        // Delete the courierCompany
        restCourierCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, courierCompany.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourierCompany> courierCompanyList = courierCompanyRepository.findAll();
        assertThat(courierCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
