package com.siukatech.poc.react.backend.module.core.business.dto;

import lombok.Data;

@Data
//@EqualsAndHashCode(callSuper = true)
public class MyKeyDto extends AbstractDto {
    private String userId;
    private String publicKey;
    private String privateKey;
}
