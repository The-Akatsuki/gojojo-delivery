package com.xiornis.web.rest;

import com.xiornis.domain.Escalation;
import com.xiornis.repository.EscalationRepository;
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
 * REST controller for managing {@link com.xiornis.domain.Escalation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EscalationResource {

    private final Logger log = LoggerFactory.getLogger(EscalationResource.class);

    private static final String ENTITY_NAME = "escalation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EscalationRepository escalationRepository;

    public EscalationResource(EscalationRepository escalationRepository) {
        this.escalationRepository = escalationRepository;
    }

    /**
     * {@code POST  /escalations} : Create a new escalation.
     *
     * @param escalation the escalation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new escalation, or with status {@code 400 (Bad Request)} if the escalation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/escalations")
    public ResponseEntity<Escalation> createEscalation(@RequestBody Escalation escalation) throws URISyntaxException {
        log.debug("REST request to save Escalation : {}", escalation);
        if (escalation.getId() != null) {
            throw new BadRequestAlertException("A new escalation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Escalation result = escalationRepository.save(escalation);
        return ResponseEntity
            .created(new URI("/api/escalations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /escalations/:id} : Updates an existing escalation.
     *
     * @param id the id of the escalation to save.
     * @param escalation the escalation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated escalation,
     * or with status {@code 400 (Bad Request)} if the escalation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the escalation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/escalations/{id}")
    public ResponseEntity<Escalation> updateEscalation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Escalation escalation
    ) throws URISyntaxException {
        log.debug("REST request to update Escalation : {}, {}", id, escalation);
        if (escalation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, escalation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!escalationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Escalation result = escalationRepository.save(escalation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, escalation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /escalations/:id} : Partial updates given fields of an existing escalation, field will ignore if it is null
     *
     * @param id the id of the escalation to save.
     * @param escalation the escalation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated escalation,
     * or with status {@code 400 (Bad Request)} if the escalation is not valid,
     * or with status {@code 404 (Not Found)} if the escalation is not found,
     * or with status {@code 500 (Internal Server Error)} if the escalation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/escalations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Escalation> partialUpdateEscalation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Escalation escalation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Escalation partially : {}, {}", id, escalation);
        if (escalation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, escalation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!escalationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Escalation> result = escalationRepository
            .findById(escalation.getId())
            .map(
                existingEscalation -> {
                    if (escalation.getRemark() != null) {
                        existingEscalation.setRemark(escalation.getRemark());
                    }

                    return existingEscalation;
                }
            )
            .map(escalationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, escalation.getId().toString())
        );
    }

    /**
     * {@code GET  /escalations} : get all the escalations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of escalations in body.
     */
    @GetMapping("/escalations")
    public List<Escalation> getAllEscalations() {
        log.debug("REST request to get all Escalations");
        return escalationRepository.findAll();
    }

    /**
     * {@code GET  /escalations/:id} : get the "id" escalation.
     *
     * @param id the id of the escalation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the escalation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/escalations/{id}")
    public ResponseEntity<Escalation> getEscalation(@PathVariable Long id) {
        log.debug("REST request to get Escalation : {}", id);
        Optional<Escalation> escalation = escalationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(escalation);
    }

    /**
     * {@code DELETE  /escalations/:id} : delete the "id" escalation.
     *
     * @param id the id of the escalation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/escalations/{id}")
    public ResponseEntity<Void> deleteEscalation(@PathVariable Long id) {
        log.debug("REST request to delete Escalation : {}", id);
        escalationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
