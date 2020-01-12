package com.mygdx.game.Screens.battleDeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.battleDeck.HUDs.battleScreenActorOverlay;
import com.mygdx.game.Screens.battleDeck.HUDs.battleScreenbuttonOverlay;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.Sprites.doorSprite;
import com.mygdx.game.Screens.battleDeck.Sprites.healthBar;
import com.mygdx.game.Screens.battleDeck.Sprites.iconSprite;
import com.mygdx.game.Sprites.grndPlayerSprite;
import com.mygdx.game.Screens.battleDeck.Sprites.shipSecSprite;
import com.mygdx.game.Tools.Graph;
import com.mygdx.game.Tools.Vertex;
import com.mygdx.game.Tools.b2dWorldCreator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

//import javafx.scene.shape.Rectangle;

public class battleShipScreen implements Screen {

    private OrthogonalTiledMapRenderer renderer;
    private MyGdxGame game;
    OrthographicCamera gamecam;
    private Viewport gameport;
    private battleScreenbuttonOverlay batScreenHUD;
    public battleScreenActorOverlay batScreenActs;
    public boolean playerView;

    // Tiled Maps
    private TiledMap map;
    private b2dWorldCreator b2dWC;
    private b2dWCbattleDeckOld b2dWCbat;
    private TiledMapTileSet tileSet;
    private int[][] pLayout;
    private float transX;
    private float transY;
    private TiledMapTileLayer terrainLayer;
    private TiledMapTileLayer objectLayer;

    // Textures
    private TextureAtlas tilesAt;
    TextureAtlas.AtlasRegion atile[];
    private TextureAtlas roomsAt;
    TextureAtlas.AtlasRegion overTile[];
    private TextureAtlas tilesAtBuilds;
    TextureAtlas.AtlasRegion buildTile[];
    private TextureAtlas shipObjsAt;
    TextureAtlas.AtlasRegion doors[];
    private TextureAtlas iconsAt;
    private TextureAtlas barsAt;

    // Coordinates
    private int Tsize;

    // map arrays
    public int[][] mapLayout;

    // Box2D variables
    public World world;
    private Box2DDebugRenderer b2dr;
    public grndPlayerSprite player;
    private float gravity;

    // World variables
    public int wWidth;         // World width tiles
    public int wHeight;        // World height tiles
    public int sWidth;         // Screen width px
    public int sHeight;        // Screen height tiles

    public int roomCount;
    private int roomsX = 12;
    private int roomsY = 12;
    public roomTypes[][] shipLayout = new roomTypes[roomsX][roomsY];      // room layout for player ship
    public float[][][] shipStatus = new float[roomsX][roomsY][3];
    public roomTypes[][] enemyShipLayout = new roomTypes[roomsX][roomsY];      // room layout for player ship
    public enum roomTypes {cockpitV,cockpitH,BigSquare,teeH,teeV,corridorV,corridorH,verticalCorR,EngineRoom,Medbay,OxyTyms,ShieldRoom,surveillanceRoom,airlockH,airlockV};
    public enum systemUpStates {o2,camera,cross,engine,power,shields,steering};
    public enum systemDownStates {o2Red,cameraRed,crossRed,engineRed,powerRed,shieldsRed,steeringRed}
    public EnumMap<battleShipScreen.roomTypes,Integer[]> roomPosMaps = new EnumMap(battleShipScreen.roomTypes.class);
    public HashMap<String, List<Float>> roomMapping = new HashMap<>();      // sizes of room types
    public HashMap<String, List<Float>> roomPositions = new HashMap<>();    // positions of rooms on player ship
    public HashMap<String, List<Float>> enemyRoomPositions = new HashMap<>();    // positions of rooms on player ship
    public HashMap<String, Float[][]> doorMapping = new HashMap<>();      // sides for doors depending on type of room
    public HashMap<String, float[][][]> doorMappings = new HashMap<>();      // sides for doors depending on type of room
    public HashMap<String, float[][][]> doorMappings2 = new HashMap<>();      // sides for doors depending on type of room
    public HashMap<String, boolean[][][]> doorMappings3 = new HashMap<>();      // sides for doors depending on type of room
    public int[][] roomShipMap = new int[roomsX][roomsY];

    public shipSecSprite[] playerRooms;
    public shipSecSprite[] playerRoomsAirOver;
    public shipSecSprite[] fireTiles;
    public shipSecSprite[] enemyRooms;
    public doorSprite[] playerDoors;
    private float[][] pDoors;
    public doorSprite[] enemyDoors;
    public iconSprite[] playerIcons;
    public iconSprite[] enemyIcons;
    public healthBar[] playerHealth;
    //public Rectangle playerRect;
    public boolean tempBool;
    public boolean testBool;

    public boolean venting;

    private Texture background;

    double currentTime = System.nanoTime();
    final int TARGET_FPS = 60;
    final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
    boolean gameRunning = true;
    long lastFpsTime;
    long fps;
    double t = 0.0;
    double dt = 1/60;

    double accumulator = 0.0;
    long lastlooptime;

    double previous;
    double lag = 0;
    double ms_per_update = 16;

    // graph variables
    Graph roomsGraph;
    Graph shipGraph;
    public Vertex[] vertices;

//    private ShapeRenderer shapeRenderer;

    public battleShipScreen(MyGdxGame game){

        background = new Texture(Gdx.files.internal("backgrounds/2gals.png"));
        testBool = false;
        tempBool = false;
        Tsize = 50;
        this.game = game;
        pLayout = new int[120][120];
        sWidth = 1280;                          // Screen width px
        wWidth = Math.round(sWidth/Tsize);                 // World width in tiles
        sHeight = 720;                         // Screen height px
        wHeight = Math.round(sHeight/Tsize);               // World height in tiles
        gravity = 10f;
        int i;
        float screenRatio = 1;//2 / 3;
        roomCount = 0;
        venting = false;

        // camera to follow sprite
        gamecam = new OrthographicCamera();
        System.out.println("gamecam 1 "+gamecam.position.x+" and "+gamecam.position.y);

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH*screenRatio / MyGdxGame.PPM, MyGdxGame.V_HEIGHT*screenRatio / MyGdxGame.PPM, gamecam) {};

        playerView = true;
        map = new TiledMap();//TmxMapLoader().load("practiceDynTiles/pDTMap3.tmx");
        tilesAt = new TextureAtlas("PNGsPacked/biomespack50px.atlas");
        tilesAtBuilds = new TextureAtlas("PNGsPacked/buildTiles200px.atlas");
        roomsAt = new TextureAtlas("batScreen/ShipParts/Battle_Top_Down - Small Grid/shipParts.atlas");
        shipObjsAt = new TextureAtlas("batScreen/objects/shipObjects.atlas");
        iconsAt = new TextureAtlas("batScreen/status_icons/iconsUp.atlas");
        barsAt = new TextureAtlas("batScreen/status_bars/bars.atlas");

