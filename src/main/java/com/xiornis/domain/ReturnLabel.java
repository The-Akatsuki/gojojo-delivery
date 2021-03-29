package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReturnLabel.
 */
@Entity
@Table(name = "return_label")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReturnLabel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "label")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "label" }, allowSetters = true)
    private Set<ReturnReason> returnReasons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReturnLabel id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ReturnLabel title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<ReturnReason> getReturnReasons() {
        return this.returnReasons;
    }

    public ReturnLabel returnReasons(Set<ReturnReason> returnReasons) {
        this.setReturnReasons(returnReasons);
        return this;
    }

    public ReturnLabel addReturnReason(ReturnReason returnReason) {
        this.returnReasons.add(returnReason);
        returnReason.setLabel(this);
        return this;
    }

    public ReturnLabel removeReturnReason(ReturnReason returnReason) {
        this.returnReasons.remove(returnReason);
        returnReason.setLabel(null);
        return this;
    }

    public void setReturnReasons(Set<ReturnReason> returnReasons) {
        if (this.returnReasons != null) {
            this.returnReasons.forEach(i -> i.setLabel(null));
        }
        if (returnReasons != null) {
            returnReasons.forEach(i -> i.setLabel(this));
        }
        this.returnReasons = returnReasons;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReturnLabel)) {
            return false;
        }
        return id != null && id.equals(((ReturnLabel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnLabel{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
