package com.siukatech.poc.react.backend.module.core.util;

import com.siukatech.poc.react.backend.module.core.AbstractUnitTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

////@Slf4j
//@SpringBootTest()
//public class EncryptionUtilTests extends ReactBackendAppCorelicationTests {
public class EncryptionUtilsTests extends AbstractUnitTests {

//    private static final Logger log = LoggerFactory.getLogger(EncryptionUtilsTests.class);

//    @Test
    void contextLoads() {
    }


    /**
     * https://stackoverflow.com/a/44474607
     * openssl genrsa -out private_pkcs8.key 2048
     * openssl rsa -in private_pkcs8.key -pubout -out public_x509.crt
     *
     * @throws Exception
     */
    @Test
    void test_encryptionUtils_rsaKeyPair() throws Exception {
        KeyPair keyPair = EncryptionUtils.generateRsaKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
//        System.out.println("test_encryptionUtils_rsaKeyPair - privateKeyBase64: [" + privateKeyBase64 + "], publicKeyBase64: [" + publicKeyBase64 + "]");
        log.debug("test_encryptionUtils_rsaKeyPair - privateKeyBase64: [" + privateKeyBase64 + "], publicKeyBase64: [" + publicKeyBase64 + "]");
        PrivateKey privateKeyAfter = EncryptionUtils.getRsaPrivateKey(privateKeyBase64);
        PublicKey publicKeyAfter = EncryptionUtils.getRsaPublicKey(publicKeyBase64);
        assertEquals("private key should be equal", new String(privateKey.getEncoded()), new String(privateKeyAfter.getEncoded()));
        assertEquals("public key should be equal", new String(publicKey.getEncoded()), new String(publicKeyAfter.getEncoded()));

        String text = "This is a test";
        byte[] encryptedData = EncryptionUtils.encryptWithRsaPublicKey(text, publicKeyBase64);
        byte[] decryptedData = EncryptionUtils.decryptWithRsaPrivateKey(encryptedData, privateKeyBase64);
        String textAfter = new String(decryptedData);
        assertEquals("texts should be same", text, textAfter);
    }

