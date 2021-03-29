package com.xiornis.web.rest;

import com.xiornis.domain.ReturnLabel;
import com.xiornis.repository.ReturnLabelRepository;
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
 * REST controller for managing {@link com.xiornis.domain.ReturnLabel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReturnLabelResource {

    private final Logger log = LoggerFactory.getLogger(ReturnLabelResource.class);

    private static final String ENTITY_NAME = "returnLabel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReturnLabelRepository returnLabelRepository;

    public ReturnLabelResource(ReturnLabelRepository returnLabelRepository) {
        this.returnLabelRepository = returnLabelRepository;
    }

    /**
     * {@code POST  /return-labels} : Create a new returnLabel.
     *
     * @param returnLabel the returnLabel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnLabel, or with status {@code 400 (Bad Request)} if the returnLabel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/return-labels")
    public ResponseEntity<ReturnLabel> createReturnLabel(@RequestBody ReturnLabel returnLabel) throws URISyntaxException {
        log.debug("REST request to save ReturnLabel : {}", returnLabel);
        if (returnLabel.getId() != null) {
            throw new BadRequestAlertException("A new returnLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReturnLabel result = returnLabelRepository.save(returnLabel);
        return ResponseEntity
            .created(new URI("/api/return-labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /return-labels/:id} : Updates an existing returnLabel.
     *
     * @param id the id of the returnLabel to save.
     * @param returnLabel the returnLabel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnLabel,
     * or with status {@code 400 (Bad Request)} if the returnLabel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/return-labels/{id}")
    public ResponseEntity<ReturnLabel> updateReturnLabel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReturnLabel returnLabel
    ) throws URISyntaxException {
        log.debug("REST request to update ReturnLabel : {}, {}", id, returnLabel);
        if (returnLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnLabelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReturnLabel result = returnLabelRepository.save(returnLabel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnLabel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /return-labels/:id} : Partial updates given fields of an existing returnLabel, field will ignore if it is null
     *
     * @param id the id of the returnLabel to save.
     * @param returnLabel the returnLabel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnLabel,
     * or with status {@code 400 (Bad Request)} if the returnLabel is not valid,
     * or with status {@code 404 (Not Found)} if the returnLabel is not found,
     * or with status {@code 500 (Internal Server Error)} if the returnLabel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/return-labels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReturnLabel> partialUpdateReturnLabel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReturnLabel returnLabel
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReturnLabel partially : {}, {}", id, returnLabel);
        if (returnLabel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, returnLabel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!returnLabelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReturnLabel> result = returnLabelRepository
            .findById(returnLabel.getId())
            .map(
                existingReturnLabel -> {
                    if (returnLabel.getTitle() != null) {
                        existingReturnLabel.setTitle(returnLabel.getTitle());
                    }

                    return existingReturnLabel;
                }
            )
            .map(returnLabelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnLabel.getId().toString())
        );
    }

    /**
     * {@code GET  /return-labels} : get all the returnLabels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of returnLabels in body.
     */
    @GetMapping("/return-labels")
    public ResponseEntity<List<ReturnLabel>> getAllReturnLabels(Pageable pageable) {
        log.debug("REST request to get a page of ReturnLabels");
        Page<ReturnLabel> page = returnLabelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /return-labels/:id} : get the "id" returnLabel.
     *
     * @param id the id of the returnLabel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnLabel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/return-labels/{id}")
    public ResponseEntity<ReturnLabel> getReturnLabel(@PathVariable Long id) {
        log.debug("REST request to get ReturnLabel : {}", id);
        Optional<ReturnLabel> returnLabel = returnLabelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(returnLabel);
    }

    /**
     * {@code DELETE  /return-labels/:id} : delete the "id" returnLabel.
     *
     * @param id the id of the returnLabel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/return-labels/{id}")
    public ResponseEntity<Void> deleteReturnLabel(@PathVariable Long id) {
        log.debug("REST request to delete ReturnLabel : {}", id);
        returnLabelRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
