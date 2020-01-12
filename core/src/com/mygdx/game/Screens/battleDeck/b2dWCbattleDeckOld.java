package com.mygdx.game.Screens.battleDeck;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public class b2dWCbattleDeckOld {
    public b2dWCbattleDeckOld(World world, TiledMap map, battleShipScreen screen, boolean healthBar, List<Float> tempSize) {

        // create body and fixture variables
        BodyDef bdef = new BodyDef();               // What the body consists of
        PolygonShape shape = new PolygonShape();    // Shape for fixture
        FixtureDef fdef = new FixtureDef();         // Fixture for body
        Body body;                                  // Object body for all objects

        if(healthBar){

            Rectangle square = new Rectangle();
            square.set(tempSize.get(2) / MyGdxGame.PPM, tempSize.get(3) / MyGdxGame.PPM, tempSize.get(0) / MyGdxGame.PPM, tempSize.get(1) / MyGdxGame.PPM);
            bdef.position.set((square.getX() + square.getWidth() / 2), (square.getY() + square.getHeight() / 2));
            body = world.createBody(bdef);
            shape.setAsBox((square.getWidth() / 2), (square.getHeight() / 2));
            fdef.shape = shape;
            body.createFixture(fdef);

        } else {

            int i = 0;
            int j = 0;
            int size = 0;
            int xPos = 0;
            int yPos = 0;
            bdef.type = BodyDef.BodyType.StaticBody;

            EnumMap<battleShipScreen.roomTypes, Integer[]> roomPosMaps = ((battleShipScreen) screen).roomPosMaps;
            battleShipScreen.roomTypes[][] shipLayout = ((battleShipScreen) screen).shipLayout;
            HashMap<String, List<Float>> roomPositions = ((battleShipScreen) screen).roomPositions;
            //Integer[] tempSize = new Integer[4];
            Integer[] tempSize2 = new Integer[4];
            List<Float> tempSize3 = new ArrayList<Float>();

            for (i = 0; i < shipLayout.length; i++) {
                for (j = 0; j < shipLayout[0].length; j++) {
                    System.out.println("i " + i + " j " + j);
                    if (shipLayout[i][j] == null) {
                        //System.out.println("empty room again");
                    } else {
                        Rectangle square = new Rectangle();
                        battleShipScreen.roomTypes name = shipLayout[i][j];
                        //System.out.println("room name" + name);
                        tempSize3 = roomPositions.get(name.name());
                        //tempSize2 = screen.getRoomData(i,j);
                        System.out.println("tempSize3 size " + tempSize3);
                        square.set(tempSize3.get(2) / MyGdxGame.PPM, tempSize3.get(3) / MyGdxGame.PPM, tempSize3.get(0) / MyGdxGame.PPM, tempSize3.get(1) / MyGdxGame.PPM);
                        bdef.position.set((square.getX() + square.getWidth() / 2), (square.getY() + square.getHeight() / 2));
                        //bdef.position.set((square.getX() + square.getWidth() / 2) / MyGdxGame.PPM, (square.getY() + square.getHeight() / 2) / MyGdxGame.PPM);
                        body = world.createBody(bdef);
                        shape.setAsBox((square.getWidth() / 2), (square.getHeight() / 2));
                        //shape.setAsBox((square.getWidth() / 2) / MyGdxGame.PPM, (square.getHeight() / 2) / MyGdxGame.PPM);
                        fdef.shape = shape;
                        body.createFixture(fdef);
                        tempSize2[0] = 0;
                        tempSize2[1] = 0;
                        tempSize2[2] = 0;
                        tempSize2[3] = 0;
                        //new grndBldgs(world, map, square);
                    }
                }
            }

            System.out.println("Finished rectangling");
        }
    }
}
