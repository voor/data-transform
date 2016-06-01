package com.planetvoor.repository;

import com.planetvoor.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author voor
 */
public interface UserRepository extends JpaRepository<UserEntity, Long>, QueryDslPredicateExecutor<UserEntity> {
}
