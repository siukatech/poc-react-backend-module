package com.siukatech.poc.react.backend.module.user.controller;

import com.siukatech.poc.react.backend.module.core.AbstractWebTests;
import com.siukatech.poc.react.backend.module.user.global.helper.UserEntityTestDataHelper;
import com.siukatech.poc.react.backend.module.core.business.dto.*;
import com.siukatech.poc.react.backend.module.user.service.UserService;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {MyController.class})
@AutoConfigureMockMvc(addFilters = false)
//@AutoConfigureWebClient
public class MyControllerTests extends AbstractWebTests {

    /**
     * Reference:
     * https://stackoverflow.com/a/72086318
     * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/setup.html#test-mockmvc-setup
     * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/authentication.html
     * <p>
     * When we inject authentication or other security related bean to our controller methods.
     * The spring-security must be set up during the MockMvc creation.
     * We need to inject the WebApplicationContext for the custom MockMvc creation.
     * Therefore, the @Autowired annotation for MockMvc cannot be used in these situations.
     */
//    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
    @MockBean
    private OAuth2ClientProperties oAuth2ClientProperties;

//    @SpyBean
//    private UserTestDataHelper userTestDataHelper;

//    @MockBean
//    private AppCoreProp appCoreProp;
//    @MockBean
//    private RestTemplateBuilder restTemplateBuilder;
//    @MockBean
//    private RestTemplate oauth2ClientRestTemplate;

    @SpyBean
    private UserEntityTestDataHelper userEntityTestDataHelper;


//    private UsernamePasswordAuthenticationToken prepareUsernamePasswordAuthenticationToken_basic() {
//        return prepareUsernamePasswordAuthenticationToken("app-user-01");
//    }

    private MyAuthenticationToken prepareMyAuthenticationToken_basic() {
        return prepareMyAuthenticationToken("app-user-01"
                , UUID.randomUUID().toString(), USER_NAME_ATTRIBUTE, this.userEntityTestDataHelper);
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
        // We dont need to setup the authentication in @BeforeEach method.
        // After applying .with(authentication([mock-authentication-object]))
        // This will also add the mock-authentication-object to the SecurityContext
//        UsernamePasswordAuthenticationToken authenticationToken = prepareAuthenticationToken_basic();
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //
        // Refer to the explanation of MockMvc above.
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(springSecurity())
                    .build();
        }
        //
        log.debug("setup - SecurityContextHolder.getContext.getAuthentication: [{}]"
                , SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
//    @WithMockUser("app-user-01")
    public void test_getPublicKey_basic() throws Exception {
        // given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("test_getPublicKey_basic - authentication: [" + authentication + "]");

        MyKeyDto myKeyDto = this.userEntityTestDataHelper.prepareMyKeyDto_basic();
        when(userService.findKeyByUserId(anyString())).thenReturn(myKeyDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/my/public-key")
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/public-key")
//                .with(authentication(this.prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                //.with(SecurityMockMvcRequestPostProcessors.users((UserDetails) authentication.getPrincipal()))
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("public-key"))
                .andReturn();

        // result
        log.debug("test_getPublicKey_basic - mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");

    }

    @Test
    public void test_getKeyInfo_basic() throws Exception {
        // given
        MyKeyDto myKeyDto = this.userEntityTestDataHelper.prepareMyKeyDto_basic();
        when(userService.findKeyByUserId(anyString())).thenReturn(myKeyDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/my/key-info")
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/key-info")
//                .with(authentication(this.prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{userId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("test_getKeyInfo_basic - mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");

    }

    @Test
    public void test_getUserInfo_basic() throws Exception {
        // given
        UserDto userDto = this.userEntityTestDataHelper.prepareUserDto_basic();
        when(userService.findUserByUserId(anyString())).thenReturn(userDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/my/user-info")
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/user-info")
//                .with(authentication(this.prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{userId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("test_getUserInfo_basic - mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");

    }

    @Test
    public void test_getPermissionInfo_basic() throws Exception {
        // given
        MyPermissionDto myPermissionDto = this.userEntityTestDataHelper.prepareMyPermissionDto_basic();
        String userId = myPermissionDto.getUserId();
        List<UserPermissionDto> userPermissionDtoList = myPermissionDto.getUserPermissionList();
        when(userService.findPermissionsByUserIdAndApplicationId(anyString(), anyString())).thenReturn(userPermissionDtoList);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX + "/my/permission-info?applicationId={applicationId}", "frontend-app")
//                .with(authentication(this.prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
//                .andExpect(content().json())
                .andExpect(content().string(containsString("appResourceId")))
                .andReturn();

        // result
        log.debug("test_getPermissionInfo_basic -  mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");
    }

    @Test
    public void test_getUserDossier_basic() throws Exception {
        // given
        UserDossierDto userDossierDto = this.userEntityTestDataHelper.prepareUserDossierDto_basic();
        when(userService.findDossierByUserIdAndApplicationId(anyString(), anyString())).thenReturn(userDossierDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX + "/my/user-dossier?applicationId={applicationId}", "frontend-app")
//                .with(authentication(this.prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().json())
                .andExpect(content().string(containsString("appResourceId")))
                .andReturn();

        // result
        log.debug("test_getUserDossier_basic - mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");
    }

    @Test
    public void test_getUserView_basic() throws Exception {
        // given
        UserViewDto userViewDto = this.userEntityTestDataHelper.prepareUserViewDto_basic();
        when(userService.findViewByUserId(anyString())).thenReturn(userViewDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/user-view")
//                .with(authentication(this.prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(this.prepareMyAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{userId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("test_getUserView_basic - mvcResult.getResponse.getContentAsString: ["
                + mvcResult.getResponse().getContentAsString()
                + "], end");

    }

}
