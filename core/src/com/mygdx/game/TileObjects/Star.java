package com.mygdx.game.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.TileObjects.InteractiveTileObject;

public class Star extends InteractiveTileObject {
    public Star(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds,"star");
        fixture.setUserData("star");
        setCategoryFilter (MyGdxGame.STAR_BIT);
    }

    @Override
    public void frontHit() {
        Gdx.app.log("Star","Collision");
    }

}
