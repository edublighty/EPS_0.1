package com.mygdx.game.Screens;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicalObject {
    Body body;
    Sprite sprite;
    float positionX;
    float positionY;
    float w;
    float y;
    float sizeFix;
    Boolean centered;
    Boolean awake;
    PolygonShape shape;
    FixtureDef fixtureDef;

    public PhysicalObject(World world, BodyDef.BodyType type, String image, float positionX, float positionY, float sizeFix, Boolean centered) {
        Texture texture = new Texture(image);
        sprite = new Sprite(texture);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        if (centered) {
            sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        }
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(positionX, positionY);
        body = world.createBody(bodyDef);
        shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth()/2/sizeFix, sprite.getHeight()/2/sizeFix);
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    void ChangeShapeSize(float w, float y, float sizeFix)
    {
        body.destroyFixture(body.getFixtureList().peek());
        shape = new PolygonShape();
        shape.setAsBox(w/sizeFix, y/sizeFix);
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    void updateObject(float sizeFix)
    {
        sprite.setPosition(body.getPosition().x / sizeFix - sprite.getOriginX(), body.getPosition().y / sizeFix - sprite.getOriginY());
        sprite.setRotation((float) Math.toDegrees(body.getTransform().getRotation()));
    }
}