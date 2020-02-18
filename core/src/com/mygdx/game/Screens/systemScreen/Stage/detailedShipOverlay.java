package com.mygdx.game.Screens.systemScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.Sprites.systemScreenShipGroup;
import com.mygdx.game.Screens.systemScreen.systemScreen2;


public class detailedShipOverlay implements Disposable {
    @Override
    public void dispose() {

    }

    public Stage stage;
    public Viewport viewport;

    public detailedShipOverlay(MyGdxGame game, World world, systemScreen2 screen, SpriteBatch sb, float viewportWidth, float viewportHeight,float toteSize) {

        viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        // ------------------------------------------------------------------------------------------------------------

        // create backdrop for ship menu

        Image backdrop = new Image();
        backdrop.setDrawable(new TextureRegionDrawable(game.getTilesAt().findRegion("pDTTile50Grey")));
        float margin = viewportHeight/10;
        float width = viewportWidth - margin;
        float height = viewportHeight - margin;
        float X = margin / 2;
        float Y = margin / 2;
        backdrop.setWidth(width);
        backdrop.setHeight(height);
        backdrop.setX(X);
        backdrop.setY(Y);
        stage.addActor(backdrop);
        //backdrop.setBounds(getX(),getY(),getWidth(),getHeight());
        //setTouchable(Touchable.enabled);

        // add image of ship

        //systemScreenShipGroup shipImage = screen.getShip();
        systemScreenShipGroup shipImage = new systemScreenShipGroup(game,world,screen,sb,toteSize);
        if(shipImage == null){
            System.out.println("ship image is null");
        }
        if(backdrop == null){
            System.out.println("backdrop is null");
        }
        //shipImage.setBounds(backdrop.getX(),backdrop.getY(),backdrop.getWidth(),backdrop.getHeight());
        System.out.println("shipImage width "+shipImage.getWidth()+" by "+shipImage.getHeight());
        System.out.println("backdrop width "+backdrop.getWidth()+" by "+backdrop.getHeight());
        float ratio = (backdrop.getWidth()*0.9f)/shipImage.getWidth();
        shipImage.setScale(ratio);
        shipImage.setOrigin(0,0);
        float thatHeight = backdrop.getHeight()/2 - (shipImage.getHeight()*ratio)/2;
        shipImage.setPosition(backdrop.getX(),thatHeight);
        System.out.println("shipImage width "+shipImage.getWidth()+" by "+shipImage.getHeight());
        //shipImage.setSize(backdrop.getWidth(),backdrop.getHeight());
        stage.addActor(shipImage);


    }

}
