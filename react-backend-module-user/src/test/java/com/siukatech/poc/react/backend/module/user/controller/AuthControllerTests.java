package com.siukatech.poc.react.backend.module.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.AbstractWebTests;
import com.siukatech.poc.react.backend.module.user.controller.AuthController;
import com.siukatech.poc.react.backend.module.user.form.auth.LoginForm;
import com.siukatech.poc.react.backend.module.user.form.auth.RefreshTokenForm;
import com.siukatech.poc.react.backend.module.user.form.auth.TokenRes;
import com.siukatech.poc.react.backend.module.user.service.AuthService;
import com.siukatech.poc.react.backend.module.user.service.UserService;
import com.siukatech.poc.react.backend.module.core.util.EncryptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
//        properties = {
//        "spring.security.oauth2.client.registration.keycloak.client-id: ${client-id}"
//        , "spring.security.oauth2.client.registration.keycloak.client-secret: ${client-secret}"
//        , "spring.security.oauth2.client.registration.keycloak.authorization-grant-type: authorization_code"
//        , "spring.security.oauth2.client.registration.keycloak.scope: openid"
//        , "spring.security.oauth2.client.registration.keycloak.redirect-uri: ${oauth2-client-redirect-uri}"
//        //
//        , "spring.security.oauth2.client.provider.keycloak.authorization-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/auth"
//        , "spring.security.oauth2.client.provider.keycloak.token-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/token"
//        , "spring.security.oauth2.client.provider.keycloak.user-info-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/userinfo"
//        , "spring.security.oauth2.client.provider.keycloak.issuer-uri: ${oauth2-client-keycloak}/realms/${client-realm}"
//        , "spring.security.oauth2.client.provider.keycloak.jwk-set-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/certs"
//        , "spring.security.oauth2.client.provider.keycloak.user-name-attribute: preferred_username"
//}
        locations = {"classpath:abstract-oauth2-tests.properties"}
)
public class AuthControllerTests extends AbstractWebTests {

    @Autowired
    private ObjectMapper objectMapperForTests;

    /**
     * Provided by WebMvcTest, therefore we can use @Autowired.
     */
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    // define this for keycloakJwtAuthenticationConverter initialization
    @MockBean
    private UserService userService;

    @MockBean
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    @MockBean
    private OAuth2ClientProperties oAuth2ClientProperties;

//    /**
//     * @Autowired is defined because the OAuth2ClientProperties will be created based on the properties.
//     * Those oauth2 properties are provided by @TestPropertiesSource
//     */
////    @MockBean
////    @Autowired
//    @SpyBean
//    private OAuth2ClientProperties oAuth2ClientProperties;
//
//    /**
//     * @MockBean is defined because we dont need real rest call in our test.
//     * Mock the oauth2ClientRestTemplate.exchange will be fine.
//     */
//    @MockBean
//    private RestTemplate oauth2ClientRestTemplate;
//
//    /**
//     * ~~@SpyBean is defined because the objectMapper has been used in 'auth' and 'token' methods.~~
//     * ~~There are some object type conversions happened in above-mentioned methods.~~
//     * ~~So we need a semi-real bean here for our action.~~
//     * @Autowired is defined because we need the real objectMapper to perform object conversion.
//     */
////    @SpyBean
//    @Autowired
//    private ObjectMapper objectMapper;

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
//    @MockBean
////    @Autowired
//    private InMemoryClientRegistrationRepository clientRegistrationRepository;

    private final String CLIENT_NAME_KEYCLOAK = "keycloak";


