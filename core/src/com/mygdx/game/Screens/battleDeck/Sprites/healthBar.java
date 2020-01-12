package com.mygdx.game.Screens.battleDeck.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;

import java.util.List;

public class healthBar extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion room;

    public healthBar(World world, battleShipScreen screen, List<Float> tempSize, String barType) {
        super(screen.getBarsAt().findRegion(barType));
        this.world = world;
        //System.out.println("icon sprite "+tempSize.get(0)+" "+tempSize.get(1)+" "+tempSize.get(2)+" "+tempSize.get(3));
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
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(tempSize.get(0),tempSize.get(1));
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
