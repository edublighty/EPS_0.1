package com.mygdx.game.Screens.systemScreen.Sprites;

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

public class planetImage extends Image {

    public World world;
    public Body b2body;
    private TextureRegion stObj;
    public double theta;
    public double posX;
    public double posY;
    public double dt;
    //public int loc;

    public planetImage(World world, systemScreen2 screen, String stelObj, double planetData[][], int i, int wWid, int wHei) {

        super(screen.getPlAtlas().findRegion(stelObj));

        // get texture from atlas by coordinates
        //stObj = new TextureRegion(getTexture(),0,0,300,300);
        //stObj = new TextureRegion(getTexture(),0,0,300,300);
        theta = 0;
        dt = 0;
        this.world = world;
        setBounds((float) (planetData[2][i] - planetData[1][i]/2) / MyGdxGame.PPM, (float) (planetData[3][i] - planetData[1][i]/2) / MyGdxGame.PPM, (float) planetData[1][i] / MyGdxGame.PPM, (float) planetData[1][i] / MyGdxGame.PPM);
    }

    public void update(float dt) {
        //setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

}