package com.siukatech.poc.react.backend.module.user.repository;

import com.siukatech.poc.react.backend.module.core.AbstractJpaTests;
import com.siukatech.poc.react.backend.module.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@DataJpaTest
//        (properties = {
////                "logging.level.org.springframework.web=TRACE"
////                , "logging.level.com.siukatech.poc.react.backend.core=TRACE"
//                "spring.jpa.show-sql: true"
//                , "spring.jpa.properties.hibernate.format_sql: true"
//                , "logging.level.org.springframework.data: TRACE"
//        })
////@TestPropertySource("classpath:application.yml")
@TestPropertySource(properties = {
        "logging.level.org.hibernate.SQL=DEBUG"
        , "logging.level.org.hibernate.orm.jdbc.bind=TRACE"
        , "logging.level.com.siukatech.poc.react.backend.core.data.listener=INFO"
})
public class UserRepositoryTests extends AbstractJpaTests {

    @Autowired
    public UserRepository userRepository;

    private UserEntity prepareUserEntity_basic() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("app-user-01");
        userEntity.setName("App-User-01");
////        userEntity.setId(1L);
//        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setVersionNo(1L);
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        userEntity.setCreatedBy("admin");
        userEntity.setLastModifiedBy("admin");
        userEntity.setCreatedDatetime(LocalDateTime.now());
        userEntity.setLastModifiedDatetime(LocalDateTime.now());
        return userEntity;
    }

    /**
     * Reference:
     * https://stackoverflow.com/a/58623532
     */
    @BeforeEach
    public void setup(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        String methodName = method.getName();
        Set<String> tags = testInfo.getTags();
        log.debug("setup - testInfo: [" + testInfo
                + "], methodName: [" + methodName
                + "], tags: [" + tags
                + "]");
        UserEntity userEntity = null;
//        switch (methodName) {
//            case "findByUserId_basic":
//                userEntity = this.prepareUserEntity_basic();
//            case "updateUser_version_updated":
//                userEntity = this.prepareUserEntity_basic();
//            case "updateUser_version_not_match":
//                userEntity = this.prepareUserEntity_basic();
//        }
        if (tags.contains("data_load")) {
            userEntity = this.prepareUserEntity_basic();
        }
        if (userEntity != null) {
            this.userRepository.save(userEntity);
        }
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        String methodName = method.getName();
        Set<String> tags = testInfo.getTags();
        log.debug("teardown - testInfo: [" + testInfo
                + "], methodName: [" + methodName
                + "], tags: [" + tags
                + "]");
//        List<String> methodNameList = List.of(
//                "findByUserId_basic"
//                , "updateUser_version_updated"
//                , "updateUser_version_not_match_v1"
//                , "updateUser_version_not_match_v2"
//        );
//        if (methodNameList.contains(methodName)) {
        if (tags.contains("data_load")) {
            Optional<UserEntity> userEntityOptional = this.userRepository.findByUserId("app-user-01");
            userEntityOptional.ifPresent(userEntity -> this.userRepository.delete(userEntity));
        }
    }

    @Test
    @Tags(value = {
            @Tag("basic")
            , @Tag("data_load")
    })
    public void test_findByUserId_basic() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        Assertions.assertEquals(userEntity.getUserId(), "app-user-01");
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void test_findByUserId_complex() {
        UserEntity userEntity = userRepository.findByUserId("app-user-02")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        Assertions.assertEquals(userEntity.getUserId(), "app-user-02");
    }

    @Test
    @Tags(value = {
            @Tag("version_updated")
            , @Tag("data_load")
    })
    public void test_updateUser_version_updated() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        userEntity.setName("App-User-01-version-updated");
        this.userRepository.save(userEntity);
        UserEntity userEntityAfterUpdate = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        log.debug("test_updateUser_version_updated - userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "]");
        Assertions.assertEquals(userEntity.getVersionNo(), 2L);
        Assertions.assertEquals(userEntityAfterUpdate.getVersionNo(), 2L);
        Assertions.assertEquals(userEntity.getUserId(), "app-user-01");
    }

    @Test
    @Tags(value = {
            @Tag("version_not_match")
            , @Tag("data_load")
    })
    public void test_updateUser_version_not_match_v1() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        userEntity.setName("App-User-01-version_updated");
        this.userRepository.save(userEntity);

        UserEntity userEntityClone = new UserEntity();
        userEntityClone.setId(userEntity.getId());
        userEntityClone.setUserId(userEntity.getUserId());
        userEntityClone.setName(userEntity.getName());
        userEntityClone.setPublicKey(userEntity.getPublicKey());
        userEntityClone.setPrivateKey(userEntity.getPrivateKey());
        userEntityClone.setName("App-User-01-version_not_match");
        userEntityClone.setVersionNo(1L);

        UserEntity userEntityAfterUpdate = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        log.debug("test_updateUser_version_not_match_v1 - 1 - userEntity.getId: [" + userEntity.getId()
                + "], userEntity.getName: [" + userEntity.getName()
                + "], userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getId: [" + userEntityAfterUpdate.getId()
                + "], userEntityAfterUpdate.getName: [" + userEntityAfterUpdate.getName()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "], userEntityClone.getId: [" + userEntityClone.getId()
                + "], userEntityClone.getName: [" + userEntityClone.getName()
                + "], userEntityClone.getVersionNo: [" + userEntityClone.getVersionNo()
                + "]");

        Exception objectOptimisticLockingFailureException = Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            this.userRepository.save(userEntityClone);
        });
        log.debug("test_updateUser_version_not_match_v1 - 2 - userEntity.getId: [" + userEntity.getId()
                + "], userEntity.getName: [" + userEntity.getName()
                + "], userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getId: [" + userEntityAfterUpdate.getId()
                + "], userEntityAfterUpdate.getName: [" + userEntityAfterUpdate.getName()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "], userEntityClone.getId: [" + userEntityClone.getId()
                + "], userEntityClone.getName: [" + userEntityClone.getName()
                + "], userEntityClone.getVersionNo: [" + userEntityClone.getVersionNo()
                + "], objectOptimisticLockingFailureException.getClass.getName: [" + (objectOptimisticLockingFailureException == null ? "NULL" : objectOptimisticLockingFailureException.getClass().getName())
                + "], objectOptimisticLockingFailureException.getMessage: [" + (objectOptimisticLockingFailureException == null ? "NULL" : objectOptimisticLockingFailureException.getMessage())
                + "]");
        log.error(objectOptimisticLockingFailureException.getMessage(), objectOptimisticLockingFailureException);

        Assertions.assertNotNull(objectOptimisticLockingFailureException);
        Assertions.assertTrue(objectOptimisticLockingFailureException.getMessage().contains("Row was updated or deleted by another transaction"));
    }

    /**
     * This test case is invalid
     * Using the same entity to perform the update would not trigger the ObjectOptimisticLockingFailureException
     */
