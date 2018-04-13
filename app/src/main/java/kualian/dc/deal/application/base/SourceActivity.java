package kualian.dc.deal.application.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.callback.OnCloseDelegateListener;
import kualian.dc.deal.application.ui.MainDelegate;
import kualian.dc.deal.application.util.CommonUtil;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.SpUtil;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import qiu.niorgai.StatusBarCompat;

import static kualian.dc.deal.application.util.RxBarTool.FlymeSetStatusBarLightMode;

/**
 * Created by zheng on 2017/12/26.
 */

public class SourceActivity extends SupportActivity implements OnCloseDelegateListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.transparent));
        StatusBarCompat.translucentStatusBar(this, true);
        StatusBarLightMode(this);
        //StatusBarCompat.
        Locale aDefault = Locale.getDefault();
        String language = aDefault.getLanguage();
        if (language.equals(Constants.LANGAE_ZH)){
            SpUtil.getInstance().setSystemLanguage(language);
        }else {
            SpUtil.getInstance().setSystemLanguage(Constants.LANGAE_EN);
        }
        if (SpUtil.getInstance().getDefaultLanguage()==null){
            if (Constants.LANGAE_ZH.equals(language)){
                CommonUtil.switchLanguage(Constants.LANGAE_ZH);
            }else {
                CommonUtil.switchLanguage(Constants.LANGAE_EN);
            }
        }else {
            CommonUtil.switchLanguage(SpUtil.getInstance().getDefaultLanguage());
        }

        setContentView(R.layout.container);
        if (findFragment(MainDelegate.class) == null) {
            loadRootFragment(R.id.fl_container, new MainDelegate());
        }


    }

    /**
     *状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(MIUISetStatusBarLightMode(activity, true)){
                result=1;
            }else if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result=3;
            }
        }
        return result;
    }
    /**
     * 需要MIUIV6以上
     * @param activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if(dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }catch (Exception e){

            }
        }
        return result;
    }





    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void OnCloseDelegate() {
        startWithPop(MainDelegate.getInstance());

    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
