package com.siukatech.poc.react.backend.module.core.business.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
//@EqualsAndHashCode(callSuper = true)
public class UserDto extends AbstractDto {
    private String id;
    private String userId;
    private String name;
    private String publicKey;
//    private String privateKey;
    private String createdBy;
    private LocalDateTime createdDatetime;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDatetime;
}