        //TextureAtlas.AtlasRegion atile = tilesAt.findRegion("pDTTile100Green");
        atile = new TextureAtlas.AtlasRegion[15];
        String colours[] = {"Blue", "Brown", "Green", "Grey", "LILACMAYBE", "Orange", "Pink", "Purple", "Red", "White", "Yellow", "Arctic1", "Desert1", "Forest1", "Volcano1"};
        for (i = 0; i < atile.length; i++) {
            atile[i] = tilesAt.findRegion("pDTTile50" + colours[i]);
        }

        buildTile = new TextureAtlas.AtlasRegion[1];
        String buildings[] = {"PopUp2"};
        for (i = 0; i < buildTile.length; i++) {
            buildTile[i] = tilesAtBuilds.findRegion(buildings[i]);
        }

        overTile = new TextureAtlas.AtlasRegion[roomTypes.values().length];
        //String overs[] = {"CockDraft1","BigSquare","horizontalCorB","horizontalCorT","normalCor","verticalCorL","verticalCorR","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm"};
        //for (i = 0; i < overTile.length; i++) {
        for (roomTypes dir : roomTypes.values()){
            overTile[i] = roomsAt.findRegion(dir.name());
        }

        doors = new TextureAtlas.AtlasRegion[2];
        doors[0] = shipObjsAt.findRegion("doorH");
        doors[1] = shipObjsAt.findRegion("doorV");

        world = new World(new Vector2(0, 0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

        playerIcons=new iconSprite[7];
        playerHealth=new healthBar[4];

        createSystIcons();
        createBars();
        roomTypesSet();
        genShip();
        //genShipCOPY();

        // initialise gamecam centrally
        //gamecam.position.set(0*100*MyGdxGame.PPM/MyGdxGame.PPM,0*100*MyGdxGame.PPM/MyGdxGame.PPM, 0);

        System.out.println("posiiton is "+gamecam.position.x+" and "+gamecam.position.y);

        //player = new grndPlayerSprite(world,this,resetX,resetY);
        //new b2dWCbattleDeckOld(world,map,this,null,null,0,0,sWidth,sHeight);

        System.out.println("Finished initialisation");

        gamecam.position.x = gameport.getWorldWidth()/2;
        gamecam.position.y = gameport.getWorldHeight()/2;

        lastlooptime = System.nanoTime();
        previous=System.nanoTime();

        demoGraph();
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

            if(dir == roomTypes.cockpitV){
                tileDoors2[0][0][1] = false;
                tileDoors2[0][0][3] = false;
            }

            doorMappings3.put(dir.name(),tileDoors2);
            tileDoors2 = new boolean[2][2][4];

        }
    }

