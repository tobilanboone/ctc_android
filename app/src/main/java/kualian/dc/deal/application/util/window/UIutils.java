package kualian.dc.deal.application.util.window;

import kualian.dc.deal.application.WalletApp;

/**
 * Created by admin on 2018/3/27.
 */

public class UIutils {
    public static String getString(int id){
        return WalletApp.getContext().getResources().getString(id);
    }
}
