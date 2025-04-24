package com.siukatech.poc.react.backend.module.core.business.mapper;

import org.mapstruct.Named;

import java.util.Optional;

public interface AbstractMapper {

//    @Named("wrapOptional")
    default <T> Optional<T> wrapOptional(T obj) {
        return Optional.ofNullable(obj);
    }

//    @Named("unwrapOptional")
    default <T> T unwrapOptional(Optional<T> objOptional) {
        return objOptional == null ? null : objOptional.orElse(null);
    }

}
