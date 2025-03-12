package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedDetail;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedInfo;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.module.core.web.advice.helper.EncryptedBodyAdviceHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Reference:
 * https://medium.com/javarevisited/requestbodyadvice-implementation-springboot-cc687a915646
 */
@Slf4j
@Component
@RestControllerAdvice
public class EncryptedRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final EncryptedBodyContext encryptedBodyContext;
    //    private final UserRepository userRepository;
//    private final ObjectMapper objectMapper;
    private final EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;

    private EncryptedRequestBodyAdvice(EncryptedBodyContext encryptedBodyContext
//            , UserRepository userRepository, ObjectMapper objectMapper
            , EncryptedBodyAdviceHelper encryptedBodyAdviceHelper
    ) {
        this.encryptedBodyContext = encryptedBodyContext;
//        this.userRepository = userRepository;
//        this.objectMapper = objectMapper;
        this.encryptedBodyAdviceHelper = encryptedBodyAdviceHelper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType
            , Class<? extends HttpMessageConverter<?>> converterType) {
        //return false;
////        boolean resultFromAnnotation = Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations())
////                .filter(annotation -> annotation.annotationType().equals(EncryptedApiV1Controller.class)).count() > 0;
//        boolean resultFromAnnotation = Arrays.stream(methodParameter.getMethod().getDeclaringClass().getDeclaredAnnotations())
//                .anyMatch(annotation -> annotation.annotationType().equals(EncryptedApiV1Controller.class));
//        //boolean resultFromPath = methodParameter.getParameter()
//        log.debug("supports - methodParameter.getMethod.getName: [" + methodParameter.getMethod().getName()
//                + "], methodParameter.getParameterType.getName: [" + methodParameter.getParameterType().getName()
//                + "], targetType.getTypeName: [" + targetType.getTypeName()
//                + "], methodParameter.getMember.getName: [" + methodParameter.getMember().getName()
////                + "], methodParameter.getConstructor.getName: [" + methodParameter.getConstructor().getName()
//                + "], methodParameter.getMethod.getDeclaringClass.getName: [" + methodParameter.getMethod().getDeclaringClass().getName()
//                + "], resultFromAnnotation: [" + resultFromAnnotation
//                + "]");
//        Arrays.stream(methodParameter.getMethod().getDeclaringClass().getDeclaredAnnotations()).forEach(annotation -> {
//            log.debug("supports - getDeclaringClass.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(methodParameter.getMethod().getDeclaringClass().getAnnotations()).forEach(annotation -> {
//            log.debug("supports - getDeclaringClass.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(methodParameter.getMethod().getDeclaredAnnotations()).forEach(annotation -> {
//            log.debug("supports - getMethod.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(methodParameter.getMethod().getAnnotations()).forEach(annotation -> {
//            log.debug("supports - getMethod.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
        boolean resultFromAnnotation = this.encryptedBodyAdviceHelper.isEncryptedApiController(methodParameter);
        return resultFromAnnotation;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage
            , MethodParameter methodParameter, Type targetType
            , Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
//        String finalUserId = userId;
//        UserEntity userEntity = this.userRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("User not found [" + finalUserId + "]"));
        UserDossierDto userDossierDto = null;
        MyKeyDto myKeyDto = null;
//        myKeyDto = this.encryptedBodyAdviceHelper.resolveMyKeyInfo(userId);
        if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
            userDossierDto = myAuthenticationToken.getUserDossierDto();
            myKeyDto = userDossierDto.getMyKeyDto();
        }

        log.debug("beforeBodyRead - methodParameter.getMethod.getName: [" + methodParameter.getMethod().getName()
                + "], methodParameter.getParameterType.getName: [" + methodParameter.getParameterType().getName()
                + "], userId: [" + userId
//                + "], userEntity.getUserId: [" + userEntity.getUserId()
                + "], myKeyDto.getUserId: [" + (myKeyDto == null ? "NULL" : myKeyDto.getUserId())
                + "], authentication.getName: [" + (authentication == null ? "NULL" : authentication.getName())
                + "], authentication.isAuthenticated: [" + (authentication == null ? "NULL" : authentication.isAuthenticated())
                + "]");
        //return inputMessage;

        try (InputStream inputStream = inputMessage.getBody()) {
            byte[] body = StreamUtils.copyToByteArray(inputStream);
//            String encryptedRsaDataBase64 = new String(body);
////            log.debug("beforeBodyRead - userId: [" + userId
////                    + "], start");
////            byte[] decryptedBodyData = CryptoUtil.decryptWithRsaPrivateKey(
////                    Base64.getDecoder().decode(encryptedRsaDataBase64)
////                    , userEntity.getPrivateKey()
////            );
////            String decryptedBodyStr = new String(decryptedBodyData);
////            log.debug("beforeBodyRead - encryptedRsaDataBase64: [" + encryptedRsaDataBase64
////                    + "], decryptedBodyStr: [" + decryptedBodyStr
////                    + "]");
////            EncryptedRequestModel encryptedRequestModel = null;
//////            ObjectMapper objectMapper = new ObjectMapper();
////            encryptedRequestModel = this.objectMapper
////                    .readValue(decryptedBodyStr, EncryptedRequestModel.class);
//////            Gson gson = new Gson();
//////            encryptedRequestModel = gson
//////                    .fromJson(decryptedBodyStr, EncryptedRequestModel.class);
////            EncryptedInfoModel encryptedInfoModel = encryptedRequestModel.info();
////
//////            byte[] aesKey = CryptoUtil.decryptWithRsaPrivateKey(
//////                    Base64.getDecoder().decode(encryptedRequestModel.key())
//////                    , userEntity.getPrivateKey()
//////            );
//////            byte[] decryptedBody = CryptoUtil.decryptWithAesKey(
//////                    Base64.getDecoder().decode(encryptedRequestModel.cipher())
//////                    , aesKey);
////            byte[] decryptedData = CryptoUtil.decryptWithAesCbcSecret(
////                    Base64.getDecoder().decode(encryptedRequestModel.cipher())
//////                    , aesKey
////                    , Base64.getDecoder().decode(encryptedInfoModel.key())
////                    , Base64.getDecoder().decode(encryptedInfoModel.iv())
////            );
//            EncryptedBodyDetail encryptedBodyDetail = this.encryptedBodyAdviceHelper
//                    .decryptRsaDataBase64ToBodyDetail(encryptedRsaDataBase64, userEntity);
//            EncryptedRequestModel encryptedRequestModel = encryptedBodyDetail.encryptedRequestModel();

            String encryptedRequestBody = new String(body);
            EncryptedDetail encryptedDetail = this.encryptedBodyAdviceHelper
                    .decryptDataBase64ToBodyDetail(encryptedRequestBody
//                            , userEntity
                            , myKeyDto
                    );

            EncryptedInfo encryptedInfo = encryptedDetail.encryptedInfo();
            byte[] decryptedData = encryptedDetail.decryptedData();
            String decryptedDataStr = new String(decryptedData);

            log.debug("beforeBodyRead - userId: [" + userId
//                    + "], encryptedRequestModel.cipher: [" + encryptedRequestModel.cipher()
                    + "], encryptedInfoModel.key: [" + encryptedInfo.key()
                    + "], encryptedInfoModel.iv: [" + encryptedInfo.iv()
//                    + "], aesKey: [" + new String(aesKey)
                    + "], decryptedDataStr: [" + decryptedDataStr
                    + "]");

//            this.encryptedBodyContext.setUserEntity(userEntity);
            this.encryptedBodyContext.setMyKeyDto(myKeyDto);
            this.encryptedBodyContext.setEncryptedDetail(encryptedDetail);

//            byte[] decryptedData = body;
//            log.debug("beforeBodyRead - body: [" + new String(decryptedData) + "]");
            DecodeHttpInputMessage decodeHttpInputMessage =
                    new DecodeHttpInputMessage(inputMessage.getHeaders()
                            , new ByteArrayInputStream(decryptedData)
                    );
            return decodeHttpInputMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage
            , MethodParameter methodParameter, Type targetType
            , Class<? extends HttpMessageConverter<?>> converterType) {
        //Map<String, String> input = (Map<String, String>) body;
        //log.debug("afterBodyRead - input: [" + input + "]");
        log.debug("afterBodyRead - methodParameter.getMethod.getName: [" + methodParameter.getMethod().getName()
                + "], methodParameter.getParameterType.getName: [" + methodParameter.getParameterType().getName()
                + "]");
        return body;
    }

    @Override
    public Object handleEmptyBody(@Nullable Object body, HttpInputMessage inputMessage
            , MethodParameter methodParameter, Type targetType
            , Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("handleEmptyBody - methodParameter.getMethod.getName: [" + methodParameter.getMethod().getName()
                + "], methodParameter.getParameterType.getName: [" + methodParameter.getParameterType().getName()
                + "]");
        return body;
    }
}
