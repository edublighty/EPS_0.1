package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.Sprites.shipSecSprite;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dEdge;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dVertex;
import com.mygdx.game.Screens.systemScreen.Tools.Edge;
import com.mygdx.game.Screens.systemScreen.Tools.Graph;
import com.mygdx.game.Screens.systemScreen.Tools.Vertex;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class systemScreenShipGroup extends Group {

    private MyGdxGame game;
    public Viewport viewport;
    private float viewportWidth;
    private float viewportHeight;
    private float shipWidth;
    private float shipHeight;
    private float tileWidth;

    // shipLayout variables
    public int roomCount;
    private int roomsX = 12;
    private int roomsY = 12;
    private enum roomTypes {oTwo,surveillance,engine,armory,reactor,cockpit,airlock,shields,medbay,cargobay};
    private enum roomSizes {corridor1x1,corridor2x1,corridor3x1,corridor2x2,corridor3x2};
    private roomSizes[][] shipLayout = new roomSizes[roomsX][roomsY];      // room layout for player ship
    private roomTypes[][] systemLayout = new roomTypes[roomsX][roomsY];      // room layout for player ship
    private float[][][] shipStatus = new float[roomsX][roomsY][3];
    private HashMap<String, boolean[][][]> doorMappings3 = new HashMap<>();      // sides for doors depending on type of room
    public int[][] roomShipMap = new int[roomsX][roomsY];
    private int[][] roomBlanksBoundary = new int [roomsX+2][roomsY+2];
    private Graph shipGraph;
    private Graph boundaryGraph;
    private Vertex vertices[];
    private Vertex boundaryVertices[];
    private shipRoomSprite[] playerRooms;
    private shipRoomSprite[] playerExternals;
    public shipRoomSprite[] playerRoomsAirOver;
    public shipRoomSprite[] playerRoomsSystems;
    private shipRoomButton[] playerRoomsSystemsEdge;
    private shipRoomButton[] playerRoomsSystemsName;
    private shipRoomButton[] playerRoomsSystemsDamage;
    private shipRoomButton playerRoomsShield;
    private shipRoomSprite[] fireTiles;
    private doorImage[] playerDoors;
    private float[][] pDoors;
    private float shipWidthPx;
    private float shipHeightPx;

    // Atlas Regions
    private TextureAtlas.AtlasRegion doors[];
    TextureAtlas roomsAt;
    public World world;
    public Body b2body;
    private Image backdrop;
    private systemScreen2 screen;

    public systemScreenShipGroup(MyGdxGame game,World world, systemScreen2 screen, SpriteBatch sb, float toteSize){

        /*viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        this.viewportWidth=viewportWidth;
        this.viewportHeight=viewportHeight;*/

        roomsAt = game.getRoomsAt();
        this.game=game;
        this.world=world;
        this.screen = screen;

        /*overTile = new TextureAtlas.AtlasRegion[roomTypes.values().length];
        //String overs[] = {"CockDraft1","BigSquare","horizontalCorB","horizontalCorT","normalCor","verticalCorL","verticalCorR","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm"};
        //for (i = 0; i < overTile.length; i++) {
        for (roomTypes dir : roomTypes.values()){
            overTile[i] = roomsAt.findRegion(dir.name());
        }*/

        doors = new TextureAtlas.AtlasRegion[2];
        doors[0] = game.getShipObjsAt().findRegion("doorH");
        doors[1] = game.getShipObjsAt().findRegion("doorV");

        roomTypesSet();
        genShip(screen);
        //defineSprite(getWidth());
        //setBounds(this.getX(),this.getY(),(toteSize)/(MyGdxGame.PPM*100),toteSize/(MyGdxGame.PPM*100));
        //setBounds(this.getX(),this.getY(),(this.getWidth())/(MyGdxGame.PPM*30),(this.getHeight())/(MyGdxGame.PPM*30));

        setHeight((toteSize));
        setWidth((toteSize));
        //setPosition(this.getX()-this.getWidth()/2,this.getY()-this.getHeight()/2);
        setOrigin(this.getWidth()/2,this.getHeight()/2);
        //stage.setDebugAll(true);

        //setUpShip();
    }

    public shipRoomSprite[] getSystems(){
        return playerRoomsSystems;
    }

    public shipRoomButton[] getSystemLabels(){
        return playerRoomsSystemsName;
    }

    public shipRoomButton[] getSystemSwitches(){
        return playerRoomsSystemsEdge;
    }

    public shipRoomButton[] getSystemDamageBars(){
        return playerRoomsSystemsDamage;
    }

    public void update(float dt){
        //setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
        //System.out.println("update group width "+this.getWidth()+" and "+this.getHeight());
        for(int i=0;i<playerDoors.length;i++){
            if(playerDoors[i] == null){
            } else {
                playerDoors[i].update(dt);
            }
        }
    }

    public void defineSprite(float toteSize){
        BodyDef bdef = new BodyDef();
        bdef.position.set(toteSize/2 / MyGdxGame.PPM,toteSize / (MyGdxGame.PPM*2));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(1 / MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
        fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef);
/*        EdgeShape front = new EdgeShape();
        front.set(new Vector2(5/MyGdxGame.PPM,5/MyGdxGame.PPM),new Vector2(5/MyGdxGame.PPM,-5/MyGdxGame.PPM));
        fdef.shape = front;
        fdef.isSensor = true;   // no longer collides as sensor

        b2body.createFixture(fdef).setUserData("front");*/
        System.out.println("definition group width "+this.getWidth()+" and "+this.getHeight());
    }

    public void roomTypesSet(){

        /*

        // This method first initialises the room types and their dimensions for use in ship generation
        int i = 0;
        List<Float> tempSize3 = new ArrayList<Float>();

        //String overs[] = {"CockDraft1","BigSquare","horizontalCorB","horizontalCorT","normalCor","verticalCorL","verticalCorR","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm","airlockB","airlockT","airlockL","airlockR"};
        for (roomTypes dir : roomTypes.values()){//i = 0; i < overTile.length; i++) {
            // luckily despite this code being wank, all the tiles are 100x100
            tempSize3.add(100f);//overTile[i].getRegionWidth()*1f);
            tempSize3.add(100f);//overTile[i].getRegionHeight()*1f);
            roomMapping.put(dir.name(),tempSize3);
            tempSize3 = new ArrayList<>();
            i++;
        }

        */

        // Next it identifies roughly where the doors go in grid structure depending on room type
        //List<Float> doorPos = new ArrayList<Float>();
        float[][][] tileDoors = new float[2][2][4];
        boolean[][][] tileDoors2 = new boolean[2][2][4];
        Float[][] doorPos2 = new Float[2][4];
        int j;
        int i = 0;
        for (roomTypes dir : roomTypes.values()){//i = 0; i < overTile.length; i++) {

            // set doors according to tile in room structure
            for(i=0;i<tileDoors.length;i++){
                for(j=0;j<tileDoors[0].length;j++){
                    tileDoors2[i][j][0] = false;
                    tileDoors2[i][j][1] = false;
                    tileDoors2[i][j][2] = false;
                    tileDoors2[i][j][3] = false;
                }
            }

            float roomWidth = 100f;     // get from graphic width/height
            float roomHeight = 100f;

            if(roomWidth==100f){
                if(roomHeight==100f){
                    // 2x2 tile room
                    // set doors according to tile in room structure
                    for(i=0;i<tileDoors.length;i++){
                        for(j=0;j<tileDoors[0].length;j++){
                            if(i==0){
                                // tile on left
                                tileDoors2[i][j][1] = true;
                                if(j==0){
                                    // bottom left tile
                                    tileDoors2[i][j][0] = true;
                                } else {
                                    // top left tile
                                    tileDoors2[i][j][2] = true;
                                }

                            } else {
                                // tile on right
                                tileDoors2[i][j][3] = true;
                                if(j==0){
                                    // bottom right tile
                                    tileDoors2[i][j][0] = true;
                                } else {
                                    // top right tile
                                    tileDoors2[i][j][2] = true;
                                }
                            }
                        }
                    }
                } else {
                    // 2x1 tile room
                    for(i=0;i<tileDoors.length;i++){
                        for(j=0;j<tileDoors[0].length;j++){
                            if(i==0){
                                // tile on left
                                tileDoors2[i][j][0] = true;
                                tileDoors2[i][j][1] = true;
                                tileDoors2[i][j][2] = true;
                            } else {
                                // tile on right
                                tileDoors2[i][j][0] = true;
                                tileDoors2[i][j][2] = true;
                                tileDoors2[i][j][3] = true;
                            }
                        }
                    }
                }
            } else {
                // 1x2 or 1x1 tile room
                if(roomHeight==100){
                    // 1x2 tile room
                    for(i=0;i<tileDoors.length;i++){
                        for(j=0;j<tileDoors[0].length;j++){
                            if(i==0){
                                // tile on top
                                tileDoors2[i][j][1] = true;
                                tileDoors2[i][j][2] = true;
                                tileDoors2[i][j][3] = true;
                            } else {
                                // tile on bottom
                                tileDoors2[i][j][0] = true;
                                tileDoors2[i][j][1] = true;
                                tileDoors2[i][j][3] = true;
                            }
                        }
                    }
                } else {
                    // 1x1 tile room
                    i=0;
                    j=0;
                    tileDoors2[i][j][0] = true;
                    tileDoors2[i][j][1] = true;
                    tileDoors2[i][j][2] = true;
                    tileDoors2[i][j][3] = true;
                }
            }

            /*if(dir == roomTypes.cockpitV){
                tileDoors2[0][0][1] = false;
                tileDoors2[0][0][3] = false;
            }*/

            doorMappings3.put(dir.name(),tileDoors2);
            tileDoors2 = new boolean[2][2][4];

        }
    }

    public void updateAir(float alpha, int i){
        Color temp = playerRoomsAirOver[vertices[i].numROomsCount].getColor();//(alpha);
        playerRoomsAirOver[vertices[i].numROomsCount].setColor(temp.r,temp.g,temp.b,alpha);
    }

    public void genShip(systemScreen2 screen){
        // Method to generate layout of enemy and player ships - order as below
        // "CockDraft1","BigSquare","horizontalCor","normalCor","VerticalCor","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm"

        // START DEFINING PLAYER SHIP by room type

        /*shipLayout[6][2]= roomTypes.cockpitV;
        shipLayout[4][2]= roomTypes.BigSquare;
        shipLayout[3][6]= roomTypes.airlockH;
        shipLayout[3][0]= roomTypes.ShieldRoom;//vertical corridor
        shipLayout[3][4]= roomTypes.Medbay;//vertical corridor
        shipLayout[2][2]= roomTypes.EngineRoom;//horizontal corridor
        shipLayout[0][1]= roomTypes.surveillanceRoom;//horizontal corridor
        shipLayout[0][3]= roomTypes.BigSquare;//horizontal corridor*/
/*

        shipLayout[0][3] = roomSizes.corridor2x1;
        shipLayout[2][3] = roomSizes.corridor3x1;
        shipLayout[5][3] = roomSizes.corridor2x1;
        shipLayout[3][4] = roomSizes.corridor2x1;
        shipLayout[1][2] = roomSizes.corridor2x1;

        systemLayout[0][3] = roomTypes.oTwo;
        systemLayout[1][3] = roomTypes.shields;
        systemLayout[2][3] = roomTypes.engine;
        systemLayout[3][3] = roomTypes.medbay;
        systemLayout[5][3] = roomTypes.surveillance;
        systemLayout[4][4] = roomTypes.cockpit;
        systemLayout[1][2] = roomTypes.airlock;
*/

        shipLayout[0][0] = roomSizes.corridor3x1;
        shipLayout[0][2] = roomSizes.corridor3x1;
        shipLayout[2][1] = roomSizes.corridor3x1;
        shipLayout[3][0] = roomSizes.corridor2x1;
        shipLayout[3][2] = roomSizes.corridor2x1;
        shipLayout[5][1] = roomSizes.corridor3x1;
        shipLayout[6][0] = roomSizes.corridor2x1;
        shipLayout[7][2] = roomSizes.corridor3x1;
        shipLayout[8][3] = roomSizes.corridor2x1;

        systemLayout[0][0] = roomTypes.engine;
        systemLayout[2][1] = roomTypes.shields;
        systemLayout[4][2] = roomTypes.airlock;
        systemLayout[4][1] = roomTypes.reactor;
        systemLayout[5][1] = roomTypes.surveillance;
        systemLayout[7][0] = roomTypes.medbay;
        systemLayout[7][1] = roomTypes.oTwo;
        systemLayout[9][2] = roomTypes.armory;
        systemLayout[9][3] = roomTypes.cockpit;

        shipGraph = new Graph();

        // initialise ship status by room
        for(int i=0;i<shipStatus.length;i++){
            for(int j=0;j<shipStatus[0].length;j++){
                // venting = 1
                shipStatus[i][j][0] = 0;
                // atm content of room
                shipStatus[i][j][1] = 100;
            }
        }

        int numROoms = roomsX*roomsY;       // max number - 6x6
        int numROomsCount = 0;  // to track and make sure got all as sprites created
        for(int iTemp2=0;iTemp2<roomShipMap.length;iTemp2++){
            for(int jTemp2=0;jTemp2<roomShipMap[0].length;jTemp2++){
                roomShipMap[iTemp2][jTemp2]=0;
            }
        }
        numROomsCount++;
        int tileCount = 0;
        int vertexCount = 0;    // to track number of vertices
        vertices = new Vertex[numROoms*5];  // 1 vertex for room info and max 4 vertices for each room tile

        // Need to convert coordinates from graphic size to fit screen
        // work on basis of ship taking up 80% of screen height as phone forced horizontal AR
        //sWidth=(int) gameport.getWorldWidth();
        //sHeight=(int) gameport.getWorldHeight();
        //float margin = sHeight/10;
        float roomWidth = 100/100;
        float tileWidth = roomWidth/2;
        float scaling = 500;
        float TLx = 0;//(viewportWidth/2 - roomsX*roomWidth/2)*MyGdxGame.PPM; // mid point of screen
        float TLy = 0;//viewportHeight*MyGdxGame.PPM/2 - roomsY*roomWidth; // y position of BL tile
        //float sideSqr = sHeight - margin*2;    // the side is 80% of screen height
        float doorHWidth = doors[0].getRegionWidth();
        float doorHHeight = doors[0].getRegionHeight();
        float doorVWidth = doors[1].getRegionWidth();
        float doorVHeight = doors[1].getRegionHeight();
        float sideGrphc = roomsX*50;
        // ratio of screen to graphic is:
        float sGrphcRatio = 100;//sideSqr/sideGrphc;
        playerRooms = new shipRoomSprite[numROoms];
        playerExternals = new shipRoomSprite[numROoms];
        playerRoomsSystems = new shipRoomSprite[numROoms];
        playerRoomsAirOver = new shipRoomSprite[numROoms];
        playerDoors = new doorImage[numROoms*8];       // maximum of 8 doors per room
        fireTiles = new shipRoomSprite[numROoms*4];
        pDoors = new float[10][numROoms*8];
        // need intermediate list to prevent altering enum
        List<Float> tempSize3 = new ArrayList<Float>();
        List<Float> tempSize4 = new ArrayList<Float>();
        int doorCount = 0;
        float doorWidth=0;
        float doorHeight=0;
        float doorX=0;
        float doorY=0;
        int doorCountings=0;
        int systemCount = 0;

        // create system sprites
        for (int i=0;i<systemLayout.length;i++) {
            for (int j = 0; j < systemLayout[0].length; j++) {
                if (systemLayout[i][j] == null) {
                    // No system here
                } else {
                    // found a system
                    float curRoomWidth = 1f;//roomsAt.findRegion(systemLayout[i][j].name()).getRegionWidth();
                    float curRoomHeight = 1f;//roomsAt.findRegion(systemLayout[i][j].name()).getRegionHeight();
                    // determine number of tiles in room
                    int xRoom = (int) (curRoomWidth/50);    // width of room divided by width of tile
                    int yRoom = (int) (curRoomHeight/50);    // height of room divied by height of tile
                    tempSize3.add(1f);
                    tempSize3.add(1f);
                    tempSize3.add(1f);
                    tempSize3.add(1f);
                    playerRoomsSystems[systemCount] = new shipRoomSprite(game,systemLayout[i][j].name(),tempSize3,sGrphcRatio,i,j,xRoom,yRoom,systemCount);
                    System.out.println("system count "+systemCount+" roomType "+systemLayout[i][j].name());
                    systemCount++;
                    tempSize3 = new ArrayList<Float>();;
                }
            }
        }

        // Give positions and sizes of rooms
        for (int i=0;i<shipLayout.length;i++) {
            for (int j = 0; j < shipLayout[0].length; j++) {
                if (shipLayout[i][j] == null) {
                    //System.out.println("EMPTY ROOM PLAYER");
                } else {
                    float curRoomWidth = roomsAt.findRegion(shipLayout[i][j].name()).getRegionWidth();
                    float curRoomHeight = roomsAt.findRegion(shipLayout[i][j].name()).getRegionHeight();
                    for(int iTile=0;iTile<(curRoomWidth/50);iTile++){
                        for(int jTile=0;jTile<(curRoomHeight/50);jTile++){
                            roomShipMap[i+iTile][j+jTile]=numROomsCount;
                        }
                    }
                    float actRoomWidth=curRoomWidth/scaling;
                    float actRoomHeight=curRoomHeight/scaling;
                    //tempSize4 = roomMapping.get(shipLayout[i][j].name());
                    tempSize3.add(actRoomWidth);//tempSize4.get(0));
                    tempSize3.add(actRoomHeight);//tempSize4.get(1));
                    float roomX = TLx+tileWidth*i*sGrphcRatio;
                    float roomY = TLy+tileWidth*j*sGrphcRatio;
                    tempSize3.add(roomX);           // x coordinate BL
                    tempSize3.add(roomY);           // y coordinate BL
                    // determine number of tiles in room
                    int xRoom = (int) (curRoomWidth/50);    // width of room divided by width of tile
                    int yRoom = (int) (curRoomHeight/50);    // height of room divied by height of tile
                    // set core room date first
                    vertices[vertexCount] = new Vertex(shipLayout[i][j].name()+"-"+numROomsCount);
                    vertices[vertexCount].BL = new int[]{i,j};
                    vertices[vertexCount].tileNo = new int[]{xRoom,yRoom};
                    vertices[vertexCount].thisRoom = Vertex.vertexRoom.room;
                    vertices[vertexCount].numROomsCount = numROomsCount;
                    vertices[vertexCount].vertexCount = vertexCount;
                    vertices[vertexCount].roomType = shipLayout[i][j].name();
                    vertices[vertexCount].roomX = roomX;
                    vertices[vertexCount].roomY = roomY;
                    vertices[vertexCount].o2 = 100;     // initialise o2 content
                    vertices[vertexCount].roomDamage = 100; // initialise room health
                    int noEquip = 0;
                    for(int iTile=0;iTile<(curRoomWidth/50);iTile++){
                        for(int jTile=0;jTile<(curRoomHeight/50);jTile++){
                            //roomShipMap[i+iTile][j+jTile]=numROomsCount;
                            if(systemLayout[i+iTile][j+jTile] == null){
                                // no system
                            } else {
                                if(noEquip<1){
                                    vertices[vertexCount].equip1 = systemLayout[i+iTile][j+jTile].name();
                                } else if(noEquip<2){
                                    vertices[vertexCount].equip2 = systemLayout[i+iTile][j+jTile].name();
                                } else {
                                    // error so do nothing - constraining to max 2 systems per room
                                }
                            }
                        }
                    }
                    int currentVertex = vertexCount;
                    vertexCount++;
                    // now connect any previous rooms
                    for(int i2=0;i2<vertexCount;i2++){
                        if(i2==currentVertex){
                            // not doing anything
                        } else {
                            if(vertices[i2].thisRoom == Vertex.vertexRoom.room) {
                                // vertex is a room
                                // first perform a horizontal check
                                //System.out.println("about to check other room is right or equal of current room left border");
                                //System.out.println(vertices[i2].BL[0] + vertices[i2].tileNo[0] + " vs " + vertices[currentVertex].BL[0]);
                                if (vertices[i2].BL[0] + vertices[i2].tileNo[0] >= vertices[currentVertex].BL[0]) {
                                    // right border of room being checked is at least same as left border of current room (or inside)
                                    // ie assuming checked room left of current room
                                    //System.out.println("about to check current room is left or equal of other room right border");
                                    //System.out.println(vertices[currentVertex].BL[0] + vertices[currentVertex].tileNo[0] + " vs " + vertices[i2].BL[0]);
                                    if (vertices[currentVertex].BL[0] + vertices[currentVertex].tileNo[0] >= vertices[i2].BL[0]) {
                                        //System.out.println("about to check other room is above or equal of current room bottom border");
                                        //System.out.println(vertices[i2].BL[1] + vertices[i2].tileNo[1] + " vs " + vertices[currentVertex].BL[1]);
                                        if (vertices[i2].BL[1] + vertices[i2].tileNo[1] >= vertices[currentVertex].BL[1]) {
                                            //System.out.println("about to check current room is below or equal to other room top border");
                                            //System.out.println(vertices[currentVertex].BL[1] + vertices[currentVertex].tileNo[1] + " vs " + vertices[i2].BL[1]);
                                            if (vertices[currentVertex].BL[1] + vertices[currentVertex].tileNo[1] >= vertices[i2].BL[1]) {
                                                //System.out.println("should be adding room now");
                                                shipGraph.addEdge(vertices[i2], vertices[currentVertex]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // now set tiles of room starting from BL
                    int iTemp = 0;
                    int jTemp = 0;
                    // for coordinates of rooms
                    for(int i2=i;i2<i+xRoom;i2++){
                        for(int j2=j;j2<j+yRoom;j2++){
                            vertices[vertexCount] = new Vertex(shipLayout[i][j].name()+numROomsCount+"Tile"+i2+j2);
                            vertices[vertexCount].BL = new int[]{i2,j2};
                            vertices[vertexCount].thisRoom = Vertex.vertexRoom.tile;
                            vertices[vertexCount].vertexCount = vertexCount;
                            vertices[vertexCount].coreRoomCount = currentVertex;
                            vertices[vertexCount].roomType = vertices[currentVertex].roomType;
                            vertices[vertexCount].tileNo = new int[]{iTemp, jTemp};
                            vertices[vertexCount].roomX = vertices[currentVertex].roomX;
                            vertices[vertexCount].roomY = vertices[currentVertex].roomY;
                            vertices[vertexCount].fireHealth = 100;
                            vertices[vertexCount].onFire = false;
                            vertices[vertexCount].fireDamaged = false;
                            float tileX = TLx+tileWidth*i2*sGrphcRatio;
                            float tileY = TLy+tileWidth*j2*sGrphcRatio;
                            List<Float> fireArray = new ArrayList<Float>();
                            float fireWidth = roomsAt.findRegion("flame").getRegionWidth();
                            float fireHeight = roomsAt.findRegion("flame").getRegionHeight();
                            fireArray.add(fireWidth);   // fire width
                            fireArray.add(fireHeight);  // fire height
                            float fireX = tileX;//+fireWidth*sGrphcRatio/2;
                            float fireY = tileY;//+fireHeight*sGrphcRatio/2;
                            fireArray.add(fireX);           // x coordinate BL
                            fireArray.add(fireY);           // y coordinate BL
                            fireTiles[tileCount] = new shipRoomSprite(game,"flame",fireArray,sGrphcRatio,i2,j2,0,0,tileCount);
                            tileCount++;
                            jTemp++;
                            // create edge between room and tile
                            shipGraph.addEdge(vertices[currentVertex],vertices[vertexCount]);
                            vertexCount++;
                        }
                        jTemp = 0;
                        iTemp++;
                    }
                    // create edges between all tiles in room
                    int noTiles = (int) ((curRoomHeight/50)*(curRoomWidth/50));
                    if(noTiles == 1){
                        // 1x1
                        // No tiles to connect
                    } else {
                        if(noTiles == 2){
                            // 1x2
                            shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-2]);
                        } else if(noTiles == 3){
                            // 1x3
                            shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-2]);
                            shipGraph.addEdge(vertices[vertexCount-2],vertices[vertexCount-3]);
                        } else if(noTiles == 4){
                            // 2x2
                            shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-2]);
                            shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-3]);
                            shipGraph.addEdge(vertices[vertexCount-4],vertices[vertexCount-2]);
                            shipGraph.addEdge(vertices[vertexCount-4],vertices[vertexCount-3]);
                        } else {
                            // 3x2
                            shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-2]);
                            shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-4]);
                            shipGraph.addEdge(vertices[vertexCount-5],vertices[vertexCount-4]);
                            shipGraph.addEdge(vertices[vertexCount-5],vertices[vertexCount-2]);
                            shipGraph.addEdge(vertices[vertexCount-5],vertices[vertexCount-6]);
                            shipGraph.addEdge(vertices[vertexCount-3],vertices[vertexCount-2]);
                            shipGraph.addEdge(vertices[vertexCount-3],vertices[vertexCount-6]);
                        }
                    }

                    // create room sprite
                    String temp = shipLayout[i][j].name();
                    playerRooms[numROomsCount] = new shipRoomSprite(game,shipLayout[i][j].name(),tempSize3,sGrphcRatio,i,j,xRoom,yRoom,numROomsCount);
                    playerExternals[numROomsCount] = new shipRoomSprite(game,"Panel1",tempSize3,sGrphcRatio,i,j,xRoom,yRoom,numROomsCount);
                    playerRoomsAirOver[numROomsCount] = new shipRoomSprite(game,"Red",tempSize3,sGrphcRatio,i,j,xRoom,yRoom,numROomsCount);
                    numROomsCount++;
                    String s = shipLayout[i][j].name();
                    System.out.println(s.substring(0, Math.min(s.length(), 7)));
                    if(s.equals("airlockH")){
                        // airlock horizontal
                        if(shipLayout[i][j + 1] == null){
                            // nothing above so put airlock door above
                            doorWidth = doorHWidth * sGrphcRatio;
                            doorHeight = doorHHeight * sGrphcRatio;
                            doorX = roomX + roomWidth * sGrphcRatio / 2 - doorHWidth * sGrphcRatio / 2;
                            doorY = roomY + roomWidth * sGrphcRatio / 2 - doorHHeight * sGrphcRatio / 2;
                            pDoors[0][doorCount] = doorWidth;
                            pDoors[1][doorCount] = doorHeight;
                            pDoors[2][doorCount] = doorX;
                            pDoors[3][doorCount] = doorY;
                            pDoors[4][doorCount] = 1;
                            pDoors[5][doorCount] = currentVertex;
                            //pDoors[6] not required as no second room for airlock
                            vertices[vertexCount] = new Vertex("Door" + doorCount);
                            vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                            vertices[vertexCount].vertexCount = vertexCount;
                            vertices[vertexCount].doorCount = doorCount;
                            shipGraph.addEdge(vertices[currentVertex],vertices[vertexCount]);
                            vertexCount++;
                            doorCount++;
                        }
                        if(shipLayout[i][j - 1] == null){
                            // bottom airlock - create door on top
                            doorWidth = doorHWidth * sGrphcRatio;
                            doorHeight = doorHHeight * sGrphcRatio;
                            doorX = roomX + roomWidth * sGrphcRatio / 2 - doorHWidth * sGrphcRatio / 2;
                            doorY = roomY + roomWidth * sGrphcRatio / 2 - doorHHeight * sGrphcRatio / 2;
                            pDoors[0][doorCount] = doorWidth;
                            pDoors[1][doorCount] = doorHeight;
                            pDoors[2][doorCount] = doorX;
                            pDoors[3][doorCount] = doorY;
                            pDoors[4][doorCount] = 1;
                            pDoors[5][doorCount] = currentVertex;
                            //pDoors[6] not required as no second room for airlock
                            vertices[vertexCount] = new Vertex("Door" + doorCount);
                            vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                            vertices[vertexCount].vertexCount = vertexCount;
                            vertices[vertexCount].doorCount = doorCount;
                            shipGraph.addEdge(vertices[currentVertex],vertices[vertexCount]);
                            doorCount++;
                            vertexCount++;
                        }
                    }
                    if(s.equals("airlockV")){
                        if(shipLayout[i+1][j] == null){
                            // right airlock - create door on left
                            doorWidth = doorVWidth*sGrphcRatio;
                            doorHeight=doorVHeight*sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio/2 - doorVWidth*sGrphcRatio/2;
                            doorY = roomY + roomWidth*sGrphcRatio/2 - doorVHeight*sGrphcRatio/2;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=1;
                            pDoors[5][doorCount]=currentVertex;
                            //pDoors[6] not required as no second room for airlock
                            vertices[vertexCount] = new Vertex("Door" + doorCount);
                            vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                            vertices[vertexCount].vertexCount = vertexCount;
                            vertices[vertexCount].doorCount = doorCount;
                            shipGraph.addEdge(vertices[currentVertex],vertices[vertexCount]);
                            doorCount++;
                            vertexCount++;
                        }
                        if(shipLayout[i-1][j] == null){
                            // left airlock - create door on right
                            doorWidth = doorVWidth*sGrphcRatio;
                            doorHeight=doorVHeight*sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio/2 - doorVWidth*sGrphcRatio/2;
                            doorY = roomY + roomWidth*sGrphcRatio/2 - doorVHeight*sGrphcRatio/2;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=1;
                            pDoors[5][doorCount]=currentVertex;
                            //pDoors[6] not required as no second room for airlock
                            vertices[vertexCount] = new Vertex("Door" + doorCount);
                            vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                            vertices[vertexCount].vertexCount = vertexCount;
                            vertices[vertexCount].doorCount = doorCount;
                            shipGraph.addEdge(vertices[currentVertex],vertices[vertexCount]);
                            doorCount++;
                            vertexCount++;
                        }
                    }
                    tempSize3 = new ArrayList<>();
                    tempSize4 = new ArrayList<>();
                }
            }
        }

        for(int i=0;i<roomShipMap.length;i++){
            for(int j=0;j<roomShipMap[0].length;j++){
                // sweeping all tiles to check for room changes and therefore door requirement
                // check left (vertical door) then below (horizontal door)
                // check left first
                if(i>0){
                    if(roomShipMap[i][j]>0){
                        // theres a room at this tile
                        if(roomShipMap[i-1][j]>0) {
                            // theres a room to the left too
                            if (roomShipMap[i][j] != roomShipMap[i - 1][j]) {
                                // these rooms are different
                                // compare doors
                                int targetRight = roomShipMap[i][j];
                                int targetLeft = roomShipMap[i - 1][j];
                                String roomTypeRight = null;
                                String roomTypeLeft = null;
                                int vertexRight = 0;
                                int vertexLeft = 0;
                                boolean searching = true;
                                int iTemp = 0;
                                int temp = 0;
                                while (searching) {
                                    if (iTemp < vertices.length) {
                                        if(vertices[iTemp].thisRoom == Vertex.vertexRoom.room) {
                                            if (vertices[iTemp].numROomsCount == targetRight) {
                                                roomTypeRight = vertices[iTemp].roomType;
                                                vertexRight = vertices[iTemp].vertexCount;
                                            } else if(vertices[iTemp].numROomsCount == targetLeft) {
                                                roomTypeLeft = vertices[iTemp].roomType;
                                                vertexLeft = vertices[iTemp].vertexCount;
                                            }
                                            if (roomTypeRight != null && roomTypeLeft != null) {
                                                searching = false;
                                            }
                                        }
                                        iTemp++;
                                    }
                                }

                                boolean door1[][][] = doorMappings3.get(roomTypeLeft);
                                boolean door2[][][] = doorMappings3.get(roomTypeRight);

                                boolean createDoor = false;
                                Vertex tileRight = null;
                                Vertex tileLeft = null;
                                // Check right most room for tile desired
                                for (int iTemp2 = 0; iTemp2 < vertices[vertexRight].getNeighborCount(); iTemp2++) {
                                    // find correct tile
                                    Vertex vTemp1 = vertices[vertexRight].getNeighbor(iTemp2).getOne();
                                    Vertex vTemp2 = vertices[vertexRight].getNeighbor(iTemp2).getTwo();
                                    if (vTemp1.thisRoom == Vertex.vertexRoom.tile) {
                                        // found a tile - check coordinates to be level with tile in question
                                        if (vTemp1.BL[0] == i && vTemp1.BL[1] == j) {
                                            // found tile
                                            tileRight = vTemp1;
                                        }
                                    } else if (vTemp2.thisRoom == Vertex.vertexRoom.tile) {
                                        // Its the other one
                                        if (vTemp2.BL[0] == i && vTemp2.BL[1] == j) {
                                            // found tile
                                            tileRight = vTemp2;
                                        }
                                    }
                                }
                                // check left most room for tile desired
                                for (int iTemp2 = 0; iTemp2 < vertices[vertexLeft].getNeighborCount(); iTemp2++) {
                                    // find correct tile
                                    Vertex vTemp1 = vertices[vertexLeft].getNeighbor(iTemp2).getOne();
                                    Vertex vTemp2 = vertices[vertexLeft].getNeighbor(iTemp2).getTwo();
                                    if (vTemp1.thisRoom == Vertex.vertexRoom.tile) {
                                        // found a tile - check coordinates to be level with tile in question
                                        if (vTemp1.BL[0] == (i - 1) && vTemp1.BL[1] == j) {
                                            // found tile
                                            tileLeft = vTemp1;
                                        }
                                    } else if (vTemp2.thisRoom == Vertex.vertexRoom.tile) {
                                        // Its the other one
                                        if (vTemp2.BL[0] == (i - 1) && vTemp2.BL[1] == j) {
                                            // found tile
                                            tileLeft = vTemp2;
                                        }
                                    }
                                }

                                createDoor = true;

                                /*if (door1[tileLeft.BL[0] - vertices[tileLeft.coreRoomCount].BL[0]][(tileLeft.BL[1] - vertices[tileLeft.coreRoomCount].BL[1])][3] == true && door2[0][(tileRight.BL[1] - vertices[tileRight.coreRoomCount].BL[1])][1] == true) {
                                    createDoor = true;
                                }*/


                                if (createDoor) {
                                    // got tiles to connect to door now
                                    // vertical door required
                                    vertices[vertexCount] = new Vertex("Door" + doorCount);
                                    vertices[vertexCount].BL = new int[]{i, j};
                                    vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                    vertices[vertexCount].vertexCount = vertexCount;
                                    vertices[vertexCount].doorCount = doorCount;
                                    doorWidth = doorVWidth * sGrphcRatio;
                                    doorHeight = doorVHeight * sGrphcRatio;

                                    // create edge between door and tiles
                                    shipGraph.addEdge(vertices[tileRight.vertexCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileLeft.vertexCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileRight.coreRoomCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileLeft.coreRoomCount], vertices[vertexCount]);

                                    doorX = TLx + tileWidth * vertices[vertexCount].BL[0] * sGrphcRatio - doorWidth / 2;    // actually the tile x coordinate
                                    doorY = TLy + tileWidth * vertices[vertexCount].BL[1] * sGrphcRatio + tileWidth * sGrphcRatio / 2 - doorHeight / 2;    // actually tile y coordinate

                                    pDoors[0][doorCount] = doorWidth;
                                    pDoors[1][doorCount] = doorHeight;
                                    pDoors[2][doorCount] = doorX;
                                    vertices[vertexCount].doorX = doorX;
                                    pDoors[3][doorCount] = doorY;
                                    vertices[vertexCount].doorY = doorY;
                                    pDoors[4][doorCount] = 0;       // tells pDoors its a normal door
                                    pDoors[5][doorCount] = tileLeft.coreRoomCount;          // room 1 vertexCount
                                    pDoors[6][doorCount] = tileRight.coreRoomCount;         // room 2 vertexCount
                                    pDoors[7][doorCount] = vertexCount;
                                    pDoors[8][doorCount] = i;
                                    pDoors[9][doorCount] = j;
                                    vertexCount++;
                                    doorCount++;
                                }
                            }
                        }
                    }
                }

                // check below next
                if(j>0){
                    if(roomShipMap[i][j]>0){
                        // theres a room at this tile
                        if(roomShipMap[i][j-1]>0) {
                            // theres a room to the left too
                            if (roomShipMap[i][j] != roomShipMap[i][j-1]) {
                                // these rooms are different
                                // compare doors
                                int targetTop = roomShipMap[i][j];
                                int targetBottom = roomShipMap[i][j-1];
                                String roomTypeTop=null;
                                String roomTypeBottom=null;
                                int vertexTop=0;
                                int vertexBottom=0;
                                boolean searching = true;
                                int iTemp = 0;
                                int temp = 0;
                                while (searching){
                                    if(iTemp < vertices.length) {
                                        if(vertices[iTemp].thisRoom == Vertex.vertexRoom.room) {
                                            if (vertices[iTemp].numROomsCount == targetTop) {
                                                roomTypeTop = vertices[iTemp].roomType;
                                                vertexTop = vertices[iTemp].vertexCount;
                                            }
                                            if (vertices[iTemp].numROomsCount == targetBottom) {
                                                roomTypeBottom = vertices[iTemp].roomType;
                                                vertexBottom = vertices[iTemp].vertexCount;
                                            }
                                            if (roomTypeTop != null && roomTypeBottom != null) {
                                                searching = false;
                                            }
                                        }
                                        iTemp++;
                                    }
                                }
                                boolean door1[][][] = doorMappings3.get(roomTypeTop);
                                boolean door2[][][] = doorMappings3.get(roomTypeBottom);
                                /*// vertices 1 and 2 are rooms, not tiles
                                if(vertices[vertex1].BL[0]<vertices[vertex2].BL[0]){
                                    // vertex1 is on left
                                    if(door1[1][(j-vertices[vertex1].BL[1])][3]==1 && door2[0][(j-vertices[vertex2].BL[1])][1]==1){
                                        // door to be created
                                        vertexChoice = vertex2;
                                        vertexOther = vertex1;
                                    }
                                } else {
                                    // vertex2 is on left
                                    if(door1[1][(j-vertices[vertex1].BL[1])][1]==1 && door2[0][(j-vertices[vertex2].BL[1])][3]==1){
                                        vertexChoice = vertex1;
                                        vertexOther = vertex2;
                                    }
                                }*/
                                boolean createDoor = false;
                                Vertex tileTop=null;
                                Vertex tileBottom=null;
                                // Check top most room for tile desired
                                for(int iTemp2=0;iTemp2<vertices[vertexTop].getNeighborCount();iTemp2++){
                                    // find correct tile
                                    Vertex vTemp1 = vertices[vertexTop].getNeighbor(iTemp2).getOne();
                                    Vertex vTemp2 = vertices[vertexTop].getNeighbor(iTemp2).getTwo();
                                    if(vTemp1.thisRoom == Vertex.vertexRoom.tile){
                                        // found a tile - check coordinates to be level with tile in question
                                        if(vTemp1.BL[0]==i && vTemp1.BL[1]==j){
                                            // found tile
                                            tileTop = vTemp1;
                                        }
                                    } else if(vTemp2.thisRoom == Vertex.vertexRoom.tile){
                                        // Its the other one
                                        if(vTemp2.BL[0]==i && vTemp2.BL[1]==j){
                                            // found tile
                                            tileTop = vTemp2;
                                        }
                                    }

                                }
                                // check bottom most room for tile desired
                                for(int iTemp2=0;iTemp2<vertices[vertexBottom].getNeighborCount();iTemp2++) {
                                    // find correct tile
                                    Vertex vTemp1 = vertices[vertexBottom].getNeighbor(iTemp2).getOne();
                                    Vertex vTemp2 = vertices[vertexBottom].getNeighbor(iTemp2).getTwo();
                                    if (vTemp1.thisRoom == Vertex.vertexRoom.tile) {
                                        // found a tile - check coordinates to be level with tile in question
                                        if (vTemp1.BL[0] == i && vTemp1.BL[1] == j - 1) {
                                            // found tile
                                            tileBottom = vTemp1;
                                        }
                                    } else if (vTemp2.thisRoom == Vertex.vertexRoom.tile) {
                                        // Its the other one
                                        if (vTemp2.BL[0] == i && vTemp2.BL[1] == j - 1) {
                                            // found tile
                                            tileBottom = vTemp2;
                                        }
                                    }
                                }

                                createDoor = true;

                                /*if(door1[(tileBottom.BL[0]-vertices[tileBottom.coreRoomCount].BL[0])][(tileBottom.BL[1]-vertices[tileBottom.coreRoomCount].BL[1])][2] == true && door2[(tileTop.BL[0]-vertices[tileTop.coreRoomCount].BL[0])][(tileTop.BL[1]-vertices[tileTop.coreRoomCount].BL[1])][0] == true){
                                    createDoor = true;
                                }*/
                                if(createDoor) {
                                    // got tiles to connect to door now
                                    // vertical door required
                                    vertices[vertexCount] = new Vertex("Door" + doorCount);
                                    //System.out.println("vertexCount at door creation vertical " + vertexCount);
                                    vertices[vertexCount].BL = new int[]{i, j};
                                    vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                    vertices[vertexCount].vertexCount = vertexCount;
                                    vertices[vertexCount].horizDoor = true;
                                    doorWidth = doorHWidth * sGrphcRatio;
                                    doorHeight = doorHHeight * sGrphcRatio;
                                    // create edge between door and tiles
                                    shipGraph.addEdge(vertices[tileTop.vertexCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileBottom.vertexCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileTop.coreRoomCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileBottom.coreRoomCount], vertices[vertexCount]);

                                    doorX = TLx + tileWidth * vertices[vertexCount].BL[0] * sGrphcRatio + tileWidth * sGrphcRatio / 2 - doorWidth / 2;    // actually the tile x coordinate
                                    doorY = TLy + tileWidth * vertices[vertexCount].BL[1] * sGrphcRatio - doorHeight / 2;    // actually tile y coordinate
                                    pDoors[0][doorCount] = doorWidth;
                                    pDoors[1][doorCount] = doorHeight;
                                    pDoors[2][doorCount] = doorX;
                                    vertices[vertexCount].doorX = doorX;
                                    pDoors[3][doorCount] = doorY;
                                    vertices[vertexCount].doorY = doorY;
                                    pDoors[4][doorCount] = 0;
                                    pDoors[5][doorCount] = tileBottom.coreRoomCount;
                                    pDoors[6][doorCount] = tileTop.coreRoomCount;
                                    pDoors[7][doorCount] = vertexCount;
                                    pDoors[8][doorCount] = i;
                                    pDoors[9][doorCount] = j;
                                    vertexCount++;
                                    doorCount++;
                                }
                            }
                        }
                    }
                }
            }
        }

        boundaryGraph = new Graph();
        boundaryVertices = new Vertex[numROoms*5];  // 1 vertex for room info and max 4 vertices for each room tile

        // initialise array with 0s
        // room blanks boundary has additional 1 tile border compared to roomShipMap to account for rooms on the edge
        for(int i=0;i<roomBlanksBoundary.length;i++) {
            for (int j = 0; j < roomBlanksBoundary[0].length; j++) {
                roomBlanksBoundary[i][j] = 0;
            }
        }

        int boundCount = 0;

        for(int i=0;i<roomShipMap.length;i++) {
            for (int j = 0; j < roomShipMap[0].length; j++) {
                if(roomShipMap[i][j]>0) {
                    // found a room
                    roomBlanksBoundary[i][j] = 1;
                    // look left
                    try {
                        if (roomShipMap[i - 1][j] == 0) {
                            // blank to left
                            roomBlanksBoundary[i - 1 + 1][j + 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i - 1 + 1][j + 1] = 1;
                        boundCount++;
                    }
                    // look top left
                    try {
                        if (roomShipMap[i - 1][j + 1] == 0) {
                            // blank to left
                            roomBlanksBoundary[i - 1 + 1][j + 1 + 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i - 1 + 1][j + 1 + 1] = 1;
                        boundCount++;
                    }
                    // look bottom left
                    try {
                        if (roomShipMap[i - 1][j - 1] == 0) {
                            // blank to left
                            roomBlanksBoundary[i - 1 + 1][j + 1 - 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i - 1 + 1][j + 1 - 1] = 1;
                        boundCount++;
                    }
                    // everybody look right
                    try {
                        if (roomShipMap[i + 1][j] == 0) {
                            // blank to right
                            roomBlanksBoundary[i + 1 + 1][j + 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i + 1 + 1][j + 1] = 1;
                        boundCount++;
                    }
                    // everybody look top right
                    try {
                        if (roomShipMap[i + 1][j + 1] == 0) {
                            // blank to right
                            roomBlanksBoundary[i + 1 + 1][j + 1 + 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i + 1 + 1][j + 1 + 1] = 1;
                        boundCount++;
                    }
                    // everybody look bottom right
                    try {
                        if (roomShipMap[i + 1][j - 1] == 0) {
                            // blank to right
                            roomBlanksBoundary[i + 1 + 1][j + 1 - 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i + 1 + 1][j + 1 - 1] = 1;
                        boundCount++;
                    }
                    // nobody sayign stop that
                    // but seriously look up
                    try {
                        if (roomShipMap[i][j + 1] == 0) {
                            // blank above
                            roomBlanksBoundary[i + 1][j + 1 + 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i + 1][j + 1 + 1] = 1;
                        boundCount++;
                    }
                    // look down
                    try {
                        if (roomShipMap[i][j - 1] == 0) {
                            // room below
                            roomBlanksBoundary[i + 1][j - 1 + 1] = 1;
                            boundCount++;
                        }
                    } catch (Exception e) {
                        // out of bounds or array so treat as if a blank space
                        roomBlanksBoundary[i + 1][j - 1 + 1] = 1;
                        boundCount++;
                    }
                }
            }
        }

        float[][] roomBlanksCoords = new float[boundCount][2];
        boundCount = 0;

        for(int i=0;i<roomBlanksBoundary.length;i++) {
            for (int j = 0; j < roomBlanksBoundary[0].length; j++) {
                if(roomBlanksBoundary[i][j]>0) {
                    boundaryVertices[boundCount] = new Vertex("boundary" + i + j);
                    boundaryVertices[boundCount].iBound = i;
                    boundaryVertices[boundCount].jBound = j;
                    boundaryVertices[boundCount].vertexCount = boundCount;
                    boundaryGraph.addVertex(boundaryVertices[boundCount],true);
                    roomBlanksCoords[boundCount][0]=i;
                    roomBlanksCoords[boundCount][1]=j;
                    boundCount++;
                }
            }
        }

        final String FNAME = "testBlanksLayout.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = " ";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (int i=0;i<roomBlanksBoundary.length;i++) {
                //System.out.println("systemData line "+i);
                for (int j = 0; j < roomBlanksBoundary[0].length; j++) {
                    //System.out.println("systemData column "+j);
                    line1 = line1 + String.valueOf(roomBlanksBoundary[i][j]);
                    if(String.valueOf(roomBlanksBoundary[i][j])==null){
                        bw.write("0" + "\n");
                    } else {
                        bw.write(line1 + "\n");
                    }
                    line1 = " ";
                }
                bw.write("\r\n");
                line1 = " ";
            }

            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }

        int tileX=0;
        int tileY = 0;
        tileWidth = 1f/40;
        int actorNum = 0;

        shipWidth = getMaxI()*tileWidth;
        shipHeight = getMaxJ()*tileWidth;

        for(int i=0;i<playerRooms.length;i++){
            if (playerRooms[i] == null) {
                // nothign to draw
            } else {
                int tempX = playerRooms[i].getiRoom();
                if(playerRooms[i].getiRoom()>tileX){
                    tileX = playerRooms[i].getiRoom();
                }
                int tempY = playerRooms[i].getjRoom();
                if(playerRooms[i].getjRoom()>tileY){
                    tileY = playerRooms[i].getjRoom();
                }
                // adjust rooms and add to group
                playerRooms[i].setWidth(playerRooms[i].getiNum()*tileWidth);
                playerRooms[i].setHeight(playerRooms[i].getjNum()*tileWidth);
                playerRooms[i].setPosition(0+playerRooms[i].getiRoom()*tileWidth,0+playerRooms[i].getjRoom()*tileWidth);
                int playerRoomsNum = actorNum;
                this.addActor(playerRooms[i]);
                actorNum++;
                // now similar for air overlay
                playerRoomsAirOver[i].setWidth(playerRooms[i].getiNum()*tileWidth);
                playerRoomsAirOver[i].setHeight(playerRooms[i].getjNum()*tileWidth);
                playerRoomsAirOver[i].setPosition(playerRooms[i].getiRoom()*tileWidth,playerRooms[i].getjRoom()*tileWidth);
                playerRoomsAirOver[i].setColor(playerRoomsAirOver[i].getColor().r,playerRoomsAirOver[i].getColor().g,playerRoomsAirOver[i].getColor().b,0);     // start zero transparency as air ok
                int playerAirRoomsNum = actorNum;
                this.addActor(playerRoomsAirOver[i]);
                actorNum++;
                playerRooms[i].setThisGroupNum(playerRoomsNum,playerAirRoomsNum);
                playerRoomsAirOver[i].setThisGroupNum(playerRoomsNum,playerAirRoomsNum);
                System.out.println("pre add to group width "+playerRooms[i].getWidth()+" height "+playerRooms[i].getWidth());
            }
        }



        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {
                // nothign to draw
            } else {
                // now adjust systems to same size as tile and position
                String temp = playerRoomsSystems[i].getRoomType();
                playerRoomsSystems[i].setWidth(tileWidth);
                playerRoomsSystems[i].setHeight(tileWidth);
                playerRoomsSystems[i].setPosition((playerRoomsSystems[i].getiRoom())* tileWidth, (playerRoomsSystems[i].getjRoom())* tileWidth);
                System.out.println("room position x "+playerRoomsSystems[i].getiRoom()+" y "+playerRoomsSystems[i].getjRoom()+" tilewidth "+tileWidth);
                this.addActor(playerRoomsSystems[i]);
                if(playerRoomsSystems[i].equals("airlock")){
                    /*// right airlock - create door on left
                    doorWidth = doorVWidth*sGrphcRatio;
                    doorHeight=doorVHeight*sGrphcRatio;
                    doorX = roomX + roomWidth*sGrphcRatio/2 - doorVWidth*sGrphcRatio/2;
                    doorY = roomY + roomWidth*sGrphcRatio/2 - doorVHeight*sGrphcRatio/2;
                    pDoors[0][doorCount]=doorWidth;
                    pDoors[1][doorCount]=doorHeight;
                    pDoors[2][doorCount]=doorX;
                    pDoors[3][doorCount]=doorY;
                    pDoors[4][doorCount]=1;
                    pDoors[5][doorCount]=currentVertex;
                    //pDoors[6] not required as no second room for airlock
                    vertices[vertexCount] = new Vertex("Door" + doorCount);
                    vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                    vertices[vertexCount].vertexCount = vertexCount;
                    vertices[vertexCount].doorCount = doorCount;
                    shipGraph.addEdge(vertices[currentVertex],vertices[vertexCount]);
                    doorCount++;
                    vertexCount++;*/
                }
            }
        }

        int temp = vertices.length;

        // creating special airlock doors
        for(int i = 0;i < temp;i++){
            if(vertices[i] == null) {
                // do owt
            } else {
                System.out.println("vertex name " + vertices[i].thisRoom);
                if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                    if (vertices[i].equip1 == null && vertices[i].equip2 == null) {
                        // no equipment in this room
                    } else {
                        if (vertices[i].equip1 == "airlock" || vertices[i].equip2 == "airlock") {
                            int iRoom = vertices[i].tileNo[0];
                            int jRoom = vertices[i].tileNo[1];
                            // create airlock above or below
                            int iAir = vertices[i].BL[0];
                            int jAir = vertices[i].BL[1];
                            vertices[i].airlock = true;
                            // check below first
                            if (roomShipMap[iAir][jAir - 1] == 0) {
                                // there is a nothing below the airlock
                                System.out.println("airlock is below");
                                doorWidth = doorHWidth * sGrphcRatio;
                                doorHeight = doorHHeight * sGrphcRatio;
                                iAir = iAir;
                                jAir = jAir;
                            } else if (roomShipMap[iAir][jAir + jRoom] == 0){
                                // there is nothing abvoe the airlock
                                System.out.println("airlock is above");
                                doorWidth = doorHWidth * sGrphcRatio;
                                doorHeight = doorHHeight * sGrphcRatio;
                                iAir = iAir;
                                jAir = jAir+jRoom;
                            } else if (roomShipMap[iAir - 1][jAir] == 0){
                                // there is nothing to left of airlock
                                System.out.println("airlock is left");
                                doorWidth = doorVWidth * sGrphcRatio;
                                doorHeight = doorVHeight * sGrphcRatio;
                                iAir = iAir;
                                jAir = jAir;
                            } else {
                                // has to be to right of airlock
                                System.out.println("airlock is right");
                                doorWidth = doorVWidth * sGrphcRatio;
                                doorHeight = doorVHeight * sGrphcRatio;
                                iAir = iAir+iRoom;
                                jAir = jAir;
                            }
                                float roomX = TLx + tileWidth * iAir * sGrphcRatio;
                                float roomY = TLy + tileWidth * (jAir + 1) * sGrphcRatio;
                                doorX = roomX + roomWidth * sGrphcRatio / 2 - doorVWidth * sGrphcRatio / 2;
                                doorY = roomY + roomWidth * sGrphcRatio / 2 - doorVHeight * sGrphcRatio / 2;
                                pDoors[0][doorCount] = doorWidth;
                                pDoors[1][doorCount] = doorHeight;
                                pDoors[2][doorCount] = doorX;
                                pDoors[3][doorCount] = doorY;
                                pDoors[4][doorCount] = 1;       // 1 = airlock
                                pDoors[5][doorCount] = vertices[i].vertexCount;
                                pDoors[8][doorCount] = iAir;
                                pDoors[9][doorCount] = jAir;
                                System.out.println("airlock connected to "+vertices[i].getLabel());
                                //pDoors[6] not required as no second room for airlock
                                vertices[vertexCount] = new Vertex("Door" + doorCount);
                                vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                vertices[vertexCount].vertexCount = vertexCount;
                                vertices[vertexCount].doorCount = doorCount;
                                vertices[vertexCount].airlock = true;
                                shipGraph.addEdge(vertices[i], vertices[vertexCount]);
                                doorCount++;
                                vertexCount++;

                            /*if(true) {
                                // otherwise airlock goes below
                                *//*doorWidth = doorVWidth * sGrphcRatio;
                                doorHeight = doorVHeight * sGrphcRatio;
                                float roomX = TLx + tileWidth * iAir * sGrphcRatio;
                                float roomY = TLy + tileWidth * (jAir) * sGrphcRatio;
                                doorX = roomX + roomWidth * sGrphcRatio / 2 - doorVWidth * sGrphcRatio / 2;
                                doorY = roomY + roomWidth * sGrphcRatio / 2 - doorVHeight * sGrphcRatio / 2;
                                pDoors[0][doorCount] = doorWidth;
                                pDoors[1][doorCount] = doorHeight;
                                pDoors[2][doorCount] = doorX;
                                pDoors[3][doorCount] = doorY;
                                pDoors[4][doorCount] = 1;
                                pDoors[5][doorCount] = vertices[i].vertexCount;
                                //pDoors[6] not required as no second room for airlock
                                vertices[vertexCount] = new Vertex("Door" + doorCount);
                                vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                vertices[vertexCount].vertexCount = vertexCount;
                                vertices[vertexCount].doorCount = doorCount;
                                shipGraph.addEdge(vertices[i], vertices[vertexCount]);
                                doorCount++;
                                vertexCount++;*//*



                                // got tiles to connect to door now
                                // vertical door required
                                vertices[vertexCount] = new Vertex("Door" + doorCount);
                                vertices[vertexCount].BL = new int[]{iAir, jAir};
                                vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                vertices[vertexCount].vertexCount = vertexCount;
                                vertices[vertexCount].doorCount = doorCount;
                                vertices[vertexCount].airlock = true;
                                doorWidth = doorHWidth * sGrphcRatio;
                                doorHeight = doorHHeight * sGrphcRatio;

                                // create edge between door and tiles
                                shipGraph.addEdge(vertices[i], vertices[vertexCount]);

                                doorX = TLx + tileWidth * vertices[vertexCount].BL[0] * sGrphcRatio - doorWidth / 2;    // actually the tile x coordinate
                                doorY = TLy + tileWidth * vertices[vertexCount].BL[1] * sGrphcRatio + tileWidth * sGrphcRatio / 2 - doorHeight / 2;    // actually tile y coordinate

                                pDoors[0][doorCount] = doorWidth;
                                pDoors[1][doorCount] = doorHeight;
                                pDoors[2][doorCount] = doorX;
                                vertices[vertexCount].doorX = doorX;
                                pDoors[3][doorCount] = doorY;
                                vertices[vertexCount].doorY = doorY;
                                pDoors[4][doorCount] = 1;       // tells pDoors its an airlock
                                pDoors[5][doorCount] = vertices[i].vertexCount;          // room 1 vertexCount
                                //pDoors[6][doorCount] = tileRight.coreRoomCount;         // room 2 vertexCount
                                //pDoors[7][doorCount] = vertexCount;
                                pDoors[8][doorCount] = iAir;
                                pDoors[9][doorCount] = jAir;
                                vertexCount++;
                                doorCount++;
                            }*/
                        }
                    }
                }
            }
        }

        screen.vertices = vertices;

        for(int i=0;i<pDoors[0].length;i++){
            if (pDoors[0][i] == 0) {
                // door has zero width so nothing to draw
            } else {
                float[] tempSize = new float[4];
                tempSize[0] = pDoors[0][i] / 1000;
                tempSize[1] = pDoors[1][i] / 1000;
                tempSize[2] = pDoors[2][i] / 100;
                tempSize[3] = pDoors[3][i] / 100;
                String doorString;
                if (pDoors[0][i] > pDoors[1][i]) {
                    if (pDoors[4][i] == 1) {
                        doorString = "airlockH";
                    } else {
                        doorString = "doorH";
                    }
                } else {
                    if (pDoors[4][i] == 1) {
                        doorString = "airlockV";
                    } else {
                        doorString = "doorV";
                    }
                }
                System.out.println("doorString for placement " + doorString);
                playerDoors[i] = new doorImage(world, screen, game, tempSize, doorString, pDoors[5][i], pDoors[6][i], pDoors[7][i], pDoors[4][i],pDoors[8][i],pDoors[9][i]);
                playerDoors[i].setWidth(pDoors[0][i] / 200000);
                playerDoors[i].setHeight(pDoors[1][i] / 200000);
                if (doorString == "doorH") {
                    playerDoors[i].setPosition(pDoors[8][i] * tileWidth + tileWidth/2 - playerDoors[i].getWidth()/2, pDoors[9][i] * tileWidth - playerDoors[i].getHeight()/2);
                } else if(doorString == "doorV"){
                    playerDoors[i].setPosition(pDoors[8][i]*tileWidth - playerDoors[i].getWidth()/2,pDoors[9][i]*tileWidth + tileWidth/2 - playerDoors[i].getHeight()/2);
                } else {
                    // hopefully airlock
                    playerDoors[i].setPosition(pDoors[8][i] * tileWidth + tileWidth/2 - playerDoors[i].getWidth()/2, pDoors[9][i] * tileWidth - playerDoors[i].getHeight()/2);
                }
                //playerDoors[i].setWidth(playerDoors[i].getiNum()*tileWidth);
                //playerDoors[i].setHeight(playerDoors[i].getjNum()*tileWidth);
                //playerDoors[i].setPosition(playerDoors[i].getiRoom()*tileWidth,playerDoors[i].getjRoom()*tileWidth);
                this.addActor(playerDoors[i]);
                //System.out.println("pre add to group width "+playerRooms[i].getWidth()+" height "+playerRooms[i].getWidth());
            }
        }

        /*
        Group externalPanels = new Group();

        for(int i=0;i<playerExternals.length-1;i++){
            if (playerExternals[i] == null) {
                // nothign to draw
            } else {
                int tempX = playerExternals[i].getiRoom();
                if(playerExternals[i].getiRoom()>tileX){
                    tileX = playerExternals[i].getiRoom();
                }
                int tempY = playerExternals[i].getjRoom();
                if(playerExternals[i].getjRoom()>tileY){
                    tileY = playerExternals[i].getjRoom();
                }
                playerExternals[i].setWidth(playerExternals[i].getiNum()*tileWidth);
                playerExternals[i].setHeight(playerExternals[i].getjNum()*tileWidth);
                playerExternals[i].setPosition(playerExternals[i].getiRoom()*tileWidth,playerExternals[i].getjRoom()*tileWidth);
                externalPanels.addActor(playerExternals[i]);
            }
        }
        */

        System.out.println("group width 1 "+this.getWidth()+" and height "+this.getHeight());
        System.out.println("tileWidths tileX "+tileX+" tileY "+tileY);

        tileX = 5*tileX;
        tileY = 5*tileY;

        int noRooms = 0;
        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                noRooms++;
            }
        }
        int noRooms2 = this.getChildren().size;

        System.out.println("children "+noRooms+" vs "+noRooms2);

        shipWidthPx = 2f*tileX;
        shipHeightPx = 2f*tileY;

        setWidth(shipWidthPx);
        setHeight(shipHeightPx);

        System.out.println("group width 2 "+this.getWidth()+" and height "+this.getHeight());

        //externalPanels.setWidth(shipWidthPx);
        //setHeight(shipHeightPx);

        // create backdrop for system detail

        // create backdrop for ship menu

        backdrop = new Image();
        backdrop.setDrawable(new TextureRegionDrawable(game.getTilesAt().findRegion("pDTTile50Red")));
        float margin = viewportHeight/10;
        float width = tileWidth*getMaxI();
        float height = tileWidth*getMaxJ();
        float X = margin / 2;
        float Y = margin / 2;
        backdrop.setWidth(width);
        backdrop.setHeight(height);
        backdrop.setX(0);
        backdrop.setY(0);
        Color color = backdrop.getColor();
        backdrop.setColor(color.r,color.g,color.b,0);
        //this.addActor(backdrop);
        playerRoomsShield = new shipRoomButton(game, this,"shipShield", false,0);
        this.addActor(playerRoomsShield);

        playerRoomsSystemsEdge = new shipRoomButton[playerRoomsSystems.length];
        playerRoomsSystemsName = new shipRoomButton[playerRoomsSystems.length];
        playerRoomsSystemsDamage = new shipRoomButton[playerRoomsSystems.length];


        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {

            } else {
                System.out.println("i " + i);
                String roomType = playerRoomsSystems[i].getRoomType();
                System.out.println("roomtype " + playerRoomsSystems[i].getRoomType());
                int systemCount2 = playerRoomsSystems[i].getArrayNum();
                playerRoomsSystemsName[i] = new shipRoomButton(game, this,roomType + "Txt", true,systemCount2);
                playerRoomsSystemsEdge[i] = new shipRoomButton(game, this,"onButton", false,systemCount2);
                playerRoomsSystemsDamage[i] = new shipRoomButton(game, this,"damage99", false,systemCount2);
                //this.addActor(playerRoomsSystemsName[i]);
                //this.addActor(playerRoomsSystemsEdge[i]);
                //this.addActor(playerRoomsSystemsDamage[i]);
            }
        }

    }

    public int getNoRooms(){
        int noRooms = 0;
        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                noRooms++;
            }
        }
        return noRooms;
    }

    public int getShipTileWidth(){
        int shipWidth = 0;
        int curI = 0;
        int iChosen = 0;
        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                if(playerRooms[i].getiRoom()>curI)
                    curI = playerRooms[i].getiRoom();
                iChosen = i;
            }
        }

        shipWidth = playerRooms[iChosen].getiNum() + curI;

        return shipWidth;
    }

    public int getShipTileHeight(){
        int shipHeight = 0;
        int curJ = 0;
        int iChosen = 0;
        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                if(playerRooms[i].getjRoom()>curJ)
                    curJ = playerRooms[i].getjRoom();
                iChosen = i;
            }
        }

        shipHeight = playerRooms[iChosen].getjNum() + curJ;

        return shipHeight;
    }


    public int getMaxI(){
        int curI = 0;
        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                if(playerRooms[i].getiRoom()>curI)
                curI = playerRooms[i].getiRoom();
            }
        }
        return curI;
    }

    public int getMaxJ(){
        int curJ = 0;
        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                if(playerRooms[i].getjRoom()>curJ)
                    curJ = playerRooms[i].getjRoom();
            }
        }
        return curJ;
    }

    public int getNoSystems(){
        int noSystems = 0;
        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {

            } else {
                noSystems++;
            }
        }
        return noSystems;
    }

    public int getDamage(int count){
        return playerRoomsSystems[count].getDamage();
    }

    public void updateDamage(int count, int damage){
        playerRoomsSystems[count].setDamage(damage);
        playerRoomsSystemsDamage[count].setDrawable(new TextureRegionDrawable(game.getRoomsAt().findRegion("damage"+damage)));
    }

    public void setUpShip(){

        /*// create backdrop for system detail

        // create backdrop for ship menu

        backdrop = new Image();
        backdrop.setDrawable(new TextureRegionDrawable(game.getTilesAt().findRegion("pDTTile50Red")));
        float margin = viewportHeight/10;
        float width = tileWidth*getMaxI();
        float height = tileWidth*getMaxJ();
        float X = margin / 2;
        float Y = margin / 2;
        backdrop.setWidth(width);
        backdrop.setHeight(height);
        backdrop.setX(0);
        backdrop.setY(0);
        Color color = backdrop.getColor();
        backdrop.setColor(color.r,color.g,color.b,0);
        this.addActor(backdrop);
        playerRoomsShield = new shipRoomButton(game, this,"shipShield", false,0);
        this.addActor(playerRoomsShield);

        playerRoomsSystemsEdge = new shipRoomButton[playerRoomsSystems.length];
        playerRoomsSystemsName = new shipRoomButton[playerRoomsSystems.length];
        playerRoomsSystemsDamage = new shipRoomButton[playerRoomsSystems.length];


        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {

            } else {
                System.out.println("i " + i);
                String roomType = playerRoomsSystems[i].getRoomType();
                System.out.println("roomtype " + playerRoomsSystems[i].getRoomType());
                int systemCount2 = playerRoomsSystems[i].getArrayNum();
                playerRoomsSystemsName[i] = new shipRoomButton(game, this,roomType + "Txt", true,systemCount2);
                playerRoomsSystemsEdge[i] = new shipRoomButton(game, this,"onButton", false,systemCount2);
                playerRoomsSystemsDamage[i] = new shipRoomButton(game, this,"damage99", false,systemCount2);
                this.addActor(playerRoomsSystemsName[i]);
                this.addActor(playerRoomsSystemsEdge[i]);
                this.addActor(playerRoomsSystemsDamage[i]);
            }
        }*/

    }

    public void resizeShip(){//float backdropHeight,float aspect){

        //System.out.println("group width "+this.getWidth()+" and height "+this.getHeight());

        Color color = backdrop.getColor();
        backdrop.setColor(color.r,color.g,color.b,0.3f);
        float aspect = screen.gamecam.viewportWidth / screen.gamecam.viewportHeight;
        float height1 = screen.gamecam.viewportHeight;
        float height2 = screen.playerShipShown.getHeight();
        float backdropHeight = height2*2;
        backdrop.setHeight(backdropHeight);
        backdrop.setWidth(backdropHeight*aspect);
        backdrop.setX(0+this.getWidth()/2-backdrop.getWidth()/2);
        backdrop.setY(0+this.getHeight()/2-backdrop.getHeight()/2);
        //System.out.println("box should be transparent");

        playerRoomsShield.setHeight(backdropHeight*0.5f);
        playerRoomsShield.setWidth(backdropHeight*aspect*0.5f);
        playerRoomsShield.setX(0+this.getWidth()/2-playerRoomsShield.getWidth()/2);
        playerRoomsShield.setY(0+this.getHeight()/2-playerRoomsShield.getHeight()/2);

        int noSystems = 0;
        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {
            } else {
                noSystems++;
            }
        }
        float txtWidth = backdrop.getWidth() / noSystems;
        System.out.println("txtWidth "+txtWidth+" vs backdrop "+backdrop.getWidth());


        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {

            } else {
                playerRoomsSystemsName[i].setWidth(txtWidth);
                playerRoomsSystemsName[i].setHeight(txtWidth / 3);
                playerRoomsSystemsName[i].setX(0 + this.getWidth()/2-backdrop.getWidth()/2 + txtWidth * i);
                float y;
                playerRoomsSystemsEdge[i].setWidth(playerRoomsSystemsName[i].getWidth());
                playerRoomsSystemsEdge[i].setHeight(playerRoomsSystemsName[i].getHeight());
                playerRoomsSystemsEdge[i].setX(playerRoomsSystemsName[i].getX()+playerRoomsSystemsName[i].getWidth()/2-playerRoomsSystemsEdge[i].getWidth()/2);
                float y2;
                playerRoomsSystemsDamage[i].setWidth(playerRoomsSystemsName[i].getWidth());
                playerRoomsSystemsDamage[i].setHeight(playerRoomsSystemsName[i].getHeight());
                playerRoomsSystemsDamage[i].setX(playerRoomsSystemsName[i].getX()+playerRoomsSystemsName[i].getWidth()/2-playerRoomsSystemsDamage[i].getWidth()/2);
                float y3;
                if(Math.pow((-1),i)>0){
                    // result is positive therefore i is even
                    // place words along bottom
                    y = 0 + this.getHeight()/2 - backdrop.getHeight()/2;
                    y2 = y + playerRoomsSystemsName[i].getHeight();
                    y3 = y2 + playerRoomsSystemsEdge[i].getHeight();
                } else {
                    // result is negative therefore i is odd
                    // place words along top
                    y = 0 + this.getHeight()/2 + backdrop.getHeight()/2 - playerRoomsSystems[i].getHeight();
                    y2 = y - playerRoomsSystemsEdge[i].getHeight();
                    y3 = y2 - playerRoomsSystemsDamage[i].getHeight();
                }
                playerRoomsSystemsName[i].setY(y);
                playerRoomsSystemsEdge[i].setY(y2);
                playerRoomsSystemsDamage[i].setY(y3);

            }
        }

        shipWidth = playerRoomsShield.getWidth();
        int noItiles = getShipTileWidth();
        float curTileWidth = shipWidth / noItiles;
        shipHeight = curTileWidth*getShipTileHeight();
        System.out.println("max X tiles "+getShipTileWidth()+" max Y tiles "+getShipTileHeight());

        // resize and position rooms

        for(int i=0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {

                // adjust rooms
                playerRooms[i].setWidth(playerRooms[i].getiNum()*curTileWidth);
                playerRooms[i].setHeight(playerRooms[i].getjNum()*curTileWidth);
                float newX = 0 + this.getWidth()/2 - shipWidth/2 + playerRooms[i].getiRoom()*curTileWidth;
                float newY = playerRoomsShield.getY() + playerRoomsShield.getHeight()/2  - shipHeight/2 + playerRooms[i].getjRoom()*curTileWidth;
                playerRooms[i].setPosition(newX,newY);

            }
        }

        // resize and position system icons

        for(int i=0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {

            } else {

                // adjust rooms
                playerRoomsSystems[i].setWidth(curTileWidth);
                playerRoomsSystems[i].setHeight(curTileWidth);
                float newX = 0 + this.getWidth()/2 - shipWidth/2 + playerRoomsSystems[i].getiRoom()*curTileWidth;
                float newY = playerRoomsShield.getY() + playerRoomsShield.getHeight()/2  - shipHeight/2 + playerRoomsSystems[i].getjRoom()*curTileWidth;
                playerRoomsSystems[i].setPosition(newX,newY);

            }
        }

        // resize and position air overlays

        for(int i=0;i<playerRoomsAirOver.length;i++) {
            if (playerRoomsAirOver[i] == null) {

            } else {
                // adjust rooms
                playerRoomsAirOver[i].setWidth(curTileWidth*playerRoomsAirOver[i].getiNum());
                playerRoomsAirOver[i].setHeight(curTileWidth*playerRoomsAirOver[i].getjNum());
                float newX = 0 + this.getWidth()/2 - shipWidth/2 + playerRoomsAirOver[i].getiRoom()*curTileWidth;
                float newY = playerRoomsShield.getY() + playerRoomsShield.getHeight()/2  - shipHeight/2 + playerRoomsAirOver[i].getjRoom()*curTileWidth;
                playerRoomsAirOver[i].setPosition(newX,newY);
                Color color2 = playerRoomsAirOver[i].getColor();
                playerRoomsAirOver[i].setColor(color2.r,color2.g,color2.b,255);

            }
        }

        // resize and position doors

        for(int i=0;i<playerDoors.length;i++) {
            if (playerDoors[i] == null) {

            } else {
                if(playerDoors[i].getVertical()){
                    // vertical door
                    playerDoors[i].setHeight(curTileWidth*0.6f);
                    playerDoors[i].setWidth(playerDoors[i].getHeight()*playerDoors[i].getAspect());
                    float newX = 0 + this.getWidth()/2 - shipWidth/2 + playerDoors[i].getiDoor()*curTileWidth - playerDoors[i].getWidth()/2;
                    float newY = playerRoomsShield.getY() + playerRoomsShield.getHeight()/2  - shipHeight/2 + playerDoors[i].getjDoor()*curTileWidth + curTileWidth/2 - playerDoors[i].getHeight()/2;
                    playerDoors[i].setPosition(newX,newY);
                } else {
                    // horizontal door
                    playerDoors[i].setWidth(curTileWidth*0.6f);
                    playerDoors[i].setHeight(playerDoors[i].getWidth()/playerDoors[i].getAspect());
                    float newX = 0 + this.getWidth()/2 - shipWidth/2 + playerDoors[i].getiDoor()*curTileWidth + curTileWidth/2 - playerDoors[i].getWidth()/2;
                    float newY = playerRoomsShield.getY() + playerRoomsShield.getHeight()/2  - shipHeight/2 + playerDoors[i].getjDoor()*curTileWidth - playerDoors[i].getHeight()/2;
                    playerDoors[i].setPosition(newX,newY);
                }
            }
        }

        // Last step - sort order of images

        int layerCount = 1;

        playerRoomsShield.setZIndex(0);

