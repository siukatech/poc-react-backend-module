package com.siukatech.poc.react.backend.module.core.security.provider;

import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.business.dto.MyPermissionDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.global.helper.UserDtoTestDataHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RemoteAuthorizationDataProviderTests extends AbstractUnitTests {

    @InjectMocks
    private RemoteAuthorizationDataProvider remoteAuthorizationDataProvider;

    @Mock
    private RestTemplate oauth2ClientRestTemplate;

    @Mock
    private AppCoreProp appCoreProp;

    @Spy
    private UserDtoTestDataHelper userDtoTestDataHelper;


    @Test
    void contextLoads() {
        // TODO
        Assertions.assertNotNull(log);
        log.debug("contextLoads - testing logging");
    }

    @Test
    void test_findUserByUserIdAndTokenValue_basic() {
        // given
        UserDto userDtoMock = this.userDtoTestDataHelper.prepareUserDto_basic();
        String targetUserId = userDtoMock.getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        String myUserInfoUrl = "https://www.google.com";
        //
//        when(this.appCoreProp.getApplicationId()).thenReturn(applicationId);
        when(this.appCoreProp.getMyUserInfoUrl()).thenReturn(myUserInfoUrl);
        when(this.oauth2ClientRestTemplate.exchange(
                anyString(), eq(HttpMethod.GET)
                , any(HttpEntity.class)
                , eq(UserDto.class)
        )).thenReturn(ResponseEntity.ok(userDtoMock));

        // when
        UserDto userDtoRet = this.remoteAuthorizationDataProvider
                .findUserByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
        assertThat(userDtoRet.getUserId()).isEqualTo(userDtoMock.getUserId());
    }

    @Test
    void test_findPermissionsByUserIdAndTokenValue_basic() {
        // given
        MyPermissionDto myPermissionDto = this.userDtoTestDataHelper.prepareMyPermissionDto_basic();
        String targetUserId = myPermissionDto.getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        String myPermissionInfoUrl = "https://www.google.com";
        //
//        when(this.appCoreProp.getApplicationId()).thenReturn(applicationId);
        when(this.appCoreProp.getMyPermissionInfoUrl()).thenReturn(myPermissionInfoUrl);
        when(this.oauth2ClientRestTemplate.exchange(
                anyString(), eq(HttpMethod.GET)
                , any(HttpEntity.class)
                , eq(MyPermissionDto.class)
        )).thenReturn(ResponseEntity.ok(myPermissionDto));

        // when
        List<UserPermissionDto> userPermissionDtoList = this.remoteAuthorizationDataProvider
                .findPermissionsByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
        assertThat(userPermissionDtoList.size()).isEqualTo(myPermissionDto.getUserPermissionList().size());
    }

    @Test
    void test_findDossierByUserIdAndTokenValue_basic() {
        // given
        UserDossierDto userDossierDtoMock = this.userDtoTestDataHelper.prepareUserDossierDto_basic();
        String targetUserId = userDossierDtoMock.getUserDto().getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        String myUserDossierUrl = "https://www.google.com";
        //
        when(appCoreProp.getApplicationId()).thenReturn(applicationId);
        when(appCoreProp.getMyUserDossierUrl()).thenReturn(myUserDossierUrl);
        when(this.oauth2ClientRestTemplate.exchange(
                anyString(), eq(HttpMethod.GET)
                , any(HttpEntity.class)
                , eq(UserDossierDto.class)
        )).thenReturn(ResponseEntity.ok(userDossierDtoMock));

        // when
        UserDossierDto userDossierDto = this.remoteAuthorizationDataProvider
                .findDossierByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
        assertThat(userDossierDto.getUserPermissionList().size())
                .isEqualTo(userDossierDtoMock.getUserPermissionList().size());

    }

}
