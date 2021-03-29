package com.xiornis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.xiornis.IntegrationTest;
import com.xiornis.domain.Order;
import com.xiornis.domain.enumeration.OrderStatus;
import com.xiornis.domain.enumeration.OrderType;
import com.xiornis.repository.OrderRepository;
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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final String DEFAULT_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_ID = "BBBBBBBBBB";

    private static final OrderType DEFAULT_ORDER_TYPE = OrderType.ForwardOrder;
    private static final OrderType UPDATED_ORDER_TYPE = OrderType.ReturnOrder;

    private static final OrderStatus DEFAULT_ORDER_STATUS = OrderStatus.Processing;
    private static final OrderStatus UPDATED_ORDER_STATUS = OrderStatus.ReadyToShip;

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL = "BBBBBBBBBB";

    private static final Double DEFAULT_SUBTOTAL = 1D;
    private static final Double UPDATED_SUBTOTAL = 2D;

    private static final Boolean DEFAULT_HAS_SHIPPING_CHARGES = false;
    private static final Boolean UPDATED_HAS_SHIPPING_CHARGES = true;

    private static final Double DEFAULT_SHIPPING = 1D;
    private static final Double UPDATED_SHIPPING = 2D;

    private static final Boolean DEFAULT_HAS_GIFTWRAP_CHARGES = false;
    private static final Boolean UPDATED_HAS_GIFTWRAP_CHARGES = true;

    private static final Double DEFAULT_GIFTWRAP = 1D;
    private static final Double UPDATED_GIFTWRAP = 2D;

    private static final Boolean DEFAULT_HAS_TRANSACTION_CHARGES = false;
    private static final Boolean UPDATED_HAS_TRANSACTION_CHARGES = true;

    private static final Double DEFAULT_TRANSACTION = 1D;
    private static final Double UPDATED_TRANSACTION = 2D;

    private static final Boolean DEFAULT_HAS_DISCOUNT = false;
    private static final Boolean UPDATED_HAS_DISCOUNT = true;

    private static final Double DEFAULT_DISCOUNT = 1D;
    private static final Double UPDATED_DISCOUNT = 2D;

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;

    private static final Float DEFAULT_WEIGHT_CHARGES = 1F;
    private static final Float UPDATED_WEIGHT_CHARGES = 2F;

    private static final Float DEFAULT_EXCESS_WEIGHT_CHARGES = 1F;
    private static final Float UPDATED_EXCESS_WEIGHT_CHARGES = 2F;

    private static final Float DEFAULT_TOTAL_FREIGHT_CHARGES = 1F;
    private static final Float UPDATED_TOTAL_FREIGHT_CHARGES = 2F;

    private static final Float DEFAULT_LENGTH = 1F;
    private static final Float UPDATED_LENGTH = 2F;

    private static final Float DEFAULT_WIDTH = 1F;
    private static final Float UPDATED_WIDTH = 2F;

    private static final Float DEFAULT_HEIGHT = 1F;
    private static final Float UPDATED_HEIGHT = 2F;

    private static final String DEFAULT_RESELLER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESELLER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GSTIN = "AAAAAAAAAA";
    private static final String UPDATED_GSTIN = "BBBBBBBBBB";

    private static final String DEFAULT_COURIER = "AAAAAAAAAA";
    private static final String UPDATED_COURIER = "BBBBBBBBBB";

    private static final String DEFAULT_AWB = "AAAAAAAAAA";
    private static final String UPDATED_AWB = "BBBBBBBBBB";

    private static final String DEFAULT_MANIFEST_ID = "AAAAAAAAAA";
    private static final String UPDATED_MANIFEST_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .orderId(DEFAULT_ORDER_ID)
            .referenceId(DEFAULT_REFERENCE_ID)
            .orderType(DEFAULT_ORDER_TYPE)
            .orderStatus(DEFAULT_ORDER_STATUS)
            .orderDate(DEFAULT_ORDER_DATE)
            .channel(DEFAULT_CHANNEL)
            .subtotal(DEFAULT_SUBTOTAL)
            .hasShippingCharges(DEFAULT_HAS_SHIPPING_CHARGES)
            .shipping(DEFAULT_SHIPPING)
            .hasGiftwrapCharges(DEFAULT_HAS_GIFTWRAP_CHARGES)
            .giftwrap(DEFAULT_GIFTWRAP)
            .hasTransactionCharges(DEFAULT_HAS_TRANSACTION_CHARGES)
            .transaction(DEFAULT_TRANSACTION)
            .hasDiscount(DEFAULT_HAS_DISCOUNT)
            .discount(DEFAULT_DISCOUNT)
            .weight(DEFAULT_WEIGHT)
            .weightCharges(DEFAULT_WEIGHT_CHARGES)
            .excessWeightCharges(DEFAULT_EXCESS_WEIGHT_CHARGES)
            .totalFreightCharges(DEFAULT_TOTAL_FREIGHT_CHARGES)
            .length(DEFAULT_LENGTH)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .resellerName(DEFAULT_RESELLER_NAME)
            .gstin(DEFAULT_GSTIN)
            .courier(DEFAULT_COURIER)
            .awb(DEFAULT_AWB)
            .manifestId(DEFAULT_MANIFEST_ID);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .orderId(UPDATED_ORDER_ID)
            .referenceId(UPDATED_REFERENCE_ID)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .channel(UPDATED_CHANNEL)
            .subtotal(UPDATED_SUBTOTAL)
            .hasShippingCharges(UPDATED_HAS_SHIPPING_CHARGES)
            .shipping(UPDATED_SHIPPING)
            .hasGiftwrapCharges(UPDATED_HAS_GIFTWRAP_CHARGES)
            .giftwrap(UPDATED_GIFTWRAP)
            .hasTransactionCharges(UPDATED_HAS_TRANSACTION_CHARGES)
            .transaction(UPDATED_TRANSACTION)
            .hasDiscount(UPDATED_HAS_DISCOUNT)
            .discount(UPDATED_DISCOUNT)
            .weight(UPDATED_WEIGHT)
            .weightCharges(UPDATED_WEIGHT_CHARGES)
            .excessWeightCharges(UPDATED_EXCESS_WEIGHT_CHARGES)
            .totalFreightCharges(UPDATED_TOTAL_FREIGHT_CHARGES)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .resellerName(UPDATED_RESELLER_NAME)
            .gstin(UPDATED_GSTIN)
            .courier(UPDATED_COURIER)
            .awb(UPDATED_AWB)
            .manifestId(UPDATED_MANIFEST_ID);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrder.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testOrder.getOrderType()).isEqualTo(DEFAULT_ORDER_TYPE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(DEFAULT_ORDER_STATUS);
        assertThat(testOrder.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrder.getChannel()).isEqualTo(DEFAULT_CHANNEL);
        assertThat(testOrder.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testOrder.getHasShippingCharges()).isEqualTo(DEFAULT_HAS_SHIPPING_CHARGES);
        assertThat(testOrder.getShipping()).isEqualTo(DEFAULT_SHIPPING);
        assertThat(testOrder.getHasGiftwrapCharges()).isEqualTo(DEFAULT_HAS_GIFTWRAP_CHARGES);
        assertThat(testOrder.getGiftwrap()).isEqualTo(DEFAULT_GIFTWRAP);
        assertThat(testOrder.getHasTransactionCharges()).isEqualTo(DEFAULT_HAS_TRANSACTION_CHARGES);
        assertThat(testOrder.getTransaction()).isEqualTo(DEFAULT_TRANSACTION);
        assertThat(testOrder.getHasDiscount()).isEqualTo(DEFAULT_HAS_DISCOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrder.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testOrder.getWeightCharges()).isEqualTo(DEFAULT_WEIGHT_CHARGES);
        assertThat(testOrder.getExcessWeightCharges()).isEqualTo(DEFAULT_EXCESS_WEIGHT_CHARGES);
        assertThat(testOrder.getTotalFreightCharges()).isEqualTo(DEFAULT_TOTAL_FREIGHT_CHARGES);
        assertThat(testOrder.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testOrder.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testOrder.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testOrder.getResellerName()).isEqualTo(DEFAULT_RESELLER_NAME);
        assertThat(testOrder.getGstin()).isEqualTo(DEFAULT_GSTIN);
        assertThat(testOrder.getCourier()).isEqualTo(DEFAULT_COURIER);
        assertThat(testOrder.getAwb()).isEqualTo(DEFAULT_AWB);
        assertThat(testOrder.getManifestId()).isEqualTo(DEFAULT_MANIFEST_ID);
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)))
            .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID)))
            .andExpect(jsonPath("$.[*].orderType").value(hasItem(DEFAULT_ORDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].orderStatus").value(hasItem(DEFAULT_ORDER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL)))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].hasShippingCharges").value(hasItem(DEFAULT_HAS_SHIPPING_CHARGES.booleanValue())))
            .andExpect(jsonPath("$.[*].shipping").value(hasItem(DEFAULT_SHIPPING.doubleValue())))
            .andExpect(jsonPath("$.[*].hasGiftwrapCharges").value(hasItem(DEFAULT_HAS_GIFTWRAP_CHARGES.booleanValue())))
            .andExpect(jsonPath("$.[*].giftwrap").value(hasItem(DEFAULT_GIFTWRAP.doubleValue())))
            .andExpect(jsonPath("$.[*].hasTransactionCharges").value(hasItem(DEFAULT_HAS_TRANSACTION_CHARGES.booleanValue())))
            .andExpect(jsonPath("$.[*].transaction").value(hasItem(DEFAULT_TRANSACTION.doubleValue())))
            .andExpect(jsonPath("$.[*].hasDiscount").value(hasItem(DEFAULT_HAS_DISCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].weightCharges").value(hasItem(DEFAULT_WEIGHT_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].excessWeightCharges").value(hasItem(DEFAULT_EXCESS_WEIGHT_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].totalFreightCharges").value(hasItem(DEFAULT_TOTAL_FREIGHT_CHARGES.doubleValue())))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH.doubleValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH.doubleValue())))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].resellerName").value(hasItem(DEFAULT_RESELLER_NAME)))
            .andExpect(jsonPath("$.[*].gstin").value(hasItem(DEFAULT_GSTIN)))
            .andExpect(jsonPath("$.[*].courier").value(hasItem(DEFAULT_COURIER)))
            .andExpect(jsonPath("$.[*].awb").value(hasItem(DEFAULT_AWB)))
            .andExpect(jsonPath("$.[*].manifestId").value(hasItem(DEFAULT_MANIFEST_ID)));
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID))
            .andExpect(jsonPath("$.referenceId").value(DEFAULT_REFERENCE_ID))
            .andExpect(jsonPath("$.orderType").value(DEFAULT_ORDER_TYPE.toString()))
            .andExpect(jsonPath("$.orderStatus").value(DEFAULT_ORDER_STATUS.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.doubleValue()))
            .andExpect(jsonPath("$.hasShippingCharges").value(DEFAULT_HAS_SHIPPING_CHARGES.booleanValue()))
            .andExpect(jsonPath("$.shipping").value(DEFAULT_SHIPPING.doubleValue()))
            .andExpect(jsonPath("$.hasGiftwrapCharges").value(DEFAULT_HAS_GIFTWRAP_CHARGES.booleanValue()))
            .andExpect(jsonPath("$.giftwrap").value(DEFAULT_GIFTWRAP.doubleValue()))
            .andExpect(jsonPath("$.hasTransactionCharges").value(DEFAULT_HAS_TRANSACTION_CHARGES.booleanValue()))
            .andExpect(jsonPath("$.transaction").value(DEFAULT_TRANSACTION.doubleValue()))
            .andExpect(jsonPath("$.hasDiscount").value(DEFAULT_HAS_DISCOUNT.booleanValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.weightCharges").value(DEFAULT_WEIGHT_CHARGES.doubleValue()))
            .andExpect(jsonPath("$.excessWeightCharges").value(DEFAULT_EXCESS_WEIGHT_CHARGES.doubleValue()))
            .andExpect(jsonPath("$.totalFreightCharges").value(DEFAULT_TOTAL_FREIGHT_CHARGES.doubleValue()))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH.doubleValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH.doubleValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.resellerName").value(DEFAULT_RESELLER_NAME))
            .andExpect(jsonPath("$.gstin").value(DEFAULT_GSTIN))
            .andExpect(jsonPath("$.courier").value(DEFAULT_COURIER))
            .andExpect(jsonPath("$.awb").value(DEFAULT_AWB))
            .andExpect(jsonPath("$.manifestId").value(DEFAULT_MANIFEST_ID));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .orderId(UPDATED_ORDER_ID)
            .referenceId(UPDATED_REFERENCE_ID)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .channel(UPDATED_CHANNEL)
            .subtotal(UPDATED_SUBTOTAL)
            .hasShippingCharges(UPDATED_HAS_SHIPPING_CHARGES)
            .shipping(UPDATED_SHIPPING)
            .hasGiftwrapCharges(UPDATED_HAS_GIFTWRAP_CHARGES)
            .giftwrap(UPDATED_GIFTWRAP)
            .hasTransactionCharges(UPDATED_HAS_TRANSACTION_CHARGES)
            .transaction(UPDATED_TRANSACTION)
            .hasDiscount(UPDATED_HAS_DISCOUNT)
            .discount(UPDATED_DISCOUNT)
            .weight(UPDATED_WEIGHT)
            .weightCharges(UPDATED_WEIGHT_CHARGES)
            .excessWeightCharges(UPDATED_EXCESS_WEIGHT_CHARGES)
            .totalFreightCharges(UPDATED_TOTAL_FREIGHT_CHARGES)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .resellerName(UPDATED_RESELLER_NAME)
            .gstin(UPDATED_GSTIN)
            .courier(UPDATED_COURIER)
            .awb(UPDATED_AWB)
            .manifestId(UPDATED_MANIFEST_ID);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrder.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testOrder.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testOrder.getHasShippingCharges()).isEqualTo(UPDATED_HAS_SHIPPING_CHARGES);
        assertThat(testOrder.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testOrder.getHasGiftwrapCharges()).isEqualTo(UPDATED_HAS_GIFTWRAP_CHARGES);
        assertThat(testOrder.getGiftwrap()).isEqualTo(UPDATED_GIFTWRAP);
        assertThat(testOrder.getHasTransactionCharges()).isEqualTo(UPDATED_HAS_TRANSACTION_CHARGES);
        assertThat(testOrder.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testOrder.getHasDiscount()).isEqualTo(UPDATED_HAS_DISCOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrder.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testOrder.getWeightCharges()).isEqualTo(UPDATED_WEIGHT_CHARGES);
        assertThat(testOrder.getExcessWeightCharges()).isEqualTo(UPDATED_EXCESS_WEIGHT_CHARGES);
        assertThat(testOrder.getTotalFreightCharges()).isEqualTo(UPDATED_TOTAL_FREIGHT_CHARGES);
        assertThat(testOrder.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testOrder.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testOrder.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testOrder.getResellerName()).isEqualTo(UPDATED_RESELLER_NAME);
        assertThat(testOrder.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testOrder.getCourier()).isEqualTo(UPDATED_COURIER);
        assertThat(testOrder.getAwb()).isEqualTo(UPDATED_AWB);
        assertThat(testOrder.getManifestId()).isEqualTo(UPDATED_MANIFEST_ID);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, order.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .channel(UPDATED_CHANNEL)
            .subtotal(UPDATED_SUBTOTAL)
            .hasShippingCharges(UPDATED_HAS_SHIPPING_CHARGES)
            .hasGiftwrapCharges(UPDATED_HAS_GIFTWRAP_CHARGES)
            .giftwrap(UPDATED_GIFTWRAP)
            .hasTransactionCharges(UPDATED_HAS_TRANSACTION_CHARGES)
            .transaction(UPDATED_TRANSACTION)
            .hasDiscount(UPDATED_HAS_DISCOUNT)
            .weight(UPDATED_WEIGHT)
            .excessWeightCharges(UPDATED_EXCESS_WEIGHT_CHARGES)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .resellerName(UPDATED_RESELLER_NAME)
            .manifestId(UPDATED_MANIFEST_ID);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testOrder.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testOrder.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testOrder.getHasShippingCharges()).isEqualTo(UPDATED_HAS_SHIPPING_CHARGES);
        assertThat(testOrder.getShipping()).isEqualTo(DEFAULT_SHIPPING);
        assertThat(testOrder.getHasGiftwrapCharges()).isEqualTo(UPDATED_HAS_GIFTWRAP_CHARGES);
        assertThat(testOrder.getGiftwrap()).isEqualTo(UPDATED_GIFTWRAP);
        assertThat(testOrder.getHasTransactionCharges()).isEqualTo(UPDATED_HAS_TRANSACTION_CHARGES);
        assertThat(testOrder.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testOrder.getHasDiscount()).isEqualTo(UPDATED_HAS_DISCOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testOrder.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testOrder.getWeightCharges()).isEqualTo(DEFAULT_WEIGHT_CHARGES);
        assertThat(testOrder.getExcessWeightCharges()).isEqualTo(UPDATED_EXCESS_WEIGHT_CHARGES);
        assertThat(testOrder.getTotalFreightCharges()).isEqualTo(DEFAULT_TOTAL_FREIGHT_CHARGES);
        assertThat(testOrder.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testOrder.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testOrder.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testOrder.getResellerName()).isEqualTo(UPDATED_RESELLER_NAME);
        assertThat(testOrder.getGstin()).isEqualTo(DEFAULT_GSTIN);
        assertThat(testOrder.getCourier()).isEqualTo(DEFAULT_COURIER);
        assertThat(testOrder.getAwb()).isEqualTo(DEFAULT_AWB);
        assertThat(testOrder.getManifestId()).isEqualTo(UPDATED_MANIFEST_ID);
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .orderId(UPDATED_ORDER_ID)
            .referenceId(UPDATED_REFERENCE_ID)
            .orderType(UPDATED_ORDER_TYPE)
            .orderStatus(UPDATED_ORDER_STATUS)
            .orderDate(UPDATED_ORDER_DATE)
            .channel(UPDATED_CHANNEL)
            .subtotal(UPDATED_SUBTOTAL)
            .hasShippingCharges(UPDATED_HAS_SHIPPING_CHARGES)
            .shipping(UPDATED_SHIPPING)
            .hasGiftwrapCharges(UPDATED_HAS_GIFTWRAP_CHARGES)
            .giftwrap(UPDATED_GIFTWRAP)
            .hasTransactionCharges(UPDATED_HAS_TRANSACTION_CHARGES)
            .transaction(UPDATED_TRANSACTION)
            .hasDiscount(UPDATED_HAS_DISCOUNT)
            .discount(UPDATED_DISCOUNT)
            .weight(UPDATED_WEIGHT)
            .weightCharges(UPDATED_WEIGHT_CHARGES)
            .excessWeightCharges(UPDATED_EXCESS_WEIGHT_CHARGES)
            .totalFreightCharges(UPDATED_TOTAL_FREIGHT_CHARGES)
            .length(UPDATED_LENGTH)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .resellerName(UPDATED_RESELLER_NAME)
            .gstin(UPDATED_GSTIN)
            .courier(UPDATED_COURIER)
            .awb(UPDATED_AWB)
            .manifestId(UPDATED_MANIFEST_ID);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testOrder.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testOrder.getOrderStatus()).isEqualTo(UPDATED_ORDER_STATUS);
        assertThat(testOrder.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrder.getChannel()).isEqualTo(UPDATED_CHANNEL);
        assertThat(testOrder.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testOrder.getHasShippingCharges()).isEqualTo(UPDATED_HAS_SHIPPING_CHARGES);
        assertThat(testOrder.getShipping()).isEqualTo(UPDATED_SHIPPING);
        assertThat(testOrder.getHasGiftwrapCharges()).isEqualTo(UPDATED_HAS_GIFTWRAP_CHARGES);
        assertThat(testOrder.getGiftwrap()).isEqualTo(UPDATED_GIFTWRAP);
        assertThat(testOrder.getHasTransactionCharges()).isEqualTo(UPDATED_HAS_TRANSACTION_CHARGES);
        assertThat(testOrder.getTransaction()).isEqualTo(UPDATED_TRANSACTION);
        assertThat(testOrder.getHasDiscount()).isEqualTo(UPDATED_HAS_DISCOUNT);
        assertThat(testOrder.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testOrder.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testOrder.getWeightCharges()).isEqualTo(UPDATED_WEIGHT_CHARGES);
        assertThat(testOrder.getExcessWeightCharges()).isEqualTo(UPDATED_EXCESS_WEIGHT_CHARGES);
        assertThat(testOrder.getTotalFreightCharges()).isEqualTo(UPDATED_TOTAL_FREIGHT_CHARGES);
        assertThat(testOrder.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testOrder.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testOrder.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testOrder.getResellerName()).isEqualTo(UPDATED_RESELLER_NAME);
        assertThat(testOrder.getGstin()).isEqualTo(UPDATED_GSTIN);
        assertThat(testOrder.getCourier()).isEqualTo(UPDATED_COURIER);
        assertThat(testOrder.getAwb()).isEqualTo(UPDATED_AWB);
        assertThat(testOrder.getManifestId()).isEqualTo(UPDATED_MANIFEST_ID);
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, order.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
