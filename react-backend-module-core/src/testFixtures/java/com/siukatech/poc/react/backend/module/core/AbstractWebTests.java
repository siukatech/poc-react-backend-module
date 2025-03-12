package com.siukatech.poc.react.backend.module.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.global.helper.UserDtoTestDataHelper;
import com.siukatech.poc.react.backend.module.core.security.evaluator.PermissionControlEvaluator;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.core.web.advice.helper.EncryptedBodyAdviceHelper;
import com.siukatech.poc.react.backend.module.core.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.module.core.web.micrometer.CorrelationIdHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class AbstractWebTests extends AbstractUnitTests {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected ObjectMapper objectMapper;

    @SpyBean
    protected EncryptedBodyContext encryptedBodyContext;
    @MockBean
    protected EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;
//    @MockBean
//    private InMemoryClientRegistrationRepository clientRegistrationRepository;
    @MockBean
    protected AuthorizationDataProvider authorizationDataProvider;
    @MockBean
    protected AppCoreProp appCoreProp;
    @MockBean
    protected PermissionControlEvaluator permissionControlEvaluator;
//    @MockBean
//    protected Tracer tracer;
    @MockBean
    protected CorrelationIdHandler correlationIdHandler;
//    @MockBean
//    protected ProblemDetailExtMapper problemDetailExtMapper;


    protected MockMvc prepareMockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    protected UsernamePasswordAuthenticationToken prepareUsernamePasswordAuthenticationToken(String username) {
        List<GrantedAuthority> convertedAuthorities = new ArrayList<>();
        UserDetails userDetails = new User(username, "", convertedAuthorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authenticationToken;
    }

    protected MyAuthenticationToken prepareMyAuthenticationToken(
            String userId, String randomId, UserDtoTestDataHelper userDtoTestDataHelper) {
        UserDossierDto userDossierDto = userDtoTestDataHelper.prepareUserDossierDto_basic();
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

}
