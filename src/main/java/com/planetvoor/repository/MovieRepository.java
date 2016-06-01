package com.planetvoor.repository;

import com.planetvoor.domain.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author voor
 */
public interface MovieRepository extends JpaRepository<MovieEntity, Long>, QueryDslPredicateExecutor<MovieEntity> {
}
