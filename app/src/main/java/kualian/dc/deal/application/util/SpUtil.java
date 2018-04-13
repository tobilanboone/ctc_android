package kualian.dc.deal.application.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.wallet.CoinIndex;

/**
 * Created by 小小程序员 on 2016/12/30.
 */

public class SpUtil {
    static final String RE_LOGIN = "re_login";
    private static SharedPreferences mSharedPreferences;
    private static SpUtil mInstance;
    private Context mContext;
    static final String WALLLET_NAME = "walllet_name";
    static final String WALLET_PW = "wallet_pw";
    static final String LOGIN_PW = "login_pw";
    static final String WALLET_SEND = "wallet_send";
    static final String WALLET_ID = "wallet_ID";
    static final String TEMP_WALLET_SEND = "temp_wallet_send";
    static final String DEFAULT_COIN = "default_coin";
    static final String DEFAULT_LANGUAGE = "default_language";
    static final String SYSTEM_LANGUAGE = "system_language";
    static final String DEFAULT_MONEY = "default_money";
    static final String IMPORT_NUMBER = "import_wallet_name";
    static final String IS_RESTORE = "is_Restore";
    static final String ACCOUNT_ASSET = "account_asset";
    private static final String CHECK_DATE = "check_date";
    private static final String VERSION = "version";
    private static final String UPDATE_URL = "update_url";
    private static final String WALLET_KEY = "wallet_list";
    private static final String DEFAULT_VERSION = "default_version";
    private static final String WALLET_NAME = "wallet_name";
    private static final String IS_SAVE_SEED = "is_save_seed";
    private static final String UUID = "uuid";

