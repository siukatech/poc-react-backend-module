package com.siukatech.poc.react.backend.module.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.oauth2.client.OAuth2ClientExtProp;
import com.siukatech.poc.react.backend.module.core.util.EncryptionUtils;
import com.siukatech.poc.react.backend.module.user.model.LoginForm;
import com.siukatech.poc.react.backend.module.user.model.RefreshTokenForm;
import com.siukatech.poc.react.backend.module.user.model.TokenRes;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith({MockitoExtension.class
        , SpringExtension.class
})
@EnableConfigurationProperties
@ContextConfiguration(classes = {
//        RegistrationConfig.class, ProviderConfig.class
//        ,
        OAuth2ClientExtProp.class
        , AppCoreProp.class
})
@TestPropertySource({"classpath:abstract-oauth2-tests.properties"
        , "classpath:global/app-core-config-tests.properties"
})
public class AuthServiceTests extends AbstractUnitTests {

//    @Autowired
//    private RegistrationConfig registrationConfig;
//    @Autowired
//    private ProviderConfig providerConfig;

//    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
////    private Map<String, String> registrationMap;
//    private String registrationClientId;
////    private String clientSecret;
////    private String authorizationGrantType;
////    private String scope;
////    private String redirectUri;
////    @Value("spring.security.oauth2.client.provider.keycloak")
////    private Map<String, String> providerMap;

    /**
     * This is the OAuth2ClientExtProp initiated with @TestPropertySource
     * Use this to mock the oAuth2ClientExtProp in AuthService
     * Because the OAuth2ClientExtProp in AuthService always created first
     * And OAuth2ClientExtProp is final, so it cannot be updated after the creation
     */
    @Autowired
    private OAuth2ClientExtProp oAuth2ClientExtPropForTests;
    @Autowired
    private AppCoreProp appCorePropForTests;

    @InjectMocks
    private AuthService authService;

    /**
     * Cannot use @Mock, dont know why cannot mock this oAuth2ClientExtProp.
     * If the AuthService.oAuth2ClientExtProp changed to NOT final,
     * then using @Mock is ok
     * Properly is because object cannot be changed if marked as final
     * <p>
     * However, if AuthService.oAuth2ClientExtProp is final,
     * then only @Spy is ok with doReturn().when()
     */
//    @Mock
    @Spy
    private OAuth2ClientExtProp oAuth2ClientExtProp;

    /**
     * Cannot use @Mock, dont know why cannot mock exchange method ?_?
     */
//    @Mock
    @Spy
    private RestTemplate oauth2ClientRestTemplate;
    @Spy
    private ObjectMapper objectMapper;
    @Spy
    private AppCoreProp appCoreProp;


//    private
////    OAuth2ClientExtProp.Registration
//    Map<String, OAuth2ClientExtProp.Registration>
//    prepareRegistration(String clientName) {
//        OAuth2ClientExtProp.Registration registration = new OAuth2ClientExtProp.Registration();
//        registration.setClientId(registrationConfig.getClientId());
//        registration.setClientSecret(registrationConfig.getClientSecret());
//        registration.setAuthorizationGrantType(registrationConfig.getAuthorizationGrantType());
//        registration.setScope(Set.of(registrationConfig.getScope()));
//        registration.setRedirectUri(registrationConfig.getRedirectUri());
////        return registration;
//        return Map.of(clientName, registration);
//    }
//    private Map<String, OAuth2ClientExtProp.Provider> prepareProvider(String clientName) {
//        OAuth2ClientExtProp.Provider provider = new OAuth2ClientExtProp.Provider();
//        provider.setAuthorizationUri(providerConfig.getAuthorizationUri());
//        provider.setTokenUri(providerConfig.getTokenUri());
//        provider.setUserInfoUri(providerConfig.getUserInfoUri());
//        provider.setIssuerUri(providerConfig.getIssuerUri());
//        provider.setJwkSetUri(providerConfig.getJwkSetUri());
//        provider.setUserNameAttribute(providerConfig.getUserNameAttribute());
//        return Map.of(clientName, provider);
//    }

    private TokenRes prepareTokenRes() {
        TokenRes tokenRes = new TokenRes("accessToken"
                , "refreshToken", "expiresIn"
                , "refreshExpiresIn", "tokenType"
                , 1, "sessionState", "scope"
        );
        return tokenRes;
    }

