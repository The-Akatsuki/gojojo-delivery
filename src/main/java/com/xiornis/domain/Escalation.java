package com.xiornis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Escalation.
 */
@Entity
@Table(name = "escalation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Escalation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "escalation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courier", "escalation" }, allowSetters = true)
    private Set<Manifest> manifests = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "escalations" }, allowSetters = true)
    private ReasonEscalation reason;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Escalation id(Long id) {
        this.id = id;
        return this;
    }

    public String getRemark() {
        return this.remark;
    }

    public Escalation remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<Manifest> getManifests() {
        return this.manifests;
    }

    public Escalation manifests(Set<Manifest> manifests) {
        this.setManifests(manifests);
        return this;
    }

    public Escalation addManifest(Manifest manifest) {
        this.manifests.add(manifest);
        manifest.setEscalation(this);
        return this;
    }

    public Escalation removeManifest(Manifest manifest) {
        this.manifests.remove(manifest);
        manifest.setEscalation(null);
        return this;
    }

    public void setManifests(Set<Manifest> manifests) {
        if (this.manifests != null) {
            this.manifests.forEach(i -> i.setEscalation(null));
        }
        if (manifests != null) {
            manifests.forEach(i -> i.setEscalation(this));
        }
        this.manifests = manifests;
    }

    public ReasonEscalation getReason() {
        return this.reason;
    }

    public Escalation reason(ReasonEscalation reasonEscalation) {
        this.setReason(reasonEscalation);
        return this;
    }

    public void setReason(ReasonEscalation reasonEscalation) {
        this.reason = reasonEscalation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Escalation)) {
            return false;
        }
        return id != null && id.equals(((Escalation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Escalation{" +
            "id=" + getId() +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
