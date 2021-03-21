package net.ddns.myapplication.dto;

import java.io.Serializable;

public class NormalAlarmDto extends AlarmDto implements Serializable {
    private String name;
    private Boolean isNameActive;

    public NormalAlarmDto(
            int id, String time, Boolean[] weekList, String soundPath, String vibrationName, String vibrationType,
            int repeatCnt, int repeatTerm, boolean isSoundActive, boolean isVibrationActive, boolean isRepeatActive,
            boolean isActive, String name, Boolean isNameActive
    ) {
        super(
                id, time, weekList, soundPath, vibrationName, vibrationType, repeatCnt,
                repeatTerm, isSoundActive, isVibrationActive, isRepeatActive, isActive
        );
        this.name = name;
        this.isNameActive = isNameActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNameActive() {
        return isNameActive;
    }

    public void setNameActive(Boolean nameActive) {
        isNameActive = nameActive;
    }

    @Override
    public String toString() {
        return "NormalAlarmDto{" +
                "name='" + name + '\'' +
                ", isNameActive=" + isNameActive +
                '}';
    }
}



/*
어플 DB

normalAlarm {
	_id: int,
	name: string,
	time: date,
	weekId: int,
	soundPath: string,
	vibrationId: int,
	repeatCnt: int,
	repeatTerm: int,
	isNameActive: boolean,
	isSoundActive: boolean,
	isVibrationActive: boolean,
	isRepeatActive: boolean,
	isActive: boolean,
}

locationAlarm {
	_id: int,
	latitude: double,
	longitude: double,
	locationName: string,
	radius: float,
	startTime: date,
	endTime: date,
	weekId: int,
	soundPath: string,
	vibrationId: int,
	repeatCnt: int,
	repeatTerm: int,
	isSoundActive: boolean,
	isVibrationActive: boolean,
	isRepeatActive: boolean,
	isActive: boolean,
}

Week{
	_id: int,
	sun: boolean,
	mon: boolean,
	tue: boolean,
	wed: boolean,
	thu: boolean,
	fri: boolean,
	sat: boolean,
}

vibrationType{
	_id: int,
	name: string,
	type: string,
}
 */