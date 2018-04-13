package kualian.dc.deal.application.util;

import android.widget.Toast;

import kualian.dc.deal.application.WalletApp;

/**
 * Created by admin on 2018/3/28.
 */

public class ToastUtil {

    public static void showToastShort(String message){
        Toast.makeText(WalletApp.getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
