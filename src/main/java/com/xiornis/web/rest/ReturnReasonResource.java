package com.xiornis.web.rest;

import com.xiornis.domain.ReturnReason;
import com.xiornis.repository.ReturnReasonRepository;
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
 * REST controller for managing {@link com.xiornis.domain.ReturnReason}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReturnReasonResource {

    private final Logger log = LoggerFactory.getLogger(ReturnReasonResource.class);

    private static final String ENTITY_NAME = "returnReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReturnReasonRepository returnReasonRepository;

    public ReturnReasonResource(ReturnReasonRepository returnReasonRepository) {
        this.returnReasonRepository = returnReasonRepository;
    }

    /**
     * {@code POST  /return-reasons} : Create a new returnReason.
     *
     * @param returnReason the returnReason to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnReason, or with status {@code 400 (Bad Request)} if the returnReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/return-reasons")
    public ResponseEntity<ReturnReason> createReturnReason(@RequestBody ReturnReason returnReason) throws URISyntaxException {
        log.debug("REST request to save ReturnReason : {}", returnReason);
        if (returnReason.getId() != null) {
            throw new BadRequestAlertException("A new returnReason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReturnReason result = returnReasonRepository.save(returnReason);
        return ResponseEntity
            .created(new URI("/api/return-reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /return-reasons/:id} : Updates an existing returnReason.
     *
     * @param id the id of the returnReason to save.
     * @param returnReason the returnReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnReason,
     * or with status {@code 400 (Bad Request)} if the returnReason is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/return-reasons/{id}")
    public ResponseEntity<ReturnReason> updateReturnReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReturnReason returnReason
    ) throws URISyntaxException {
        log.debug("REST request to update ReturnReason : {}, {}", id, returnReason);
        if (returnReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReturnReason result = returnReasonRepository.save(returnReason);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnReason.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /return-reasons/:id} : Partial updates given fields of an existing returnReason, field will ignore if it is null
     *
     * @param id the id of the returnReason to save.
     * @param returnReason the returnReason to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnReason,
     * or with status {@code 400 (Bad Request)} if the returnReason is not valid,
     * or with status {@code 404 (Not Found)} if the returnReason is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnReason couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/return-reasons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReturnReason> partialUpdateReturnReason(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReturnReason returnReason
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReturnReason partially : {}, {}", id, returnReason);
        if (returnReason.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnReason.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnReason> result = returnReasonRepository
            .findById(returnReason.getId())
            .map(
                existingReturnReason -> {
                    if (returnReason.getComment() != null) {
                        existingReturnReason.setComment(returnReason.getComment());
                    }
                    if (returnReason.getImage() != null) {
                        existingReturnReason.setImage(returnReason.getImage());
                    }

                    return existingReturnReason;
                }
            )
            .map(returnReasonRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnReason.getId().toString())
        );
    }

    /**
     * {@code GET  /return-reasons} : get all the returnReasons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of returnReasons in body.
     */
    @GetMapping("/return-reasons")
    public List<ReturnReason> getAllReturnReasons() {
        log.debug("REST request to get all ReturnReasons");
        return returnReasonRepository.findAll();
    }

    /**
     * {@code GET  /return-reasons/:id} : get the "id" returnReason.
     *
     * @param id the id of the returnReason to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnReason, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/return-reasons/{id}")
    public ResponseEntity<ReturnReason> getReturnReason(@PathVariable Long id) {
        log.debug("REST request to get ReturnReason : {}", id);
        Optional<ReturnReason> returnReason = returnReasonRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(returnReason);
    }

    /**
     * {@code DELETE  /return-reasons/:id} : delete the "id" returnReason.
     *
     * @param id the id of the returnReason to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/return-reasons/{id}")
    public ResponseEntity<Void> deleteReturnReason(@PathVariable Long id) {
        log.debug("REST request to delete ReturnReason : {}", id);
        returnReasonRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
