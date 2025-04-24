package com.siukatech.poc.mapper.model;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class SourceObjWithOptional {
    //
    // *** The target property name MUST be same as source property.
    // Here both are "simpleData".
    // If property name is different, then @Mapping is required.
    //
//    private Optional<SimpleData> simpleDataOptional;
    private Optional<SourceSimpleData> simpleData;
    private String name;
    private Integer number;
    private Optional<String> description1;
}
