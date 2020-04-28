package com.mygdx.game.Tools.Managers;

import com.mygdx.game.Screens.systemScreen.Sprites.shipRoomSprite;
import com.mygdx.game.Screens.systemScreen.Stage.systemScreenActors;
import com.mygdx.game.Screens.systemScreen.systemScreen2;
import com.mygdx.game.Tools.Managers.alarmManagers.alarmManager_Attributes;
import com.mygdx.game.Tools.Managers.alarmManagers.alarmManager_Systems;

public class shipManager {

    private systemScreen2 screen;
    private systemScreenActors systemActors;
    private shipRoomSprite[] playerRoomsSystems;
    private enum systemTypes {oTwo,surveillance,engine,armory,reactor,cockpit,airlock,shields,medbay,cargobay}
    private systemManager systems[];
    private alarmManager_Systems alarms_sys[];
    private alarmManager_Attributes alarms_atts[];
    private enum alarmSuperType {system,attribute,enemy};
    private enum alarmTypes {system_unavailable,system_damaged,system_off,attribute_low,attribute_high,enemy_detected,enemy_engaging,temp_low,temp_high,temp_critical,rads_high,rads_critical,shields_low,o2_low,power_low,health_low};
    private double shipTemp;
    private double shipRads;
    private float shipShields;
    private float shipHealth;

    public shipManager(systemScreen2 screen, systemScreenActors systemActors) {

        // links to other classes
        this.systemActors = systemActors;
        this.screen = screen;

        // initialise ship attributes
        shipTemp = 20f;
        shipRads = 0f;
        shipShields = 99f;
        shipHealth = 99f;

        // initialise system type counters
        int numO2 = 0;
        int numSurv = 0;
        int numEngines = 0;
        int numArmory = 0;
        int numReactor = 0;
        int numCockpit = 0;
        int numAirlock = 0;
        int numShields = 0;
        int numMedbay = 0;
        int numCargo = 0;

        // get ship systems
        playerRoomsSystems = screen.playerShipShown.getSystems();
        int numSystems = screen.playerShipShown.getNoSystems();
        int numAlarms = numSystems*3+7;
        int alarmCount = 0;
        systems = new systemManager[numSystems];
        alarms_sys = new alarmManager_Systems[numAlarms];
        alarms_atts = new alarmManager_Attributes[numAlarms];

        for(int i=0; i<numSystems; i++){

            String tempSystem = playerRoomsSystems[i].getRoomType();
            int tempNum = 0;

            switch (tempSystem) {
                case "oTwo":
                    numO2++;
                    tempNum = numO2;
                    break;
                case "surveillance":
                    numSurv++;
                    tempNum = numSurv;
                    break;
                case "engine":
                    numEngines++;
                    tempNum = numEngines;
                    break;
                case "armory":
                    numArmory++;
                    tempNum = numArmory;
                    break;
                case "reactor":
                    numReactor++;
                    tempNum = numReactor;
                    break;
                case "cockpit":
                    numCockpit++;
                    tempNum = numCockpit;
                    break;
                case "airlock":
                    numAirlock++;
                    tempNum = numAirlock;
                    break;
                case "shields":
                    numShields++;
                    tempNum = numShields;
                    break;
                case "medbay":
                    numMedbay++;
                    tempNum = numMedbay;
                    break;
                case "cargobay":
                    numCargo++;
                    tempNum = numCargo;
                    break;
                default:
                    // default nothing
                    System.out.println("UNEXPECTED RESULT IN SHIPMANAGER SWITCH");
            }

            String systemType = tempSystem;
            String systemName = tempSystem+" "+tempNum;
            int systemID = i;
            // new system manager
            systems[i] = new systemManager(screen,systemName,systemType,systemID);
            // new alarm - damaged
            String alarmDamaged = systemName+" damaged";
            String alarmSuperType = ""+shipManager.alarmSuperType.system;
            String alarmType = ""+alarmTypes.system_damaged;
            alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType, alarmDamaged,systemName,systemType,systemID,alarmCount);
            alarmCount++;
            // new alarm - unavailable
            String alarmUnavail = systemName+" unavailable";
            alarmSuperType = ""+shipManager.alarmSuperType.system;
            alarmType = ""+alarmTypes.system_unavailable;
            alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmUnavail,systemName,systemType,systemID,alarmCount);
            alarmCount++;

