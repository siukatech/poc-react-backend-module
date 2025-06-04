package com.siukatech.poc.react.backend.module.core.security.provider;

import com.siukatech.poc.react.backend.module.core.business.dto.MyPermissionDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.module.core.caching.config.DefaultCachingConfig;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class RemoteAuthorizationDataProvider implements AuthorizationDataProvider {

    private final RestTemplate oauth2ClientRestTemplate;

    private final AppCoreProp appCoreProp;

    private static final String PARAM_APPLICATION_ID = "applicationId";

    public RemoteAuthorizationDataProvider(
            AppCoreProp appCoreProp
            , RestTemplate oauth2ClientRestTemplate) {
        this.appCoreProp = appCoreProp;
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
    }

    private void prepareHttpHeaders(HttpHeaders httpHeaders, String tokenValue) {
        // Special handling of adding token to oauth2ClientRestTemplate
        // authentication from SecurityContext is null when the process is KeycloakJwtAuthenticationConverter.
        // authentication is still preparing at that moment.
        // So header is required to configured at that moment.
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
    }

    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findUserByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataUserCacheKeyGenerator"
    )
//    @Override
    public UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findUserByUserIdAndTokenValue - start");
        UserDto userDto = null;
        String myUserInfoUrl = this.appCoreProp.getMyUserInfoUrl();
        if (StringUtils.isNotEmpty(myUserInfoUrl)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            prepareHttpHeaders(httpHeaders, tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            try {
                ResponseEntity<UserDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                        myUserInfoUrl, HttpMethod.GET
//                    , HttpEntity.EMPTY
                        , httpEntity
                        , UserDto.class);
                userDto = responseEntity.getBody();
                log.debug("findUserByUserIdAndTokenValue - userId: [{}], myUserInfoUrl: [{}]"
                                + ", userDto.getUserId: [{}]"
//                                + ", responseEntity.getBody.toString: [{}]"
                        , userId, myUserInfoUrl
                        , (userDto == null ? "NULL" : userDto.getUserId())
//                        , responseEntity.getBody().toString()
                );
                if (userDto == null || !userId.equals(userDto.getUserId())) {
                    throw new EntityNotFoundException(
                            "User does not match userId: [%s], userDto.getUserId: [%s]"
                                    .formatted(userId, userDto.getUserId()));
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error occurred during calling api: [%s]".formatted(myUserInfoUrl)
                        , e);
            }
        } else {
            log.debug("findUserByUserIdAndTokenValue - userId: [{}], myUserInfoUrl: [{}]"
                    , userId, myUserInfoUrl
            );
            throw new RuntimeException(
                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(userId));
        }
        log.debug("findUserByUserIdAndTokenValue - end");
        return userDto;
    }

    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findPermissionsByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataPermissionCacheKeyGenerator"
    )
