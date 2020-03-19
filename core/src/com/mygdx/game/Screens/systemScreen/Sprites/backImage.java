package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class backImage extends Image {

    public backImage(systemScreen2 screen, float X, float Y, float width, float height) {

        super(screen.getStAnim().findRegion("galaxy2"));

        this.setPosition(X,Y);
        this.setSize(width,height);

    }

}
