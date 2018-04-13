package kualian.dc.deal.application.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import kualian.dc.deal.application.WalletApp;

/**
 * Created by Meiji on 2017/3/10.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AnyBit";
    private static final int DB_VERSION = 5;
    private static final String CLEAR_TABLE_DATA = "delete from ";
    private static final String DROP_TABLE = "drop table if exists ";
    private static DatabaseHelper instance = null;
    private static SQLiteDatabase db = null;

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper(WalletApp.getContext(), DB_NAME, null, DB_VERSION);
        }
        return instance;
    }

    public static synchronized SQLiteDatabase getDatabase() {
        if (db == null) {
            db = getInstance().getWritableDatabase();
        }
        return db;
    }

    public static synchronized void closeDatabase() {
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContactTable.CREATE_TABLE);
        db.execSQL(AssetTable.CREATE_TABLE);
        db.execSQL(CoinTable.CREATE_TABLE);
        db.execSQL(WalletTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      /*  switch (oldVersion) {
            case 1:
                db.execSQL(MediaChannelTable.CREATE_TABLE);
                break;
            case 2:
                db.execSQL(CLEAR_TABLE_DATA + NewsChannelTable.TABLENAME);
                break;
            case 3:
                ContentValues values = new ContentValues();
                values.put(NewsChannelTable.ID, "");
                values.put(NewsChannelTable.NAME, "推荐");
                values.put(NewsChannelTable.IS_ENABLE, 0);
                values.put(NewsChannelTable.POSITION, 46);
                db.insert(NewsChannelTable.TABLENAME, null, values);
                break;
            case 4:
                db.execSQL(ContactTable.CREATE_TABLE);
                break;
        }*/
    }
}
