package com.siukatech.poc.react.backend.module.core.caching.service;

import com.siukatech.poc.react.backend.module.core.caching.config.DefaultCachingConfig;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressModel;
import com.siukatech.poc.react.backend.module.core.caching.model.AddressOptionalModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AddressService {

    private Map<String, AddressModel> addressModelMap = new HashMap<>();

    public void saveAddressModel(@NonNull AddressModel addressModel) {
        if (addressModel.getId() == null) addressModel.setId(UUID.randomUUID().toString());
        this.addressModelMap.put(addressModel.getId(), addressModel);
    }

    public void printAddressModelMap() {
        log.debug("printAddressModelMap - addressModelMap: [{}]", this.addressModelMap);
    }

    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_DEFAULT}, key = "#addressId")
    public AddressModel getAddressModelById(String addressId, String label) {
        log.debug("getAddressModelById - label: [{}], addressId: [{}]", label, addressId);
        AddressModel addressModel = this.addressModelMap.get(addressId);
        return addressModel;
    }

    @CacheEvict(value = {DefaultCachingConfig.CACHE_NAME_DEFAULT}, allEntries = true)
    public void evictAllCacheValues() {}

    @Cacheable(value = {DefaultCachingConfig.CACHE_NAME_DEFAULT}, key = "'addressOptionalList'")
    public List<AddressOptionalModel> getAddressOptionalModelList(String label) {
        log.debug("getAddressOptionalModelList - label: [{}]", label);
        List<AddressOptionalModel> addressOptionalList = this.addressModelMap
                .values().stream().map(v -> {
                    AddressOptionalModel addressOptionalModel = new AddressOptionalModel(
                            Optional.of(v.getId())
                            , Optional.of(v.getLocation())
                            , Optional.of(v.getStreet())
                            , Optional.of(v.getDistrict())
                    );
                    return addressOptionalModel;
                })
                .collect(Collectors.toList())
                ;
        return addressOptionalList;
    }

}
