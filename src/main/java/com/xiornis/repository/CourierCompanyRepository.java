package com.xiornis.repository;

import com.xiornis.domain.CourierCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourierCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourierCompanyRepository extends JpaRepository<CourierCompany, Long> {}
