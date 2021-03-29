package com.xiornis.repository;

import com.xiornis.domain.PinCodes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PinCodes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PinCodesRepository extends JpaRepository<PinCodes, Long> {}
