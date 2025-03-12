package com.siukatech.poc.react.backend.module.core.web.advice.mapper;

import com.siukatech.poc.react.backend.module.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.module.core.web.advice.model.ProblemDetailExt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.ProblemDetail;

/**
 * Mapper(componentModel = "spring")
 * Reference:
 * https://stackoverflow.com/a/70438120
 * https://blog.csdn.net/qq_39535439/article/details/131167805
 *
 * https://www.baeldung.com/mapstruct-custom-mapper
 */
//@Component  // This is not working, could not convert the generated class to a spring Component
@Mapper  // This is not working, does not support the @Autowired
//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProblemDetailExtMapper extends AbstractMapper {

    @Mapping(target = "correlationId", ignore = true)
    ProblemDetailExt convertProblemDetailToExtWithoutCorrelationId(ProblemDetail problemDetail);
}
