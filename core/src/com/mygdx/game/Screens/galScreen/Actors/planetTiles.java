package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class planetTiles extends Image {

    private galaxyScreen screen;

    public planetTiles(galaxyScreen screen, float width, float height, float Xcoord, float Ycoord, String texture){
        super(screen.getPlAtlas().findRegion(texture));
        this.screen = screen;
        setWidth(width);
        setHeight(height);
        setX(Xcoord);
        setY(Ycoord);
        setOrigin(getWidth()/2,getHeight()/2);
        setBounds(getX(),getY(),getWidth(),getHeight());
        //setTouchable(Touchable.enabled);

    }

}
