package com.planetvoor.repository;

import com.planetvoor.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author voor
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
