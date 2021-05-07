package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Manifest.
 */
@Entity
@Table(name = "manifest")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Manifest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manifest_id")
    private String manifestId;

    @Column(name = "awb")
    private String awb;

    @Column(name = "awb_assigned_date")
    private Instant awbAssignedDate;

    @Column(name = "pickup_exception")
    private String pickupException;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "pickup_reference_number")
    private String pickupReferenceNumber;

    @Column(name = "status")
    private String status;

    @JsonIgnoreProperties(
        value = { "buyerDetails", "shipmentActivities", "products", "manifest", "payment", "pickupaddress", "order" },
        allowSetters = true
    )
    @OneToOne
    @JoinColumn(unique = true)
    private Order order;

    @ManyToOne
    @JsonIgnoreProperties(value = { "manifests" }, allowSetters = true)
    private CourierCompany courier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "manifests", "reason" }, allowSetters = true)
    private Escalation escalation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Manifest id(Long id) {
        this.id = id;
        return this;
    }

    public String getManifestId() {
        return this.manifestId;
    }

    public Manifest manifestId(String manifestId) {
        this.manifestId = manifestId;
        return this;
    }

    public void setManifestId(String manifestId) {
        this.manifestId = manifestId;
    }

    public String getAwb() {
        return this.awb;
    }

    public Manifest awb(String awb) {
        this.awb = awb;
        return this;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public Instant getAwbAssignedDate() {
        return this.awbAssignedDate;
    }

    public Manifest awbAssignedDate(Instant awbAssignedDate) {
        this.awbAssignedDate = awbAssignedDate;
        return this;
    }

    public void setAwbAssignedDate(Instant awbAssignedDate) {
        this.awbAssignedDate = awbAssignedDate;
    }

    public String getPickupException() {
        return this.pickupException;
    }

    public Manifest pickupException(String pickupException) {
        this.pickupException = pickupException;
        return this;
    }

    public void setPickupException(String pickupException) {
        this.pickupException = pickupException;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Manifest remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPickupReferenceNumber() {
        return this.pickupReferenceNumber;
    }

    public Manifest pickupReferenceNumber(String pickupReferenceNumber) {
        this.pickupReferenceNumber = pickupReferenceNumber;
        return this;
    }

    public void setPickupReferenceNumber(String pickupReferenceNumber) {
        this.pickupReferenceNumber = pickupReferenceNumber;
    }

    public String getStatus() {
        return this.status;
    }

    public Manifest status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order getOrder() {
        return this.order;
    }

    public Manifest order(Order order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public CourierCompany getCourier() {
        return this.courier;
    }

    public Manifest courier(CourierCompany courierCompany) {
        this.setCourier(courierCompany);
        return this;
    }

    public void setCourier(CourierCompany courierCompany) {
        this.courier = courierCompany;
    }

    public Escalation getEscalation() {
        return this.escalation;
    }

    public Manifest escalation(Escalation escalation) {
        this.setEscalation(escalation);
        return this;
    }

    public void setEscalation(Escalation escalation) {
        this.escalation = escalation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Manifest)) {
            return false;
        }
        return id != null && id.equals(((Manifest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Manifest{" +
            "id=" + getId() +
            ", manifestId='" + getManifestId() + "'" +
            ", awb='" + getAwb() + "'" +
            ", awbAssignedDate='" + getAwbAssignedDate() + "'" +
            ", pickupException='" + getPickupException() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", pickupReferenceNumber='" + getPickupReferenceNumber() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
