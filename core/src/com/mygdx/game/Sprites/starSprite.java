package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemPlay;

public class starSprite extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion stObj;
    //public int loc;

    public starSprite(World world, systemPlay screen, String sType, int nP, int wWid, int wHei) {

        super(screen.getStAtlas().findRegion(sType));

        //this.loc = loc;
        int size;
        //size = (int) planetData[4][i];
        int locX = (750*(nP+1)+500) / 2 - 750 / 2;
        int locY = (750*(nP+1)+500) / 2 - 750 / 2;
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
        stObj = new TextureRegion(getTexture(),0,0,300,300);
        //stObj = new TextureRegion(getTexture(),0,0,300,300);
        this.world = world;
        //defineSprite();       // not necessary for stationary object sprite
        setBounds((float) locX / MyGdxGame.PPM, (float) locY / MyGdxGame.PPM, (float) 750 / MyGdxGame.PPM, (float) 750 / MyGdxGame.PPM);
    }

    public void update(float dt) {
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void defineSprite() {
        // for collisions if doesnt have object such as rectangle
        /*BodyDef bdef = new BodyDef();
        bdef.position.set(500 , 500 );
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);*/
/*
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;*/

        /*fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5 / MyGdxGame.PPM, 5 / MyGdxGame.PPM), new Vector2(5 / MyGdxGame.PPM, -5 / MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor*/

        //b2body.createFixture(fdef).setUserData("front");
    }
}
