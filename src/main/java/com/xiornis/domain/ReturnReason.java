package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReturnReason.
 */
@Entity
@Table(name = "return_reason")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReturnReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JsonIgnoreProperties(value = { "returnReasons" }, allowSetters = true)
    private ReturnLabel label;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReturnReason id(Long id) {
        this.id = id;
        return this;
    }

    public String getComment() {
        return this.comment;
    }

    public ReturnReason comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return this.image;
    }

    public ReturnReason image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ReturnLabel getLabel() {
        return this.label;
    }

    public ReturnReason label(ReturnLabel returnLabel) {
        this.setLabel(returnLabel);
        return this;
    }

    public void setLabel(ReturnLabel returnLabel) {
        this.label = returnLabel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReturnReason)) {
            return false;
        }
        return id != null && id.equals(((ReturnReason) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReturnReason{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
