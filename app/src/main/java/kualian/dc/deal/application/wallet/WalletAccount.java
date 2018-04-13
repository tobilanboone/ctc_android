package kualian.dc.deal.application.wallet;

/**
 * Created by idmin on 2018/2/23.
 */

public class WalletAccount {
    public  String walletName;
    public  String walletId;
    public  String walletLoginPw;
    public  String walletTradePw;
    public  int walletDefaultIndex;
    public  String wallet_seed;
    public  String wallet_is_copy;

    public String getWallet_seed() {
        return wallet_seed;
    }

    public void setWallet_seed(String wallet_seed) {
        this.wallet_seed = wallet_seed;
    }

    public String getWallet_is_copy() {
        return wallet_is_copy;
    }

    public void setWallet_is_copy(String wallet_is_copy) {
        this.wallet_is_copy = wallet_is_copy;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletLoginPw() {
        return walletLoginPw;
    }

    public void setWalletLoginPw(String walletLoginPw) {
        this.walletLoginPw = walletLoginPw;
    }

    public String getWalletTradePw() {
        return walletTradePw;
    }

    public void setWalletTradePw(String walletTradePw) {
        this.walletTradePw = walletTradePw;
    }

    public int getWalletDefaultIndex() {
        return walletDefaultIndex;
    }

    public void setWalletDefaultIndex(int walletDefaultIndex) {
        this.walletDefaultIndex = walletDefaultIndex;
    }


    @Override
    public String toString() {
        return "WalletAccount{" +
                "walletName='" + walletName + '\'' +
                ", walletId='" + walletId + '\'' +
                ", walletLoginPw='" + walletLoginPw + '\'' +
                ", walletTradePw='" + walletTradePw + '\'' +
                ", walletDefaultIndex=" + walletDefaultIndex +
                ", wallet_seed=" + wallet_seed +
                ", wallet_is_copy=" + wallet_is_copy +
                '}';
    }
}
