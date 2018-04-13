package kualian.dc.deal.application;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.multidex.MultiDex;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zhouyou.http.RxHttp;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.bitcoinj.crypto.MnemonicCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import kualian.dc.deal.application.config.Latte;
import kualian.dc.deal.application.util.KeyUtil;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.wallet.CoinType;
import kualian.dc.deal.application.wallet.coins.CtcMain;
import okhttp3.OkHttpClient;


/**
 * @author John L. Jegutanis
 * @author Andreas Schildbach
 */

public class WalletApp extends Application {
    private static Context mContext;
    private static List<CoinType> coinTypeList = new ArrayList<>();
    public static boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        WalletTool.init(this);
        Latte.init(this);
        Latte.getConfigurator().configure();
        //ChangeLanguageHelper.init(this);
        mContext = getApplicationContext();
        if (MnemonicCode.INSTANCE == null) {
            try {
                MnemonicCode.INSTANCE = new MnemonicCode();
            } catch (IOException e) {
                throw new RuntimeException("Could not set MnemonicCode.INSTANCE", e);
            }
        }
        String random = KeyUtil.getRandom();
        RxHttp.init(this);
        //设置请求参数 clienttype":"Android","handshake":"lightwallet415506.83007309574locklinker.com","imie":"123","language":"zh","random":"415506.83007309574","trancode":"httpPost","version":"10001","walletid":"fe9c543679f2f37b73d000094af708d8"

        // params.put("clienttype","Android");
        //设置请求头
      /*  HttpHeaders headers = new HttpHeaders();
        headers.put("User-Agent", SystemInfoUtils.getUserAgent(this, "10001"));

        RxHttp.getInstance()
                .debug("RxEasyHttp", true)
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(10 * 1000)
                .setRetryCount(1)//默认网络不好自动重试3次
                .setRetryDelay(1000)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setBaseUrl("https://192.168.1.220/")
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(1)//缓存版本为1
                .setCertificates();*/


        //ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
        // .addCommonHeaders(headers)//设置全局公共头
        // .addCommonParams(params)//设置全局公共参数
        ;//添加参数签名拦截器
        //.addInterceptor(new HeTInterceptor());//处理自己业务的拦截器
        if (SpUtil.getInstance().getWalletID() != null) {
            Latte.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    coinTypeList.add(new CtcMain());
                }
            });

        }

        initUmeng();

    }

    private void initUmeng(){
        UMConfigure.init(getContext(), "5acdd1c6f43e4842d9000030", "ctca100000", 0, null);
        MobclickAgent.setScenarioType(getContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode( true );

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

    }

    public static Context getContext() {
        return mContext;
    }

    public static List<CoinType> getCoinList() {
        return coinTypeList;
    }

    public static void setCoinTypeList(List<CoinType> list) {

        coinTypeList = list;
    }
    public static String getJson(String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = getContext().getAssets();
        //使用IO流读取json文件内容
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            try {
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        return stringBuilder.toString();
    }


}