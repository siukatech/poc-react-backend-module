package com.siukatech.poc.react.backend.module.core.caching.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OptionalDeserializer extends JsonDeserializer<Optional<?>> {

    @Override
    public Optional<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // You can customize how to deserialize based on your JSON structure
        // For example, if null represents an empty Optional:
        Object value = p.readValueAs(Object.class); // Read the underlying value
        return Optional.ofNullable(value);
    }
}