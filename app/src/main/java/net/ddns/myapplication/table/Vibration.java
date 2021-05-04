package net.ddns.myapplication.table;

import java.io.Serializable;
import java.util.Arrays;

public class Vibration implements Serializable {
    String name;
    long timimg[];

    public Vibration() {
    }

    public Vibration(String name, long[] timimg) {
        this.name = name;
        this.timimg = timimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long[] getTimimg() {
        return timimg;
    }

    public void setTimimg(long[] timimg) {
        this.timimg = timimg;
    }

    @Override
    public String toString() {
        return "Vibration{" +
                "name='" + name + '\'' +
                ", timimg=" + Arrays.toString(timimg) +
                '}';
    }
}
