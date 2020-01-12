package com.mygdx.game.Screens.systemScreen.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class systemMapSystem extends Image {

    private systemScreen2 screen;

    public systemMapSystem(systemScreen2 screen, float width, float height, float Xcoord, float Ycoord){
        super(screen.getThrustAt().findRegion("SYSTEMBUTTON"));
        this.screen = screen;
        setWidth(width);
        setHeight(height);
        setX(Xcoord);
        setY(Ycoord);
        setOrigin(getWidth()/2,getHeight()/2);
        setBounds(getX(),getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);

        addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                //
                //System.out.println("TOUCHING MY HANDLE SON");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //screen.galActors.updateMarkers(Xcoord,Ycoord,width,height,galHUD,systName);
                //System.out.println("TOUCHING MY HANDLE SON");
                //screen.handling = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //
                //System.out.println("TOUCHING MY HANDLE SON");
                //screen.handling = false;
            }
        });
    }
}
