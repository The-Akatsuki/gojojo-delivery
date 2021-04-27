package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderBuyerDetails.
 */
@Entity
@Table(name = "order_buyer_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderBuyerDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "alternate_mobile")
    private String alternateMobile;

    @Column(name = "email")
    private String email;

    @Column(name = "addressline_1")
    private String addressline1;

    @Column(name = "addressline_2")
    private String addressline2;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "is_billing_same")
    private Boolean isBillingSame;

    @Column(name = "bill_addressline_1")
    private String billAddressline1;

    @Column(name = "bill_addressline_2")
    private String billAddressline2;

    @Column(name = "bill_pincode")
    private String billPincode;

    @Column(name = "bill_city")
    private String billCity;

    @Column(name = "bill_state")
    private String billState;

    @Column(name = "bill_country")
    private String billCountry;

    @JsonIgnoreProperties(
        value = { "buyerDetails", "shipmentActivities", "products", "payment", "pickupaddress", "order" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "buyerDetails")
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderBuyerDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public OrderBuyerDetails name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public OrderBuyerDetails mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateMobile() {
        return this.alternateMobile;
    }

    public OrderBuyerDetails alternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
        return this;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getEmail() {
        return this.email;
    }

    public OrderBuyerDetails email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressline1() {
        return this.addressline1;
    }

    public OrderBuyerDetails addressline1(String addressline1) {
        this.addressline1 = addressline1;
        return this;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return this.addressline2;
    }

    public OrderBuyerDetails addressline2(String addressline2) {
        this.addressline2 = addressline2;
        return this;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getPincode() {
        return this.pincode;
    }

    public OrderBuyerDetails pincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return this.city;
    }

    public OrderBuyerDetails city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public OrderBuyerDetails state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public OrderBuyerDetails country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public OrderBuyerDetails companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getIsBillingSame() {
        return this.isBillingSame;
    }

    public OrderBuyerDetails isBillingSame(Boolean isBillingSame) {
        this.isBillingSame = isBillingSame;
        return this;
    }

    public void setIsBillingSame(Boolean isBillingSame) {
        this.isBillingSame = isBillingSame;
    }

    public String getBillAddressline1() {
        return this.billAddressline1;
    }

    public OrderBuyerDetails billAddressline1(String billAddressline1) {
        this.billAddressline1 = billAddressline1;
        return this;
    }

    public void setBillAddressline1(String billAddressline1) {
        this.billAddressline1 = billAddressline1;
    }

    public String getBillAddressline2() {
        return this.billAddressline2;
    }

    public OrderBuyerDetails billAddressline2(String billAddressline2) {
        this.billAddressline2 = billAddressline2;
        return this;
    }

    public void setBillAddressline2(String billAddressline2) {
        this.billAddressline2 = billAddressline2;
    }

    public String getBillPincode() {
        return this.billPincode;
    }

    public OrderBuyerDetails billPincode(String billPincode) {
        this.billPincode = billPincode;
        return this;
    }

    public void setBillPincode(String billPincode) {
        this.billPincode = billPincode;
    }

    public String getBillCity() {
        return this.billCity;
    }

    public OrderBuyerDetails billCity(String billCity) {
        this.billCity = billCity;
        return this;
    }

    public void setBillCity(String billCity) {
        this.billCity = billCity;
    }

    public String getBillState() {
        return this.billState;
    }

    public OrderBuyerDetails billState(String billState) {
        this.billState = billState;
        return this;
    }

    public void setBillState(String billState) {
        this.billState = billState;
    }

    public String getBillCountry() {
        return this.billCountry;
    }

    public OrderBuyerDetails billCountry(String billCountry) {
        this.billCountry = billCountry;
        return this;
    }

    public void setBillCountry(String billCountry) {
        this.billCountry = billCountry;
    }

    public Order getOrder() {
        return this.order;
    }

    public OrderBuyerDetails order(Order order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Order order) {
        if (this.order != null) {
            this.order.setBuyerDetails(null);
        }
        if (order != null) {
            order.setBuyerDetails(this);
        }
        this.order = order;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderBuyerDetails)) {
            return false;
        }
        return id != null && id.equals(((OrderBuyerDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderBuyerDetails{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", alternateMobile='" + getAlternateMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", addressline1='" + getAddressline1() + "'" +
            ", addressline2='" + getAddressline2() + "'" +
            ", pincode='" + getPincode() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", isBillingSame='" + getIsBillingSame() + "'" +
            ", billAddressline1='" + getBillAddressline1() + "'" +
            ", billAddressline2='" + getBillAddressline2() + "'" +
            ", billPincode='" + getBillPincode() + "'" +
            ", billCity='" + getBillCity() + "'" +
            ", billState='" + getBillState() + "'" +
            ", billCountry='" + getBillCountry() + "'" +
            "}";
    }
}
