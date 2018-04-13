package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import kualian.dc.deal.application.wallet.CoinType;

/**
 * Created by idmin on 2018/3/8.
 */

public class CoinDao {

    private SQLiteDatabase db;

    public CoinDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    public boolean add(int resource, String address, String coinType,int index,String walletId) {
        ContentValues values = new ContentValues();
        values.put(CoinTable.COINTYPE, coinType);
        values.put(CoinTable.COIN_ADDRESS, address);
        values.put(CoinTable.COIN_RESOURCE, resource);
        values.put(CoinTable.COIN_COININDEX, index);
        values.put(CoinTable.WALLET_ID, walletId);
        long result = db.insert(CoinTable.TABLENAME, null, values);
        return result != -1;
    }

    public List<CoinType> queryAll() {
        Cursor cursor = db.query(CoinTable.TABLENAME, null, null, null, null, null, CoinTable.COINTYPE + " desc");
        List<CoinType> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            CoinType bean = new CoinType();
            bean.setCoinName(cursor.getString(CoinTable.ID_COINTYPE));
            bean.setCoinAddress(cursor.getString(CoinTable.ID_COIN_ADDRESS));
            bean.setCoinResource(cursor.getInt(CoinTable.ID_COIN_RESOURCE));
            bean.setCoinIndex(cursor.getInt(CoinTable.ID_COIN_COININDEX));
            bean.setWalletId(cursor.getString(CoinTable.ID_WALLET_ID));
            list.add(bean);
        }
        cursor.close();
        return list;
    }
    public List<CoinType> queryByWalletId(String args) {
        Cursor cursor = db.query(CoinTable.TABLENAME, null, WalletTable.WALLET_ID + "=?", new String[]{args}, null, null, CoinTable.COINTYPE + " desc");
        List<CoinType> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            CoinType bean = new CoinType();
            bean.setCoinName(cursor.getString(CoinTable.ID_COINTYPE));
            bean.setCoinAddress(cursor.getString(CoinTable.ID_COIN_ADDRESS));
            bean.setCoinResource(cursor.getInt(CoinTable.ID_COIN_RESOURCE));
            bean.setCoinIndex(cursor.getInt(CoinTable.ID_COIN_COININDEX));
            bean.setWalletId(cursor.getString(CoinTable.ID_WALLET_ID));
            list.add(bean);
        }
        cursor.close();
        return list;
    }

    public boolean queryisExist(String keyWord) {
        Cursor cursor = db.query(CoinTable.TABLENAME, null, CoinTable.COINTYPE + "=?", new String[]{keyWord}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean delete(String keyWord) {
        int id = db.delete(CoinTable.TABLENAME, CoinTable.COINTYPE + "=?", new String[]{keyWord});
        return id != -1;
    }

    public boolean update(int index, String address, String coinType, String Key,int coinResource) {
        ContentValues values = new ContentValues();
        values.put(CoinTable.COINTYPE, coinType);
        values.put(CoinTable.COIN_ADDRESS, address);
        values.put(CoinTable.COIN_COININDEX, coinResource);
        values.put(CoinTable.COIN_RESOURCE, index);
        int result = db.update(CoinTable.TABLENAME, values, CoinTable.COINTYPE + "=?", new String[]{Key});
        return result != -1;
    }

    public boolean deleteAll() {
        int id = db.delete(CoinTable.TABLENAME, null, null);
        return id != -1;
    }

    public boolean deleteByWalletId(String args) {
        int id = db.delete(CoinTable.TABLENAME, CoinTable.WALLET_ID + "=?", new String[]{args});
        return id != -1;
    }


}
