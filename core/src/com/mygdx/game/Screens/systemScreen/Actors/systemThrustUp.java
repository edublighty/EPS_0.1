package com.mygdx.game.Screens.systemScreen.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class systemThrustUp extends Image {

    public float currentY;
    private float minY;
    private float maxY;
    public float percentPow;
    private systemScreen2 screen;
    private float grad;
    private float c;

    public systemThrustUp(systemScreen2 screen, float width, float height, float Xcoord, float Ycoord){
        super(screen.getThrustAt().findRegion("engine3"));
        this.screen = screen;
        setWidth(width);
        setHeight(height);
        setX(Xcoord);
        setY(Ycoord);
        setOrigin(getWidth()/2,getHeight()/2);
        currentY = Ycoord;
        percentPow = 0.5f;
        minY = -height/2;
        maxY = height - height/2;
        setBounds(getX(),getY(),getWidth(),getHeight());
        setTouchable(Touchable.enabled);
        grad = 1 / (maxY - minY);
        c = grad*(maxY-minY)/2;

        addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                //
                //System.out.println("TOUCHING MY HANDLE SON");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //screen.galActors.updateMarkers(Xcoord,Ycoord,width,height,galHUD,systName);
                //System.out.println("TOUCHING MY HANDLE SON");
                screen.handling = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //
                //System.out.println("TOUCHING MY HANDLE SON");
                screen.handling = false;
            }
        });
    }

    public boolean touchDragged2(){//, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        System.out.println("touch dragged "+x+" "+y);
        /*if(x<1&&y<1){
            // point click - no drag
            posDX = Gdx.input.getX();                           // destination on screen X
            posDY = Gdx.input.getY();                           // destination on screen Y
            //System.out.println("DX "+posDX+" and DY "+posDY);
            setPointers();

        }*/

        setX(getX()+x);
        setY(getY()+y);

        return true;
    }

    /*public void updateMarker(){
        try {
            //System.out.println("delta y is "+screen.deltas.y);
            //System.out.println("mos y "+screen.mousePos.y+" getY "+getY());
        if ((screen.deltas.y - (getHeight() / 2)) < maxY && (screen.deltas.y - (getHeight() / 2)) > minY){
            currentY = screen.deltas.y - getHeight() / 2;
            setY(screen.deltas.y - getHeight() / 2);
            percentPow = grad*getY()+c;
            screen.burnThrust = screen.maxBurnThrust*percentPow;
            //System.out.println("percent power is "+percentPow);
        }
        } catch(Exception e) {

        }

    }*/
}
