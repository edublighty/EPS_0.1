package com.mygdx.game.Screens.systemScreen.Sprites;

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
import com.mygdx.game.MyGdxGame;

import java.util.List;

public class alarmButtons extends Image {

    public World world;
    public Body b2body;
    private TextureRegion room;
    private double roomO2;
    private int iRoom;  // BL of room position from ship origin
    private int jRoom;  // BL of room position from ship origin
    private int iNum;   // number of tiles wide
    private int jNum;   // number of tiles tall
    private int playerGroupNum;
    private int airGroupNum;
    private String roomType;
    private boolean roomTxt;
    private int arrayNum;
    private boolean systemON;
    private systemScreenShipGroup shipGroup;
    MyGdxGame game;
    private float aspect;

    public alarmButtons(MyGdxGame game,String buttonType,int alarmNo) {
        super(game.getRoomsAt().findRegion(buttonType));
        this.game = game;

        aspect = game.getRoomsAt().findRegion(buttonType).getRegionWidth() / game.getRoomsAt().findRegion(buttonType).getRegionHeight();
        setWidth(200f);
        setHeight(this.getWidth()/aspect);

    }

    public void update() {
    }

    public float getAspect(){ return aspect; }

}
