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

public class shipRoomButton extends Image {

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

    public shipRoomButton(MyGdxGame game, systemScreenShipGroup shipGroup, String roomType, boolean roomTxt, int systemCount) {
        super(game.getRoomsAt().findRegion(roomType));
        this.game = game;
        this.roomType = roomType;
        this.roomTxt = roomTxt;
        this.arrayNum = systemCount;
        this.systemON = true;
        this.shipGroup = shipGroup;
        //System.out.println("SHIPSECSPRITE "+roomType+" "+tempSize.get(0)+" "+tempSize.get(1)+" "+tempSize.get(2)+" "+tempSize.get(3));
        //defineSprite(tempSize);
        //room = new TextureRegion((getTexture()), 0, 0, tempSize.get(0), tempSize.get(1));
        // set bounds is coords first and then width/height
        float aspect = game.getRoomsAt().findRegion(roomType).getRegionWidth() / game.getRoomsAt().findRegion(roomType).getRegionHeight();
        setX(50f);
        setY(50f);
        setWidth(200f);
        setHeight(this.getWidth()/aspect);
        if(!roomTxt) {
            addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    //
                    //System.out.println("TOUCHING MY HANDLE SON");
                }

                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    //screen.galActors.updateMarkers(Xcoord,Ycoord,width,height,galHUD,systName);
                    if(systemON){
                        // turn off
                        systemON = false;
                        shipGroup.toggleSystems(systemON,arrayNum);
                    } else {
                        // turn on
                        systemON = true;
                        shipGroup.toggleSystems(systemON,arrayNum);
                    }
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    //
                    //System.out.println("TOUCHING MY HANDLE SON");
                }
            });
        }
        //setBounds(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3)/ MyGdxGame.PPM, tempSize.get(0)*ratio / MyGdxGame.PPM, tempSize.get(1)*ratio / MyGdxGame.PPM);
    }

    public void update() {
    }

    public void updateDamage(int damage){
        this.setDrawable(new TextureRegionDrawable(game.getRoomsAt().findRegion("damage"+damage)));
    }

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
}
