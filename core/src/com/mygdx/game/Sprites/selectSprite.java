package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

public class selectSprite extends Sprite {

    public World world;
    private TextureRegion selectTR;

    public selectSprite(TextureRegion selectT, float X, float Y){

        super(selectT);
        // get texture from atlas by coordinates
        selectTR = new TextureRegion(selectT, 0, 0, 50, 50);
        this.world = world;
        setBounds(X / MyGdxGame.PPM, Y / MyGdxGame.PPM, 100 / MyGdxGame.PPM, 100 / MyGdxGame.PPM);
    }
}
