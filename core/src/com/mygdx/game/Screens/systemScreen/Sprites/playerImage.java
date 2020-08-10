package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class playerImage extends Image {


    public World world;
    public Body b2body;

    public playerImage(MyGdxGame game, World world, systemScreen2 screen, float toteSize,String level,float startX,float startY){
        super(screen.getShipIconsAt().findRegion(level));
        this.world = world;
        defineSprite(toteSize,startX,startY);
        setBounds(0,0,toteSize/(MyGdxGame.PPM*100),toteSize/(MyGdxGame.PPM*100));
        setOrigin(getWidth()/2,getHeight()/2);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
    }

    public void defineSprite(float toteSize,float startX,float startY){
        BodyDef bdef = new BodyDef();
        bdef.position.set(startX,startY);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5/MyGdxGame.PPM,5/MyGdxGame.PPM),new Vector2(5/MyGdxGame.PPM,-5/MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor

        b2body.createFixture(fdef).setUserData("front");
    }
}
