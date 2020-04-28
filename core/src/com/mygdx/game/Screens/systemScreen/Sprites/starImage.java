package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.Texture;
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

        super(screen.getStAnim().findRegion(""+1));

        stateTimer = 0;
        currentState = State.SHINING;
        previousState = State.SHINING;
        frameCount = 13;
        frameDuration = 0.15f;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //TextureRegion[] frames = new TextureRegion[frameCount-1];
        TextureRegion frame1 = new TextureRegion();
        TextureRegion frame2 = new TextureRegion();
        TextureRegion frame3 = new TextureRegion();
        TextureRegion frame4 = new TextureRegion();

        frame1 = new TextureRegion(screen.getStAnim().findRegion(""+1));
        frame2 = new TextureRegion(screen.getStAnim().findRegion(""+2));
        frame3 = new TextureRegion(screen.getStAnim().findRegion(""+3));
        frame4 = new TextureRegion(screen.getStAnim().findRegion(""+4));

        for(int i = 1; i < frameCount; i++){
            //System.out.println("star image i "+i);
            frames.add(new TextureRegion(screen.getStAnim().findRegion(""+i)));
            //frames[i-1] = new TextureRegion(screen.getStAnim().findRegion("s"+i));
        }

        starShine = new Animation(frameDuration,frames);

        // get texture from atlas by coordinates
        this.world = world;

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
        //System.out.println("update star animation dt "+dt);
        setDrawable(new TextureRegionDrawable(new TextureRegion(getFrame(dt))));
        //System.out.println("updated star animation ");
    }

}
