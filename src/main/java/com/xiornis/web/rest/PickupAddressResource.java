package com.xiornis.web.rest;

import com.xiornis.domain.PickupAddress;
import com.xiornis.repository.PickupAddressRepository;
import com.xiornis.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.xiornis.domain.PickupAddress}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PickupAddressResource {

    private final Logger log = LoggerFactory.getLogger(PickupAddressResource.class);

    private static final String ENTITY_NAME = "pickupAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PickupAddressRepository pickupAddressRepository;

    public PickupAddressResource(PickupAddressRepository pickupAddressRepository) {
        this.pickupAddressRepository = pickupAddressRepository;
    }

    /**
     * {@code POST  /pickup-addresses} : Create a new pickupAddress.
     *
     * @param pickupAddress the pickupAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pickupAddress, or with status {@code 400 (Bad Request)} if the pickupAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pickup-addresses")
    public ResponseEntity<PickupAddress> createPickupAddress(@RequestBody PickupAddress pickupAddress) throws URISyntaxException {
        log.debug("REST request to save PickupAddress : {}", pickupAddress);
        if (pickupAddress.getId() != null) {
            throw new BadRequestAlertException("A new pickupAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PickupAddress result = pickupAddressRepository.save(pickupAddress);
        return ResponseEntity
            .created(new URI("/api/pickup-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pickup-addresses/:id} : Updates an existing pickupAddress.
     *
     * @param id the id of the pickupAddress to save.
     * @param pickupAddress the pickupAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pickupAddress,
     * or with status {@code 400 (Bad Request)} if the pickupAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pickupAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pickup-addresses/{id}")
    public ResponseEntity<PickupAddress> updatePickupAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PickupAddress pickupAddress
    ) throws URISyntaxException {
        log.debug("REST request to update PickupAddress : {}, {}", id, pickupAddress);
        if (pickupAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pickupAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pickupAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PickupAddress result = pickupAddressRepository.save(pickupAddress);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pickupAddress.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pickup-addresses/:id} : Partial updates given fields of an existing pickupAddress, field will ignore if it is null
     *
     * @param id the id of the pickupAddress to save.
     * @param pickupAddress the pickupAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pickupAddress,
     * or with status {@code 400 (Bad Request)} if the pickupAddress is not valid,
     * or with status {@code 404 (Not Found)} if the pickupAddress is not found,
     * or with status {@code 500 (Internal Server Error)} if the pickupAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pickup-addresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PickupAddress> partialUpdatePickupAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PickupAddress pickupAddress
    ) throws URISyntaxException {
        log.debug("REST request to partial update PickupAddress partially : {}, {}", id, pickupAddress);
        if (pickupAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pickupAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pickupAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PickupAddress> result = pickupAddressRepository
            .findById(pickupAddress.getId())
            .map(
                existingPickupAddress -> {
                    if (pickupAddress.getNickName() != null) {
                        existingPickupAddress.setNickName(pickupAddress.getNickName());
                    }
                    if (pickupAddress.getMobile() != null) {
                        existingPickupAddress.setMobile(pickupAddress.getMobile());
                    }
                    if (pickupAddress.getAlternateMobile() != null) {
                        existingPickupAddress.setAlternateMobile(pickupAddress.getAlternateMobile());
                    }
                    if (pickupAddress.getEmail() != null) {
                        existingPickupAddress.setEmail(pickupAddress.getEmail());
                    }
                    if (pickupAddress.getAddressline1() != null) {
                        existingPickupAddress.setAddressline1(pickupAddress.getAddressline1());
                    }
                    if (pickupAddress.getAddressline2() != null) {
                        existingPickupAddress.setAddressline2(pickupAddress.getAddressline2());
                    }
                    if (pickupAddress.getPincode() != null) {
                        existingPickupAddress.setPincode(pickupAddress.getPincode());
                    }
                    if (pickupAddress.getCity() != null) {
                        existingPickupAddress.setCity(pickupAddress.getCity());
                    }
                    if (pickupAddress.getState() != null) {
                        existingPickupAddress.setState(pickupAddress.getState());
                    }
                    if (pickupAddress.getCountry() != null) {
                        existingPickupAddress.setCountry(pickupAddress.getCountry());
                    }
                    if (pickupAddress.getCompanyName() != null) {
                        existingPickupAddress.setCompanyName(pickupAddress.getCompanyName());
                    }
                    if (pickupAddress.getIsMobileVerified() != null) {
                        existingPickupAddress.setIsMobileVerified(pickupAddress.getIsMobileVerified());
                    }
                    if (pickupAddress.getOtp() != null) {
                        existingPickupAddress.setOtp(pickupAddress.getOtp());
                    }

                    return existingPickupAddress;
                }
            )
            .map(pickupAddressRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pickupAddress.getId().toString())
        );
    }

    /**
     * {@code GET  /pickup-addresses} : get all the pickupAddresses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pickupAddresses in body.
     */
    @GetMapping("/pickup-addresses")
    public List<PickupAddress> getAllPickupAddresses() {
        log.debug("REST request to get all PickupAddresses");
        return pickupAddressRepository.findAll();
    }

    /**
     * {@code GET  /pickup-addresses/:id} : get the "id" pickupAddress.
     *
     * @param id the id of the pickupAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pickupAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pickup-addresses/{id}")
    public ResponseEntity<PickupAddress> getPickupAddress(@PathVariable Long id) {
        log.debug("REST request to get PickupAddress : {}", id);
        Optional<PickupAddress> pickupAddress = pickupAddressRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pickupAddress);
    }

    /**
     * {@code DELETE  /pickup-addresses/:id} : delete the "id" pickupAddress.
     *
     * @param id the id of the pickupAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pickup-addresses/{id}")
    public ResponseEntity<Void> deletePickupAddress(@PathVariable Long id) {
        log.debug("REST request to delete PickupAddress : {}", id);
        pickupAddressRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
