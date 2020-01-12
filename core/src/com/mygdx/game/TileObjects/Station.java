package com.mygdx.game.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.TileObjects.InteractiveTileObject;

public class Station extends InteractiveTileObject {
    public Station(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds,"station");
        fixture.setUserData("station");
        setCategoryFilter (MyGdxGame.STAR_BIT);
    }

    @Override
    public void frontHit() {
        Gdx.app.log("Station","Collision");
    }

}