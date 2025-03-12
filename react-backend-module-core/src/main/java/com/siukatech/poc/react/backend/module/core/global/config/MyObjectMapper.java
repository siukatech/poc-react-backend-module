package com.siukatech.poc.react.backend.module.core.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Deprecated
public class MyObjectMapper extends ObjectMapper {

    public MyObjectMapper() {
        super();
        this.registerModule(new JavaTimeModule());

        log.debug("MyObjectMapper - getDeserializationConfig.getDeserializationFeatures - 1: [{}]"
                , getDeserializationConfig().getDeserializationFeatures()
        );

        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // here is configured for encrypted data in EncryptedRequestBodyAdvice and EncryptedResponseBodyAdvice
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // ignore unknown json properties to prevent HttpMessageNotReadableException
        // https://stackoverflow.com/a/5455563
//        disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
//        configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);

        log.debug("MyObjectMapper - getDeserializationConfig.getDeserializationFeatures - 2: [{}]"
                , getDeserializationConfig().getDeserializationFeatures()
        );
        configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        log.debug("MyObjectMapper - getDeserializationConfig.getDeserializationFeatures - 3: [{}]"
                , getDeserializationConfig().getDeserializationFeatures()
        );
    }

    @Override
    public ObjectReader reader() {
        ObjectReader reader = super.reader();
        log.debug("reader - reader.getConfig.getDeserializationFeatures - 1: [{}]"
                , reader.getConfig().getDeserializationFeatures()
        );
        reader = reader.without(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        reader = reader.without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        log.debug("reader - reader.getConfig.getDeserializationFeatures - 2: [{}]"
                , reader.getConfig().getDeserializationFeatures()
        );
        return reader;
    }

    @Override
    public <T> T readValue(JsonParser p, JavaType valueType) throws IOException {
        log.debug("readValue - valueType: [{}]", valueType.getClass().getName());
        return super.readValue(p, valueType);
    }

}
