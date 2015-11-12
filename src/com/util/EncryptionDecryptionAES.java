package com.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

/**
 * 加密算法
 * Created by alvawen on 2015/11/12.
 */
public class EncryptionDecryptionAES {
    static String IV = "ABCAAHIDEFGAACBA";
    private static final String charsetName = "UTF-8";
    private static Cipher cipher;

    public static void main(String[] args) throws Exception {
        try {
            System.out.println(EncryptionDecryptionAES.encryptUserPassword("nidayu163"));
            System.out.println(EncryptionDecryptionAES.decryptUserPassword("64ECDE3E35311715E00923CBF2C5D1C0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test1() {
        String plaintext = "test abc21text 123"; /*Note null padding*/
        String encryptionKey = "0123456789abcdef";
        try {
            System.out.println("==Java==");
            System.out.println("plain:   " + plaintext);

            String cipher = encrypt(plaintext, encryptionKey);

            System.out.println("cipher:  " + cipher);
            long ts = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                System.out.println(i + " cipher:  " + encrypt("1234567890123456789" + i, encryptionKey));
            }
            System.out.println((System.currentTimeMillis() - ts) / 1000.0);
            //System.out.println("cipher:  " + encrypt("abcefghij", encryptionKey));
            System.out.println("");
            String decrypted = decrypt(cipher, encryptionKey);
            System.out.println("decrypt: " + decrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test2() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        cipher = Cipher.getInstance("AES");

        String plainText = "AES Symmetric Encryption Decryption";
        System.out.println("Plain Text Before Encryption: " + plainText);

        String encryptedText = encrypt(plainText, secretKey);
        System.out.println("Encrypted Text After Encryption: " + encryptedText);

        String decryptedText = decrypt(encryptedText, secretKey);
        System.out.println("Decrypted Text After Decryption: " + decryptedText);
    }

    private static void test3() throws Exception {
        String key1 = "Bar12345Bar12345"; // 128 bit key
        String key2 = "ThisIsASecretKet";
        System.out.println(decrypt(key1, key2, encrypt(key1, key2, "Hellossssd World")));
    }

    private static final String algorithm = "AES/CBC/PKCS5PADDING";
    //private static final String algorithm = "AES";
    private static final String ENCRY_KEY1 = "0123456789abcdef";
    private static final String ENCRY_KEY2 = "abcd456789abcdef";
    private static final String ENCRY_KEY3 = "enaeskeytiontest";
    public static String encryptUserPassword(String plainText) throws Exception {
        return encrypt(plainText, ENCRY_KEY1);
    }
    public static String encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(charsetName), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes(charsetName)));
        //return Base64.encodeBase64String(cipher.doFinal(plainText.getBytes(charsetName)));
        return byte2hex(cipher.doFinal(plainText.getBytes(charsetName)));
    }
    public static String decryptUserPassword(String cipherText) throws Exception{
        return decrypt(cipherText, ENCRY_KEY1);
    }
    public static String decrypt(String cipherText, String encryptionKey) throws Exception{
        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(charsetName), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes(charsetName)));
        return new String(cipher.doFinal(new BigInteger(cipherText, 16).toByteArray()), charsetName);
        //return new String(cipher.doFinal(Base64.decodeBase64(cipherText)), charsetName);
    }

    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }

    public static String encrypt(String key1, String key2, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string:" + Base64.encodeBase64String(encrypted));
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String key1, String key2, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));

            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    /**
     * 十六进制字符串转化为2进制
     *
     * @param hex
     * @return
     */
    public static byte[] hex2byte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = hex.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }


}
