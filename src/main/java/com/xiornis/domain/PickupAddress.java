package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PickupAddress.
 */
@Entity
@Table(name = "pickup_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PickupAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

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

    @Column(name = "is_mobile_verified")
    private Boolean isMobileVerified;

    @Column(name = "otp")
    private String otp;

    @OneToMany(mappedBy = "pickupaddress")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "shipmentActivities", "wallets", "buyerDetails", "product", "payment", "pickupaddress" },
        allowSetters = true
    )
    private Set<Order> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PickupAddress id(Long id) {
        this.id = id;
        return this;
    }

    public String getNickName() {
        return this.nickName;
    }

    public PickupAddress nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public PickupAddress mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateMobile() {
        return this.alternateMobile;
    }

    public PickupAddress alternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
        return this;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getEmail() {
        return this.email;
    }

    public PickupAddress email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressline1() {
        return this.addressline1;
    }

    public PickupAddress addressline1(String addressline1) {
        this.addressline1 = addressline1;
        return this;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return this.addressline2;
    }

    public PickupAddress addressline2(String addressline2) {
        this.addressline2 = addressline2;
        return this;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getPincode() {
        return this.pincode;
    }

    public PickupAddress pincode(String pincode) {
        this.pincode = pincode;
        return this;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return this.city;
    }

    public PickupAddress city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public PickupAddress state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public PickupAddress country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public PickupAddress companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getIsMobileVerified() {
        return this.isMobileVerified;
    }

    public PickupAddress isMobileVerified(Boolean isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
        return this;
    }

    public void setIsMobileVerified(Boolean isMobileVerified) {
        this.isMobileVerified = isMobileVerified;
    }

    public String getOtp() {
        return this.otp;
    }

    public PickupAddress otp(String otp) {
        this.otp = otp;
        return this;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public PickupAddress orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public PickupAddress addOrder(Order order) {
        this.orders.add(order);
        order.setPickupaddress(this);
        return this;
    }

    public PickupAddress removeOrder(Order order) {
        this.orders.remove(order);
        order.setPickupaddress(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setPickupaddress(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setPickupaddress(this));
        }
        this.orders = orders;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PickupAddress)) {
            return false;
        }
        return id != null && id.equals(((PickupAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PickupAddress{" +
            "id=" + getId() +
            ", nickName='" + getNickName() + "'" +
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
            ", isMobileVerified='" + getIsMobileVerified() + "'" +
            ", otp='" + getOtp() + "'" +
            "}";
    }
}