    @BeforeEach
    public void setup() {
//        log.debug("setup - registrationConfig: [{}], providerConfig: [{}]"
//                        + ", registrationConfig.clientId: [{}]"
//                        + ", registrationConfig.scope: [{}]"
//                , this.registrationConfig, this.providerConfig
//                , this.registrationConfig.getClientId()
//                , this.registrationConfig.getScope()
//        );
//////        log.debug("setup - registrationMap: [{}], providerMap: [{}]"
//////                , this.registrationMap, this.providerMap);
////        log.debug("setup - registrationClientId: [{}]"
////                , this.registrationClientId);
        log.debug("setup - oAuth2ClientExtPropForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientExtPropForTests.getRegistration().size()
        );
    }

    private MyKeyDto prepareMyKeyDto_basic() throws NoSuchAlgorithmException {
        KeyPair keyPair = EncryptionUtils.generateRsaKeyPair();
        MyKeyDto myKeyDto = new MyKeyDto();
        myKeyDto.setUserId("app-user-01");
//        myKeyDto.setName("App User 01");
//        myKeyDto.setPublicKey("public-key");
//        myKeyDto.setPrivateKey("private-key");
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        myKeyDto.setPrivateKey(privateKeyBase64);
        myKeyDto.setPublicKey(publicKeyBase64);
        return myKeyDto;
    }

//    @Test
//    public void test_resolveMyKeyInfo_basic() throws NoSuchAlgorithmException {
//        log.debug("test_resolveMyKeyInfo_basic - appCorePropForTests.myUserInfoUrl: [{}]"
//                        + ", appCoreProp.myUserInfoUrl: [{}]"
//                        + ", appCoreProp.getMyKeyInfoUrl: [{}]"
//                        + ", appCoreProp.getMyKeyInfoUrl: [{}]"
//                , this.appCorePropForTests.getMyUserInfoUrl()
//                , this.appCoreProp.getMyUserInfoUrl()
//                , this.appCorePropForTests.getMyKeyInfoUrl()
//                , this.appCoreProp.getMyKeyInfoUrl()
//        );
//
//        // given
//        MyKeyDto myKeyDto = prepareMyKeyDto_basic();
//        String userId = myKeyDto.getUserId();
//        when(this.appCoreProp.getMyKeyInfoUrl())
//                .thenReturn(this.appCorePropForTests.getMyKeyInfoUrl());
////        when(this.oauth2ClientRestTemplate.exchange(anyString()
////                , eq(HttpMethod.POST), eq(HttpEntity.EMPTY), eq(MyKeyDto.class)))
////                .thenReturn(ResponseEntity.ok(prepareMyKeyDto_basic()));
//        doReturn(ResponseEntity.ok(myKeyDto))
//                .when(this.oauth2ClientRestTemplate).exchange(anyString(), eq(HttpMethod.POST)
//                        , ArgumentMatchers.any(HttpEntity.class), eq(MyKeyDto.class))
//        ;
//
//        // when
//        MyKeyDto myKeyRet = this.authService.resolveMyKeyInfo(userId);
//
//        // then
//        log.debug("test_resolveMyKeyInfo_basic - myKeyRet: [{}]", myKeyRet);
//        assertThat(myKeyRet).hasFieldOrProperty("privateKey")
//                .has(new Condition<>(u -> u.getPrivateKey().contains(myKeyDto.getPrivateKey())
//                        , "Has %s", "private-key"))
//        ;
//
//    }

    @Test
    public void test_getAuthCodeLoginUrl_basic() {
        // given
        String clientName = CLIENT_NAME;
        String codeChallenge = null;
////        when(this.oAuth2ClientExtProp.getRegistration())
////                .thenReturn(prepareRegistration(clientName));
////        when(this.oAuth2ClientExtProp.getProvider())
////                .thenReturn(prepareProvider(clientName));
//        doReturn(prepareRegistration(clientName))
//                .when(this.oAuth2ClientExtProp).getRegistration();
//        doReturn(prepareProvider(clientName))
//                .when(this.oAuth2ClientExtProp).getProvider();
        //
        // oAuth2ClientExtProp is created by auto bean creation
        // oAuth2ClientExtPropForTests is the bean having the values from test properties
        // so that the oAuth2ClientExtPropForTests is the source of mocking
        doReturn(this.oAuth2ClientExtPropForTests.getRegistration())
                .when(this.oAuth2ClientExtProp).getRegistration();
        doReturn(this.oAuth2ClientExtPropForTests.getProvider())
                .when(this.oAuth2ClientExtProp).getProvider();
        doReturn(this.oAuth2ClientExtPropForTests.getProviderExt())
                .when(this.oAuth2ClientExtProp).getProviderExt();
        log.debug("test_getAuthCodeLoginUrl_basic - oAuth2ClientExtPropForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientExtPropForTests.getRegistration().size()
        );

        // when
        String authCodeLoginUrl = this.authService.getAuthCodeLoginUrl(clientName, codeChallenge);

        // then
        assertThat(authCodeLoginUrl).contains("response_type");
    }

