package kualian.dc.deal.application.database;

import kualian.dc.deal.application.util.SpUtil;

/**
 * Created by idmin on 2018/3/22.
 */

public class RecordTable {
    public static final String TABLENAME = "RecordTable";

    public static final String ID = "id";
    public static final String JSON = "json";

    public static final int ID_ID = 0;
    public static final int ID_JSON = 1;

    /**
     * 创建表
     */
    public static final String CREATE_TABLE = "create table if not exists " + TABLENAME + "(" +
            ID + " text auto_increment, " +
            JSON + " text) ";
}
