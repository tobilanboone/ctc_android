package kualian.dc.deal.application.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.bitcoin.sign.BitcoinWallet;
import com.bitcoin.sign.bitcoindrpc.Output;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import ctc_shares1_decode.CTCKeyUtil;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.bean.TradeResponse;


public class KeyUtil {

    private static final int SEED_ENTROPY_DEFAULT = 24;
    private static final String MASTER_KEY_PASSPHRASE = "";

    /**
     * 生成18个单词的SEED列表
     *
     * @return
     */
    public static List<String> getSeedWordList() {
        byte[] entropy = new byte[SEED_ENTROPY_DEFAULT];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(entropy);
        try {

            return MnemonicCode.INSTANCE.toMnemonic(entropy);
        } catch (MnemonicLengthException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成18个单词的SEED字符串,以空格间隔
     *
     * @return
     */
    public static String getSeedWordStr() {
        List<String> seedList = getSeedWordList();
        return seedList == null ? "" : Joiner.on(' ').join(seedList);
    }

    /**
     * 把SEED字符串转成SEED列表
     *
     * @param seedStr SEED字符串
     * @return
     */
    public static List<String> seedStr2List(String seedStr) {
        return seedStr == null ? null : Arrays.asList(seedStr.trim().split(" "));
    }

    /**
     * 根据SEED字符串生成主私钥与公钥
     *
     * @param seedStr SEED字符串
     */
    public static DeterministicKey genMasterPriKey(String seedStr) {
        // 生成主私钥
        DeterministicKey masterKey = CTCKeyUtil.genMasterPriKey(seedStr);
       return masterKey;

        /*try {
            DeterministicSeed dSeed = new DeterministicSeed(seedStr, null, MASTER_KEY_PASSPHRASE, System.currentTimeMillis());
            return HDKeyDerivation.createMasterPrivateKey(dSeed.getSeedBytes());
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    /**
     * 根据SEED字符串生成16进制的主私钥
     *
     * @param seedStr SEED字符串
     * @return 16进制的私钥
     */
    public static String genMasterPriKeyHex(String seedStr) {
        DeterministicKey masterKey = genMasterPriKey(seedStr);
        return masterKey == null ? null : masterKey.getPrivateKeyAsHex();
    }

    /**
     * 根据SEED字符串与网络类型生成WIF格式的主私钥
     *
     * @param net     比特币网络类型
     * @param seedStr SEED字符串
     * @return WIF格式的私钥
     */
    public static String genMasterPriKeyWif(NetworkParameters net, String seedStr) {
        DeterministicKey masterKey = genMasterPriKey(seedStr);
        return masterKey == null ? null : masterKey.getPrivateKeyAsWiF(net);
    }

    /**
     * 根据SEED字符串生成16进制的公钥
     *
     * @param seedStr SEED字符串
     * @return 16进制的公钥
     */
    public static String genMasterPubKeyHex(String seedStr) {
        DeterministicKey masterKey = genMasterPriKey(seedStr);
        return masterKey == null ? null : masterKey.getPublicKeyAsHex();
    }

    /**
     * 根据SEED字符串以及网络类型生成相应的地址
     *
     * @param net     网络类型
     * @param seedStr SEED字符串
     * @return 钱包地址
     */
    public static String genMasterPubKeyAddr(NetworkParameters net, String seedStr) {
        DeterministicKey masterKey = genMasterPriKey(seedStr);
        return masterKey == null ? null : masterKey.toAddress(net).toBase58();
    }

    /**
     * 根据主KEY以及序号生成相应的子私钥
     *
     * @param masterKey 主key
     * @param index     子KEY的序号
     * @return 子KEY
     */
    public static DeterministicKey genSubKeyFromMasterKey(DeterministicKey masterKey, int index) {
        DeterministicHierarchy dh = new DeterministicHierarchy(masterKey);
        ImmutableList<ChildNumber> list = ImmutableList.of(new ChildNumber(index));
        return dh.get(list, true, true);
    }

    /**
     * 根据主KEY以及序号生成16进制的子私钥
     *
     * @param masterKey 主key
     * @param index     子KEY的序号
     * @return 16进制子私钥
     */
    public static String genSubPriKeyHexFromMasterKey(DeterministicKey masterKey, int index) {
        DeterministicKey subKey = genSubKeyFromMasterKey(masterKey, index);
        return subKey.getPrivateKeyAsHex();
    }

    /**
     * 根据主KEY以及序号生成WIF格式的子私钥
     *
     * @param masterKey 主key
     * @param index     子KEY的序号
     * @return WIF格式的子私钥
     */
    public static String genSubPriKeyWifFromMasterKey(DeterministicKey masterKey, int index, NetworkParameters net) {
       /* DeterministicKey subKey = genSubKeyFromMasterKey(masterKey, index);
        return subKey.getPrivateKeyAsWiF(net);*/

        // 生成CTC的子私钥与地址
        ECKey ctcPriKey = CTCKeyUtil.genSubKeyFromMasterKey(masterKey, 3);
        String ctcPriKeyWif = CTCKeyUtil.genCTCPriKeyWif(ctcPriKey);
        return ctcPriKeyWif;
    }

    /**
     * 根据主KEY以及序号生成16进制的子公钥
     *
     * @param masterKey 主KEY
     * @param index     子KEY的序号
     * @return 16进制的主KEY
     */
    public static String genSubPubKeyHexFromMasterKey(DeterministicKey masterKey, int index) {
        DeterministicKey subKey = genSubKeyFromMasterKey(masterKey, index);
        return subKey.getPublicKeyAsHex();
    }

    public static String getCTCPubKey(String seedStr){
        DeterministicKey masterKey = CTCKeyUtil.genMasterPriKey(seedStr);
        ECKey ctcPriKey = CTCKeyUtil.genSubKeyFromMasterKey(masterKey, 3);
        String ctcPubKeyBase58 = CTCKeyUtil.genCTCPubKeyBase58(ctcPriKey);
        return ctcPubKeyBase58;
    }

    /**
     * 根据主KEY、序号以及网络类型生成子地址
     *
     * @param masterKey 主KEY
     * @param index     子KEY的序号
     * @return 子地址
     */
    public static String genSubPubAddrWifFromMasterKey(DeterministicKey masterKey, int index, NetworkParameters net) {
        ECKey ctcPriKey = CTCKeyUtil.genSubKeyFromMasterKey(masterKey, 3);
        String ctcAddr = CTCKeyUtil.genCTCAddress(ctcPriKey);
        /*DeterministicKey subKey = genSubKeyFromMasterKey(masterKey, index);
        return subKey.toAddress(net).toBase58();*/
        return ctcAddr;
    }

    /**
     * 导入私钥
     *
     * @param priKey WIF格式的私钥
     * @param net    网络类型
     * @return KEY
     */
    public static ECKey importKeyFromPriKeyWif(String priKey, NetworkParameters net) {
        //MainNetParams.get();
        DumpedPrivateKey privateKey = DumpedPrivateKey.fromBase58(net, priKey);
        return privateKey.getKey();
    }

    /**
     * 根据私钥获取地址
     *
     * @param priKey WIF格式的私钥
     * @param net    网络类型
     * @return 地址
     */
    public static String getPubAddrFromPriKeyWif(String priKey, NetworkParameters net) {
        DumpedPrivateKey privateKey = DumpedPrivateKey.fromBase58(net, priKey);
        return privateKey.getKey().toAddress(net).toBase58();
    }

    public static ArrayList<String> parseMnemonic(String mnemonicString) {
        ArrayList<String> seedWords = new ArrayList<>();
        for (String word : mnemonicString.trim().split(" ")) {
            if (word.isEmpty() || word.equals(" ")) continue;
            seedWords.add(word);
        }
        return seedWords;
    }
    public static String getSendWords(ArrayList<String> list){
        StringBuilder stringBuilder =new StringBuilder();
        int size = list.size();
        for (int i = 0; i <size ; i++) {
            if (i!=list.size()-1){
                stringBuilder.append(list.get(i)+" ");
            }else {
                stringBuilder.append(list.get(i));
            }
        }
        return stringBuilder.toString();

    }
   /* *//**
     * 根据种子密钥生成钱包ID
     *
     * @param seedStr
     * @return 钱包ID
     *//*
    public static String genWalletIdFromSeed(String seedStr) {
        String salt = "Bitbank:";
        String macAlg = "HmacSHA256";
        SecretKeySpec sKeySpec = new SecretKeySpec(salt.getBytes(), macAlg);
        Mac mac = null;
        try {
            mac = Mac.getInstance(macAlg);
            mac.init(sKeySpec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] seedBytes = mac.doFinal(seedStr.getBytes());

        RIPEMD256Digest hash256 = new RIPEMD256Digest();
        hash256.update(seedBytes, 0, seedBytes.length);
        hash256.update(seedBytes, 0, seedBytes.length);

        RIPEMD160Digest ripemd160Digest = new RIPEMD160Digest();
        ripemd160Digest.update(seedBytes, 0, seedBytes.length);
        byte[] out = new byte[16];
        ripemd160Digest.doFinal(out, 0);

       *//* RIPEMD128Digest hash128 = new RIPEMD128Digest();
        hash128.update(seedBytes, 0, seedBytes.length);
        byte[] out = new byte[16];
        hash128.doFinal(out, 0);*//*
        return Hex.toHexString(out);
    }*/
    /**
     *  利用java原生的摘要实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256StrJava(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }


    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
    /**
     * 密码 加密
     *
     * @param pw
     * @return
     */
    public static String getPwMessage(String pw)  {
        String salt = "Bitbank:";
        String macAlg = "HmacSHA256";
        SecretKeySpec sKeySpec = new SecretKeySpec(salt.getBytes(), macAlg);
        Mac mac = null;
        try {
            mac = Mac.getInstance(macAlg);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(sKeySpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] seedBytes = mac.doFinal(pw.getBytes());
        RIPEMD128Digest hash128 = new RIPEMD128Digest();
        hash128.update(seedBytes, 0, seedBytes.length);
        byte[] out = new byte[16];
        hash128.doFinal(out, 0);
        return Hex.toHexString(out);
    }
    public static String genWalletIdFromSeed(String seedStr) {
        String alg = "SHA-256";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] seedBytes = md.digest(md.digest(seedStr.trim().getBytes()));
        RIPEMD160Digest hash160 = new RIPEMD160Digest();
        hash160.update(seedBytes, 0, seedBytes.length);
        byte[] out = new byte[20];
        hash160.doFinal(out, 0);
        return Hex.toHexString(out);

    }
    public static String getRandom() {
        return ((int) (Math.random() * 9 + 1) * 100000) + "";
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) WalletApp.getContext().getSystemService(WalletApp.getContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(WalletApp.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String imei = telephonyManager.getDeviceId();
            return imei;
        }
        return "imei";
    }


    /**
     * 生成签名信息
     */
    public static String genSignMessage(List<TradeResponse.DataBean.InputsBean> inputs, String rawTrx, String address, String privateKey) {
        if (inputs == null) {
            return "";
        }

        Output[] outputs = new Output[inputs.size()];
        // Output[] outputs = new Output[inputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            Output output = new Output();
            output.setAccount("");
            // output.setAddress("n1i8CRPgUdpGctP8mw4yvi2ZwZr3tyk2NP");
            output.setAmount(BigDecimal.valueOf(inputs.get(i).getAmout()));
            output.setOutputIndex(inputs.get(i).getVout());
            output.setTransactionId(inputs.get(i).getTxid());
            output.setScriptPubKey(inputs.get(i).getScriptPubKey());
            outputs[i] = output;
        }


        BitcoinWallet wallet = new BitcoinWallet();
        // String address = "n1i8CRPgUdpGctP8mw4yvi2ZwZr3tyk2NP";

//        List<String> privateKeyList = new ArrayList<>();
//        privateKeyList.add(privateKey);
        Map<String, String> addressKey = new HashMap<>();
        addressKey.put(address, privateKey);
        String sign = wallet.signTransaction(rawTrx, addressKey, outputs, "UBTC");
        LogUtils.i("sign= " + sign);
        return sign;
    }
}