package com.xiornis.repository;

import com.xiornis.domain.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
        value = "select distinct order from Order order left join fetch order.products",
        countQuery = "select count(distinct order) from Order order"
    )
    Page<Order> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct order from Order order left join fetch order.products")
    List<Order> findAllWithEagerRelationships();

    @Query("select order from Order order left join fetch order.products where order.id =:id")
    Optional<Order> findOneWithEagerRelationships(@Param("id") Long id);
}
