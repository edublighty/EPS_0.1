package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class shipSprite extends Image {

    public World world;
    public Body b2body;

    public shipSprite(World world, systemScreen2 screen, String sType, int nP, int wWid, int wHei, int size) {

        super(screen.getShipIconsAt().findRegion("FREIGHTER"+1));
        // get texture from atlas by coordinates
        this.world = world;

    }

    public void update(float dt) {
        //setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        //setRegion(getFrame(dt));
        //System.out.println("update star animation dt "+dt);
        //setDrawable(new TextureRegionDrawable(new TextureRegion(getFrame(dt))));
        //System.out.println("updated star animation ");
    }

}
