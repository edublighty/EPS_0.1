package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by GCUser on 13/05/2018.
 */

public class gameInitialiser extends ApplicationAdapter {

    GameMap gameMap;
    OrthographicCamera cam;

    public void create(){
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.update();

        gameMap = new TiledGameMap();

    }

    public void render(){
        gameMap.render(cam);
    }

    public void dispose(){

    }

}
