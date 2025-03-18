package com.siukatech.poc.react.backend.module.user.repository;

import com.siukatech.poc.react.backend.module.core.AbstractJpaTests;
import com.siukatech.poc.react.backend.module.user.entity.ApplicationEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionJpaEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
// Reference:
// https://stackoverflow.com/a/1713464
// https://stackoverflow.com/a/74862954
@TestPropertySource(properties = {
        "logging.level.org.hibernate.SQL=DEBUG"
//        , "logging.level.org.hibernate.type=TRACE"
        , "logging.level.org.hibernate.orm.jdbc.bind=TRACE"
        , "logging.level.com.siukatech.poc.react.backend.module.core.data.listener=INFO"
//        , "spring.h2.console.enabled=true"
})
public class UserPermissionJpaRepositoryTests extends AbstractJpaTests {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ApplicationRepository applicationRepository;

    @Autowired
    public UserPermissionJpaRepository userPermissionJpaRepository;

    @BeforeEach
    public void setup(TestInfo testInfo) {
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
    }


    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
//            , "/scripts/users/11-data-01-find-by-login-id.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            ,
            "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findByUserRoleEntityUserRoleUserEntitiesUserEntityUserIdAndApplicationEntityApplicationId_basic() {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId("app-user-02");
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findWithAppResourceEntitiesByApplicationId("frontend-app");
        log.debug("test_findByUserRoleEntityUserRoleUserEntitiesUserEntityUserIdAndApplicationEntityApplicationId_basic - userEntityOptional.get: [{}], applicationEntityOptional: [{}]"
                , userEntityOptional.get(), applicationEntityOptional.get());
        List<UserPermissionJpaEntity> userPermissionJpaEntityList_UserIdJpa = userPermissionJpaRepository
                .findByUserRoleEntityUserRoleUserEntitiesUserEntityUserId("app-user-02");
        List<UserPermissionJpaEntity> userPermissionEntityList_ApplicationJpaEntityId = userPermissionJpaRepository
                .findByApplicationEntityApplicationId("frontend-app");
        log.debug("test_findByUserRoleEntityUserRoleUserEntitiesUserEntityUserIdAndApplicationEntityApplicationId_basic - "
                        + "userPermissionJpaEntityList_UserId.size: [{}], userPermissionJpaEntityList_ApplicationEntityId.size: [{}]"
                , userPermissionJpaEntityList_UserIdJpa.size(), userPermissionEntityList_ApplicationJpaEntityId.size());
        List<UserPermissionJpaEntity> userPermissionJpaEntityList = userPermissionJpaRepository
                .findByUserRoleEntityUserRoleUserEntitiesUserEntityUserIdAndApplicationEntityApplicationId("app-user-02", "frontend-app");

        log.debug("test_findByUserRoleEntityUserRoleUserEntitiesUserEntityUserIdAndApplicationEntityApplicationId_basic - userPermissionEntityList.size: [" + userPermissionJpaEntityList.size()
                + "], userPermissionEntityList: [" + userPermissionJpaEntityList
                + "]");
//        Assertions.assertEquals(userPermissionJpaEntityList.get(0).getUserRoleEntity().getUserRoleUserEntities().get(0).getUserEntity().getUserId(), "app-user-02");
        Assertions.assertEquals(userPermissionJpaEntityList.get(0).getApplicationEntity().getApplicationId(), "frontend-app");
    }


}
