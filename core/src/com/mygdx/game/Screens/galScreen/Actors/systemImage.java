package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Screens.galScreen.Stage.galScreenActorsSystembyPlanets;
import com.mygdx.game.Screens.galScreen.Stage.galScreenHUD;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Tools.Vertex;

public class systemImage extends Image {

    public boolean selected;
    Skin skin;
    private float X;
    private float Y;
    public String systName;
    public boolean currentPosition;
    public final int row;

    public systemImage(galaxyScreen screen, float width, float Xcoord, float Ycoord, galScreenHUD galHUD, galScreenActorsSystembyPlanets galScreenPlanets, String pointType, int rowN, String systName){
        super(screen.getStarSelsAt().findRegion(pointType));
        this.systName = systName;
        currentPosition = false;
        X = Xcoord;
        Y = Ycoord;
        this.row = rowN;
        setWidth(width);
        float tempA = screen.getStarSelsAt().findRegion(pointType).originalHeight;
        float tempB = screen.getStarSelsAt().findRegion(pointType).originalWidth;
        float aspectStar = tempA / tempB;
        float height = width*aspectStar;
        setHeight(height);
        setX(Xcoord);
        setY(Ycoord);
        setBounds(getX(),getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);
        selected = false;
        skin = new Skin(screen.getStarsAt2());

        if(pointType=="starmebbeSel"){

        } else {
            addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    //
                }

                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("touching a system " + pointType);
                    System.out.println("this is system row number "+row);
                    System.out.println("this system is at "+getX()+" by "+getY());
                    screen.galActors.updateMarkers(X, Y, width, height, galHUD, galScreenPlanets, systName, row);
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    //
                }
            });
        }
    }

    public float getTheX(){
        return X;
    }

    public float getTheY(){
        return Y;
    }
}
