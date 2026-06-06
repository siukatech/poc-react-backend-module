package com.siukatech.poc.react.backend.module.quartz.mapper;

import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.module.quartz.model.JobKeyRec;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.quartz.JobKey;

@Mapper
public interface JobKeyMapper extends AbstractMapper {

    JobKeyMapper INSTANCE = Mappers.getMapper(JobKeyMapper.class);

    @Named("jobKey")
    JobKeyRec convertSrcToRec(JobKey jobKey);

}
