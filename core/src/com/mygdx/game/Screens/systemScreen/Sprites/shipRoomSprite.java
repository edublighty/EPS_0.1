package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;

import java.util.List;

public class shipRoomSprite extends Image {

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

    public shipRoomSprite(MyGdxGame game, String roomType, List<Float> tempSize, float ratio,int i,int j,int iNum,int jNum) {
        super(game.getRoomsAt().findRegion(roomType));
        this.roomType = roomType;
        this.iRoom=i;
        this.jRoom=j;
        this.iNum=iNum;
        this.jNum=jNum;
        //System.out.println("SHIPSECSPRITE "+roomType+" "+tempSize.get(0)+" "+tempSize.get(1)+" "+tempSize.get(2)+" "+tempSize.get(3));
        //defineSprite(tempSize);
        //room = new TextureRegion((getTexture()), 0, 0, tempSize.get(0), tempSize.get(1));
        // set bounds is coords first and then width/height
        setX(tempSize.get(2) / MyGdxGame.PPM);
        setY(tempSize.get(3) / MyGdxGame.PPM);
        setWidth(tempSize.get(0)*ratio / MyGdxGame.PPM);
        setHeight(tempSize.get(1)*ratio / MyGdxGame.PPM);
        addListener(new ClickListener() {
                        public void clicked(InputEvent event, float x, float y) {
                            //
                            //System.out.println("TOUCHING MY HANDLE SON");
                        }

                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            //screen.galActors.updateMarkers(Xcoord,Ycoord,width,height,galHUD,systName);
                            System.out.println("TOUCHING room "+roomType);
                            return true;
                        }

                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            //
                            //System.out.println("TOUCHING MY HANDLE SON");
                        }
                    });
        //setBounds(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3)/ MyGdxGame.PPM, tempSize.get(0)*ratio / MyGdxGame.PPM, tempSize.get(1)*ratio / MyGdxGame.PPM);
    }

    public void update() {
        System.out.println("in room update");
        if(this.roomO2<100){
            //setTexture();
        }
        // setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void setThisGroupNum(int thisGroupNum,int airGroupNum){
        this.playerGroupNum = thisGroupNum;
        this.airGroupNum = airGroupNum;
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
}