    @Test
    void test_cryptoUtil_rsaContent_1() throws Exception {
        String publicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn83aiRingPf7agaWIPHsOonO5kcecJzB/mZkcwtTGim1jLtfB+RyO1nqsnKlhI6QpOE3+X3/0wdJSFStk3nRO6KeXx/m4rX/Hs/oijV0cwM07cEg1FtAWXJ5uy+gq57EJmGjRTer03B3SXfev1Z6ujFTrOHBblCXeTit1Zd4TjIfZ30YjT34iqBLT8/fawgj0K0HEybX+yZpoGNIqGxnyrK2RWf5R4yN621u9a+lsLj3fEjVzCx4jhhwzLEgw6wC4T0ns+sRl7Q2CIT1ETfeRgSZeAQomZtnkYgYVe/kmKQxzK5qeB+62ZsfYDVdo6VfgaV/EMnV5382Plt/oRQITQIDAQAB";
        String privateKeyBase64 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfzdqJGKeA9/tqBpYg8ew6ic7mRx5wnMH+ZmRzC1MaKbWMu18H5HI7WeqycqWEjpCk4Tf5ff/TB0lIVK2TedE7op5fH+bitf8ez+iKNXRzAzTtwSDUW0BZcnm7L6CrnsQmYaNFN6vTcHdJd96/Vnq6MVOs4cFuUJd5OK3Vl3hOMh9nfRiNPfiKoEtPz99rCCPQrQcTJtf7JmmgY0iobGfKsrZFZ/lHjI3rbW71r6WwuPd8SNXMLHiOGHDMsSDDrALhPSez6xGXtDYIhPURN95GBJl4BCiZm2eRiBhV7+SYpDHMrmp4H7rZmx9gNV2jpV+BpX8QydXnfzY+W3+hFAhNAgMBAAECggEAKgT2aCMn9EUAgk4ODsaeH3xZ/3oG2oSVi/+/fPKm3NPHzaP9JnWexdVsXZTAyfy0su4Xik5jxMj/L3o4k/Utm7k0XH3md4l0dlUj56eLoaxfr/6e08fUwVBVCu/IlyftmswEovAzCatialqPIw+pwKi0wFoyQwAE/FZ3exjThPfFElFAjIIbg7H58xvnlIDYlnXo50BroH6HsyW3b5ymSr/FOjuQZTeFnZcxzpQZ6TVrvfhYkUW4gscvP2Q/RNVOHT2WJBXp12OQObG7dxfKjBkxa6NZ/KVPD937XJEDoRg1TfmeD/OuW8sXQIass+JzDN/TjuvAl2pP5i6lUoPefwKBgQDgXZHL0g2QFxt6lp1rxM3tXhML+3Huna+yiylcNMmMcsiheVVeIt6BmnahxHmx20xc9wja/ixHOczqG0cHX7H4Qk02BJCNfLugAHOZ82pj9H9Lr+8ydj4ntdXBICTVJVODgsWDKhTfMC7O61qv8/XrwuKVY+wHMUaS2oEV3G7E2wKBgQC2VfP2an+Q5hA9qoOxyritb9afpXCMNOIYqsO1ZEvixdKvFoCphtyyhQVmEznaLGJgT201CX5MUIicuQr7xkw5RjswkP7IkhASNqfe+k8GQLzZTQkxpmEYDqptN9KmEBbZaXT4L/D1xV/wWJmQEC922F3uuPY1KzIxktVghKMb9wKBgQDKsd3QfEEChs2tbzpirRa5Nf8RGSVxxAWB0JUaFxpqhxxYPOxxsiDAh5JbTMwAcTVI22ilp3DLHB3S9beyorJ+rS9h+Le7Cw/aWe4WDEF2ceE6uIPpW8eFSpIE+owr/5+2NMNJXxAiHj57c0anpUfrqVHYUsBeMxcl/r8Vx+JOdwKBgEljayBBWTru/pZzbTJAT/iuzQnQwu4L0vzurnrx9YE+8j+6pOqW93l7BONC1cQ0nRv6nA/+1DDPMU0zRXJ1K/TZibVQhtwfvgw2p3ad9PSlVd14njy3pNjT/lCbaVOhojC7u3KxiSDC4oyQfK55dUE2cQhGtgJMcpV1biC9AhGdAoGAE76S2yrL9IKBfmfipeoGY+7Bb9N5ryrAyP0u7oNDZBqEtTrAM6rxt7hBzjgXqwqG5g17pTfQTtFb+UozRXVXA/9z3zCyosVdHcoGSsQzyQqivKfRPE0dVS6mTuLGlhyu7GD8mWIMytC/f+xLmB/uHc36RX/z51F5B1em0uHyrPI=";
        String dataStr = "B8TpBWx+FykRoNAmCzb7Jo5to+MpoFINA4ZqYmY/muHx6OeDLvLk9inyGLMclltC0xNOf1MoVYsk5AV4uNjVDNRHOOZzFk89CtJ/mw8i3Gdapk0onR8yIdzjVwliQzTtHOR6JcHSMoL39CdBu5oCctUQR7ZDjyls0qjqlnxpUIDclFMyTLebSMdAqQzCjIccs4vLC5VAteFOALpk/Yje3T0Bq5hXSonc1Z3UJgCxLNvrIl+qLJDpLlylelCOMD+ERlK7g75C6305M4LKguQBf+TqXD36QdwNCXpJwMCp+B2Wp+kr/dp4rL6LGdTHGXdUH6vGKRur6waZtz7UEqVJ+w==";
        dataStr = "CH/1IKmYog+cQkUb3ta/plmEQy7rfEpDf1xoFV/OqHmmRlAAPb9gpihufEkDIAreDR3vt1zObm0SGKJnfVSGeGEQqGCHCH6jcG9XIj6TFuShvSvVizkxcSwtXZBYI8czI5Hak85ecWuomthEUC7BeeBKXXCS0jCjRbYerglVh+UNhcw+IWVJjopcbjo+3LDjC+ggMojHAGMPpxhHJecnAK7REApCftS1imCy5egn12a5brTPYTrcNDAljmQ1FP4nhxowwwkWdxEIxz6FRtrcHDxxRuTUQ085LCpIWhtshhGkSfe2Ee/bHOVptXqeJ+hI/EN0Qxt5So+Rsbh9+ts7vQ==";
        byte[] decryptedData = EncryptionUtils.decryptWithRsaPrivateKey(Base64.getDecoder().decode(dataStr), privateKeyBase64);
        String decryptedStr = new String(decryptedData);
//        System.out.println("test_cryptoUtil_rsaContent_1 - decryptedStr: [" + decryptedStr + "]");
        log.debug("test_cryptoUtil_rsaContent_1 - decryptedStr: [" + decryptedStr + "]");
        assertThat(decryptedStr).contains("key");
        assertThat(decryptedStr).contains("cipher");
    }

