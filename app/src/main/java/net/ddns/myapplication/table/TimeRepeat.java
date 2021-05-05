package net.ddns.myapplication.table;

import java.io.Serializable;

public class TimeRepeat implements Serializable {
    int minute;
    int count;

    public TimeRepeat() {
    }

    public TimeRepeat(int minute, int count) {
        this.minute = minute;
        this.count = count;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    @Override
    public String toString() {
        return "TimeRepeat{" +
                "minute=" + minute +
                ", count=" + count +
                '}';
    }
}
