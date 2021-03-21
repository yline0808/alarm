package net.ddns.myapplication;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import net.ddns.myapplication.table.NormalAlarm;

public abstract class AlarmDatabase extends RoomDatabase {
    //데이터베이스를 매번 생성하는건 리소스를 많이사용하므로 싱글톤이 권장된다고한다.
    private static AlarmDatabase INSTANCE;

    public abstract NormalAlarm normalAlarmDao();

    //디비객체생성 가져오기
    public static AlarmDatabase getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, AlarmDatabase.class , "alarm-db").build();

            /*INSTANCE = Room.databaseBuilder(context, TodoDatabase.class , "todo-db")
                    .allowMainThreadQueries() =>이걸 추가해서 AsyncTask를 사용안하고 간편하게할수있지만 오류가많아 실제 앱을 만들때 사용하면 안된다고한다.
                    .build();*/
        }
        return  INSTANCE;
    }

    //디비객체제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
