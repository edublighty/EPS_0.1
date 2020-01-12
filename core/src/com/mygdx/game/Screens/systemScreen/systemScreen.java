package com.mygdx.game.Screens.systemScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HUDs.sideHUD;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.practiceDynTiles;
import com.mygdx.game.Screens.systemScreen.Sprites.traceImage;
import com.mygdx.game.Screens.systemScreen.Sprites.orbLinesSprite;
import com.mygdx.game.Screens.systemScreen.Sprites.planetSpriteGRAVY;
import com.mygdx.game.Screens.systemScreen.Sprites.starSprite;
import com.mygdx.game.Screens.systemScreen.Sprites.sysShipSpriteGRAVY;
import com.mygdx.game.Screens.systemScreen.Stage.buttonOverlay;
import com.mygdx.game.Screens.systemScreen.Stage.systemScreenHUD;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.b2dWorldCreator;
import com.mygdx.game.Screens.systemScreen.Tools.systemGenerator;

public class systemScreen implements Screen {

    private float tStep = 1/60f;

    // Tiled variables
    private TiledMap map;
    private TmxMapLoader mapLoader;
    OrthographicCamera gamecam;
    private Viewport gameport;
    private OrthogonalTiledMapRenderer renderer;
    private ShapeRenderer shapeRenderer;
    private MyGdxGame game;
    private TextureAtlas atlas;             // atlas for pointers
    private TextureAtlas Platlas;           // atlas for planet objects
    private TextureAtlas Statlas;           // atlas for star objects
    private TextureAtlas tilesAt;
    private TextureRegion rect;
    private TextureAtlas starAnim;
    public buttonOverlay buttonoverlay;
    private float posDX;      // Destination X
    private float posDY;      // Destination Y
    private float posCX;      // Current X
    private float posCY;      // Current Y
    private double thetaS;        // angle of travel for player ship
    private float zS = 25;
    private boolean moveFlag;
    private double deltaZ;
    Vector2 centerPosition;
    Vector2 mouseLoc;
    Vector3 mousePos;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // sprites
    public planetSpriteGRAVY[] planets = new planetSpriteGRAVY[9];
    public starSprite starr;
    public orbLinesSprite[] orbline = new orbLinesSprite[9];
    private orbLinesSprite[] planetLines = new orbLinesSprite[9];
    public sysShipSpriteGRAVY player;
    private traceImage dummy;

    // booleans
    public static boolean burnFlag;
    public static boolean planetFlag;
    private boolean moveToPoint;
    public boolean notStarted;
    public boolean autoOrbit;
    private boolean tracing;
    private boolean collision;
    public int orbDir;
    private float planetOrbDia;         // diameter of planet orbit boundary
    private boolean starOnly;           // controls whether star only used for gravity eqns
    private boolean useAccMap;          // uses precalculated gravity map instead of calculating each loop
    private boolean orbitPlanet;        // controls whether single planet only used for gravity eqns
    private int planetNum;              // captures planet number when entering orbit range
    private boolean planetDeburn;       // used to slow player when entering planetary orbit range so dont overshoot
    private float maxOrbSpeed;          // maximum speed allowed when entering orbit of planet
    private int solarM;
    private int planetM1;
    private int G;
    private int gravR;

    // system info
    private double[][] gravData;
    private double[][] planetData;
    private double[][] stellarAccMap;
    private double[] starData;
    private double[][] velComps;
    //private int iCount;
    private int nP;
    public float solarRad;


    public double dR;               // radius of player from star
    public double omega;               // angular speed of orbit
    public double theta;
    public double v;
    public boolean orbButClick;
    private ShapeRenderer sr;
    private double winWidth;
    private double winHeight;
    private int points;
    private double winWorldX;
    private double winWorldY;
    private double scrWidth;
    private double scrHeight;
    private int pathCount;
    private int pathCountLimit;//=(int) (99999*2);
    Array<Vector2> path = new Array<Vector2>();
    int wWid;
    int wHei;

    // Timing variables for render loop
    // last second
    double lastTime;
    //Time-step stuff
    private double accumulator;
    private double currentTime;
    private float step;// = 1f/60f*1000;
    private TimeStep timeStepType;
    public enum TimeStep { FIXED, FIXED_INTERPOLATION, VARIABLE }
    private boolean doPhysics;

    // Viewport variables
    private boolean initialsing;
    private float maxZoomX;
    private float maxZoomY;
    private float minZoomX;
    private float minZoomY;

    // HUD variables
    public systemScreenHUD systemHUD;
    public sideHUD pauseHUD;
    private InputMultiplexer multiplexer;

    // sprite variables
    private float playerVx;
    private float playerVy;
    private float playersX;
    private float playersY;
    private float dummyVx;
    private float dummyVy;
    private float dummysX;
    private float dummysY;

    // Screen booleans
    public boolean paused;

