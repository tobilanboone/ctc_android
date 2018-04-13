package kualian.dc.deal.application.presenter.impl;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.PublicKey;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.WalletApp;
import kualian.dc.deal.application.bean.RequestA;
import kualian.dc.deal.application.bean.RequestR;
import kualian.dc.deal.application.callback.ServerCallBack;
import kualian.dc.deal.application.util.Constants;
import kualian.dc.deal.application.util.DevicesUtils;
import kualian.dc.deal.application.util.LogUtils;
import kualian.dc.deal.application.util.NetUtil;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.SpUtil;
import kualian.dc.deal.application.util.security.AES;
import kualian.dc.deal.application.util.security.RSA;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by idmin on 2018/3/6.
 */

public class RequestUtil {


    public static void execute(RequestA.packet packet, ServerCallBack<RequestR.responsePacket> serverCallBack){
        boolean available = NetUtil.isNetworkAvailable(WalletApp.getContext());
        if (!available){
            //Toast.makeText(WalletApp.getContext(),R.string.view_no_network,Toast.LENGTH_LONG).show();
            RxToast.showToast(R.string.view_no_network);
        }
        if(SpUtil.getInstance().getString(Constants.AES_KEY).equals("")){
            changeKey(packet,serverCallBack);
            return;
        }
        String packetString = "";
        String encrypt = "";
        String toJson = new Gson().toJson(packet);
        try {
            if(packet.getActionCode().equals("100000")){
                PublicKey publicKey = RSA.getPublicKey("ctc-app-test-public.cer");
                encrypt = RSA.encrypt(publicKey, toJson.getBytes());
            }else {
                encrypt = AES.encrypt(SpUtil.getInstance().getString(Constants.AES_KEY).getBytes(),toJson.getBytes());
            }
        }catch (Exception e){

        }
        packetString = encrypt;


        OkHttpClient okHttpClient  = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        //post方式提交的数据
        FormBody formBody = new FormBody.Builder()
                .add("packet",packetString)
                .add("sid",SpUtil.getInstance().getString(Constants.SID))
                .add("version",SpUtil.getInstance().getDefaultVersion())
                .add("format","json")
                .add("action","100000")
                .add("encrypt","1")
                .build();

        final Request request = new Request.Builder()
                .url(Constants.REQUEST_URL)//请求的url
                .post(formBody)
                .build();


        //创建/Call
        Call call = okHttpClient.newCall(request);
        //加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                serverCallBack.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200) {
//                    System.out.println(response.body().string());
                    String response1 =null;
                    try {
                        response1 =response.body().string();
                        RequestR requestR = new Gson().fromJson(response1, RequestR.class);
                        LogUtils.i("responseR = "+requestR.toString());

                        String responsePacket = requestR.getResponsePacket();
                        String aes_key = SpUtil.getInstance().getString(Constants.AES_KEY);
                        String rePacketJson = new String(AES.decrypt(aes_key.getBytes(), responsePacket));
                        RequestR.responsePacket responsePacket1 = new Gson().fromJson(rePacketJson, RequestR.responsePacket.class);

                        serverCallBack.onSuccess(responsePacket1);

                    }catch (Exception e){

                    }

                }
            }
        });


        /*OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(json)
                .addHeader("packet","lKc1ZNiNL8g75bEd7k5JaJLhJODVdxHreH6SXIKcvFXTguGoysyWtmKEreZJ%2B2EVeCHRbBf3gnmaNIx%2B7CUGwYIapIynyepPE2qj2ZSmbfm2yDSAYZ%2F8J3884MSxbgMFcErNYbtsvcsJMhA50%2FUEqsFCyzW%2FEq8hh99Nhj%2F%2BfpxQd47C7sb4jqOVqWwIa7lT5Ecaweu%2BR9pWAssKBtrPs%2BAxYoUqW3JbfTDx4Ev3T71%2BaWzRnmRrDaLva6sP1jW2g6yaLD0ZrJsYfJpTx7AeXp5SUj4HCHB%2FmBFrRsPU0mYjw5puiSZ1hXc0A8XKOVfup26E0%2Fier0c6tVv9PYCpRQ%3D%3D")
                .addHeader("sid","")
                .addHeader("version",SpUtil.getInstance().getDefaultVersion())
                .addHeader("language","zh")
                .addHeader("format","json")
                .addHeader("action","100000")
                .addHeader("encrypt","1")
                .build()
                .execute(new Callback<String>() {
                    @Override
                    public String parseNetworkResponse(Response data, int id) throws Exception {
                        String response =null;
                        try {
                            response =data.body().string();
                            LogUtils.i("response = "+response);
                            RequestR requestR = new Gson().fromJson(response, RequestR.class);
                            serverCallBack.onSuccess(requestR);

                        }catch (Exception e){

                        }


                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (available){
                            RxToast.showToast(R.string.service_fail);
                        }
                        AppLoader.stopLoading();
                        ResponseCode resultCode=new ResponseCode();
                        resultCode.setRtnCode(Constants.RTNCODE_ERROR);
                        resultCode.setErrMsg("");
                        resultCode.setErrCode(Constants.ERRCODE_ERROR);
                        serverCallBack.onFailure();

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });*/

       /* RxHttp.post("server/process")
                .upJson(json)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        LogUtils.i("RxHttp = "+e.toString());

                        //showToast(e.getMessage()+"  "+e.getCode());
                    }

                    @Override
                    public void onSuccess(String response) {
                        try {
                            ResponseCode responseCode = new Gson().fromJson(response, ResponseCode.class);
                            if (responseCode!=null&&responseCode.getRtnCode()!=1){
                                RxToast.showToast(responseCode.getErrMsg());
                            }
                        }catch (Exception e){
                            if (!tag.equals(Constants.update_query)){
                                RxToast.showToast(R.string.service_fail);
                            }
                        }
                        result.setResponse(response);
                        result.setApiException(null);
                        mView.getServiceData(result,tag);
                    }
                });*/
    }

    public static void changeKey(RequestA.packet oldPacket,ServerCallBack<RequestR.responsePacket>serverCallBack){
        String uuid = UUID.randomUUID().toString().replace("-","").trim();
        if(uuid.length() < 16){
            return;
        }else {
            uuid = uuid.substring(uuid.length()-16,uuid.length());
        }
        SpUtil.getInstance().setString(Constants.AES_KEY,uuid);
        RequestA.packet packet = new RequestA.packet("100000");
        packet.setDeviceId("");
        packet.setDeviceBrand(DevicesUtils.getDeviceBrand());
        packet.setOsVersion(DevicesUtils.getSystemVersion());
        packet.setOsName("Android");
        packet.setDeviceModel(DevicesUtils.getSystemModel());
        packet.setKey(SpUtil.getInstance().getString(Constants.AES_KEY));

//        RequestA requestA = new RequestA(packet);
//        String jsonString = new Gson().toJson(requestA);
        execute(packet, new ServerCallBack<RequestR.responsePacket>() {


            /**
             * 成功回调
             *
             * @param responsePacket
             */
            @Override
            public void onSuccess(RequestR.responsePacket responsePacket) {
                SpUtil.getInstance().setString(Constants.SID,responsePacket.getSid());
                execute(oldPacket,serverCallBack);
            }

            @Override
            public void onFailure() {
                LogUtils.e("密钥交换：","请求失败");
            }
        });


    }
}
