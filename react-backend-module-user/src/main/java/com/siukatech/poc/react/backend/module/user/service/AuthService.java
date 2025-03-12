package com.siukatech.poc.react.backend.module.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.GrantType;
import com.siukatech.poc.react.backend.module.core.util.URLEncoderUtils;
import com.siukatech.poc.react.backend.module.user.form.auth.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    public static final String RESPONSE_TYPE_CODE = "code";
    public static final String CODE_CHALLENGE_METHOD = "S256";

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final RestTemplate oauth2ClientRestTemplate;
    //    private final AppCoreProp appCoreProp;
    private final ObjectMapper objectMapper;

    public AuthService(OAuth2ClientProperties oAuth2ClientProperties
            , RestTemplate oauth2ClientRestTemplate
//            , AppCoreProp appCoreProp
            , ObjectMapper objectMapper
    ) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
//        this.appCoreProp = appCoreProp;
        this.objectMapper = objectMapper;
    }

//    public MyKeyDto resolveMyKeyInfo(String userId) {
//        String myKeyInfoUrl = this.appCoreProp.getMyKeyInfoUrl();
//        MyKeyDto myKeyDto = null;
//        if (StringUtils.isNotEmpty(myKeyInfoUrl)) {
//            ResponseEntity<MyKeyDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
//                    myKeyInfoUrl, HttpMethod.POST, HttpEntity.EMPTY, MyKeyDto.class);
//            myKeyDto = responseEntity.getBody();
//            log.debug("resolveMyKeyInfo - userId: [{}], myKeyInfoUrl: [{}], myKeyDto.getUserId: [{}]"
////                + ", responseEntity.getBody.toString: [{}]"
//                    , userId, myKeyInfoUrl, myKeyDto.getUserId()
////                , responseEntity.getBody().toString()
//            );
//            if (!userId.equals(myKeyDto.getUserId())) {
//                throw new EntityNotFoundException(
//                        "User does not match userId: [%s], myKeyDto.getUserId: [%s]"
//                                .formatted(userId, myKeyDto.getUserId()));
//            }
//        }
//        else {
//            log.debug("resolveMyKeyInfo - userId: [{}], myKeyInfoUrl: [{}]"
//                    , userId, myKeyInfoUrl
//            );
//            throw new RuntimeException(
//                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
//                            .formatted(userId));
//        }
//        return myKeyDto;
//    }

    public String getAuthCodeLoginUrl(String clientName, String codeChallenge) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);
//        response_type=code&client_id=react-backend-client-01&scope=openid&redirect_uri=http://localhost:3000/redirect

        log.debug("getAuthCodeLoginUrl - registration: [{}]", registration);
        log.debug("getAuthCodeLoginUrl - registration.getScope().size: [{}]"
                        + ", registration.getScope: [{}]"
                , (registration == null ? "NULL" : registration.getScope().size())
                , (registration == null ? "NULL" : registration.getScope())
        );

        AuthCodeReq.AuthCodeReqBuilder authCodeReqBuilder = AuthCodeReq.builder()
                .responseType(RESPONSE_TYPE_CODE)
                .clientId(registration.getClientId())

                // the separator of scope is space " ", not comma
//                .scope(String.join(",", registration.getScope()))
                .scope(String.join(" ", registration.getScope()))

                .redirectUri(registration.getRedirectUri());
        if (StringUtils.hasText(codeChallenge)) {
            authCodeReqBuilder = authCodeReqBuilder
                    .codeChallenge(codeChallenge)
                    .codeChallengeMethod(CODE_CHALLENGE_METHOD)
            ;
        }
        AuthReq authCodeReq = authCodeReqBuilder.build();
//        Map authCodeReqMap = this.objectMapper.convertValue(authCodeReq, Map.class);
//        List<Map.Entry<String, String>> entryList = authCodeReqMap.entrySet().stream().toList();
//        List<NameValuePair> nameValuePairList = entryList.stream()
//                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
        Map<String, String> authCodeReqMap = this.objectMapper.convertValue(authCodeReq, Map.class);
        List<NameValuePair> nameValuePairList = authCodeReqMap.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList())
                ;
        // https://stackoverflow.com/a/2810434
        // https://stackoverflow.com/a/16066990
        String queryString =
//                URLEncodedUtils.format(nameValuePairList, Charset.forName(StandardCharsets.UTF_8.name()))
//                URLEncoder.encode(
                URLEncoderUtils.encodeToQueryString(nameValuePairList);
        String authUrl = new StringBuffer()
                .append(provider.getAuthorizationUri())
                .append("?")
