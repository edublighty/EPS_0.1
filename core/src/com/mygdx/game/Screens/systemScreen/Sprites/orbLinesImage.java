package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class orbLinesImage extends Image {

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

    public orbLinesImage(World world, systemScreen2 screen, float orbDia, float X, float Y){
        this.world = world;

        defineSprite(orbDia,X,Y);

        //setBounds((toteSize-2/2)/MyGdxGame.PPM,(toteSize-2/2)/MyGdxGame.PPM,orbDia/ MyGdxGame.PPM,orbDia/MyGdxGame.PPM);      // how big sprite is on screen
    }

    public void update(float dt){
    }

    public void defineSprite(float orbDia,float X,float Y){
        BodyDef bdef = new BodyDef();
        bdef.position.set((X/MyGdxGame.PPM),(Y/MyGdxGame.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(orbDia);//1 / MyGdxGame.PPM);
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
