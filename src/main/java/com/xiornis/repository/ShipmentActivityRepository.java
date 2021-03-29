package com.xiornis.repository;

import com.xiornis.domain.ShipmentActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShipmentActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentActivityRepository extends JpaRepository<ShipmentActivity, Long> {}
