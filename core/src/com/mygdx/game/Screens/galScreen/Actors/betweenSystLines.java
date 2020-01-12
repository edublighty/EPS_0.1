package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.galScreen.Stage.galScreenHUD;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class betweenSystLines extends Image {

    public boolean selected;
    Skin skin;
    public double X;
    public double Y;
    public String systName;
    public boolean currentPosition;
    public int row;

    public betweenSystLines(galaxyScreen screen, float x1, float x2, float y1, float y2, float th, float offsetX, float offsetY,String fuelOK){
        super(screen.getStarSelsAt().findRegion(fuelOK));
        float dx = x2 - x1;
        float dy = y2 - y1;
        float theta = (float) (Math.atan(dy/dx)*180/Math.PI);
        if(dx>0){
            if(dy>0){
                // top right - so nothing further required
                theta -= 90;
            } else {
                // bottom right - take from 360
                theta += 270;
            }
        } else {
            if(dy>0){
                // top left
                theta += 90;
            } else {
                // bottom left
                theta += 180;
            }
        }
        theta+=90;
        float rad = (float) Math.sqrt( Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) );
        setWidth(rad);
        setHeight(th);
        setRotation(theta);
        //this.rotateBy(10);
        setX(x1+offsetX);
        setY(y1+offsetY);
        //setBounds(getX(),getY(),getWidth(),getHeight());
    }
}
