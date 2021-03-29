package com.xiornis.repository;

import com.xiornis.domain.ReturnLabel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReturnLabel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReturnLabelRepository extends JpaRepository<ReturnLabel, Long> {}