/*
        backdrop.setZIndex(layerCount);
        layerCount++;*/
/*

        System.out.println("playerRoomsSystemsName length "+playerRoomsSystemsName.length);

        for(int i = 0;i<playerRoomsSystemsName.length;i++) {
            if (playerRoomsSystemsName[i] == null) {

            } else {
                playerRoomsSystemsName[i].setZIndex(layerCount);
                layerCount++;
            }
        }

        for(int i = 0;i<playerRoomsSystemsEdge.length;i++) {
            if (playerRoomsSystemsEdge[i] == null) {

            } else {
                playerRoomsSystemsEdge[i].setZIndex(layerCount);
                layerCount++;
            }
        }

        for(int i = 0;i<playerRoomsSystemsDamage.length;i++) {
            if (playerRoomsSystemsDamage[i] == null) {

            } else {
                playerRoomsSystemsDamage[i].setZIndex(layerCount);
                layerCount++;
            }
        }

        playerRoomsShield.setZIndex(layerCount);
        layerCount++;

        for(int i = 0;i<playerRooms.length;i++) {
            if (playerRooms[i] == null) {

            } else {
                playerRooms[i].setZIndex(layerCount);
                layerCount++;
            }
        }

        for(int i = 0;i<playerRoomsSystems.length;i++) {
            if (playerRoomsSystems[i] == null) {

            } else {
                playerRoomsSystems[i].setZIndex(layerCount);
                layerCount++;
            }
        }

        System.out.println("ship shown children "+this.getChildren().size+" vs "+layerCount);
*/


    }

    public void transparentBox(float backdropHeight,float aspect){

        //Color color = backdrop.getColor();
        //backdrop.setColor(color.r,color.g,color.b,0.3f);
        //setUpShip();
        //resizeShip();

    }

    public void toggleSystems(boolean bool,int systemCount){
        playerRoomsSystems[systemCount].toggleSystem(bool);
        if(!bool) {
            // turn off
            playerRoomsSystemsEdge[systemCount].setDrawable(new TextureRegionDrawable(game.getRoomsAt().findRegion("offButton")));
        } else {
            playerRoomsSystemsEdge[systemCount].setDrawable(new TextureRegionDrawable(game.getRoomsAt().findRegion("onButton")));
        }
    }
}
