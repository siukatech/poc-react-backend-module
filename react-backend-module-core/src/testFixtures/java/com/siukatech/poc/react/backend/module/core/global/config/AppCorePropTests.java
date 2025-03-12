package com.siukatech.poc.react.backend.module.core.global.config;

import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reference:
 * https://stackoverflow.com/a/26840503
 *
 * @Test only supports ".properties" files only.
 *
 * @ConfigDataApplicationContextInitializer.class is the replacement of @ConfigFileApplicationContextInitializer.class.
 * @ConfigDataApplicationContextInitializer.class activates regular Spring Boot loading sequence.
 * As a result, yml in "main/resources" will be loaded instead of "test/resources"
 *
 * NOT recommended.
 *
 */
@Slf4j
@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties(value = AppCoreProp.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = AppCoreProp.class)
//@ContextConfiguration(classes = AppCoreProp.class, initializers = ConfigDataApplicationContextInitializer.class)
//@TestPropertySource("classpath:global/app-core-config-tests.yml")
@TestPropertySource("classpath:global/app-core-config-tests.properties")
//@TestPropertySource(properties = {"spring.config.location = classpath:global/app-core-config-tests.properties"})
//@TestPropertySource(locations= {"classpath:global/app-core-config-tests.yml"})
public class AppCorePropTests extends AbstractUnitTests {

    @Autowired
    private AppCoreProp appCoreProp;

    @BeforeAll
    public static void init() {
//        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);

        AbstractUnitTests.init();
    }

    @Test
    public void test_appCoreProp_basic() {
        log.debug("test_appCoreProp_basic - hostName: [{}]"
                        + ", myUserInfo: [{}]"
                        + ", myKeyInfo: [{}]"
                        + ", myPermissionInfo: [{}]"
                        + ", myUserDossier: [{}]"
                , appCoreProp.getHostName()
                , appCoreProp.getApi().getMyUserInfo()
                , appCoreProp.getApi().getMyKeyInfo()
                , appCoreProp.getApi().getMyPermissionInfo()
                , appCoreProp.getApi().getMyUserDossier()
        );
        assertThat(appCoreProp.getHostName()).isEqualTo("http://localhost:28080");
        assertThat(appCoreProp.getApi().getMyUserInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(appCoreProp.getApi().getMyUserInfo()).contains("/my/user-info");
//        assertThat(appCoreProp.getApp().getHostName()).isEqualTo("http://localhost:28080");
//        assertThat(appCoreProp.getApi().getMyUserInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
//        assertThat(appCoreProp.getApi().getMyUserInfo()).contains("/my/user-info");
        assertThat(appCoreProp.getApi().getMyKeyInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(appCoreProp.getApi().getMyKeyInfo()).contains("/my/key-info");
        assertThat(appCoreProp.getApi().getMyPermissionInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(appCoreProp.getApi().getMyPermissionInfo()).contains("/my/permission-info");
        assertThat(appCoreProp.getApi().getMyUserDossier()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(appCoreProp.getApi().getMyUserDossier()).contains("/my/user-dossier");
    }

    @Test
    public void test_getMyUserInfoUrl_basic() {
        // given

        // when
        String url = this.appCoreProp.getMyUserInfoUrl();
        log.debug("test_getMyUserInfoUrl_basic - url: [{}]", url);

        // then
        assertThat(url).contains("/my/user-info");
    }

    @Test
    public void test_getMyKeyInfoUrl_basic() {
        // given

        // when
        String url = this.appCoreProp.getMyKeyInfoUrl();
        log.debug("test_getMyKeyInfoUrl_basic - url: [{}]", url);

        // then
        assertThat(url).contains("/my/key-info");
    }

    @Test
    public void test_getMyPermissionInfoUrl_basic() {
        // given

        // when
        String url = this.appCoreProp.getMyPermissionInfoUrl();
        log.debug("test_getMyPermissionInfoUrl_basic - url: [{}]", url);

        // then
        assertThat(url).contains("/my/permission-info");
    }

    @Test
    public void test_getMyUserDossierUrl_basic() {
        // given

        // when
        String url = this.appCoreProp.getMyUserDossierUrl();
        log.debug("test_getMyUserDossierUrl_basic - url: [{}]", url);

        // then
        assertThat(url).contains("/my/user-dossier");
    }

}
