package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.galScreen.Stage.galScreenActorsSystembyPlanets;
import com.mygdx.game.Screens.galScreen.Stage.galScreenHUD;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class seePlanetsButton extends Image {

    private boolean plansShowing;
    private float hideDistance;

    public seePlanetsButton(galaxyScreen screen, galScreenActorsSystembyPlanets galScreenPlanets, float width, float X, float Y, String pointType,boolean planetsShowing, float hideDist){
        super(screen.getStarSelsAt().findRegion(pointType));
        setWidth(width);
        setHeight(width);
        setX(X);
        setY(Y);
        this.hideDistance=hideDist;
        setBounds(getX(),getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);
        this.plansShowing=planetsShowing;

        addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                //
            }
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("moving the planets stage from galScreenHud");
                if(plansShowing) {
                    // planets showing so hide em sonnn
                    galScreenPlanets.camera.position.y += hideDistance;
                    galScreenPlanets.camera2.position.y += hideDistance;
                    plansShowing=false;
                    screen.showingPlanets=false;
                } else {
                    // planets hidden so move camera up
                    galScreenPlanets.camera.position.y -= hideDistance;
                    galScreenPlanets.camera2.position.y -= hideDistance;
                    plansShowing=true;
                    screen.showingPlanets=true;
                }
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //
            }
        });
    }
    public void updateMarker(){

    }
}
