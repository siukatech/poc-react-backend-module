package com.siukatech.poc.mapper.model;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class TargetObjWithoutOptional {
    //
    // *** The target property name MUST be same as source property.
    // Here both are "simpleData".
    // If property name is different, then @Mapping is required.
    //
    private TargetSimpleData simpleData;
//    private Optional<String> nameOptional;
    private Optional<String> name;
    private Optional<Integer> numberOptional;
    private String description2;
}
