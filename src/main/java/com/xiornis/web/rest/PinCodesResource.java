package com.xiornis.web.rest;

import com.xiornis.domain.PinCodes;
import com.xiornis.repository.PinCodesRepository;
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
 * REST controller for managing {@link com.xiornis.domain.PinCodes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PinCodesResource {

    private final Logger log = LoggerFactory.getLogger(PinCodesResource.class);

    private static final String ENTITY_NAME = "pinCodes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PinCodesRepository pinCodesRepository;

    public PinCodesResource(PinCodesRepository pinCodesRepository) {
        this.pinCodesRepository = pinCodesRepository;
    }

    /**
     * {@code POST  /pin-codes} : Create a new pinCodes.
     *
     * @param pinCodes the pinCodes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pinCodes, or with status {@code 400 (Bad Request)} if the pinCodes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pin-codes")
    public ResponseEntity<PinCodes> createPinCodes(@RequestBody PinCodes pinCodes) throws URISyntaxException {
        log.debug("REST request to save PinCodes : {}", pinCodes);
        if (pinCodes.getId() != null) {
            throw new BadRequestAlertException("A new pinCodes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PinCodes result = pinCodesRepository.save(pinCodes);
        return ResponseEntity
            .created(new URI("/api/pin-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pin-codes/:id} : Updates an existing pinCodes.
     *
     * @param id the id of the pinCodes to save.
     * @param pinCodes the pinCodes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pinCodes,
     * or with status {@code 400 (Bad Request)} if the pinCodes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pinCodes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pin-codes/{id}")
    public ResponseEntity<PinCodes> updatePinCodes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PinCodes pinCodes
    ) throws URISyntaxException {
        log.debug("REST request to update PinCodes : {}, {}", id, pinCodes);
        if (pinCodes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pinCodes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pinCodesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PinCodes result = pinCodesRepository.save(pinCodes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pinCodes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pin-codes/:id} : Partial updates given fields of an existing pinCodes, field will ignore if it is null
     *
     * @param id the id of the pinCodes to save.
     * @param pinCodes the pinCodes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pinCodes,
     * or with status {@code 400 (Bad Request)} if the pinCodes is not valid,
     * or with status {@code 404 (Not Found)} if the pinCodes is not found,
     * or with status {@code 500 (Internal Server Error)} if the pinCodes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pin-codes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PinCodes> partialUpdatePinCodes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PinCodes pinCodes
    ) throws URISyntaxException {
        log.debug("REST request to partial update PinCodes partially : {}, {}", id, pinCodes);
        if (pinCodes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pinCodes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pinCodesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PinCodes> result = pinCodesRepository
            .findById(pinCodes.getId())
            .map(
                existingPinCodes -> {
                    if (pinCodes.getPin() != null) {
                        existingPinCodes.setPin(pinCodes.getPin());
                    }
                    if (pinCodes.getCity() != null) {
                        existingPinCodes.setCity(pinCodes.getCity());
                    }
                    if (pinCodes.getState() != null) {
                        existingPinCodes.setState(pinCodes.getState());
                    }
                    if (pinCodes.getCountry() != null) {
                        existingPinCodes.setCountry(pinCodes.getCountry());
                    }

                    return existingPinCodes;
                }
            )
            .map(pinCodesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pinCodes.getId().toString())
        );
    }

    /**
     * {@code GET  /pin-codes} : get all the pinCodes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pinCodes in body.
     */
    @GetMapping("/pin-codes")
    public ResponseEntity<List<PinCodes>> getAllPinCodes(Pageable pageable) {
        log.debug("REST request to get a page of PinCodes");
        Page<PinCodes> page = pinCodesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pin-codes/:id} : get the "id" pinCodes.
     *
     * @param id the id of the pinCodes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pinCodes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pin-codes/{id}")
    public ResponseEntity<PinCodes> getPinCodes(@PathVariable Long id) {
        log.debug("REST request to get PinCodes : {}", id);
        Optional<PinCodes> pinCodes = pinCodesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pinCodes);
    }

    /**
     * {@code DELETE  /pin-codes/:id} : delete the "id" pinCodes.
     *
     * @param id the id of the pinCodes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pin-codes/{id}")
    public ResponseEntity<Void> deletePinCodes(@PathVariable Long id) {
        log.debug("REST request to delete PinCodes : {}", id);
        pinCodesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
