package com.siukatech.poc.react.backend.module.user.repository;

import com.siukatech.poc.react.backend.module.core.AbstractJpaTests;
import com.siukatech.poc.react.backend.module.user.entity.AppResourceEntity;
import com.siukatech.poc.react.backend.module.user.entity.ApplicationEntity;
import com.siukatech.poc.react.backend.module.user.entity.UserPermissionJpaEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@DataJpaTest
@TestPropertySource(properties = {
        "logging.level.org.hibernate.SQL=DEBUG"
        , "logging.level.org.hibernate.orm.jdbc.bind=TRACE"
        , "logging.level.com.siukatech.poc.react.backend.module.core.data.listener=INFO"
        , "logging.level.org.springframework.jdbc.datasource.init=INFO"
//        // Reference:
//        // https://spring.io/blog/2024/08/23/structured-logging-in-spring-boot-3-4
//        , "logging.structured.format.console=ecs"
})
public class ApplicationRepositoryTests extends AbstractJpaTests {

    @Autowired
    public ApplicationRepository applicationRepository;

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
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findWithGraphAttrPathByApplicationId_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithGraphAttrPathByApplicationId("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getApplicationId(), "frontend-app");

        log.debug("test_findWithGraphAttrPathByApplicationId_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserPermissionJpaEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserPermissionJpaEntities().size()
        );
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findWithJpqlAppResourcesByApplicationId_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithJpqlAppResourcesByApplicationId("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getApplicationId(), "frontend-app");

        log.debug("test_findWithJpqlAppResourcesByApplicationId_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserPermissionJpaEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserPermissionJpaEntities().size()
        );
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findWithJpqlUserPermissionJpaEntitiesByApplicationId_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithJpqlUserPermissionJpaEntitiesByApplicationId("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getApplicationId(), "frontend-app");

        log.debug("test_findWithJpqlUserPermissionJpaEntitiesByApplicationId_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserPermissionJpaEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserPermissionJpaEntities().size()
        );
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findWithAppResourceEntitiesByApplicationId_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithAppResourceEntitiesByApplicationId("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getApplicationId(), "frontend-app");

        log.debug("test_findWithAppResourceEntitiesByApplicationId_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserPermissionJpaEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserPermissionJpaEntities().size()
        );
//        if (applicationEntity.getAppResourceEntities() != null) {
//            applicationEntity.getAppResourceEntities().forEach(appResourceEntity -> {
//                log.debug("findWithAppResourceEntitiesByApplicationId_basic - appResourceEntity.getId: [{}]"
//                        , appResourceEntity.getId());
//            });
//        }
        String appResourceEntityIdsStr = StringUtils.join(
                applicationEntity.getAppResourceEntities().stream()
                        .map(AppResourceEntity::getId).toList()
        );
        log.debug("test_findWithAppResourceEntitiesByApplicationId_basic - appResourceEntityIdsStr: [{}]"
                , appResourceEntityIdsStr);
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findWithUserPermissionJpaEntitiesByApplicationId_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithUserPermissionJpaEntitiesByApplicationId("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getApplicationId(), "frontend-app");

        log.debug("test_findWithUserPermissionJpaEntitiesByApplicationId_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserPermissionJpaEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserPermissionJpaEntities().size()
        );
        if (applicationEntity.getUserPermissionJpaEntities() != null) {
//            applicationEntity.getUserPermissionJpaEntities().forEach(userPermissionJpaEntity -> {
//                log.debug("findWithUserPermissionJpaEntitiesByApplicationId_basic - userPermissionJpaEntity.getId: [{}]"
//                        , userPermissionJpaEntity.getId());
//            });
            String userPermissionJpaEntityIdsStr = StringUtils.join(
                    applicationEntity.getUserPermissionJpaEntities().stream()
                            .map(UserPermissionJpaEntity::getId).toList()
            );
            log.debug("test_findWithUserPermissionJpaEntitiesByApplicationId_basic - userPermissionJpaEntityIdsStr: [{}]"
                    , userPermissionJpaEntityIdsStr);
        }
    }


}
