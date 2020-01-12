package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.practiceDynTiles;

public class grndPlayerSprite extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING };
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    public float genPosX;
    public float genPosY;
    private TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;
    private float stateTimer;
    private boolean runningRight;

    public grndPlayerSprite(World world, practiceDynTiles screen, float genPosX, float genPosY){
        super(screen.getMarAt().findRegion("marioLittle2"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;


        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));
        }

        marioRun = new Animation(0.1f,frames);

        for(int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(),i*16,0,16,16));
        }
        marioJump = new Animation(0.1f,frames);



        this.genPosX = genPosX;
        this.genPosY = genPosY;
        defineSprite();
        marioStand = new TextureRegion((getTexture()),0,0,16,16);   // Mario looks right
        setBounds(0,0,100/ MyGdxGame.PPM,100/MyGdxGame.PPM);      // how big sprite is on screen
        setRegion(marioStand);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = marioStand;
                    break;
        }

        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight=false;
        } else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }

        // This needs explaining
        // Does currentState equal previousState? If so then increment dt or else set 0
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y>0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            // jumping
            return State.JUMPING;
        } else if(b2body.getLinearVelocity().y<0){
            // falling
            return State.FALLING;
        } else if(b2body.getLinearVelocity().x !=0){
            // running
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void defineSprite(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(genPosX, genPosY);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50 / MyGdxGame.PPM);//1 / MyGdxGame.PPM);
        //fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        //fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);
     /*   EdgeShape front = new EdgeShape();
        front.set(new Vector2(5/MyGdxGame.PPM,5/MyGdxGame.PPM),new Vector2(5/MyGdxGame.PPM,-5/MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor*/

        //b2body.createFixture(fdef).setUserData("front");
    }

}
