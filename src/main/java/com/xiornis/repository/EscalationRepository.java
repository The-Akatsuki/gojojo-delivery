package com.xiornis.repository;

import com.xiornis.domain.Escalation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Escalation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {}
