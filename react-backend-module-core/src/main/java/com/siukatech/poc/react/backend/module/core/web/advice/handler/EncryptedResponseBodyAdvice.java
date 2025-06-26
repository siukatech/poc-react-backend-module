package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import com.siukatech.poc.react.backend.module.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.module.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedDetail;
import com.siukatech.poc.react.backend.module.core.business.form.encrypted.EncryptedInfo;
import com.siukatech.poc.react.backend.module.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.module.core.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.module.core.web.advice.helper.EncryptedBodyAdviceHelper;
import com.siukatech.poc.react.backend.module.core.web.context.EncryptedBodyContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Reference:
 * http://www.javabyexamples.com/quick-guide-to-responsebodyadvice-in-spring-mvc
 */
@Slf4j
@Component
@RestControllerAdvice
public class EncryptedResponseBodyAdvice implements ResponseBodyAdvice {

    // Reference:
    // https://stackoverflow.com/a/3561399
    // Prefix "X-" IS NOT a recommendation for custom http header name.
    // Custom header name is just sensible without the "X-" prefix.
    private static final String HEADER_X_DATA_ENC_INFO = "X-Data-Enc-Info";
    private final EncryptedBodyContext encryptedBodyContext;
//    private final UserRepository userRepository;
//    private final ObjectMapper objectMapper;
    private final EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;

