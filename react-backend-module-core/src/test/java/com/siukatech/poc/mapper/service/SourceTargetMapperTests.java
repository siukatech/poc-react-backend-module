package com.siukatech.poc.mapper.service;

import com.siukatech.poc.mapper.mapstruct.SourceTargetMapper;
import com.siukatech.poc.mapper.model.SourceSimpleData;
import com.siukatech.poc.mapper.model.SourceObjWithOptional;
import com.siukatech.poc.mapper.model.TargetObjWithoutOptional;
import com.siukatech.poc.mapper.model.TargetSimpleData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Slf4j
public class SourceTargetMapperTests {

    @Test
    void test_simpleData_basic() {
        SourceTargetMapper sourceTargetMapper = Mappers.getMapper(SourceTargetMapper.class);

        SourceSimpleData sourceSimpleData =
//                new SourceSimpleData();
//        sourceSimpleData.setName("source-simple-data");
//        sourceSimpleData.setNumber(Optional.of(50));
                SourceSimpleData.builder()
                        .name("source-simple-data")
                        .number(Optional.of(50))
                        .build();
        TargetSimpleData targetSimpleData = sourceTargetMapper.toTargetSimpleData(sourceSimpleData);
        SourceSimpleData sourceSimpleData2 = sourceTargetMapper.toSourceSimpleData(targetSimpleData);
        log.info("test_simpleData_basic - targetSimpleData: [{}]", targetSimpleData);
        log.info("test_simpleData_basic - sourceSimpleData2: [{}]", sourceSimpleData2);
    }

    @Test
    void test_objWithOptional_basic() {
        SourceTargetMapper sourceTargetMapper = Mappers.getMapper(SourceTargetMapper.class);

        SourceSimpleData sourceSimpleData =
                SourceSimpleData.builder()
                .name("source-simple-data")
                .number(Optional.of(50))
                .build();
//                new SourceSimpleData();
//        sourceSimpleData.setName("source-simple-data");
//        sourceSimpleData.setNumber(Optional.of(50));
        SourceObjWithOptional sourceObjWithOptional = SourceObjWithOptional.builder()
//                .simpleDataOptional(Optional.of(simpleData))
                .simpleData(Optional.of(sourceSimpleData))
                .name("name")
                .number(70)
                .description1(Optional.of("description1"))
                .build();

        TargetObjWithoutOptional targetObjWithoutOptional = sourceTargetMapper.toTargetObjWithOptional(sourceObjWithOptional);
        SourceObjWithOptional sourceObjWithOptional1 = sourceTargetMapper.toSourceObjWithOptional(targetObjWithoutOptional);
        log.info("test_wrapOptional_basic - targetObjWithoutOptional: [{}]", targetObjWithoutOptional);
        log.info("test_wrapOptional_basic - sourceObjWithOptional1: [{}]", sourceObjWithOptional1);

    }

}
