package com.xiornis.web.rest;

import com.xiornis.domain.Manifest;
import com.xiornis.repository.ManifestRepository;
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
 * REST controller for managing {@link com.xiornis.domain.Manifest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ManifestResource {

    private final Logger log = LoggerFactory.getLogger(ManifestResource.class);

    private static final String ENTITY_NAME = "manifest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManifestRepository manifestRepository;

    public ManifestResource(ManifestRepository manifestRepository) {
        this.manifestRepository = manifestRepository;
    }

    /**
     * {@code POST  /manifests} : Create a new manifest.
     *
     * @param manifest the manifest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manifest, or with status {@code 400 (Bad Request)} if the manifest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manifests")
    public ResponseEntity<Manifest> createManifest(@RequestBody Manifest manifest) throws URISyntaxException {
        log.debug("REST request to save Manifest : {}", manifest);
        if (manifest.getId() != null) {
            throw new BadRequestAlertException("A new manifest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Manifest result = manifestRepository.save(manifest);
        return ResponseEntity
            .created(new URI("/api/manifests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /manifests/:id} : Updates an existing manifest.
     *
     * @param id the id of the manifest to save.
     * @param manifest the manifest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manifest,
     * or with status {@code 400 (Bad Request)} if the manifest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manifest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/manifests/{id}")
    public ResponseEntity<Manifest> updateManifest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manifest manifest
    ) throws URISyntaxException {
        log.debug("REST request to update Manifest : {}, {}", id, manifest);
        if (manifest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manifest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manifestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Manifest result = manifestRepository.save(manifest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manifest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /manifests/:id} : Partial updates given fields of an existing manifest, field will ignore if it is null
     *
     * @param id the id of the manifest to save.
     * @param manifest the manifest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manifest,
     * or with status {@code 400 (Bad Request)} if the manifest is not valid,
     * or with status {@code 404 (Not Found)} if the manifest is not found,
     * or with status {@code 500 (Internal Server Error)} if the manifest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/manifests/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Manifest> partialUpdateManifest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manifest manifest
    ) throws URISyntaxException {
        log.debug("REST request to partial update Manifest partially : {}, {}", id, manifest);
        if (manifest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manifest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manifestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Manifest> result = manifestRepository
            .findById(manifest.getId())
            .map(
                existingManifest -> {
                    if (manifest.getManifestId() != null) {
                        existingManifest.setManifestId(manifest.getManifestId());
                    }
                    if (manifest.getAwb() != null) {
                        existingManifest.setAwb(manifest.getAwb());
                    }
                    if (manifest.getAwbAssignedDate() != null) {
                        existingManifest.setAwbAssignedDate(manifest.getAwbAssignedDate());
                    }
                    if (manifest.getPickupException() != null) {
                        existingManifest.setPickupException(manifest.getPickupException());
                    }
                    if (manifest.getRemarks() != null) {
                        existingManifest.setRemarks(manifest.getRemarks());
                    }
                    if (manifest.getPickupReferenceNumber() != null) {
                        existingManifest.setPickupReferenceNumber(manifest.getPickupReferenceNumber());
                    }
                    if (manifest.getStatus() != null) {
                        existingManifest.setStatus(manifest.getStatus());
                    }

                    return existingManifest;
                }
            )
            .map(manifestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manifest.getId().toString())
        );
    }

    /**
     * {@code GET  /manifests} : get all the manifests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manifests in body.
     */
    @GetMapping("/manifests")
    public List<Manifest> getAllManifests() {
        log.debug("REST request to get all Manifests");
        return manifestRepository.findAll();
    }

    /**
     * {@code GET  /manifests/:id} : get the "id" manifest.
     *
     * @param id the id of the manifest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manifest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/manifests/{id}")
    public ResponseEntity<Manifest> getManifest(@PathVariable Long id) {
        log.debug("REST request to get Manifest : {}", id);
        Optional<Manifest> manifest = manifestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(manifest);
    }

    /**
     * {@code DELETE  /manifests/:id} : delete the "id" manifest.
     *
     * @param id the id of the manifest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manifests/{id}")
    public ResponseEntity<Void> deleteManifest(@PathVariable Long id) {
        log.debug("REST request to delete Manifest : {}", id);
        manifestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
