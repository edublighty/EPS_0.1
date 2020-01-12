package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class warpDistance extends Image {

    public boolean selected;
    Skin skin;
    public double X;
    public double Y;
    public String systName;
    public boolean currentPosition;
    public int row;

    public warpDistance(galaxyScreen screen, float x1, float y1, float warpDist){
        super(screen.getStarSelsAt().findRegion("warpDist"));
        float x = x1 - warpDist;
        float y = y1 - warpDist;
        setWidth(warpDist*2);
        setHeight(warpDist*2);
        setX(x);
        setY(y);
        //setBounds(getX(),getY(),getWidth(),getHeight());
    }
}
