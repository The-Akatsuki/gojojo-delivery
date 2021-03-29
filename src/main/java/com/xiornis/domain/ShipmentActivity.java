package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShipmentActivity.
 */
@Entity
@Table(name = "shipment_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShipmentActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "shipmentActivities", "wallets", "buyerDetails", "product", "payment", "pickupaddress" },
        allowSetters = true
    )
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShipmentActivity id(Long id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    public ShipmentActivity status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPincode() {
        return this.pincode;
    }

    public ShipmentActivity pincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocation() {
        return this.location;
    }

    public ShipmentActivity location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Order getOrder() {
        return this.order;
    }

    public ShipmentActivity order(Order order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentActivity)) {
            return false;
        }
        return id != null && id.equals(((ShipmentActivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShipmentActivity{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", pincode='" + getPincode() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
