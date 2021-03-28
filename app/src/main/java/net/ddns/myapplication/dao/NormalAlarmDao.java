package net.ddns.myapplication.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import net.ddns.myapplication.table.NormalAlarm;

import java.util.List;

@Dao
public interface NormalAlarmDao {
    @Query("SELECT * FROM normal_alarm ORDER BY id DESC")
    List<NormalAlarm> getAll();
//    LiveData<List<NormalAlarm>> getAll();

    @Insert
    void insert(NormalAlarm normalAlarm);

    @Update
    void update(NormalAlarm normalAlarm);

    @Delete
    void delete(NormalAlarm normalAlarm);

    @Query("DELETE FROM normal_alarm")
    void deleteAll();
}
