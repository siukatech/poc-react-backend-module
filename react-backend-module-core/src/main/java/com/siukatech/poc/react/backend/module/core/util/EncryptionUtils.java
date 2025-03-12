package com.siukatech.poc.react.backend.module.core.util;

import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * https://4youngpadawans.com/end-to-end-encryption-between-react-and-spring/
 */
@Slf4j
public class EncryptionUtils {

    public static final String ALGORITHM_RSA = "RSA";
    public static final String ALGORITHM_AES = "AES";
    public static final String TRANSFORMATION_RSA = "RSA";
    public static final String TRANSFORMATION_RSA_NONE_OAEPWITHSHA256_MGF1PADDING = "RSA/None/OAEPWITHSHA-256ANDMGF1PADDING";
    public static final String TRANSFORMATION_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    public static final String TRANSFORMATION_AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
    public static final String TRANSFORMATION_AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    public static final Integer AES_GCM_SECRET_LENGTH_MIN = 16;
    public static final String HASHING_ALGORITHM_SHA_512 = "SHA-512";
    public static final String HASHING_ALGORITHM_SHA_265 = "SHA-256";
    public static final String HASHING_ALGORITHM_MD5 = "MD5";


    private EncryptionUtils() {
    }

    /**
     * Reference:
     * https://stackoverflow.com/a/50381020
     *
     * @param length
     * @return
     */
    public static String generateRandomToken(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        String tokenStr = encoder.encodeToString(bytes);
        return tokenStr;
    }

    public static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        SecureRandom secureRandom = new SecureRandom();
        //1024 bit long key
        //keyGen.initialize(1024);
        keyGen.initialize(2048, secureRandom);
        //generating RSA key pair (public and private)
        return keyGen.genKeyPair();
    }

    public static PublicKey getRsaPublicKey(String base64PublicKey) throws Exception {
        byte[] publicKey = Base64.getDecoder().decode(base64PublicKey);
        EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getRsaPrivateKey(String base64PrivateKey) throws Exception {
        log.debug("getRsaPrivateKey - base64PrivateKey: [" + base64PrivateKey + "]");
        byte[] privateKey = Base64.getDecoder().decode(base64PrivateKey);
        //PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] encryptWithRsaPublicKey(String text, String rsaPublicKeyBase64) throws Exception {
        //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey(rsaPublicKeyBase64));
        return cipher.doFinal(text.getBytes());
    }

    public static byte[] decryptWithRsaPrivateKey(byte[] data, String rsaPrivateKeyBase64) throws Exception {
        //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA);
        cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(rsaPrivateKeyBase64));
        return cipher.doFinal(data);
    }

    public static byte[] encryptWithAesCbcSecret(String str, byte[] secret, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_CBC_PKCS5PADDING);
        //Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES), new IvParameterSpec(iv));
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] decryptWithAesCbcSecret(byte[] data, byte[] secret, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_CBC_PKCS5PADDING);
        //Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES), new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    @Deprecated
    public static byte[] encryptWithAesEcbSecret(String str, byte[] secret) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES));
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }

    @Deprecated
    public static byte[] decryptWithAesEcbSecret(byte[] data, byte[] secret) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES));
        return cipher.doFinal(data);
    }

    /**
     * Reference:
     * https://www.javainterviewpoint.com/java-aes-256-gcm-encryption-and-decryption/
     *
     * @param str
     * @param secret
     * @param iv
     * @return
     * @throws Exception
     */
    private static void assertSecretLength(byte[] secret, int len) {
        // secret length MUST be a multiple of 16, like 16, 32, 48
        assert (secret.length % len == 0);
    }
    public static byte[] encryptWithAesGcmSecret(String str, byte[] secret, byte[] iv) throws Exception {
        assertSecretLength(secret, AES_GCM_SECRET_LENGTH_MIN);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_GCM_NOPADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES)
                , new GCMParameterSpec(16 * 8, iv));
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] decryptWithAesGcmSecret(byte[] data, byte[] secret, byte[] iv) throws Exception {
        assertSecretLength(secret, AES_GCM_SECRET_LENGTH_MIN);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_GCM_NOPADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES)
                , new GCMParameterSpec(16 * 8, iv));
        return cipher.doFinal(data);
    }


    public static byte[] encryptWithRsaPrivateKey(String text, String rsaPrivateKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, getRsaPrivateKey(rsaPrivateKeyBase64));
        return cipher.doFinal(text.getBytes());
    }

    public static byte[] decryptWithRsaPublicKey(byte[] data, String rsaPublicKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, getRsaPublicKey(rsaPublicKeyBase64));
        return cipher.doFinal(data);
    }

    /**
     * Reference:
     * https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce/call-your-api-using-the-authorization-code-flow-with-pkce#java-sample
     *
     * @return
     */
    public static String generateCodeVerifier() {
        // Dependency: Apache Commons Codec
        // https://commons.apache.org/proper/commons-codec/
        // Import the Base64 class.
        // import org.apache.commons.codec.binary.Base64;
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(code);
        return verifier;
    }

    /**
     * Reference:
     * https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce/call-your-api-using-the-authorization-code-flow-with-pkce#java-sample
     *
     * @param verifier
     * @return
     */
    public static String generateCodeChallenge(String verifier)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // Dependency: Apache Commons Codec
        // https://commons.apache.org/proper/commons-codec/
        // Import the Base64 class.
        // import org.apache.commons.codec.binary.Base64;
//        byte[] bytes = verifier.getBytes("US-ASCII");
        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest md = MessageDigest.getInstance(HASHING_ALGORITHM_SHA_265);
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        String challenge = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(digest);
        return challenge;
    }

    public static String generateMD5Hash(byte[] content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASHING_ALGORITHM_MD5);
        md.update(content);
        byte[] digest = md.digest();
        String hashStr = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hashStr;
    }

}
