package com.planetvoor.repository;

import com.planetvoor.domain.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author voor
 */
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
}
