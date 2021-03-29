package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.PickupAddress;
import com.xiornis.repository.PickupAddressRepository;
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
 * Integration tests for the {@link PickupAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PickupAddressResourceIT {

    private static final String DEFAULT_NICK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NICK_NAME = "BBBBBBBBBB";

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

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MOBILE_VERIFIED = false;
    private static final Boolean UPDATED_IS_MOBILE_VERIFIED = true;

    private static final String DEFAULT_OTP = "AAAAAAAAAA";
    private static final String UPDATED_OTP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pickup-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PickupAddressRepository pickupAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPickupAddressMockMvc;

    private PickupAddress pickupAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PickupAddress createEntity(EntityManager em) {
        PickupAddress pickupAddress = new PickupAddress()
            .nickName(DEFAULT_NICK_NAME)
            .mobile(DEFAULT_MOBILE)
            .alternateMobile(DEFAULT_ALTERNATE_MOBILE)
            .email(DEFAULT_EMAIL)
            .addressline1(DEFAULT_ADDRESSLINE_1)
            .addressline2(DEFAULT_ADDRESSLINE_2)
            .pincode(DEFAULT_PINCODE)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .country(DEFAULT_COUNTRY)
            .companyName(DEFAULT_COMPANY_NAME)
            .isMobileVerified(DEFAULT_IS_MOBILE_VERIFIED)
            .otp(DEFAULT_OTP);
        return pickupAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PickupAddress createUpdatedEntity(EntityManager em) {
        PickupAddress pickupAddress = new PickupAddress()
            .nickName(UPDATED_NICK_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .addressline2(UPDATED_ADDRESSLINE_2)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .companyName(UPDATED_COMPANY_NAME)
            .isMobileVerified(UPDATED_IS_MOBILE_VERIFIED)
            .otp(UPDATED_OTP);
        return pickupAddress;
    }

    @BeforeEach
    public void initTest() {
        pickupAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createPickupAddress() throws Exception {
        int databaseSizeBeforeCreate = pickupAddressRepository.findAll().size();
        // Create the PickupAddress
        restPickupAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pickupAddress)))
            .andExpect(status().isCreated());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PickupAddress testPickupAddress = pickupAddressList.get(pickupAddressList.size() - 1);
        assertThat(testPickupAddress.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testPickupAddress.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testPickupAddress.getAlternateMobile()).isEqualTo(DEFAULT_ALTERNATE_MOBILE);
        assertThat(testPickupAddress.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPickupAddress.getAddressline1()).isEqualTo(DEFAULT_ADDRESSLINE_1);
        assertThat(testPickupAddress.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testPickupAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testPickupAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPickupAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testPickupAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPickupAddress.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testPickupAddress.getIsMobileVerified()).isEqualTo(DEFAULT_IS_MOBILE_VERIFIED);
        assertThat(testPickupAddress.getOtp()).isEqualTo(DEFAULT_OTP);
    }

    @Test
    @Transactional
    void createPickupAddressWithExistingId() throws Exception {
        // Create the PickupAddress with an existing ID
        pickupAddress.setId(1L);

        int databaseSizeBeforeCreate = pickupAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPickupAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pickupAddress)))
            .andExpect(status().isBadRequest());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPickupAddresses() throws Exception {
        // Initialize the database
        pickupAddressRepository.saveAndFlush(pickupAddress);

        // Get all the pickupAddressList
        restPickupAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pickupAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].alternateMobile").value(hasItem(DEFAULT_ALTERNATE_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].addressline1").value(hasItem(DEFAULT_ADDRESSLINE_1)))
            .andExpect(jsonPath("$.[*].addressline2").value(hasItem(DEFAULT_ADDRESSLINE_2)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].isMobileVerified").value(hasItem(DEFAULT_IS_MOBILE_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)));
    }

    @Test
    @Transactional
    void getPickupAddress() throws Exception {
        // Initialize the database
        pickupAddressRepository.saveAndFlush(pickupAddress);

        // Get the pickupAddress
        restPickupAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, pickupAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pickupAddress.getId().intValue()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.alternateMobile").value(DEFAULT_ALTERNATE_MOBILE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.addressline1").value(DEFAULT_ADDRESSLINE_1))
            .andExpect(jsonPath("$.addressline2").value(DEFAULT_ADDRESSLINE_2))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.isMobileVerified").value(DEFAULT_IS_MOBILE_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP));
    }

    @Test
    @Transactional
    void getNonExistingPickupAddress() throws Exception {
        // Get the pickupAddress
        restPickupAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPickupAddress() throws Exception {
        // Initialize the database
        pickupAddressRepository.saveAndFlush(pickupAddress);

        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();

        // Update the pickupAddress
        PickupAddress updatedPickupAddress = pickupAddressRepository.findById(pickupAddress.getId()).get();
        // Disconnect from session so that the updates on updatedPickupAddress are not directly saved in db
        em.detach(updatedPickupAddress);
        updatedPickupAddress
            .nickName(UPDATED_NICK_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .addressline2(UPDATED_ADDRESSLINE_2)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .companyName(UPDATED_COMPANY_NAME)
            .isMobileVerified(UPDATED_IS_MOBILE_VERIFIED)
            .otp(UPDATED_OTP);

        restPickupAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPickupAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPickupAddress))
            )
            .andExpect(status().isOk());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
        PickupAddress testPickupAddress = pickupAddressList.get(pickupAddressList.size() - 1);
        assertThat(testPickupAddress.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testPickupAddress.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testPickupAddress.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testPickupAddress.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPickupAddress.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testPickupAddress.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testPickupAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testPickupAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPickupAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPickupAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPickupAddress.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testPickupAddress.getIsMobileVerified()).isEqualTo(UPDATED_IS_MOBILE_VERIFIED);
        assertThat(testPickupAddress.getOtp()).isEqualTo(UPDATED_OTP);
    }

    @Test
    @Transactional
    void putNonExistingPickupAddress() throws Exception {
        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();
        pickupAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPickupAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pickupAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pickupAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPickupAddress() throws Exception {
        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();
        pickupAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickupAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pickupAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPickupAddress() throws Exception {
        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();
        pickupAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickupAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pickupAddress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePickupAddressWithPatch() throws Exception {
        // Initialize the database
        pickupAddressRepository.saveAndFlush(pickupAddress);

        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();

        // Update the pickupAddress using partial update
        PickupAddress partialUpdatedPickupAddress = new PickupAddress();
        partialUpdatedPickupAddress.setId(pickupAddress.getId());

        partialUpdatedPickupAddress
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .companyName(UPDATED_COMPANY_NAME)
            .otp(UPDATED_OTP);

        restPickupAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPickupAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPickupAddress))
            )
            .andExpect(status().isOk());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
        PickupAddress testPickupAddress = pickupAddressList.get(pickupAddressList.size() - 1);
        assertThat(testPickupAddress.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testPickupAddress.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testPickupAddress.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testPickupAddress.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPickupAddress.getAddressline1()).isEqualTo(DEFAULT_ADDRESSLINE_1);
        assertThat(testPickupAddress.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testPickupAddress.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testPickupAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testPickupAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPickupAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPickupAddress.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testPickupAddress.getIsMobileVerified()).isEqualTo(DEFAULT_IS_MOBILE_VERIFIED);
        assertThat(testPickupAddress.getOtp()).isEqualTo(UPDATED_OTP);
    }

    @Test
    @Transactional
    void fullUpdatePickupAddressWithPatch() throws Exception {
        // Initialize the database
        pickupAddressRepository.saveAndFlush(pickupAddress);

        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();

        // Update the pickupAddress using partial update
        PickupAddress partialUpdatedPickupAddress = new PickupAddress();
        partialUpdatedPickupAddress.setId(pickupAddress.getId());

        partialUpdatedPickupAddress
            .nickName(UPDATED_NICK_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .email(UPDATED_EMAIL)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .addressline2(UPDATED_ADDRESSLINE_2)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .country(UPDATED_COUNTRY)
            .companyName(UPDATED_COMPANY_NAME)
            .isMobileVerified(UPDATED_IS_MOBILE_VERIFIED)
            .otp(UPDATED_OTP);

        restPickupAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPickupAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPickupAddress))
            )
            .andExpect(status().isOk());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
        PickupAddress testPickupAddress = pickupAddressList.get(pickupAddressList.size() - 1);
        assertThat(testPickupAddress.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testPickupAddress.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testPickupAddress.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testPickupAddress.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPickupAddress.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testPickupAddress.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testPickupAddress.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testPickupAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testPickupAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testPickupAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPickupAddress.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testPickupAddress.getIsMobileVerified()).isEqualTo(UPDATED_IS_MOBILE_VERIFIED);
        assertThat(testPickupAddress.getOtp()).isEqualTo(UPDATED_OTP);
    }

    @Test
    @Transactional
    void patchNonExistingPickupAddress() throws Exception {
        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();
        pickupAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPickupAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pickupAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pickupAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPickupAddress() throws Exception {
        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();
        pickupAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickupAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pickupAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPickupAddress() throws Exception {
        int databaseSizeBeforeUpdate = pickupAddressRepository.findAll().size();
        pickupAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPickupAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pickupAddress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PickupAddress in the database
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePickupAddress() throws Exception {
        // Initialize the database
        pickupAddressRepository.saveAndFlush(pickupAddress);

        int databaseSizeBeforeDelete = pickupAddressRepository.findAll().size();

        // Delete the pickupAddress
        restPickupAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, pickupAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PickupAddress> pickupAddressList = pickupAddressRepository.findAll();
        assertThat(pickupAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
