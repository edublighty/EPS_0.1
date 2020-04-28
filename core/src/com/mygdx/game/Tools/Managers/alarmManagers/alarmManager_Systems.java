package com.mygdx.game.Tools.Managers.alarmManagers;

import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class alarmManager_Systems {

    private String alarmName;
    private String alarmSuperType;
    private String alarmType;
    private String systemName;
    private String systemType;
    private int systemNo;

    private boolean alarmTriggered;
    private boolean alarmInhibited;

    public alarmManager_Systems(systemScreen2 screen, String alarmSuperType, String alarmType, String alarmName, String systemName, String systemType, int systemNo, int alarmCount) {

        this.alarmName = alarmName;
        this.alarmSuperType = alarmSuperType;
        this.alarmType = alarmType;
        this.systemName = systemName;
        this.systemType = systemType;
        this.systemNo = systemNo;

        alarmTriggered = false;
        alarmInhibited = false;

    }

    public String getAlarmName(){ return alarmName; }

    public String getAlarmSystemName() {
        return systemName;
    }

    public String getAlarmSystemType() { return systemType; }

    public int getAlarmSystemNo() { return systemNo; }

    public void setAlarmTriggered(boolean triggered){
        this.alarmTriggered = triggered;
    }

    public void setAlarmInhibited(boolean alarmInhibited) {
        this.alarmInhibited = alarmInhibited;
    }

    public boolean getAlarmTriggered(){ return alarmTriggered; }

    public boolean getAlarmInhibited(){ return alarmInhibited; }
}
