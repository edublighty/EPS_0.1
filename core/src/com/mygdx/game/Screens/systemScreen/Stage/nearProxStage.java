package com.mygdx.game.Screens.systemScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class nearProxStage implements Disposable {

    public Stage stage;
    public Stage backstage;
    public Stage forestage;
    public OrthographicCamera wStageCam;
    public OrthographicCamera wStageCam2;
    public OrthographicCamera wStageCam3;
    private MyGdxGame game;
    public Viewport viewport;
    public Viewport viewport2;
    public Viewport viewport3;

    public nearProxStage(MyGdxGame game, systemScreen2 screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        wStageCam = new OrthographicCamera();
        viewport = new FillViewport(viewportWidth,viewportHeight, wStageCam);
        stage = new Stage(viewport, sb);    // stage for all the collision objects

        wStageCam2 = new OrthographicCamera();
        viewport2 = new FillViewport(viewportWidth,viewportHeight, wStageCam2);
        backstage = new Stage(viewport2,sb);    // background objects

        wStageCam3 = new OrthographicCamera();
        viewport3 = new FillViewport(viewportWidth,viewportHeight, wStageCam3);
        forestage = new Stage(viewport2,sb);    // foreground objects that do not move e.g. players ship (we just rotate the midground camera oowhee)

        Gdx.input.setInputProcessor(stage);
        this.game=game;

        //stage.setDebugAll(true);
    }

    /*public void addActor(Actor actor){
        stage.addActor(actor);
    }*/

    @Override
    public void dispose() {

    }

    public void update(double dt){
        double A = 1; //1px/s

    }
}
