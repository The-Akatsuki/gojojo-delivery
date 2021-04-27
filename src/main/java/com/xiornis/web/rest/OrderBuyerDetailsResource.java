package com.xiornis.web.rest;

import com.xiornis.domain.OrderBuyerDetails;
import com.xiornis.repository.OrderBuyerDetailsRepository;
import com.xiornis.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.xiornis.domain.OrderBuyerDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OrderBuyerDetailsResource {

    private final Logger log = LoggerFactory.getLogger(OrderBuyerDetailsResource.class);

    private static final String ENTITY_NAME = "orderBuyerDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderBuyerDetailsRepository orderBuyerDetailsRepository;

    public OrderBuyerDetailsResource(OrderBuyerDetailsRepository orderBuyerDetailsRepository) {
        this.orderBuyerDetailsRepository = orderBuyerDetailsRepository;
    }

    /**
     * {@code POST  /order-buyer-details} : Create a new orderBuyerDetails.
     *
     * @param orderBuyerDetails the orderBuyerDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderBuyerDetails, or with status {@code 400 (Bad Request)} if the orderBuyerDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-buyer-details")
    public ResponseEntity<OrderBuyerDetails> createOrderBuyerDetails(@RequestBody OrderBuyerDetails orderBuyerDetails)
        throws URISyntaxException {
        log.debug("REST request to save OrderBuyerDetails : {}", orderBuyerDetails);
        if (orderBuyerDetails.getId() != null) {
            throw new BadRequestAlertException("A new orderBuyerDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderBuyerDetails result = orderBuyerDetailsRepository.save(orderBuyerDetails);
        return ResponseEntity
            .created(new URI("/api/order-buyer-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-buyer-details/:id} : Updates an existing orderBuyerDetails.
     *
     * @param id the id of the orderBuyerDetails to save.
     * @param orderBuyerDetails the orderBuyerDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderBuyerDetails,
     * or with status {@code 400 (Bad Request)} if the orderBuyerDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderBuyerDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-buyer-details/{id}")
    public ResponseEntity<OrderBuyerDetails> updateOrderBuyerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderBuyerDetails orderBuyerDetails
    ) throws URISyntaxException {
        log.debug("REST request to update OrderBuyerDetails : {}, {}", id, orderBuyerDetails);
        if (orderBuyerDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderBuyerDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderBuyerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderBuyerDetails result = orderBuyerDetailsRepository.save(orderBuyerDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderBuyerDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-buyer-details/:id} : Partial updates given fields of an existing orderBuyerDetails, field will ignore if it is null
     *
     * @param id the id of the orderBuyerDetails to save.
     * @param orderBuyerDetails the orderBuyerDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderBuyerDetails,
     * or with status {@code 400 (Bad Request)} if the orderBuyerDetails is not valid,
     * or with status {@code 404 (Not Found)} if the orderBuyerDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderBuyerDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-buyer-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrderBuyerDetails> partialUpdateOrderBuyerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderBuyerDetails orderBuyerDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderBuyerDetails partially : {}, {}", id, orderBuyerDetails);
        if (orderBuyerDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderBuyerDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderBuyerDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderBuyerDetails> result = orderBuyerDetailsRepository
            .findById(orderBuyerDetails.getId())
            .map(
                existingOrderBuyerDetails -> {
                    if (orderBuyerDetails.getName() != null) {
                        existingOrderBuyerDetails.setName(orderBuyerDetails.getName());
                    }
                    if (orderBuyerDetails.getMobile() != null) {
                        existingOrderBuyerDetails.setMobile(orderBuyerDetails.getMobile());
                    }
                    if (orderBuyerDetails.getAlternateMobile() != null) {
                        existingOrderBuyerDetails.setAlternateMobile(orderBuyerDetails.getAlternateMobile());
                    }
                    if (orderBuyerDetails.getEmail() != null) {
                        existingOrderBuyerDetails.setEmail(orderBuyerDetails.getEmail());
                    }
                    if (orderBuyerDetails.getAddressline1() != null) {
                        existingOrderBuyerDetails.setAddressline1(orderBuyerDetails.getAddressline1());
                    }
                    if (orderBuyerDetails.getAddressline2() != null) {
                        existingOrderBuyerDetails.setAddressline2(orderBuyerDetails.getAddressline2());
                    }
                    if (orderBuyerDetails.getPincode() != null) {
                        existingOrderBuyerDetails.setPincode(orderBuyerDetails.getPincode());
                    }
                    if (orderBuyerDetails.getCity() != null) {
                        existingOrderBuyerDetails.setCity(orderBuyerDetails.getCity());
                    }
                    if (orderBuyerDetails.getState() != null) {
                        existingOrderBuyerDetails.setState(orderBuyerDetails.getState());
                    }
                    if (orderBuyerDetails.getCountry() != null) {
                        existingOrderBuyerDetails.setCountry(orderBuyerDetails.getCountry());
                    }
                    if (orderBuyerDetails.getCompanyName() != null) {
                        existingOrderBuyerDetails.setCompanyName(orderBuyerDetails.getCompanyName());
                    }
                    if (orderBuyerDetails.getIsBillingSame() != null) {
                        existingOrderBuyerDetails.setIsBillingSame(orderBuyerDetails.getIsBillingSame());
                    }
                    if (orderBuyerDetails.getBillAddressline1() != null) {
                        existingOrderBuyerDetails.setBillAddressline1(orderBuyerDetails.getBillAddressline1());
                    }
                    if (orderBuyerDetails.getBillAddressline2() != null) {
                        existingOrderBuyerDetails.setBillAddressline2(orderBuyerDetails.getBillAddressline2());
                    }
                    if (orderBuyerDetails.getBillPincode() != null) {
                        existingOrderBuyerDetails.setBillPincode(orderBuyerDetails.getBillPincode());
                    }
                    if (orderBuyerDetails.getBillCity() != null) {
                        existingOrderBuyerDetails.setBillCity(orderBuyerDetails.getBillCity());
                    }
                    if (orderBuyerDetails.getBillState() != null) {
                        existingOrderBuyerDetails.setBillState(orderBuyerDetails.getBillState());
                    }
                    if (orderBuyerDetails.getBillCountry() != null) {
                        existingOrderBuyerDetails.setBillCountry(orderBuyerDetails.getBillCountry());
                    }

                    return existingOrderBuyerDetails;
                }
            )
            .map(orderBuyerDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderBuyerDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /order-buyer-details} : get all the orderBuyerDetails.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderBuyerDetails in body.
     */
    @GetMapping("/order-buyer-details")
    public List<OrderBuyerDetails> getAllOrderBuyerDetails(@RequestParam(required = false) String filter) {
        if ("order-is-null".equals(filter)) {
            log.debug("REST request to get all OrderBuyerDetailss where order is null");
            return StreamSupport
                .stream(orderBuyerDetailsRepository.findAll().spliterator(), false)
                .filter(orderBuyerDetails -> orderBuyerDetails.getOrder() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all OrderBuyerDetails");
        return orderBuyerDetailsRepository.findAll();
    }

    /**
     * {@code GET  /order-buyer-details/:id} : get the "id" orderBuyerDetails.
     *
     * @param id the id of the orderBuyerDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderBuyerDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-buyer-details/{id}")
    public ResponseEntity<OrderBuyerDetails> getOrderBuyerDetails(@PathVariable Long id) {
        log.debug("REST request to get OrderBuyerDetails : {}", id);
        Optional<OrderBuyerDetails> orderBuyerDetails = orderBuyerDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(orderBuyerDetails);
    }

    /**
     * {@code DELETE  /order-buyer-details/:id} : delete the "id" orderBuyerDetails.
     *
     * @param id the id of the orderBuyerDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-buyer-details/{id}")
    public ResponseEntity<Void> deleteOrderBuyerDetails(@PathVariable Long id) {
        log.debug("REST request to delete OrderBuyerDetails : {}", id);
        orderBuyerDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