    public systemScreen(MyGdxGame game){

        //bgGenerator starGen = new bgGenerator();

        // update variables
        starOnly = true;
        tracing = true;
        collision = false;
        useAccMap = false;
        timeStepType = TimeStep.VARIABLE;
        currentTime = TimeUtils.millis() / 1000.0;
        doPhysics = false;
        step = 1f/(30f);
        pathCountLimit=(int) (100);
        orbitPlanet = false;
        planetDeburn = false;
        planetOrbDia = 200f / MyGdxGame.PPM;
        solarM = 100;
        planetM1 = 1;
        gravR = 3;      // power of radius in grav equations
        G = 8;

        // initial player conditions
        float startVx = 5f;
        float startVy = -2f;

        // import of assets
        atlas = new TextureAtlas("PNGsPacked/Pointers.atlas");
        Platlas = new TextureAtlas("PNGsPacked/allPlanets.atlas");
        Statlas = new TextureAtlas("PNGsPacked/Stars.atlas");
        starAnim = new TextureAtlas("systemScreen/starPNGs/Randomised/starsAnim.atlas");//conwaysGOL/starsConway.atlas");
        tilesAt = new TextureAtlas("PNGsPacked/biomespack50px.atlas");
        rect = tilesAt.findRegion("pDTTile50Orange");

        // set game variable
        this.game=game;

        map = new TiledMap();// mapLoader.load("map/systems/Syst6.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);
        shapeRenderer = new ShapeRenderer();

        // camera to follow touch and drag
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam) {};

        // world variables
        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        // create game overlays
        systemHUD = new systemScreenHUD(game, game.batch,gamecam.viewportWidth,gamecam.viewportHeight,null);        // CHANGE BACK TO SYSTEMSCREEN
        //buttonoverlay = new buttonOverlay(game.batch,this);
        pauseHUD = new sideHUD(game,gameport.getWorldWidth(),gameport.getWorldHeight());

        // set contact listeners for objects
        world.setContactListener(new WorldContactListener());

        this.notStarted = true;

        //gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(systemHUD.stage);
        multiplexer.addProcessor(pauseHUD.stage);
        multiplexer.addProcessor(new InputAdapter() {
            int offsetX;
            int offsetY;
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                offsetX = screenX;
                offsetY = screenY;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                //gamecam.translate(offsetX, offsetY);
                touchDragged2(screenX,screenY);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                offsetX = 0;
                offsetY = 0;
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);

        // Initialisation of system
        systemGenerator sGen = new systemGenerator(game);
        sGen.planetGen();
        solarRad = sGen.toteSize;
        planetData = sGen.getPlanetData();
        stellarAccMap = new double[200][2];
        sGen.starGen();
        starData = sGen.getStarData();
        nP = planetData[0].length;
        gravData = new double[nP+1][4];
        gravData[0][0] = sGen.getStarData()[3];
        gravData[0][1] = solarRad;
        gravData[0][2] = solarRad;
        paused = false;

        velComps = new double[4][15];
        //iCount=0;

        points = 50;

        burnFlag = false;
        planetFlag = false;

        // load map
        map = new TiledMap();
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);

        // initialise gamecam centrally
        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        wWid = (int) gameport.getWorldWidth();
        wHei = (int) gameport.getWorldHeight();
        //buttonoverlay.stage.getCamera().position.y = 100;

        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        new b2dWorldCreator(world,map,this,planetData,null,0,0,0,0);

        float toteSize = sGen.toteSize;//(750*(nP+1)+500)/2;
        // create sprite in game world
        player = new sysShipSpriteGRAVY(world, null,toteSize);  // CHANGE BACK TO SYSTEMSCREEN
        //dummy = new traceImage(world, null,toteSize);   // CHANGE BACK TO SYSTEMSCREEN

        int i = 0;
        int locX;// = 5750/2-size/2;
        int locY;// = (int) (5750/2 +  (Math.pow(-1,i))*((i)*375+750))-size/2;
        int size;

        maxZoomX = 0;
        maxZoomY = 0;

        while(i<nP) {
            String stelObj = "terrs";
            int pType = (int) planetData[0][i];
            size = (int) planetData[1][i];
            switch (pType) {
                default:
                    break;
                case 0:
                    // terrestrial
                    stelObj = "terr"+Integer.toString(1);
                    break;
                case 1:
                    // gaseous terrestrial
                    stelObj = "terGas"+Integer.toString(1);
                    break;
                case 2:
                    // gas giant
                    stelObj = "gasGi"+Integer.toString(1);
                    break;
            }
            planets[i] = new planetSpriteGRAVY(world,null,stelObj,planetData,i,wWid,wHei);  // CHANGE BACK TO SYSTEMSCREEN
            gravData[i+1][0] = 0.01;
            gravData[i+1][1] = planetData[2][i];
            gravData[i+1][2] = planetData[3][i];
            orbline[i] = new orbLinesSprite(world,null,Math.round(planetData[8][i])/MyGdxGame.PPM,toteSize,toteSize);   // CHANGE BACK TO SYSTEMSCREEN
            planetLines[i] = new orbLinesSprite(world,null,planetOrbDia,(float) planetData[2][i],(float) planetData[3][i]); // CHANGE BACK TO SYSTEMSCREEN

            wWid = (int) gameport.getWorldWidth();
            wHei = (int) gameport.getWorldHeight();

            if(toteSize>maxZoomX){
                maxZoomX = solarRad*2;
            }

            if(toteSize>maxZoomY){
                maxZoomY = toteSize;
            }
            i++;
        }

        maxZoomX = solarRad*2*1.25f;
        maxZoomY = solarRad*2*1.25f;

        int sType = 1; //(int) starData[0];
        String sObj = "star"+Integer.toString(sType);

        // set star data
        starr = new starSprite(world, null, sObj, nP, wWid, wHei,(int) gravData[0][0]); // CHANGE BACK TO SYSTEMSCREEN

