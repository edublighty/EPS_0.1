package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class starTiles extends Image {

    private galaxyScreen screen;

    public starTiles(galaxyScreen screen, float width, float height, float Xcoord, float Ycoord, String texture){
        super(screen.getStAnim().findRegion(Integer.toString(1)));
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
