package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by GCUser on 13/05/2018.
 */

public abstract class GameMap {

    public abstract void render(OrthographicCamera camera);
    public abstract void update(float delta);
    public abstract void dispose();

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getLayers();
}
