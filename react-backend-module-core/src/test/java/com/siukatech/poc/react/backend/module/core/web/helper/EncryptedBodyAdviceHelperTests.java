package com.siukatech.poc.react.backend.module.core.web.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedDetail;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedInfo;
import com.siukatech.poc.react.backend.module.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.module.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.module.core.util.EncryptionUtils;
import com.siukatech.poc.react.backend.module.core.web.advice.helper.EncryptedBodyAdviceHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith({MockitoExtension.class
        , SpringExtension.class
})
@EnableConfigurationProperties
@ContextConfiguration(classes = {OAuth2ClientProperties.class, AppCoreProp.class})
@TestPropertySource({"classpath:abstract-oauth2-tests.properties"
        , "classpath:global/app-core-config-tests.properties"
})
public class EncryptedBodyAdviceHelperTests extends AbstractUnitTests {

    /**
     * This is the OAuth2ClientProperties initiated with @TestPropertySource
     */
    @Autowired
    private OAuth2ClientProperties oAuth2ClientPropertiesForTests;
    @Autowired
    private AppCoreProp appCorePropForTests;

    //    @InjectMocks
    private EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;

    @Spy
    private ObjectMapper objectMapper;
    //    @Mock
    @Spy
    private RestTemplate oauth2ClientRestTemplate;
    @Spy
    private AppCoreProp appCoreProp;
//    @Mock
//    private AuthService authService;
    @Mock
    private AuthorizationDataProvider authorizationDataProvider;


