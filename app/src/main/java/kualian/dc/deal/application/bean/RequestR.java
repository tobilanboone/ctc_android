package kualian.dc.deal.application.bean;

import java.io.Serializable;

/**
 * Created by admin on 2018/4/10.
 */

public class RequestR implements Serializable {
    private String code;
    private String errorMessage;
    private String extraData;
    private String responsePacket;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(String responsePacket) {
        this.responsePacket = responsePacket;
    }

    public static class responsePacket{
        private String sid;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        @Override
        public String toString() {
            return "responsePacket{" +
                    "sid='" + sid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RequestR{" +
                "code='" + code + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", extraData='" + extraData + '\'' +
                ", responsePacket=" + responsePacket +
                '}';
    }
}
