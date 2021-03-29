package com.xiornis.web.rest;

import com.xiornis.domain.ShipmentActivity;
import com.xiornis.repository.ShipmentActivityRepository;
import com.xiornis.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.xiornis.domain.ShipmentActivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ShipmentActivityResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentActivityResource.class);

    private static final String ENTITY_NAME = "shipmentActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentActivityRepository shipmentActivityRepository;

    public ShipmentActivityResource(ShipmentActivityRepository shipmentActivityRepository) {
        this.shipmentActivityRepository = shipmentActivityRepository;
    }

    /**
     * {@code POST  /shipment-activities} : Create a new shipmentActivity.
     *
     * @param shipmentActivity the shipmentActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentActivity, or with status {@code 400 (Bad Request)} if the shipmentActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-activities")
    public ResponseEntity<ShipmentActivity> createShipmentActivity(@RequestBody ShipmentActivity shipmentActivity)
        throws URISyntaxException {
        log.debug("REST request to save ShipmentActivity : {}", shipmentActivity);
        if (shipmentActivity.getId() != null) {
            throw new BadRequestAlertException("A new shipmentActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipmentActivity result = shipmentActivityRepository.save(shipmentActivity);
        return ResponseEntity
            .created(new URI("/api/shipment-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-activities/:id} : Updates an existing shipmentActivity.
     *
     * @param id the id of the shipmentActivity to save.
     * @param shipmentActivity the shipmentActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentActivity,
     * or with status {@code 400 (Bad Request)} if the shipmentActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-activities/{id}")
    public ResponseEntity<ShipmentActivity> updateShipmentActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShipmentActivity shipmentActivity
    ) throws URISyntaxException {
        log.debug("REST request to update ShipmentActivity : {}, {}", id, shipmentActivity);
        if (shipmentActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShipmentActivity result = shipmentActivityRepository.save(shipmentActivity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shipment-activities/:id} : Partial updates given fields of an existing shipmentActivity, field will ignore if it is null
     *
     * @param id the id of the shipmentActivity to save.
     * @param shipmentActivity the shipmentActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentActivity,
     * or with status {@code 400 (Bad Request)} if the shipmentActivity is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentActivity is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shipment-activities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ShipmentActivity> partialUpdateShipmentActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ShipmentActivity shipmentActivity
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShipmentActivity partially : {}, {}", id, shipmentActivity);
        if (shipmentActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentActivity> result = shipmentActivityRepository
            .findById(shipmentActivity.getId())
            .map(
                existingShipmentActivity -> {
                    if (shipmentActivity.getStatus() != null) {
                        existingShipmentActivity.setStatus(shipmentActivity.getStatus());
                    }
                    if (shipmentActivity.getPincode() != null) {
                        existingShipmentActivity.setPincode(shipmentActivity.getPincode());
                    }
                    if (shipmentActivity.getLocation() != null) {
                        existingShipmentActivity.setLocation(shipmentActivity.getLocation());
                    }

                    return existingShipmentActivity;
                }
            )
            .map(shipmentActivityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentActivity.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-activities} : get all the shipmentActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentActivities in body.
     */
    @GetMapping("/shipment-activities")
    public ResponseEntity<List<ShipmentActivity>> getAllShipmentActivities(Pageable pageable) {
        log.debug("REST request to get a page of ShipmentActivities");
        Page<ShipmentActivity> page = shipmentActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-activities/:id} : get the "id" shipmentActivity.
     *
     * @param id the id of the shipmentActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-activities/{id}")
    public ResponseEntity<ShipmentActivity> getShipmentActivity(@PathVariable Long id) {
        log.debug("REST request to get ShipmentActivity : {}", id);
        Optional<ShipmentActivity> shipmentActivity = shipmentActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(shipmentActivity);
    }

    /**
     * {@code DELETE  /shipment-activities/:id} : delete the "id" shipmentActivity.
     *
     * @param id the id of the shipmentActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-activities/{id}")
    public ResponseEntity<Void> deleteShipmentActivity(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentActivity : {}", id);
        shipmentActivityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
