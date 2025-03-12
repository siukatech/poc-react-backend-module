package com.siukatech.poc.react.backend.module.user.controller;

import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.module.user.global.helper.UserEntityTestDataHelper;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.user.service.UserService;
import com.siukatech.poc.react.backend.module.core.security.evaluator.PermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.core.web.annotation.v1.ProtectedApiV1Controller;
import com.siukatech.poc.react.backend.module.core.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.module.core.web.advice.helper.EncryptedBodyAdviceHelper;
import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Slf4j
@WebMvcTest(controllers = {UserController.class}
////        , excludeAutoConfiguration = {SecurityAutoConfiguration.class}
        , properties = {
        "client-id=XXX"
        , "client-secret=XXX"
        , "client-realm=react-backend-realm"
        , "oauth2-client-keycloak=http://keycloak-host-name"
        , "oauth2-client-redirect-uri=http://redirect-host-name/redirect"
        , "spring.profiles.active=dev"
        , "logging.level.com.siukatech.poc.react.backend.core: TRACE"
    }
////    , useDefaultFilters = false
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = {"classpath:abstract-oauth2-tests.properties"})
public class UserControllerTests {

    protected static final Logger log = LoggerFactory.getLogger(UserControllerTests.class);

//    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
//    @MockBean
//    private UserRepository userRepository;
    @MockBean
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    @SpyBean
    private EncryptedBodyContext encryptedBodyContext;
    @MockBean
    private EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;
    @MockBean
    private InMemoryClientRegistrationRepository clientRegistrationRepository;
    @MockBean
    private AuthorizationDataProvider authorizationDataProvider;
    @MockBean
    private PermissionControlEvaluator permissionControlEvaluator;
//    @MockBean
//    protected Tracer tracer;
    @MockBean
    protected CorrelationIdHandler correlationIdHandler;
    @MockBean
    private OAuth2ClientProperties oAuth2ClientProperties;
    // @MockBean
    // protected ProblemDetailExtMapper problemDetailExtMapper;
    @SpyBean
    private UserEntityTestDataHelper userEntityTestDataHelper;


    private UsernamePasswordAuthenticationToken prepareAuthenticationToken_basic() {
        List<GrantedAuthority> convertedAuthorities = new ArrayList<>();
        UserDetails userDetails = new User("app-user-01", "", convertedAuthorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authenticationToken;
    }

    private MyAuthenticationToken prepareMyAuthenticationToken_basic() {
        return prepareMyAuthenticationToken("app-user-01"
                , UUID.randomUUID().toString(), this.userEntityTestDataHelper);
    }

    protected MyAuthenticationToken prepareMyAuthenticationToken(
            String userId, String randomId, UserEntityTestDataHelper userEntityTestDataHelper) {
        UserDossierDto userDossierDto = userEntityTestDataHelper.prepareUserDossierDto_basic();
        List<GrantedAuthority> convertedAuthorities = new ArrayList<>();
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put(StandardClaimNames.PREFERRED_USERNAME, userId);
        attributeMap.put(MyAuthenticationToken.ATTR_TOKEN_VALUE, "TOKEN");
        attributeMap.put(MyAuthenticationToken.ATTR_USER_ID, userId);
        attributeMap.put(MyAuthenticationToken.ATTR_USER_DOSSIER_DTO, userDossierDto);
        OAuth2User oAuth2User = new DefaultOAuth2User(convertedAuthorities, attributeMap, StandardClaimNames.PREFERRED_USERNAME);
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(oAuth2User, convertedAuthorities, "keycloak");
        return authenticationToken;
    }

//    @BeforeAll
//    public static void init() {
//    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        switch (method.getName()) {
            case "getPublicKey_basic":
            case "getUserInfo_basic":
            default:
        }
        //
//        UsernamePasswordAuthenticationToken authenticationToken = prepareAuthenticationToken_basic();
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                    .apply(springSecurity())
                    .build();
        }
        //
        log.debug("setup - SecurityContextHolder.getContext.getAuthentication: [" + SecurityContextHolder.getContext().getAuthentication() + "]");
    }
//
//    @Test
////    @WithMockUser("app-user-01")
//    public void test_getPublicKey_basic() throws Exception {
//        // given
////        UserEntity userEntity = this.prepareUserEntity_basic();
////        when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));
//        UserDto userDto = this.prepareUserDto_basic();
//        when(userService.findByUserId(anyString())).thenReturn(userDto);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.debug("test_getPublicKey_basic - authentication: [" + authentication + "]");
//
//        // when
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/users/{targetUserId}/public-key", userDto.getUserId())
//                .with(authentication(prepareAuthenticationToken_basic()))
//                .with(csrf())
//                //.with(SecurityMockMvcRequestPostProcessors.user((UserDetails) authentication.getPrincipal()))
//                .accept(MediaType.APPLICATION_JSON);
//
//        // then / verify
//        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string("public-key"))
//                .andReturn();
//
//        // result
//        log.debug("test_getPublicKey_basic - mvcResult.getResponse.getContentAsString: ["
//                + mvcResult.getResponse().getContentAsString()
//                + "], end");
//
//    }

    @Test
    public void test_getUserInfo_basic() throws Exception {
        // given
//        UserDto userDto = this.prepareUserDto_basic();
//        when(userService.findByUserId(anyString())).thenReturn(userDto);
        UserViewDto userViewDto = this.userEntityTestDataHelper.prepareUserViewDto_basic();
        when(userService.findUserByUserId(anyString())).thenReturn(userViewDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/users/{targetUserId}/user-info"
//                        , userDto.getUserId()
                        , userViewDto.getUserId()
                )
//                .with(authentication(this.prepareAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{userId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("test_getUserInfo_basic - mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");

    }

}
