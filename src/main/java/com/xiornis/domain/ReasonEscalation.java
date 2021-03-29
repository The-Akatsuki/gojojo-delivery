package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReasonEscalation.
 */
@Entity
@Table(name = "reason_escalation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReasonEscalation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "reason")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "manifests", "reason" }, allowSetters = true)
    private Set<Escalation> escalations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReasonEscalation id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public ReasonEscalation title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Escalation> getEscalations() {
        return this.escalations;
    }

    public ReasonEscalation escalations(Set<Escalation> escalations) {
        this.setEscalations(escalations);
        return this;
    }

    public ReasonEscalation addEscalation(Escalation escalation) {
        this.escalations.add(escalation);
        escalation.setReason(this);
        return this;
    }

    public ReasonEscalation removeEscalation(Escalation escalation) {
        this.escalations.remove(escalation);
        escalation.setReason(null);
        return this;
    }

    public void setEscalations(Set<Escalation> escalations) {
        if (this.escalations != null) {
            this.escalations.forEach(i -> i.setReason(null));
        }
        if (escalations != null) {
            escalations.forEach(i -> i.setReason(this));
        }
        this.escalations = escalations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReasonEscalation)) {
            return false;
        }
        return id != null && id.equals(((ReasonEscalation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReasonEscalation{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
