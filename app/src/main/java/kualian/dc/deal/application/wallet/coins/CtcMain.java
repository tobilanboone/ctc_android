package kualian.dc.deal.application.wallet.coins;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.wallet.CoinIndex;
import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by admin on 2018/3/26.
 */

public class CtcMain extends CoinType{
    public CtcMain() {
        id = "ctc.main";

        addressHeader = 0;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 128;

        coinName = "CTC";
        coinIndex= CoinIndex.UBCOIN.index;
        coinResource= R.mipmap.home_icon_ctc;
       /* if (key!=null){
            coinAddress= KeyUtil.genSubPubAddrWifFromMasterKey(KeyUtil.genMasterPriKey(SpUtil.getInstance().getWalletSend(key)), coinIndex, MainNetParams.get());
            SpUtil.getInstance().setWalletAddress(coinAddress);

        }*/
    }

   /* private static UbMain instance = new UbMain();
    public static   UbMain get() {
        return instance;
    }*/
}