    public static SpUtil getInstance() {
        if (mSharedPreferences == null) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(WalletApp.getContext());
        }
        if (mInstance == null) mInstance = new SpUtil();
        return mInstance;
    }

    public void Initialize(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    public boolean getIsReLogin() {

        return mSharedPreferences.getBoolean(RE_LOGIN, false);
    }

    public String getDefaultVersion() {
        return mSharedPreferences.getString(DEFAULT_VERSION, "1.0.1");
    }

    public void setDefaultVersion(String s) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DEFAULT_VERSION, s);
        editor.apply();
    }

    public void setWalletImportName(int number) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(IMPORT_NUMBER, number);
        editor.apply();
    }

    public int getWalletImportName() {
        return mSharedPreferences.getInt(IMPORT_NUMBER,0);
    }

    public void setUUID(String uuid) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(UUID, uuid);
        editor.apply();
    }

    public String getUUID() {
        return mSharedPreferences.getString(UUID,"");
    }
    public Boolean getIsSaveSeed() {
        return mSharedPreferences.getBoolean(IS_SAVE_SEED, false);
    }

    public void setIsSaveSeed(boolean isSaveSeed) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_SAVE_SEED, isSaveSeed);
        editor.apply();
    }

    public void setIsReLogin(boolean isRelogin) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(RE_LOGIN, isRelogin);
        editor.commit();
    }


    public void setSystemLanguage(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SYSTEM_LANGUAGE, name);
        editor.apply();
    }

    public String getSystemLanguage() {
        return mSharedPreferences.getString(SYSTEM_LANGUAGE, "en");
    }


    public void setDefaultLanguage(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DEFAULT_LANGUAGE, name);
        editor.apply();
    }

    public String getDefaultLanguage() {
        return mSharedPreferences.getString(DEFAULT_LANGUAGE, null);
    }

    public void setIsRestore(boolean isRestore) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_RESTORE, isRestore);
        editor.apply();
    }

    public boolean getIsRestore() {
        return mSharedPreferences.getBoolean(IS_RESTORE, false);
    }

    public void setDefaultMoney(boolean isYuan) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(DEFAULT_MONEY, isYuan);
        editor.apply();
    }

    public boolean getDefaultMoney() {
        return mSharedPreferences.getBoolean(DEFAULT_MONEY, true);
    }

    public void setWalletAddress(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(WALLLET_NAME, name);
        editor.apply();
    }

    public String getWalletAddress() {
        return mSharedPreferences.getString(WALLLET_NAME, null);
    }


    public void setWalletPw(String pw) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(WALLET_PW, KeyUtil.getPwMessage(pw));
        editor.apply();
    }
    public void setWalletPw(String pw,boolean encrypt) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (encrypt){
            editor.putString(WALLET_PW, KeyUtil.getPwMessage(pw));
        }else{
            editor.putString(WALLET_PW, pw);
        }

        editor.apply();
    }

    public String getWalletPw() {
        return mSharedPreferences.getString(WALLET_PW, null);
    }

    public void setLoginPw(String pw) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGIN_PW, KeyUtil.getPwMessage(pw));
        editor.apply();
    }

    public String getLoginPw() {
        return mSharedPreferences.getString(LOGIN_PW, null);
    }

    public void setDefaultCoinIndex(int index) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(DEFAULT_COIN, index);
        editor.apply();
    }

    public int getDefaultCoinIndex() {
        return mSharedPreferences.getInt(DEFAULT_COIN, CoinIndex.BITCOIN.index);
    }

    public void setTempWalletSend(String send) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            editor.putString(TEMP_WALLET_SEND, AesUtil.encrypt(AesUtil.temp, send));
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public String getTempWalletSend() {
        if (mSharedPreferences.getString(TEMP_WALLET_SEND, null) == null) {
            return null;
        }
        try {
            return AesUtil.decrypt(AesUtil.temp, mSharedPreferences.getString(TEMP_WALLET_SEND, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setWalletID(String walletID) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(WALLET_ID, walletID);

        editor.apply();
    }

    public String getWalletID() {
      /*  if (mSharedPreferences.getString(WALLET_ID, null) == null) {
            return null;
        }
        try {
            return AesUtil.decrypt(key, mSharedPreferences.getString(WALLET_SEND, null));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return mSharedPreferences.getString(WALLET_ID, "");
    }

    public void setWalletSend(String walletId, String send) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            editor.putString(WALLET_SEND, AesUtil.encrypt(walletId, send));
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public String getWalletSend(String walletId) {
        if (mSharedPreferences.getString(WALLET_SEND, null) == null) {
            return null;
        }
        try {
            return AesUtil.decrypt(walletId, mSharedPreferences.getString(WALLET_SEND, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getMasterPriKey(String key) {
      /*  if (mSharedPreferences.getString(WALLET_ID, null) == null) {
            return null;
        }
        try {
            return AesUtil.decrypt(key, mSharedPreferences.getString(WALLET_SEND, null));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return mSharedPreferences.getString(key, "");
    }

    public void setMasterPriKey(String key, String priKey) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            editor.putString(WALLET_SEND, AesUtil.encrypt(key, priKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.apply();
    }



    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
/*

    */

    /**
     * 使用SharedPreference保存对象
     *
     * @param saveObject 储存的对象
     */
    public void saveWallet(Object saveObject) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        String string = Object2String(saveObject);
        editor.putString(WALLET_KEY, string);
        editor.commit();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @return object 返回根据key得到的对象
     */
    public Object getWallet() {
        String string = mSharedPreferences.getString(WALLET_KEY, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }

    public String getCheckDate() {
        return mSharedPreferences.getString(CHECK_DATE, "0");
    }

    public void setCheckDate(String number) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CHECK_DATE, number);
        editor.apply();
    }

    public String getVersion() {

        return mSharedPreferences.getString(VERSION, getDefaultVersion());
    }

    public void setUpdateUrl(String s) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(UPDATE_URL, s);
        editor.apply();
    }

    public String getUpdateUrl() {

        return mSharedPreferences.getString(UPDATE_URL, getDefaultVersion());
    }

    public void setVersion(String s) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(VERSION, s);
        editor.apply();
    }

    public void setAccountAsset(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(getWalletID()+ACCOUNT_ASSET, name);
        editor.apply();
    }

    public String getAccountAsset() {
        return mSharedPreferences.getString(getWalletID()+ACCOUNT_ASSET, "{\"data\":{\"walletid\":\"d2a10bfb2e2e1749c2af2ec39982130b\",\"assets\":[{\"coinType\":\"UBTC\",\"num\":0.00000000,\"usdtAmt\":0.00,\"cnyAmt\":0.00}],\"totalUsdtAmt\":0.00,\"totalCnyAmt\":0.00},\"errCode\":\"10000\",\"errMsg\":\"\",\"rtnCode\":1}");
    }

    public void setWalletName(String name) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString( WALLET_NAME, name);
        editor.apply();
    }

    public String getWalletName() {
        return mSharedPreferences.getString(WALLET_NAME, "");
    }

    public void setString(String key,String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString( key, value);
        editor.apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }
    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class<T> clazz) {
        if (mSharedPreferences.contains(key)) {
            String objectVal = mSharedPreferences.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