    @Test
    void test_cryptoUtil_rsaContent_2() throws Exception {
        String publicKeyBase64 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn83aiRingPf7agaWIPHsOonO5kcecJzB/mZkcwtTGim1jLtfB+RyO1nqsnKlhI6QpOE3+X3/0wdJSFStk3nRO6KeXx/m4rX/Hs/oijV0cwM07cEg1FtAWXJ5uy+gq57EJmGjRTer03B3SXfev1Z6ujFTrOHBblCXeTit1Zd4TjIfZ30YjT34iqBLT8/fawgj0K0HEybX+yZpoGNIqGxnyrK2RWf5R4yN621u9a+lsLj3fEjVzCx4jhhwzLEgw6wC4T0ns+sRl7Q2CIT1ETfeRgSZeAQomZtnkYgYVe/kmKQxzK5qeB+62ZsfYDVdo6VfgaV/EMnV5382Plt/oRQITQIDAQAB";
        String privateKeyBase64 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfzdqJGKeA9/tqBpYg8ew6ic7mRx5wnMH+ZmRzC1MaKbWMu18H5HI7WeqycqWEjpCk4Tf5ff/TB0lIVK2TedE7op5fH+bitf8ez+iKNXRzAzTtwSDUW0BZcnm7L6CrnsQmYaNFN6vTcHdJd96/Vnq6MVOs4cFuUJd5OK3Vl3hOMh9nfRiNPfiKoEtPz99rCCPQrQcTJtf7JmmgY0iobGfKsrZFZ/lHjI3rbW71r6WwuPd8SNXMLHiOGHDMsSDDrALhPSez6xGXtDYIhPURN95GBJl4BCiZm2eRiBhV7+SYpDHMrmp4H7rZmx9gNV2jpV+BpX8QydXnfzY+W3+hFAhNAgMBAAECggEAKgT2aCMn9EUAgk4ODsaeH3xZ/3oG2oSVi/+/fPKm3NPHzaP9JnWexdVsXZTAyfy0su4Xik5jxMj/L3o4k/Utm7k0XH3md4l0dlUj56eLoaxfr/6e08fUwVBVCu/IlyftmswEovAzCatialqPIw+pwKi0wFoyQwAE/FZ3exjThPfFElFAjIIbg7H58xvnlIDYlnXo50BroH6HsyW3b5ymSr/FOjuQZTeFnZcxzpQZ6TVrvfhYkUW4gscvP2Q/RNVOHT2WJBXp12OQObG7dxfKjBkxa6NZ/KVPD937XJEDoRg1TfmeD/OuW8sXQIass+JzDN/TjuvAl2pP5i6lUoPefwKBgQDgXZHL0g2QFxt6lp1rxM3tXhML+3Huna+yiylcNMmMcsiheVVeIt6BmnahxHmx20xc9wja/ixHOczqG0cHX7H4Qk02BJCNfLugAHOZ82pj9H9Lr+8ydj4ntdXBICTVJVODgsWDKhTfMC7O61qv8/XrwuKVY+wHMUaS2oEV3G7E2wKBgQC2VfP2an+Q5hA9qoOxyritb9afpXCMNOIYqsO1ZEvixdKvFoCphtyyhQVmEznaLGJgT201CX5MUIicuQr7xkw5RjswkP7IkhASNqfe+k8GQLzZTQkxpmEYDqptN9KmEBbZaXT4L/D1xV/wWJmQEC922F3uuPY1KzIxktVghKMb9wKBgQDKsd3QfEEChs2tbzpirRa5Nf8RGSVxxAWB0JUaFxpqhxxYPOxxsiDAh5JbTMwAcTVI22ilp3DLHB3S9beyorJ+rS9h+Le7Cw/aWe4WDEF2ceE6uIPpW8eFSpIE+owr/5+2NMNJXxAiHj57c0anpUfrqVHYUsBeMxcl/r8Vx+JOdwKBgEljayBBWTru/pZzbTJAT/iuzQnQwu4L0vzurnrx9YE+8j+6pOqW93l7BONC1cQ0nRv6nA/+1DDPMU0zRXJ1K/TZibVQhtwfvgw2p3ad9PSlVd14njy3pNjT/lCbaVOhojC7u3KxiSDC4oyQfK55dUE2cQhGtgJMcpV1biC9AhGdAoGAE76S2yrL9IKBfmfipeoGY+7Bb9N5ryrAyP0u7oNDZBqEtTrAM6rxt7hBzjgXqwqG5g17pTfQTtFb+UozRXVXA/9z3zCyosVdHcoGSsQzyQqivKfRPE0dVS6mTuLGlhyu7GD8mWIMytC/f+xLmB/uHc36RX/z51F5B1em0uHyrPI=";
        String dataStr = "{\"versionNo\":null,\"id\":40,\"title\":null,\"purchasedDate\":null,\"createdBy\":null,\"createdDatetime\":null,\"lastModifiedBy\":null,\"lastModifiedDatetime\":null}";
        byte[] encryptedData = EncryptionUtils.encryptWithRsaPrivateKey(dataStr, privateKeyBase64);
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
        //
        byte[] decryptedData = EncryptionUtils.decryptWithRsaPublicKey(encryptedData, publicKeyBase64);
        String decryptedStr = new String(decryptedData);
        //
//        System.out.println("test_cryptoUtil_rsaContent_2 - encryptedDataBase64: [" + encryptedDataBase64 + "]");
//        System.out.println("test_cryptoUtil_rsaContent_2 - decryptedStr: [" + decryptedStr + "]");
        log.debug("test_cryptoUtil_rsaContent_2 - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_rsaContent_2 - decryptedStr: [" + decryptedStr + "]");
        assertThat(decryptedStr).contains("versionNo");
        assertThat(decryptedStr).contains("purchasedDate");
    }