//    @Test
    @Tags(value = {
            @Tag("version_not_match")
            , @Tag("data_load")
    })
    public void test_updateUser_version_not_match_v2() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        userEntity.setName("App-User-01-version_updated");
        this.userRepository.save(userEntity);

//        UserEntity userEntityClone = new UserEntity();
//        userEntityClone.setId(userEntity.getId());
//        userEntityClone.setUserId(userEntity.getUserId());
//        userEntityClone.setName(userEntity.getName());
//        userEntityClone.setPublicKey(userEntity.getPublicKey());
//        userEntityClone.setPrivateKey(userEntity.getPrivateKey());
//        userEntityClone.setName("App-User-01-version_not_match");
//        userEntityClone.setVersionNo(1L);

        UserEntity userEntityAfterUpdate = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]"));
        log.debug("test_updateUser_version_not_match_v2 - 1 - userEntity.getId: [" + userEntity.getId()
                + "], userEntity.getName: [" + userEntity.getName()
                + "], userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getId: [" + userEntityAfterUpdate.getId()
                + "], userEntityAfterUpdate.getName: [" + userEntityAfterUpdate.getName()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
//                + "], userEntityClone.getId: [" + userEntityClone.getId()
//                + "], userEntityClone.getName: [" + userEntityClone.getName()
//                + "], userEntityClone.getVersionNo: [" + userEntityClone.getVersionNo()
                + "]");


        userEntity.setName("App-User-01-version_not_match");
        userEntity.setVersionNo(1L);

        Exception objectOptimisticLockingFailureException = Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
//            this.userRepository.save(userEntityClone);
            this.userRepository.save(userEntity);
        });
        log.debug("test_updateUser_version_not_match_v2 - 2 - userEntity.getId: [" + userEntity.getId()
                + "], userEntity.getName: [" + userEntity.getName()
                + "], userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getId: [" + userEntityAfterUpdate.getId()
                + "], userEntityAfterUpdate.getName: [" + userEntityAfterUpdate.getName()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
//                + "], userEntityClone.getId: [" + userEntityClone.getId()
//                + "], userEntityClone.getName: [" + userEntityClone.getName()
//                + "], userEntityClone.getVersionNo: [" + userEntityClone.getVersionNo()
                + "], objectOptimisticLockingFailureException.getClass.getName: [" + (objectOptimisticLockingFailureException == null ? "NULL" : objectOptimisticLockingFailureException.getClass().getName())
                + "], objectOptimisticLockingFailureException.getMessage: [" + (objectOptimisticLockingFailureException == null ? "NULL" : objectOptimisticLockingFailureException.getMessage())
                + "]");

        Assertions.assertNotNull(objectOptimisticLockingFailureException);
        Assertions.assertTrue(objectOptimisticLockingFailureException.getMessage().contains("Row was updated or deleted by another transaction"));
    }

}