//    @Override
    public List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findPermissionsByUserIdAndTokenValue - start");
        List<UserPermissionDto> userPermissionDtoList = new ArrayList<>();
        //
        String myPermissionInfoUrl = this.appCoreProp.getMyPermissionInfoUrl();
        if (StringUtils.isNotEmpty(myPermissionInfoUrl)) {
            UriComponentsBuilder myPermissionInfoUriBuilder = UriComponentsBuilder
                    .fromUriString(myPermissionInfoUrl)
                    .queryParam(PARAM_APPLICATION_ID, this.appCoreProp.getApplicationId());
            String myPermissionInfoUri = myPermissionInfoUriBuilder.encode().toUriString();
            HttpHeaders httpHeaders = new HttpHeaders();
            prepareHttpHeaders(httpHeaders, tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<MyPermissionDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myPermissionInfoUri, HttpMethod.GET
                    , httpEntity
//                    , new ParameterizedTypeReference<UserPermissionInfoDto>() {
//                    }
                    , MyPermissionDto.class
                    );
            MyPermissionDto myPermissionDto = responseEntity.getBody();
            log.debug("findPermissionsByUserIdAndTokenValue - userId: [{}]"
                            + ", myPermissionInfoUri: [{}]"
                            + ", userPermissionInfoDto.getUserId: [{}]"
                            + ", userPermissionDtoList.size: [{}]"
//                + ", responseEntity.getBody.toString: [{}]"
                    , userId, myPermissionInfoUri
                    , (Objects.isNull(myPermissionDto) ? "NULL" : myPermissionDto.getUserId())
                    , (Objects.isNull(myPermissionDto) ? "NULL" : myPermissionDto.getUserPermissionList().size())
//                , responseEntity.getBody().toString()
            );
            if (Objects.isNull(myPermissionDto) || !userId.equals(myPermissionDto.getUserId())) {
                throw new EntityNotFoundException(
                        "User does not match userId: [%s], userPermissionInfoDto.getUserId: [%s]"
                                .formatted(userId
                                        , (Objects.isNull(myPermissionDto) ? null : myPermissionDto.getUserId())
                                ));
            }
            else {
                userPermissionDtoList.addAll(myPermissionDto.getUserPermissionList());
                log.debug("findPermissionsByUserIdAndTokenValue - userId: [{}], userPermissionDtoList: [{}]"
                        , userId, userPermissionDtoList.size()
                );
            }
        } else {
            log.debug("findPermissionsByUserIdAndTokenValue - userId: [{}], myUserPermissionInfoUrl: [{}]"
                    , userId, myPermissionInfoUrl
            );
            throw new RuntimeException(
                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(userId));
        }
        log.debug("findPermissionsByUserIdAndTokenValue - end");
        return userPermissionDtoList;
    }

    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_AUTH}
//            , key = "'" + CACHE_KEY_findDossierByUserIdAndTokenValue + "' + #userId"
            , keyGenerator = "authorizationDataDossierCacheKeyGenerator"
    )
    @Override
    public UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findDossierByUserIdAndTokenValue - start");
        UserDossierDto userDossierDto = null;
        //
        String myUserDossierUrl = this.appCoreProp.getMyUserDossierUrl();
        if (StringUtils.isNotEmpty(myUserDossierUrl)) {
            UriComponentsBuilder myUserDossierUriBuilder = UriComponentsBuilder
                    .fromUriString(myUserDossierUrl)
                    .queryParam(PARAM_APPLICATION_ID, this.appCoreProp.getApplicationId());
            String myUserDossierUri = myUserDossierUriBuilder.encode().toUriString();
            HttpHeaders httpHeaders = new HttpHeaders();
            prepareHttpHeaders(httpHeaders, tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<UserDossierDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myUserDossierUri, HttpMethod.GET
                    , httpEntity
                    , UserDossierDto.class
            );
            userDossierDto = responseEntity.getBody();
            log.debug("findDossierByUserIdAndTokenValue - userId: [{}], myUserDossierUri: [{}]"
                            + ", userDossierDto.getUserDto: [{}]"
                            + ", userDossierDto.getUserPermissionList.size: [{}]"
                    , userId, myUserDossierUri
                    , (Objects.isNull(userDossierDto) ? "NULL" : userDossierDto.getUserDto())
                    , (Objects.isNull(userDossierDto) ? "NULL" : userDossierDto.getUserPermissionList().size())
            );
        } else {
            log.debug("findDossierByUserIdAndTokenValue - userId: [{}], myUserPermissionInfoUrl: [{}]"
                    , userId, myUserDossierUrl
            );
            throw new RuntimeException(
                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(userId));
        }
        log.debug("findDossierByUserIdAndTokenValue - end");
        return userDossierDto;

    }

    @CacheEvict(value = {DefaultCachingConfig.CACHE_NAME_AUTH})
    @Override
    public void evictDossierCache() {
        // do nothing
        log.debug("evictDossierCache - cache is evicted");
    }

}