    @Test
    void test_cryptoUtil_aesContent_cbc() throws Exception {
        String dataStr = "{\"toyId\":\"40\"}";  //ObL3ky7/l2JxNK4QXaQqJw==
        String keyBase64 = "m7NSVjUpKFDcQ9t0wVBa7jQ21xH24u1huA0EQvqXQpU=";
        byte[] key = Base64.getDecoder().decode(keyBase64);
        String ivBase64 = "ZO2vTWmBh1/JD76ZO5e53g==";
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        byte[] encryptedData = EncryptionUtils.encryptWithAesCbcSecret(dataStr, key, iv);
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
        //
        byte[] data = Base64.getDecoder().decode(encryptedDataBase64);
        byte[] decryptedData = EncryptionUtils.decryptWithAesCbcSecret(data, key, iv);
        String decryptedStr = new String(decryptedData);
        //
//        System.out.println("test_cryptoUtil_aesContent_cbc - dataStr: [" + dataStr
//                + "], decryptedStr: [" + decryptedStr
//                + "]");
        log.debug("test_cryptoUtil_aesContent_cbc - key.length: [" + key.length + "]");
        log.debug("test_cryptoUtil_aesContent_cbc - iv.length: [" + iv.length + "]");
        log.debug("test_cryptoUtil_aesContent_cbc - dataStr: [" + dataStr
                + "], decryptedStr: [" + decryptedStr
                + "]");
        assertThat(dataStr).isEqualTo(decryptedStr);
        Assertions.assertEquals(dataStr, decryptedStr);
    }
    @Test
    void test_cryptoUtil_aesContent2_ecb() throws Exception {
        String key = "ABCDEFGHIJKLMNOP";
        byte[] keyArr = key.getBytes(StandardCharsets.UTF_8);
        String keyArrBase64 = Base64.getEncoder().encodeToString(keyArr);
        String message = "안녕하세요";
        byte[] encryptedData = EncryptionUtils.encryptWithAesEcbSecret(message, keyArr);
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
        byte[] decryptedData = Base64.getDecoder().decode(encryptedDataBase64);
//        System.out.println("test_cryptoUtil_aesContent2_ecb - keyArrBase64: [" + keyArrBase64 + "]");
//        System.out.println("test_cryptoUtil_aesContent2_ecb - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_ecb - keyArr.length: [" + keyArr.length + "]");
        log.debug("test_cryptoUtil_aesContent2_ecb - keyArrBase64: [" + keyArrBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_ecb - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_ecb - encryptedData.length: [" + encryptedData.length + "]");
        log.debug("test_cryptoUtil_aesContent2_ecb - decryptedData.length: [" + decryptedData.length + "]");
        Assertions.assertNotNull(decryptedData);
        assertThat(encryptedData.length).isEqualTo(decryptedData.length);
    }

    @Test
    void test_cryptoUtil_aesContent2_cbc() throws Exception {
        String keyStr = "ABCDEFGHIJKLMNOP";
        byte[] key = keyStr.getBytes(StandardCharsets.UTF_8);
        String keyBase64 = Base64.getEncoder().encodeToString(key);
        String dataStr = "안녕하세요";
        String ivBase64 = "ZO2vTWmBh1/JD76ZO5e53g==";
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        String ivStr = new String(iv);
        byte[] encryptedData = EncryptionUtils.encryptWithAesCbcSecret(dataStr, key, iv);
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
        byte[] decryptedData = Base64.getDecoder().decode(encryptedDataBase64);
//        System.out.println("test_cryptoUtil_aesContent2_cbc - key.length: [" + key.length + "], keyBase64: [" + keyBase64 + "]");
//        System.out.println("test_cryptoUtil_aesContent2_cbc - iv.length: [" + iv.length + "], ivStr: [" + ivStr + "]");
//        System.out.println("test_cryptoUtil_aesContent2_cbc - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_cbc - key.length: [" + key.length + "], keyBase64: [" + keyBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_cbc - iv.length: [" + iv.length + "], ivStr: [" + ivStr + "]");
        log.debug("test_cryptoUtil_aesContent2_cbc - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_cbc - encryptedData.length: [" + encryptedData.length + "]");
        log.debug("test_cryptoUtil_aesContent2_cbc - decryptedData.length: [" + decryptedData.length + "]");
        Assertions.assertNotNull(decryptedData);
        assertThat(encryptedData.length).isEqualTo(decryptedData.length);
    }

    @Test
    void test_cryptoUtil_aesContent2_gcm() throws Exception {
        String keyStr = "ABCDEFGHIJKLMNOP";
        byte[] key = keyStr.getBytes(StandardCharsets.UTF_8);
        String keyBase64 = Base64.getEncoder().encodeToString(key);
        String dataStr = "안녕하세요";
        String ivBase64 = "ZO2vTWmBh1/JD76ZO5e53g==";
        byte[] iv = Base64.getDecoder().decode(ivBase64);
        String ivStr = new String(iv);
        byte[] encryptedData = EncryptionUtils.encryptWithAesGcmSecret(dataStr, key, iv);
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
        //
        byte[] data = Base64.getDecoder().decode(encryptedDataBase64);
        byte[] decryptedData = EncryptionUtils.decryptWithAesGcmSecret(data, key, iv);
        String decryptedStr = new String(decryptedData);
        //
//        System.out.println("test_cryptoUtil_aesContent2_gcm - key.length: [" + key.length + "], keyBase64: [" + keyBase64 + "]");
//        System.out.println("test_cryptoUtil_aesContent2_gcm - iv.length: [" + iv.length + "], ivStr: [" + ivStr + "]");
//        System.out.println("test_cryptoUtil_aesContent2_gcm - encryptedDataBase64: [" + encryptedDataBase64 + "]");
//        System.out.println("test_cryptoUtil_aesContent2_gcm - decryptedStr: [" + decryptedStr + "]");
        log.debug("test_cryptoUtil_aesContent2_gcm - key.length: [" + key.length + "], keyBase64: [" + keyBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_gcm - iv.length: [" + iv.length + "], ivStr: [" + ivStr + "]");
        log.debug("test_cryptoUtil_aesContent2_gcm - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent2_gcm - decryptedStr: [" + decryptedStr + "]");
        //
        assertThat(decryptedStr).isEqualTo(dataStr);
    }

    @Test
    void test_cryptoUtil_aesContent3_ecb() throws Exception {
        String keyBase64 = "axAaqoXFeyDi7x1qotchNcEeOlGZ7cqKt4nZpl4JWCI=";
        byte[] keyArr = Base64.getDecoder().decode(keyBase64);
        String keyArrBase64 = Base64.getEncoder().encodeToString(keyArr);
        String message = "{\"versionNo\":null,\"id\":40,\"title\":null,\"purchasedDate\":null,\"createdBy\":null,\"createdDatetime\":null,\"lastModifiedBy\":null,\"lastModifiedDatetime\":null}";
        byte[] encryptedData = EncryptionUtils.encryptWithAesEcbSecret(message, keyArr);
        String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);
        //
        byte[] data = Base64.getDecoder().decode(encryptedDataBase64);
        byte[] decryptedData = EncryptionUtils.decryptWithAesEcbSecret(data, keyArr);
        String decryptedStr = new String(decryptedData);
        //
//        System.out.println("test_cryptoUtil_aesContent3_ecb - keyBase64: [" + keyBase64 + "]");
//        System.out.println("test_cryptoUtil_aesContent3_ecb - keyArrBase64: [" + keyArrBase64 + "]");
//        System.out.println("test_cryptoUtil_aesContent3_ecb - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent3_ecb - keyArr.length: [" + keyArr.length + "]");
        log.debug("test_cryptoUtil_aesContent3_ecb - keyBase64: [" + keyBase64 + "]");
        log.debug("test_cryptoUtil_aesContent3_ecb - keyArrBase64: [" + keyArrBase64 + "]");
        log.debug("test_cryptoUtil_aesContent3_ecb - encryptedDataBase64: [" + encryptedDataBase64 + "]");
        log.debug("test_cryptoUtil_aesContent3_ecb - decryptedStr: [" + decryptedStr + "]");
        //
        assertThat(decryptedStr).isEqualTo(message);
    }

}
