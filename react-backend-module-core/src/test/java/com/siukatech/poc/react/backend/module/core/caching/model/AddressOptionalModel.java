package com.siukatech.poc.react.backend.module.core.caching.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressOptionalModel implements Serializable {

    private Optional<String> id;
    private Optional<String> location;
    private Optional<String> street;
    private Optional<String> district;

}
