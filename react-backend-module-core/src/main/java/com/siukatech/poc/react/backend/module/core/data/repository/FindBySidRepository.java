package com.siukatech.poc.react.backend.module.core.data.repository;

import java.util.Optional;

/**
 *
 * *** READ this ***
 * This is NOT working, DO NOT define an abstract repository interface for more than 3 types.
 * public interface AbstractRepository<T, ID, SID> extends JpaRepository<T, ID>, FindBySidRepository<T, SID> {}
 *
 * @param <T>
 * @param <SID>
 */
public interface FindBySidRepository<T, SID> {
    Optional<T> findBySid(SID sid);
}
