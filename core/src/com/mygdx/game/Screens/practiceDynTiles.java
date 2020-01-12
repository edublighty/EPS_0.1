
package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;
import com.mygdx.game.Perlin.Perlin2;
import com.mygdx.game.Sprites.grndPlayerSprite;
import com.mygdx.game.HUDs.atmosHud;
import com.mygdx.game.Tools.b2dWorldCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class practiceDynTiles implements Screen {

    private OrthogonalTiledMapRenderer renderer;
    private MyGdxGame game;
    OrthographicCamera gamecam;
    private Viewport gameport;
    public Vector3 posCameraDesired;

    // Tiled Maps
    private TiledMap map;
    private TiledMapTileSet tileSet;
    private TiledMapTileLayer baseLayer;
    private TiledMapTileLayer copyLayer;
    private MapLayers mapLayers;
    private Texture tiles;
    private int[][] pLayout;
    private float transX;
    private float transY;
    private TiledMapTileLayer terrainLayer;
    private TiledMapTileLayer objectLayer;

    private float time;

    // Textures
    private TextureRegion[][] splitTiles;
    private TextureAtlas marAt;
    private TextureAtlas tilesAt;
    TextureAtlas.AtlasRegion atile[];
    private TextureAtlas tilesAtOvers;
    TextureAtlas.AtlasRegion overTile[];
    private TextureAtlas tilesAtBuilds;
    TextureAtlas.AtlasRegion buildTile[];

    // Coordinates
    public float posX;
    public float posY;
    private int posTX;
    private int posTY;
    private int Tsize;
    public int TLi;
    public int TLj;
    private int TLiInc;
    private int TLjInc;
    private int TLiInit;
    private int TLjInit;
    private float initPosX;
    private float initPosY;
    private float prevPosX;
    private float prevPosY;
    private float resetX;
    private float resetY;
    private float maxBuffX;
    private float minBuffX;
    private float maxBuffY;
    private float minBuffY;
    private int offsetX;
    private int offsetY;
    private boolean bool;

    // map arrays
    public int[][] mapLayout;
    public int[] mapHeights;
    public int[][] mapLayoutOvers;
    public int[][] mapLayoutBuilds;

    // Box2D variables
    private atmosHud atmoshud;
    public World world;
    private Box2DDebugRenderer b2dr;
    public grndPlayerSprite player;
    private float gravity;
    private boolean undWater;

    // World variables
    public int wWidth;         // World width tiles
    public int wHeight;        // World height tiles
    public int sWidth;         // Screen width px
    public int sHeight;        // Screen height tiles
    private int mWidth;         // Entire map width TILES
    private int mHeight;        // Entire map height TILES
    private int mapWidth;
    private int mapHeight;

    // Body variables
    private Array<Body> bodies; // Array containing bodies
    public int constrX1;       // Constraint reached left/right viewport at which bodies are reloaded
    public int constrX2;       // Constraint bodies are drawn to left/right of viewport
    public int constrY1;       // Constraint reached above/below viewport at which bodies are reloaded
    public int constrY2;       // Constraint bodies are drawn to above or below viewport

    Perlin2 noise = new Perlin2();
    Perlin noiseOrig = new Perlin();
    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private TextureRegion[] regions = new TextureRegion[4];
    private Texture texture;
    private b2dWorldCreator b2dWC;

    int PXtotal;
    private float[][] perlinNoise;
    private float[][] perlinNoise2D;
    int tileTotal;
    int tileVTotal;

    public float[][] humidityNoise;
    public float[][] tempNoise;
    public float[][] terrainProfile;
    public int[][] biomesData;
    public int[][] biomesLayout;
    int biomeLayout[][];

    long time1;

    public practiceDynTiles(MyGdxGame game) {

        time1 = 0;
        time1 = System.currentTimeMillis() - time1;
        System.out.println("time constructor start "+time1);
        time1 = System.currentTimeMillis();

        Tsize = 50;
        this.game = game;
        bool = false;
        pLayout = new int[120][120];
        sWidth = 2000;                          // Screen width px
        wWidth = Math.round(sWidth/Tsize);                 // World width in tiles
        sHeight = 1200;                         // Screen height px
        wHeight = Math.round(sHeight/Tsize);               // World height in tiles
        offsetX = -1;                            // offset X for screen draw
        offsetY = -1;                            // offset Y for screen draw
        resetX = 100F;                          // variable to reset player position on respawn
        resetY = 100;
        mapWidth = 3200;//mapLayout.length;      // entire map width in terms of tiles
        mapHeight = 3200;//mapLayout[0].length;   // entire map height in terms of tiles
        //mapHeights = new int[]
        TLi = 100;
        TLj = 100;
        gravity = 10f;
        time = 4;

        // Body variable initialisation
        constrX1 = sWidth/(Tsize*2);
        constrX2 = sWidth/Tsize;
        constrY1 = sHeight/(Tsize*2);
        constrY2 = sHeight/Tsize;

        // Variables for player movement around temporary world
        prevPosX = wWidth * Tsize / (2*MyGdxGame.PPM);
        prevPosY = wHeight * Tsize / (2*MyGdxGame.PPM);
        TLiInc = 15;
        TLjInc = 15;            // delete these too
        maxBuffX = prevPosX+TLiInc*500000/MyGdxGame.PPM;//+1f*(1+TLiInc/10);            // all this can probably be deleted now
        minBuffX = prevPosX-TLiInc*500000/MyGdxGame.PPM;//*(1-TLiInc/10);
        maxBuffY = prevPosY+TLjInc*500000/MyGdxGame.PPM;//*(1+TLjInc/10);
        minBuffY = prevPosY-TLjInc*500000/MyGdxGame.PPM;//*(1-TLjInc/10);

        System.out.println("initial initial player X "+prevPosX+" and Y "+prevPosY );
        System.out.println("initial initial player maxBuffX "+maxBuffX+" and maxBuffY "+maxBuffY );

        String sCurrentLine;
        biomesData = new int[6][10];
        FileHandle handle = Gdx.files.internal("Arrays/biomes.txt");
        BufferedReader reader = new BufferedReader(handle.reader());

        int i;
        int j = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                for(i=2;i<7;i++) {
                    biomesData[i-2][j] = Integer.parseInt(arr[i]);
                }
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        // generate maps before anything else
        landmapGenandPerlin();
/*
        String sCurrentLine;
        FileHandle handle = Gdx.files.internal("practiceDynTiles/pLayout2.txt");
        BufferedReader reader = new BufferedReader(handle.reader());


        int i = 0;
        int j;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                for (j = 0; j < 120; j++) {
                    pLayout[i][j] = Integer.parseInt(arr[j]);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }


        System.out.println("Text file load complete");*/
        i = 3;

        //System.out.println("layouts are " + pLayout[i][0] + " " + pLayout[i][1] + " " + pLayout[i][2] + " " + pLayout[i][3]);

        // camera to follow sprite
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(sWidth / MyGdxGame.PPM, sHeight / MyGdxGame.PPM, gamecam) {
        };//MyGdxGame.V_WIDTH*2/MyGdxGame.PPM,MyGdxGame.V_HEIGHT*2/MyGdxGame.PPM,gamecam) {             /*ScreenViewport allows player to see more with bigger screen*/        };
        atmoshud = new atmosHud(game.batch,sWidth,sHeight);

        map = new TmxMapLoader().load("practiceDynTiles/pDTMap3.tmx");
        //tileSet = map.getTileSets().getTileSet("pDTSet");
        //tiles = new Texture(Gdx.files.internal("PNGsPacked/pLayoutTiles.png"));
        marAt = new TextureAtlas("PNGsPacked/marioPack.atlas");
        tilesAt = new TextureAtlas("PNGsPacked/biomespack50px.atlas");
        tilesAtBuilds = new TextureAtlas("PNGsPacked/buildTiles200px.atlas");
        tilesAtOvers = new TextureAtlas("PNGsPacked/overTiles.atlas");
        //splitTiles = TextureRegion.split(tiles, 100, 100);
        //map = new TiledMap();
        //TiledMapTileLayer newLayer = new TiledMapTileLayer(1600, 1600, 100, 100);
        //mapLayers = new MapLayers();

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

        overTile = new TextureAtlas.AtlasRegion[2];
        String overs[] = {"pDTTile50Nada","water3"};
        for (i = 0; i < overTile.length; i++) {
            overTile[i] = tilesAtOvers.findRegion(overs[i]);
        }

        world = new World(new Vector2(0, -gravity), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();
        resetX = TLi*Tsize/MyGdxGame.PPM;
        //TLj=TLi;
        resetY = TLj*Tsize/MyGdxGame.PPM;
        System.out.println("resetX "+resetX+" terrProf at resetX "+terrainProfile[Math.round(resetX*MyGdxGame.PPM/Tsize)][0]);
        //resetY = (terrainProfile[Math.round(resetX*MyGdxGame.PPM/Tsize)][0])*Tsize/MyGdxGame.PPM;
        player = new grndPlayerSprite(world, this,resetX,resetY);
        System.out.println("this is the subtraction "+2*sWidth/(Tsize));
        TLiInit = TLi;//Math.round(resetX/Tsize*MyGdxGame.PPM-sWidth/(2*Tsize));
        TLjInit = TLj;//Math.round(resetY/Tsize*MyGdxGame.PPM-sHeight/(2*Tsize));
        /*TLi = TLiInit;
        TLj = TLjInit;*/
        b2dWC = new b2dWorldCreator(world,map,this,null,mapLayout,TLi,TLj,sWidth,sHeight);
        bodies = new Array<Body>();
        world.getBodies(bodies);
        System.out.println("size of body array "+bodies.size);
        System.out.println("position of a body "+bodies.get(0).getPosition());
        //world.destroyBody(bodies.get(0));

        System.out.println("Finished setting map");
        //updateMap();



        if (map == null) {
            System.out.println("Null TM");
        } else {
            System.out.println("Active TM");
        }

        if (tileSet == null) {
            System.out.println("Null TS");
        } else {
            System.out.println("Active TS");
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

        // initialise gamecam centrally
        //gamecam.position.set(wWidth / 2, wWidth / 2, 0);

        //player = new grndPlayerSprite(world,this,resetX,resetY);

        System.out.println("BEFORE");
        System.out.println("posX " + posX + " and posY " + posY);
        System.out.println("world width " + gameport.getWorldWidth() + " and world height " + gameport.getWorldHeight());
        System.out.println("initial x " + initPosX + " and Y " + initPosY);
        //player.b2body.set
        System.out.println("initial Playerx " + player.b2body.getPosition().x + " and PlayerY " + player.b2body.getPosition().y);

        transX = 0;
        transY = 0;
        posX = player.b2body.getPosition().x;
        posY = player.b2body.getPosition().y;
        initPosX = posX;
        initPosY = posY;

        System.out.println("AFTER");
        System.out.println("posX " + posX + " and posY " + posY);
        System.out.println("world width " + gameport.getWorldWidth() + " and world height " + gameport.getWorldHeight());
        System.out.println("initial x " + initPosX + " and Y " + initPosY);
        System.out.println("transX " + transX + " and transY " + transY);
        //player.b2body.set
        System.out.println("initial Playerx " + player.b2body.getPosition().x + " and PlayerY " + player.b2body.getPosition().y);

        System.out.println("Finished initialisation");

        time1 = System.currentTimeMillis() - time1;
        System.out.println("time end of constructor "+time1);
        time1 = System.currentTimeMillis();

        System.out.println("maplayout is "+mapLayout.length+" by "+mapLayout[0].length);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //System.out.println("Render");

        posX = Math.round(player.b2body.getPosition().x);
        posY = Math.round(player.b2body.getPosition().y);
        //TLi = (int) Math.floor(posX / 0.5);
        //TLj = (int) Math.floor(posY / 0.5);
        TLi = Math.round(posX/Tsize*MyGdxGame.PPM-sWidth/(2*Tsize));
        TLj = Math.round(posY/Tsize*MyGdxGame.PPM-sHeight/(2*Tsize));

        update(delta);

        //System.out.println("play render");
        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        renderer.render();


        //gamecam.frustum.planePoints.

        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        game.batch.setProjectionMatrix(gamecam.combined);

        //posTX = Math.floor(posX/50);
        //posTY = Math.floor(posY/50);

        int i;// = Math.round(posX*MyGdxGame.PPM)/50;
        int j;// = Math.round(posY*MyGdxGame.PPM)/50;
        game.batch.begin();
        //System.out.println("Batching hard, i "+i+" and j "+j+" and maplayout "+mapLayout[i][j]);
        //player.draw(game.batch);

//        TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
        // this loop constructs map for first time
        for (i = 0 - offsetX; i < sWidth/50 + offsetX; i++) {
            for (j = 0 - offsetY; j < sHeight/50 + offsetY; j++) {
                // here change tiles diagonally;
                //System.out.println("buildTile "+mapLayoutBuilds[i+TLi][j+TLj]+" and overTile "+mapLayoutOvers[i+TLi][j+TLj]);
                //System.out.println("buildTile "+mapLayoutBuilds.length+" and overTile "+mapLayoutOvers.length);
                //newCell.setTile(new StaticTiledMapTile(atile[mapLayout[i][j]]));//splitTiles[0][pLayout[i][j]]));
                //terrainLayer.setCell(i-TLi, j-TLj, newCell);
                //System.out.println("i+TLi "+i+TLi+" and j+TLj "+j+TLj);
                game.batch.draw(atile[mapLayout[i+TLi][j+TLj]],((i+TLi)*50/MyGdxGame.PPM),((j+TLj)*50/MyGdxGame.PPM),50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
                if(mapLayoutBuilds[i+TLi][j+TLj]==1) {
                    // there is a building there
                    game.batch.draw(buildTile[0/*mapLayoutBuilds[i + TLi][j + TLj]*/], ((i + TLi) * 50 / MyGdxGame.PPM), ((j + TLj) * 50 / MyGdxGame.PPM), 200 / MyGdxGame.PPM, 200 / MyGdxGame.PPM);
                }
                player.draw(game.batch);
                game.batch.draw(overTile[mapLayoutOvers[i+TLi][j+TLj]],((i+TLi)*50/MyGdxGame.PPM),((j+TLj)*50/MyGdxGame.PPM),50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
                //newCell = null;
            }
        }
        //game.batch.draw(atile[mapLayout[i][j]], (posX), (posY), 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        game.batch.end();

        game.batch.setProjectionMatrix(atmoshud.stage.getCamera().combined);
        atmoshud.stage.draw();

        // render Box2D Debug lines
        b2dr.render(world,gamecam.combined);


    }

    @Override
    public void resize(int width, int height) {
        System.out.println("play resize");
        gameport.update(width, height, false);
        gamecam.update();
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

    public void handleInput(float dt) {
        // If mouse pressed on world, move map left
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //System.out.println("UPPING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            //gamecam.position.y += 10;// * Gdx.graphics.getDeltaTime();
            player.b2body.applyLinearImpulse(new Vector2(0, 0.5f), player.b2body.getWorldCenter(), true);
            //TLj++;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            //System.out.println("UPPING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            //gamecam.position.y -= 10;// * Gdx.graphics.getDeltaTime();
            //TLj--;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            //System.out.println("UPPING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            //gamecam.position.x -= 10;// * Gdx.graphics.getDeltaTime();
            //gamecam.position.x = player.b2body.getPosition().x;
            //gamecam.position.y = player.b2body.getPosition().y;
            player.b2body.applyLinearImpulse(new Vector2(10f, 0), player.b2body.getWorldCenter(), true);
            //TLi--;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            //System.out.println("UPPING");
            //gamecam.position.x+=100.0f * Gdx.graphics.getDeltaTime();
            //gamecam.position.x += 10;// * Gdx.graphics.getDeltaTime();
            player.b2body.applyLinearImpulse(new Vector2(-10f,0),player.b2body.getWorldCenter(),true);
            //TLi++;
            //updateMap();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            // see above
        } else {
            if(player.b2body.getLinearVelocity().x > 0) {
                player.b2body.applyLinearImpulse(new Vector2(-1f, 0), player.b2body.getWorldCenter(), true);
            } else if (player.b2body.getLinearVelocity().x < 0) {
                player.b2body.applyLinearImpulse(new Vector2(1f, 0), player.b2body.getWorldCenter(), true);
            }
        }

        if (Gdx.input.isTouched()) {
            //gamecam.translate(dt,-dt);
            //gamecam.update();
        }

    }

    public void update(float dt) {
        time = time + dt;

        if (time > 5) {
            if(player.b2body==null || player==null){
                System.out.println("Player null");
            }
            time = 0;
            System.out.println("TIME");
            System.out.println("posX " + posX + " and posY " + posY);
            System.out.println("init x " + initPosX + " and init Y " + initPosY);
            System.out.println("TLi " + TLi + " and TLj " + TLj);
        }
        handleInput(dt);

        //posX = player.b2body.getPosition().x;     // already done in render
        //posY = player.b2body.getPosition().y;     // as is update of TLs
        Vector2 playerVec = player.b2body.getLinearVelocity();
        int i;
        Vector2 bVec = bodies.get(0).getPosition();

        //System.out.println("TL");

        world.step(1/60f,6,2);


        player.update(dt);

        if(mapLayout[TLi][TLj]==1 && mapLayout[TLi][TLj+1]!=1){
            // we are entering the water my friends
            // reload player
            undWater = true;
            reloadObjs(playerVec);
        } else if(mapLayout[TLi][TLj-1]==1 && mapLayout[TLi][TLj]!=1){
            // we are leaving the water my buddies
            undWater = false;
            reloadObjs(playerVec);
        } else {
            // the other things
        }

        if(TLi > TLiInit+wWidth+constrX1 || TLi < TLiInit-constrX1 || TLj > TLjInit+wHeight+constrY1 || TLj < TLjInit-constrY1) {
            reloadObjs(playerVec);
            /*resetX = posX;//Math.round((TLi+wWidth/2)*Tsize/MyGdxGame.PPM);
            resetY = posY;//Math.round((TLj+wHeight/2)*Tsize/MyGdxGame.PPM);
            bodies = null;
            bodies = new Array<>();
            world.getBodies(bodies);
            System.out.println("TLi "+TLi+" TLj "+TLj+"DESTROYING BODIES FOR UPDATE: " + world.getBodyCount() + " compared to " + bodies.size);

            for (i = 0; i<bodies.size-1; i++) {
                // destroy all bodies
                //System.out.println("DESTROYING " + i + " out of " + world.getBodyCount() + " " + bodies.size);
                if (!world.isLocked()) {
                    world.destroyBody(bodies.get(i));
                } else {
                    System.out.println("world locked");
                }
            }
            System.out.println("finished destroying yay");
            bodies = null;
            bodies = new Array<>();
            world.getBodies(bodies);
            System.out.println("number of world bodies " + world.getBodyCount() + " vs " + bodies.size);//bodies.get(0).getPosition().x + " by " + bodies.get(2).getPosition().y);
            b2dWC.updateSqs(this);
            player = new grndPlayerSprite(world, this,resetX,resetY);
            player.b2body.applyLinearImpulse(playerVec,player.b2body.getWorldCenter(),true);
            System.out.println("and back again TLi "+TLi+" TLj "+TLj);
            TLiInit = TLi;
            TLjInit = TLj;*/
        }



/*
        if(TLi > TLjInit + wWidth + constrX1){
            // Going right
            for(i=0;i<bodies.size;i++){
                if(bVec.x < TLi + wWidth + constrX2 && bVec.x > TLi - constrX2 && bVec.y < TLj + sHeight + constrY2 && bVec.y > TLj - constrY2){
                    // bodies are within boundaries
                } else {
                    // bodies not within boundaries - destroy the bastards
                    world.destroyBody(bodies.get(i));
                }
            }
            for(i=Math.round((initPosX + wWidth + constrX2)/50);i<Math.round((TLi + wWidth + constrX2*2)/50);i++){
                // create new bodies
                int i0 = Math.round((initPosX + wWidth + constrX2)/50);
                int i1 = Math.round((TLi + wWidth + constrX2*2)/50);
                int j0 = Math.round()
                b2dWC.updateSqs(this,i0,i1);

        } else if(posY > initPosY + sHeight + constrY1) {
            // Going up
            for (i = 0; i < bodies.size; i++) {
                if (bVec.x < posX + sWidth + constrX2 && bVec.x > posX - constrX2 && bVec.y < posY + sHeight + constrY2 && posY > posY - constrY2) {
                    // bodies are within boundaries
                } else {
                    // bodies not within boundaries - destroy the bastards
                    world.destroyBody(bodies.get(i));
                }
            }
        }

            if (posX < initPosX - constrX1) {
                // Going left
                for (i = 0; i < bodies.size; i++) {
                    if (bVec.x < posX + sWidth + constrX2 && bVec.x > posX - constrX2 && bVec.y < posY + sHeight + constrY2 && posY > posY - constrY2) {
                        // bodies are within boundaries
                    } else {
                        // bodies not within boundaries - destroy the bastards
                        world.destroyBody(bodies.get(i));
                    }
                }
            } else if (posY < initPosY - constrY1) {
                // Going down
                for (i = 0; i < bodies.size; i++) {
                    if (bVec.x < posX + sWidth + constrX2 && bVec.x > posX - constrX2 && bVec.y < posY + sHeight + constrY2 && posY > posY - constrY2) {
                        // bodies are within boundaries
                    } else {
                        // bodies not within boundaries - destroy the bastards
                        world.destroyBody(bodies.get(i));
                    }
                }
            }
*/


/*

        if (posX > maxBuffX) {
            // moved past tile on right
            System.out.println("left");
            */
/*if(TLi > TLiMax - 10){
                // At world left
            } else {*//*

            time1 = System.currentTimeMillis() - time1;
            System.out.println("time before update "+time1);
            time1 = System.currentTimeMillis();
                //TLi+=TLiInc;
                resetX=prevPosX;
                resetY=posY;
                //updateMap();
            time1 = System.currentTimeMillis() - time1;
            System.out.println("time after update "+time1);
            time1 = System.currentTimeMillis();
                //gamecam.position.x = initPosX;
            //}
        } else if (posX < -minBuffX*1000) {
            // moved past tile on left
            System.out.println("right");
            //TLi-=TLiInc;
            resetX=prevPosX;
            resetY=posY;
            //updateMap();
        } else {
            // still within boundaries
        }

        if (posY > maxBuffY*5000) {
            // moved past tile up
            //System.out.println("down");
            //TLj+=TLjInc;
            //updateMap();
        } else if (posY < -minBuffY*2000) {
            // moved past tile down
            //System.out.println("up");
            //TLj-=TLjInc;
            //updateMap();
        } else {
            // still within boundaries
        }
*/
        atmoshud.pHeight = (int) Math.round(player.b2body.getPosition().y*100);
        atmoshud.pXSpeed = (int) Math.round(Math.pow((Math.pow(player.b2body.getLinearVelocity().x,2)+Math.pow(player.b2body.getLinearVelocity().y,2)),0.5));
        atmoshud.update(dt);
        //gamecam.position.x = player.b2body.getPosition().x;

        gamecam.update();
        renderer.setView(gamecam);
    }

    public void reloadObjs(Vector2 playerVec){
        // delete objects and reload
        resetX = posX;//Math.round((TLi+wWidth/2)*Tsize/MyGdxGame.PPM);
        resetY = posY;//Math.round((TLj+wHeight/2)*Tsize/MyGdxGame.PPM);
        bodies = null;
        bodies = new Array<>();
        world.getBodies(bodies);
        System.out.println("TLi "+TLi+" TLj "+TLj+"DESTROYING BODIES FOR UPDATE: " + world.getBodyCount() + " compared to " + bodies.size);
        int i;

        for (i = 0; i<bodies.size-1; i++) {
            // destroy all bodies
            //System.out.println("DESTROYING " + i + " out of " + world.getBodyCount() + " " + bodies.size);
            if (!world.isLocked()) {
                world.destroyBody(bodies.get(i));
            } else {
                System.out.println("world locked");
            }
        }
        System.out.println("finished destroying yay");
        bodies = null;
        bodies = new Array<>();
        world.getBodies(bodies);
        System.out.println("number of world bodies " + world.getBodyCount() + " vs " + bodies.size);//bodies.get(0).getPosition().x + " by " + bodies.get(2).getPosition().y);
        b2dWC.updateSqs(this);
        player = new grndPlayerSprite(world, this,resetX,resetY);
        player.b2body.applyLinearImpulse(playerVec,player.b2body.getWorldCenter(),true);
        System.out.println("and back again TLi "+TLi+" TLj "+TLj);
        TLiInit = TLi;
        TLjInit = TLj;
    }

    public void updateMap() {
        // Pretty sure this is now redundant
        //System.out.println("Disposing map");
        /*if(bool == true) {
            mapLayers.remove(newLayer);
        } else {
            bool = true;
        }*/
        System.out.println("updateMap");
            world.dispose();
            world = new World(new Vector2(0, -gravity), true); // 0,0 transpires as no gravity
            b2dr.dispose();
            b2dr = new Box2DDebugRenderer();
            map.dispose();
            map = new TiledMap();
            //System.out.println("wTwidth "+wWidth);
            terrainLayer = new TiledMapTileLayer(wWidth*50, wWidth*50, 50, 50);
            //objectLayer = new TiledMapTileLayer(wWidth*50, wWidth*50, 50, 50);
            mapLayers = map.getLayers();

            int i;
            int j;

            System.out.print("array x " + mapLayout.length + " and array y " + mapLayout[0].length);
            System.out.print("TLi " + TLi + " and TLj " + TLj);
            //System.out.print("pLyt x "+mapLayout[TLi][TLj]+" STOP! ");// sT size "+splitTiles.length +" + "+splitTiles[0].length+" stop ");

            // this loop constructs map for first time
            for (i = TLi; i < TLi + wWidth; i++) {
                for (j = TLj; j < TLj + wWidth; j++) {
                    // here change tiles diagonally;
                    TiledMapTileLayer.Cell newCell = new TiledMapTileLayer.Cell();
                    newCell.setTile(new StaticTiledMapTile(atile[mapLayout[i][j]]));//splitTiles[0][pLayout[i][j]]));
                    terrainLayer.setCell(i-TLi, j-TLj, newCell);
                    newCell = null;
                }
            }
            mapLayers.add(terrainLayer);
            //new b2dWorldCreator(world,map,this,null,mapLayout,TLi,TLj,wWidth);
            player = new grndPlayerSprite(world, this,resetX,resetY);
            renderer = new OrthogonalTiledMapRenderer(map,1 / MyGdxGame.PPM);
    }


    public void landmapGenPRACTICE(){
        System.out.println("ERROR IN REDUNDANT FUNCTION - LANDMAPGEN PRACTICE");
        mapLayout = new int[mapWidth][mapHeight];
        System.out.println("mapwidth "+mapWidth+" and mapheight "+mapHeight);
        int i;
        int j;
        for(i=0;i<mapWidth;i++){
            for(j=0;j<mapHeight;j++){
                mapLayout[i][j] = 0;        // Blue - sky
            }
        }

        System.out.println("Sky set");

        for(i=0;i<mapWidth;i++){
            for(j=0;j<4;j++){
                if(j<3) {
                    //System.out.println("Setting dirt yay at j "+j+" and mapHeight "+mapHeight);
                    mapLayout[i][j] = 1;        // Brown - dirt
                }
                if(j==3 && (i % 2) == 0){
                    //mapLayout[i][j] = 1;        // Also brown - yay!
                }
            }
        }

        System.out.println("Land set eg "+mapLayout[0][0]);

        j=20;

        for(i=0;i<mapWidth;i++){
            if((i % 2) != 0){
                mapLayout[i][j] = 9;            // white fluffy cloud stuff like marshmallows but not. i have never actually eaten a cloud
            }
        }

        System.out.println("Clouds set");

        final String FNAME = "test.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<mapHeight;j++) {
                for (i = 1; i < mapWidth; i++) {
                    line1 = line1 + String.valueOf(mapLayout[i][j]);
                }
                bw.write(line1 + "\n");
                bw.write("STOP NOW");
                bw.write("\r\n");
                line1 = "";
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }


    }

    public void landmapGen(){
        // To be used once on loading a terrain screen - made redundant by lamdmapGenandPerlin
        System.out.println("ERROR IN REDUNDANT FUNCTION - LANDMAPGEN");
        //mapLayout = new int[mapWidth][mapHeight];
        double cL;          // planet liquid coefficient
        double cT;          // planet temp coefficient
        double cI;          // island coefficient
        double cP;          // planet size coefficient

        int PX;             // generic variable to represent a pixel
        //int PXtotal;        // planet circumference pixel size
        double PXperRad;       // pixels per radian
        int PXpoles;        // poles pixel size
        double thetaStart;  // Start of land gen after first pole clockwise - LAND EAST
        int pxStart;
        double thetaM1;     // End of land gen before second pole clockwise - LAND EAST
        int pxM1;
        double thetaM2;     // Start of land gen after second pole clockwise - LAND WEST
        int pxM2;
        double thetaEnd;    // End of land gen before first pole clockwise - LAND WEST
        int pxEnd;

        double h;           // Height for island function
        double phi;         // off set for island function
        int i;
        int j;

        // Initialise variables
        cP = 0.75;
        cT = 0.25;
        cI = 0.05;
        cL = 0.5;
        PX = 0;
        phi=0;

        PXtotal = (int) Math.round(((20 + 80*cP)*sWidth)/50)*50;
        int iTotal = PXtotal / 50;
        PXperRad = PXtotal / (2*Math.PI);
        PXpoles = (int) Math.round(cT*5*sWidth);
        thetaStart = (PXpoles / 2) * 360 / PXtotal;
        int iStart = (PXpoles / 2) / 50;
        thetaEnd = 360 - thetaStart;
        int iEnd = (PXtotal - PXpoles/2) / 50;
        thetaM1 = 180 - thetaStart;
        int iM1 = ((PXtotal / 2) - (PXpoles / 2)) / 50;
        thetaM2 = 180 + thetaStart;
        int iM2 = ((PXtotal / 2) + (PXpoles / 2)) / 50;

        pxStart = PXpoles / 2;
        pxM1 = PXtotal / 2 - pxStart;
        pxM2 = PXtotal / 2 + pxStart;
        pxEnd = PXtotal - pxStart;

        System.out.println("itotal "+iTotal+" PXpoles "+PXpoles+" iStart "+iStart+" iM1 "+iM1+" iM2 "+iM2+" iEnd "+iEnd);

        mapLayout = new int[iTotal][mapHeight];

        // Begin by setting all values to sky
        //System.out.println("mapwidth "+mapWidth+" and mapheight "+mapHeight);
        for(i=0;i<mapWidth;i++){
            for(j=0;j<mapHeight;j++){
                mapLayout[i][j] = 0;        // SKY
            }
        }

        // First half of northern pole
        for(i=0;i<iStart;i++){
            for(j=0;j<4;j++) {
                mapLayout[i][j] = 1;        // POLE
            }
        }

        // Eastern section - determines whether sea or land
        for(i=iStart;i<iM1;i++) {
            h = cL * Math.sin(cI * i + phi);
            if( h > 0){
                // land
                for(j=0;j<4;j++) {
                    //System.out.println("i "+i+" j "+j+" h "+h);
                    mapLayout[i][j] = 2;        // LAND
                }
            } else {
                // sea
                for(j=0;j<3;j++) {
                    //System.out.println("i "+i+" j "+j+" h "+h);
                    mapLayout[i][j] = 3;        // SEA
                }
            }
        }

        // Southern pole
        for(i=iM1;i<iM2;i++){
            for(j=0;j<4;j++) {
                mapLayout[i][j] = 1;        // POLE
            }

        }

        // Western section - determines whether sea or land
        for(i=iM2;i<iEnd;i++){
            h = cL * Math.sin(cI * i + phi);
            if( h > 0){
                // land
                for(j=0;j<4;j++) {
                    mapLayout[i][j] = 2;        // LAND
                }
            } else {
                // sea
                for(j=0;j<3;j++) {
                    mapLayout[i][j] = 3;        // SEA
                }
            }
        }

        // Second half of northern pole
        for(i=iEnd;i<iTotal;i++){
            for(j=0;j<4;j++) {
                mapLayout[i][j] = 1;        // POLE
            }
        }

        System.out.println("Done loads of land calcs");

        j=20;

        for(i=0;i<mapWidth;i++){
            if((i % 2) != 0){
                mapLayout[i][j] = 9;            // white fluffy cloud stuff like marshmallows but not. i have never actually eaten a cloud
            }
        }

        System.out.println("Clouds set");
/*

        final String FNAME = "test2.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<mapHeight;j++) {
                for (i = 1; i < mapWidth; i++) {
                    line1 = line1 + String.valueOf(mapLayout[i][j]);
                }
                bw.write(line1 + "\n");
                bw.write("STOP NOW");
                bw.write("\r\n");
                line1 = "";
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }
*/


    }

    public void landmapGenandPerlin(){
        // To be used once on loading a terrain screen
        //mapLayout = new int[mapWidth][mapHeight];
        double cL;          // planet liquid coefficient
        double cT;          // planet temp coefficient
        double cI;          // island coefficient
        double cP;          // planet size coefficient

        int PX;             // generic variable to represent a pixel
        //int PXtotal;        // planet circumference pixel size
        double PXperRad;       // pixels per radian
        int PXpoles;        // poles pixel size
        double thetaStart;  // Start of land gen after first pole clockwise - LAND EAST
        int pxStart;
        double thetaM1;     // End of land gen before second pole clockwise - LAND EAST
        int pxM1;
        double thetaM2;     // Start of land gen after second pole clockwise - LAND WEST
        int pxM2;
        double thetaEnd;    // End of land gen before first pole clockwise - LAND WEST
        int pxEnd;

        double h;           // Height for island function
        double phi;         // off set for island function
        int i;
        int j;

        // Initialise variables
        cP = 0.75;
        cT = 0.25;
        cI = 0.05;
        cL = 0.5;
        PX = 0;
        phi=0;

        PXtotal = (int) Math.round(((20 + 80*cP)*sWidth)/50)*50;        // not tied into genTerrain yet
        mWidth = PXtotal / 50;
        mHeight = Math.round(mWidth);
        int iTotal = PXtotal / 50;
        PXperRad = PXtotal / (2*Math.PI);
        PXpoles = (int) Math.round(cT*5*sWidth);
        thetaStart = (PXpoles / 2) * 360 / PXtotal;
        int iStart = (PXpoles / 2) / 50;
        thetaEnd = 360 - thetaStart;
        int iEnd = (PXtotal - PXpoles/2) / 50;
        thetaM1 = 180 - thetaStart;
        int iM1 = ((PXtotal / 2) - (PXpoles / 2)) / 50;
        thetaM2 = 180 + thetaStart;
        int iM2 = ((PXtotal / 2) + (PXpoles / 2)) / 50;

        pxStart = PXpoles / 2;
        pxM1 = PXtotal / 2 - pxStart;
        pxM2 = PXtotal / 2 + pxStart;
        pxEnd = PXtotal - pxStart;

        genTerrain();           // generate overall terrain profile for land/sky boundary

        int seaLevel = 1000;
        int tilePX = 50;
        tileTotal = terrainProfile.length;//PXtotal / tilePX;
        tileVTotal = terrainProfile.length;//Math.round(tileTotal / 4);
        System.out.println("lndmpGenPrln tileTot "+tileTotal+" and tileVTot "+tileVTotal);

        perlinNoise2D = new float[tileTotal][2];
        perlinNoise2D = perlinFunOLD(tileTotal,2);

        perlinNoise = new float[tileTotal][tileVTotal];
        perlinNoise = perlinFunOLD(tileTotal,tileVTotal);



        /*
        mapHeights = new int[mapWidth];
        //System.out.println("itotal "+iTotal+" PXpoles "+PXpoles+" iStart "+iStart+" iM1 "+iM1+" iM2 "+iM2+" iEnd "+iEnd);
        System.out.println("perlinNoise2D "+perlinNoise2D[0][1]+" tileTotal "+tileTotal+" mapWidth "+mapWidth);
        System.out.println("perlinNoise2D * tileVTotal = "+( (int) perlinNoise2D[3][1]*tileVTotal));
        for(i=0;i<mapWidth;i++){
            mapHeights[i] = Math.round(perlinNoise2D[i][1]*tileVTotal);
        }
        */

        mapLayout = new int[tileTotal][tileVTotal];
        mapLayoutOvers = new int[tileTotal][tileVTotal];
        mapLayoutBuilds = new int[tileTotal][tileVTotal];

        //perlinNoise = new float[tileTotal][tileVTotal];
        mapHeight=terrainProfile.length;

        //{"Blue", "Brown", "Green", "Grey", "LILACMAYBE", "Orange", "Pink", "Purple", "Red", "White", "Yellow", "Arctic1", "Desert1", "Forest1", "Volcano1"};

        // Begin by setting a load of Perlin noise for absolute bants
        System.out.println("mapwidth "+mapWidth+" and mapheight "+mapHeight);
        for(i=0;i<mapWidth;i++){
            for(j=0;j<mapHeight;j++){
                //System.out.println("i "+i+" j "+j+" perly "+perlinNoise[i][j]);
                if(perlinNoise[i][j]<0.1){
                    /*if(i==0 || j==0) {
                        if (i == 0 && j == 0) {
                            // At left or bottom
                            mapLayout[i][j] = 1;        // Dirt
                        } else if (i == 0 && mapLayout[i][j - 1] == 1) {
                            mapLayout[i][j] = 1;        // Dirt
                        } else if (j == 0 && mapLayout[i - 1][j] == 1) {
                            mapLayout[i][j] = 1;        // Dirt
                        }
                    } else if(mapLayout[i-1][j]==1 || mapLayout[i][j-1]==1 || mapLayout[i-1][j-1]==1) {
                        mapLayout[i][j] = 1;        // Dirt
                    } else {
                        if(Math.pow((mapLayout[i][j]),1/j)>0.2) {
                            // no dirt
                            mapLayout[i][j] = 0;        // Water
                        }
                    }*/
                    mapLayout[i][j] = 2;        // Green
                } else if (perlinNoise[i][j]>=0.1 && perlinNoise[i][j]<0.2){
                    mapLayout[i][j] = 3;        // Stone
                } else if (perlinNoise[i][j]>=0.2 && perlinNoise[i][j]<0.3){
                    mapLayout[i][j] = 4;        // Purple?
                } else if (perlinNoise[i][j]>=0.3 && perlinNoise[i][j]<0.5){
                    mapLayout[i][j] = 5;        // Orange??
                } else if (perlinNoise[i][j]>=0.5 && perlinNoise[i][j]<0.6){
                    mapLayout[i][j] = 6;        // Pink??
                } else if (perlinNoise[i][j]>=0.6 && perlinNoise[i][j]<0.7){
                    mapLayout[i][j] = 7;        // More purple???
                } else if (perlinNoise[i][j]>=0.7 && perlinNoise[i][j]<0.8){
                    mapLayout[i][j] = 8;        // RED????
                } else if (perlinNoise[i][j]>=0.8 && perlinNoise[i][j]<0.9){
                    mapLayout[i][j] = 9;        // Snow maybe??
                } else if (perlinNoise[i][j]>=0.9 && perlinNoise[i][j]<1){
                    mapLayout[i][j] = 10;        // Yellerh??
                } else {
                    // error
                    System.out.println("ERROR IN INITIAL LAND MAP GEN AND PERLIN");
                    System.out.println("perlinNoise value "+perlinNoise[i][j]);
                }
                //mapLayout[i][j] = 0;        // SKY
            }
        }

        // Then sort difference between land and sky
        int floorLevel;
        int jStart;
        int jEnd;
        for(i=0;i<mapWidth;i++){
            floorLevel = Math.round(terrainProfile[i][1]);//mapHeights[i];
            System.out.println("floorLevel "+floorLevel);
            //System.out.println("floorLevel "+floorLevel+" and i "+i);
            if(floorLevel<10){
                jEnd = 0;
                jStart=0;
            } else {
                jStart = 0;
                jEnd = 5;
            }
            for(j=jStart;j<jEnd;j++) {
                System.out.println("making dirt bitch");
                mapLayout[i][floorLevel-j] = 13;//Math.round(terrainProfile[i][0]);//+10; // set biome floor
            }
            // then set sky above
            for(j=floorLevel+1;j<mapHeight;j++){
                //System.out.println("sky yay");
                mapLayout[i][j] = 0;        // SKY yaaaaaaaaas
            }
            // finally add some sea mothafuckaaa
            if(terrainProfile[i][1]<seaLevel){
                // below sea level
                for(j=(int) terrainProfile[i][1]+1;j<seaLevel;j++) {
                    mapLayoutOvers[i][j] = 1;           // i think this is sea
                }
            }
        }

/*

        final String FNAME2 = "test"+"MPLYT.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME2)) )
        {
            for (j=0;j<mapLayout.length;j++) {
                for (i = 0; i < 100; i++) {
                    line1 = line1 + String.valueOf(mapLayout[i][j]) +" ";
                }
                bw.write(line1 + "\n");
                bw.write("STOP NOW");
                bw.write("\r\n");
                line1 = "";
            }

            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }
*/

        System.out.println("Done loads of land calcs");

        j=3020;
        int k = 0;
        boolean building = true;
        int num;

        for(i=0;i<mapWidth;i++){
            if((i % 2) != 0){
                mapLayout[i][j] = 9;            // white fluffy cloud stuff like marshmallows but not. i have never actually eaten a cloud
            }
/*

            if(k>75) {
                if (perlinNoise2D[i][0] > 0.2) {
                    if (perlinNoise2D[i][0] > 0.4) {
                        if (perlinNoise2D[i][0] > 0.6) {
                            if (perlinNoise2D[i][0] > 0.8) {
                                // Building 3
                                num = 3;
                                genBuilds(num,i);
                                mapLayoutBuilds[i][(int) terrainProfile[i][1] + 1] = 4;
                            }
                        } else {
                            // Building 2
                            num = 2;
                            genBuilds(num,i);
                            mapLayoutBuilds[i][(int) terrainProfile[i][1] + 1] = 3;
                        }
                    } else {
                        // Building 1
                        num = 1;
                        genBuilds(num,i);
                        mapLayoutBuilds[i][(int) terrainProfile[i][1] + 1] = 2;
                    }
                } else {
                    // NO BUILDING THANK YOU
                    //mapLayoutBuilds[i][(int) terrainProfile[i][1] + 1] = 1;
                }
                } else {
                // Don't build too close
            }
*/

            if(building) {
                if (k < 10) {
                    mapLayoutBuilds[i][(int) terrainProfile[i][1] + 1] = 1;      // add a building every 10 places
                } else {
                    k=0;
                    building = false;
                }
            }
            k++;
        if(k > 30) {
            //building = true;
            genBuilds(1,i);
            k=0;
        }

        }

        j=0;

        for(i=0;i<terrainProfile.length/4;i++){
            if(terrainProfile[i][0]>1000){
                TLi=i;
                //mapLayout[i][j] = 1;            // Put some dirt on down yo in case Perlin noise is shit
            }
        }



        TLj = Math.round(terrainProfile[TLi][1])+10;
        //TLj=200;

        System.out.println("TLj is now "+TLj);

        System.out.println("Clouds set");
/*

        final String FNAME = "test2.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<mapHeight;j++) {
                for (i = 1; i < mapWidth; i++) {
                    line1 = line1 + String.valueOf(mapLayout[i][j]);
                }
                bw.write(line1 + "\n");
                bw.write("STOP NOW");
                bw.write("\r\n");
                line1 = "";
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }

*/

    }

    public void genTerrain(){

        int i;
        //int mWidth = 3200;      // size of map in tile number
        int iCount=0;           // length of current biome
        boolean searching=true;
        int temp;
        int humid;
        int j = 0;
        biomeLayout = new int[50][3];
        humidityNoise = perlinFun(mapWidth,3,1,4,10);
        System.out.println("humidity "+humidityNoise[0][1]);
        temp = (int) Math.round(50*MathUtils.sin(MathUtils.PI*4*0/mapWidth-MathUtils.PI/2)+50);
        humid = Math.round(humidityNoise[0][1] * 100);
        System.out.println("humidity "+humid+" and temp "+temp);
        System.out.println("humidity limits "+biomesData[2][0]+" and "+biomesData[3][0]+" and temp limits "+biomesData[0][0]+" and "+biomesData[1][0]);

        // allocate biomes based on noise
        while (searching) {
            // looking for correct biome
            if (temp >= biomesData[0][j] && temp < biomesData[1][j] && humid >= biomesData[2][j] && humid < biomesData[3][j]) {
                // found a biome!
                searching = false;
                biomeLayout[0][0] = j;                      // give biome type
                biomeLayout[0][2] = biomesData[4][j];       // give flatness factor
            } else {
                // keep searching
                j++;
                //System.out.println("j "+j);
                if(j>10){
                    System.out.println("Biome location error");
                }
            }
        }

        System.out.println("initialised biome data - init biome "+biomeLayout[0][0]);

        int n = 0;
        j = 0;
        searching = true;

        System.out.println("humidityNoise size "+humidityNoise.length+" by "+humidityNoise[0].length);

        // Start measuring out sections of biomes
        for(i=0;i<mapWidth;i++) {
            //System.out.println("i "+i+" and iCount "+iCount);
            if (iCount < 200) {
                // do nothing - not long enough yet
                iCount++;
            } else {
                // time to change perhaps - record previous length
                temp = (int) Math.round(50*MathUtils.sin(MathUtils.PI*4*i/mapWidth-MathUtils.PI/2)+50);
                humid = Math.round(humidityNoise[i][1] * 100);
                System.out.println("temp "+temp+" and humidity "+humid+" i "+i+" "+mapWidth);
                j=biomeLayout[n][0];
                if(temp >= biomesData[0][j] && temp < biomesData[1][j] && humid >= biomesData[2][j] && humid < biomesData[3][j]){
                    // same biome - continue counting
                    iCount++;
                } else {
                    // different biome - go find it!
                    j=0;
                    biomeLayout[n][1] = iCount;
                    iCount = 0;
                    n++;
                    while (searching) {
                        // looking for correct biome
                        if (temp >= biomesData[0][j] && temp < biomesData[1][j] && humid >= biomesData[2][j] && humid < biomesData[3][j]) {
                            // found a biome - exit while loop!
                            searching = false;
                            biomeLayout[n][0] = j;
                            biomeLayout[n][2] = biomesData[4][j];
                            //System.out.println("biome "+biomeLayout[n][0]+" and flatness "+biomeLayout[n][2]);
                        } else if (temp>=100) {
                            if (humid < 25) {
                                // Desert
                                j = 0;
                            } else if (humid < 75) {
                                // Savannah
                                j = 4;
                            } else {
                                // Volcanoes and shit!
                                j = 8;
                            }
                            searching = false;
                        } else if (humid>=100){
                            if (temp<25){
                                // Mountainous Tundra
                                j = 7;
                            } else if (temp<75) {
                                // Rainforest
                                j = 9;
                            } else {
                                // Volcanoes
                                j = 8;
                            }
                            searching = false;
                        } else {
                            // keep searching
                            j++;
                        }
                    }
                    // reset for next search for a biome if required
                    searching = true;
                    j = 0;
                }
            }
        }

        // place final part of iCount
        biomeLayout[n][1] = iCount;

        System.out.println("Biome gen finished - as follows");

        for(i=0;i<50;i++){
            System.out.println("biome "+biomeLayout[i][0]+" and length "+biomeLayout[i][1]+" and flatness "+biomeLayout[i][2]);
        }

        System.out.println("about to test");
        i=0;

        try {
            while (biomeLayout[i][1] != 0) {
                //System.out.println("testing "+biomeLayout[i][1]);
                i++;
            }
        } catch(RuntimeException e) {
            e.printStackTrace ();
            // Assume reached end of array
            i=32;
        }

        System.out.println("i "+i);

        biomesLayout = new int[i][3];
        for(j=0;j<i;j++){
            biomesLayout[j][0] = biomeLayout[j][0];
            biomesLayout[j][1] = biomeLayout[j][1];
            biomesLayout[j][2] = biomeLayout[j][2];
        }

        System.out.println("BL at "+i+" "+biomesLayout[i-1][0]+" and "+biomesLayout[i-1][1]);

        //System.out.println("biomes Lengt h "+biomesLayout.length+" vs "+biomesLayout[0].length);
        List<float[][]> biomesArray = new ArrayList<float[][]>();
        //biomesArray.add(new float[1][2]);
        float lastY = 1600;
        int tempX;
        int oct = 1;
        int oct6 = 7;
        int oct7 = 9;

        System.out.println("biomeslayout length "+biomesLayout.length);

        // Takes array for biomes and their lenghts, converts to terrain profile for world circumference using Perlin noise
        for(i=0;i<biomesLayout.length;i++){
            if(biomesLayout[i][2]==1){
                // flat
                oct = 1;
                oct6 = 1;
                oct7 = 13;
            } else if(biomesLayout[i][2]==2){
                // mild
                oct = 1;
                oct6 = 1;
                oct7 = 11;
            } else if(biomesLayout[i][2]==3){
                // mountainous
                oct = 1;
                oct6 = 7;
                oct7 = 9;
            } else {
                // No flatness factor - ERROR
                System.out.println("ERROR NO FLATNESS FACTOR ASSIGNED");
            }
            tempNoise = perlinFun2(lastY,biomesLayout[i][1],3,oct,oct6,oct7,mapHeight);
            tempNoise[0][0] = biomeLayout[i][0];            // biome
            tempNoise[0][2] = biomeLayout[i][2];            // flatness factor - yes that is a thing (1=flat, 2 = mild, 3 = mountainous)
            //System.out.println("flatness factor "+biomeLayout[0][2]);
            //System.out.println("length tempNosie "+tempNoise.length+" and "+tempNoise[0].length);
            tempX = (int) (biomesLayout[i][1]);
            lastY=tempNoise[tempX-1][1];
            //System.out.println("lastY "+lastY);
            biomesArray.add(tempNoise);
        }

        terrainProfile = new float[mapWidth+1000][3];
        iCount=0;
        j = 0;
        System.out.println("mapwidth "+mapWidth);
        lastY = 0;

        // Generate terrain profile from noise profiles
        for(i=0;i<mapWidth;i++){
            //System.out.println("i "+i+" and iCount "+iCount+" j "+j+" biomelength "+biomesArray.get(j).length+"bArray size "+biomesArray.size());
            //System.out.println("flatness "+biomesArray.get(j)[0][2]);
            if(j<biomesArray.size()-1){
                // within Perlin Noise array still
                if(iCount>=biomesArray.get(j).length){
                    j++;
                    lastY = terrainProfile[i-1][1];
                    //System.out.println("lasty "+lastY);
                    iCount=0;
                }
                //System.out.println("current bArray value "+biomesArray.get(j)[iCount][1]);
                terrainProfile[i][0]=biomesArray.get(j)[0][0];              // biomes
                terrainProfile[i][2]=biomesArray.get(j)[0][2];              // flatness factor
                terrainProfile[i][1]=biomesArray.get(j)[iCount][1];//lastY+biomesArray.get(j)[iCount][1]*biomesArray.get(j)[0][2]*1000-biomesArray.get(j)[0][2]*1000/2;;    // height
                System.out.println("assigning terrain. biome "+biomesArray.get(j)[iCount][0]+" length "+biomesArray.get(j)[iCount][1]+" and flatness "+biomesArray.get(j)[iCount][2]);
                iCount++;
            } else {
                // Reached end - fill the gap
                terrainProfile[i][1]=biomesArray.get(j)[iCount][1];//*biomesArray.get(j)[0][2]*1000;
            }
        }

        for(j=0;j<biomesArray.size();j++) {
            //System.out.println("biome j " + biomesArray.get(j)[0][0] + " height " + biomesArray.get(j)[0][1] + " flatness "+biomesArray.get(j)[0][2]);
        }

        for(i=0;i<mapWidth;i++) {
            System.out.println("terrain at " + i + " is " + terrainProfile[i][1]+" and biome "+terrainProfile[i][0]+" flatness factor "+terrainProfile[i][2]);
        }

        // Now use loop to make up delta between two world sides
        float initY = terrainProfile[0][1];
        float finalY = terrainProfile[mapWidth-1][1];
        System.out.println("initial "+initY+" final "+finalY);
        float m =(finalY-initY)/800;
        for(i=mapWidth;i<mapWidth+800;i++){
            terrainProfile[i][1] = terrainProfile[i-1][1]-m;
        }
        for(i=mapWidth+800;i<mapWidth+1000;i++){
            terrainProfile[i][1] = initY;
        }
        System.out.println("final 2 "+terrainProfile[mapWidth+999][1]);

        int nHeight = 10;
        tempNoise = perlinFun2(5,terrainProfile.length,2,1,3,4,nHeight);

        for(i=0;i<terrainProfile.length;i++){
            //System.out.println("tempNoise "+tempNoise[i][0]);
            terrainProfile[i][1] = terrainProfile[i][1]+tempNoise[i][0];
        }

    }

    public void genBuilds(int num, int iPos){
        boolean mPos = false;       // Positive gradient
        boolean searching = true;   // boolean for while loop to find available land
        int highestPoint = (int) terrainProfile[iPos][1];
        int pWidth=8;

        if(iPos<2){
            iPos=2;
        }

        int i;

        if((int) terrainProfile[iPos+2][1] > (int) terrainProfile[iPos-2][1]){
            // Positive gradient - ie terrain on right
            //mPos = true;
            iPos = iPos - 2;
        } else {
            // Negative gradient or flat - ie terrain on left or flat
            //mPos = false;
            iPos = iPos + 2;
        }

        // Find land for number of buildings required
        for(i=1;i<pWidth*num;i++){
            if(terrainProfile[iPos+i][1]>highestPoint){
                // New high point
                highestPoint = (int) terrainProfile[iPos+i][1];
            } else {
                // Oldest remains highest
            }
        }

        highestPoint = highestPoint - pWidth + 3;
        int i2=pWidth;
        int j;

        for(j=0;j<pWidth;j++){
            for(i=0-i2;i<pWidth*num+i2;i++){
                mapLayout[iPos+i][highestPoint+j+2] = 13;
            }
            i2--;
        }

        /*while(searching){
            for(i=1;i<6*num;i++){
                if(terrainProfile[iPos+i][1]>terrainProfile[iPos][1]){
                    // Not enough gap
                    searching = true;
                } else {
                    // Enough gap
                    searching = false;
                }
            }
            // If true, not found gap yet. Try moving land upwards

        }

        if(terrainProfile[iPos-2][1]<terrainProfile[iPos+2][1]){
            mPos = true;
        }

        if(mPos){
            // Building right to left

        } else {
            // Building left to right
            // First flatten terrain
            for(i=0;i<6*num;i++) {
                terrainProfile[i][1]=1;
            }
        }*/

    }

    public float[][] perlinFun(int width, int height,int oct,int oct6,int oct7) {
        System.out.println("PERLIN FUN");
        // PXTotal is total width of world
        // Height world at like 10 screens?
        //System.out.println("perly width "+width+" and height "+height);
        //time1 = System.currentTimeMillis();
        /*spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("assets/data/textures/basictextures.png"));

        regions[0] = new TextureRegion(texture,0,0,16,16); //grass
        regions[1] = new TextureRegion(texture,16,0,16,16); //water
        regions[2] = new TextureRegion(texture,0,17,16,16);48 //sand
        regions[3] = new TextureRegion(texture,17,17,16,16); //rock*/

        int oct1 = oct;
        int oct2 = oct;
        int oct3 = oct;
        int oct4 = oct;
        int oct5 = oct;

        float[][] seed = noiseOrig.GenerateWhiteNoise(width, height);

        float[][] seedC = noiseOrig.GenerateSmoothNoise(seed, oct1);

        float[][] seedD = noiseOrig.GenerateSmoothNoise(seedC, oct2);

        float[][] seedE = noiseOrig.GenerateSmoothNoise(seedD, oct3);

        float[][] seedF = noiseOrig.GenerateSmoothNoise(seedE, oct4);

        float[][] seedG = noiseOrig.GenerateSmoothNoise(seedF, oct5);

        float[][] seedH = noiseOrig.GenerateSmoothNoise(seedG, oct6);


        float[][] perlinNoise = noiseOrig.GeneratePerlinNoise(seedH, oct7);

        /*
        String FNAME = "perlinOct"+"PER"+height+".txt";
        ArrayList list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";
        int i;
        int j;

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<height;j++) {
                for (i = 1; i < width; i++) {
                    line1 = line1 + " " + String.valueOf(perlinNoise[i][j]);

                    bw.write(line1);// + "\n");
                    //bw.write("STOP NOW");
                    bw.write("\r\n");
                    line1 = "";
                    //System.out.println("Printing "+i+" + "+j);
                }
            }

            //System.out.println("Printed supposedly");
            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }
*/

        this.perlinNoise = perlinNoise;

        return perlinNoise;
    }

    public float[][] perlinFunOLD(int width,int height){

        System.out.println("PERLIN FUN OLD - SET OCTAVES");

        int oct1 = 1;
        int oct2 = 2;
        int oct3 = 3;
        int oct4 = 4;
        int oct5 = 5;
        int oct6 = 5;
        int oct7 = 5;


        float[][] seed =  noiseOrig.GenerateWhiteNoise(width, height);

        float[][] seedC = noiseOrig.GenerateSmoothNoise( seed, oct1);

        float[][] seedD = noiseOrig.GenerateSmoothNoise( seedC, oct2);

        float[][] seedE = noiseOrig.GenerateSmoothNoise( seedD, oct3);

        float[][] seedF = noiseOrig.GenerateSmoothNoise( seedE, oct4);

        float[][] seedG = noiseOrig.GenerateSmoothNoise( seedF, oct5);

        float[][] seedH = noiseOrig.GenerateSmoothNoise( seedG, oct6);

        float[][] perlinNoise = noiseOrig.GeneratePerlinNoise(seedH, oct7);

        this.perlinNoise = perlinNoise;

        return perlinNoise;
    }

    public float[][] perlinFun2(float lastY, int width, int height,int oct,int oct6,int oct7,int nHeight) {

        System.out.println("PERLIN FUN 2");

        int oct1 = oct;
        int oct2 = oct;
        int oct3 = oct;
        int oct4 = oct;
        int oct5 = oct;

        float[][] seed = noise.GenerateWhiteNoise(width, height,lastY,nHeight);

        float[][] seedC = noise.GenerateSmoothNoise(seed, oct1);

        float[][] seedD = noise.GenerateSmoothNoise(seedC, oct2);

        float[][] seedE = noise.GenerateSmoothNoise(seedD, oct3);

        float[][] seedF = noise.GenerateSmoothNoise(seedE, oct4);

        float[][] seedG = noise.GenerateSmoothNoise(seedF, oct5);

        float[][] seedH = noise.GenerateSmoothNoise(seedG, oct6);


        float[][] perlinNoise = noise.GeneratePerlinNoise(seedH, oct7);

        this.perlinNoise = perlinNoise;

        return perlinNoise;
    }

    public TextureAtlas getMarAt() {
        return marAt;
    }
}
