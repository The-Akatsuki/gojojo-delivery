package com.xiornis.repository;

import com.xiornis.domain.Manifest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Manifest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ManifestRepository extends JpaRepository<Manifest, Long> {}
