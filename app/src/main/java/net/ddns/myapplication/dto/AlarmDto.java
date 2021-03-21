package net.ddns.myapplication.dto;

import java.util.Arrays;

public class AlarmDto {
    private int id;
    private String time;
    private Boolean[] weekList = new Boolean[7];
    private String soundPath;
    private String vibrationName;
    private String vibrationType;
    private int repeatCnt;
    private int repeatTerm;
    private boolean isSoundActive;
    private boolean isVibrationActive;
    private boolean isRepeatActive;
    private boolean isActive;

    public AlarmDto(
            int id, String time, Boolean[] weekList, String soundPath,
            String vibrationName, String vibrationType, int repeatCnt, int repeatTerm,
            boolean isSoundActive, boolean isVibrationActive, boolean isRepeatActive, boolean isActive
    ) {
        this.id = id;
        this.time = time;
        this.weekList = weekList;
        this.soundPath = soundPath;
        this.vibrationName = vibrationName;
        this.vibrationType = vibrationType;
        this.repeatCnt = repeatCnt;
        this.repeatTerm = repeatTerm;
        this.isSoundActive = isSoundActive;
        this.isVibrationActive = isVibrationActive;
        this.isRepeatActive = isRepeatActive;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean[] getWeekList() {
        return weekList;
    }

    public void setWeekList(Boolean[] weekList) {
        this.weekList = weekList;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public String getVibrationName() {
        return vibrationName;
    }

    public void setVibrationName(String vibrationName) {
        this.vibrationName = vibrationName;
    }

    public String getVibrationType() {
        return vibrationType;
    }

    public void setVibrationType(String vibrationType) {
        this.vibrationType = vibrationType;
    }

    public int getRepeatCnt() {
        return repeatCnt;
    }

    public void setRepeatCnt(int repeatCnt) {
        this.repeatCnt = repeatCnt;
    }

    public int getRepeatTerm() {
        return repeatTerm;
    }

    public void setRepeatTerm(int repeatTerm) {
        this.repeatTerm = repeatTerm;
    }

    public boolean isSoundActive() {
        return isSoundActive;
    }

    public void setSoundActive(boolean soundActive) {
        isSoundActive = soundActive;
    }

    public boolean isVibrationActive() {
        return isVibrationActive;
    }

    public void setVibrationActive(boolean vibrationActive) {
        isVibrationActive = vibrationActive;
    }

    public boolean isRepeatActive() {
        return isRepeatActive;
    }

    public void setRepeatActive(boolean repeatActive) {
        isRepeatActive = repeatActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "AlarmDto{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", weekList=" + Arrays.toString(weekList) +
                ", soundPath='" + soundPath + '\'' +
                ", vibrationName='" + vibrationName + '\'' +
                ", vibrationType='" + vibrationType + '\'' +
                ", repeatCnt=" + repeatCnt +
                ", repeatTerm=" + repeatTerm +
                ", isSoundActive=" + isSoundActive +
                ", isVibrationActive=" + isVibrationActive +
                ", isRepeatActive=" + isRepeatActive +
                ", isActive=" + isActive +
                '}';
    }
}
