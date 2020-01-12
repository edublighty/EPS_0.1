package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class starImage extends Image {

    public World world;
    public Body b2body;
    private Animation<TextureRegion> starShine;
    private TextureRegion stObj;
    private float stateTimer;
    public enum State { SHINING };
    public State currentState;
    public State previousState;
    private int frameCount;
    private float frameDuration;
    //public int loc;

    public starImage(World world, systemScreen2 screen, String sType, int nP, int wWid, int wHei, int size) {

        super(screen.getStAnim().findRegion(Integer.toString(1)));

        stateTimer = 0;
        currentState = State.SHINING;
        previousState = State.SHINING;
        frameCount = 10;
        frameDuration = 0.1f;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < frameCount; i++){
            System.out.println("sprite i "+i);
            frames.add(new TextureRegion(screen.getStAnim().findRegion(Integer.toString(i))));
        }

        starShine = new Animation(frameDuration,frames);

        //this.loc = loc;
        //size = (int) planetData[4][i];
        int locX = (size*(nP+1)+500) / 2 - size / 2;
        int locY = (size*(nP+1)+500) / 2 - size / 2;
/*
        switch(stelObj){
            case "terrs":
                // terrestrial
                size = 100;
            case "gasTerrs":
                // gaseous terrestrial
                size = 200;
            case "gasGis":
                // gas giant
                size = 300;
        }*/

        // get texture from atlas by coordinates
        stObj = new TextureRegion(screen.getStAnim().findRegion(Integer.toString(1)));
        //stObj = new TextureRegion(getTexture(),0,0,300,300);
        this.world = world;
        //defineSprite();       // not necessary for stationary object sprite
        setBounds((float) locX / MyGdxGame.PPM, (float) locY / MyGdxGame.PPM, (float) size / MyGdxGame.PPM, (float) size / MyGdxGame.PPM);
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
        //setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        //setRegion(getFrame(dt));
        setDrawable(new TextureRegionDrawable(getFrame(dt)));
    }

}
