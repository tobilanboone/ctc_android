package kualian.dc.deal.application.util;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by admin on 2018/4/10.
 */

public class DevicesUtils {
    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context) {
        String uuid = SpUtil.getInstance().getString(Constants.UUID);
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SpUtil.getInstance().setString(Constants.UUID, uuid);
        }
        return uuid;
    }
}
