package com.mygdx.game.Screens.systemScreen.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class systemTiles extends Image {

    private systemScreen2 screen;

    public systemTiles(systemScreen2 screen, float width, float height, float Xcoord, float Ycoord, String texture){
        super(screen.getThrustAt().findRegion(texture));
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
