package com.mygdx.game.Screens.battleDeck.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;
import com.mygdx.game.Tools.Vertex;

import java.util.List;

public class doorImage extends Image {

    public World world;
    public Body b2body;
    public boolean doorOpen;
    public int room1Count;
    public int room2Count;
    public int doorVertex;

/*
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }*/

    public doorImage(World world, battleShipScreen screen, float[][] tempDoor,int iDoor, String doorType){
        super(screen.getDoorAt().findRegion(doorType));
        /*System.out.println("setting weird door start "+tempSize.get(2)+" by "+tempSize.get(3));
        System.out.println("setting weird door end "+tempSize.get(0)+" by "+tempSize.get(1));*/
        setWidth(tempDoor[0][iDoor]);
        setHeight(tempDoor[1][iDoor]);
        setX(tempDoor[2][iDoor]);
        setY(tempDoor[3][iDoor]);
        setBounds(getX(),getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);

        room1Count = Math.round(tempDoor[5][iDoor]);
        room2Count = Math.round(tempDoor[6][iDoor]);
        doorVertex = Math.round(tempDoor[7][iDoor]);

        addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("hello from actor class click");
                //screen.testBool = true;
            }
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(doorType.substring(0, Math.min(doorType.length(), 7)).equals("airlock")){
                    if(doorOpen){
                        // airlock open so close and no longer vent
                        screen.venting = false;
                        screen.vertices[room1Count].vacuuming=false;
                        setDrawable(new TextureRegionDrawable(screen.getDoorAt().findRegion(doorType + "open")));
                        doorOpen = true;
                    } else {
                        // airlock closed so open and start venting
                        screen.venting = true;
                        screen.vertices[room1Count].vacuuming=true;
                        setDrawable(new TextureRegionDrawable(screen.getDoorAt().findRegion(doorType)));
                        doorOpen = false;
                    }
                } else {
                    if (doorOpen) {
                        // door open so make closed
                        setDrawable(new TextureRegionDrawable(screen.getDoorAt().findRegion(doorType)));
                        doorOpen = false;
                        screen.vertices[doorVertex].doorOpen = doorOpen;
                        // lets check whether closing the door has stopped the room being evacuated
                        int vacCount = 0;
                        for(int vert=0;vert<screen.vertices[room1Count].getNeighborCount();vert++){
                            Vertex tempVertex = screen.vertices[room1Count].getNeighbor(vert).getOne();
                            if(tempVertex.thisRoom == Vertex.vertexRoom.door){
                                // found the door
                            } else if(screen.vertices[room1Count].getNeighbor(vert).getTwo().thisRoom == Vertex.vertexRoom.door){
                                // maybe now have found the door
                                tempVertex = screen.vertices[room1Count].getNeighbor(vert).getTwo();
                            } else {
                                tempVertex = null;
                            }
                            if(tempVertex!=null){
                                // found one of the rooms doors
                                if(tempVertex.doorOpen){
                                    // the door is open - check evacuating
                                    if(tempVertex.vacuuming){
                                        vacCount++;
                                    }
                                }
                            }
                        }
                        if(vacCount>0){
                            screen.vertices[room1Count].vacuuming = true;
                        } else {
                            // all doors shut or connecting rooms not vacuuming
                            screen.vertices[room1Count].vacuuming = false;
                        }

                        // Repeat for second room on other side of door
                        vacCount = 0;
                        for(int vert=0;vert<screen.vertices[room2Count].getNeighborCount();vert++){
                            Vertex tempVertex = screen.vertices[room2Count].getNeighbor(vert).getOne();
                            if(tempVertex.thisRoom == Vertex.vertexRoom.door){
                                // found the door
                            } else if(screen.vertices[room2Count].getNeighbor(vert).getTwo().thisRoom == Vertex.vertexRoom.door){
                                // maybe now have found the door
                                tempVertex = screen.vertices[room2Count].getNeighbor(vert).getTwo();
                            } else {
                                tempVertex = null;
                            }
                            if(tempVertex!=null){
                                // found one of the rooms doors
                                if(tempVertex.doorOpen){
                                    // the door is open - check evacuating
                                    if(tempVertex.vacuuming){
                                        vacCount++;
                                    }
                                }
                            }
                        }
                        if(vacCount>0){
                            screen.vertices[room2Count].vacuuming = true;
                        } else {
                            // all doors shut or connecting rooms not vacuuming
                            screen.vertices[room2Count].vacuuming = false;
                        }

                    } else {
                        // door clsoed so make open
                        setDrawable(new TextureRegionDrawable(screen.getDoorAt().findRegion(doorType + "open")));
                        doorOpen = true;
                        screen.vertices[doorVertex].doorOpen = doorOpen;
                        if (screen.vertices[room1Count].vacuuming == true || screen.vertices[room2Count].vacuuming == true) {
                            screen.vertices[room1Count].vacuuming = true;
                            screen.vertices[room2Count].vacuuming = true;
                        }
                    }
                }

                // Switch to player
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hello from actor class up");
                //screen.testBool = true;
            }
        });
    }
/*
    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }*/

/*
    public doorActor(World world, battleShipScreen screen, List<Float> tempSize, String doorType) {

        //super(screen.getDoorAt().findRegion(doorType));
        System.out.println("door type "+doorType);
        this.world = world;
        System.out.println("door sprite "+tempSize.get(0)+" "+tempSize.get(1)+" "+tempSize.get(2)+" "+tempSize.get(3));
        //defineSprite(tempSize);
        //room = new TextureRegion((getTexture()), 0, 0, tempSize.get(0), tempSize.get(1));
        // set bounds is coords first and then width/height
        setBounds(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3)/ MyGdxGame.PPM, tempSize.get(0) / MyGdxGame.PPM, tempSize.get(1) / MyGdxGame.PPM);
    }

    public void update(float dt) {
        // setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void defineSprite(List<Float> tempSize) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3) / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        *//*fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;*//*

        fdef.shape = shape;
        b2body.createFixture(fdef);
*//*
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5 / MyGdxGame.PPM, 5 / MyGdxGame.PPM), new Vector2(5 / MyGdxGame.PPM, -5 / MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor*//*

        //b2body.createFixture(fdef).setUserData("front");
    }*/



}
