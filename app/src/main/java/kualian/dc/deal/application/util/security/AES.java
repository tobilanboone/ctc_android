package kualian.dc.deal.application.util.security;

/**
 * Created by admin on 2018/4/10.
 */


import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static byte[] createKey()
            throws Exception
    {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keygen.init(random);
        Key key = keygen.generateKey();
        byte[] keyBytes = key.getEncoded();
        return keyBytes;
    }
    public static String encrypt(byte[] aesKeyBytes, byte[] text)
            throws Exception
    {
        SecretKeySpec key = new SecretKeySpec(aesKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] doFinal = cipher.doFinal(text);
        return new String(Base64.getEncoder().encode(doFinal));
    }
    public static byte[] decrypt(byte[] aesKeyBytes, String encryptData)
            throws Exception
    {
        byte[] dataBytes = Base64.getDecoder().decode(encryptData.getBytes());
        SecretKeySpec key = new SecretKeySpec(aesKeyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(dataBytes);
    }
    /*public static void main(String[] args) throws Exception {
        AES aes = new AES();
        byte[] aesKeyBytes = aes.createKey();
        System.out.println(aesKeyBytes.length);
//AES 密钥 RSA 加密后传输
        String encryptData =
                aes.encrypt(aesKeyBytes,"hello!".getBytes());
        System.out.println(encryptData);
        byte[] decryptBytes = aes.decrypt(aesKeyBytes,encryptData);
        System.out.println(new String(decryptBytes));
        }*/
}
