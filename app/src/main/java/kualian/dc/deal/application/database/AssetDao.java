package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/3/8.
 */

public class AssetDao {
    private SQLiteDatabase db;

    public AssetDao() {
        this.db = DatabaseHelper.getDatabase();
    }

    public boolean add(String content) {
        ContentValues values = new ContentValues();
        values.put(AssetTable.JSON, content);
        values.put(AssetTable.WALLET_ID, SpUtil.getInstance().getWalletID());

        long result = db.insert(AssetTable.TABLENAME, null, values);
        return result != -1;
    }



    public String queryAsset() {
        Cursor cursor = db.query(AssetTable.TABLENAME, null, null, null, null, null, AssetTable.ID + " desc");
        String content = null;
        while (cursor.moveToNext()) {
            content = cursor.getString(AssetTable.ID_JSON);
        }
        cursor.close();
        return content;
    }

    public String queryAssetByWalletId(String args) {
        Cursor cursor = db.query(AssetTable.TABLENAME, null, WalletTable.WALLET_ID + "=?", new String[]{args}, null, null, AssetTable.ID + " desc");
        String content = "";
        while (cursor.moveToNext()) {
            content = cursor.getString(AssetTable.ID_JSON);
        }
        cursor.close();
        return content;
    }


    public boolean deleteAll() {
        int id = db.delete(AssetTable.TABLENAME, null, null);
        return id != -1;
    }
    public boolean deleteByWalletId(String args) {
        int id = db.delete(AssetTable.TABLENAME, WalletTable.WALLET_ID + "=?", new String[]{args});
        return id != -1;
    }
}
