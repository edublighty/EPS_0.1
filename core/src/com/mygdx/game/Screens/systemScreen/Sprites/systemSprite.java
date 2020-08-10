package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

import java.util.List;

public class systemSprite extends Image {

    public World world;
    public Body b2body;
    private TextureRegion room;
    private double roomO2;
    private int iRoom;  // BL of room position from ship origin
    private int jRoom;  // BL of room position from ship origin
    private int iNum;   // number of tiles wide
    private int jNum;   // number of tiles tall
    private int systemTypeID;   // iteration of this type of system
    private int arrayNum;
    private int playerGroupNum;
    private int airGroupNum;
    private String roomType;
    private boolean systemON;
    private MyGdxGame game;
    private systemScreen2 screen;
    private int damage;

    private Animation<TextureRegion> starShine;
    private float stateTimer;
    public enum State { SHINING };
    public starImage.State currentState;
    public starImage.State previousState;
    private int frameCount;
    private float frameDuration;
    private String baseString;

    public systemSprite(MyGdxGame game, systemScreen2 screen, String roomType, List<Float> tempSize, float ratio, int i, int j, int iNum, int jNum, int systemCount) {
        super(game.getSystemsAt().findRegion("reactor0"));
        systemON = true;
        this.game = game;
        this.screen = screen;
        this.roomType = roomType;
        this.iRoom=i;
        this.jRoom=j;
        this.iNum=iNum;
        this.jNum=jNum;
        this.arrayNum = systemCount;
        damage = 99;
        setX(tempSize.get(2) / MyGdxGame.PPM);
        setY(tempSize.get(3) / MyGdxGame.PPM);
        setWidth(tempSize.get(0)*ratio / MyGdxGame.PPM);
        setHeight(tempSize.get(1)*ratio / MyGdxGame.PPM);

        stateTimer = 0;
        currentState = starImage.State.SHINING;
        previousState = starImage.State.SHINING;
        frameCount = 0;
        frameDuration = 0.15f;
        baseString = "";
        // oTwo,surveillance,engine,armory,reactor,cockpit,airlock,shields,medbay,cargobay
        switch(roomType){
            case "oTwo":
                // o2
                frameCount = 4;
                baseString = "o2Station";
                break;
            case "surveillance":
                // security room
                frameCount = 1;
                baseString = "surveillanceStation";
                break;
            case "engine":
                // engine room
                frameCount = 13;
                baseString = "engRoom";
                break;
            case "armory":
                // weapon station
                frameCount = 1;
                baseString = "weaponStation";
                break;
            case "reactor":
                // reactor room
                frameCount = 11;
                baseString = "reactor";
                break;
            case "cockpit":
                // cockpit
                frameCount = 10;
                baseString = "cockpit";
                break;
            case "airlock":
                // airlock
                frameCount = 6;
                baseString = "airlockOpen";
                break;
            case "shields":
                // shields station
                frameCount = 4;
                baseString = "shieldGen";
                break;
            case "medbay":
                // medical station
                frameCount = 42;
                baseString = "medbay";
                break;
            case "cargobay":
                // cargo bay
                frameCount = 0;
                baseString = "cargoBay";
                break;
        }


        Array<TextureRegion> frames = new Array<TextureRegion>();

        if(frameCount>0) {
            for (int k = 0; k < frameCount; k++) {
                System.out.println("frame called "+baseString+""+k);
                frames.add(new TextureRegion(game.getSystemsAt().findRegion(baseString + "" + k)));
            }
        } else {
            frames.add(new TextureRegion(game.getSystemsAt().findRegion(baseString + "" + 0)));
        }

        starShine = new Animation(frameDuration,frames);

        addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                //
                //System.out.println("TOUCHING MY HANDLE SON");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //screen.galActors.updateMarkers(Xcoord,Ycoord,width,height,galHUD,systName);

                System.out.println("TOUCHING system "+roomType);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //
                //System.out.println("TOUCHING MY HANDLE SON");
            }
        });
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = starShine.getKeyFrame(stateTimer);
        if(stateTimer > frameDuration*frameCount ){
            stateTimer = 0;
        }

        // Does currentState equal previousState? If so then increment dt or else set 0
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;

    }

    public void update(float dt) {

        if(systemON) {
            if (frameCount > 0) {
                setDrawable(new TextureRegionDrawable(new TextureRegion(getFrame(dt))));
            }
        }
    }

    public int getArrayNum(){ return arrayNum; }

    public void toggleSystem(boolean bool){
        this.systemON = bool;
        if(baseString == "medbay" || baseString == "cargobay"){
            // do nothing
        } else {
            // toggle system on/off
            if(!systemON){
                //systemON = false;
                setDrawable(new TextureRegionDrawable(new TextureRegion(game.getSystemsAt().findRegion(baseString + "OFF"))));
                screen.getShipManager().getSystemManagers()[arrayNum].setSystemOn(false);
            } else {
                //systemON = true;
                setDrawable(new TextureRegionDrawable(new TextureRegion(game.getSystemsAt().findRegion(baseString + "" + 0))));
                stateTimer = 0;
                screen.getShipManager().getSystemManagers()[arrayNum].setSystemOn(true);
            }
        }/*
        if(!systemON){
            // system off
            this.setDrawable(new TextureRegionDrawable(game.getRoomsAt().findRegion(roomType+"Down")));
        } else {
            this.setDrawable(new TextureRegionDrawable(game.getRoomsAt().findRegion(roomType)));
        }*/
    }

    public void setThisGroupNum(int thisGroupNum,int airGroupNum){
        this.playerGroupNum = thisGroupNum;
        this.airGroupNum = airGroupNum;
    }

    public void setDamage(int damage){
        this.damage = damage;
        screen.getShipManager().getSystemManagers()[arrayNum].damageSystem(damage);
    }

    public int getDamage(){
        return damage;
    }

    public int getiRoom(){
        return iRoom;
    }

    public int getjRoom(){
        return jRoom;
    }

    public int getiNum(){ return iNum; }

    public int getjNum(){ return jNum; }

    public String getRoomType(){
        return roomType;
    }

    public void defineSprite(List<Float> tempSize) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3) / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        /*fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;*/

        fdef.shape = shape;
        b2body.createFixture(fdef);
/*
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5 / MyGdxGame.PPM, 5 / MyGdxGame.PPM), new Vector2(5 / MyGdxGame.PPM, -5 / MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor*/

        //b2body.createFixture(fdef).setUserData("front");
    }

    public void setSystemTypeID(int num){
        this.systemTypeID = num;
    }

    public int getSystemTypeID(){
        return systemTypeID;
    }
}
