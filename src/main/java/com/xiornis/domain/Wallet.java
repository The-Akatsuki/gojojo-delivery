package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Wallet.
 */
@Entity
@Table(name = "wallet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usable_balance")
    private Double usableBalance;

    @Column(name = "total_balance")
    private Double totalBalance;

    @Column(name = "balance_on_hold")
    private Double balanceOnHold;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "buyerDetails", "shipmentActivities", "products", "payment", "pickupaddress", "order" },
        allowSetters = true
    )
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "wallet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "wallet" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet id(Long id) {
        this.id = id;
        return this;
    }

    public Double getUsableBalance() {
        return this.usableBalance;
    }

    public Wallet usableBalance(Double usableBalance) {
        this.usableBalance = usableBalance;
        return this;
    }

    public void setUsableBalance(Double usableBalance) {
        this.usableBalance = usableBalance;
    }

    public Double getTotalBalance() {
        return this.totalBalance;
    }

    public Wallet totalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
        return this;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getBalanceOnHold() {
        return this.balanceOnHold;
    }

    public Wallet balanceOnHold(Double balanceOnHold) {
        this.balanceOnHold = balanceOnHold;
        return this;
    }

    public void setBalanceOnHold(Double balanceOnHold) {
        this.balanceOnHold = balanceOnHold;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public Wallet orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Wallet addOrder(Order order) {
        this.orders.add(order);
        order.setOrder(this);
        return this;
    }

    public Wallet removeOrder(Order order) {
        this.orders.remove(order);
        order.setOrder(null);
        return this;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setOrder(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setOrder(this));
        }
        this.orders = orders;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public Wallet transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Wallet addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setWallet(this);
        return this;
    }

    public Wallet removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setWallet(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setWallet(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setWallet(this));
        }
        this.transactions = transactions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Wallet)) {
            return false;
        }
        return id != null && id.equals(((Wallet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", usableBalance=" + getUsableBalance() +
            ", totalBalance=" + getTotalBalance() +
            ", balanceOnHold=" + getBalanceOnHold() +
            "}";
    }
}
