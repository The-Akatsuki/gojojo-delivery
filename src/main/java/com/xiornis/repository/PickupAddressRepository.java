package com.xiornis.repository;

import com.xiornis.domain.PickupAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PickupAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PickupAddressRepository extends JpaRepository<PickupAddress, Long> {}
