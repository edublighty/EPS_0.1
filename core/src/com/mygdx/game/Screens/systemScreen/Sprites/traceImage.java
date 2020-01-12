package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class traceImage extends Image {

    public World world;
    public Body b2body;
    private TextureRegion shipUs;

    public traceImage(MyGdxGame game, systemScreen2 screen, float x, float y, float dx, float dy){
        super(game.getTilesAt().findRegion("pDTTile50Orange"));
        setBounds(x,y,dx,dy);
    }
}
