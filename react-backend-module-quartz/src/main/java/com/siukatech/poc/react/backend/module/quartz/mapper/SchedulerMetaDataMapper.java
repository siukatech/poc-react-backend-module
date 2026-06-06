package com.siukatech.poc.react.backend.module.quartz.mapper;

import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.module.quartz.model.SchedulerInfoRec;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.quartz.SchedulerMetaData;

@Mapper
public interface SchedulerMetaDataMapper extends AbstractMapper {

    SchedulerMetaDataMapper INSTANCE = Mappers.getMapper(SchedulerMetaDataMapper.class);

    SchedulerInfoRec convertMetaDataToInfoRec(SchedulerMetaData schedulerMetaData);

}

