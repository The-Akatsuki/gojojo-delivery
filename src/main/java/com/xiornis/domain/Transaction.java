package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiornis.domain.enumeration.TransactionType;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "credit")
    private Float credit;

    @Column(name = "debit")
    private Float debit;

    @Column(name = "final_balance")
    private Float finalBalance;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "transactions" }, allowSetters = true)
    private Wallet wallet;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction id(Long id) {
        this.id = id;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Transaction category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getCredit() {
        return this.credit;
    }

    public Transaction credit(Float credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }

    public Float getDebit() {
        return this.debit;
    }

    public Transaction debit(Float debit) {
        this.debit = debit;
        return this;
    }

    public void setDebit(Float debit) {
        this.debit = debit;
    }

    public Float getFinalBalance() {
        return this.finalBalance;
    }

    public Transaction finalBalance(Float finalBalance) {
        this.finalBalance = finalBalance;
        return this;
    }

    public void setFinalBalance(Float finalBalance) {
        this.finalBalance = finalBalance;
    }

    public String getDescription() {
        return this.description;
    }

    public Transaction description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    public Transaction transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public Transaction wallet(Wallet wallet) {
        this.setWallet(wallet);
        return this;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", credit=" + getCredit() +
            ", debit=" + getDebit() +
            ", finalBalance=" + getFinalBalance() +
            ", description='" + getDescription() + "'" +
            ", transactionType='" + getTransactionType() + "'" +
            "}";
    }
}
