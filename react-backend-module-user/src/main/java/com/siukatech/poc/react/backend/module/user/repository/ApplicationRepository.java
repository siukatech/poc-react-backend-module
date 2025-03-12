package com.siukatech.poc.react.backend.module.user.repository;

import com.siukatech.poc.react.backend.module.user.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, String> {

    @EntityGraph(attributePaths = {"appResourceEntities"
//            , "userPermissionJpaEntities"
    })
    Optional<ApplicationEntity> findWithGraphAttrPathByApplicationId(String applicationId);

    @Query(value = "select o from ApplicationEntity o "
            + "join fetch o.appResourceEntities ar "
////            + "join fetch o.userPermissionJpaEntities urp "
////            + "join fetch ar.userPermissionJpaEntities urp "
////            + "join fetch urp.userRoleEntity ur "
////            + "join fetch ur.userRoleUserEntities uru "
////            + "join fetch uru.userEntity u "
//            + "join fetch o.appResourceEntities.userPermissionJpaEntities urp "
            + "where 1=1 and o.applicationId = :applicationId ")
    Optional<ApplicationEntity> findWithJpqlAppResourcesByApplicationId(String applicationId);

    @Query(value = "select o from ApplicationEntity o "
//            + "join fetch o.appResourceEntities ar "
            + "join fetch o.userPermissionJpaEntities urp "
            + "join fetch urp.userRoleEntity ur "
//            + "join fetch ur.userRoleUserEntities uru "
//            + "join fetch uru.userEntity u "
            + "where 1=1 and o.applicationId = :applicationId ")
    Optional<ApplicationEntity> findWithJpqlUserPermissionJpaEntitiesByApplicationId(String applicationId);

    @EntityGraph(value = "ApplicationEntity.appResourceEntities")
    Optional<ApplicationEntity> findWithAppResourceEntitiesByApplicationId(String applicationId);

//    @EntityGraph(value = "ApplicationEntity.userPermissionJpaEntities")
    Optional<ApplicationEntity> findWithUserPermissionJpaEntitiesByApplicationId(String applicationId);

}
