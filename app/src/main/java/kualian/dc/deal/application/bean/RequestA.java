package kualian.dc.deal.application.bean;

import java.io.Serializable;

import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by admin on 2018/4/10.
 */

public class RequestA implements Serializable {
    private String sid;
    private String version;
    private String format;
    private String action;
    private String encrypt;
    private String packet;

    public RequestA() {
    }

    public RequestA(packet packet) {
        /*String packetString = "";
        byte[] encode = new byte[0];
        String json = new Gson().toJson(packet);
        if(packet.getActionCode().equals("100000")){
            try {
                PublicKey publicKey = RSA.getPublicKey("ctc-app-test-public.cer");
                String encrypt = RSA.encrypt(publicKey, json.getBytes());
                packetString = UrlUtils.toURLEncoded(encrypt);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                encode = Base64.encode(AES.encrypt(SpUtil.getInstance().getString(Constants.AES_KEY).getBytes(), json.getBytes()).getBytes(), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.sid = SpUtil.getInstance().getString(Constants.SID);
        this.version = SpUtil.getInstance().getDefaultVersion();
        this.format = "json";
        this.action = packet.getActionCode();
        this.encrypt = "1";
        this.packet = packetString;*/

    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }

    public String getPacket() {
        return packet;
    }

    public void setPacket(String packet) {
        this.packet = packet;
    }

    public static class packet{
        private String appVersion;
        private String actionCode;
        private String deviceId;
        private String deviceBrand;
        private String osName;
        private String osVersion;
        private String deviceModel;
        private String key;
        private String username;
        private String password;
        private String verifyCode;



        public packet(String actionCode) {
            this.actionCode = actionCode;
            this.appVersion = SpUtil.getInstance().getDefaultVersion();

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getVerifyCode() {
            return verifyCode;
        }

        public void setVerifyCode(String verifyCode) {
            this.verifyCode = verifyCode;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getActionCode() {
            return actionCode;
        }

        public void setActionCode(String actionCode) {
            this.actionCode = actionCode;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceBrand() {
            return deviceBrand;
        }

        public void setDeviceBrand(String deviceBrand) {
            this.deviceBrand = deviceBrand;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "packet{" +
                    "appVersion='" + appVersion + '\'' +
                    ", actionCode='" + actionCode + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", deviceBrand='" + deviceBrand + '\'' +
                    ", osName='" + osName + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    ", deviceModel='" + deviceModel + '\'' +
                    ", key='" + key + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", verifyCode='" + verifyCode + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RequestA{" +
                "sid='" + sid + '\'' +
                ", version='" + version + '\'' +
                ", format='" + format + '\'' +
                ", action='" + action + '\'' +
                ", encrypt='" + encrypt + '\'' +
                ", packet=" + packet +
                '}';
    }
}
