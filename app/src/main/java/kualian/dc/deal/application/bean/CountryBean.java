package kualian.dc.deal.application.bean;

/**
 * Created by admin on 2018/4/10.
 */

public class CountryBean  {
    private String CountryZH;
    private String CountryEN;
    private String CountryCode;

    public String getCountryZH() {
        return CountryZH;
    }

    public void setCountryZH(String countryZH) {
        CountryZH = countryZH;
    }

    public String getCountryEN() {
        return CountryEN;
    }

    public void setCountryEN(String countryEN) {
        CountryEN = countryEN;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    @Override
    public String toString() {
        return "CountryBean{" +
                "CountryZH='" + CountryZH + '\'' +
                ", CountryEN='" + CountryEN + '\'' +
                ", CountryCode='" + CountryCode + '\'' +
                '}';
    }
}
