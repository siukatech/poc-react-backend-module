package com.siukatech.poc.react.backend.module.core.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MapperConfig {

    /**
     * Reference:
     * https://stackoverflow.com/a/67973800
     * https://github.com/FasterXML/jackson-databind/issues/3262#issuecomment-909008472
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
//                .json()
//                .build();
//        objectMapper().registerModule(new JavaTimeModule());

        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())

                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
//                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)

                // here is configured for encrypted data in EncryptedRequestBodyAdvice and EncryptedResponseBodyAdvice
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

                // ignore unknown json properties to prevent HttpMessageNotReadableException
                // https://stackoverflow.com/a/5455563
//                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                .build();

//        MyObjectMapper objectMapper = new MyObjectMapper();

        return objectMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        //
        // Prevent modelMapper matches multiple source property hierarchies
        // Reference:
        // https://stackoverflow.com/a/59215139
        // https://stackoverflow.com/a/61692064
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }


}
