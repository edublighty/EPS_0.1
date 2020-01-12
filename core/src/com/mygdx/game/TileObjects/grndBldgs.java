package com.mygdx.game.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.TileObjects.InteractiveTileObject;

public class grndBldgs extends InteractiveTileObject {
    public grndBldgs(World world, TiledMap map, Rectangle bounds){
        super(world,map,bounds,"bldg");
        fixture.setUserData("bldg");
        setCategoryFilter(MyGdxGame.sDest_BIT);
    }

    @Override
    public void frontHit() {
        Gdx.app.log("bldg","Collision");
    }
}
