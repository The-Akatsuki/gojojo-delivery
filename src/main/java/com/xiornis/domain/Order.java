package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiornis.domain.enumeration.OrderStatus;
import com.xiornis.domain.enumeration.OrderType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "reference_id")
    private String referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "channel")
    private String channel;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "has_shipping_charges")
    private Boolean hasShippingCharges;

    @Column(name = "shipping")
    private Double shipping;

    @Column(name = "has_giftwrap_charges")
    private Boolean hasGiftwrapCharges;

    @Column(name = "giftwrap")
    private Double giftwrap;

    @Column(name = "has_transaction_charges")
    private Boolean hasTransactionCharges;

    @Column(name = "transaction")
    private Double transaction;

    @Column(name = "has_discount")
    private Boolean hasDiscount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "weight_charges")
    private Float weightCharges;

    @Column(name = "excess_weight_charges")
    private Float excessWeightCharges;

    @Column(name = "total_freight_charges")
    private Float totalFreightCharges;

    @Column(name = "length")
    private Float length;

    @Column(name = "width")
    private Float width;

    @Column(name = "height")
    private Float height;

    @Column(name = "reseller_name")
    private String resellerName;

    @Column(name = "gstin")
    private String gstin;

    @Column(name = "courier")
    private String courier;

    @Column(name = "awb")
    private String awb;

    @Column(name = "manifest_id")
    private String manifestId;

    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private OrderBuyerDetails buyerDetails;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<ShipmentActivity> shipmentActivities = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_jhi_order__product",
        joinColumns = @JoinColumn(name = "jhi_order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnoreProperties(value = { "categories", "orders" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private PaymentMethod payment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private PickupAddress pickupaddress;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "transactions" }, allowSetters = true)
    private Wallet order;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order id(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public Order orderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getReferenceId() {
        return this.referenceId;
    }

    public Order referenceId(String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public OrderType getOrderType() {
        return this.orderType;
    }

    public Order orderType(OrderType orderType) {
        this.orderType = orderType;
        return this;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public Order orderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public Order orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public String getChannel() {
        return this.channel;
    }

    public Order channel(String channel) {
        this.channel = channel;
        return this;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public Order subtotal(Double subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Boolean getHasShippingCharges() {
        return this.hasShippingCharges;
    }

    public Order hasShippingCharges(Boolean hasShippingCharges) {
        this.hasShippingCharges = hasShippingCharges;
        return this;
    }

    public void setHasShippingCharges(Boolean hasShippingCharges) {
        this.hasShippingCharges = hasShippingCharges;
    }

    public Double getShipping() {
        return this.shipping;
    }

    public Order shipping(Double shipping) {
        this.shipping = shipping;
        return this;
    }

    public void setShipping(Double shipping) {
        this.shipping = shipping;
    }

    public Boolean getHasGiftwrapCharges() {
        return this.hasGiftwrapCharges;
    }

    public Order hasGiftwrapCharges(Boolean hasGiftwrapCharges) {
        this.hasGiftwrapCharges = hasGiftwrapCharges;
        return this;
    }

    public void setHasGiftwrapCharges(Boolean hasGiftwrapCharges) {
        this.hasGiftwrapCharges = hasGiftwrapCharges;
    }

    public Double getGiftwrap() {
        return this.giftwrap;
    }

    public Order giftwrap(Double giftwrap) {
        this.giftwrap = giftwrap;
        return this;
    }

    public void setGiftwrap(Double giftwrap) {
        this.giftwrap = giftwrap;
    }

    public Boolean getHasTransactionCharges() {
        return this.hasTransactionCharges;
    }

    public Order hasTransactionCharges(Boolean hasTransactionCharges) {
        this.hasTransactionCharges = hasTransactionCharges;
        return this;
    }

    public void setHasTransactionCharges(Boolean hasTransactionCharges) {
        this.hasTransactionCharges = hasTransactionCharges;
    }

    public Double getTransaction() {
        return this.transaction;
    }

    public Order transaction(Double transaction) {
        this.transaction = transaction;
        return this;
    }

    public void setTransaction(Double transaction) {
        this.transaction = transaction;
    }

    public Boolean getHasDiscount() {
        return this.hasDiscount;
    }

    public Order hasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
        return this;
    }

    public void setHasDiscount(Boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public Order discount(Double discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Float getWeight() {
        return this.weight;
    }

    public Order weight(Float weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getWeightCharges() {
        return this.weightCharges;
    }

    public Order weightCharges(Float weightCharges) {
        this.weightCharges = weightCharges;
        return this;
    }

    public void setWeightCharges(Float weightCharges) {
        this.weightCharges = weightCharges;
    }

    public Float getExcessWeightCharges() {
        return this.excessWeightCharges;
    }

    public Order excessWeightCharges(Float excessWeightCharges) {
        this.excessWeightCharges = excessWeightCharges;
        return this;
    }

    public void setExcessWeightCharges(Float excessWeightCharges) {
        this.excessWeightCharges = excessWeightCharges;
    }

    public Float getTotalFreightCharges() {
        return this.totalFreightCharges;
    }

    public Order totalFreightCharges(Float totalFreightCharges) {
        this.totalFreightCharges = totalFreightCharges;
        return this;
    }

    public void setTotalFreightCharges(Float totalFreightCharges) {
        this.totalFreightCharges = totalFreightCharges;
    }

    public Float getLength() {
        return this.length;
    }

    public Order length(Float length) {
        this.length = length;
        return this;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Float getWidth() {
        return this.width;
    }

    public Order width(Float width) {
        this.width = width;
        return this;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return this.height;
    }

    public Order height(Float height) {
        this.height = height;
        return this;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public String getResellerName() {
        return this.resellerName;
    }

    public Order resellerName(String resellerName) {
        this.resellerName = resellerName;
        return this;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName;
    }

    public String getGstin() {
        return this.gstin;
    }

    public Order gstin(String gstin) {
        this.gstin = gstin;
        return this;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getCourier() {
        return this.courier;
    }

    public Order courier(String courier) {
        this.courier = courier;
        return this;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getAwb() {
        return this.awb;
    }

    public Order awb(String awb) {
        this.awb = awb;
        return this;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getManifestId() {
        return this.manifestId;
    }

    public Order manifestId(String manifestId) {
        this.manifestId = manifestId;
        return this;
    }

    public void setManifestId(String manifestId) {
        this.manifestId = manifestId;
    }

    public OrderBuyerDetails getBuyerDetails() {
        return this.buyerDetails;
    }

    public Order buyerDetails(OrderBuyerDetails orderBuyerDetails) {
        this.setBuyerDetails(orderBuyerDetails);
        return this;
    }

    public void setBuyerDetails(OrderBuyerDetails orderBuyerDetails) {
        this.buyerDetails = orderBuyerDetails;
    }

    public Set<ShipmentActivity> getShipmentActivities() {
        return this.shipmentActivities;
    }

    public Order shipmentActivities(Set<ShipmentActivity> shipmentActivities) {
        this.setShipmentActivities(shipmentActivities);
        return this;
    }

    public Order addShipmentActivity(ShipmentActivity shipmentActivity) {
        this.shipmentActivities.add(shipmentActivity);
        shipmentActivity.setOrder(this);
        return this;
    }

    public Order removeShipmentActivity(ShipmentActivity shipmentActivity) {
        this.shipmentActivities.remove(shipmentActivity);
        shipmentActivity.setOrder(null);
        return this;
    }

    public void setShipmentActivities(Set<ShipmentActivity> shipmentActivities) {
        if (this.shipmentActivities != null) {
            this.shipmentActivities.forEach(i -> i.setOrder(null));
        }
        if (shipmentActivities != null) {
            shipmentActivities.forEach(i -> i.setOrder(this));
        }
        this.shipmentActivities = shipmentActivities;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public Order products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Order addProduct(Product product) {
        this.products.add(product);
        product.getOrders().add(this);
        return this;
    }

    public Order removeProduct(Product product) {
        this.products.remove(product);
        product.getOrders().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public PaymentMethod getPayment() {
        return this.payment;
    }

    public Order payment(PaymentMethod paymentMethod) {
        this.setPayment(paymentMethod);
        return this;
    }

    public void setPayment(PaymentMethod paymentMethod) {
        this.payment = paymentMethod;
    }

    public PickupAddress getPickupaddress() {
        return this.pickupaddress;
    }

    public Order pickupaddress(PickupAddress pickupAddress) {
        this.setPickupaddress(pickupAddress);
        return this;
    }

    public void setPickupaddress(PickupAddress pickupAddress) {
        this.pickupaddress = pickupAddress;
    }

    public Wallet getOrder() {
        return this.order;
    }

    public Order order(Wallet wallet) {
        this.setOrder(wallet);
        return this;
    }

    public void setOrder(Wallet wallet) {
        this.order = wallet;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", referenceId='" + getReferenceId() + "'" +
            ", orderType='" + getOrderType() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", channel='" + getChannel() + "'" +
            ", subtotal=" + getSubtotal() +
            ", hasShippingCharges='" + getHasShippingCharges() + "'" +
            ", shipping=" + getShipping() +
            ", hasGiftwrapCharges='" + getHasGiftwrapCharges() + "'" +
            ", giftwrap=" + getGiftwrap() +
            ", hasTransactionCharges='" + getHasTransactionCharges() + "'" +
            ", transaction=" + getTransaction() +
            ", hasDiscount='" + getHasDiscount() + "'" +
            ", discount=" + getDiscount() +
            ", weight=" + getWeight() +
            ", weightCharges=" + getWeightCharges() +
            ", excessWeightCharges=" + getExcessWeightCharges() +
            ", totalFreightCharges=" + getTotalFreightCharges() +
            ", length=" + getLength() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", resellerName='" + getResellerName() + "'" +
            ", gstin='" + getGstin() + "'" +
            ", courier='" + getCourier() + "'" +
            ", awb='" + getAwb() + "'" +
            ", manifestId='" + getManifestId() + "'" +
            "}";
    }
}
