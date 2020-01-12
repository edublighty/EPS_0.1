package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by GCUser on 13/05/2018.
 */

public class TiledGameMap extends GameMap {

    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    public TiledGameMap(){
        System.out.println("before");
        tiledMap = new TmxMapLoader().load("smaller.tmx");
        System.out.println("after");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void render(OrthographicCamera camera){
        tiledMapRenderer.setView(camera);
    }

    @Override
    public void update(float delta){

    }

    @Override
    public void dispose(){
        tiledMap.dispose();
    }

    @Override
    public int getWidth(){
        return 0;
    }

    @Override
    public int getHeight(){
        return 0;
    }

    @Override
    public int getLayers(){
        return 0;
    }


}
