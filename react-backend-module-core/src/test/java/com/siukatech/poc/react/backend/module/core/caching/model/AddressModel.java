package com.siukatech.poc.react.backend.module.core.caching.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel implements Serializable {

    private String id;
    private String location;
    private String street;
    private String district;

}