    public void genShip(){
        // Method to generate layout of enemy and player ships - order as below
        // "CockDraft1","BigSquare","horizontalCor","normalCor","VerticalCor","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm"

        int i;
        int j;

        // START DEFINING PLAYER SHIP by room type

        shipLayout[6][2]=roomTypes.cockpitV;
        shipLayout[4][2]=roomTypes.BigSquare;
        shipLayout[3][6]=roomTypes.airlockH;
        shipLayout[3][0]=roomTypes.ShieldRoom;//vertical corridor
        shipLayout[3][4]=roomTypes.Medbay;//vertical corridor
        shipLayout[2][2]=roomTypes.EngineRoom;//horizontal corridor
        shipLayout[0][1]=roomTypes.surveillanceRoom;//horizontal corridor
        shipLayout[0][3]=roomTypes.BigSquare;//horizontal corridor

        shipGraph = new Graph();

        // initialise ship status by room
        for(i=0;i<shipStatus.length;i++){
            for(j=0;j<shipStatus[0].length;j++){
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
        sWidth=(int) gameport.getWorldWidth();
        sHeight=(int) gameport.getWorldHeight();
        float margin = sHeight/10;
        float TLx = margin; // x position of BL tile
        float TLy = margin; // y position of BL tile
        float sideSqr = sHeight - margin*2;    // the side is 80% of screen height
        float roomWidth = 100;
        float tileWidth = 50;
        float doorHWidth = doors[0].getRegionWidth();
        float doorHHeight = doors[0].getRegionHeight();
        float doorVWidth = doors[1].getRegionWidth();
        float doorVHeight = doors[1].getRegionHeight();
        float sideGrphc = roomsX*50;
        // ratio of screen to graphic is:
        float sGrphcRatio = sideSqr/sideGrphc;
        playerRooms = new shipSecSprite[numROoms];
        playerRoomsAirOver = new shipSecSprite[numROoms];
        playerDoors = new doorSprite[numROoms*8];       // maximum of 8 doors per room
        fireTiles = new shipSecSprite[numROoms*4];
        pDoors = new float[9][numROoms*8];
        // need intermediate list to prevent altering enum
        List<Float> tempSize3 = new ArrayList<Float>();
        List<Float> tempSize4 = new ArrayList<Float>();
        int doorCount = 0;
        float doorWidth=0;
        float doorHeight=0;
        float doorX=0;
        float doorY=0;
        int doorCountings=0;

        // Give positions and sizes of rooms
        for (i=0;i<shipLayout.length;i++) {
            for (j = 0; j < shipLayout[0].length; j++) {
                if (shipLayout[i][j] == null) {
                    System.out.println("EMPTY ROOM PLAYER");
                } else {
                    System.out.println(shipLayout[i][j].name()+" is "+roomsAt.findRegion(shipLayout[i][j].name()).getRegionWidth()+" wide by "+roomsAt.findRegion(shipLayout[i][j].name()).getRegionHeight()+" high");
                    float curRoomWidth = roomsAt.findRegion(shipLayout[i][j].name()).getRegionWidth();
                    float curRoomHeight = roomsAt.findRegion(shipLayout[i][j].name()).getRegionHeight();
                    for(int iTile=0;iTile<(curRoomWidth/50);iTile++){
                        for(int jTile=0;jTile<(curRoomHeight/50);jTile++){
                            roomShipMap[i+iTile][j+jTile]=numROomsCount;
                        }
                    }
                    //tempSize4 = roomMapping.get(shipLayout[i][j].name());
                    tempSize3.add(curRoomWidth);//tempSize4.get(0));
                    tempSize3.add(curRoomHeight);//tempSize4.get(1));
                    float roomX = TLx+tileWidth*i*sGrphcRatio;
                    float roomY = TLy+tileWidth*j*sGrphcRatio;
                    tempSize3.add(roomX);           // x coordinate BL
                    tempSize3.add(roomY);           // y coordinate BL
                    // determine number of tiles in room
                    int xRoom = (int) (curRoomWidth/50);    // width of room divided by width of room
                    int yRoom = (int) (curRoomHeight/50);    // height of room divied by height of room
                    // set core room date first
                    vertices[vertexCount] = new Vertex(this,shipLayout[i][j].name()+numROomsCount);
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
                            System.out.println("about to check other room is right or equal of current room left border");
                            System.out.println(vertices[i2].BL[0] + vertices[i2].tileNo[0] + " vs " + vertices[currentVertex].BL[0]);
                            if (vertices[i2].BL[0] + vertices[i2].tileNo[0] >= vertices[currentVertex].BL[0]) {
                                // right border of room being checked is at least same as left border of current room (or inside)
                                // ie assuming checked room left of current room
                                System.out.println("about to check current room is left or equal of other room right border");
                                System.out.println(vertices[currentVertex].BL[0] + vertices[currentVertex].tileNo[0] + " vs " + vertices[i2].BL[0]);
                                if (vertices[currentVertex].BL[0] + vertices[currentVertex].tileNo[0] >= vertices[i2].BL[0]) {
                                    System.out.println("about to check other room is above or equal of current room bottom border");
                                    System.out.println(vertices[i2].BL[1] + vertices[i2].tileNo[1] + " vs " + vertices[currentVertex].BL[1]);
                                    if (vertices[i2].BL[1] + vertices[i2].tileNo[1] >= vertices[currentVertex].BL[1]) {
                                        System.out.println("about to check current room is below or equal to other room top border");
                                        System.out.println(vertices[currentVertex].BL[1] + vertices[currentVertex].tileNo[1] + " vs " + vertices[i2].BL[1]);
                                        if (vertices[currentVertex].BL[1] + vertices[currentVertex].tileNo[1] >= vertices[i2].BL[1]) {
                                            System.out.println("should be adding room now");
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
                            vertices[vertexCount] = new Vertex(this,shipLayout[i][j].name()+numROomsCount+"Tile"+i2+j2);
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
                            fireTiles[tileCount] = new shipSecSprite(game,null,"flame",fireArray,sGrphcRatio);
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
                    shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-2]);
                    shipGraph.addEdge(vertices[vertexCount-1],vertices[vertexCount-3]);
                    shipGraph.addEdge(vertices[vertexCount-4],vertices[vertexCount-2]);
                    shipGraph.addEdge(vertices[vertexCount-4],vertices[vertexCount-3]);
                    // create room sprite
                    playerRooms[numROomsCount] = new shipSecSprite(game,null,shipLayout[i][j].name(),tempSize3,sGrphcRatio);
                    playerRoomsAirOver[numROomsCount] = new shipSecSprite(game,null,"BigSquare30%atm",tempSize3,sGrphcRatio);
                    playerRoomsAirOver[numROomsCount].setAlpha(0f);
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
                            vertices[vertexCount] = new Vertex(this, "Door" + doorCount);
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
                            vertices[vertexCount] = new Vertex(this, "Door" + doorCount);
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
                            vertices[vertexCount] = new Vertex(this, "Door" + doorCount);
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
                            vertices[vertexCount] = new Vertex(this, "Door" + doorCount);
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

        for(i=0;i<roomShipMap.length;i++){
            for(j=0;j<roomShipMap[0].length;j++){
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
                                if (door1[tileLeft.BL[0] - vertices[tileLeft.coreRoomCount].BL[0]][(tileLeft.BL[1] - vertices[tileLeft.coreRoomCount].BL[1])][3] == true && door2[0][(tileRight.BL[1] - vertices[tileRight.coreRoomCount].BL[1])][1] == true) {
                                    createDoor = true;
                                }
                                if (createDoor) {
                                    // got tiles to connect to door now
                                    // vertical door required
                                    vertices[vertexCount] = new Vertex(this, "Door" + doorCount);
                                    vertices[vertexCount].BL = new int[]{i, j};
                                    vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                    vertices[vertexCount].vertexCount = vertexCount;
                                    vertices[vertexCount].doorCount = doorCount;
                                    doorWidth = doorVWidth * sGrphcRatio;
                                    doorHeight = doorVHeight * sGrphcRatio;

                                    // create edge between door and tiles
                                    shipGraph.addEdge(vertices[tileRight.vertexCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileLeft.vertexCount], vertices[vertexCount]);

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
                                    pDoors[8][doorCount] = j;
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

                                if(door1[(tileBottom.BL[0]-vertices[tileBottom.coreRoomCount].BL[0])][(tileBottom.BL[1]-vertices[tileBottom.coreRoomCount].BL[1])][2] == true && door2[(tileTop.BL[0]-vertices[tileTop.coreRoomCount].BL[0])][(tileTop.BL[1]-vertices[tileTop.coreRoomCount].BL[1])][0] == true){
                                    createDoor = true;
                                }
                                if(createDoor) {
                                    // got tiles to connect to door now
                                    // vertical door required
                                    vertices[vertexCount] = new Vertex(this, "Door" + doorCount);
                                    System.out.println("vertexCount at door creation vertical " + vertexCount);
                                    vertices[vertexCount].BL = new int[]{i, j};
                                    vertices[vertexCount].thisRoom = Vertex.vertexRoom.door;
                                    vertices[vertexCount].vertexCount = vertexCount;
                                    vertices[vertexCount].horizDoor = true;
                                    doorWidth = doorHWidth * sGrphcRatio;
                                    doorHeight = doorHHeight * sGrphcRatio;
                                    // create edge between door and tiles
                                    shipGraph.addEdge(vertices[tileTop.vertexCount], vertices[vertexCount]);
                                    shipGraph.addEdge(vertices[tileBottom.vertexCount], vertices[vertexCount]);
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
                                    vertexCount++;
                                    doorCount++;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("finished initialising player ship");

        // create icons for system status in method
        batScreenHUD = new battleScreenbuttonOverlay(world,game.batch,this,margin,gameport.getWorldWidth(),gameport.getWorldHeight(),pDoors);

        // ---------------------------------------------------------------------------------------
        // THEN DEFINE ENEMY SHIP

        /*for(i=0;i<enemyShipLayout.length;i++){
            for(j=0;j<enemyShipLayout[0].length;j++){
                enemyShipLayout[i][j] = roomTypes.normalCor;
            }
        }*/

        //enemyShipLayout[1][1]=roomTypes.normalCor;
        enemyShipLayout[0][2]=roomTypes.ShieldRoom;
        //enemyShipLayout[3][3]=roomTypes.normalCor;
        //enemyShipLayout[4][5]=roomTypes.normalCor;
        enemyShipLayout[5][2]=roomTypes.cockpitV;      // Cockpit
        enemyShipLayout[4][2]=roomTypes.BigSquare;       // Big square corridor
        enemyShipLayout[4][3]=roomTypes.OxyTyms;         // life support
        enemyShipLayout[3][1]=roomTypes.Medbay;          // Medbay
        enemyShipLayout[3][2]=roomTypes.corridorH;   // horizontal corridor
        enemyShipLayout[2][2]=roomTypes.EngineRoom;        // Engine room
        enemyShipLayout[2][3]=roomTypes.ShieldRoom;      // shields
        enemyShipLayout[2][1]=roomTypes.surveillanceRoom; // surveillance

        numROoms = 36;       // max number - 6x6
        numROomsCount = 0;  // to track and make sure got all as sprites created
        // Need to convert coordinates from graphic size to fit screen
        // work on basis of ship taking up 80% of screen height as phone forced horizontal AR
        sWidth=(int) gameport.getWorldWidth();
        sHeight=(int) gameport.getWorldHeight();
        TLy = margin; // y position of TL tile
        TLx = sWidth-margin-(sideGrphc)*sGrphcRatio; // x position of TL tile
        // ratio of screen to graphic is:
        sGrphcRatio = sideSqr/sideGrphc;
        System.out.println("sidesqr "+sideSqr+" and ratio "+sGrphcRatio);
        System.out.println("BLs "+TLx+" and "+TLy);

        enemyRooms = new shipSecSprite[numROoms];
        // need intermediate list to prevent altering enum
        tempSize3 = new ArrayList<Float>();
        tempSize4 = new ArrayList<Float>();

        // Give positions and sizes of rooms
        for (i=0;i<enemyShipLayout.length;i++) {
            for (j = 0; j < enemyShipLayout[0].length; j++) {
                if (enemyShipLayout[i][j] == null) {
                    System.out.println("EMPTY ROOM ENEMY");
                } else {
                    float curRoomWidth = roomsAt.findRegion(enemyShipLayout[i][j].name()).getRegionWidth();
                    float curRoomHeight = roomsAt.findRegion(enemyShipLayout[i][j].name()).getRegionHeight();
                    //System.out.println("tempsize pre get " + tempSize3);
                    tempSize4 = roomMapping.get(enemyShipLayout[i][j].name());
                    tempSize3.add(curRoomWidth);
                    tempSize3.add(curRoomHeight);
                    //System.out.println("full map "+roomMapping);
                    //System.out.println("tempsize post get " + tempSize3);
                    //tempSize2[0] = tempSize[0];     // x length
                    //tempSize2[1] = tempSize[1];     // y length
                    tempSize3.add(TLx+100*i*sGrphcRatio);           // x coordinate BL
                    tempSize3.add(TLy+100*j*sGrphcRatio);           // y coordinate BL
                    //System.out.println("specific room coords " + tempSize3);
                    //System.out.println("Roomtype name "+shipLayout[i][j].name());
                    System.out.println("Room "+enemyShipLayout[i][j].name());
                    enemyRooms[numROomsCount] = new shipSecSprite(game,null,enemyShipLayout[i][j].name(),tempSize3,sGrphcRatio);
                    numROomsCount++;
                    //roomPositions.put(shipLayout[i][j].name(), tempSize3);
                    tempSize3 = new ArrayList<>();
                    tempSize4 = new ArrayList<>();
                    //System.out.println("post delete tempsize3 " + tempSize3);
                    //System.out.println("");
                }
            }
        }

        System.out.println("Finished creating room sprites. ROomcount comparison "+numROoms+" vs "+numROomsCount);

        Gdx.graphics.setWindowedMode(640,360);

        // start creating objects
        // perhaps dont need the enum map above


    }


    public void demoGraph(){

        System.out.println("demoGraph son!");
        for(int i = 0; i < vertices.length; i++) {
            System.out.println(vertices[i]);
            if (vertices[i] == null) {
                // do nada
            } else {
                for (int j = 0; j < vertices[i].getNeighborCount(); j++) {
                    System.out.println(vertices[i].getNeighbor(j));
                }
                System.out.println();
            }
        }

/*

        // test to create doors for first two rooms
        for(int i=1;i<vertices.length;i++){
            if(vertices[i].thisRoom==Vertex.vertexRoom.room){
                // if the vertex is a room, we need to check for a connecting room
                // worth noting that the following 2/4 vertices following are tiles

            }
        }
*/

/*
        Graph graph = new Graph();
        Vertex vertex1 = new Vertex(this,"room1");
        vertex1.thisRoom = Vertex.vertexRoom.room;
        graph.addVertex(vertex1,true);
        Vertex vertex2 = new Vertex(this,"tile1");
        vertex2.thisRoom = Vertex.vertexRoom.tile;
        int[] tileNo = new int[2];
        tileNo[0]=1;
        tileNo[1]=1;
        vertex2.tileNo=tileNo;
        graph.addEdge(vertex1,vertex2);
        Vertex vertex3 = new Vertex(this,"door1");
        vertex2.thisRoom = Vertex.vertexRoom.door;
        graph.addEdge(vertex2,vertex3);

        System.out.println("here is the graph hopefuly");
        System.out.println(graph);*/



/*

        //display the initial setup- all vertices adjacent to each other
        for(int i = 0; i < vertices.length; i++){
            System.out.println(vertices[i]);
            for(int j = 0; j < vertices[i].getNeighborCount(); j++){
                System.out.println(vertices[i].getNeighbor(j));
            }
            System.out.println();
        }

*/


    }

    public void createSystIcons(){
        // creates icon sprites and places on screen for either player or enemy
        // 7 icons - 14 including disabled status

        int i=0;
        List<Float> iconSize = new ArrayList<Float>();
        sWidth=(int) gameport.getWorldWidth();
        sHeight=(int) gameport.getWorldHeight();
        float margin = sHeight/10;
        float TLx = margin; // x position of BL tile
        float TLy = margin; // y position of BL tile
        float sideSqr = sHeight - margin*2;    // the side is 80% of screen height
        float iconSide = 50;
        float interIconMargin = 10;
        float nIcons = 7;
        float sideGrphc = 50*nIcons+interIconMargin*(nIcons-1);
        float sGrphcRatio = sideSqr/sideGrphc;
        float iconX;
        float iconY;


        for(systemUpStates dir : systemUpStates.values()){
            // cycle through icons and add to list
            // first add dimensions
            iconSize.add(50f*sGrphcRatio);      // x length
            iconSize.add(50f*sGrphcRatio);      // y length
            iconX = TLx/2 - iconSide/2*sGrphcRatio;
            iconY = TLy+iconSide*i*sGrphcRatio;
            iconSize.add(iconX);
            iconSize.add(iconY);
            System.out.println("icon "+dir.name());
            System.out.println("iconX "+iconX+" iconY"+iconY);
            playerIcons[i] = new iconSprite(world,this,iconSize,dir.name());
            i++;
            iconSize = new ArrayList<Float>();
        }
    }

    public void createBars(){
        // create health and shields bars for each player
        sWidth=(int) gameport.getWorldWidth();
        sHeight=(int) gameport.getWorldHeight();
        float margin = sHeight/10;
        List<Float> tempSize = new ArrayList<Float>();
        float tempX;
        float tempY;
        float TLx = margin/2;
        float TLy = sHeight - margin/2;
        float conHeight = margin*1.1f;
        float conWidth = 5*margin*1.1f;
        float barHeight = margin*1f;
        float barWidth = 5*margin*1f;
        int i;

        for(i=0;i<2;i+=2) {
            // container
            tempSize.add(conWidth);             // width
            tempSize.add(conHeight);            // height
            tempX = TLx / 2;                    // x position
            tempY = TLy - i*(margin+conHeight); // y position
            tempSize.add(tempX);
            tempSize.add(tempY);
            playerHealth[i] = new healthBar(world, this, tempSize, "barContainer");


            // bar
            tempSize = new ArrayList<Float>();
            tempSize.add(barWidth);             // width
            tempSize.add(barHeight);            // height
            tempX = tempY+(conWidth-barWidth)/2;              // x position
            tempY = tempY-(conHeight-barHeight)/2;              // y position
            tempSize.add(tempX);
            tempSize.add(tempY);
            playerHealth[i+1] = new healthBar(world, this, tempSize, "shieldBar");
        }



    }

    public TextureAtlas getAtlas(){
        return roomsAt;
    }

    public TextureAtlas getDoorAt(){
        return shipObjsAt;
    }

    public TextureAtlas getIconAt(){
        return iconsAt;
    }

    public TextureAtlas getBarsAt(){
        return barsAt;
    }


    @Override
    public void show() {

    }

    public void update(double dt){
        //world.step(1/60f,6,2);

        // example changing of sprite texture
        //overTile[0].setTexture();

       /* if(venting){

            for(int i=0;i<(vertices.length);i++) {
                if(vertices[i]==null){
                } else {
                    if(vertices[i].thisRoom==null){
                    } else {
                        if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                            if (vertices[i].vacuuming) {
                                // room is under vacuum or linked to a room under vacuum
                                System.out.println(vertices[i].getLabel() + " is venting. At " + vertices[i].o2 + "%");
                                if (vertices[i].o2 > 0) {
                                    vertices[i].o2 -= dt * 0.001;
                                }
                            } else {
                                if (vertices[i].o2 < 100) {
                                    vertices[i].o2 += dt * 0.0001;
                                }
                            }
                            if (vertices[i].o2 < 30) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.56f);
                            } else if (vertices[i].o2 < 40) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.48f);
                            } else if (vertices[i].o2 < 50) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.40f);
                            } else if (vertices[i].o2 < 60) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.32f);
                            } else if (vertices[i].o2 < 70) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.24f);
                            } else if (vertices[i].o2 < 80) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.16f);
                            } else if (vertices[i].o2 < 90) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.08f);
                            }
                        }
                    }
                }
            }

        } else {*/
            for(int i=0;i<(vertices.length);i++) {
                // search all vertices for rooms / tiles / doors
                if(vertices[i]==null){
                } else {
                    if(vertices[i].thisRoom==null){
                    } else {
                        if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                            if (vertices[i].vacuuming) {
                                // room is under vacuum or linked to a room under vacuum
                                System.out.println(vertices[i].getLabel() + " is venting. At " + vertices[i].o2 + "%");
                                if (vertices[i].o2 > 0) {
                                    vertices[i].o2 -= dt * 0.01;
                                }
                            } else {
                                if (vertices[i].o2 < 100) {
                                    vertices[i].o2 += dt * 0.01;
                                }
                            }
                            if (vertices[i].o2 < 30) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.56f);
                            } else if (vertices[i].o2 < 40) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.48f);
                            } else if (vertices[i].o2 < 50) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.40f);
                            } else if (vertices[i].o2 < 60) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.32f);
                            } else if (vertices[i].o2 < 70) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.24f);
                            } else if (vertices[i].o2 < 80) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.16f);
                            } else if (vertices[i].o2 < 90) {
                                playerRoomsAirOver[vertices[i].numROomsCount].setAlpha(0.08f);
                            }
                        } /*else if(vertices[i].thisRoom == Vertex.vertexRoom.tile){
                            if(vertices[i].onFire){
                                // tile is on fire
                                if(vertices[i].fireHealth>0){
                                    // tile fire is still burning
                                    vertices[i].fireHealth--;
                                    vertices[vertices[i].coreRoomCount].roomDamage--;
                                } else {
                                    // tile was on fire and now gone out
                                    vertices[i].onFire = false;
                                    vertices[i].fireDamaged = true;
                                }
                            }
                        }*/
                    }
                }
            }
        //}


        handleInput(dt);
        gamecam.update();
        renderer.setView(gamecam);

    }

    public void handleInput(double dt) {

        // If mouse pressed on world, move map left
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            System.out.println("UPPING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            gamecam.position.y += 1;// * Gdx.graphics.getDeltaTime();
            //player.b2body.applyLinearImpulse(new Vector2(0, 0.5f), player.b2body.getWorldCenter(), true);
            //TLj++;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            System.out.println("DOWNING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            gamecam.position.y -= 1;// * Gdx.graphics.getDeltaTime();
            //TLj--;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            System.out.println("RIGHTING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            gamecam.position.x -= 1;// * Gdx.graphics.getDeltaTime();
            //gamecam.position.x = player.b2body.getPosition().x;
            //gamecam.position.y = player.b2body.getPosition().y;
            //player.b2body.applyLinearImpulse(new Vector2(10f, 0), player.b2body.getWorldCenter(), true);
            //TLi--;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            System.out.println("LEFTING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            gamecam.position.x += 1;// * Gdx.graphics.getDeltaTime();
            //player.b2body.applyLinearImpulse(new Vector2(-10f,0),player.b2body.getWorldCenter(),true);
            //TLi++;
            //updateMap();
        }
        if(Gdx.input.isTouched()) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            //System.out.println("X "+mousePos.x+" Y "+mousePos.y+" Z "+mousePos.z);
            //gamecam.unproject(mousePos);
            //System.out.println(" and in stereo! X " + mousePos.x + " Y " + mousePos.y + " Z " + mousePos.z);
        }
    }

    @Override
    public void render(float delta) {

        /*while(gameRunning) {

            double newTime = System.nanoTime();
            double frameTime = newTime - currentTime;
            if (frameTime > dt * 25) {
                frameTime = dt * 25;

            }
            currentTime = newTime;

            accumulator += frameTime;

            while (accumulator >= dt) {
                //previous = current;
                t += dt;
                accumulator -= dt;
            }

            double interpolation = accumulator / dt;

            // update frame counter
            //lastFpsTime += updateLength;
            fps++;

            // update FPS counter if second has passed since last recorded
            if (lastFpsTime >= 1000000000) {
                System.out.println("(FPS: " + fps + ")");
                lastFpsTime = 0;
                fps = 0;
            }
*/

        if (gameRunning){
            double current = System.nanoTime(); // ns
            double elapsed = (current - previous)/1000000; // ms
            previous = current;
            lag += elapsed; // ms
            while(lag>=ms_per_update) {
                update(elapsed);
                lag -= ms_per_update;
            }

            Gdx.gl.glClearColor((float) 60 / 255, (float) 181 / 255, (float) 0 / 255, (float) 0);    // clears screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                       // clears screen

            // render game map
            renderer.render();
            game.batch.setProjectionMatrix(gamecam.combined);

            int i = 0;
            int nCount = playerRooms.length;

            game.batch.begin();
            //game.batch.draw(background, 0, 0, gameport.getWorldWidth(), gameport.getWorldHeight());
            if (playerView) {
                // show player ship detail
                while (i < nCount) {
                    //System.out.println("i "+i);
                    if (playerRooms[i] == null) {
                        // nothign to draw
                    } else {
                        playerRooms[i].draw(game.batch);
                    }
                    if (playerRoomsAirOver[i] == null) {
                        // nothign to draw
                    } else {
                        playerRoomsAirOver[i].draw(game.batch);
                    }
                    if (fireTiles[i] == null) {
                        // nothign to draw
                    } else {
                        fireTiles[i].draw(game.batch);
                    }
                    i++;
                }
            } else {
                // show enemy ship detail
                while (i < nCount) {
                    //System.out.println("i "+i);
                    if (enemyRooms[i] == null) {
                        // nothing to draw
                    } else {
                        enemyRooms[i].draw(game.batch);
                    }
                    i++;
                }
            }
            game.batch.end();
            game.batch.setProjectionMatrix(batScreenHUD.stage.getCamera().combined);
            batScreenHUD.stage.draw();
            if (playerView) {
                batScreenHUD.stage2.draw();
            } else {

            }
            //batScreenActs.stage.act(Gdx.graphics.getDeltaTime());
            //batScreenActs.stage.draw();

            // ensures frame takes 10ms
           /* try{
                Thread.sleep( (lastlooptime-System.nanoTime()+OPTIMAL_TIME)/1000000);
            } catch(InterruptedException e){
                System.out.println("Interrupted exception!");
            }*/
        }
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resizing "+height+" by "+width);
        gameport.update(width,height);
        gamecam.update();
        batScreenHUD.viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void genShipCOPY(){
        // Method to generate layout of enemy and player ships - order as below
        // "CockDraft1","BigSquare","horizontalCor","normalCor","VerticalCor","EnginRum","Medbay","OxyTyms","ShieldRoom","SurveillanceRUm"

        int i;
        int j;

        // START DEFINING PLAYER SHIP by room type
/*
        shipLayout[1][4]=roomTypes.horizontalTeeB;
        shipLayout[1][3]=roomTypes.EngineRoom;
        shipLayout[1][2]=roomTypes.horizontalTeeT;

        shipLayout[2][4]=roomTypes.horizontalCorB;
        shipLayout[2][3]=roomTypes.airlockR;
        shipLayout[2][2]=roomTypes.horizontalCorT;

        shipLayout[3][5]=roomTypes.airlockB;
        shipLayout[3][4]=roomTypes.BigSquare;
        shipLayout[3][3]=roomTypes.OxyTyms;
        shipLayout[3][2]=roomTypes.horizontalTeeT;

        shipLayout[4][4]=roomTypes.horizontalCorB;
        shipLayout[4][2]=roomTypes.BigSquare;
        shipLayout[4][1]=roomTypes.airlockT;

        shipLayout[5][5]=roomTypes.Medbay;
        shipLayout[5][4]=roomTypes.BigSquare;
        shipLayout[5][3]=roomTypes.verticalCorL;
        shipLayout[5][2]=roomTypes.horizontalTeeT;

        shipLayout[6][4]=roomTypes.cockpitL;*/

        shipLayout[6][3]=roomTypes.cockpitH;
        shipLayout[5][3]=roomTypes.BigSquare;
        shipLayout[5][4]=roomTypes.teeH;
        shipLayout[5][2]=roomTypes.teeH;
        shipLayout[4][4]=roomTypes.Medbay;
        shipLayout[4][2]=roomTypes.EngineRoom;
        shipLayout[3][4]=roomTypes.BigSquare;
        shipLayout[3][2]=roomTypes.BigSquare;
        shipLayout[3][5]=roomTypes.airlockH;
        shipLayout[3][1]=roomTypes.airlockH;
        shipLayout[2][4]=roomTypes.teeH;
        shipLayout[2][2]=roomTypes.teeH;
        shipLayout[2][3]=roomTypes.ShieldRoom;
        shipLayout[1][2]=roomTypes.OxyTyms;
        shipLayout[1][4]=roomTypes.surveillanceRoom;
        shipLayout[0][4]=roomTypes.teeH;
        shipLayout[0][2]=roomTypes.teeH;
        shipLayout[0][3]=roomTypes.BigSquare;

        // initialise ship status by room
        for(i=0;i<shipStatus.length;i++){
            for(j=0;j<shipStatus[0].length;j++){
                // venting = 1
                shipStatus[i][j][0] = 0;
                // atm content of room
                shipStatus[i][j][1] = 100;
            }
        }

        int numROoms = 36;       // max number - 6x6
        int numROomsCount = 0;  // to track and make sure got all as sprites created

        // Need to convert coordinates from graphic size to fit screen
        // work on basis of ship taking up 80% of screen height as phone forced horizontal AR
        sWidth=(int) gameport.getWorldWidth();
        sHeight=(int) gameport.getWorldHeight();
        float margin = sHeight/10;
        float TLx = margin; // x position of BL tile
        float TLy = margin; // y position of BL tile
        float sideSqr = sHeight - margin*2;    // the side is 80% of screen height
        float roomWidth = 100;
        float tileWidth = 50;
        float doorHWidth = doors[0].getRegionWidth();
        float doorHHeight = doors[0].getRegionHeight();
        float doorVWidth = doors[1].getRegionWidth();
        float doorVHeight = doors[1].getRegionHeight();
        float sideGrphc = 12*50;
        // ratio of screen to graphic is:
        float sGrphcRatio = sideSqr/sideGrphc;
        System.out.println("sidesqr "+sideSqr+" and ratio "+sGrphcRatio);
        System.out.println("BLs "+TLx+" and "+TLy);
        playerRooms = new shipSecSprite[numROoms];
        playerDoors = new doorSprite[numROoms*8];       // maximum of 8 doors per room
        pDoors = new float[9][numROoms*8];
        // need intermediate list to prevent altering enum
        List<Float> tempSize3 = new ArrayList<Float>();
        List<Float> tempSize4 = new ArrayList<Float>();
        int doorCount = 0;
        float doorWidth=0;
        float doorHeight=0;
        float doorX=0;
        float doorY=0;
        float iRoom1;
        float iRoom2;
        float jRoom1;
        float jRoom2;

        // Give positions and sizes of rooms
        for (i=0;i<shipLayout.length;i++) {
            for (j = 0; j < shipLayout[0].length; j++) {
                if (shipLayout[i][j] == null) {
                    System.out.println("EMPTY ROOM PLAYER");
                } else {
                    System.out.println("creating room " + shipLayout[i][j].name());
                    tempSize4 = roomMapping.get(shipLayout[i][j].name());
                    tempSize3.add(tempSize4.get(0));
                    tempSize3.add(tempSize4.get(1));
                    float roomX = TLx+roomWidth*i*sGrphcRatio;
                    float roomY = TLy+roomWidth*j*sGrphcRatio;
                    tempSize3.add(roomX);           // x coordinate BL
                    tempSize3.add(roomY);           // y coordinate BL
                    playerRooms[numROomsCount] = new shipSecSprite(game,null,shipLayout[i][j].name(),tempSize3,sGrphcRatio);
                    numROomsCount++;
                    String s = shipLayout[i][j].name();
                    System.out.println(s.substring(0, Math.min(s.length(), 7)));
                    if(s.substring(0, Math.min(s.length(), 7)).equals("airlock")){
                        // airlock
                        System.out.println("airlock found");
                        if(s.substring(s.length() - 1).equals("T")){
                            // top airlock - create door on bottom
                            doorWidth = doorHWidth * sGrphcRatio;
                            doorHeight = doorHHeight * sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio/2 - doorHWidth*sGrphcRatio/2;
                            doorY = roomY + roomWidth*sGrphcRatio/2 - doorHHeight*sGrphcRatio/2;
                            iRoom1=i;
                            jRoom1=j;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=1;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=jRoom1;
                            doorCount++;
                        }
                        if(s.substring(s.length() - 1).equals("B")){
                            // bottom airlock - create door on top
                            doorWidth = doorHWidth * sGrphcRatio;
                            doorHeight = doorHHeight * sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio/2 - doorHWidth*sGrphcRatio/2;
                            doorY = roomY + roomWidth*sGrphcRatio/2 - doorHHeight*sGrphcRatio/2;
                            iRoom1=i;
                            jRoom1=j;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=1;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=jRoom1;
                            doorCount++;
                        }
                        if(s.substring(s.length() - 1).equals("R")){
                            // right airlock - create door on left
                            doorWidth = doorVWidth*sGrphcRatio;
                            doorHeight=doorVHeight*sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio/2 - doorVWidth*sGrphcRatio/2;
                            doorY = roomY + roomWidth*sGrphcRatio/2 - doorVHeight*sGrphcRatio/2;
                            iRoom1=i;
                            jRoom1=j;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=1;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=jRoom1;
                            doorCount++;
                        }
                        if(s.substring(s.length() - 1).equals("L")){
                            // left airlock - create door on right
                            doorWidth = doorVWidth*sGrphcRatio;
                            doorHeight=doorVHeight*sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio/2 - doorVWidth*sGrphcRatio/2;
                            doorY = roomY + roomWidth*sGrphcRatio/2 - doorVHeight*sGrphcRatio/2;
                            iRoom1=i;
                            jRoom1=j;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=1;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=jRoom1;
                            doorCount++;
                        }
                    }

                    // then create doors - start by creating horizontal doors (doors that connect rooms vertically)
                    //List<Float> tempDoor = new ArrayList<Float>();
                    if(shipLayout[i][j+1]==null){
                        // no adjacent room - do nothing
                        System.out.println("no room below");
                    } else {
                        // adjacent room - create door(s)
                        roomTypes room1 = shipLayout[i][j];
                        roomTypes room2 = shipLayout[i][j+1];

                        if(doorMapping.get(room1.name())[0][3] == 1 && doorMapping.get(room2.name())[0][0] == 1){//doorPat1[0][3]==1 && doorPat2[0][0]==1){
                            // create left door
                            //tempDoor.add(doorHWidth*sGrphcRatio);
                            //tempDoor.add(doorHHeight*sGrphcRatio);
                            doorWidth = doorHWidth * sGrphcRatio;
                            doorHeight = doorHHeight * sGrphcRatio;
                            doorX = roomX + doorHWidth*sGrphcRatio/2;
                            //tempDoor.add(doorX);
                            doorY = roomY + roomWidth*sGrphcRatio - doorHHeight*sGrphcRatio/2;
                            iRoom1=i;
                            iRoom2=i;
                            jRoom1=j;
                            jRoom2=j+1;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=0;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=iRoom2;
                            pDoors[7][doorCount]=jRoom1;
                            pDoors[8][doorCount]=jRoom2;
                            doorCount++;
                        }
                        if(doorMapping.get(room1.name())[1][3] == 1 && doorMapping.get(room2.name())[1][0] == 1) {//doorPat1[1][3]==1 && doorPat2[1][0]==1){
                            // create right door
                            //tempDoor.add(doorHWidth * sGrphcRatio);
                            //tempDoor.add(doorHHeight * sGrphcRatio);
                            doorWidth = doorHWidth * sGrphcRatio;
                            doorHeight = doorHHeight * sGrphcRatio;
                            doorX = roomX + roomWidth * sGrphcRatio / 2 + doorHWidth * sGrphcRatio / 2;
                            //tempDoor.add(doorX);
                            doorY = roomY + roomWidth * sGrphcRatio - doorHHeight * sGrphcRatio / 2;
                            iRoom1=i;
                            iRoom2=i;
                            jRoom1=j;
                            jRoom2=j+1;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=0;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=iRoom2;
                            pDoors[7][doorCount]=jRoom1;
                            pDoors[8][doorCount]=jRoom2;
                            doorCount++;
                        }

                    }
                    // then create vertical doors (ones that connect rooms horizontally)
                    if(shipLayout[i+1][j]==null){
                        // no adjacent room - do nothing
                        System.out.println("no room right");
                    } else {
                        // adjacent room - create door(s)
                        System.out.println("room right");

                        // retrieve data on potential door sites
                        roomTypes room1 = shipLayout[i][j];
                        roomTypes room2 = shipLayout[i+1][j];

                        if(doorMapping.get(room1.name())[1][1] == 1 && doorMapping.get(room2.name())[0][1] == 1){//doorPat1[1][1]==1 && doorPat2[0][1]==1){
                            // create bottom  door
                            //tempDoor.add(doorVWidth*sGrphcRatio);
                            //tempDoor.add(doorVHeight*sGrphcRatio);
                            doorWidth = doorVWidth * sGrphcRatio;
                            doorHeight = doorVHeight * sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio - doorVWidth*sGrphcRatio/2;
                            //tempDoor.add(doorX);
                            doorY = roomY + doorVHeight*sGrphcRatio/2;
                            iRoom1=i+1;
                            iRoom2=i;
                            jRoom1=j;
                            jRoom2=j;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=0;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=iRoom2;
                            pDoors[7][doorCount]=jRoom1;
                            pDoors[8][doorCount]=jRoom2;
                            doorCount++;
                        }
                        if(doorMapping.get(room1.name())[1][2] == 1 && doorMapping.get(room2.name())[0][2] == 1){//doorPat1[1][2]==1 && doorPat2[0][2]==1){
                            // create top door
                            //tempDoor.add(doorVWidth*sGrphcRatio);
                            //tempDoor.add(doorVHeight*sGrphcRatio);
                            doorWidth = doorVWidth * sGrphcRatio;
                            doorHeight = doorVHeight * sGrphcRatio;
                            doorX = roomX + roomWidth*sGrphcRatio - doorVWidth*sGrphcRatio/2;
                            //tempDoor.add(doorX);
                            doorY = roomY + roomWidth*sGrphcRatio/2 + doorVHeight*sGrphcRatio/2;
                            iRoom1=i+1;
                            iRoom2=i;
                            jRoom1=j;
                            jRoom2=j;
                            pDoors[0][doorCount]=doorWidth;
                            pDoors[1][doorCount]=doorHeight;
                            pDoors[2][doorCount]=doorX;
                            pDoors[3][doorCount]=doorY;
                            pDoors[4][doorCount]=0;
                            pDoors[5][doorCount]=iRoom1;
                            pDoors[6][doorCount]=iRoom2;
                            pDoors[7][doorCount]=jRoom1;
                            pDoors[8][doorCount]=jRoom2;
                            doorCount++;
                        }
                    }

                    tempSize3 = new ArrayList<>();
                    tempSize4 = new ArrayList<>();
                }
            }
        }

        // create icons for system status in method
        batScreenHUD = new battleScreenbuttonOverlay(world,game.batch,this,margin,gameport.getWorldWidth(),gameport.getWorldHeight(),pDoors);

        // ---------------------------------------------------------------------------------------
        // THEN DEFINE ENEMY SHIP

        /*for(i=0;i<enemyShipLayout.length;i++){
            for(j=0;j<enemyShipLayout[0].length;j++){
                enemyShipLayout[i][j] = roomTypes.normalCor;
            }
        }*/

        //enemyShipLayout[1][1]=roomTypes.normalCor;
        enemyShipLayout[0][2]=roomTypes.ShieldRoom;
        //enemyShipLayout[3][3]=roomTypes.normalCor;
        //enemyShipLayout[4][5]=roomTypes.normalCor;
        enemyShipLayout[5][2]=roomTypes.cockpitH;      // Cockpit
        enemyShipLayout[4][2]=roomTypes.BigSquare;       // Big square corridor
        enemyShipLayout[4][3]=roomTypes.OxyTyms;         // life support
        enemyShipLayout[3][1]=roomTypes.Medbay;          // Medbay
        enemyShipLayout[3][2]=roomTypes.corridorH;   // horizontal corridor
        enemyShipLayout[2][2]=roomTypes.EngineRoom;        // Engine room
        enemyShipLayout[2][3]=roomTypes.ShieldRoom;      // shields
        enemyShipLayout[2][1]=roomTypes.surveillanceRoom; // surveillance

        numROoms = 36;       // max number - 6x6
        numROomsCount = 0;  // to track and make sure got all as sprites created
        // Need to convert coordinates from graphic size to fit screen
        // work on basis of ship taking up 80% of screen height as phone forced horizontal AR
        sWidth=(int) gameport.getWorldWidth();
        sHeight=(int) gameport.getWorldHeight();
        TLy = margin; // y position of TL tile
        TLx = sWidth-margin-(sideGrphc)*sGrphcRatio; // x position of TL tile
        // ratio of screen to graphic is:
        sGrphcRatio = sideSqr/sideGrphc;
        System.out.println("sidesqr "+sideSqr+" and ratio "+sGrphcRatio);
        System.out.println("BLs "+TLx+" and "+TLy);

        enemyRooms = new shipSecSprite[numROoms];
        // need intermediate list to prevent altering enum
        tempSize3 = new ArrayList<Float>();
        tempSize4 = new ArrayList<Float>();

        // Give positions and sizes of rooms
        for (i=0;i<enemyShipLayout.length;i++) {
            for (j = 0; j < enemyShipLayout[0].length; j++) {
                if (enemyShipLayout[i][j] == null) {
                    System.out.println("EMPTY ROOM ENEMY");
                } else {
                    //System.out.println("tempsize pre get " + tempSize3);
                    tempSize4 = roomMapping.get(enemyShipLayout[i][j].name());
                    tempSize3.add(tempSize4.get(0));
                    tempSize3.add(tempSize4.get(1));
                    //System.out.println("full map "+roomMapping);
                    //System.out.println("tempsize post get " + tempSize3);
                    //tempSize2[0] = tempSize[0];     // x length
                    //tempSize2[1] = tempSize[1];     // y length
                    tempSize3.add(TLx+100*i*sGrphcRatio);           // x coordinate BL
                    tempSize3.add(TLy+100*j*sGrphcRatio);           // y coordinate BL
                    //System.out.println("specific room coords " + tempSize3);
                    //System.out.println("Roomtype name "+shipLayout[i][j].name());
                    System.out.println("Room "+enemyShipLayout[i][j].name());
                    enemyRooms[numROomsCount] = new shipSecSprite(game,null,enemyShipLayout[i][j].name(),tempSize3,sGrphcRatio);
                    numROomsCount++;
                    //roomPositions.put(shipLayout[i][j].name(), tempSize3);
                    tempSize3 = new ArrayList<>();
                    tempSize4 = new ArrayList<>();
                    //System.out.println("post delete tempsize3 " + tempSize3);
                    //System.out.println("");
                }
            }
        }

        System.out.println("Finished creating room sprites. ROomcount comparison "+numROoms+" vs "+numROomsCount);

        Gdx.graphics.setWindowedMode(640,360);

        // start creating objects
        // perhaps dont need the enum map above


    }

}
