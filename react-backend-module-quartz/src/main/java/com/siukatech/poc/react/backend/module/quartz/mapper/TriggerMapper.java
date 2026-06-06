package com.siukatech.poc.react.backend.module.quartz.mapper;

import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.module.quartz.model.TriggerKeyRec;
import com.siukatech.poc.react.backend.module.quartz.model.TriggerRec;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

@Mapper(uses = {JobKeyMapper.class})
public interface TriggerMapper extends AbstractMapper {

    TriggerMapper INSTANCE = Mappers.getMapper(TriggerMapper.class);

    @Named("triggerKey")
    TriggerKeyRec convertKeyToKeyRec(TriggerKey triggerKey);

//    @Mapping(source = "mayFirstAgain", target = "mayFirstAgain")
    @Mapping(source = "jobKey", target = "jobKeyRec", qualifiedByName = {"jobKey"})
    @Mapping(source = "key", target = "triggerKeyRec", qualifiedByName = {"triggerKey"})
    TriggerRec convertSrcToRec(Trigger trigger);

}
