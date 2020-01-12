package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.orbitPlay;

public class orbShipSprite extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion shipUs;

    public orbShipSprite(World world, orbitPlay screen){
        super(screen.getAtlas().findRegion("shipSpr"));
        this.world = world;
        defineSprite();
        // sprite size
        shipUs = new TextureRegion((getTexture()),0,0,256,256);
        // sprite boundaries
        setBounds(0,0,256/ MyGdxGame.PPM,256/MyGdxGame.PPM);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
    }

    public void defineSprite(){
        BodyDef bdef = new BodyDef();
        // initial location of sprite
        bdef.position.set(500 / MyGdxGame.PPM,4500 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // collision detector
        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5/MyGdxGame.PPM,5/MyGdxGame.PPM),new Vector2(5/MyGdxGame.PPM,-5/MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor

        b2body.createFixture(fdef).setUserData("front");
    }
}
