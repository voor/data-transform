package com.planetvoor.repository;

import com.planetvoor.domain.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author voor
 */
public interface RatingRepository extends JpaRepository<RatingEntity, Long>, QueryDslPredicateExecutor<RatingEntity> {
}
