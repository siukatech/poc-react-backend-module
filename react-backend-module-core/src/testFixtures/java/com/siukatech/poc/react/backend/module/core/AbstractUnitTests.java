package com.siukatech.poc.react.backend.module.core;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.siukatech.poc.react.backend.module.core.global.log.MemoryAppender;
import lombok.Getter;
import org.javatuples.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.context.TestPropertySource;

import java.util.List;


//@Slf4j
//@TestPropertySource(properties = {
//        "client-id: XXX"
//        , "client-secret: XXX"
//        , "client-realm: react-backend-realm"
////		, "oauth2-client-keycloak: http://localhost:38180"
//        , "oauth2-client-keycloak: XXX"
//        , "spring.profiles.active: dev"
//        , "logging.level.org.springframework.web: TRACE"
////        , "logging.level.com.siukatech.poc.react.backend.module.core: TRACE"
//        , "logging.level.com.siukatech.poc.react.backend.module.core: DEBUG"
//        , "logging.level.com.siukatech.poc.react.backend.app: DEBUG"
//})
@TestPropertySource(
//        properties = {
//        "client-id=XXX"
//        , "client-secret=XXX"
//        , "client-realm=react-backend-realm"
//        , "oauth2-client-keycloak=http://keycloak-host-name"
//        , "oauth2-client-redirect-uri=http://redirect-host-name/redirect"
//        , "spring.profiles.active=dev"
//        , "logging.level.org.springframework.web: TRACE"
//        , "logging.level.com.siukatech.poc.react.backend.module.core: DEBUG"
//}
        locations = {"classpath:abstract-unit-tests.properties"}
)
public abstract class AbstractUnitTests {

    public static final String USER_NAME_ATTRIBUTE = StandardClaimNames.PREFERRED_USERNAME;
    public static final String CLIENT_NAME = "keycloak";

    protected static final org.slf4j.Logger log = LoggerFactory.getLogger(AbstractUnitTests.class);

    @Getter
    private MemoryAppender memoryAppender;

    // The InMemoryClientRegistrationRepository is used to mock the OAuth2 configuration
    /**
     * @MockBean is defined for InMemoryClientRegistrationRepository.
     * Because we dont need the real clientRegistrationRepository.
     * The reason behind is that during the initialization of InMemoryClientRegistrationRepository in OAuth2ClientRegistrationRepositoryConfiguration,
     * Spring will make a real rest call to the registered issuer-uri in OAuth2ClientPropertiesMapper.asClientRegistration.
     * -> OAuth2ClientPropertiesMapper.getClientRegistration
     * -> OAuth2ClientPropertiesMapper.getBuilderFromIssuerIfPossible
     * -> ClientRegistrations.fromIssuerLocation
     * -> ClientRegistrations.oidc
     *
     * private static Supplier<ClientRegistration.Builder> oidc(URI issuer) {
     * 	// @formatter:off
     * 	URI uri = UriComponentsBuilder.fromUri(issuer)
     * 	    .replacePath(issuer.getPath() + OIDC_METADATA_PATH)
     *  	.build(Collections.emptyMap());
     *  // @formatter:on
     *  return () -> {
     *      RequestEntity<Void> request = RequestEntity.get(uri).build();
     * 		Map<String, Object> configuration = rest.exchange(request, typeReference).getBody();
     * 		OIDCProviderMetadata metadata = parse(configuration, OIDCProviderMetadata::parse);
     * 		ClientRegistration.Builder builder = withProviderConfiguration(metadata, issuer.toASCIIString())
     * 				.jwkSetUri(metadata.getJWKSetURI().toASCIIString());
     * 		if (metadata.getUserInfoEndpointURI() != null) {
     * 			builder.userInfoUri(metadata.getUserInfoEndpointURI().toASCIIString());
     *      }
     *		return builder;
     *   };
     * }
     *
     * As a see, an Exception will be thrown at this stage.
     *
     */
    @MockBean
//    @Autowired
    protected InMemoryClientRegistrationRepository clientRegistrationRepository;
    // @MockBean
    // protected ProblemDetailExtMapper problemDetailExtMapper;


    @BeforeAll
    public static void init() {
        final Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);
        rootLogger.setLevel(Level.DEBUG);

        log.debug("AbstractUnitTests.init............");

    }

    @AfterAll
    public static void terminate() {
        log.debug("AbstractUnitTests.terminate............");
    }

    public void initMemoryAppender(List<Pair<String, Level>> loggerInfoList) {
        if (memoryAppender == null) memoryAppender = new MemoryAppender();
        loggerInfoList.forEach(loggerInfo -> {
            Logger logger = (Logger) LoggerFactory.getLogger(loggerInfo.getValue0());
            logger.setLevel(loggerInfo.getValue1());
            logger.addAppender(memoryAppender);
        });
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        memoryAppender.start();
    }

}
