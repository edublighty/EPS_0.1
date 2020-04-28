package com.mygdx.game.Tools.Managers;

import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class systemManager {

    private String systemName;
    private String systemType;
    private int systemNo;

    private float damage;
    private float thrustMultiplier;

    private boolean avail;
    private boolean damaged;
    private boolean systemOn;

    public systemManager(systemScreen2 screen, String systemName, String systemType, int systemNo) {

        this.systemName = systemName;
        this.systemType = systemType;
        this.systemNo = systemNo;

        damage = 100f;
        avail = true;
        damaged = false;
        systemOn = true;
        thrustMultiplier = 1;

    }

    public float getThrustMultiplier(){
        return thrustMultiplier;
    }

    public float getDamage(float dmg) {
        return damage;
    }

    public void damageSystem(float dmg){damage-=dmg;}

    public void repairSystem(float dmg){damage+=dmg;}

    public boolean getIsAvail() {
        return avail;
    }

    public boolean getIsDamaged() {
        return damaged;
    }

    public boolean getIsSystemOn() {
        return systemOn;
    }

    public void setAvail(boolean avail) {
        this.avail = avail;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public void setSystemOn(boolean systemOn) {
        this.systemOn = systemOn;
    }

    public String getName() {
        return systemName;
    }

    public String getSystemType() {
        return systemType;
    }


}
