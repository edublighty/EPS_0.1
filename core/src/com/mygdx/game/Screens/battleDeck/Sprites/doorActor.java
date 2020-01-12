package com.mygdx.game.Screens.battleDeck.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;

import java.util.List;

public class doorActor extends Actor {

    public World world;
    public Body b2body;
    private TextureRegion room;
    Sprite sprite = new Sprite(new Texture(Gdx.files.internal("badlogic.jpg")));


    public doorActor(){
        setBounds(sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight());
        setTouchable(Touchable.enabled);

        addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.RIGHT){
                    MoveByAction mba = new MoveByAction();
                    mba.setAmount(100f,0f);
                    mba.setDuration(5f);

                    doorActor.this.addAction(mba);
                }
                return true;
            }
        });
    }

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
    }

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
