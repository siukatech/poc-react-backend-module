package com.siukatech.poc.mapper.model;

import lombok.*;

import java.util.Optional;

@Data
@Builder
//@NoArgsConstructor
//@Setter
//@Getter
public class SourceSimpleData {
    private String name;
    private Optional<Integer> number;
}
