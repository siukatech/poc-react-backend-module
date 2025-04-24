package com.siukatech.poc.mapper.model;

import lombok.*;

import java.util.Optional;

@Data
@Builder
//@NoArgsConstructor
//@Setter
//@Getter
public class TargetSimpleData {
    private Optional<String> name;
    private Integer number;
}
