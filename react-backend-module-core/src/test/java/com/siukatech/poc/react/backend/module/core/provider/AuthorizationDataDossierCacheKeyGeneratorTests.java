package com.siukatech.poc.react.backend.module.core.provider;

import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataDossierCacheKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

//import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {AuthorizationDataDossierCacheKeyGenerator.class})
public class AuthorizationDataDossierCacheKeyGeneratorTests {

    @Autowired
    AuthorizationDataDossierCacheKeyGenerator authorizationDataDossierCacheKeyGenerator;

    @Test
    void test_generate_basic() {
        // given
        Object target = new Object();
        Method method = target.getClass().getMethods()[0];
        Object[] params = new String[] {"userId1"};

        // when
        Object key = authorizationDataDossierCacheKeyGenerator.generate(target, method, params);

        // then
        Assertions.assertTrue(key.toString().contains("userId1"));

    }

    @Test
    void test_generate_invalid_params() {
        // given
        Object target = new Object();
        Method method = target.getClass().getMethods()[0];
        Object[] params = new String[] {};

        // when
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Object key = authorizationDataDossierCacheKeyGenerator.generate(target, method, params);
        });

        // then
        Assertions.assertTrue(exception.getMessage().contains("userId"));

    }

}
