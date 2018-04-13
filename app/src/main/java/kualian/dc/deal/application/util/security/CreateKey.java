package kualian.dc.deal.application.util.security;

/**
 * Created by admin on 2018/4/10.
 */

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class CreateKey {
    private void createRsaKey() throws Exception {
        KeyPairGenerator keyPairGen =
                KeyPairGenerator.getInstance("RSA");
        //密钥位数
        keyPairGen.initialize(1024);
        //密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey)
                keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey)
                keyPair.getPrivate();
        //模
        String modulus = publicKey.getModulus().toString();
        System.out.println("modulus:" + modulus);
        //公钥指数
        String public_exponent =
                publicKey.getPublicExponent().toString();
        System.out.println("public_exponent:" + public_exponent);
        //私钥指数
        String private_exponent =
                privateKey.getPrivateExponent().toString();
        System.out.println("private_exponent:" + private_exponent);
        int keyLen = publicKey.getModulus().bitLength();
        System.out.println("keyLen:" + keyLen);
    }

    /*public static void main(String[] args) throws Exception {
        System.out.println("创建版本升级协议 RSA 公私钥:");
        CreateKey key = new CreateKey();
        key.createRsaKey();
    }*/
}
