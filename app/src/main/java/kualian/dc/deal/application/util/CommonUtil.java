package kualian.dc.deal.application.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kualian.dc.deal.application.WalletApp;

/**
 * @author yiw
 * @ClassName: CommonUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午4:17:01
 */
public class CommonUtil {

    public static int dip2px(float dpValue) {
        final float scale = WalletApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = WalletApp.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取手机的密度
     */
    public static float getDensity() {
        DisplayMetrics dm = WalletApp.getContext().getResources().getDisplayMetrics();
        return dm.density;
    }

    public static int getScreenWidth() {
        final Resources resources = WalletApp.getContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = WalletApp.getContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void copyContent(Activity activity, String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 利用正则表达式判断字符串是否是数字和字母以及位数是否正确
     *
     * @param str
     * @return
     */

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{26,35}");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    public static boolean isNumericExp(String str) {
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{58,67}");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    public static boolean isNumAndLetter(List<String> list) {
        boolean isLetter =true;
        for (String word : list) {
            if (!isLetter(word)){
                isLetter =false;
                break;
            }
        }
       return isLetter;
    }
    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    /*  public static void startScan(Activity activity){
          //请求Camera权限 与 文件读写 权限
           new AlertDialog.Builder(activity)
                  .setTitle(R.string.camera_tips)
                  .setMessage(R.string.camera_sure)
                  .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                              ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 1);
                          }
                      }
                  });


      }*/
  /*
  * 获得汉语拼音首字母*/
    public static String getAlpha(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(
                            nameChar[i], defaultFormat)[0].charAt(0);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /**
     * 11      * 将文字转为汉语拼音
     * 12      * @param chineselanguage 要转成拼音的中文
     * 13
     */
    public static String toHanyuPinyin(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// 如果字符是中文,则将中文转为汉语拼音
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {// 如果字符不是中文,则不转换
                    hanyupinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }
    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    public static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }
    public static void setTranslateAnimationX(View view){
        TranslateAnimation animation = new TranslateAnimation(0, -8, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

    //语言设置的代码
    public static void switchLanguage(String language) {
        if (language==null){
            language=SpUtil.getInstance().getSystemLanguage();
        }
        Resources resources = WalletApp.getContext().getResources();
        android.content.res.Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case "zh":
                config.locale = Locale.CHINESE;
                resources.updateConfiguration(config, dm);
                break;
            case "en":
                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);
                break;
            default:
                config.locale = Locale.US;
                resources.updateConfiguration(config, dm);
                break;
        }
    }
}
