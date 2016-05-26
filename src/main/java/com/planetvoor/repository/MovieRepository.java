package com.planetvoor.repository;

import com.planetvoor.domain.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author voor
 */
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
}
