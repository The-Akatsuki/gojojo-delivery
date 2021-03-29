package com.xiornis.repository;

import com.xiornis.domain.ReasonEscalation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReasonEscalation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReasonEscalationRepository extends JpaRepository<ReasonEscalation, Long> {}
