package com.xiornis.web.rest;

import com.xiornis.domain.ReasonEscalation;
import com.xiornis.repository.ReasonEscalationRepository;
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
 * REST controller for managing {@link com.xiornis.domain.ReasonEscalation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReasonEscalationResource {

    private final Logger log = LoggerFactory.getLogger(ReasonEscalationResource.class);

    private static final String ENTITY_NAME = "reasonEscalation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReasonEscalationRepository reasonEscalationRepository;

    public ReasonEscalationResource(ReasonEscalationRepository reasonEscalationRepository) {
        this.reasonEscalationRepository = reasonEscalationRepository;
    }

    /**
     * {@code POST  /reason-escalations} : Create a new reasonEscalation.
     *
     * @param reasonEscalation the reasonEscalation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reasonEscalation, or with status {@code 400 (Bad Request)} if the reasonEscalation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reason-escalations")
    public ResponseEntity<ReasonEscalation> createReasonEscalation(@RequestBody ReasonEscalation reasonEscalation)
        throws URISyntaxException {
        log.debug("REST request to save ReasonEscalation : {}", reasonEscalation);
        if (reasonEscalation.getId() != null) {
            throw new BadRequestAlertException("A new reasonEscalation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReasonEscalation result = reasonEscalationRepository.save(reasonEscalation);
        return ResponseEntity
            .created(new URI("/api/reason-escalations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reason-escalations/:id} : Updates an existing reasonEscalation.
     *
     * @param id the id of the reasonEscalation to save.
     * @param reasonEscalation the reasonEscalation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reasonEscalation,
     * or with status {@code 400 (Bad Request)} if the reasonEscalation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reasonEscalation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reason-escalations/{id}")
    public ResponseEntity<ReasonEscalation> updateReasonEscalation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReasonEscalation reasonEscalation
    ) throws URISyntaxException {
        log.debug("REST request to update ReasonEscalation : {}, {}", id, reasonEscalation);
        if (reasonEscalation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reasonEscalation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reasonEscalationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReasonEscalation result = reasonEscalationRepository.save(reasonEscalation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reasonEscalation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reason-escalations/:id} : Partial updates given fields of an existing reasonEscalation, field will ignore if it is null
     *
     * @param id the id of the reasonEscalation to save.
     * @param reasonEscalation the reasonEscalation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reasonEscalation,
     * or with status {@code 400 (Bad Request)} if the reasonEscalation is not valid,
     * or with status {@code 404 (Not Found)} if the reasonEscalation is not found,
     * or with status {@code 500 (Internal Server Error)} if the reasonEscalation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reason-escalations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReasonEscalation> partialUpdateReasonEscalation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReasonEscalation reasonEscalation
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReasonEscalation partially : {}, {}", id, reasonEscalation);
        if (reasonEscalation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reasonEscalation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reasonEscalationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReasonEscalation> result = reasonEscalationRepository
            .findById(reasonEscalation.getId())
            .map(
                existingReasonEscalation -> {
                    if (reasonEscalation.getTitle() != null) {
                        existingReasonEscalation.setTitle(reasonEscalation.getTitle());
                    }

                    return existingReasonEscalation;
                }
            )
            .map(reasonEscalationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reasonEscalation.getId().toString())
        );
    }

    /**
     * {@code GET  /reason-escalations} : get all the reasonEscalations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reasonEscalations in body.
     */
    @GetMapping("/reason-escalations")
    public ResponseEntity<List<ReasonEscalation>> getAllReasonEscalations(Pageable pageable) {
        log.debug("REST request to get a page of ReasonEscalations");
        Page<ReasonEscalation> page = reasonEscalationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reason-escalations/:id} : get the "id" reasonEscalation.
     *
     * @param id the id of the reasonEscalation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reasonEscalation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reason-escalations/{id}")
    public ResponseEntity<ReasonEscalation> getReasonEscalation(@PathVariable Long id) {
        log.debug("REST request to get ReasonEscalation : {}", id);
        Optional<ReasonEscalation> reasonEscalation = reasonEscalationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reasonEscalation);
    }

    /**
     * {@code DELETE  /reason-escalations/:id} : delete the "id" reasonEscalation.
     *
     * @param id the id of the reasonEscalation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reason-escalations/{id}")
    public ResponseEntity<Void> deleteReasonEscalation(@PathVariable Long id) {
        log.debug("REST request to delete ReasonEscalation : {}", id);
        reasonEscalationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
