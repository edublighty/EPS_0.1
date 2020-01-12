package com.mygdx.game.Screens.systemScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class worldStage implements Disposable {

    public Table table;
    public Stage stage;
    public Stage traceStage;
    public OrthographicCamera wStageCam;
    private MyGdxGame game;
    public Viewport viewport;

    public worldStage(MyGdxGame game, systemScreen2 screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        wStageCam = new OrthographicCamera();
        viewport = new FillViewport(viewportWidth,viewportHeight, wStageCam);
        stage = new Stage(viewport, sb);
        traceStage = new Stage(viewport, sb);
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
}
