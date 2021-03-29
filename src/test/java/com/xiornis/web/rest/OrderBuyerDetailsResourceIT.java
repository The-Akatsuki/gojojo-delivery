package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.OrderBuyerDetails;
import com.xiornis.repository.OrderBuyerDetailsRepository;
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
 * Integration tests for the {@link OrderBuyerDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderBuyerDetailsResourceIT {

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

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_BILLING_SAME = false;
    private static final Boolean UPDATED_IS_BILLING_SAME = true;

    private static final String DEFAULT_BILL_ADDRESSLINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_BILL_ADDRESSLINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_ADDRESSLINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_BILL_ADDRESSLINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_BILL_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_CITY = "AAAAAAAAAA";
    private static final String UPDATED_BILL_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_STATE = "AAAAAAAAAA";
    private static final String UPDATED_BILL_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_BILL_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_BILL_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/order-buyer-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderBuyerDetailsRepository orderBuyerDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderBuyerDetailsMockMvc;

    private OrderBuyerDetails orderBuyerDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderBuyerDetails createEntity(EntityManager em) {
        OrderBuyerDetails orderBuyerDetails = new OrderBuyerDetails()
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
            .companyName(DEFAULT_COMPANY_NAME)
            .isBillingSame(DEFAULT_IS_BILLING_SAME)
            .billAddressline1(DEFAULT_BILL_ADDRESSLINE_1)
            .billAddressline2(DEFAULT_BILL_ADDRESSLINE_2)
            .billPincode(DEFAULT_BILL_PINCODE)
            .billCity(DEFAULT_BILL_CITY)
            .billState(DEFAULT_BILL_STATE)
            .billCountry(DEFAULT_BILL_COUNTRY);
        return orderBuyerDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderBuyerDetails createUpdatedEntity(EntityManager em) {
        OrderBuyerDetails orderBuyerDetails = new OrderBuyerDetails()
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
            .companyName(UPDATED_COMPANY_NAME)
            .isBillingSame(UPDATED_IS_BILLING_SAME)
            .billAddressline1(UPDATED_BILL_ADDRESSLINE_1)
            .billAddressline2(UPDATED_BILL_ADDRESSLINE_2)
            .billPincode(UPDATED_BILL_PINCODE)
            .billCity(UPDATED_BILL_CITY)
            .billState(UPDATED_BILL_STATE)
            .billCountry(UPDATED_BILL_COUNTRY);
        return orderBuyerDetails;
    }

    @BeforeEach
    public void initTest() {
        orderBuyerDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeCreate = orderBuyerDetailsRepository.findAll().size();
        // Create the OrderBuyerDetails
        restOrderBuyerDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isCreated());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        OrderBuyerDetails testOrderBuyerDetails = orderBuyerDetailsList.get(orderBuyerDetailsList.size() - 1);
        assertThat(testOrderBuyerDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrderBuyerDetails.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testOrderBuyerDetails.getAlternateMobile()).isEqualTo(DEFAULT_ALTERNATE_MOBILE);
        assertThat(testOrderBuyerDetails.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrderBuyerDetails.getAddressline1()).isEqualTo(DEFAULT_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testOrderBuyerDetails.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOrderBuyerDetails.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testOrderBuyerDetails.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testOrderBuyerDetails.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOrderBuyerDetails.getIsBillingSame()).isEqualTo(DEFAULT_IS_BILLING_SAME);
        assertThat(testOrderBuyerDetails.getBillAddressline1()).isEqualTo(DEFAULT_BILL_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getBillAddressline2()).isEqualTo(DEFAULT_BILL_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getBillPincode()).isEqualTo(DEFAULT_BILL_PINCODE);
        assertThat(testOrderBuyerDetails.getBillCity()).isEqualTo(DEFAULT_BILL_CITY);
        assertThat(testOrderBuyerDetails.getBillState()).isEqualTo(DEFAULT_BILL_STATE);
        assertThat(testOrderBuyerDetails.getBillCountry()).isEqualTo(DEFAULT_BILL_COUNTRY);
    }

    @Test
    @Transactional
    void createOrderBuyerDetailsWithExistingId() throws Exception {
        // Create the OrderBuyerDetails with an existing ID
        orderBuyerDetails.setId(1L);

        int databaseSizeBeforeCreate = orderBuyerDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderBuyerDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderBuyerDetails() throws Exception {
        // Initialize the database
        orderBuyerDetailsRepository.saveAndFlush(orderBuyerDetails);

        // Get all the orderBuyerDetailsList
        restOrderBuyerDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderBuyerDetails.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].isBillingSame").value(hasItem(DEFAULT_IS_BILLING_SAME.booleanValue())))
            .andExpect(jsonPath("$.[*].billAddressline1").value(hasItem(DEFAULT_BILL_ADDRESSLINE_1)))
            .andExpect(jsonPath("$.[*].billAddressline2").value(hasItem(DEFAULT_BILL_ADDRESSLINE_2)))
            .andExpect(jsonPath("$.[*].billPincode").value(hasItem(DEFAULT_BILL_PINCODE)))
            .andExpect(jsonPath("$.[*].billCity").value(hasItem(DEFAULT_BILL_CITY)))
            .andExpect(jsonPath("$.[*].billState").value(hasItem(DEFAULT_BILL_STATE)))
            .andExpect(jsonPath("$.[*].billCountry").value(hasItem(DEFAULT_BILL_COUNTRY)));
    }

    @Test
    @Transactional
    void getOrderBuyerDetails() throws Exception {
        // Initialize the database
        orderBuyerDetailsRepository.saveAndFlush(orderBuyerDetails);

        // Get the orderBuyerDetails
        restOrderBuyerDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, orderBuyerDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderBuyerDetails.getId().intValue()))
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
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.isBillingSame").value(DEFAULT_IS_BILLING_SAME.booleanValue()))
            .andExpect(jsonPath("$.billAddressline1").value(DEFAULT_BILL_ADDRESSLINE_1))
            .andExpect(jsonPath("$.billAddressline2").value(DEFAULT_BILL_ADDRESSLINE_2))
            .andExpect(jsonPath("$.billPincode").value(DEFAULT_BILL_PINCODE))
            .andExpect(jsonPath("$.billCity").value(DEFAULT_BILL_CITY))
            .andExpect(jsonPath("$.billState").value(DEFAULT_BILL_STATE))
            .andExpect(jsonPath("$.billCountry").value(DEFAULT_BILL_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingOrderBuyerDetails() throws Exception {
        // Get the orderBuyerDetails
        restOrderBuyerDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrderBuyerDetails() throws Exception {
        // Initialize the database
        orderBuyerDetailsRepository.saveAndFlush(orderBuyerDetails);

        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();

        // Update the orderBuyerDetails
        OrderBuyerDetails updatedOrderBuyerDetails = orderBuyerDetailsRepository.findById(orderBuyerDetails.getId()).get();
        // Disconnect from session so that the updates on updatedOrderBuyerDetails are not directly saved in db
        em.detach(updatedOrderBuyerDetails);
        updatedOrderBuyerDetails
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
            .companyName(UPDATED_COMPANY_NAME)
            .isBillingSame(UPDATED_IS_BILLING_SAME)
            .billAddressline1(UPDATED_BILL_ADDRESSLINE_1)
            .billAddressline2(UPDATED_BILL_ADDRESSLINE_2)
            .billPincode(UPDATED_BILL_PINCODE)
            .billCity(UPDATED_BILL_CITY)
            .billState(UPDATED_BILL_STATE)
            .billCountry(UPDATED_BILL_COUNTRY);

        restOrderBuyerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrderBuyerDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrderBuyerDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderBuyerDetails testOrderBuyerDetails = orderBuyerDetailsList.get(orderBuyerDetailsList.size() - 1);
        assertThat(testOrderBuyerDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrderBuyerDetails.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrderBuyerDetails.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testOrderBuyerDetails.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrderBuyerDetails.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testOrderBuyerDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOrderBuyerDetails.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrderBuyerDetails.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testOrderBuyerDetails.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOrderBuyerDetails.getIsBillingSame()).isEqualTo(UPDATED_IS_BILLING_SAME);
        assertThat(testOrderBuyerDetails.getBillAddressline1()).isEqualTo(UPDATED_BILL_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getBillAddressline2()).isEqualTo(UPDATED_BILL_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getBillPincode()).isEqualTo(UPDATED_BILL_PINCODE);
        assertThat(testOrderBuyerDetails.getBillCity()).isEqualTo(UPDATED_BILL_CITY);
        assertThat(testOrderBuyerDetails.getBillState()).isEqualTo(UPDATED_BILL_STATE);
        assertThat(testOrderBuyerDetails.getBillCountry()).isEqualTo(UPDATED_BILL_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();
        orderBuyerDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderBuyerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderBuyerDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();
        orderBuyerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBuyerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();
        orderBuyerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBuyerDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderBuyerDetailsWithPatch() throws Exception {
        // Initialize the database
        orderBuyerDetailsRepository.saveAndFlush(orderBuyerDetails);

        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();

        // Update the orderBuyerDetails using partial update
        OrderBuyerDetails partialUpdatedOrderBuyerDetails = new OrderBuyerDetails();
        partialUpdatedOrderBuyerDetails.setId(orderBuyerDetails.getId());

        partialUpdatedOrderBuyerDetails
            .name(UPDATED_NAME)
            .mobile(UPDATED_MOBILE)
            .alternateMobile(UPDATED_ALTERNATE_MOBILE)
            .addressline1(UPDATED_ADDRESSLINE_1)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .billCountry(UPDATED_BILL_COUNTRY);

        restOrderBuyerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderBuyerDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderBuyerDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderBuyerDetails testOrderBuyerDetails = orderBuyerDetailsList.get(orderBuyerDetailsList.size() - 1);
        assertThat(testOrderBuyerDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrderBuyerDetails.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrderBuyerDetails.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testOrderBuyerDetails.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrderBuyerDetails.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getAddressline2()).isEqualTo(DEFAULT_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testOrderBuyerDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOrderBuyerDetails.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testOrderBuyerDetails.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testOrderBuyerDetails.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testOrderBuyerDetails.getIsBillingSame()).isEqualTo(DEFAULT_IS_BILLING_SAME);
        assertThat(testOrderBuyerDetails.getBillAddressline1()).isEqualTo(DEFAULT_BILL_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getBillAddressline2()).isEqualTo(DEFAULT_BILL_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getBillPincode()).isEqualTo(DEFAULT_BILL_PINCODE);
        assertThat(testOrderBuyerDetails.getBillCity()).isEqualTo(DEFAULT_BILL_CITY);
        assertThat(testOrderBuyerDetails.getBillState()).isEqualTo(DEFAULT_BILL_STATE);
        assertThat(testOrderBuyerDetails.getBillCountry()).isEqualTo(UPDATED_BILL_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateOrderBuyerDetailsWithPatch() throws Exception {
        // Initialize the database
        orderBuyerDetailsRepository.saveAndFlush(orderBuyerDetails);

        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();

        // Update the orderBuyerDetails using partial update
        OrderBuyerDetails partialUpdatedOrderBuyerDetails = new OrderBuyerDetails();
        partialUpdatedOrderBuyerDetails.setId(orderBuyerDetails.getId());

        partialUpdatedOrderBuyerDetails
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
            .companyName(UPDATED_COMPANY_NAME)
            .isBillingSame(UPDATED_IS_BILLING_SAME)
            .billAddressline1(UPDATED_BILL_ADDRESSLINE_1)
            .billAddressline2(UPDATED_BILL_ADDRESSLINE_2)
            .billPincode(UPDATED_BILL_PINCODE)
            .billCity(UPDATED_BILL_CITY)
            .billState(UPDATED_BILL_STATE)
            .billCountry(UPDATED_BILL_COUNTRY);

        restOrderBuyerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderBuyerDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderBuyerDetails))
            )
            .andExpect(status().isOk());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
        OrderBuyerDetails testOrderBuyerDetails = orderBuyerDetailsList.get(orderBuyerDetailsList.size() - 1);
        assertThat(testOrderBuyerDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrderBuyerDetails.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testOrderBuyerDetails.getAlternateMobile()).isEqualTo(UPDATED_ALTERNATE_MOBILE);
        assertThat(testOrderBuyerDetails.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrderBuyerDetails.getAddressline1()).isEqualTo(UPDATED_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getAddressline2()).isEqualTo(UPDATED_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testOrderBuyerDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOrderBuyerDetails.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrderBuyerDetails.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testOrderBuyerDetails.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testOrderBuyerDetails.getIsBillingSame()).isEqualTo(UPDATED_IS_BILLING_SAME);
        assertThat(testOrderBuyerDetails.getBillAddressline1()).isEqualTo(UPDATED_BILL_ADDRESSLINE_1);
        assertThat(testOrderBuyerDetails.getBillAddressline2()).isEqualTo(UPDATED_BILL_ADDRESSLINE_2);
        assertThat(testOrderBuyerDetails.getBillPincode()).isEqualTo(UPDATED_BILL_PINCODE);
        assertThat(testOrderBuyerDetails.getBillCity()).isEqualTo(UPDATED_BILL_CITY);
        assertThat(testOrderBuyerDetails.getBillState()).isEqualTo(UPDATED_BILL_STATE);
        assertThat(testOrderBuyerDetails.getBillCountry()).isEqualTo(UPDATED_BILL_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();
        orderBuyerDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderBuyerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderBuyerDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();
        orderBuyerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBuyerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderBuyerDetails() throws Exception {
        int databaseSizeBeforeUpdate = orderBuyerDetailsRepository.findAll().size();
        orderBuyerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderBuyerDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderBuyerDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderBuyerDetails in the database
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderBuyerDetails() throws Exception {
        // Initialize the database
        orderBuyerDetailsRepository.saveAndFlush(orderBuyerDetails);

        int databaseSizeBeforeDelete = orderBuyerDetailsRepository.findAll().size();

        // Delete the orderBuyerDetails
        restOrderBuyerDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderBuyerDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderBuyerDetails> orderBuyerDetailsList = orderBuyerDetailsRepository.findAll();
        assertThat(orderBuyerDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