    @Test
    public void test_resolveAuthCodeTokenRes_basic() {
        // given
        String clientName = CLIENT_NAME;
        String code = "this-is-an-unit-test-code";
        String codeVerifier = null;
//        doReturn(this.prepareRegistration(clientName))
//                .when(this.oAuth2ClientExtProp).getRegistration();
//        doReturn(this.prepareProvider(clientName))
//                .when(this.oAuth2ClientExtProp).getProvider();
////        when(this.oauth2ClientRestTemplate.exchange(anyString()
////                , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class)))
////                .thenReturn(ResponseEntity.ok(prepareTokenRes()));
        doReturn(this.oAuth2ClientExtPropForTests.getRegistration())
                .when(this.oAuth2ClientExtProp).getRegistration();
        doReturn(this.oAuth2ClientExtPropForTests.getProvider())
                .when(this.oAuth2ClientExtProp).getProvider();
        doReturn(this.oAuth2ClientExtPropForTests.getProviderExt())
                .when(this.oAuth2ClientExtProp).getProviderExt();
        doReturn(ResponseEntity.ok(prepareTokenRes()))
                .when(this.oauth2ClientRestTemplate).exchange(anyString()
                        , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class))
        ;

        // when
        TokenRes tokenRes = this.authService.resolveAuthCodeTokenRes(clientName, code, codeVerifier);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value: %s", List.of("accessToken")))
        ;
    }

    @Test
    public void test_resolvePasswordTokenRes_basic() {
        // given
        String clientName = CLIENT_NAME;
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("username");
        loginForm.setPassword("password");
        doReturn(this.oAuth2ClientExtPropForTests.getRegistration())
                .when(this.oAuth2ClientExtProp).getRegistration();
        doReturn(this.oAuth2ClientExtPropForTests.getProvider())
                .when(this.oAuth2ClientExtProp).getProvider();
        doReturn(this.oAuth2ClientExtPropForTests.getProviderExt())
                .when(this.oAuth2ClientExtProp).getProviderExt();
        doReturn(ResponseEntity.ok(prepareTokenRes()))
                .when(this.oauth2ClientRestTemplate).exchange(anyString()
                        , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class))
        ;

        // when
        TokenRes tokenRes = this.authService.resolvePasswordTokenRes(clientName, loginForm);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value: %s", List.of("accessToken")))
        ;
    }

    @Test
    public void test_resolveRefreshTokenTokenRes_basic() {
        // given
        String clientName = CLIENT_NAME;
        RefreshTokenForm refreshTokenForm = new RefreshTokenForm();
        refreshTokenForm.setAccessToken("access_token");
        refreshTokenForm.setRefreshToken("refresh_token");
        doReturn(this.oAuth2ClientExtPropForTests.getRegistration())
                .when(this.oAuth2ClientExtProp).getRegistration();
        doReturn(this.oAuth2ClientExtPropForTests.getProvider())
                .when(this.oAuth2ClientExtProp).getProvider();
        doReturn(this.oAuth2ClientExtPropForTests.getProviderExt())
                .when(this.oAuth2ClientExtProp).getProviderExt();
        doReturn(ResponseEntity.ok(prepareTokenRes()))
                .when(this.oauth2ClientRestTemplate).exchange(anyString()
                        , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class))
        ;

        // when
        TokenRes tokenRes = this.authService.resolveRefreshTokenTokenRes(clientName, refreshTokenForm);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value: %s", List.of("accessToken")))
        ;
    }

    @Test
    public void test_doAuthLogout_basic() throws URISyntaxException {
//        public HttpStatusCode doAuthLogout(String logoutApi) {
        // given
        String logoutApi = "http://localhost:8080/logout";
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).build();
//        when(oauth2ClientRestTemplate.getForEntity(any(URI.class), eq(Map.class))).thenReturn(responseEntity);
        doReturn(responseEntity).when(this.oauth2ClientRestTemplate).getForEntity(any(URI.class), eq(Map.class));

        // when
        HttpStatusCode httpStatusCode = this.authService.doAuthLogout(logoutApi);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

}
