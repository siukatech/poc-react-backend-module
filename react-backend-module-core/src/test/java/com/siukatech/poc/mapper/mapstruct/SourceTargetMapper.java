package com.siukatech.poc.mapper.mapstruct;

import com.siukatech.poc.mapper.model.SourceObjWithOptional;
import com.siukatech.poc.mapper.model.TargetObjWithoutOptional;
import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import org.mapstruct.Mapping;

public interface SourceTargetMapper extends AbstractMapper {

    @Mapping(source = "simpleDataOptional", target = "simpleData", qualifiedByName = {"wrapOptional"})
    @Mapping(source = "name", target = "nameOptional", qualifiedByName = {"wrapOptional"})
    @Mapping(source = "description1", target = "description2")
    TargetObjWithoutOptional sourceObjToTargetObj(SourceObjWithOptional sourceObjWithOptional);

    @Mapping(source = "simpleData", target = "simpleDataOptional", qualifiedByName = {"unwrapOptional"})
    @Mapping(source = "nameOptional", target = "name", qualifiedByName = {"wrapOptional"})
    @Mapping(source = "description2", target = "description1")
    SourceObjWithOptional targetObjToSourceObj(TargetObjWithoutOptional targetObjWithoutOptional);
}