    @BeforeEach
    public void setup() {
        // Reference:
        // https://stackoverflow.com/a/34878977
        // If test-target's members are 'final', then we dont use @InjectMocks
        // We create the test-target manually with constructor injection
        if (encryptedBodyAdviceHelper == null) {
            encryptedBodyAdviceHelper = new EncryptedBodyAdviceHelper(objectMapper
                    , oauth2ClientRestTemplate
                    , appCoreProp
//                    , authService
            );
        }
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

    private EncryptedInfo prepareEncryptedInfo_basic() {
        // Reference:
        // https://stackoverflow.com/a/29354222
        // aes key MUST be 16, 24, 32
        // iv MUST be 16
        // salt MUST be 16
        String key = EncryptionUtils.generateRandomToken(32);
        String iv = EncryptionUtils.generateRandomToken(16);
        String salt = EncryptionUtils.generateRandomToken(16);
        EncryptedInfo encryptedInfo = new EncryptedInfo(key, iv, salt);
        return encryptedInfo;
    }

    @Test
    public void test_encryptBodyToDataBase64_basic() throws Exception {
        // given
        MyKeyDto body = this.prepareMyKeyDto_basic();
        String bodyStr = this.objectMapper.writeValueAsString(body);
        EncryptedInfo encryptedInfo = this.prepareEncryptedInfo_basic();
        byte[] aesKey = EncryptionUtils.encryptWithRsaPublicKey(encryptedInfo.key(), body.getPublicKey());
        String aesKeyBase64 = Base64.getEncoder().encodeToString(aesKey);
        log.debug("test_encryptBodyToDataBase64_basic - aesKeyBase64.length: [{}]"
                        + ", bodyStr: [{}]"
                , aesKeyBase64.length()
                , bodyStr
        );

        // when
        String cipherText = this.encryptedBodyAdviceHelper.encryptBodyToDataBase64(body, encryptedInfo);

        // then
        log.debug("test_encryptBodyToDataBase64_basic - cipherText: [{}]", cipherText);
        byte[] data = Base64.getDecoder().decode(cipherText);
        byte[] secret = Base64.getDecoder().decode(encryptedInfo.key());
        byte[] iv = Base64.getDecoder().decode(encryptedInfo.iv());
        byte[] content = EncryptionUtils.decryptWithAesCbcSecret(data, secret, iv);
        String contentStr = new String(content);
        log.debug("test_encryptBodyToDataBase64_basic - contentStr: [{}]", contentStr);

        assertThat(bodyStr).isEqualTo(contentStr);

    }

//    @Test
//    public void test_resolveRsaInfoAesContent_basic() throws Exception {
//        // given
//        MyKeyDto body = this.prepareMyKeyDto_basic();
//        String bodyStr = this.objectMapper.writeValueAsString(body);
//        EncryptedInfo encryptedInfo = this.prepareEncryptedInfo_basic();
//        byte[] aesKey = EncryptionUtil.encryptWithRsaPublicKey(encryptedInfo.key(), body.getPublicKey());
//        String aesKeyBase64 = Base64.getEncoder().encodeToString(aesKey);
//        String contentBase64 = Base64.getEncoder().encodeToString("This is a test".getBytes(StandardCharsets.UTF_8));
//
//        // when
//        this.encryptedBodyAdviceHelper.re
//
//        // then
//    }

    @Test
    public void test_decryptDataBase64ToBodyDetail_basic() throws Exception {
        // given
        MyKeyDto userDto = this.prepareMyKeyDto_basic();
        String userDtoStr = this.objectMapper.writeValueAsString(userDto);
        EncryptedInfo encryptedInfo = this.prepareEncryptedInfo_basic();
        String encryptedInfoStr = this.objectMapper.writeValueAsString(encryptedInfo);
        byte[] secret = Base64.getDecoder().decode(encryptedInfo.key());
        byte[] iv = Base64.getDecoder().decode(encryptedInfo.iv());
        byte[] aesKey = EncryptionUtils.encryptWithRsaPublicKey(encryptedInfo.key(), userDto.getPublicKey());
        String aesKeyBase64 = Base64.getEncoder().encodeToString(aesKey);
        byte[] headerData = EncryptionUtils.encryptWithRsaPublicKey(encryptedInfoStr, userDto.getPublicKey());
        String headerDataBase64 = Base64.getEncoder().encodeToString(headerData);
        byte[] bodyData = EncryptionUtils.encryptWithAesCbcSecret(userDtoStr, secret, iv);
        String bodyDataBase64 = Base64.getEncoder().encodeToString(bodyData);
        String encryptedBody = headerDataBase64 + bodyDataBase64;
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedBody.getBytes(StandardCharsets.UTF_8));
        log.debug("test_decryptDataBase64ToBodyDetail_basic - headerDataBase64.length: [{}]"
                        + ", bodyDataBase64.length: [{}]"
                , headerDataBase64.length()
                , bodyDataBase64.length()
        );

        // when
        EncryptedDetail encryptedDetail = this.encryptedBodyAdviceHelper.decryptDataBase64ToBodyDetail(encryptedDataBase64, userDto);

        // then
        log.debug("test_decryptDataBase64ToBodyDetail_basic - encryptedDetail: [{}]"
                        + ""
                , encryptedDetail
        );
        assertThat(encryptedDetail.encryptedInfo()).isEqualTo(encryptedInfo);
        assertThat(new String(encryptedDetail.decryptedData())).isEqualTo(userDtoStr);
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
////        when(oauth2ClientRestTemplate.exchange(anyString()
////                , eq(HttpMethod.GET), eq(HttpEntity.EMPTY), eq(MyKeyDto.class)))
////                .thenReturn(ResponseEntity.ok(prepareMyKeyDto_basic()));
//        doReturn(ResponseEntity.ok(myKeyDto))
//                .when(this.oauth2ClientRestTemplate).exchange(anyString(), eq(HttpMethod.GET)
//                        , ArgumentMatchers.any(HttpEntity.class), eq(MyKeyDto.class))
//        ;
//
//////        doReturn(myKeyDto).when(authService).resolveMyKeyInfo(userId);
////        when(authService.resolveMyKeyInfo(anyString())).thenReturn(myKeyDto);
//
//        // when
//        MyKeyDto myKeyRet = this.encryptedBodyAdviceHelper.resolveMyKeyInfo(userId);
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
    public void test_isEncryptedApiController_basic() {
        // given
        Method methodForTrue = Arrays.stream(EncryptedForTestController.class.getMethods()).findFirst().get();
        MethodParameter methodParameterForTrue = new MethodParameter(methodForTrue, 0);
        Method methodForFalse = Arrays.stream(ProtectedForTestController.class.getMethods()).findFirst().get();
        MethodParameter methodParameterForFalse = new MethodParameter(methodForFalse, 0);

        // when
        boolean resultForTrue = this.encryptedBodyAdviceHelper.isEncryptedApiController(methodParameterForTrue);
        boolean resultForFalse = this.encryptedBodyAdviceHelper.isEncryptedApiController(methodParameterForFalse);

        // then
        log.debug("test_isEncryptedApiController_basic - resultForTrue: [{}]"
                        + ", resultForFalse: [{}]"
                , resultForTrue
                , resultForFalse
        );
        assertThat(resultForTrue).isEqualTo(true);
        assertThat(resultForFalse).isEqualTo(false);
    }

}
