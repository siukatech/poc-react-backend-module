package com.siukatech.poc.react.backend.module.core.business.form.encrypted;

public record EncryptedDetail(EncryptedReq encryptedReq
        , EncryptedInfo encryptedInfo, byte[] decryptedData) {
}
