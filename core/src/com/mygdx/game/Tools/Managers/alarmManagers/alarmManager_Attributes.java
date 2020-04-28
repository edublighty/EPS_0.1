package com.mygdx.game.Tools.Managers.alarmManagers;

import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class alarmManager_Attributes {

    private String alarmName;
    private String alarmSuperType;
    private String alarmType;

    private boolean alarmTriggered;
    private boolean alarmInhibited;
    private boolean alarmMemorised;

    private int alarmToInhibit;

    private float MIN1;
    private float MAX1;

    public alarmManager_Attributes(systemScreen2 screen, String alarmSuperType, String alarmType, String alarmName, int alarmCount, int inhibitCount) {

        this.alarmName = alarmName;
        this.alarmSuperType = alarmSuperType;
        this.alarmType = alarmType;
        this.alarmToInhibit = inhibitCount;

        alarmTriggered = false;
        alarmInhibited = false;
        alarmMemorised = false;

    }

    public int getAlarmToInhibit(){ return alarmToInhibit; }

    public void setMIN1(float setPoint){MIN1=setPoint;}

    public float getMIN1(){ return MIN1; }

    public void setMAX1(float setPoint){MAX1=setPoint;}

    public float getMAX1(){ return MAX1; }

    public String getAlarmName(){ return alarmName; }

    public String getAlarmType(){ return alarmType; }

    public void setAlarmTriggered(boolean triggered){
        this.alarmTriggered = triggered;
    }

    public void setAlarmInhibited(boolean alarmInhibited) {
        this.alarmInhibited = alarmInhibited;
    }

    public void setAlarmMemorised(boolean alarmMemorised) {
        this.alarmMemorised = alarmMemorised;
    }

    public boolean getAlarmTriggered(){ return alarmTriggered; }

    public boolean getAlarmInhibited(){ return alarmInhibited; }

    public boolean getAlarmMemorised(){ return alarmMemorised; }

}
