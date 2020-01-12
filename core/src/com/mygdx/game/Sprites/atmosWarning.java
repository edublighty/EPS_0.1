package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.TileObjects.InteractiveTileObject;

public class atmosWarning extends InteractiveTileObject {
    public atmosWarning(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds,"warning");
        fixture.setUserData("warning");
        setCategoryFilter (MyGdxGame.STAR_BIT);
    }

    @Override
    public void frontHit() {
        Gdx.app.log("Warning","Collision");
    }

}