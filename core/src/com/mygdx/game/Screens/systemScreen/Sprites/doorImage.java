package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;
import com.mygdx.game.Screens.systemScreen.Tools.Vertex;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

import java.util.List;

public class doorImage extends Image {

    public World world;
    public Body b2body;
    private TextureRegion room;
    public int room1Count;
    public int room2Count;
    public int doorVertex;
    public boolean doorOpen;
    public boolean airlock;
    private float stateTimer;
    public enum State { NEUTRAL, OPENING, CLOSING };
    public doorImage.State currentState;
    public doorImage.State previousState;
    private int frameCount;
    private float frameDuration;
    private Animation<TextureRegion> doorOpening;
    private Animation<TextureRegion> doorClosing;

    public doorImage(World world, systemScreen2 screen, MyGdxGame game, float[] tempSize, String doorType, float room1CountA,float room2CountA,float doorVertexA,float airlockNum) {
        super(game.getShipObjsAt().findRegion(doorType));
        System.out.println("door type "+doorType);
        if(airlockNum>0){
            //airlock
            airlock = true;
        } else {
            airlock = false;
        }

        frameDuration = 1f;
        currentState = State.NEUTRAL;
        previousState = State.NEUTRAL;
        stateTimer = 0;

        if(airlock){
            // create opening animation
            frameCount = 9;
            Array<TextureRegion> frames = new Array<TextureRegion>();
            for(int i = 1; i < frameCount; i++){
                //System.out.println("sprite i "+i);
                frames.add(new TextureRegion(game.getDoorsAt().findRegion("ExternalDoor-"+Integer.toString(i))));
            }

            doorOpening = new Animation(frameDuration,frames);

            frames = new Array<TextureRegion>();
            for(int i = frameCount; i > 0; i--){
                //System.out.println("sprite i "+i);
                frames.add(new TextureRegion(game.getDoorsAt().findRegion("ExternalDoor-"+Integer.toString(i))));
            }

            doorClosing = new Animation(frameDuration,frames);

            this.setDrawable(new TextureRegionDrawable(game.getDoorsAt().findRegion("ExternalDoor-"+Integer.toString(1))));
        } else {
            frameCount = 8;
            // create opening animation
            Array<TextureRegion> frames = new Array<TextureRegion>();
            for(int i = 1; i < frameCount; i++){
                //System.out.println("sprite i "+i);
                frames.add(new TextureRegion(game.getDoorsAt().findRegion("InternalDoor-"+Integer.toString(i))));
            }

            doorOpening = new Animation(frameDuration,frames);

            frames = new Array<TextureRegion>();
            for(int i = frameCount; i > 0; i--){
                //System.out.println("sprite i "+i);
                frames.add(new TextureRegion(game.getDoorsAt().findRegion("InternalDoor-"+Integer.toString(i))));
            }

            doorClosing = new Animation(frameDuration,frames);

            this.setDrawable(new TextureRegionDrawable(game.getDoorsAt().findRegion("InternalDoor-"+Integer.toString(1))));
        }

        this.world = world;
        System.out.println("door sprite "+tempSize[0]+" "+tempSize[1]+" "+tempSize[2]+" "+tempSize[3]);
        //defineSprite(tempSize);
        //room = new TextureRegion((getTexture()), 0, 0, tempSize.get(0), tempSize.get(1));
        // set bounds is coords first and then width/height
        setBounds(tempSize[2]/ MyGdxGame.PPM, tempSize[3]/ MyGdxGame.PPM, tempSize[0]/ MyGdxGame.PPM, tempSize[1]/ MyGdxGame.PPM);

        setTouchable(Touchable.enabled);

        doorOpen = false;

        this.room1Count = (int) room1CountA;
        this.room2Count = (int) room2CountA;
        this.doorVertex = (int) doorVertexA;

        addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                //System.out.println("hello from actor class click");
                //screen.testBool = true;
            }
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("clocking door");
                if(airlock){
                    // its an airlock
                    if(doorOpen){
                        currentState = State.CLOSING;
                        // airlock open so close and no longer vent
                        System.out.println("closing");
                        //screen.venting = false;
                        //screen.vertices[room1Count].vacuuming=false;
                        screen.checkAirlocks();
                        screen.checkRoomVenting();
                        //setDrawable(new TextureRegionDrawable(game.getShipObjsAt().findRegion(doorType)));
                        doorOpen = false;

                    } else {
                        currentState = State.OPENING;
                        // airlock closed so open and start venting
                        System.out.println("opening");
                        screen.venting = true;
                        screen.vertices[room1Count].vacuuming=true;
                        //setDrawable(new TextureRegionDrawable(game.getShipObjsAt().findRegion(doorType+ "open")));
                        doorOpen = true;
                    }
                } else {
                    // its a normal door
                    if (doorOpen) {
                        // door open so make closed
                        currentState = State.CLOSING;
                        //setDrawable(new TextureRegionDrawable(game.getShipObjsAt().findRegion(doorType)));
                        doorOpen = false;
                        screen.vertices[doorVertex].doorOpen = doorOpen;
                        // lets check whether closing the door has stopped the room being evacuated
                        /*int vacCount = 0;
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
*/
                    } else {
                        // door clsoed so make open
                        currentState = State.OPENING;
                        //setDrawable(new TextureRegionDrawable(game.getShipObjsAt().findRegion(doorType + "open")));
                        doorOpen = true;
                        screen.vertices[doorVertex].doorOpen = doorOpen;
                        if (screen.vertices[room1Count].vacuuming == true || screen.vertices[room2Count].vacuuming == true) {
                            screen.vertices[room1Count].vacuuming = true;
                            screen.vertices[room2Count].vacuuming = true;
                        }
                    }
                    screen.checkRoomVenting();
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

    public TextureRegion getFrame(float dt) {
        TextureRegion region;

        if(currentState == State.OPENING) {
            region = doorOpening.getKeyFrame(stateTimer);
        } else {
            region = doorClosing.getKeyFrame(stateTimer);
        }
        if(stateTimer > frameDuration*frameCount ){
            stateTimer = 0;
            currentState = State.NEUTRAL;
        }

        return region;
    }

    public void update(float dt) {
        // setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        if(currentState == State.NEUTRAL){
        } else {
            setDrawable(new TextureRegionDrawable(getFrame(dt)));
        }
    }

    public void defineSprite(List<Float> tempSize) {
        /*BodyDef bdef = new BodyDef();
        bdef.position.set(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3) / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        *//*fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;*//*

        fdef.shape = shape;
        b2body.createFixture(fdef);*/
/*
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5 / MyGdxGame.PPM, 5 / MyGdxGame.PPM), new Vector2(5 / MyGdxGame.PPM, -5 / MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor*/

        //b2body.createFixture(fdef).setUserData("front");
    }
}
