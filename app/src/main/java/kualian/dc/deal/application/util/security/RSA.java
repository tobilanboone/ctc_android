package kualian.dc.deal.application.util.security;

/**
 * Created by admin on 2018/4/10.
 */


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import kualian.dc.deal.application.WalletApp;

public class RSA
{
    private static PublicKey getPublicKey(String modulus,String
            publicExponent) throws Exception
    {
        BigInteger m = new BigInteger(modulus);
        BigInteger e = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m,e);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    private static PrivateKey getPrivateKey(String modulus,String
            privateExponent) throws Exception
    {
        BigInteger m = new BigInteger(modulus);
        BigInteger e = new BigInteger(privateExponent);
        RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m,e);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
    public static String encrypt(PublicKey publicKey, byte[] data)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        RSAPublicKey rsaPublicKey = (RSAPublicKey)publicKey;
        int keyLen = rsaPublicKey.getModulus().bitLength();
        int max_encrypt_block=keyLen/8-11;
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
// 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > max_encrypt_block) {
                cache = cipher.doFinal(data, offset,
                        max_encrypt_block);
            } else {
                cache = cipher.doFinal(data, offset, inputLen -
                        offset);
            }
            out.write(cache, 0, cache.length);
            offset += max_encrypt_block;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();

        return new String(Base64.getEncoder().encode(encryptedData));
//        return new String(Base64.encode(encryptedData,Base64.DEFAULT));

//        return DatatypeConverter.printBase64Binary(encryptedData);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] decrypt(PrivateKey privateKey, String
            encryptData)
            throws Exception
    {
        byte[] data = Base64.getDecoder().decode(encryptData);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)privateKey;
        int keyLen = rsaPrivateKey.getModulus().bitLength();
        int max_encrypt_block=keyLen/8;
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > max_encrypt_block) {
                cache = cipher.doFinal(data, offset,
                        max_encrypt_block);
            } else {
                cache = cipher.doFinal(data, offset, inputLen -
                        offset);
            }
            out.write(cache, 0, cache.length);
            offset += max_encrypt_block;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
//    public static void main(String[] args) throws Exception
//    {
//        String modulus =
//                "1010614861982733844574484537033473051515384223265156581365625734846776
//        19037885542942192101969880732297336504913442660267394247766552731511609
//        08432464301896250237695499923741776743481487530943263679772103724620860
//        52701877280908005339841714570525694190530094914009003336985050120338118
//        38849431429669391389749529";
//        String publicExponent = "65537";
//        String privateExponent =
//                "3338697587132180366645126373088747583557316297345607083502016080255054
//        59479265131184125181157191774870627686326952261691769970916220332019751
//        93931868196638713298309919409357016591683996259177257354897423241562278
//        63331629665280526531608619555481341365434517721846728950512780119877163
//        1206809967423233540896605";
//        RSA rsa = new RSA();
//        RSAPublicKey publicKey =
//                (RSAPublicKey)rsa.getPublicKey(modulus, publicExponent);
//        String encryptData = rsa.encrypt(publicKey,"
//        {\"appVersion\":\"1.0.0\",\"actionCode\":\"100000\",\"deviceId\":\"\",\
//            "deviceBrand\":\"HUAWEI\",\"deviceModel\":\"MI7-
//            TL00\",\"osName\":\"Android\",\"osVersion\":\"6.0\",\"key\":\"e8b43e060
//            400ef\"} ".getBytes());
//            System.out.println("加密数据:\r\n"+encryptData);
//            RSAPrivateKey privateKey =
//                    (RSAPrivateKey)rsa.getPrivateKey(modulus, privateExponent);
//            byte[] decryptBytes = rsa.decrypt(privateKey,encryptData);
//            System.out.println("加密数据:\r\n"+new String(decryptBytes));
//        }
//FileInputStream fin = new FileInputStream("PathToCertificate");
//    CertificateFactory f = CertificateFactory.getInstance("X.509");
//    X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
//    PublicKey pk = certificate.getPublicKey();

    public static PublicKey getPublicKey(String path)  {
        InputStream inputStream = null;
        try {

//            inputStream = new FileInputStream(path);
            inputStream = WalletApp.getContext().getAssets().open(path);
            CertificateFactory f = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) f.generateCertificate(inputStream);
            return certificate.getPublicKey();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException ignored) {
            }
        }

    }
}