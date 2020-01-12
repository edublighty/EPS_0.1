package com.mygdx.game.Tools;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.perlinDrawTest;
import com.mygdx.game.Screens.systemScreen.systemPlayGRAVY;
import com.mygdx.game.TileObjects.Planet;
import com.mygdx.game.Screens.orbitPlay;
import com.mygdx.game.Screens.systemScreen.systemPlay;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.practiceDynTiles;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;
import com.mygdx.game.TileObjects.Station;
import com.mygdx.game.Sprites.atmosWarning;
import com.mygdx.game.TileObjects.atmosphere;
import com.mygdx.game.TileObjects.grndBldgs;

import java.util.EnumMap;

public class b2dWorldCreator {
    public b2dWorldCreator(World world, TiledMap map, Screen screen, double[][] planetData,int[][] mapLayout, int TLi, int TLj, int sWidth, int sHeight){

        // create body and fixture variables
        BodyDef bdef = new BodyDef();               // What the body consists of
        PolygonShape shape = new PolygonShape();    // Shape for fixture
        FixtureDef fdef = new FixtureDef();         // Fixture for body
        Body body;                                  // Object body for all objects

        if(screen instanceof systemPlay || screen instanceof systemPlayGRAVY) {
            System.out.println("instance of systemPlay");
            // create sun fixture
            /*for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                //bdef.type = BodyDef.BodyType.StaticBody;
                new Star(world,map,rect);
            }*/

            int nP = planetData.length;     // does this start from zero
            int nP2 = planetData[0].length;
            int i = 0;
            int size = 0;
            int xPos;
            int yPos;

            double toteSize = (750*(nP+1)+500)/2;

            System.out.println("nP "+nP);
            System.out.println("nP2 "+nP2);

            // do the same for star

            while (i < nP2) {


                // create planet fixtures per orbit

                // planet sizes:
                // terrestrial - 200 px square
                // gaseous terrestrial - 400 px square
                // gas giant - 600 px square

                /*// planet type
                int pType = (int) planetData[i][0];

                switch (pType) {
                    case 0:
                        // terrestrial
                        size = 100;
                    case 1:
                        // gaseous terrestrial
                        size = 200;
                    case 2:
                        // gas giant
                        size = 300;
                }*/

                size = (int) planetData[1][i];
                xPos = (int) planetData[2][i];
                yPos = (int) planetData[3][i];

                int xOrbPos=(int) Math.round(toteSize-planetData[9][i]);
                int yOrbPos=(int) Math.round(toteSize-planetData[9][i]);
                int orbSize = (int) planetData[9][i];

                System.out.println("Rectangle no. "+i+" x "+xPos+" y "+yPos);
                Rectangle square = new Rectangle();
                square.set(xPos, yPos, size, size);
                Circle circle = new Circle();
                circle.set(xPos, yPos, size);
                new Planet(world, map, square);


            /*for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                new Planet(world,map,rect);
            }*/

                // procedural object creation
                //Rectangle square = new Rectangle();
                //square.set(500, 500, 500, 500);
                i++;
            }


        } else if(screen instanceof orbitPlay){
            System.out.println("instance of orbitPlay");
            // create station fixture
            for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                //bdef.type = BodyDef.BodyType.StaticBody;
                new Station(world,map,rect);
            }
            // create approach warning fixture
            for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                new atmosWarning(world,map,rect);
            }
            // create planet fixture
            for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                new atmosphere(world,map,rect);
            }
        } else if(screen instanceof galaxyScreen) {
            // not used

        } else if(screen instanceof practiceDynTiles) {
            // generate objects for maps

            int i;
            int j;
            int size = 0;
            int xPos = 0;
            int yPos = 0;
            bdef.type = BodyDef.BodyType.StaticBody;
/*

            Rectangle square = new Rectangle();
            square.set(3 * 50, 3 * 50, 150, 150);
            bdef.position.set(square.getX() + square.getWidth() / 2, square.getY() + square.getHeight() / 2);

            body = world.createBody(bdef);

            shape.setAsBox(square.getWidth() / 2, square.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
            new grndBldgs(world, map, square);
*/
            System.out.println("b2dWC TLi " + TLi + " and TLj " + TLj);

            int iX1 = 0 - ((practiceDynTiles) screen).constrX2;
            int iX2 = ((practiceDynTiles) screen).wWidth + ((practiceDynTiles) screen).constrX2;//mapLayout.length;
            int iY1 = 0 - ((practiceDynTiles) screen).constrY2;
            int iY2 = ((practiceDynTiles) screen).wHeight + ((practiceDynTiles) screen).constrY2;//mapLayout[0].length;

            //iX1 *= 3;
            //iX2 *= 3;
            //iY1 *= 3;
            //iY2 *= 3;

            System.out.println("TLi "+TLi+" and TLj "+TLj);

            for (i = iX1 + 1; i < iX2; i++) {
                for (j = iY1; j < iY2; j++) {
                    if (mapLayout[i + TLi][j + TLj] == 11 || mapLayout[i + TLi][j + TLj] == 12 || mapLayout[i + TLi][j + TLj] == 13 || mapLayout[i + TLi][j + TLj] == 14) {
                        System.out.println("i " + i + " j " + j + " and i+TLi " + i+TLi + " j+TLj " + j+TLj);
                        //System.out.println("Rectangle no. " + i + " x " + xPos + " y " + yPos);
                        Rectangle square = new Rectangle();
                        square.set((((i + TLi) * 50 / MyGdxGame.PPM)), ((j + TLj) * 50 / MyGdxGame.PPM), 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
                        bdef.position.set((square.getX() + square.getWidth() / 2), (square.getY() + square.getHeight() / 2));
                        //bdef.position.set((square.getX() + square.getWidth() / 2) / MyGdxGame.PPM, (square.getY() + square.getHeight() / 2) / MyGdxGame.PPM);
                        body = world.createBody(bdef);
                        shape.setAsBox((square.getWidth() / 2), (square.getHeight() / 2));
                        //shape.setAsBox((square.getWidth() / 2) / MyGdxGame.PPM, (square.getHeight() / 2) / MyGdxGame.PPM);
                        fdef.shape = shape;
                        body.createFixture(fdef);
                        //new grndBldgs(world, map, square);
                    }
                }
            }


        } else if(screen instanceof perlinDrawTest){
            int i;
            int j;
            int size = 0;
            int xPos = 0;
            int yPos = 0;
            bdef.type = BodyDef.BodyType.StaticBody;
/*

            Rectangle square = new Rectangle();
            square.set(3 * 50, 3 * 50, 150, 150);
            bdef.position.set(square.getX() + square.getWidth() / 2, square.getY() + square.getHeight() / 2);

            body = world.createBody(bdef);

            shape.setAsBox(square.getWidth() / 2, square.getHeight() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);
            new grndBldgs(world, map, square);
*/

            float array[][] = ((perlinDrawTest) screen).terrainProfile;//perlinNoise;
            System.out.println("b2dWC TLi " + TLi + " and TLj " + TLj);

            for (i = 0; i < array.length; i++) {
                        //System.out.println("Rectangle no. " + i + " x " + xPos + " y " + yPos);
                        Rectangle square = new Rectangle();
                        //System.out.println("height "+array[i][1]*sHeight);
                        square.set(i, Math.round(array[i][1]), 5, 5);
                        bdef.position.set((square.getX() + square.getWidth() / 2), (square.getY() + square.getHeight() / 2));
                        //bdef.position.set((square.getX() + square.getWidth() / 2) / MyGdxGame.PPM, (square.getY() + square.getHeight() / 2) / MyGdxGame.PPM);
                        body = world.createBody(bdef);
                        shape.setAsBox((square.getWidth() / 2), (square.getHeight() / 2));
                        //shape.setAsBox((square.getWidth() / 2) / MyGdxGame.PPM, (square.getHeight() / 2) / MyGdxGame.PPM);
                        fdef.shape = shape;
                        body.createFixture(fdef);
                        //new grndBldgs(world, map, square);
                }

                System.out.println("Finished rectangling");

        } else if(screen instanceof battleShipScreen){
        int i=0;
        int j=0;
        int size = 0;
        int xPos = 0;
        int yPos = 0;
        bdef.type = BodyDef.BodyType.StaticBody;

        EnumMap<battleShipScreen.roomTypes,Integer[]> roomPosMaps = ((battleShipScreen) screen).roomPosMaps;
        battleShipScreen.roomTypes[][] shipLayout = ((battleShipScreen) screen).shipLayout;
        Integer[] tempSize = new Integer[4];
        Integer[] tempSize2 = new Integer[4];
        System.out.println("shiplayout x "+shipLayout.length+" and y "+shipLayout[0].length);

        for (i = 0; i < shipLayout.length; i++) {
            for (j = 0; j < shipLayout[0].length; j++) {
                System.out.println("i "+i+" j "+j);
                if (shipLayout[i][j] == null) {
                    System.out.println("empty room again");
                } else {
                    Rectangle square = new Rectangle();
                    battleShipScreen.roomTypes name = shipLayout[i][j];
                    System.out.println("room name"+name);
                    //tempSize = {battleShipScreen) screen.getRoomData(name);
                    //System.out.println("tempSize2 size "+tempSize2.length);
                    System.out.println("tempSize2 0 width " + tempSize2[0]);
                    System.out.println("tempSize2 1 height " + tempSize2[1]);
                    System.out.println("tempSize2 2 x " + tempSize2[2]);
                    System.out.println("tempSize2 3 y " + tempSize2[3]);
                    square.set(tempSize2[2]/MyGdxGame.PPM, tempSize2[3]/MyGdxGame.PPM, tempSize2[0]/MyGdxGame.PPM, tempSize2[1]/MyGdxGame.PPM);
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

        } else {
            System.out.println("somethign else");
            Rectangle square = new Rectangle();
            square.set(100, 100, 500, 500);
            new grndBldgs(world,map,square);
        }
    }

    public void updateSqs(Screen screen){
        System.out.println("into update squares");
        BodyDef bdef = new BodyDef();               // What the body consists of
        PolygonShape shape = new PolygonShape();    // Shape for fixture
        FixtureDef fdef = new FixtureDef();         // Fixture for body
        Body body;                                  // Object body for all objects

        int TLi = ((practiceDynTiles) screen).TLi;
        int TLj = ((practiceDynTiles) screen).TLj;
        int[][] mapLayout = ((practiceDynTiles) screen).mapLayout;
        World world = ((practiceDynTiles) screen).world;

        System.out.println("imported data");

        int i;
        int j;
        int size = 0;
        int xPos=0;
        int yPos=0;
        bdef.type = BodyDef.BodyType.StaticBody;

        int iX1 = 0 - ((practiceDynTiles) screen).constrX2;
        int iX2 = ((practiceDynTiles) screen).wWidth+2*(((practiceDynTiles) screen).constrX2);//mapLayout.length;
        int iY1 = 0 - ((practiceDynTiles) screen).constrY2;
        int iY2 = ((practiceDynTiles) screen).wHeight+2*(((practiceDynTiles) screen).constrY2);//mapLayout[0].length;

        //iX1 *= 3;
        //iX2 *= 3;
        //iY1 *= 3;
        //iY2 *= 3;

        System.out.println("initialised variables");

        for(i=iX1;i<iX2;i++){
            for(j=iY1;j<iY2;j++){
                if (mapLayout[i + TLi][j + TLj] == 11 || mapLayout[i + TLi][j + TLj] == 12 || mapLayout[i + TLi][j + TLj] == 13 || mapLayout[i + TLi][j + TLj] == 14) {
                    System.out.println("i " + i + " j " + j + " and i+TLi " + i+TLi + " j+TLj " + j+TLj);
                    Rectangle square = new Rectangle();
                    square.set((((i+TLi)*50/MyGdxGame.PPM)),((j+TLj)*50/MyGdxGame.PPM),50/MyGdxGame.PPM, 50/MyGdxGame.PPM);
                    bdef.position.set((square.getX() + square.getWidth() / 2), (square.getY() + square.getHeight() / 2));
                    //System.out.println("Set bdef position");
                    //bdef.position.set((square.getX() + square.getWidth() / 2) / MyGdxGame.PPM, (square.getY() + square.getHeight() / 2) / MyGdxGame.PPM);
                    body = world.createBody(bdef);
                    //System.out.println("Created body");
                    shape.setAsBox((square.getWidth() / 2), (square.getHeight() / 2));
                    //System.out.println("Set as box");
                    //shape.setAsBox((square.getWidth() / 2) / MyGdxGame.PPM, (square.getHeight() / 2) / MyGdxGame.PPM);
                    fdef.shape = shape;
                    body.createFixture(fdef);
                    //System.out.println("created fixture");
                    //new grndBldgs(world, map, square);
                }
            }
        }
        System.out.println("finished for loop squares");
    }

}
