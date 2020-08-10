package com.mygdx.game.Tools.Managers;

import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class systemManager {

    private enum systemTypes {oTwo,surveillance,engine,armory,reactor,cockpit,airlock,shields,medbay,cargobay}
    private shipManager manager;
    private systemScreen2 screen;

    private String systemName;
    private String systemType;
    private int systemNo;

    private float damage;
    private float thrustMultiplier;

    private boolean avail;
    private boolean damaged;
    private boolean systemOn;

    public systemManager(systemScreen2 screen, shipManager manager, String systemName, String systemType, int systemNo) {

        this.manager = manager;
        this.screen = screen;

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

    public void damageSystem(float dmg){
        damage-=dmg;
        int floorDmg = (int) Math.floor(damage);
        screen.getSystemActors().updateSystemDamage(systemNo,floorDmg);
    }

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

    public void setIsDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public void setSystemOn(boolean systemOn) {

        this.systemOn = systemOn;
        if(systemType==systemTypes.engine.name()){
            manager.toggleEngines(systemOn);
        }
    }

    public String getName() {
        return systemName;
    }

    public String getSystemType() {
        return systemType;
    }


}