            // create additional alarms
            // alarms concerning vital systems off
            String alarmOff;// = systemName+" off";
            switch (tempSystem) {
                case "oTwo":
                    // new alarm - off
                    alarmOff = systemName+" off";
                    alarmSuperType = ""+shipManager.alarmSuperType.system;
                    alarmType = ""+alarmTypes.system_off;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmOff,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    // o2 low
                    String alarmName = "O2 Low";
                    alarmSuperType = ""+shipManager.alarmSuperType.attribute;
                    alarmType = ""+alarmTypes.o2_low;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmName,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    break;
                case "engine":
                    // new alarm - off
                    alarmOff = systemName+" off";
                    alarmSuperType = ""+shipManager.alarmSuperType.system;
                    alarmType = ""+alarmTypes.system_off;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmOff,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    break;
                case "reactor":
                    // new alarm - off
                    alarmOff = systemName+" off";
                    alarmSuperType = ""+shipManager.alarmSuperType.system;
                    alarmType = ""+alarmTypes.system_off;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmOff,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    // power low
                    alarmName = "Power Low";
                    alarmSuperType = ""+shipManager.alarmSuperType.attribute;
                    alarmType = ""+alarmTypes.power_low;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmName,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    break;
                case "shields":
                    // new alarm - off
                    alarmOff = systemName+" off";
                    alarmSuperType = ""+shipManager.alarmSuperType.system;
                    alarmType = ""+alarmTypes.system_off;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmOff,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    // shields low
                    alarmName = "Shields Low";
                    alarmSuperType = ""+shipManager.alarmSuperType.attribute;
                    alarmType = ""+alarmTypes.shields_low;
                    alarms_sys[alarmCount] = new alarmManager_Systems(screen,alarmSuperType,alarmType,alarmName,systemName,systemType,systemID,alarmCount);
                    alarmCount++;
                    break;
                default:
                    // default nothing as systems not vital
            }

    }

        // then create attribute based alarms
        // Temp high
        String alarmName = "High Temperature";
        String alarmSuperType = ""+shipManager.alarmSuperType.attribute;
        String alarmType = ""+alarmTypes.temp_high;
        alarms_atts[alarmCount] = new alarmManager_Attributes(screen,alarmSuperType,alarmType,alarmName,alarmCount,-1);
        alarms_atts[alarmCount].setMAX1(150f);  // set max1 temp at 150 degC
        alarmCount++;
        // Temp CRITICAL
        alarmName = "CRITICAL Temperature";
        alarmSuperType = ""+shipManager.alarmSuperType.attribute;
        alarmType = ""+alarmTypes.temp_critical;
        alarms_atts[alarmCount] = new alarmManager_Attributes(screen,alarmSuperType,alarmType,alarmName,alarmCount,(alarmCount-1));
        alarms_atts[alarmCount].setMAX1(300f);  // set max2 temp at 300 degC
        alarmCount++;
        // Rad high
        alarmName = "High Radiation";
        alarmSuperType = ""+shipManager.alarmSuperType.attribute;
        alarmType = ""+alarmTypes.rads_high;
        alarms_atts[alarmCount] = new alarmManager_Attributes(screen,alarmSuperType,alarmType,alarmName,alarmCount,-1);
        alarms_atts[alarmCount].setMAX1(200f);  // set max1 rads at 200 rad/s
        alarmCount++;
        // Rad CRITICAL
        alarmName = "CRITICAL Radiation";
        alarmSuperType = ""+shipManager.alarmSuperType.attribute;
        alarmType = ""+alarmTypes.rads_critical;
        alarms_atts[alarmCount] = new alarmManager_Attributes(screen,alarmSuperType,alarmType,alarmName,alarmCount,(alarmCount-1));
        alarms_atts[alarmCount].setMAX1(500f);  // set max2 rads at 500 rad/s
        alarmCount++;
        // Enemy detected
        alarmName = "Enemy Detected";
        alarmSuperType = ""+shipManager.alarmSuperType.enemy;
        alarmType = ""+alarmTypes.enemy_detected;
        alarms_atts[alarmCount] = new alarmManager_Attributes(screen,alarmSuperType,alarmType,alarmName,alarmCount,-1);
        alarmCount++;
        // Enemy engaged
        alarmName = "Enemy Engaged";
        alarmSuperType = ""+shipManager.alarmSuperType.enemy;
        alarmType = ""+alarmTypes.enemy_engaging;
        alarms_atts[alarmCount] = new alarmManager_Attributes(screen,alarmSuperType,alarmType,alarmName,alarmCount,-1);
        alarmCount++;

    }

    public double getShipTemp(){ return shipTemp; }

    public double getShipRads(){ return shipRads; }

    public float getShipShields(){ return 0; }

    public float getShipHealth(){ return 0; }

    public void updateShipTemp(double newTemp,float dt){
        shipTemp = newTemp;
        systemActors.updateTemp(newTemp);

        boolean tempHigh = false;
        boolean tempCritical = false;
        float tempMAX1 = 150;
        float tempMAX2 = 300;

        for(int i=0; i<alarms_atts.length; i++){
            if(alarms_atts[i]==null){
                //
            } else {
                // check MAX1 temp
                if (alarms_atts[i].getAlarmType().equals(""+alarmTypes.temp_high)) {
                    if (newTemp >= alarms_atts[i].getMAX1()) {
                        // temp higher than alarm set point - trigger if not already
                        tempHigh = true;
                        tempMAX1 = alarms_atts[i].getMAX1();
                        if (!alarms_atts[i].getAlarmTriggered()) {
                            // trigger on
                            alarms_atts[i].setAlarmTriggered(true);
                            alarms_atts[i].setAlarmMemorised(true);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    } else {
                        if (alarms_atts[i].getAlarmTriggered()) {
                            // trigger off
                            alarms_atts[i].setAlarmTriggered(false);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    }
                }
                // check MAX2 temp
                if (alarms_atts[i].getAlarmType().equals(""+alarmTypes.temp_critical)) {
                    if (newTemp >= alarms_atts[i].getMAX1()) {
                        // temp higher than alarm set point - trigger if not already
                        tempCritical = true;
                        tempMAX2 = alarms_atts[i].getMAX1();
                        if (!alarms_atts[i].getAlarmTriggered()) {
                            // trigger on
                            alarms_atts[i].setAlarmTriggered(true);
                            alarms_atts[i].setAlarmMemorised(true);
                            alarms_atts[alarms_atts[i].getAlarmToInhibit()].setAlarmInhibited(true);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    } else {
                        if (alarms_atts[i].getAlarmTriggered()) {
                            // trigger off
                            alarms_atts[i].setAlarmTriggered(false);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    }
                }
            }
        }
        // checked alarms
        // carry out effects from temperature damage
        float shieldDamage = 0;
        float hullDamage = 0;
        if(tempCritical){
            // temp critical
            // shields take double damage above MAX2 threshold
            // if shields down, hull takes damage
            float totalDamage = (float) ((newTemp-tempMAX2)*2+tempMAX1)*(1/tempMAX1)*dt;
            if(checkShieldsUp()){
                shieldDamage = totalDamage/2;
                hullDamage = shieldDamage;
            } else {
                hullDamage = totalDamage;
            }
        } else if(tempHigh){
            // temp high
            // shields take normal damage above MAX1 threshold
            float totalDamage = (float) (newTemp-tempMAX1)*(1/tempMAX1)*dt;
            if(checkShieldsUp()) {
                shieldDamage = totalDamage;
            } else {
                hullDamage = totalDamage;
            }
        } else {
            // temp below MAX1 threshold
            //
        }
        if(shieldDamage>0){
            // update shields
            shipShields -= shieldDamage;
            System.out.println("sheilds ship manager "+shipShields);
            systemActors.updateShields(( (int) Math.floor(shipShields) ));
        }

        if(hullDamage>0){
            // update hull
            shipHealth -= hullDamage;
            System.out.println("hull ship manager "+shipHealth);
            systemActors.updateHealth( (int) Math.floor(shipHealth) );
        }

    }

    public boolean checkShieldsUp(){

        boolean shieldsUp = false;

        for(int i=0; i<systems.length; i++){
            if(systems[i].getSystemType().equals(""+systemTypes.shields)){
                if(systems[i].getIsSystemOn()){
                    if(systems[i].getIsAvail()){
                        if(!systems[i].getIsDamaged()){
                            shieldsUp = true;
                        }
                    }
                }
            }
        }

        return shieldsUp;
    }

    public void updateShipRad(double newRad){
        shipRads = newRad;
        systemActors.updateRad(newRad);

        for(int i=0; i<alarms_atts.length; i++){
            if(alarms_atts[i]==null){
                //
            } else {
                if (alarms_atts[i].getAlarmType().equals(""+alarmTypes.rads_high)) {
                    if (newRad >= alarms_atts[i].getMAX1()) {
                        // temp higher than alarm set point - trigger if not already
                        if (!alarms_atts[i].getAlarmTriggered()) {
                            // trigger on
                            alarms_atts[i].setAlarmTriggered(true);
                            alarms_atts[i].setAlarmMemorised(true);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    } else {
                        if (alarms_atts[i].getAlarmTriggered()) {
                            // trigger off
                            alarms_atts[i].setAlarmTriggered(false);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    }
                }
                // check MAX2 temp
                if (alarms_atts[i].getAlarmType().equals(""+alarmTypes.rads_critical)) {
                    if (newRad >= alarms_atts[i].getMAX1()) {
                        // temp higher than alarm set point - trigger if not already
                        if (!alarms_atts[i].getAlarmTriggered()) {
                            // trigger on
                            alarms_atts[i].setAlarmTriggered(true);
                            alarms_atts[i].setAlarmMemorised(true);
                            alarms_atts[alarms_atts[i].getAlarmToInhibit()].setAlarmInhibited(true);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    } else {
                        if (alarms_atts[i].getAlarmTriggered()) {
                            // trigger off
                            alarms_atts[i].setAlarmTriggered(false);
                            // update alarm panel
                            updateAlarmPanel();
                        }
                    }
                }
            }
        }
    }

    public void updateShipShields(float newShields){
        shipShields = newShields;
    }

    public void updateShipHealth(float newHealth){
        shipHealth = newHealth;
    }

    public systemManager[] getSystemManagers(){return systems;}

    public alarmManager_Attributes[] getAlarmManagers_atts(){ return alarms_atts; }

    public alarmManager_Systems[] getAlarmManagers_sys(){ return alarms_sys; }

    public void updateAlarmPanel(){
        systemActors.clearAlarmTable();
        for(int i=0; i<alarms_atts.length;i++) {
            if(alarms_atts[i]==null){
                //
            } else {
                if(alarms_atts[i].getAlarmInhibited()){
                    // dont display if not inhibited
                } else if(alarms_atts[i].getAlarmMemorised()){
                    // ensure alarm continues to show until memorisation negated by clear button
                    systemActors.addAlarm(alarms_atts[i].getAlarmName());
                } else {
                    if (alarms_atts[i].getAlarmTriggered()) {
                        // alarm is triggered so display if not inhibited
                        systemActors.addAlarm(alarms_atts[i].getAlarmName());
                    }
                }
            }
        }
    }

    public void checkSystems(){

        // sub called in the event that a change is expected
        // output all systems to their dependents (images etc)
        for(int i=0; i<systems.length; i++){

        }
    }


}