    private EncryptedResponseBodyAdvice(EncryptedBodyContext encryptedBodyContext
//            , UserRepository userRepository, ObjectMapper objectMapper
            , EncryptedBodyAdviceHelper encryptedBodyAdviceHelper
    ) {
        this.encryptedBodyContext = encryptedBodyContext;
//        this.userRepository = userRepository;
//        this.objectMapper = objectMapper;
        this.encryptedBodyAdviceHelper = encryptedBodyAdviceHelper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
//        boolean resultFromAnnotation = Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations())
//                .anyMatch(annotation -> annotation.annotationType().equals(EncryptedApiV1Controller.class));
//        //boolean resultFromPath = returnType.getParameter()

//        log.debug("supports - returnType.getMethod.getName: [" + returnType.getMethod().getName()
//                + "], returnType.getMethod.getDeclaringClass.getName: [" + returnType.getMethod().getDeclaringClass().getName()
//                + "], resultFromAnnotation: [" + resultFromAnnotation
//                + "]");
//        Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations()).forEach(annotation -> {
//            log.debug("supports - getDeclaringClass.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(returnType.getMethod().getDeclaringClass().getAnnotations()).forEach(annotation -> {
//            log.debug("supports - getDeclaringClass.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(returnType.getMethod().getDeclaredAnnotations()).forEach(annotation -> {
//            log.debug("supports - getMethod.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(returnType.getMethod().getAnnotations()).forEach(annotation -> {
//            log.debug("supports - getMethod.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });

        boolean resultFromAnnotation = this.encryptedBodyAdviceHelper.isEncryptedApiController(returnType);
        return resultFromAnnotation;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType
            , MediaType selectedContentType, Class selectedConverterType
            , ServerHttpRequest request, ServerHttpResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        log.debug("beforeBodyWrite - returnType.getMethod.getName: [" + returnType.getMethod().getName()
                + "], returnType.getParameterName: [" + returnType.getParameterName()
                + "], selectedContentType.getCharset: [" + (selectedContentType == null ? "NULL" : selectedContentType.getCharset())
                + "], selectedConverterType.getName: [" + selectedConverterType.getName()
                + "], object: [" + body.getClass().getName()
                + "], authentication.getName: [" + (authentication == null ? "NULL" : authentication.getName())
                + "], authentication.isAuthenticated: [" + (authentication == null ? "NULL" : authentication.isAuthenticated())
                + "], userId: [" + userId
                + "]");

//        UserEntity userEntity = this.encryptedBodyContext.getUserEntity();
        MyKeyDto myKeyDto = this.encryptedBodyContext.getMyKeyDto();
        EncryptedDetail encryptedDetail = this.encryptedBodyContext.getEncryptedDetail();
        String encryptedRsaDataBase64 = request.getHeaders().getFirst(HEADER_X_DATA_ENC_INFO);
        EncryptedInfo encryptedInfo = null;
        log.debug("beforeBodyWrite - returnType.getMethod.getName: [" + returnType.getMethod().getName()
//                + "], userEntity.getId: [" + (userEntity == null ? "NULL" : userEntity.getId())
                + "], myKeyDto.getUserId: [" + (myKeyDto == null ? "NULL" : myKeyDto.getUserId())
                + "], encryptedBodyDetail.encryptedInfoModel.key: [" + (encryptedDetail == null ? "NULL" : encryptedDetail.encryptedInfo().key())
                + "], encryptedRsaDataBase64: [" + encryptedRsaDataBase64
                + "]");

        EncryptedBodyContext encryptedBodyContextForDebug = this.resolveEncryptedBodyContext();
        log.debug("beforeBodyWrite - encryptedBodyContextForDebug: [{}]", encryptedBodyContextForDebug);

//        if (userEntity == null) {
//            String finalUserId = userId;
//            userEntity = this.userRepository.findByUserId(userId)
//                    .orElseThrow(() -> new EntityNotFoundException("User not found [" + finalUserId + "]"));
//        }
        if (myKeyDto == null) {
//            myKeyDto = this.encryptedBodyAdviceHelper.resolveMyKeyInfo(userId);
            if (authentication instanceof MyAuthenticationToken myAuthenticationToken) {
                UserDossierDto userDossierDto = myAuthenticationToken.getUserDossierDto();
                myKeyDto = userDossierDto.getMyKeyDto();
            }
        }
        if (encryptedDetail == null) {
//            // should obtain from SecurityContext again
//            userId = userId == null ? "app-user-01" : userId;
            try {
//                encryptedBodyDetail = this.encryptedBodyAdviceHelper
//                        .decryptRsaDataBase64ToBodyDetail(encryptedRsaDataBase64, userEntity);
                encryptedDetail = this.encryptedBodyAdviceHelper.decryptDataBase64ToBodyDetail(
                        encryptedRsaDataBase64
//                        , userEntity
                        , myKeyDto
                );
                encryptedInfo = encryptedDetail.encryptedInfo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
//            userId = userEntity.getUserId();
            encryptedInfo = encryptedDetail.encryptedInfo();
        }

        log.debug("beforeBodyWrite - returnType.getMethod.getName: [" + returnType.getMethod().getName()
                + "], userId: [" + userId
                + "], encryptedInfoModel.key: [" + encryptedInfo.key()
                + "], encryptedInfoModel.iv: [" + encryptedInfo.iv()
                + "]");

        try {
//            String bodyStr = null;
////            ObjectMapper objectMapper = new ObjectMapper();
//            bodyStr = this.objectMapper.writeValueAsString(body);
////            Gson gson = new GsonBuilder()
////                    .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
////                        @Override
////                        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
////                            Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
////                            return LocalDate.ofInstant(instant, ZoneId.systemDefault());
////                        }
////                    })
////                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
////                        @Override
////                        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
////                            Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
////                            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
////                        }
////                    })
////                    .create();
////            bodyStr = gson.toJson(body);
//
////            byte[] encryptedAesData = CryptoUtil.encryptWithAesCbcSecret(bodyStr
////                    , Base64.getDecoder().decode(encryptedInfoModel.key())
////                    , Base64.getDecoder().decode(encryptedInfoModel.iv())
////            );
//            byte[] decodedKey = Base64.getDecoder().decode(encryptedInfoModel.key());
//            //byte[] decodedKey = encryptedInfoModel.key().getBytes(StandardCharsets.UTF_8);
//            byte[] encryptedAesData = CryptoUtil.encryptWithAesEcbSecret(bodyStr
//                    , decodedKey
//            );
//            String encryptedAesDataBase64 = Base64.getEncoder().encodeToString(encryptedAesData);
////            byte[] encryptedRsaData = CryptoUtil.encryptWithRsaPrivateKey(encryptedAesDataBase64, userEntity.getPrivateKey());
////            String encryptedRsaDataBase64 = Base64.getEncoder().encodeToString(encryptedRsaData);
//            log.debug("beforeBodyWrite - bodyStr: [" + bodyStr
//                    + "], decodedKey.length: [" + decodedKey.length
//                    + "], encryptedAesData.length: [" + encryptedAesData.length
//                    + "], encryptedDataBase64: [" + encryptedAesDataBase64
////                    + "], encryptedRsaDataBase64: [" + encryptedRsaDataBase64
//                    + "]");
            String encryptedAesDataBase64 = this.encryptedBodyAdviceHelper
                    .encryptBodyToDataBase64(body, encryptedInfo);
            return encryptedAesDataBase64;
//            return encryptedRsaDataBase64;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public EncryptedBodyContext resolveEncryptedBodyContext() {
        EncryptedBodyContext encryptedBodyContext = (EncryptedBodyContext) RequestContextHolder
                .currentRequestAttributes()
                .getAttribute(
                        EncryptedBodyContext.CONTEXT_NAME, RequestAttributes.SCOPE_REQUEST
                );
        return encryptedBodyContext;
    }

}
