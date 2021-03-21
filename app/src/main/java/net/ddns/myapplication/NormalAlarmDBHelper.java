package net.ddns.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NormalAlarmDBHelper extends SQLiteOpenHelper {
    public NormalAlarmDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists normalAlarm ("
                + "_id integer primary key autoincrement,"
                + "name text,"
                + "time text,"
                + "weekId integer,"
                + "soundPath string,"
                + "vibrationId int,"
                + "repeatCnt int,"
                + "repeatTerm int,"
                + "isNameActive int,"
                + "isSoundActive int,"
                + "isVibrationActive int,"
                + "isRepeatActive int,"
                + "isActive int"
                + ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists mytable";

        db.execSQL(sql);
        onCreate(db);
    }
}