//                .append("response_type").append("=").append("code")
//                .append("&")
//                .append("client_id").append("=").append(registration.getClientId())
//                .append("&")
//                .append("scope").append("=")
////                .append(String.join(",", registration.getScope()))
//                .append(String.join(" ", registration.getScope()))
//                .append("&")
//                .append("redirect_uri").append("=").append(registration.getRedirectUri())
                .append(queryString)
                .toString();
        log.debug("getAuthCodeLoginUrl - clientName: [{}], authUrl: [{}]", clientName, authUrl);
        return authUrl;
    }

    private TokenRes resolveOAuth2TokenRes(String clientName, TokenReq tokenReq, HttpHeaders httpHeaders) {
        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);

        String tokenUrl = provider.getTokenUri();
//        MultiValueMap<String, String> tokenReqMap = new LinkedMultiValueMap<String, String>();
//        tokenReqMap.add("client_id", registration.getClientId());
//        tokenReqMap.add("client_secret", registration.getClientSecret());
//        tokenReqMap.add("redirect_uri", registration.getRedirectUri());
//        tokenReqMap.add("grant_type", registration.getAuthorizationGrantType());
//        tokenReqMap.add("code", code);
        Map<String, String> tokenReqMap = this.objectMapper.convertValue(tokenReq, Map.class);
        //
        // IllegalArgumentException with message below will be thrown if converting to MultiValueMap directly
        // Manually convert to MultiValueMap is required
        // java.lang.IllegalArgumentException: Cannot find a deserializer for non-concrete Map type [map type; class org.springframework.util.MultiValueMap, [simple type, class java.lang.Object] -> [collection type; class java.util.List, contains [simple type, class java.lang.Object]]]
        MultiValueMap<String, String> tokenReqMultiValueMap = new LinkedMultiValueMap<>();
        tokenReqMap.forEach((key, value) -> {
            if (value != null) tokenReqMultiValueMap.add(key, value);
        });
        //
        HttpEntity<?> httpEntity = new HttpEntity<>(tokenReqMultiValueMap, httpHeaders);

        log.debug("resolveOAuth2TokenRes - clientName: [" + clientName
                + "], tokenReq: [" + tokenReq
                + "], tokenReqMap: [" + tokenReqMap
                + "], httpHeaders: [" + httpHeaders
                + "], oauth2ClientRestTemplate.toString: [" + oauth2ClientRestTemplate.toString()
                + "], oauth2ClientRestTemplate.getMessageConverters.size: [" + oauth2ClientRestTemplate.getMessageConverters().size()
                + "]");
//        oauth2ClientRestTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
//            log.debug("resolveOAuth2TokenRes - httpMessageConverter.getClass.getName: [" + httpMessageConverter.getClass().getName() + "]");
//        });

        try {
            ResponseEntity<TokenRes> responseEntity = oauth2ClientRestTemplate.exchange(tokenUrl
                    , HttpMethod.POST, httpEntity, TokenRes.class);

            TokenRes tokenRes = responseEntity.getBody();
            log.debug("resolveOAuth2TokenRes - responseEntity: [" + responseEntity
                    + "], clientName: [" + clientName
                    + "], tokenReq: [" + tokenReq
                    + "]");
            return tokenRes;

        } catch (Exception e) {
            log.error(e.getMessage(), e.fillInStackTrace());
            throw e;
        }
    }

    public TokenRes resolveAuthCodeTokenRes(String clientName, String code, String codeVerifier) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
//        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);

        // headers.set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
        // headers.set('Authorization', `Basic ${Buffer.from(`${client_id}:${client_secret}`)}`);

//        String tokenUrl = provider.getTokenUri();
        HttpHeaders httpHeaders = new HttpHeaders();
//        String basicAuthorization = registration.getClientId() + ":" + registration.getClientSecret();
//        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(basicAuthorization.getBytes(StandardCharsets.UTF_8)) + "");
//        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

