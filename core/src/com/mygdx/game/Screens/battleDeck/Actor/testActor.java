package com.mygdx.game.Screens.battleDeck.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;

public class testActor extends Actor {
    Texture texture;// = new Texture(Gdx.files.internal("data/jet.png"));
    float actorX = 0, actorY = 0;
    public boolean started = false;

    public testActor(battleShipScreen syst){
        TextureAtlas actorTextureAt = new TextureAtlas("batScreen/objects/shipObjects.atlas");
        TextureRegion actorTexture = actorTextureAt.findRegion("doorH");
        texture = actorTexture.getTexture();
        System.out.println("height testActor "+texture.getHeight()+" and width "+texture.getWidth());
        setWidth(texture.getWidth());
        setHeight(texture.getHeight());
        setBounds(actorX,actorY,getWidth()*100,getHeight()*100);
        addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                syst.testBool = true;
                ((testActor)event.getTarget()).started = true;
                System.out.println("doing it");
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture,getX(),getY());
    }

    @Override
    public void act(float delta){
        if(started){
            actorX+=5;
        }
    }

}
