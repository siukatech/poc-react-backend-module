package com.siukatech.poc.react.backend.module.user.caching;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

//import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {UserPermissionCacheKeyGenerator.class})
public class UserPermissionCacheKeyGeneratorTests {

    @Autowired
    UserPermissionCacheKeyGenerator userPermissionCacheKeyGenerator;

    @Test
    void test_generate_basic() {
        // given
        Object target = new Object();
        Method method = target.getClass().getMethods()[0];
        Object[] params = new String[] {"targetUserId1", "applicationId1"};

        // when
        Object key = userPermissionCacheKeyGenerator.generate(target, method, params);

        // then
        Assertions.assertTrue(key.toString().contains("targetUserId1"));
        Assertions.assertTrue(key.toString().contains("applicationId1"));
    }

    @Test
    void test_generate_invalid_params() {
        // given
        Object target = new Object();
        Method method = target.getClass().getMethods()[0];
        Object[] params = new String[] {};

        // when
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Object key = userPermissionCacheKeyGenerator.generate(target, method, params);
        });

        // then
        Assertions.assertTrue(exception.getMessage().contains("targetUserId"));
        Assertions.assertTrue(exception.getMessage().contains("applicationId"));
    }

}
