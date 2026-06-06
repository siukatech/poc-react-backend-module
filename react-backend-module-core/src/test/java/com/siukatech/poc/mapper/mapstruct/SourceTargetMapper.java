package com.siukatech.poc.mapper.mapstruct;

import com.siukatech.poc.mapper.model.SourceObjWithOptional;
import com.siukatech.poc.mapper.model.SourceSimpleData;
import com.siukatech.poc.mapper.model.TargetObjWithoutOptional;
import com.siukatech.poc.mapper.model.TargetSimpleData;
import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.module.core.web.advice.mapper.ProblemDetailExtMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SourceTargetMapper extends AbstractMapper {

    SourceTargetMapper INSTANCE = Mappers.getMapper(SourceTargetMapper.class);

    TargetSimpleData toTargetSimpleData(SourceSimpleData sourceSimpleData);

    SourceSimpleData toSourceSimpleData(TargetSimpleData targetSimpleData);

////    @Mapping(source = "simpleDataOptional", target = "simpleData", qualifiedByName = {"wrapOptional"})
////    @Mapping(source = "name", target = "nameOptional", qualifiedByName = {"wrapOptional"})
    @Mapping(source = "number", target = "numberOptional")
    @Mapping(source = "description1", target = "description2")
    TargetObjWithoutOptional toTargetObjWithOptional(SourceObjWithOptional sourceObjWithOptional);

////    @Mapping(source = "simpleData", target = "simpleDataOptional", qualifiedByName = {"unwrapOptional"})
////    @Mapping(source = "nameOptional", target = "name", qualifiedByName = {"wrapOptional"})
    @Mapping(source = "numberOptional", target = "number")
    @Mapping(source = "description2", target = "description1")
    SourceObjWithOptional toSourceObjWithOptional(TargetObjWithoutOptional targetObjWithoutOptional);

}
