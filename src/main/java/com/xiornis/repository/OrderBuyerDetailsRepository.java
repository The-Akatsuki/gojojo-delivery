package com.xiornis.repository;

import com.xiornis.domain.OrderBuyerDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrderBuyerDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderBuyerDetailsRepository extends JpaRepository<OrderBuyerDetails, Long> {}
