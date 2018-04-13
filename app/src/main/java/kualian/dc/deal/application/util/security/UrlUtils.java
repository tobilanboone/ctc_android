package kualian.dc.deal.application.util.security;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by admin on 2018/1/10.
 */

public class UrlUtils {
    /**
     * url encode
     * @param paramString
     * @return
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
        }

        return "";
    }

    /**
     * url decode
     * @param paramString
     * @return
     */
    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
        }

        return "";
    }
}
