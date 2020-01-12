package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class galTiles extends Image {

    private galaxyScreen screen;

    public galTiles(galaxyScreen screen, float width, float height, float Xcoord, float Ycoord, String texture){
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