//        MultiValueMap<String, String> tokenReqMap = new LinkedMultiValueMap<String, String>();
//        tokenReqMap.add("client_id", registration.getClientId());
//        tokenReqMap.add("client_secret", registration.getClientSecret());
//        tokenReqMap.add("grant_type", registration.getAuthorizationGrantType());
//        tokenReqMap.add("redirect_uri", registration.getRedirectUri());
//        tokenReqMap.add("code", code);
        TokenCodeReq.TokenCodeReqBuilder tokenCodeReqBuilder = TokenCodeReq.builder()
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .grantType(registration.getAuthorizationGrantType())
                .redirectUri(registration.getRedirectUri())
                .code(code);
        if (StringUtils.hasText(codeVerifier)) {
            tokenCodeReqBuilder = tokenCodeReqBuilder
                    .codeVerifier(codeVerifier);
        }
        TokenReq tokenCodeReq = tokenCodeReqBuilder.build();
//        Map<String, String> tokenReqMap = this.objectMapper.convertValue(tokenCodeReq, Map.class);
//        //
//        // IllegalArgumentException with message below will be thrown if converting to MultiValueMap directly
//        // Manually convert to MultiValueMap is required
//        // java.lang.IllegalArgumentException: Cannot find a deserializer for non-concrete Map type [map type; class org.springframework.util.MultiValueMap, [simple type, class java.lang.Object] -> [collection type; class java.util.List, contains [simple type, class java.lang.Object]]]
//        MultiValueMap<String, String> tokenReqMultiValueMap = new LinkedMultiValueMap<>();
//        tokenReqMap.forEach((key, value) -> {
//            if (value != null) tokenReqMultiValueMap.add(key, value);
//        });
//        //
//        HttpEntity<?> httpEntity = new HttpEntity<>(tokenReqMultiValueMap, httpHeaders);
//
//        log.debug("resolveTokenRes - clientName: [" + clientName
//                + "], code: [" + code
//                + "], tokenCodeReq: [" + tokenCodeReq
//                + "], httpHeaders: [" + httpHeaders
//                + "], oauth2ClientRestTemplate.toString: [" + oauth2ClientRestTemplate.toString()
//                + "], oauth2ClientRestTemplate.getMessageConverters.size: [" + oauth2ClientRestTemplate.getMessageConverters().size()
//                + "]");
//        oauth2ClientRestTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
//            log.debug("token - httpMessageConverter.getClass.getName: [" + httpMessageConverter.getClass().getName() + "]");
//        });
//
//        ResponseEntity<TokenRes> responseEntity = oauth2ClientRestTemplate.exchange(tokenUrl
//                , HttpMethod.POST, httpEntity, TokenRes.class);
//
//        TokenRes tokenRes = responseEntity.getBody();
//        log.debug("resolveTokenRes - clientName: [" + clientName
//                + "], code: [" + code
//                + "], tokenCodeReq: [" + tokenCodeReq
//                + "], responseEntity: [" + responseEntity
//                + "]");

        TokenRes tokenRes = this.resolveOAuth2TokenRes(clientName, tokenCodeReq, httpHeaders);
        return tokenRes;
    }

    public TokenRes resolvePasswordTokenRes(String clientName, LoginForm loginForm) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        HttpHeaders httpHeaders = new HttpHeaders();
        TokenReq tokenPasswordReq = TokenPasswordReq.builder()
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .grantType(GrantType.PASSWORD.getValue())
//                .redirectUri(registration.getRedirectUri())
//                .grantType(registration.getAuthorizationGrantType())
                .username(loginForm.getUsername())
                .password(loginForm.getPassword())
                .build();
        TokenRes tokenRes = this.resolveOAuth2TokenRes(clientName, tokenPasswordReq, httpHeaders);
        return tokenRes;
    }

    public TokenRes resolveRefreshTokenTokenRes(String clientName, RefreshTokenForm refreshTokenForm) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        HttpHeaders httpHeaders = new HttpHeaders();
        TokenReq tokenRefreshTokenReq = TokenRefreshTokenReq.builder()
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .grantType(GrantType.REFRESH_TOKEN.getValue())
                .refreshToken(refreshTokenForm.getRefreshToken())
//                .accessToken(refreshTokenForm.getAccessToken())
                .build();
        TokenRes tokenRes = this.resolveOAuth2TokenRes(clientName, tokenRefreshTokenReq, httpHeaders);
        return tokenRes;
    }

    public HttpStatusCode doAuthLogout(String logoutApi) throws URISyntaxException {
        ResponseEntity<Map> responseEntity = this.oauth2ClientRestTemplate.getForEntity(new URI(logoutApi), Map.class);
        Map<String, String> map = responseEntity.getBody();
        log.debug("doAuthLogout - map: [{}], responseEntity: [{}]"
                , map, responseEntity);
        return responseEntity.getStatusCode();
    }
}