    private TokenRes prepareTokenRes() {
        TokenRes tokenRes = new TokenRes("accessToken"
                , "refreshToken", "expiresIn"
                , "refreshExpiresIn", "tokenType"
                , 1, "sessionState", "scope"
//                , null
        );
        return tokenRes;
    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
//        OAuth2ClientProperties.Registration registration = new OAuth2ClientProperties.Registration();
//        registration.setClientId("");
//        registration.setAuthorizationGrantType("");
//        registration.setProvider("");
//        registration.setClientName("");
//        registration.setScope(Set.of(""));
//        registration.setClientAuthenticationMethod("");
//        registration.setClientSecret("");
//        registration.setRedirectUri("");
//        OAuth2ClientProperties.Provider provider = new OAuth2ClientProperties.Provider();
//        provider.setAuthorizationUri("");
//        provider.setIssuerUri("");
//        provider.setJwkSetUri("");
//        provider.setTokenUri("");
//        provider.setUserInfoUri("");
//        provider.setUserInfoAuthenticationMethod("");
//        provider.setUserNameAttribute("");
//        oAuth2ClientProperties = new OAuth2ClientProperties();
//        oAuth2ClientProperties.getRegistration().put("keycloak", registration);
//        oAuth2ClientProperties.getProvider().put("keycloak", provider);
    }

    @Test
    public void test_doAuthCodeLogin_basic() throws Exception {
        // given
        String clientName = CLIENT_NAME_KEYCLOAK;
        String codeChallenge = null;
        String codeVerifier = EncryptionUtils.generateCodeVerifier();
        codeChallenge = EncryptionUtils.generateCodeChallenge(codeVerifier);

                // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v1/public" + "/auth/login/{clientName}"
                        , clientName)
                .queryParam("codeChallenge", codeChallenge)
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn()
                ;

    }

    @Test
    public void test_doAuthCodeToken_basic() throws Exception {
        // given
        String clientName = CLIENT_NAME_KEYCLOAK;
        String code = "this-is-an-unit-test-code";
        String codeVerifier = null;
        codeVerifier = EncryptionUtils.generateCodeVerifier();
//        when(oauth2ClientRestTemplate.exchange(anyString()
//                , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class)))
//                .thenReturn(ResponseEntity.ok(prepareTokenRes()));
        when(authService.resolveAuthCodeTokenRes(clientName, code, codeVerifier))
                .thenReturn(prepareTokenRes());

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v1/public" + "/auth/token/{clientName}/{code}"
                        , clientName, code)
                .param("codeVerifier", codeVerifier)
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("accessToken")))
                .andReturn()
                ;
    }

    @Test
    public void test_doPasswordLogin_basic() throws Exception {
        // given
        String clientName = CLIENT_NAME_KEYCLOAK;
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("username");
        loginForm.setPassword("password");
        String loginFormStr = this.objectMapperForTests.writeValueAsString(loginForm);
//        when(oauth2ClientRestTemplate.exchange(anyString()
//                , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class)))
//                .thenReturn(ResponseEntity.ok(prepareTokenRes()));
        when(authService.resolvePasswordTokenRes(clientName, loginForm)).thenReturn(prepareTokenRes());

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v1/public" + "/auth/login/{clientName}", clientName)
                .content(loginFormStr)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("accessToken")))
                .andReturn()
                ;
    }

    @Test
    public void test_doAuthTokenRefresh_basic() throws Exception {
        // given
        String clientName = CLIENT_NAME_KEYCLOAK;
        RefreshTokenForm refreshTokenForm = new RefreshTokenForm();
        refreshTokenForm.setAccessToken("app-user-01");
        refreshTokenForm.setRefreshToken("keycloak");
        String refreshTokenFormStr = this.objectMapperForTests.writeValueAsString(refreshTokenForm);
//        when(oauth2ClientRestTemplate.exchange(anyString()
//                , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class)))
//                .thenReturn(ResponseEntity.ok(prepareTokenRes()));
        when(authService.resolveRefreshTokenTokenRes(clientName, refreshTokenForm)).thenReturn(prepareTokenRes());

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v1/public" + "/auth/refresh-token/{clientName}", clientName)
                .content(refreshTokenFormStr)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("accessToken")))
                .andReturn()
                ;
    }

    @Test
    public void test_doAuthLogout_basic() throws Exception {
        // given
        when(authService.doAuthLogout(anyString()))
                .thenReturn(HttpStatusCode.valueOf(HttpStatus.OK.value()));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(new URI("/v1/public" + "/logout"))
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                ;
    }

}
