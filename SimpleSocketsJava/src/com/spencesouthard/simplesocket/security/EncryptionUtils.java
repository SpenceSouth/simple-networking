package com.spencesouthard.simplesocket.security;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class EncryptionUtils {


    public static byte[] encryptRsa(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    public static byte[] decryptRsa(PrivateKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(ciphertext);
    }

    public static byte[] readFileBytes(String filename) throws IOException
    {
        Path path = Paths.get(filename);
        return Files.readAllBytes(path);
    }

    public static PublicKey readPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    public static PrivateKey readPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(readFileBytes(filename));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static SecretKey generateAesKey() throws NoSuchAlgorithmException, NoSuchPaddingException {

        KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
        KeyGen.init(256);

        return KeyGen.generateKey();
    }

    public static String encryptAes(SecretKey skey, String message) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return Base64.encode(cipher.doFinal(message.getBytes()));
    }

    public static String decryptAes(SecretKey key, String encrypted) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Key k = new SecretKeySpec(Base64.decode(Base64.encode(key.getEncoded())), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] decodedValue = Base64.decode(encrypted);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    public static SecretKey aesStringToKey(String skey){

        byte[] decodedKey = Base64.decode(skey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    }

    public static String aesKeyToString(SecretKey key){
        return Base64.encode(key.getEncoded());
    }

}
