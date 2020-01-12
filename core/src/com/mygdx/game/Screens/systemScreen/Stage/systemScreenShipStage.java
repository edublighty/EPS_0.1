package com.mygdx.game.Screens.systemScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.HUDs.battleScreenbuttonOverlay;
import com.mygdx.game.Screens.battleDeck.Sprites.doorSprite;
import com.mygdx.game.Screens.battleDeck.Sprites.shipSecSprite;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.Actors.systemControlHandle;
import com.mygdx.game.Screens.systemScreen.Actors.systemControlModule;
import com.mygdx.game.Screens.systemScreen.Actors.systemMapGalaxy;
import com.mygdx.game.Screens.systemScreen.Actors.systemMapPlanet;
import com.mygdx.game.Screens.systemScreen.Actors.systemMapSystem;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustDown;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustLeft;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustRight;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustUp;
import com.mygdx.game.Screens.systemScreen.Actors.systemTiles;
import com.mygdx.game.Screens.systemScreen.Sprites.doorImage;
import com.mygdx.game.Screens.systemScreen.Sprites.shipRoomSprite;
import com.mygdx.game.Screens.systemScreen.Sprites.systemScreenShipGroup;
import com.mygdx.game.Screens.systemScreen.Tools.Graph;
import com.mygdx.game.Screens.systemScreen.Tools.Vertex;
import com.mygdx.game.Screens.systemScreen.systemScreen;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class systemScreenShipStage implements Disposable {

    public Table table;
    public Stage stage;
    private MyGdxGame game;
    public Viewport viewport;
    private float viewportWidth;
    private float viewportHeight;

    // shipLayout variables
    public int roomCount;
    private int roomsX = 12;
    private int roomsY = 12;
    private enum roomTypes {cockpitV,cockpitH,BigSquare,teeH,teeV,corridorV,corridorH,verticalCorR,EngineRoom,Medbay,OxyTyms,ShieldRoom,surveillanceRoom,airlockH,airlockV};
    private roomTypes[][] shipLayout = new roomTypes[roomsX][roomsY];      // room layout for player ship
    private float[][][] shipStatus = new float[roomsX][roomsY][3];
    private HashMap<String, boolean[][][]> doorMappings3 = new HashMap<>();      // sides for doors depending on type of room
    public int[][] roomShipMap = new int[roomsX][roomsY];
    private Graph shipGraph;
    private Vertex vertices[];
    private shipRoomSprite[] playerRooms;
    private shipRoomSprite[] playerRoomsAirOver;
    private shipRoomSprite[] fireTiles;
    private doorImage[] playerDoors;
    private float[][] pDoors;
    private systemScreenShipGroup systemShipGroup;

    // Atlas Regions
    private TextureAtlas.AtlasRegion doors[];
    TextureAtlas roomsAt;

    public systemScreenShipStage(MyGdxGame game, World world, systemScreen2 screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        this.viewportWidth=viewportWidth;
        this.viewportHeight=viewportHeight;
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);
        //roomsAt = game.getRoomsAt();
        this.game=game;

        //systemShipGroup = new systemScreenShipGroup(game,world,screen,sb,viewportWidth,viewportHeight);
        stage.addActor(systemShipGroup);
        systemShipGroup.setWidth(5000);
        System.out.println("get group image width here "+systemShipGroup.getWidth());
        systemShipGroup.setPosition(0,0);//viewportWidth/2-systemShipGroup.getWidth()*150/2,viewportHeight/2-systemShipGroup.getHeight()*150/2);

        /*overTile = new TextureAtlas.AtlasRegion[roomTypes.values().length];
        //String overs[] = {"CockDraft1","BigSquare","horizontalCorB","horizontalCorT","normalCor","verticalCorL","verticalCorR","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm"};
        //for (i = 0; i < overTile.length; i++) {
        for (roomTypes dir : roomTypes.values()){
            overTile[i] = roomsAt.findRegion(dir.name());
        }*/



        //stage.setDebugAll(true);
    }

    public void updateGroupCoords(float x, float y){
        systemShipGroup.setPosition(x,y);
    }

    @Override
    public void dispose() {

    }
}
