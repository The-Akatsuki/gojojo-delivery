package com.xiornis.repository;

import com.xiornis.domain.ReturnReason;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReturnReason entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnReasonRepository extends JpaRepository<ReturnReason, Long> {}
