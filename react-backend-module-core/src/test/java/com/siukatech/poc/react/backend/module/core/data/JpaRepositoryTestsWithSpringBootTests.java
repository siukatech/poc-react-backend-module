package com.siukatech.poc.react.backend.module.core.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.HandlerExceptionResolver;


/**
 * This is not working at this moment
 */
//@Slf4j
//@Deprecated
@SpringBootTest
@OverrideAutoConfiguration(enabled = false)
@AutoConfigureDataJpa
@AutoConfigureWebClient(registerRestTemplate = true)
//@AutoConfigureTestDatabase
////@TestPropertySource({
////		"classpath:application.yml"
////})
@TestPropertySource({"classpath:abstract-unit-tests.properties"
        , "classpath:abstract-jpa-tests.properties"
        , "classpath:abstract-oauth2-tests.properties"})
public class JpaRepositoryTestsWithSpringBootTests {

    protected static final Logger log = LoggerFactory.getLogger(JpaRepositoryTestsWithSpringBootTests.class);

//    @MockBean
    @SpyBean
//    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;
//    @MockBean
//    private UserRepository userRepository;
//    @MockBean
//    private UserPermissionRepository userPermissionRepository;
//    @MockBean
//    private UserViewRepository userViewRepository;
    // @MockBean
    // private ProblemDetailExtMapper problemDetailExtMapper;
    @MockBean
    private HandlerExceptionResolver handlerExceptionResolver;

    // This is not working, start
//    @MockBean
//    private RestTemplateBuilder restTemplateBuilder;
    // This is not working, end

    @Test
    void contextLoads() {
        Assertions.assertNotNull(log);
        log.debug("contextLoads - testing logging");
    }

}
