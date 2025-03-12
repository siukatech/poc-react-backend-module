package com.siukatech.poc.react.backend.module.user.repository;

import com.siukatech.poc.react.backend.module.user.entity.UserViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserViewRepository extends JpaRepository<UserViewEntity, String> {

    Optional<UserViewEntity> findByUserId(String userId);
}
