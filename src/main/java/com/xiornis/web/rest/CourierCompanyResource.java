package com.xiornis.web.rest;

import com.xiornis.domain.CourierCompany;
import com.xiornis.repository.CourierCompanyRepository;
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
 * REST controller for managing {@link com.xiornis.domain.CourierCompany}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CourierCompanyResource {

    private final Logger log = LoggerFactory.getLogger(CourierCompanyResource.class);

    private static final String ENTITY_NAME = "courierCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourierCompanyRepository courierCompanyRepository;

    public CourierCompanyResource(CourierCompanyRepository courierCompanyRepository) {
        this.courierCompanyRepository = courierCompanyRepository;
    }

    /**
     * {@code POST  /courier-companies} : Create a new courierCompany.
     *
     * @param courierCompany the courierCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courierCompany, or with status {@code 400 (Bad Request)} if the courierCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courier-companies")
    public ResponseEntity<CourierCompany> createCourierCompany(@RequestBody CourierCompany courierCompany) throws URISyntaxException {
        log.debug("REST request to save CourierCompany : {}", courierCompany);
        if (courierCompany.getId() != null) {
            throw new BadRequestAlertException("A new courierCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourierCompany result = courierCompanyRepository.save(courierCompany);
        return ResponseEntity
            .created(new URI("/api/courier-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courier-companies/:id} : Updates an existing courierCompany.
     *
     * @param id the id of the courierCompany to save.
     * @param courierCompany the courierCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courierCompany,
     * or with status {@code 400 (Bad Request)} if the courierCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courierCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courier-companies/{id}")
    public ResponseEntity<CourierCompany> updateCourierCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourierCompany courierCompany
    ) throws URISyntaxException {
        log.debug("REST request to update CourierCompany : {}, {}", id, courierCompany);
        if (courierCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courierCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courierCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourierCompany result = courierCompanyRepository.save(courierCompany);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courierCompany.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /courier-companies/:id} : Partial updates given fields of an existing courierCompany, field will ignore if it is null
     *
     * @param id the id of the courierCompany to save.
     * @param courierCompany the courierCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courierCompany,
     * or with status {@code 400 (Bad Request)} if the courierCompany is not valid,
     * or with status {@code 404 (Not Found)} if the courierCompany is not found,
     * or with status {@code 500 (Internal Server Error)} if the courierCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courier-companies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CourierCompany> partialUpdateCourierCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CourierCompany courierCompany
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourierCompany partially : {}, {}", id, courierCompany);
        if (courierCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courierCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courierCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourierCompany> result = courierCompanyRepository
            .findById(courierCompany.getId())
            .map(
                existingCourierCompany -> {
                    if (courierCompany.getName() != null) {
                        existingCourierCompany.setName(courierCompany.getName());
                    }
                    if (courierCompany.getMobile() != null) {
                        existingCourierCompany.setMobile(courierCompany.getMobile());
                    }
                    if (courierCompany.getAlternateMobile() != null) {
                        existingCourierCompany.setAlternateMobile(courierCompany.getAlternateMobile());
                    }
                    if (courierCompany.getEmail() != null) {
                        existingCourierCompany.setEmail(courierCompany.getEmail());
                    }
                    if (courierCompany.getAddressline1() != null) {
                        existingCourierCompany.setAddressline1(courierCompany.getAddressline1());
                    }
                    if (courierCompany.getAddressline2() != null) {
                        existingCourierCompany.setAddressline2(courierCompany.getAddressline2());
                    }
                    if (courierCompany.getPincode() != null) {
                        existingCourierCompany.setPincode(courierCompany.getPincode());
                    }
                    if (courierCompany.getCity() != null) {
                        existingCourierCompany.setCity(courierCompany.getCity());
                    }
                    if (courierCompany.getState() != null) {
                        existingCourierCompany.setState(courierCompany.getState());
                    }
                    if (courierCompany.getCountry() != null) {
                        existingCourierCompany.setCountry(courierCompany.getCountry());
                    }
                    if (courierCompany.getImage() != null) {
                        existingCourierCompany.setImage(courierCompany.getImage());
                    }

                    return existingCourierCompany;
                }
            )
            .map(courierCompanyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courierCompany.getId().toString())
        );
    }

    /**
     * {@code GET  /courier-companies} : get all the courierCompanies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courierCompanies in body.
     */
    @GetMapping("/courier-companies")
    public ResponseEntity<List<CourierCompany>> getAllCourierCompanies(Pageable pageable) {
        log.debug("REST request to get a page of CourierCompanies");
        Page<CourierCompany> page = courierCompanyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /courier-companies/:id} : get the "id" courierCompany.
     *
     * @param id the id of the courierCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courierCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courier-companies/{id}")
    public ResponseEntity<CourierCompany> getCourierCompany(@PathVariable Long id) {
        log.debug("REST request to get CourierCompany : {}", id);
        Optional<CourierCompany> courierCompany = courierCompanyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(courierCompany);
    }

    /**
     * {@code DELETE  /courier-companies/:id} : delete the "id" courierCompany.
     *
     * @param id the id of the courierCompany to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courier-companies/{id}")
    public ResponseEntity<Void> deleteCourierCompany(@PathVariable Long id) {
        log.debug("REST request to delete CourierCompany : {}", id);
        courierCompanyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
