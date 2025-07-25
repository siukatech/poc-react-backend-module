package com.siukatech.poc.react.backend.module.core.business.mapper;

import org.mapstruct.Named;

import java.util.Optional;

/**
 * The @Named annotation is unnecessary.
 * After the removal of @Named annotation, Mapstruct can generate implementation class correctly.
 */
public interface AbstractMapper {

//    @Named("wrapOptional")
    default <T> Optional<T> wrapOptional(T obj) {
        return Optional.ofNullable(obj);
    }

//    @Named("unwrapOptional")
    default <T> T unwrapOptional(Optional<T> optional) {
        return optional == null ? null : optional.orElse(null);
    }

}
