package com.siukatech.poc.mapper.model;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class TargetObjWithoutOptional {
    private SimpleData simpleData;
    private Optional<String> nameOptional;
    private String description2;
}
