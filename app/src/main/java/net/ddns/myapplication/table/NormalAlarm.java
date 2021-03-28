package net.ddns.myapplication.table;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "normal_alarm")
public class NormalAlarm {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String time;
    private int weekId;

    public NormalAlarm(String name, String time, int weekId){
        this.name = name;
        this.time = time;
        this.weekId = weekId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWeekId() {
        return weekId;
    }

    public void setWeekId(int weekId) {
        this.weekId = weekId;
    }

    @Override
    public String toString() {
        return "NormalAlarm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", weekId=" + weekId +
                '}';
    }
}