        //planets[0].setPosition(200,200);
        //planets[1].setPosition(200,100);

        // set contact listeners for objects
        world.setContactListener(new WorldContactListener());

        this.notStarted = true;
        autoOrbit = false;

        if(autoOrbit){
            dR = Math.sqrt(Math.pow(toteSize/MyGdxGame.PPM-player.b2body.getPosition().x,2)+Math.pow(toteSize/MyGdxGame.PPM-player.b2body.getPosition().y,2));
            omega = 2*Math.PI/(15);         // deg/s
            theta = 0;
            v = omega*dR;
            //omega = omega*180/Math.PI;
        }
        player.b2body.setLinearVelocity(new Vector2(startVx,startVy));

        float currentVx = player.b2body.getLinearVelocity().x;
        float currentVy = player.b2body.getLinearVelocity().y;

        genAccMap();
/*
        gamecam.viewportWidth = maxZoomX * 0.1f;
        gamecam.viewportHeight = maxZoomY * 0.1f;*/

    }

    // Generate map of accelerations according to player position relative to stellar bodies
    public void genAccMap(){
        int i;
        int j;
        int k;
        int G=15;

        for(i=-100;i<100;i++){
            double r;
            double Ax;
            double Ay;
            if(starOnly) {
                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - i, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - i, 2));
                Ax = G * -(i - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                Ay = G * -(i - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
            } else {
                Ax = 0;
                Ay = 0;
                for(k=0;k<nP;k++){
                    r = Math.sqrt(Math.pow(gravData[k+1][1] / MyGdxGame.PPM - i, 2) + Math.pow(gravData[k+1][2] / MyGdxGame.PPM - i, 2));
                    Ax = Ax + (G * -(i - gravData[k+1][1] / MyGdxGame.PPM) * (gravData[k+1][1] / MyGdxGame.PPM) / Math.pow(r, 3));
                    Ay = Ay + (G * -(i - gravData[k+1][2] / MyGdxGame.PPM) * (gravData[k+1][2] / MyGdxGame.PPM) / Math.pow(r, 3));
                }
                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - i, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - i, 2));
                Ax = Ax + G * -(i - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                Ay = Ay + G * -(i - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
            }
            stellarAccMap[i+100][0] = Ax;
            stellarAccMap[i+100][1] = Ay;
        }
    }

    public void getTrace(float dt) {

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth*gamecam.zoom;
        scrHeight = (int) gamecam.viewportHeight*gamecam.zoom;
        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen
        dt = dt / 600;
        collision = false;

        boolean buffer1 = false;
        boolean buffer2 = true;

        if (orbitPlanet) {
            double initX = 0;
            double initY = 0;
            double sX = player.b2body.getPosition().x;
            double sY = player.b2body.getPosition().y;
            double pX = 0;
            double pY = 0;
            double Vx = player.b2body.getLinearVelocity().x;
            double Vy = player.b2body.getLinearVelocity().y;
            double VxNew;
            double VyNew;
            path.clear();
            pathCount = 0;

            while(buffer2){
                // cycle through points predicting player movement
                double r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - sY, 2));
                double Ax = G * planetM1 * -(sX - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                double Ay = G * planetM1 * -(sY - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                sX = sX + pX;
                sY = sY + pY;
                path.add(new Vector2((float) (winWidth / 2 + (initX) * winWorldX), (float) (winHeight / 2 + (initY) * winWorldY)));
                if (!collision) {
                    collision = inCircle(gravData[planetNum][1] / MyGdxGame.PPM, gravData[planetNum][2] / MyGdxGame.PPM, gravData[planetNum][0] / MyGdxGame.PPM, sX, sY);
                }
                VxNew = (Vx + Ax * dt);                    // new velocity at end of timestep
                pX = (VxNew) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                VyNew = (Vy + Ay * dt);                    // new velocity at end of timestep
                pY = (VyNew) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                Vx = VxNew;
                Vy = VyNew;
                initX = initX + pX;
                initY = initY + pY;
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
            }
        } else {
            double initX = 0;
            double initY = 0;
            double sX = player.b2body.getPosition().x;
            double sY = player.b2body.getPosition().y;
            double pX = 0;
            double pY = 0;
            double Vx = player.b2body.getLinearVelocity().x;
            double Vy = player.b2body.getLinearVelocity().y;
            double VxNew;
            double VyNew;
            path.clear();

            pathCount=0;
            while(buffer2){//pathCount<pathCountLimit){
                // cycle through points predicting player movement
                double r;
                double Ax = 0;
                double Ay = 0;
                if(!starOnly) {
                    for (int i = 0; i < nP; i++) {
                        r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - sY, 2));
                        Ax = Ax + (G * planetM1 * -(sX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        Ay = Ay + (G * planetM1 * -(sY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        if (!collision) {
                            collision = inCircle(gravData[i + 1][1] / MyGdxGame.PPM, gravData[i + 1][2] / MyGdxGame.PPM, gravData[i + 1][0] / MyGdxGame.PPM, sX, sY);
                        }
                    }
                }
                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                Ax = Ax + G * solarM * -(sX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                Ay = Ay + G * solarM * -(sY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                sX = sX + pX;
                sY = sY + pY;
                path.add(new Vector2((float) (winWidth / 2 + (initX) * winWorldX), (float) (winHeight / 2 + (initY) * winWorldY)));
                if(!collision) {
                    collision = inCircle(gravData[0][1] / MyGdxGame.PPM, gravData[0][2] / MyGdxGame.PPM, gravData[0][0] / MyGdxGame.PPM, sX, sY);
                }
                VxNew = (Vx + Ax * dt);                    // new velocity at end of timestep
                pX = (VxNew) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                VyNew = (Vy + Ay * dt);                    // new velocity at end of timestep
                pY = (VyNew) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                Vx = VxNew;
                Vy = VyNew;
                initX = initX + pX;
                initY = initY + pY;
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
            }
        }
    }

    public void getTrace2(float dt) {

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth*gamecam.zoom;
        scrHeight = (int) gamecam.viewportHeight*gamecam.zoom;
        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen
        dt = dt;// / 500;
        collision = false;

        boolean buffer1 = false;
        boolean buffer2 = true;

        if (orbitPlanet) {
            double initX = 0;
            double initY = 0;
            double sX = player.b2body.getPosition().x;
            double sY = player.b2body.getPosition().y;
            double pX = 0;
            double pY = 0;
            double Vx = player.b2body.getLinearVelocity().x;
            double Vy = player.b2body.getLinearVelocity().y;
            double VxNew;
            double VyNew;
            path.clear();
            pathCount = 0;

            while(buffer2){
                // cycle through points predicting player movement
                double r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - sY, 2));
                double Ax = G * planetM1 * -(sX - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                double Ay = G * planetM1 * -(sY - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                //sX = sX + pX;
                //sY = sY + pY;
                path.add(new Vector2((float) sX, (float) sY));
                /*if (!collision) {
                    collision = inCircle(gravData[planetNum][1] / MyGdxGame.PPM, gravData[planetNum][2] / MyGdxGame.PPM, gravData[planetNum][0] / MyGdxGame.PPM, sX, sY);
                }*/
                VxNew = (Vx + Ax * dt);                    // new velocity at end of timestep
                pX = (VxNew) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                VyNew = (Vy + Ay * dt);                    // new velocity at end of timestep
                pY = (VyNew) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                Vx = VxNew;
                Vy = VyNew;
                sX = sX + pX;
                sY = sY + pY;
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
            }
        } else {
            double initX = 0;
            double initY = 0;
            double sX = player.b2body.getPosition().x;
            double sY = player.b2body.getPosition().y;
            double pX = 0;
            double pY = 0;
            double Vx = player.b2body.getLinearVelocity().x;
            double Vy = player.b2body.getLinearVelocity().y;
            double VxNew;
            double VyNew;
            path.clear();
            boolean tempBool = false;

            pathCount=0;
            while(buffer2){//pathCount<pathCountLimit){
                // cycle through points predicting player movement
                double r;
                double Ax = 0;
                double Ay = 0;
                if(!starOnly) {
                    for (int i = 0; i < nP; i++) {
                        r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - sY, 2));
                        Ax = Ax + (G * planetM1 * -(sX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        Ay = Ay + (G * planetM1 * -(sY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        /*if (!collision) {
                            collision = inCircle(gravData[i + 1][1] / MyGdxGame.PPM, gravData[i + 1][2] / MyGdxGame.PPM, gravData[i + 1][0] / MyGdxGame.PPM, sX, sY);
                        }*/
                        if(!tempBool) {
                            System.out.println("trace "+System.nanoTime()+" planet " + i + " " + Ax + " by " + Ay);
                        }
                    }
                }
                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                Ax = Ax + G * solarM * -(sX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                Ay = Ay + G * solarM * -(sY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                if(!tempBool) {
                    System.out.println("trace "+System.nanoTime()+" star " + Ax + " by " + Ay);
                    System.out.println(" ");
                }
                path.add(new Vector2((float) sX, (float) sY));
                /*if(!collision) {
                    collision = inCircle(gravData[0][1] / MyGdxGame.PPM, gravData[0][2] / MyGdxGame.PPM, gravData[0][0] / MyGdxGame.PPM, sX, sY);
                }*/
                VxNew = (Vx + Ax * dt);                    // new velocity at end of timestep
                pX = (VxNew) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                VyNew = (Vy + Ay * dt);                    // new velocity at end of timestep
                pY = (VyNew) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                Vx = VxNew;
                Vy = VyNew;
                sX = sX + pX;
                sY = sY + pY;
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
                if(pathCount>2){
                    tempBool = true;
                }
            }
        }
    }

    public void getTrace3(float dt) {

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth*gamecam.zoom;
        scrHeight = (int) gamecam.viewportHeight*gamecam.zoom;
        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen
        dt = dt;// / 500;
        collision = false;

        boolean buffer2 = true;

        dummy.b2body.setTransform(player.b2body.getPosition().x,player.b2body.getPosition().y,0);
        //dummy.b2body.setLinearVelocity(player.b2body.getLinearVelocity());

        double sX;
        double sY;
        float Vx = player.b2body.getLinearVelocity().x;
        float Vy = player.b2body.getLinearVelocity().y;
        double VxNew;
        double VyNew;
        path.clear();
        pathCount = 0;

        if (orbitPlanet) {


            while(buffer2){
                // cycle through points predicting player movement
                sX = dummy.b2body.getPosition().x;
                sY = dummy.b2body.getPosition().y;
                double r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - sY, 2));
                double Ax = G * planetM1 * -(sX - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                double Ay = G * planetM1 * -(sY - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                //sX = sX + pX;
                //sY = sY + pY;
                path.add(new Vector2((float) sX, (float) sY));
                /*if (!collision) {
                    collision = inCircle(gravData[planetNum][1] / MyGdxGame.PPM, gravData[planetNum][2] / MyGdxGame.PPM, gravData[planetNum][0] / MyGdxGame.PPM, sX, sY);
                }*/
                Vx = (float) (player.b2body.getLinearVelocity().x + Ax * dt);
                Vy = (float) (player.b2body.getLinearVelocity().y + Ay * dt);
                dummy.b2body.setLinearVelocity(new Vector2(Vx, Vy));
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
                world.step(dt,6,2);
                //dummy.update((float) dt);
            }
        } else {
            boolean tempBool = true;

            while(buffer2){//pathCount<pathCountLimit){
                // cycle through points predicting player movement
                double r;
                double Ax = 0;
                double Ay = 0;
                dummyVx = dummy.b2body.getLinearVelocity().x;
                dummyVy = dummy.b2body.getLinearVelocity().y;
                dummysX = dummy.b2body.getPosition().x;
                dummysY = dummy.b2body.getPosition().y;
                sX = dummy.b2body.getPosition().x;
                sY = dummy.b2body.getPosition().y;
                if(!starOnly) {
                    for (int i = 0; i < nP; i++) {
                        r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - sY, 2));
                        Ax = Ax + (G * planetM1 * -(sX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        Ay = Ay + (G * planetM1 * -(sY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        /*if (!collision) {
                            collision = inCircle(gravData[i + 1][1] / MyGdxGame.PPM, gravData[i + 1][2] / MyGdxGame.PPM, gravData[i + 1][0] / MyGdxGame.PPM, sX, sY);
                        }*/
                        if(!tempBool) {
                            System.out.println("trace "+System.nanoTime()+" planet " + i + " " + Ax + " by " + Ay);
                        }
                    }
                }
                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                Ax = Ax + G * solarM * -(sX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                Ay = Ay + G * solarM * -(sY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                if(!tempBool) {
                    System.out.println("trace "+System.nanoTime()+" star " + Ax + " by " + Ay);
                    System.out.println(" ");
                }
                path.add(new Vector2((float) sX, (float) sY));
                /*if(!collision) {
                    collision = inCircle(gravData[0][1] / MyGdxGame.PPM, gravData[0][2] / MyGdxGame.PPM, gravData[0][0] / MyGdxGame.PPM, sX, sY);
                }*/
                Vx = (float) (dummy.b2body.getLinearVelocity().x + Ax * dt);
                Vy = (float) (dummy.b2body.getLinearVelocity().y + Ay * dt);
                dummy.b2body.setLinearVelocity(new Vector2(Vx, Vy));
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
                if(pathCount>2){
                    tempBool = true;
                }
                world.step(dt,6,2);
                //dummy.update((float) dt);
            }
        }
    }

    public void setAutoOrbit(){
        // Auto orbit most gravitationally-strong stellar body
        if(autoOrbit==true){
            autoOrbit=false;
        } else {
            // start auto orbitting
            autoOrbit=true;
            dR = Math.sqrt(Math.pow(solarRad/MyGdxGame.PPM-player.b2body.getPosition().x,2)+Math.pow(solarRad/MyGdxGame.PPM-player.b2body.getPosition().y,2));
            omega = 2*Math.PI/(15);         // deg/s
            theta = Math.atan((player.b2body.getPosition().x-solarRad/MyGdxGame.PPM)/(player.b2body.getPosition().y-solarRad/MyGdxGame.PPM));
            if(player.b2body.getPosition().x>solarRad/MyGdxGame.PPM){
                if(player.b2body.getPosition().y>solarRad/MyGdxGame.PPM){
                    // Top Right
                    // Theta = theta so piss off
                } else {
                    // Bottom right
                    theta = Math.PI - Math.abs(theta);
                }
                orbDir = (int) (-player.b2body.getLinearVelocity().y/Math.abs(player.b2body.getLinearVelocity().y));
            } else {
                if(player.b2body.getPosition().y>solarRad/MyGdxGame.PPM){
                    // Top Left
                    theta = 2*Math.PI - Math.abs(theta);
                } else {
                    // Bottom Left
                    theta = Math.PI + Math.abs(theta);
                }
                if(player.b2body.getLinearVelocity().x>0) {
                    if (player.b2body.getPosition().y < solarRad / MyGdxGame.PPM) {
                        // anticlockwise
                        orbDir = (int) -1;//(-player.b2body.getLinearVelocity().y / Math.abs(player.b2body.getLinearVelocity().y));
                    } else {
                        // clockwise
                        orbDir = (int) 1;//(player.b2body.getLinearVelocity().y / Math.abs(player.b2body.getLinearVelocity().y));
                    }
                } else if(player.b2body.getLinearVelocity().x<0) {
                    if (player.b2body.getPosition().y < solarRad / MyGdxGame.PPM) {
                        // clockwise
                        orbDir = (int) 1;//(player.b2body.getLinearVelocity().y / Math.abs(player.b2body.getLinearVelocity().y));
                    } else {
                        // anticlockwise
                        orbDir = (int) -1;//(-player.b2body.getLinearVelocity().y / Math.abs(player.b2body.getLinearVelocity().y));
                    }
                } else {
                }
            }
            theta = 2*Math.PI + theta;
            v = omega*dR;
        }
    }

    public boolean inCircle(double planetX, double planetY, double planetRad, double posX, double posY){
        boolean inCircleBool = false;

        double circleRad = Math.pow((posX - planetX),2) + Math.pow((posY - planetY),2);

        if(circleRad<Math.pow(planetRad,2)){
            // player will land within planet circle
            inCircleBool = true;
        }

        return inCircleBool;
    }

    public Vector2 calcEscapeVel(double dX, double dY, double r){
        // method to calculate velocity for planetary orbit
        int ome;
        if(dY>0){
            // player "above" planet
            if(player.b2body.getLinearVelocity().x>0){
                // travelling right and above so clockwise
                ome = 1;
            } else {
                // travelling left and above so anticlockwise
                ome = -1;
            }
        } else if(dY<0){
            // player "below" planet
            if(player.b2body.getLinearVelocity().x>0){
                // travelling right and below so anticlockwise
                ome = -1;
            } else {
                // travelling left and below so clockwise
                ome = 1;
            }
        } else {
            // player in line with planet, how unlikely was that?
            if(dX>0) {
                // player right of planet
                if (player.b2body.getLinearVelocity().y > 0) {
                    // right and travelling up so anticlockwise
                    ome = -1;
                } else {
                    // right and travelling down so clockwise
                    ome = 1;
                }
            } else {
                // player left of planet
                if(player.b2body.getLinearVelocity().y>0){
                    // left and travelling up so clockwise
                    ome = 1;
                } else {
                    // left and travelling down so anticlockwise
                    ome = -1;
                }
            }
        }
        double V = Math.sqrt(G*planetM1/r);
        float Vx = (float) (V*(dY/r)*(1)*ome);
        float Vy = (float) (V*(dX/r)*(-1)*ome);

        Vector2 vec = new Vector2();
        vec.x = Vx;
        vec.y = Vy;
        return vec;
    }

    // atlas for pointers
    public TextureAtlas getAtlas() {
        return atlas;
    }

    // atlas for planets
    public TextureAtlas getPlAtlas(){
        return Platlas;
    }

    // atlas for planets
    public TextureAtlas getStAtlas(){
        return Statlas;
    }

    // atlas for planets
    public TextureAtlas getStAnim(){
        return starAnim;
    }

    @Override
    public void show () {
        System.out.println("play show");
        System.out.println("play show - created camera");
    }

    @Override
    public void render (float delta) {

       /* if(initialsing){
            gamecam.viewportWidth = maxZoomX;
            gamecam.viewportHeight = maxZoomY;
            initialsing = false;
        }*/

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        //renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        int i = 0;
        while(i<nP){
            planets[i].draw(game.batch);
            i++;
        }
        starr.draw(game.batch);
        System.out.println(pathCount);
        for(i=0;i<pathCount-1;i++) {
            float x1 = path.get(i).x;
            float x2 = path.get(i+1).x;
            float y1 = path.get(i).y;
            float y2 = path.get(i+1).y;
            float dx = 0.1f;//(x2 - x1)*5;
            float dy = 0.1f;//(y2-y1)*5;
            /*
            float dist = (float)Math.sqrt(dx*dx + dy*dy);
            float rad = (float)Math.atan2(dy, dx);*/
            game.batch.draw(rect, x1-dx/2, y1-dy/2,dx, dy);
        }
        //game.batch.draw(rect)
        game.batch.end();
/*
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if(collision) {
            shapeRenderer.setColor(1, 0, 0, 1);
        } else {
            shapeRenderer.setColor(1, 1, 1, 1);
        }
        for(i=0;i<pathCount-1;i++) {
            //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(path.get(i),path.get(i+1));
            //shapeRenderer.end();
        }
        shapeRenderer.end();*/

        // draw HUDs here
        /*game.batch.setProjectionMatrix(galActors.stage.getCamera().combined);
        galActors.stage.draw();*/

        if(paused) {
            game.batch.setProjectionMatrix(pauseHUD.stage.getCamera().combined);
            pauseHUD.stage.draw();
        }
    }

    @Override
    public void resize (int width, int height) {

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth;
        scrHeight = (int) gamecam.viewportHeight;
        System.out.println("initial veiwport "+scrWidth+" and "+scrHeight);

        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen

        gameport.update(width,height);
        gamecam.update();
        // update HUDs here
        //galHUD.viewport.update(width,height);
    }

    @Override
    public void hide () {
        dispose();
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void dispose () {
        // dispose / clear memory?
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }

    public void handleInput(float dt){
        // THIS IS WHERE TOUCH DRAG WILL OCCUR (no its not)
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){                         // destination on screen Y
            if(gamecam.viewportWidth<maxZoomX && gamecam.viewportHeight<maxZoomY) {
                //gamecam.viewportWidth=gamecam.viewportWidth*1.01f;
                System.out.println("before "+gamecam.viewportWidth+" by "+gamecam.viewportHeight+" and zoom "+gamecam.zoom);
                gamecam.zoom+=0.02f;
                System.out.println("after "+gamecam.viewportWidth+" by "+gamecam.viewportHeight);
                //gamecam.viewportHeight=(float) (gamecam.viewportWidth*winWorldX/winWorldY);
                //player.setBounds(0,0,player.getWidth()*1.01f,player.getHeight()*1.01f);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            if(gamecam.viewportWidth>minZoomX && gamecam.viewportHeight>minZoomY) {
                //gamecam.viewportWidth = gamecam.viewportWidth * 0.99f;
                //gamecam.viewportHeight = (float) (gamecam.viewportWidth*winWorldX/winWorldY);
                System.out.println("before "+gamecam.viewportWidth+" by "+gamecam.viewportHeight);
                gamecam.zoom -= 0.02f;
                System.out.println("after "+gamecam.viewportWidth+" by "+gamecam.viewportHeight);
                //player.setBounds(0,0,player.getWidth()*0.99f,player.getHeight()*0.99f);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            Vector2 vec = new Vector2();
            vec.x = 1;
            vec.y = 2;
            player.b2body.setLinearVelocity(vec);
        }

        if(Gdx.input.isTouched()) {
            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gamecam.unproject(mousePos); // mousePos is now in world coordinates
            double X = player.b2body.getPosition().x - mousePos.x;
            double Y = player.b2body.getPosition().y - mousePos.y;
            double r = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
            float vX = (float) (0.3 * X / r);
            float vY = (float) (0.3 * Y / r);
            Vector2 vector = new Vector2(vX + player.b2body.getLinearVelocity().x, vY + player.b2body.getLinearVelocity().y);
            player.b2body.setLinearVelocity(vector);
        }

    }

    public boolean touchDragged2(int screenX, int screenY){//, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();
        gamecam.translate(-x,y);
        return true;
    }

    public void update(float dt){
        handleInput(dt);

        //world.step(1/60f,6,2);  //timeStep 60 per second
        accumulator += dt;

        double frameTime = 0;
        double newTime = TimeUtils.millis() / 1000.0;
        frameTime = newTime - currentTime;
        currentTime = newTime;
        float physTime = step;
        float newVx;
        float newVy;

        if(accumulator >= step){
            doPhysics = true;
            accumulator -= step;
        }

        //doPhysics = true;

        int i;

        if(burnFlag){
            // applying burning to player and hud
        }

        /*if(planetFlag) {
            // player over planet
            //System.out.println("trying to raise it: " + buttonoverlay.stage.getCamera().position.y);
            if(buttonoverlay.stage.getCamera().position.y > 150) {
                //buttonoverlay.stage.getCamera().position.y = buttonoverlay.stage.getCamera().position.y - 1;
            } else {
                // stage in position
            }
        } else {
            // player not over planet
            //System.out.println("trying to lower it: " + buttonoverlay.stage.getCamera().position.y);
            if (buttonoverlay.stage.getCamera().position.y < 180) {
                //buttonoverlay.stage.getCamera().position.y = buttonoverlay.stage.getCamera().position.y + 1;
            }
        }*/

        boolean tempBool = false;
        //doPhysics = false;

        if(doPhysics) {
                if (autoOrbit) {
                    // see calcEscapeVel
                    Vector2 vec2 = new Vector2((float) (/*Math.pow(-1,orbDir)**/(orbDir * v * Math.cos(theta))), (float) (/*Math.pow(-1,orbDir)**/-orbDir * v * Math.sin(theta)));
                    player.b2body.setLinearVelocity(vec2);
                    if (theta < 2 * Math.PI) {
                        theta = theta + 2 * Math.PI;
                    }
                    theta = (theta + orbDir * omega * dt);
                } else {
                    // do the solar system thing
                        if (useAccMap) {
                            Vector2 As = interpolateAcc(player.b2body.getPosition().x, player.b2body.getPosition().y);
                            double Ax = As.x;
                            double Ay = As.y;
                            float Vx = (float) (player.b2body.getLinearVelocity().x + Ax * dt);
                            float Vy = (float) (player.b2body.getLinearVelocity().y + Ay * dt);
                            Vector2 vec2 = new Vector2(Vx, Vy);
                            player.b2body.setLinearVelocity(vec2);
                        } else {
                            if (starOnly) {
                                dt = (float) frameTime;
                                double r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                                //double Ax = G * -(player.b2body.getPosition().x - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                                //double Ay = G * -(player.b2body.getPosition().y - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
                                double Ax = G * solarM*-(player.b2body.getPosition().x - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                                double Ay = G * solarM*-(player.b2body.getPosition().y - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                                float Vx = (float) (player.b2body.getLinearVelocity().x + Ax * physTime);
                                float Vy = (float) (player.b2body.getLinearVelocity().y + Ay * physTime);
                                Vector2 vec2 = new Vector2(Vx, Vy);
                                player.b2body.setLinearVelocity(vec2);
                            } else {
                                if (orbitPlanet) {
                                    dt = (float) frameTime;
                                    double r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                                    double Ax = G * planetM1*-(player.b2body.getPosition().x - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                                    double Ay = G * planetM1*-(player.b2body.getPosition().y - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                                    float Vx = (float) (player.b2body.getLinearVelocity().x + Ax * physTime);
                                    float Vy = (float) (player.b2body.getLinearVelocity().y + Ay * physTime);
                                    if(planetDeburn){
                                        G=G/2;
                                        Vector2 orbVel = calcEscapeVel((player.b2body.getPosition().x - gravData[planetNum][1] / MyGdxGame.PPM),(player.b2body.getPosition().y - gravData[planetNum][2] / MyGdxGame.PPM),r);
                                        Vx = orbVel.x;
                                        Vy = orbVel.y;
                                        if(player.b2body.getLinearVelocity().x>orbVel.x || player.b2body.getLinearVelocity().y>orbVel.y){
                                            if(player.b2body.getLinearVelocity().x>orbVel.x) {
                                                Vx = orbVel.x;
                                                // Vx = Vx * 0.95f;
                                                planetDeburn = true;
                                            } else if(player.b2body.getLinearVelocity().y>orbVel.y) {
                                                //Vy = Vy * 0.95f;
                                                Vy = orbVel.y;
                                                planetDeburn = true;
                                            } else {
                                                planetDeburn = false;
                                            }
                                        }
                                        if(player.b2body.getLinearVelocity().x<orbVel.x && player.b2body.getLinearVelocity().y<orbVel.y){
                                            Vx = orbVel.x;
                                            Vy = orbVel.y;
                                        }
                                        planetDeburn = false;
                                    }
                                    Vector2 vec2 = new Vector2(Vx, Vy);
                                    player.b2body.setLinearVelocity(vec2);

                                    if(inCircle(gravData[planetNum][1] / MyGdxGame.PPM, gravData[planetNum][2] / MyGdxGame.PPM, gravData[planetNum][0] / MyGdxGame.PPM, player.b2body.getPosition().x, player.b2body.getPosition().y)){
                                        // transfer to planet screen
                                        game.setScreen(new practiceDynTiles(game));
                                    }

                                } else {
                                dt = (float) frameTime;
                                double r;
                                double Ax = 0;
                                double Ay = 0;
                                for (i = 0; i < nP; i++) {
                                    r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                                    //System.out.println("r is " + r);
                                    if (r < planetOrbDia) {
                                        // within orbit of planet
                                        orbitPlanet = true;
                                        planetNum = i+1;
                                        planetDeburn = true;
                                    }
                                    //Ax = Ax + (G * -(player.b2body.getPosition().x - gravData[i + 1][1] / MyGdxGame.PPM) * (gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, 3));
                                    //Ay = Ay + (G * -(player.b2body.getPosition().y - gravData[i + 1][2] / MyGdxGame.PPM) * (gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, 3));
                                    Ax = Ax + (G * planetM1 * -(player.b2body.getPosition().x - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                                    Ay = Ay + (G * planetM1 * -(player.b2body.getPosition().y - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                                    if(!tempBool) {
                                        System.out.println("update "+System.nanoTime()+" planet " + i + " " + Ax + " by " + Ay);
                                    }
                                }
                                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                                Ax = Ax + G * solarM * -(player.b2body.getPosition().x - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                                Ay = Ay + G * solarM * -(player.b2body.getPosition().y - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                                if(!tempBool) {
                                    System.out.println("update "+System.nanoTime()+" star " + Ax + " by " + Ay);
                                }
                                float Vx = (float) (player.b2body.getLinearVelocity().x + Ax * dt);
                                float Vy = (float) (player.b2body.getLinearVelocity().y + Ay * dt);
                                Vector2 vec2 = new Vector2(Vx, Vy);
                                player.b2body.setLinearVelocity(vec2);
                            }
                            }
                        }
                }
            world.step((float) physTime, 6, 2);
            player.update((float) physTime);
            playerVx = player.b2body.getLinearVelocity().x;
            playerVy = player.b2body.getLinearVelocity().y;
            playersX = player.b2body.getPosition().x;
            playersY = player.b2body.getPosition().y;
            Vector2 tempVec = player.b2body.getLinearVelocity();
            dummy.b2body.setLinearVelocity(player.b2body.getLinearVelocity());
            player.b2body.setLinearVelocity(0,0);
            if(tracing) {
                getTrace3(physTime);
                //tracing = false;
            }
            player.b2body.setLinearVelocity(tempVec);
        }

        doPhysics = false;

        newVx = player.b2body.getLinearVelocity().x;
        newVy = player.b2body.getLinearVelocity().y;

/*
        double oldTheta = Math.atan(currentVy/currentVx);
        float newTheta = (float) Math.atan(newVy/newVx);

        float difTheta =(float) ((newTheta - oldTheta)*180/Math.PI);*/

        //player.setOrigin(player.b2body.getPosition().x+player.getWidth()/2,player.b2body.getPosition().y+player.getHeight()/2);
        //player.setOriginCenter();
        //player.setRotation(newTheta);
        //player.rotate(difTheta);

        //player.update((float) step);

        // Get camera to follow sprite x and y movements
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        starr.update(dt);

        // update camera and render
        gamecam.update();
        renderer.setView(gamecam);
    }

    public Vector2 interpolateAcc(double dx,double dy){
        Vector2 ans = new Vector2();
        int x1 = (int) Math.floor(dx);
        int x2 = (int) Math.ceil(dx);
        int y1 = (int) Math.floor(dy);
        int y2 = (int) Math.ceil(dy);
        double Ax1 = stellarAccMap[x1+100][0];
        double Ax2 = stellarAccMap[x2+100][0];
        double Ay1 = stellarAccMap[y1+100][1];
        double Ay2 = stellarAccMap[y2+100][1];

        ans.x = (float) (Ax1 + ((Ax2-Ax1)/(x2-x1))*(dx-x1));
        ans.y = (float) (Ay1 + ((Ay2-Ay1)/(y2-y1))*(dy-y1));
        return ans;
    }

}
