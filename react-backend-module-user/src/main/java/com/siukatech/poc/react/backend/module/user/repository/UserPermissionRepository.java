package com.siukatech.poc.react.backend.module.user.repository;

import com.siukatech.poc.react.backend.module.user.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, String> {

    // https://stackoverflow.com/a/73583022
    static String SQL_FIND_BY_USER_ID_AND_APPLICATION_ID = "" +
            "select xxx1.* from ( " +
            "select u.user_id user_id, u.id u_id " +
////            "--, uru.id user_role_user_id, uru.user_id, uru.user_role_id, ur.id user_role_id_2 " +
            ", up.id id, up.user_role_id user_role_id" +
            ", up.application_id application_id" +
            ", up.app_resource_id app_resource_id" +
            ", up.access_right access_right " +
            "from users u " +
            "inner join user_role_users uru on 1=1 " +
            "and uru.user_id = u.user_id " +
            "inner join user_roles ur on 1=1 " +
            "and ur.user_role_id = uru.user_role_id " +
            "inner join user_permissions up on 1=1 " +
            "and up.user_role_id = ur.user_role_id " +
            "inner join app_resources ar on 1=1 " +
            "and ar.app_resource_id = up.app_resource_id " +
            "and ar.application_id = up.application_id " +
            "inner join applications a on 1=1 " +
            "and a.application_id = ar.application_id " +
            "where 1=1 " +
            "and u.user_id = :userId " +
            "and a.application_id = :applicationId " +
            ") as xxx1 " +
            "";
    @Query(value = SQL_FIND_BY_USER_ID_AND_APPLICATION_ID
            , nativeQuery = true)
    List<UserPermissionEntity> findByUserIdAndApplicationId(@Param("userId") String userId
            , @Param("applicationId") String applicationId);
}
