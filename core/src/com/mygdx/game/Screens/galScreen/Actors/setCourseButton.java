package com.mygdx.game.Screens.galScreen.Actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.galScreen.Stage.galScreenActorsSystembyPlanets;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class setCourseButton extends Image {

    public setCourseButton(galaxyScreen screen, galScreenActorsSystembyPlanets galScreenPlanets, float width, float height, float X, float Y, String pointType){
        super(screen.getStarSelsAt().findRegion(pointType));
        setWidth(width);
        setHeight(height);
        setX(X);
        setY(Y);
        setBounds(getX(),getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);

        addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                //
            }
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Setting course son");
                screen.galActors.drawPath();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //
            }
        });
    }
}
