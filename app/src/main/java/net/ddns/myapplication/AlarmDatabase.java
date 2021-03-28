package net.ddns.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import net.ddns.myapplication.dao.NormalAlarmDao;
import net.ddns.myapplication.table.NormalAlarm;

@Database(entities = {NormalAlarm.class}, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {
    //데이터베이스를 매번 생성하는건 리소스를 많이사용하므로 싱글톤이 권장된다고한다.
    private static AlarmDatabase INSTANCE;

    public abstract NormalAlarmDao normalAlarmDao();

    //디비객체생성 가져오기
    public static AlarmDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
//            INSTANCE = Room.databaseBuilder(context, AlarmDatabase.class , "normal_alarm").build();

//            INSTANCE = Room.databaseBuilder(
//                    context.getApplicationContext(), AlarmDatabase.class , "normal_alarm"
//            ).allowMainThreadQueries().build();

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AlarmDatabase.class , "normal_alarm")
                    .allowMainThreadQueries()
                    .build();
        }
        return  INSTANCE;
    }

    //디비객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
