package com.siukatech.poc.mapper.model;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class SourceObjWithOptional {
    private Optional<SimpleData> simpleDataOptional;
    private String name;
    private String description1;
}
