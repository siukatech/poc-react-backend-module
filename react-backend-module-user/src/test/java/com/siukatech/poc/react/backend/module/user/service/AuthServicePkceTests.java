package com.siukatech.poc.react.backend.module.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.util.EncryptionUtils;
import com.siukatech.poc.react.backend.module.user.form.auth.TokenRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@Slf4j
@ExtendWith({MockitoExtension.class
        , SpringExtension.class
})
@EnableConfigurationProperties
@ContextConfiguration(classes = {
        OAuth2ClientProperties.class
        , AppCoreProp.class
})
@TestPropertySource({"classpath:abstract-oauth2-tests.properties"
        , "classpath:global/app-core-config-tests.properties"
//        , "classpath:oauth2-tests-local.properties"
})
public class AuthServicePkceTests extends AbstractUnitTests {

    /**
     * This is the OAuth2ClientProperties initiated with @TestPropertySource
     */
    @Autowired
    private OAuth2ClientProperties oAuth2ClientPropertiesForTests;
    @Autowired
    private AppCoreProp appCorePropForTests;

    @InjectMocks
    private AuthService authService;

    /**
     * Cannot use @Mock, dont know why cannot mock this oAuth2ClientProperties.
     * If the AuthService.oAuth2ClientProperties changed to NOT final,
     * then using @Mock is ok
     * <p>
     * However, if AuthService.oAuth2ClientProperties is final,
     * then only @Spy is ok with doReturn().when()
     */
//    @Mock
    @Spy
    private OAuth2ClientProperties oAuth2ClientProperties;

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
        log.debug("setup - oAuth2ClientPropertiesForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientPropertiesForTests.getRegistration().size()
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

    /**
     * Reference:
     * https://blog.csdn.net/zzhongcy/article/details/127282700
     * https://www.baeldung.com/httpclient-redirect-on-http-post#1-for-httpclient-5x
     *
     * @return
     */
//    private RestTemplate prepareRestTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setRedirectStrategy(new DefaultRedirectStrategy())
//                .build();
//        requestFactory.setHttpClient(httpClient);
//        restTemplate.setRequestFactory(requestFactory);
//        return restTemplate;
//    }

    @Test
    public void test_doAuthCodeFlow_basic() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // given
        String clientName = CLIENT_NAME;
        String codeVerifier = EncryptionUtils.generateCodeVerifier();
        String codeChallenge = EncryptionUtils.generateCodeChallenge(codeVerifier);
        doReturn(this.oAuth2ClientPropertiesForTests.getRegistration())
                .when(this.oAuth2ClientProperties).getRegistration();
        doReturn(this.oAuth2ClientPropertiesForTests.getProvider())
                .when(this.oAuth2ClientProperties).getProvider();
        log.debug("test_getAuthCodeLoginUrl_basic - oAuth2ClientPropertiesForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientPropertiesForTests.getRegistration().size()
        );

        // when
        String authCodeLoginUrl = this.authService.getAuthCodeLoginUrl(clientName, codeChallenge);

        // then
//        assertThat(authCodeLoginUrl).contains("response_type");
        //
//        RestTemplate restTemplate = this.prepareRestTemplate();
//        //
//        HttpHeaders httpHeaders = new HttpHeaders();
//        MultiValueMap<String, String> authCodeReqMultiValueMap = new LinkedMultiValueMap<>();
//        HttpEntity<?> httpEntity = new HttpEntity<>(authCodeReqMultiValueMap, httpHeaders);
//        ResponseEntity<?> responseEntity = restTemplate.exchange(authCodeLoginUrl
//                , HttpMethod.GET, httpEntity, EncryptionUtilsPkceTests.AuthCodeRes.class);

        CloseableHttpClient httpclient = HttpClients
//                .createDefault()
                .custom()
                .disableRedirectHandling()
                .build()
                ;
        HttpGet httpGet = new HttpGet(authCodeLoginUrl);
        String resultContent = null;
        String actionUrl = null;
        Header header = null;
        String redirectUrl = null;
        String code = null;
        if (resultContent == null) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // Get status code
                log.debug("test_doAuthCodeFlow_basic - 1 - response.getVersion: [{}]"
                                + ", response.getCode: [{}]"
                                + ", response.getReasonPhrase: [{}]"
                        , response.getVersion() // HTTP/1.1
                        , response.getCode() // 200
                        , response.getReasonPhrase() // OK
                );
                HttpEntity entity = response.getEntity();
                // Get response information
                resultContent = EntityUtils.toString(entity);
                //
                actionUrl = resultContent;
                actionUrl = actionUrl.substring(actionUrl.indexOf("action=\"") + "action=\"".length());
                actionUrl = actionUrl.substring(0, actionUrl.indexOf("\" method=\"post\""));
                actionUrl = actionUrl.replaceAll("&amp;", "&");
                log.debug("test_doAuthCodeFlow_basic - 1 - resultContent: [{}], actionUrl: [{}]", resultContent, actionUrl);
            } catch (IOException | ParseException e) {
                log.error(e.getMessage(), e.fillInStackTrace());
            }
        }
        if (actionUrl != null) {
            HttpPost httpPost = new HttpPost(actionUrl);// form parameters.
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("username", "app-user-01"));
            nvps.add(new BasicNameValuePair("password", "admin01"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                log.debug("test_doAuthCodeFlow_basic - 2 - response.getVersion: [{}]"
                                + ", response.getCode: [{}]"
                                + ", response.getReasonPhrase: [{}]"
                        , response.getVersion() // HTTP/1.1
                        , response.getCode() // 200
                        , response.getReasonPhrase() // OK
                );
                //
                header = response.getHeader("Location");
                redirectUrl = header.getValue().trim();
                code = redirectUrl;
                code = code.substring(code.indexOf("code=") + "code=".length());
                log.debug("test_doAuthCodeFlow_basic - 2 - redirectUrl: [{}], code: [{}]", redirectUrl, code);
                //
            } catch (IOException | ProtocolException e) {
                throw new RuntimeException(e);
            }
        }
        if (code != null) {
            TokenRes tokenRes = this.authService.resolveAuthCodeTokenRes(clientName, code, codeVerifier);
            log.debug("test_doAuthCodeFlow_basic - 3 - tokenRes: [{}]", tokenRes);

            // then
            assertThat(tokenRes)
                    .hasFieldOrProperty("accessToken")
                    .has(new Condition<>(x -> {
//                        return "accessToken".equals(x.accessToken());
                        return x.accessToken() != null;
                    }, "Has value: %s", List.of("accessToken")))
            ;
        }

    }

}
