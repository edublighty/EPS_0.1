package com.mygdx.game.Screens.systemScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HUDs.sideHUD;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.Sprites.backImage;
import com.mygdx.game.Screens.systemScreen.Sprites.polygonImage;
import com.mygdx.game.Screens.systemScreen.Sprites.systemScreenShipGroup;
import com.mygdx.game.Screens.systemScreen.Sprites.traceImage;
import com.mygdx.game.Screens.systemScreen.Sprites.orbLinesSprite;
import com.mygdx.game.Screens.systemScreen.Sprites.planetImage;
import com.mygdx.game.Screens.systemScreen.Sprites.playerImage;
import com.mygdx.game.Screens.systemScreen.Sprites.starImage;
import com.mygdx.game.Screens.systemScreen.Stage.nearProxStage;
import com.mygdx.game.Screens.systemScreen.Stage.systemScreenActors;
import com.mygdx.game.Screens.systemScreen.Stage.worldStage;
import com.mygdx.game.Screens.systemScreen.Tools.Vertex;
import com.mygdx.game.Screens.systemScreen.Tools.greeble.greebleGenerator;
import com.mygdx.game.Screens.systemScreen.Tools.roomGenerators.engRoomGenerator;
import com.mygdx.game.Screens.systemScreen.Tools.systemGenerator;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.b2dWorldCreator;
import com.mygdx.game.Tools.Managers.shipManager;

import java.awt.image.BufferedImage;
import java.io.File;

public class systemScreen2 implements Screen {

    // physics variables
    private float tStep = 1/60f;

    // camera and window variables
    private float windowWidth;
    private float windowHeight;

    // Tiled variables
    private TiledMap map;
    private TmxMapLoader mapLoader;
    public OrthographicCamera gamecam;
    private Viewport gameport;
    private OrthogonalTiledMapRenderer renderer;
    private MyGdxGame game;
    private TextureAtlas atlas;             // atlas for pointers
    private TextureAtlas Platlas;           // atlas for planet objects
    private TextureAtlas Statlas;           // atlas for star objects
    private TextureAtlas starAnim;
    private TextureAtlas thrustAt;
    private TextureAtlas healthAt;
    private TextureAtlas shieldAt;
    private TextureAtlas shipIconsAt;
    private TextureAtlas shipPointersAt;

    // Box2D variables
    private World world;
    private World worldProx;
    private Box2DDebugRenderer b2dr;

    // sprites
    public planetImage[] wells = new planetImage[9];
    public planetImage[] wellsOuter = new planetImage[9];
    public planetImage[] planets = new planetImage[9];
    public planetImage[] stations = new planetImage[9];
    public planetImage test;
    public planetImage test2;
    public planetImage[] orbits = new planetImage[9];
    public starImage starr;
    public orbLinesSprite[] orbline = new orbLinesSprite[9];
    private orbLinesSprite[] planetLines = new orbLinesSprite[9];
    public playerImage player;
    private polygonImage playerProx;
    private Array<polygonImage> proxObjects;
    public playerImage playerOver;
    public playerImage playerLevelsInterm;
    public systemScreenShipGroup playerShipShown;
    public shipManager shipManager;

    // booleans
    private boolean rendering;
    public boolean notStarted;
    public boolean autoOrbit;
    private int targetI;
    private boolean orbitingStar;
    private boolean orbitingPlanet;
    private boolean syncStation;
    private boolean stationSynced;
    private boolean changedProx;
    private boolean prefDirAC;
    private boolean targeting;
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
    private float G;
    private double gravR;
    private double minR;
    public boolean venting;
    private float angle1;
    private float angle2;

    // system info
    private double[][] gravData;
    private double[][] planetData;
    private double[][] stellarAccMap;
    private double[] starData;
    private int nP;
    public float solarRad;
    private float firstOrbRad;
    private float planSpace;
    private float maxOrbRad;
    private double previousGrad;
    private float orbRad;
    private double targetRad;
    private int targetBody;
    private int targetDir;
    private double targetAngle1;
    private double targetAngle2;

    public double dR;               // radius of player from star
    public double omega;               // angular speed of orbit
    public double theta;
    public double v;
    private int pathCount;
    private int pathCountLimit;//=(int) (99999*2);
    Array<Vector2> path = new Array<Vector2>();
    int wWid;
    int wHei;

    // ship-specific variables
    private float maxBurnThrust;    // maximum thrust at open throttle
    private float burnMultiplier;  // 0 if engines off, 1 if they are on
    private double stationOmega;


    // system-specific variables
    public int starRad;
    private float radMultiplier;
    public int starTemp;
    private float tempMultiplier;
    private float radSqr;
    private int tempSqr;
    private float shipRadDef;
    private float shipTempDef;
    private float shipRadLevel;
    private float shipTempLevel;

    // Timing variables for render loop
    // last second

    //Time-step stuff
    private double accumulator;
    private double currentTime;
    private float step;// = 1f/60f*1000;
    private boolean doPhysics;
    private boolean engBurn;
    private float engBurnX;
    private float engBurnY;

    // allstop variables
    private boolean allStop;
    private boolean startingAllStop;
    private float deltaV1;
    private float deltaV2;
    private float curThrust;
    private float constThrust;
    private float prevVel;
    private float velError;

    // Viewport variables
    private float maxZoomX;
    private float maxZoomY;

    // HUD variables
    private worldStage wStage;
    private nearProxStage proxStage;
    public sideHUD pauseHUD;
    public systemScreenActors systemActors;
    public Vector2 deltas;
    public boolean handling;
    private InputMultiplexer multiplexer;
    private float playerCamOffX;
    private float playerCamOffY;

    // sprite variables
    private float playerVx;
    private float playerVy;
    private float playersX;
    private float playersY;
    private double dummyVx;
    private double dummyVy;
    private double dummysX;
    private double dummysY;
    // Proximity variables
    private float proxVx;
    private float proxVy;
    private int proxAxInt;
    private int proxAyInt;
    private float proxOmega;
    private int proxAomega;
    private float colTime;

    public Vertex vertices[];
    private int playerHealth;
    private int playerShields;
    private int playerThrust;

    private boolean swtch;

    public enum Level { HIGHEST, MIDDLE, LOWEST }

    Level level;

    // Screen booleans
    public boolean paused;

    public systemScreen2(MyGdxGame game){

        //backgroundGenerator6_Pixmap bg = new backgroundGenerator6_Pixmap(game);
        //engConeGenerator engCon = new engConeGenerator(game);
        //new engRoomGenerator(game);
        //greebleGenerator greebGen = new greebleGenerator(game);

        // update variables
        stationOmega = Math.PI/20;
        int starSystNo = 1+1;
        game.randManager.setBaseCounts(starSystNo);
        shipRadLevel = 4f;      // current level of ship to deal with rads - to be contribution from hull and shields
        shipTempLevel = 4f;     // current level of ship to deal with temp - to be contribution from hull and shields
        allStop = false;
        startingAllStop = false;
        velError = game.V_WIDTH/10000;
        rendering = true;
        swtch = false;
        playerHealth = 100;
        playerShields = 100;
        playerThrust = 0;
        venting = false;
        engBurn = false;
        starOnly = false;
        targeting = false;
        targetI = 0;
        tracing = true;
        collision = false;
        useAccMap = false;
        currentTime = TimeUtils.millis() / 1000.0;
        doPhysics = false;
        step = 1f/(60f);
        pathCountLimit=(int) (1000);
        orbitPlanet = false;
        orbitingStar = false;
        orbitingPlanet = false;
        syncStation = false;
        stationSynced = false;
        changedProx = false;
        prefDirAC = false;
        planetDeburn = false;
        planetOrbDia = game.V_WIDTH/30;
        solarM = 1000;
        planetM1 = solarM/10;
        gravR = 3.5;      // power of radius in grav equations
        G = (float) (450*Math.pow(game.V_WIDTH,2)/Math.pow(30,2));
        handling = false;
        deltas = new Vector2(0,0);
        previousGrad = 0;
        angle1=0;
        angle2=0;
        proxAomega = 0;
        proxAxInt = 0;
        proxAyInt = 0;
        proxOmega = 0;
        proxVx = 0;
        proxVy = 0;
        colTime = 6;

        // ship-specific variables
        maxBurnThrust = (float) (250f/50*Math.pow(game.V_WIDTH,1)/Math.pow(30,1));
        burnMultiplier = 1;

        // test array function
        Array<Integer> testIntegers = new Array<>();
        for(int z=0;z<100;z++){
            testIntegers.add(z);
        }

        // initial player conditions
        float startVx = 0f*game.V_WIDTH/30;
        float startVy = 0f*game.V_WIDTH/30;

        // import of assets
        atlas = new TextureAtlas("PNGsPacked/Pointers.atlas");
        Platlas = new TextureAtlas("PNGsPacked/allPlanets.atlas");
        Statlas = new TextureAtlas("PNGsPacked/Stars.atlas");
        starAnim = new TextureAtlas("PNGsPacked/stars/starsPack.atlas");
        thrustAt = new TextureAtlas("systemScreen/ui/thrustControl.atlas");
        shieldAt = new TextureAtlas("systemScreen/ui/shieldBars.atlas");
        healthAt = new TextureAtlas("systemScreen/ui/healthBars.atlas");
        shipIconsAt = new TextureAtlas("shipIcons/shipIcons.atlas");
        shipPointersAt = new TextureAtlas("shipIcons/shipTypePointers.atlas");

        // set game variable
        this.game=game;

        map = new TiledMap();// mapLoader.load("map/systems/Syst6.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);

        // camera to follow touch and drag
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam) {};
        //System.out.println("viewport width "+gameport.getWorldWidth()+" and height "+gameport.getWorldHeight());

        // world variables
        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        worldProx = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        // create game overlays
        wStage = new worldStage(game,this,game.batch,gameport.getWorldWidth(),gameport.getWorldHeight());
        proxStage = new nearProxStage(game,this,game.batch,gameport.getWorldWidth(),gameport.getWorldHeight());

        pauseHUD = new sideHUD(game,gameport.getWorldWidth(),gameport.getWorldHeight());
        //systemShipImage = new systemScreenShipStage(game,this,game.batch,gameport.getWorldWidth()*MyGdxGame.PPM, gameport.getWorldHeight()*MyGdxGame.PPM);

        // set contact listeners for objects
        world.setContactListener(new WorldContactListener());

        this.notStarted = true;

        //gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(wStage.stage);
        GestureDetector gd = new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                if(burnMultiplier>0) {
                    Vector3 mousePos = new Vector3(Gdx.input.getX() / MyGdxGame.PPM, Gdx.input.getY() / MyGdxGame.PPM, 0);
                    float midX = windowWidth / (MyGdxGame.PPM * 2);//*gamecam.zoom);
                    float midY = windowHeight / (MyGdxGame.PPM * 2);//*gamecam.zoom);
                    float burnI = -(mousePos.x - midX);
                    float burnJ = -(mousePos.y - midY);
                    getburnAngle(burnI, burnJ);

                    allStop = false;
                    engBurn = true;
                }
                    return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                //System.out.println("tap");
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                //System.out.println("long press");
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                //System.out.println("fling");
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                //System.out.println("pan");
                if(handling){
                    deltas.x = x / MyGdxGame.PPM;
                    deltas.y = gameport.getWorldHeight() - y / MyGdxGame.PPM;

                }
                //System.out.println("pan x "+x+" y "+y);
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                //System.out.println("pan stop");
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                //System.out.println("zoom");
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                //System.out.println("pinch");
                return false;
            }

            @Override
            public void pinchStop() {
                //System.out.println("pinch stop");
            }
        });

        // Initialisation of system
        systemGenerator sGen = new systemGenerator(game,this);
        sGen.planetGen();
        solarRad = sGen.toteSize;
        planetData = sGen.getPlanetData();
        stellarAccMap = new double[200][2];
        sGen.starGen();
        starData = sGen.getStarData();
        starTemp = (int) starData[2];
        starRad = (int) starData[3];
        nP = planetData[0].length;
        gravData = new double[nP+1][4];
        gravData[0][0] = sGen.getStarData()[3];
        gravData[0][1] = 0;//game.V_WIDTH/2;
        gravData[0][2] = 0;//game.V_WIDTH/2;
        paused = false;
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

        int i = 0;
        int locX;// = 5750/2-size/2;
        int locY;// = (int) (5750/2 +  (Math.pow(-1,i))*((i)*375+750))-size/2;
        int size;

        int sType = 1; //(int) starData[0];
        String sObj = "star"+Integer.toString(sType);

        playerCamOffX=0;
        playerCamOffY=0;
        // set background image
        float backW = gameport.getWorldWidth()*1.5f;
        float backH = backW;
        float backX = (float) (gameport.getWorldWidth()/2-backW/2);
        float backY = (float) (gameport.getWorldHeight()/2-backH/2);
        backImage backImage = new backImage(this,backX,backY,backW,backH);
        Color color = backImage.getColor();
        //backImage.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(bg.getPixmap()))));
        wStage.backstage.addActor(backImage);

        // set star data
        starr = new starImage(world,this, sObj, nP, wWid, wHei,(int) gravData[0][0]);//new starSprite(world, this, sObj, nP, wWid, wHei,(int) gravData[0][0]);
        starr.setWidth(gameport.getWorldWidth()*(5f/60));
        starr.setHeight(gameport.getWorldWidth()*(5f/60));
        starr.setPosition((float) (gravData[0][1] - starr.getWidth()/2),(float) (gravData[0][2] - starr.getHeight()/2));
        wStage.stage.addActor(starr);
        minR = starr.getWidth();
        firstOrbRad = sGen.getfirstOrbRad();
        planSpace = sGen.getPlanSpace();
        maxOrbRad = firstOrbRad + planSpace*(nP+2);
        targetRad = maxOrbRad;
        targetDir = 1;
        targetAngle1 = 0;
        targetAngle2 = 0;
        targetBody = 0; // initialise sun

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
            orbits[i] = new planetImage(world,this,"orbit2",planetData,i,wWid,wHei);//new planetSpriteGRAVY(world,this,stelObj,planetData,i,wWid,wHei);
            wStage.stage.addActor(orbits[i]);
            orbits[i].setZIndex(1);
            orbRad = firstOrbRad+planSpace*i;
            orbits[i].setWidth(orbRad*2);
            orbits[i].setHeight(orbRad*2);
            float orbThickness = orbits[i].getWidth()*(3/500);
            orbits[i].setPosition(starr.getX()+starr.getWidth()/2-orbits[i].getWidth()/2,starr.getY()+starr.getHeight()/2-orbits[i].getHeight()/2);

            wellsOuter[i]  = new planetImage(world,this,"planetShine",planetData,i,wWid,wHei);
            wellsOuter[i].setWidth(starr.getWidth()*2);
            wellsOuter[i].setHeight(starr.getWidth()*2);
            wellsOuter[i].setPosition((float) (planetData[2][i]-wellsOuter[i].getWidth()/2 - orbThickness/2), (float) (planetData[3][i]-wellsOuter[i].getHeight()/2 - orbThickness/2));
            wellsOuter[i].setColor(wellsOuter[i].getColor().r,wellsOuter[i].getColor().g,wellsOuter[i].getColor().b,wellsOuter[i].getColor().a*2/3);
            wStage.stage.addActor(wellsOuter[i]);
            wells[i]  = new planetImage(world,this,"planetShine",planetData,i,wWid,wHei);
            wells[i].setWidth(starr.getWidth());
            wells[i].setHeight(starr.getWidth());
            wells[i].setPosition((float) (planetData[2][i]-wells[i].getWidth()/2 - orbThickness/2), (float) (planetData[3][i]-wells[i].getHeight()/2 - orbThickness/2));
            wells[i].setColor(wells[i].getColor().r,wells[i].getColor().g,wells[i].getColor().b,wells[i].getColor().a/3);
            wStage.stage.addActor(wells[i]);

            planets[i] = new planetImage(world,this,"planetShine",planetData,i,wWid,wHei);//new planetSpriteGRAVY(world,this,stelObj,planetData,i,wWid,wHei);
            planets[i].setWidth(starr.getWidth()/4);
            planets[i].setHeight(starr.getWidth()/4);
            gravData[i+1][0] = 0.01;    // completely unnecessary
            gravData[i+1][1] = planetData[2][i];
            gravData[i+1][2] = planetData[3][i];
            planets[i].setPosition((float) (planetData[2][i]-planets[i].getWidth()/2 - orbThickness/2), (float) (planetData[3][i]-planets[i].getHeight()/2 - orbThickness/2));
            wStage.stage.addActor(planets[i]);

            stations[i] = new planetImage(world,this,"planetShine",planetData,i,wWid,wHei);
            stations[i].setWidth(planets[i].getWidth()/3);
            stations[i].setHeight(planets[i].getHeight()/3);
            stations[i].setPosition((float) (planets[i].getX()+planets[i].getWidth()/2), (float) (planets[i].getY()+planets[i].getHeight()/2+wells[i].getHeight()/2));
            stations[i].posX = planets[i].getX() + planets[i].getWidth()/2;
            stations[i].posY = planets[i].getY() + planets[i].getHeight()/2;
            wStage.stage.addActor(stations[i]);

            orbline[i] = new orbLinesSprite(world,this,Math.round(planetData[8][i])/MyGdxGame.PPM,toteSize,toteSize);
            planetLines[i] = new orbLinesSprite(world,this,planetOrbDia,(float) planetData[2][i],(float) planetData[3][i]);

            radSqr = 2;     // define whether intensity is proportional to inverse or inverse square of distance from star
            tempSqr = 2;    // define whether intensity is proportional to inverse or inverse square of distance from star

            shipRadDef = (float) (shipRadLevel/(Math.pow(planSpace*4,radSqr)));           // Radiation Deflection constant - defined by score of 4 at radius 2000
            shipTempDef = (float) (shipTempLevel/(Math.pow(planSpace*3,tempSqr)));          // Temperature Deflection constant - defined by score of 4 at radius 1500
            // system-specific variables
            radMultiplier = (float) (200/(2*(shipRadLevel/Math.pow(planSpace,radSqr)-shipRadDef))); // based on starRad of 4, powRad of 20 degC/dt, shipTempDef over 1 dt at one orbital spacing
            tempMultiplier = (float) (20/((shipTempLevel/Math.pow(planSpace,tempSqr)-shipTempDef))); // based on starTemp of 4, powHeat of 20 degC/dt, shipTempDef over 1 dt at one orbital spacing

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

        float startX = starr.getX() + planSpace*6f;
        float startY = starr.getY() + planSpace*6f;
        System.out.println("startX "+startX+" startY "+startY);
        player = new playerImage(game, world,this,toteSize,"baseFreighter1",startX,startY);
        playerOver = new playerImage(game, world,this,toteSize,"C_1_Player",startX,startY);
        float temp1 = getShipPointersAt().findRegion("C_1_Player").getRegionWidth();
        float temp2 = getShipPointersAt().findRegion("C_1_Player").getRegionHeight();
        float aspect = temp2/temp1;//getShipPointersAt().findRegion("C_1_Player").getRegionHeight()/getShipPointersAt().findRegion("C_1_Player").getRegionWidth();
        //playerOver.rotateBy(-90);
        playerOver.setHeight(wStage.foreStage.getHeight()/50);
        playerOver.setWidth(playerOver.getHeight()/aspect);
        playerOver.setX(wStage.foreStage.getWidth()/2 - playerOver.getWidth()/2);
        playerOver.setY(wStage.foreStage.getHeight()/2 - playerOver.getHeight()/2);
        wStage.foreStage.addActor(playerOver);
        wStage.wStageCam3.rotate(-90);

        //wStage.stage.addActor(player);
        //playerLevelsInterm = new playerImage(game, world,this,toteSize,"Level2SHIP",startX,startY);
        //wStage.stage.addActor(playerLevelsInterm);




        // set up ship graphic
        float shipWidth = starr.getWidth();
        playerShipShown = new systemScreenShipGroup(game,world,this,game.batch,shipWidth);
        //wStage.stage.addActor(playerShipShown);

        systemActors = new systemScreenActors(game,this,game.batch,gameport.getWorldWidth(), gameport.getWorldHeight());
        shipManager = new shipManager(this,systemActors);
        multiplexer.addProcessor(systemActors.stage);
        multiplexer.addProcessor(gd);
        multiplexer.addProcessor(new InputProcessor() {
            int offsetX;
            int offsetY;
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                offsetX = screenX;
                offsetY = screenY;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                offsetX = 0;
                offsetY = 0;
                System.out.println("stopping engine burn");
                engBurn = false;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                if(wStage.wStageCam.zoom>0.1) {
                    wStage.wStageCam.zoom += amount * 0.05;
                    //System.out.println("zoom is " + wStage.wStageCam.zoom + " sonnnn");
                } else {
                    if(amount<0){
                        // do nowt - dont wanna zoom more. Son
                    } else {
                        wStage.wStageCam.zoom += amount * 0.05;
                    }
                }
                return false;
            }
        });
//        multiplexer.addProcessor(wStage.stage);
        Gdx.input.setInputProcessor(multiplexer);
/*
        // initially set other two layers transparent
        Color tempColor = playerLevelsInterm.getColor();
        playerLevelsInterm.setColor(tempColor.r,tempColor.g,tempColor.b,0);
        tempColor = playerShipShown.getColor();
        playerShipShown.setColor(tempColor.r,tempColor.g,tempColor.b,0);
        level = Level.HIGHEST;*/

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
        playerVx = startVx;
        playerVy = startVy;
        player.b2body.setLinearVelocity(new Vector2(0,0));

        float currentVx = player.b2body.getLinearVelocity().x;
        float currentVy = player.b2body.getLinearVelocity().y;

        genAccMap();
/*
        gamecam.viewportWidth = maxZoomX * 0.1f;
        gamecam.viewportHeight = maxZoomY * 0.1f;*/

        //systemHUD = new systemScreenHUD(game, game.batch,gameport.getWorldWidth(),gameport.getWorldHeight(),this);



    }

    public systemScreenShipGroup getShip(){
        return playerShipShown;
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

    public void toggleRender(){
        if(rendering){
            rendering = false;
        } else {
            rendering = true;
        }
    }

    public void getTrace(float dt) {

        collision = false;

        boolean buffer2 = true;

        // initialise trace dummy speed and posish
        //dummy.b2body.setTransform(player.b2body.getPosition().x,player.b2body.getPosition().y,0);
        dummysX = player.b2body.getPosition().x;
        dummysY = player.b2body.getPosition().y;
        dummyVx = playerVx;
        dummyVy = playerVy;

        double sX;
        double sY;
        float Vx = player.b2body.getLinearVelocity().x;
        float Vy = player.b2body.getLinearVelocity().y;
        double VxNew;
        double VyNew;
        path.clear();
        pathCount = 0;

            while(buffer2){//pathCount<pathCountLimit){
                // cycle through points predicting player movement
                double r;
                double Ax = 0;
                double Ay = 0;
                double pX;// = 0;
                double pY;// = 0;

                sX = dummysX;
                sY = dummysY;
                orbitPlanet=false;
                if(orbitPlanet){
                    System.out.println("ORBITTING SONNN");
                    r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - dummysX, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - dummysY, 2));
                    Ax = (float) (G * planetM1*-(dummysX - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                    Ay = (float) (G * planetM1*-(dummysY - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                } else {
                    if (!starOnly) {
                        for (int i = 0; i < nP; i++) {
                            r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - sY, 2));
                            if(r<minR){
                                r=minR;
                            }
                            Ax = Ax + (G * planetM1 * -(sX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            Ay = Ay + (G * planetM1 * -(sY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        }
                    }
                    r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                    if(r<minR){
                        r=minR;
                    }
                    Ax = Ax + G * solarM * -(sX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                    Ay = Ay + G * solarM * -(sY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                }
                path.add(new Vector2((float) sX, (float) sY));
                dummyVx = (dummyVx + Ax * dt);                    // new velocity at end of timestep
                pX = (dummyVx) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                dummyVy = (dummyVy + Ay * dt);                    // new velocity at end of timestep
                pY = (dummyVy) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                dummysX = dummysX + pX;
                dummysY = dummysY + pY;
                if(collision){
                    buffer2 = false;
                } else if(pathCount==pathCountLimit){
                    buffer2 = false;
                } else {
                    pathCount++;
                }
            }
        //}
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

    public void getburnAngle(double burnI, double burnJ){
        // method calculating burn angle taking into account camera rotation

        // burnI is distance from ship to mouse click in x-direction relative to the rotating ship plane
        // burnJ is distance from ship to mouse click in y-direction etc
        // if burnI is positive, then are decelerating in the ships direction
        // if burnJ is positive, then the vector is the normal to above
        // both of these have a world x-y impact

        float burnR = (float) Math.sqrt(Math.pow(burnI,2)+Math.pow(burnJ,2));
        double ratioI = burnI/burnR;
        double ratioJ = burnJ/burnR;

        System.out.println("burnI "+burnI+" burnJ "+burnJ+" burnR "+burnR);
        System.out.println("ratioI "+ratioI+" ratioJ "+ratioJ);

        // Vector in player direction
        double burnIx = playerVx;
        double burnIy = playerVy;
        // Vector perpendicular
        double burnJx = -burnIy;
        double burnJy = burnIx;

        System.out.println("burnIx "+burnIx+" burnIy "+burnIy);
        System.out.println("burnJx "+burnJx+" burnJy "+burnJy);

        double hypotI = Math.sqrt((Math.pow(burnIx,2))+(Math.pow(burnIy,2)));
        float xRatioI = (float) (burnIx/hypotI);
        float yRatioI = (float) (burnIy/hypotI);

        System.out.println("hypotI "+hypotI+" xRatioI "+xRatioI+" yRatioI "+yRatioI);

        double hypotJ = Math.sqrt(Math.pow(burnJx,2)+Math.pow(burnJy,2));
        float xRatioJ = (float) (burnJx/hypotJ);
        float yRatioJ = (float) (burnJy/hypotJ);

        System.out.println("hypotJ "+hypotJ+" xRatioJ "+xRatioJ+" yratioJ "+yRatioJ);

        float AxI = (float) ((playerThrust * maxBurnThrust * xRatioI * ratioI) / 100);
        float AyI = (float) ((playerThrust * maxBurnThrust * yRatioI * ratioI) / 100);

        System.out.println("AxI "+AxI+" AyI "+AyI);

        float AxJ = (float) ((playerThrust * maxBurnThrust * yRatioI * ratioJ) / 100);
        float AyJ = (float) ((playerThrust * maxBurnThrust * yRatioI * ratioJ) / 100);

        System.out.println("AxJ "+AxJ+" AyJ "+AyJ);
/*

        float AxJ = (float) ((playerThrust * maxBurnThrust * xRatioJ * ratioJ) / 100);
        float AyJ = (float) ((playerThrust * maxBurnThrust * yRatioJ * ratioJ) / 100);

        System.out.println("AxJ "+AxJ+" AyJ "+AyJ);
*/

        engBurnX = AxI + AxJ;
        engBurnY = AyI + AyJ;

        System.out.println("engBurnX "+engBurnX+" engburny "+engBurnY);

    }

    public Vector2 getStellarAcc(){
        // determine acceleration in x and y due to stellar bodies
        Vector2 A = new Vector2();
        float Ax=0;
        float Ay=0;
        double r;

        if (!starOnly) {
            for (int i = 0; i < nP; i++) {
                r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - playersY, 2));

                if (r < planetOrbDia) {
                    // within orbit of planet
                    orbitPlanet = true;
                    planetNum = i + 1;
                    planetDeburn = true;
                }
                Ax = Ax + (float) (G * planetM1 * -(playersX - gravData[i + 1][1]) / Math.pow(r, gravR));
                Ay = Ay + (float) (G * planetM1 * -(playersY - gravData[i + 1][2]) / Math.pow(r, gravR));
            }
        }
        r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - playersY, 2));
        Ax = Ax + (float) (G * solarM * -(playersX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
        Ay = Ay + (float) (G * solarM * -(playersY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR));

        A.x=Ax;
        A.y=Ay;

        return A;
    }

    public void toggleAllStop(){
        if(!allStop) {
            targeting = true;
            allStop = true;
            startingAllStop = true;
            engBurn = false;
        } else {
            allStop = false;
        }
    }

    public Vector2 allStop(){
        // arbitrary timestep
        float physTime = 1f/60; // 60 FPS - good approximation
        // Acceleration due to stellar bodies
        Vector2 A = getStellarAcc();
        // get absolute value of acceleration
        double Ar = Math.sqrt(Math.pow(A.x,2)+Math.pow(A.y,2));
        // get absolute value of velocity
        double Vr = getPlayerSpeedR();

        if(startingAllStop){
            // first loop of answering allstop
            deltaV1 = 1f;
            deltaV2 = 1f;
            curThrust = maxBurnThrust/2;
            constThrust = 1.1f;
            prevVel = (float) Vr;
        }

        // Acceleration due to engines
        float eAx;  // x axis
        float eAy;  // y axis
        float eAr;  // absolute

        if(true){//Ar>maxBurnThrust){
            // not going to be able to all stop

            System.out.println("Vx "+playerVx+" Vy "+playerVy+" Vr "+Vr);

            eAx = (float) (curThrust*playerVx/Vr);
            eAy = (float) (curThrust*playerVy/Vr);
            eAr = (float) (Math.sqrt(Math.pow(eAx,2)+Math.pow(eAy,2)));

            System.out.println("Ax "+A.x+" eAx "+eAx+" Ay "+A.y+" eAy "+eAy);

            float v1 = (float) (Vr + Ar*physTime);
            float v2 = (float) (Vr + (Ar-eAr)*physTime);

            System.out.println("v1 "+v1+" v2 "+v2);

            if(v2>v1){
                // overshoot - not slowing down
                curThrust /= constThrust;
            } else {
                // slowing down (thumbs up emojii)
                curThrust *= constThrust;
            }

            deltaV2 = Math.abs(((float) getPlayerSpeedR()) - prevVel);
            System.out.println("deltav2 "+deltaV2+" deltav1 "+deltaV1);

            if(deltaV2>deltaV1){
                // new change in velocity is greater than previous
                // reduce thrust constant to reduce error
                constThrust /= 1.05f;
            } else {
                // new change in velocity smaller than previous
                // heading in right direction so leave?
                constThrust *= 1.05f;
            }

            System.out.println("constThrust "+constThrust);

            eAx = (float) (curThrust*playerVx/Vr);
            eAy = (float) (curThrust*playerVy/Vr);

            A.x -= eAx;
            A.y -= eAy;

            System.out.println("player speed "+getPlayerSpeedR()+" vs velError "+velError);

            if(getPlayerSpeedR()<velError){
                System.out.println("player zero velocity");
                playerVx=0;
                playerVy=0;
                A.x = 0;
                A.y = 0;
            }

            // update variables for next timestep
            deltaV1 = deltaV2;
            prevVel = (float) getPlayerSpeedR();

        } else {
            // enough force to all stop
            A.x = 0;
            A.y = 0;
        }

        return A;
    }

    public void orbitStar(boolean outside){

        targetBody = 0;
        if(outside) {
            targetRad = maxOrbRad;//Math.sqrt(Math.pow((playersX-gravData[0][1]),2)+Math.pow((playersY-gravData[0][2]),2));
        } else {
            targetRad = getPlayerPositionR(0);
        }

        orbitingStar = true;
        targeting = true;

    }

    public void orbitPlanet(int iPlanet){
        Vector2 playerPos = getPlayerPosition();

        targetI = iPlanet;
        targetRad = maxOrbRad;// Math.sqrt(Math.pow((playersX-gravData[iPlanet][1]),2)+Math.pow((playersY-gravData[iPlanet][2]),2));

        orbitingPlanet = true;
        targeting = true;

    }

    public void orbitNearestPlanet(){

        System.out.println("choosing nearest planet");

        double rad = Math.pow(10,10);
        targetBody = 0;

        for(int i=1;i<gravData.length;i++){
            System.out.println("checking body "+i);
            double tempR = getPlayerPositionR(i);
            System.out.println("body at tempR "+tempR);
            System.out.println("compared to "+rad);
            if(tempR<rad){
                System.out.println("new body chosen "+i);
                targetBody = i;
                rad = tempR;
            }
        }

        System.out.println("body chosen "+targetBody);

        targetRad = wells[targetBody].getWidth()*1.5/2;// Math.sqrt(Math.pow((playersX-gravData[iPlanet][1]),2)+Math.pow((playersY-gravData[iPlanet][2]),2));

        orbitingPlanet = true;
        targeting = true;

    }

    public void syncStation(){
        // first match orbital radius
        syncStation = true;
        prefDirAC = true;
        targetRad = wells[targetBody].getWidth()/2;
    }

    public traceImage setUpAngledRect(Vector2 p1,Vector2 p2,float thickness){
        // set up short section between two points at an angle
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        float cenX = p1.x + dx/2;
        float cenY = p1.y + dy/2;
        Vector2 centre = new Vector2(cenX,cenY);
        float theta = (float) ((180/Math.PI)*Math.atan(dy/dx));
        traceImage temp = new traceImage(game, this, centre.x - dx/2, centre.y - thickness/2, dx, thickness);
        temp.setOrigin(temp.getWidth()/2,temp.getHeight()/2);
        temp.rotateBy(theta);
        //temp.setColor(0,255,0,255);
        return temp;
    }

    public void setupProximity(){
        // transparent backdrop
        float backW = gameport.getWorldWidth()*1.5f;
        float backH = backW;
        float backX = (float) (proxStage.stage.getWidth()/2-backW/2);
        float backY = (float) (proxStage.stage.getHeight()/2-backH/2);
        backImage backImage = new backImage(this,backX,backY,backW,backH);
        backImage.setDrawable(new TextureRegionDrawable(new TextureRegion(game.getTilesAt().findRegion("pDTTile50Black"))));
        Color color = backImage.getColor();
        backImage.setColor(color.r,color.g,color.b,color.a*5/6);
        proxStage.backstage.addActor(backImage);

        // station added
        // create internal first
        float stationH = proxStage.stage.getWidth()/10;
        String stationInt = "domsWankStationInt";
        String stationExt = "domsWankStationCen";
        double aspect = getShipIconsAt().findRegion(stationExt).getRegionHeight()/getShipIconsAt().findRegion(stationExt).getRegionWidth();
        float stationW = stationH*25;
        float stationX = proxStage.stage.getWidth()/2 - backW/2;
        float stationY = proxStage.stage.getHeight()/2 - backH/2;
        proxObjects = new Array<polygonImage>();
        // internal station floor
        float offsetX = 0;
        float offsetY = 0;
        proxObjects.add(new polygonImage(game,worldProx,this,1,"floorCeil",stationX+offsetX,stationY+offsetY));
        proxObjects.get(0).setPosition(stationX+offsetX,stationY+offsetY);
        proxObjects.get(0).setSize(stationW,stationH);
        proxObjects.get(0).setCentre(proxObjects.get(0).getX()+proxObjects.get(0).getWidth()/2,proxObjects.get(0).getY()+proxObjects.get(0).getHeight()/2);
        proxObjects.get(0).setCollision(true);
        BufferedImage bImage = null;
        try {
            bImage = javax.imageio.ImageIO.read(new File("shipIcons/shipIcons/FREIGHTER/floorCeil.png"));//new BufferedImage(tex2.getRegionWidth(), tex2.getRegionHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch(Exception e) {
            //  Block of code to handle errors
        }
        Array<Vector2> vertices = returnBorder2(bImage,0);
        proxObjects.get(0).setVertices(vertices);
        Array<Vector3> shapes = returnShapes(vertices);
        proxObjects.get(0).setShapes(shapes);

        // internal station ceiling
        proxObjects.add(new polygonImage(game,worldProx,this,1,"floorCeil",stationX+offsetX,stationY+offsetY));
        offsetY = 4*stationH;
        proxObjects.get(1).setPosition(stationX+offsetX,stationY+offsetY);
        proxObjects.get(1).setSize(stationW,stationH);
        proxObjects.get(1).setCentre(proxObjects.get(1).getX()+proxObjects.get(1).getWidth()/2,proxObjects.get(1).getY()+proxObjects.get(1).getHeight()/2);
        proxObjects.get(1).setCollision(true);
        bImage = null;
        try {
            bImage = javax.imageio.ImageIO.read(new File("shipIcons/shipIcons/FREIGHTER/floorCeil.png"));//new BufferedImage(tex2.getRegionWidth(), tex2.getRegionHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch(Exception e) {
            //  Block of code to handle errors
        }
        vertices = returnBorder2(bImage,1);
        proxObjects.get(1).setVertices(vertices);
        shapes = returnShapes(vertices);
        proxObjects.get(1).setShapes(shapes);

        // internal left shield door
        proxObjects.add(new polygonImage(game,worldProx,this,1,"Laser_Blue",stationX+offsetX,stationY+offsetY));
        offsetY = stationH;
        offsetX = stationH;
        proxObjects.get(2).setSize(stationW/250,stationH*3);
        proxObjects.get(2).setPosition(stationX+offsetX,stationY+offsetY);
        proxObjects.get(2).setCentre(stationX+offsetX+proxObjects.get(2).getWidth()/2,stationY+offsetY+proxObjects.get(2).getHeight()/2);
        proxObjects.get(2).setCollision(false);

        // landing pad 1
        proxObjects.add(new polygonImage(game,worldProx,this,1,"dockingPoint",stationX+offsetX,stationY+offsetY));
        proxObjects.get(3).setSize(stationH*7,stationH/3);
        offsetY = stationH - proxObjects.get(3).getHeight()/2;
        offsetX = stationH;
        proxObjects.get(3).setPosition(stationX+offsetX,stationY+offsetY);
        proxObjects.get(3).setCentre(stationX+offsetX+proxObjects.get(3).getWidth()/2,stationY+offsetY+proxObjects.get(3).getHeight()/2);
        proxObjects.get(3).setCollision(true);
        bImage = null;
        try {
            bImage = javax.imageio.ImageIO.read(new File("shipIcons/shipIcons/FREIGHTER/dockingPoint.png"));//new BufferedImage(tex2.getRegionWidth(), tex2.getRegionHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch(Exception e) {
            //  Block of code to handle errors
        }
        vertices = returnBorder2(bImage,3);
        proxObjects.get(3).setVertices(vertices);
        shapes = returnShapes(vertices);
        proxObjects.get(3).setShapes(shapes);

        // landing pad 2
        proxObjects.add(new polygonImage(game,worldProx,this,1,"dockingPoint",stationX+offsetX,stationY+offsetY));
        proxObjects.get(4).setSize(stationH*7,stationH/3);
        offsetY = stationH - proxObjects.get(4).getHeight()/2;
        offsetX = stationH*9;
        proxObjects.get(4).setPosition(stationX+offsetX,stationY+offsetY);
        proxObjects.get(4).setCentre(stationX+offsetX+proxObjects.get(4).getWidth()/2,stationY+offsetY+proxObjects.get(4).getHeight()/2);
        proxObjects.get(4).setCollision(true);
        bImage = null;
        try {
            bImage = javax.imageio.ImageIO.read(new File("shipIcons/shipIcons/FREIGHTER/dockingPoint.png"));//new BufferedImage(tex2.getRegionWidth(), tex2.getRegionHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch(Exception e) {
            //  Block of code to handle errors
        }
        vertices = returnBorder2(bImage,4);
        proxObjects.get(4).setVertices(vertices);
        shapes = returnShapes(vertices);
        proxObjects.get(4).setShapes(shapes);

        // landing pad 3
        proxObjects.add(new polygonImage(game,worldProx,this,1,"dockingPoint",stationX+offsetX,stationY+offsetY));
        proxObjects.get(5).setSize(stationH*7,stationH/3);
        offsetY = stationH - proxObjects.get(5).getHeight()/2;
        offsetX = stationH*17;
        proxObjects.get(5).setPosition(stationX+offsetX,stationY+offsetY);
        proxObjects.get(5).setCentre(stationX+offsetX+proxObjects.get(5).getWidth()/2,stationY+offsetY+proxObjects.get(5).getHeight()/2);
        proxObjects.get(5).setCollision(true);
        bImage = null;
        try {
            bImage = javax.imageio.ImageIO.read(new File("shipIcons/shipIcons/FREIGHTER/dockingPoint.png"));//new BufferedImage(tex2.getRegionWidth(), tex2.getRegionHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch(Exception e) {
            //  Block of code to handle errors
        }
        vertices = returnBorder2(bImage,5);
        proxObjects.get(5).setVertices(vertices);
        shapes = returnShapes(vertices);
        proxObjects.get(5).setShapes(shapes);

        // add all actors to stage
        for(int i=0;i<proxObjects.size;i++){
            proxStage.stage.addActor(proxObjects.get(i));
        }

        // Create debug lines if required of all shapes in polygon
        boolean debug1 = true;
        if(debug1) {
            for (int i = 0; i < proxObjects.size; i++) {
                // stationW/250
                Array<Vector3> tempShapes = proxObjects.get(i).getShapes();
                if(tempShapes==null) {
                    // do nothing
                } else {
                    Array<Vector2> tempVerts = proxObjects.get(i).getVertices();
                    float t = stationW / 2500;
                    ; // thickness for bar
                    for (int j = 0; j < tempShapes.size; j++) {
                        int nodeX = (int) tempShapes.get(j).x;
                        int nodeY = (int) tempShapes.get(j).y;
                        int nodeZ = (int) tempShapes.get(j).z;
                        Vector2 node1 = tempVerts.get(nodeX);
                        Vector2 node2 = tempVerts.get(nodeY);
                        Vector2 node3 = tempVerts.get(nodeZ);
                        proxStage.stage.addActor(setUpAngledRect(node1, node2, t));
                        proxStage.stage.addActor(setUpAngledRect(node2, node3, t));
                        proxStage.stage.addActor(setUpAngledRect(node3, node1, t));
                    }
                }
            }
        }

        // create debug lines of outline of polygon only
        boolean debug2 = false;
        if(debug2) {
            for (int i = 0; i < proxObjects.size; i++) {
                // stationW/250
                Array<Vector2> tempShapes = proxObjects.get(i).getOutsideEdges();
                if(tempShapes==null) {
                    // do nothing
                } else {
                    Array<Vector2> tempVerts = proxObjects.get(i).getVertices();
                    float t = stationW / 1250;
                    ; // thickness for bar
                    for (int j = 0; j < tempShapes.size; j++) {
                        int nodeX = (int) tempShapes.get(j).x;
                        int nodeY = (int) tempShapes.get(j).y;
                        Vector2 node1 = tempVerts.get(nodeX);
                        Vector2 node2 = tempVerts.get(nodeY);
                        proxStage.stage.addActor(setUpAngledRect(node1, node2, t));
                    }
                }
            }
        }

        TextureRegion tex = getShipIconsAt().findRegion("baseFreighter1");

        // player added
        float playerH = proxObjects.get(2).getHeight()/12;
        double tempH = getShipIconsAt().findRegion("baseFreighterSideCut").getRegionHeight();
        double tempW = getShipIconsAt().findRegion("baseFreighterSideCut").getRegionWidth();
        aspect = (tempH/tempW);
        float playerW = (float) (playerH/aspect);
        float playerX = proxObjects.get(0).getX()+stationW/2;
        float playerY = proxObjects.get(0).getY()+stationH*2;
        playerProx = new polygonImage(game,worldProx,this,1,"baseFreighterSideCut",playerX,playerY);

        try {
            bImage = javax.imageio.ImageIO.read(new File("shipIcons/shipIcons/FREIGHTER/baseFreighter1.png"));//new BufferedImage(tex2.getRegionWidth(), tex2.getRegionHeight(), BufferedImage.TYPE_INT_ARGB);
        } catch(Exception e) {
            //  Block of code to handle errors
        }
          returnBorder2(bImage,0);
        playerProx.setVertices(vertices);

        //playerProx.b2body.setTransform(playerX,playerY,0);
        //playerProx.defineSprite(1,playerProx.b2body.getPosition().x,playerProx.b2body.getPosition().y,false,"PLAYER_PROX");
        playerProx.setCentre(playerX,playerY);
        playerProx.setSize(playerW,playerH);
        playerProx.setPosition(playerX - playerProx.getWidth()/2,playerY - playerProx.getHeight()/2);
        playerProx.setColor(0,0,0,0);
        proxStage.stage.addActor(playerProx);
        playerShipShown.setSize(playerW,playerH);
        playerProx.setPosition(playerX - playerProx.getWidth()/2,playerY - playerProx.getHeight()/2);
        proxStage.stage.addActor(playerShipShown);

        proxStage.wStageCam.position.x = playerProx.getX() + playerProx.getWidth()/2;
        proxStage.wStageCam.position.y = playerProx.getY() + playerProx.getHeight()/2;

        proxStage.stage.setDebugAll(false);

        changedProx = true;
    }

    public Array<Vector2> returnBorder(BufferedImage image,int proxN){

        int pixX = image.getWidth();
        int pixY = image.getHeight();
        int incX = 10;
        int incY = 1;

        int pixels[][] = new int[pixX][pixY];
        Array<Vector2> border = new Array<Vector2>();
        //Vector2 border = new Vector2();

        for(int i=0;i<pixX;i+=incX){
            for(int j=0;j<pixY;j+=incY){
                pixels[i][j] = image.getRGB(i,j);
                int alpha = (pixels[i][j] >> 24) & 0xff;
                boolean bordPix = false;
                if(alpha>0){
                    // its a station pixel - check for a pixel around that is a border
                    if(i>0 && j>0) {
                        for (int i1=-1;i1<2;i1++) {
                            for (int j1=-1;j1<2;j1++){
                                int tempC = image.getRGB(i+i1,j+j1);
                                int tempA = (tempC >> 24) & 0xff;
                                if(tempA<10){
                                    bordPix = true;
                                }
                            }
                        }
                    }
                }
                if(bordPix){
                    border.add(new Vector2(i,pixY-j));
                }
            }
        }

        System.out.println("border size "+border.size);

        int tempIndex = 0;
        double tempDist = 999999999;
        int currentInd = 0;
        double currentX = border.get(0).x;
        double currentY = border.get(0).y;
        Array<Vector2> ordered = new Array<Vector2>();
        ordered.add(border.get(0));
        border.removeIndex(0);

        while(border.size>1) {
            for (int i = 0; i < border.size; i++) {
                double tempR = Math.sqrt(Math.pow((currentX - ((double) (border.get(i).x))), 2) + Math.pow((currentY - ((double) (border.get(i).y))), 2));
                if (tempR < tempDist) {
                    tempDist = tempR;
                    tempIndex = i;
                }
            }
            //System.out.println("tempIndex "+tempIndex);
            ordered.add(border.get(tempIndex));
            border.removeIndex(tempIndex);
            tempIndex=0;
            tempDist = 999999999;
        }

        Array<Vector2> ordered2 = new Array<Vector2>();

        int tempI=1;
        while (tempI < ordered.size-1){
            //ordered2.add(ordered.get(tempI));
            tempI += 2;
            System.out.println("ordered size "+ordered.size+" tempI "+tempI);
        }

        while(ordered.size>1000) {
            tempI=ordered.size-1;
            while (tempI > 3) {
                ordered.removeIndex(tempI);
                tempI -= 2;
            }
        }

        boolean angleMethod = false;

        if(angleMethod) {
            for (int i = 0; i < ordered.size; i++) {
                double modX1;
                double modY1;
                double modX2;
                double modY2;
                if (i == 0) {
                    modX1 = Math.abs(ordered.get(ordered.size - 1).x - ordered.get(0).x);
                    modY1 = Math.abs(ordered.get(ordered.size - 1).y - ordered.get(0).y);
                    modX2 = Math.abs(ordered.get(i).x - ordered.get(i + 1).x);
                    modY2 = Math.abs(ordered.get(i).y - ordered.get(i + 1).y);
                } else if (i == ordered.size - 1) {
                    modX1 = Math.abs(ordered.get(i).x - ordered.get(i - 1).x);
                    modY1 = Math.abs(ordered.get(i).y - ordered.get(i - 1).y);
                    modX2 = Math.abs(ordered.get(ordered.size - 1).x - ordered.get(0).x);
                    modY2 = Math.abs(ordered.get(ordered.size - 1).y - ordered.get(0).y);
                } else {
                    modX1 = Math.abs(ordered.get(i).x - ordered.get(i - 1).x);
                    modY1 = Math.abs(ordered.get(i).y - ordered.get(i - 1).y);
                    modX2 = Math.abs(ordered.get(i).x - ordered.get(i + 1).x);
                    modY2 = Math.abs(ordered.get(i).y - ordered.get(i + 1).y);
                }
                double phi1 = Math.tan(modY1 / modX1);
                double phi2 = Math.tan(modY2 / modX2);
                double phi = phi1 + phi2;
                if (phi > (5 * Math.PI / 180)) {
                    // enough angle on this point to count it
                    ordered2.add(ordered.get(i));
                }
            }
        } else {
            tempI = 0;
            ordered2.add(ordered.get(0));
            for (int i = 1; i < ordered.size; i++) {
                tempDist = Math.sqrt(Math.pow((((double) (ordered.get(i).x - ordered.get(tempI).x))), 2) + Math.pow((((double) (ordered.get(i).y - ordered.get(tempI).y))), 2));
                if(tempDist>10){
                    // far enough away to add to list
                    ordered2.add(ordered.get(i));
                    tempI=i;
                }
            }
        }

        //ordered2 = new Array<Vector2>();
        for(int i=0;i<ordered.size;i++){
            //ordered2.add(ordered.get(i));
        }

        System.out.println("ordered size "+ordered.size);

        Array<Vector2> ordered3 = new Array<Vector2>();
        while(ordered2.size>1) {
            for (int i = 0; i < border.size; i++) {
                double tempR = Math.sqrt(Math.pow((currentX - ((double) (border.get(i).x))), 2) + Math.pow((currentY - ((double) (border.get(i).y))), 2));
                if (tempR < tempDist) {
                    tempDist = tempR;
                    tempIndex = i;
                }
            }
            //System.out.println("tempIndex "+tempIndex);
            ordered3.add(ordered2.get(tempIndex));
            ordered2.removeIndex(tempIndex);
            tempIndex=0;
            tempDist = 999999999;
        }

        Array<Vector2> ordered4 = new Array<Vector2>();
        // convert from image coordinates to world coordinates
        for(int i=0;i<ordered3.size;i++){
            Vector2 tempVec = new Vector2();
            tempVec.x = ordered3.get(i).x*proxObjects.get(proxN).getWidth()/image.getWidth()+proxObjects.get(proxN).getX();
            tempVec.y = ordered3.get(i).y*proxObjects.get(proxN).getHeight()/image.getHeight()+proxObjects.get(proxN).getY();
            ordered4.add(tempVec);
        }


        float[] formatted = new float[ordered.size*2];

        for(int i=0;i<ordered.size;i+=2){
            formatted[i] = ordered.get(i/2).x;
            formatted[i+1] = ordered.get(i/2).y;
        }

        return ordered4;

    }

    public Array<Vector2> returnBorder2(BufferedImage image,int proxN){

        int pixX = image.getWidth();
        int pixY = image.getHeight();
        int incX = 10;
        int incY = 1;

        int pixels[][] = new int[pixX][pixY];
        Array<Vector2> border = new Array<Vector2>();
        Array<Vector2> initialBorder = new Array<Vector2>();
        //Vector2 border = new Vector2();

        boolean useRadMethod = true;
        boolean useRadMethod2 = false;

        if(useRadMethod2){

        } else {
            if (useRadMethod) {
                boolean searchingPix = true;
                Vector2 point1 = new Vector2(0, pixY / 2);
                int iX = 0;
                int jY = 0;
                int pixel;
                while (searchingPix) {
                    // find left most pixel
                    System.out.println("ix " + iX + " jY " + jY);
                    System.out.println("pixX " + pixX + " pixY " + pixY);
                    pixel = image.getRGB(iX, jY);
                    int alpha = (pixel >> 24) & 0xff;
                    if (alpha > 10) {
                        searchingPix = false;
                        point1 = new Vector2(iX, jY);
                        initialBorder.add(point1);
                    }
                    jY++;
                    if (jY >= pixY) {
                        iX++;
                        jY = 0;
                    }
                }
                // found most left pixel
                Vector2 point2 = point1;
                float searchRad = 1;
                float searchRadMin = 1;
                int j2 = pixY - 1;
                iX = 0;
                searchingPix = true;
                while (searchingPix) {
                    // find next pixel that should be above and right for initial direction
                    if(iX>=pixX){
                        iX = 0;
                        j2--;
                    }
                    pixel = image.getRGB(iX, j2);
                    int alpha = (pixel >> 24) & 0xff;
                    if (alpha > 10) {
                        searchingPix = false;
                        point2 = new Vector2(iX, j2);
                        initialBorder.add(point2);
                    }
                    iX++;
                }
                Vector2 initPoint = point1;
                double distPoint1 = 25;//Math.sqrt(Math.pow((point2.x-point1.x),2)+Math.pow((point2.y-point1.y),2));
                int buffer = 0;
                while (distPoint1 > (15 * searchRadMin)) {
                    // found next pixel - now use these two for initial direction around shape
                    searchRad = searchRadMin;
                    Vector2 dirVec = new Vector2((point2.x - point1.x), (point2.y - point1.y));
                    float magDir = (float) Math.sqrt(Math.pow((point2.x - point1.x), 2) + Math.pow((point2.y - point1.y), 2));
                    Vector2 unitDir = new Vector2((dirVec.x / magDir), (dirVec.y / magDir));
                    Vector2 newPoint = new Vector2(0, 0);
                    /*
                    if(unitDir.x == 0){
                        // moving vertically
                        if(unitDir.y>0){
                            // moving up
                            newPoint = new Vector2((point2.x + unitDir.x * searchRad * 2), (point2.y + unitDir.y * searchRad * 2));
                        } else {
                            // moving down
                            newPoint = new Vector2((point2.x + unitDir.x * searchRad * 2), (point2.y - unitDir.y * searchRad * 2));
                        }
                    } else if (unitDir.x > 0) {
                        // moving left to right
                        newPoint = new Vector2((point2.x + unitDir.x * searchRad * 2), (point2.y + unitDir.y * searchRad * 2));
                    } else {
                        newPoint = new Vector2((point2.x - unitDir.x * searchRad * 2), (point2.y - unitDir.y * searchRad * 2));
                    }
                    */
                    newPoint = new Vector2((point2.x + unitDir.x * searchRad * 3), (point2.y + unitDir.y * searchRad * 3));
                    if(newPoint.x<0){
                        newPoint.x=0;
                    }
                    if(newPoint.y<0){
                        newPoint.y=0;
                    }
                    if(newPoint.x>pixX){
                        newPoint.x=pixX;
                    }
                    if(newPoint.y>pixY){
                        newPoint.y=pixY;
                    }
                    // now we have new point and need to establish if it is within the image
                    boolean pixelInside = false;
                    pixel = image.getRGB(((int) newPoint.x), ((int) newPoint.y));
                    int alpha = (pixel >> 24) & 0xff;
                    if (alpha > 10) {
                        pixelInside = true;
                    } else {
                        pixelInside = false;
                    }
                    // so now we know if pixel inside/outside boundary
                    // find location where this changes
                    // search in 3x3 grid around current pixel
                    boolean foundDifference = false;
                    Vector2 point3 = new Vector2(0, 0);  // to contain potential new boundary point
                    while (!foundDifference) {
                        // keep searching and increasing radius until difference is found
                        if(searchRad>5){
                            System.out.println("");
                        }
                        Array<Boolean> searchPixInside = new Array<>();
                        Array<Vector2> searchPixCoords = new Array<>();
                        for (int theta = 0; theta<360;theta+=36) {
                            float tempX = (float) (newPoint.x + searchRad*Math.cos(theta));
                            float tempY = (float) (newPoint.y + searchRad*Math.sin(theta));
                            searchPixCoords.add( new Vector2(tempX,tempY));
                        }
                        for(int i=0;i<searchPixCoords.size;i++){
                            if (searchPixCoords.get(i).x < 0 || searchPixCoords.get(i).y < 0 || searchPixCoords.get(i).x >= pixX || searchPixCoords.get(i).y >= pixY) {
                                // out of bounds
                                searchPixInside.add(null);
                            } else {
                                //System.out.println("searchCoords X "+(searchPixCoords[1 + i1][1 + j1].x));
                                //System.out.println("searchCoords Y "+(searchPixCoords[1 + i1][1 + j1].y));
                                int tempC = image.getRGB((int) (searchPixCoords.get(i).x), (int) searchPixCoords.get(i).y);
                                int tempA = (tempC >> 24) & 0xff;
                                if (tempA > 10) {
                                    searchPixInside.add(true);
                                } else {
                                    searchPixInside.add(false);
                                }
                            }
                        }
                        double pointR = pixX * pixY;  // this will be distance between point2 and potential boundary condition
                        for (int i = 0; i < searchPixInside.size; i++) {
                                // search all to see if different to newPoint
                                if (searchPixInside.get(i) == null) {
                                    // do nothing as centre
                                } else {
                                    if (searchPixInside.get(i) != pixelInside) {
                                        // found a boundary
                                        double tempR = Math.sqrt(Math.pow((searchPixCoords.get(i).x - point2.x), 2) + Math.pow((searchPixCoords.get(i).y - point2.y), 2));
                                        if (true){//tempR < pointR) {
                                            // closer point potentially most likely one
                                            // check how colinear the points are
                                            boolean searchArray = true;
                                            boolean colinear = false;
                                            int aCount = 1;
                                            while(searchArray){
                                                Vector2 prevNode;
                                                /*
                                                if(aCount==0){
                                                    prevNode = initialBorder.get(initialBorder.size-1);
                                                } else {
                                                    prevNode = initialBorder.get(aCount-1);
                                                }
                                                */
                                                prevNode = initialBorder.get(aCount-1);
                                                Vector2 nextNode = initialBorder.get(aCount);
                                                if((nextNode.x-searchPixCoords.get(i).x)*(searchPixCoords.get(i).x - prevNode.x)>=0){
                                                    // the x coordinates of the point in question is inbetween that of two connected previous points
                                                    // check whether colinear and simply picking a point on a previous edge
                                                    double colin = searchPixCoords.get(i).x*(prevNode.y - nextNode.y) + prevNode.x*(nextNode.y - searchPixCoords.get(i).y) + nextNode.x*(searchPixCoords.get(i).y - prevNode.y);
                                                    double dR = Math.sqrt(Math.pow((prevNode.x - nextNode.x),2) + Math.pow((prevNode.y - nextNode.y),2));
                                                    double colPerLen = Math.abs(colin / dR);
                                                    if(colPerLen<4){
                                                        colinear = true;
                                                        searchArray = false;
                                                    }
                                                }
                                                aCount++;
                                                if(aCount>initialBorder.size-1){
                                                    // reached the end
                                                    searchArray = false;
                                                }
                                            }
                                            if(!colinear) {
                                                pointR = tempR;
                                                point3 = new Vector2(searchPixCoords.get(i).x, searchPixCoords.get(i).y);
                                                foundDifference = true;
                                            }
                                        }
                                    }
                                }

                        }
                        searchRad += searchRadMin;
                    }
                    buffer++;
                    initialBorder.add(point3);
                    point1 = point2;
                    point2 = point3;
                    if (buffer > 10) {
                        distPoint1 = Math.sqrt(Math.pow((point2.x - initPoint.x), 2) + Math.pow((point2.y - initPoint.y), 2));
                    }
                }
            }
        }

        System.out.println("INITIAL BORDER COMPLETE");

        Array<Vector2> ordered2 = new Array<>();
        ordered2.add(initialBorder.get(0));
        int prevN = 0;
        for(int i=1;i<initialBorder.size;i++){
        double dR = Math.sqrt(Math.pow((initialBorder.get(i).x - initialBorder.get(prevN).x),2) + Math.pow((initialBorder.get(i).y - initialBorder.get(prevN).y),2));
        if(dR>8){
            // if spacing is less than 5 pixels, add to ordered2 array
            ordered2.add(initialBorder.get(i));
            prevN = i;
        }
        }

        Array<Vector2> ordered3 = ordered2;
        Array<Vector2> ordered4 = new Array<Vector2>();
        // convert from image coordinates to world coordinates
        for(int i=0;i<ordered3.size;i++){
            Vector2 tempVec = new Vector2();
            tempVec.x = ordered3.get(i).x*proxObjects.get(proxN).getWidth()/image.getWidth()+proxObjects.get(proxN).getX();
            tempVec.y = (pixY-ordered3.get(i).y)*proxObjects.get(proxN).getHeight()/image.getHeight()+proxObjects.get(proxN).getY();
            ordered4.add(tempVec);
        }
/*

        Array<Vector2> ordered5 = new Array<>();
        boolean xSearch = false;    // if looking at points close in x
        boolean ySearch = false;    // if looking at points close in y
        ordered5.add(ordered4.get(0));
        prevN = 0;
        int tempN = 1;
        if((ordered4.get(tempN).y - ordered4.get(prevN).y)>0){
            // assume inline vertically
            xSearch = true;
        } else {
            ySearch = true;
        }
        for(int i=2;i<ordered4.size;i++){
            // go through each point to separate vertices if in-line
            double tempdX = ordered4.get(i).x - ordered4.get(prevN).x;
            double tempdY = ordered4.get(i).y - ordered4.get(prevN).y;
            if(xSearch){
                // already have points close in x
                if(tempdX>5){
                    // no longer close in x
                    ordered5.add(ordered4.get(tempN));
                    prevN = tempN;
                    tempN = i;
                    xSearch = false;
                }
            } else if(ySearch){
                // already have point close in y
                if(tempdY>5){
                    // no longer close in y
                    ordered5.add(ordered4.get(tempN));
                    prevN = tempN;
                    tempN = i;
                    ySearch = false;
                }
            } else {
                ordered5.add(ordered4.get(tempN));
                prevN = tempN;
                tempN = i;
                ySearch = false;
            }
            if(Math.abs(ordered4.get(tempN).x - ordered4.get(prevN).x)<5){
                // gap in x is small
                xSearch = true;
            } else if(Math.abs(ordered4.get(tempN).y - ordered4.get(prevN).y)<5){
                // gap in y is small
                ySearch = true;
            } else {
                // gap is good in both directions
            }
        }
*/

        boolean searching = true;
        int i = 1;

        while(searching){
            prevN = i - 1;
            int nextN = i + 1;
            if(prevN<0){
                prevN = ordered4.size-1;
            }
            if(nextN>ordered4.size-1){
                nextN = 0;
            }
            System.out.println("prevN "+prevN+" nextN "+nextN);
            Vector2 prevNode = ordered4.get(prevN);
            Vector2 nextNode = ordered4.get(nextN);
            double colin = ordered4.get(i).x*(prevNode.y - nextNode.y) + prevNode.x*(nextNode.y - ordered4.get(i).y) + nextNode.x*(ordered4.get(i).y - prevNode.y);
            double dR = Math.sqrt(Math.pow((prevNode.x - nextNode.x),2) + Math.pow((prevNode.y - nextNode.y),2));
            double colPerLen = Math.abs(colin / dR);
            if(colPerLen<4){
                // too colinear so remove
                ordered4.removeIndex(i);
                // start again
                i = 0;
            }
            i++;
            if(i>ordered4.size-1){
                searching = false;
            }
        }


        return ordered4;

    }

    public Array<Vector2> returnBorder3(BufferedImage image,int proxN){

        int pixX = image.getWidth();
        int pixY = image.getHeight();
        int incX = 5;
        int incY = 1;

        int pixels[][] = new int[pixX][pixY];
        Array<Vector2> border = new Array<Vector2>();
        //Vector2 border = new Vector2();

        for(int i=0;i<pixX;i+=incX){
            for(int j=0;j<pixY;j+=incY){
                pixels[i][j] = image.getRGB(i,j);
                int alpha = (pixels[i][j] >> 24) & 0xff;
                boolean bordPix = false;
                if(alpha>0){
                    // its a station pixel - check for a pixel around that is a border
                    if(i>0 && j>0) {
                        for (int i1=-1;i1<2;i1++) {
                            for (int j1=-1;j1<2;j1++){
                                int tempC = image.getRGB(i+i1,j+j1);
                                int tempA = (tempC >> 24) & 0xff;
                                if(tempA<10){
                                    bordPix = true;
                                }
                            }
                        }
                    }
                }
                if(bordPix){
                    border.add(new Vector2(i,pixY-j));
                }
            }
        }

        boolean searching = true;
        Array<Vector2> ordered2 = new Array<Vector2>();
        int tempN = 0;
        double tempR = pixX*pixY;
        for(int i=0;i<border.size;i++){
            if(border.get(i).x<tempR){
                // found a more left x coordinate
                // make first node in array
                tempR = border.get(i).x;
                tempN = i;
            }
        }
        ordered2.add(border.get(tempN));
        border.removeIndex(tempN);
        int prevN = 0;

        while(searching){
            // while there are still nodes left, add to ordered2 and remove from border next closest node
            tempR = pixX*pixY;
            for(int i=0;i<border.size;i++){
                double minR = Math.sqrt(Math.pow((border.get(i).x - ordered2.get(prevN).x),2) + Math.pow((border.get(i).y - ordered2.get(prevN).y),2));
                if(minR<tempR){
                    tempN = i;
                    tempR = minR;
                }
                ordered2.add(border.get(tempN));
                border.removeIndex(tempN);
                if(border.size<1){
                    searching = false;
                }
            }
        }

        System.out.println("border size "+border.size);
        Array<Vector2> ordered3 = new Array<Vector2>();
        ordered3.add(ordered2.get(0));
        prevN = 0;
        for(int i=1;i<ordered2.size;i++){
            double dR = Math.sqrt(Math.pow((ordered2.get(i).x - ordered2.get(prevN).x),2) + Math.pow((ordered2.get(i).y - ordered2.get(prevN).y),2));
            if(dR>8){
                // if spacing is less than 5 pixels, add to ordered2 array
                ordered3.add(ordered2.get(i));
                prevN = i;
            }
        }
        Array<Vector2> ordered4 = new Array<Vector2>();
        // convert from image coordinates to world coordinates
        for(int i=0;i<ordered3.size;i++){
            Vector2 tempVec = new Vector2();
            tempVec.x = ordered3.get(i).x*proxObjects.get(proxN).getWidth()/image.getWidth()+proxObjects.get(proxN).getX();
            tempVec.y = ordered3.get(i).y*proxObjects.get(proxN).getHeight()/image.getHeight()+proxObjects.get(proxN).getY();
            ordered4.add(tempVec);
        }

        return ordered4;

    }

    public Array<Vector3> returnShapes(Array<Vector2> ordered4){
        // add all connections between nodes as edges in order
        Array<Vector2> edges = new Array<>();
        for(int i=0;i<ordered4.size;i++){
            int[] tempInt = new int[2];
            if(i<ordered4.size-1){
                tempInt[0]=i;
                tempInt[1]=i+1;
            } else {
                tempInt[0] = i;
                tempInt[1] = 0;
            }
            edges.add(new Vector2(tempInt[0],tempInt[1]));
        }

        for(int i=0;i<ordered4.size;i++){
            int nMinus;
            int nPlus;

            // determine where current, previous and next nodes are
            nMinus = i - 1;
            nPlus = i + 1;
            if(nMinus<0){
                nMinus = ordered4.size - 1;
            }
            if(nPlus>ordered4.size-1){
                nPlus = 0;
            }

            Vector2 A = new Vector2((ordered4.get(nPlus).x - ordered4.get(i).x),(ordered4.get(nPlus).y - ordered4.get(i).y)); // from point n to n-1
            Vector2 C = new Vector2((ordered4.get(i).x - ordered4.get(nMinus).x),(ordered4.get(i).y - ordered4.get(nMinus).y)); // from point n to n+1

            Array<Vector2> tempArray = new Array<>();

            for(int j=0;j<ordered4.size;j++) {

                Vector2 B = new Vector2((ordered4.get(j).x - ordered4.get(i).x), (ordered4.get(j).y - ordered4.get(i).y)); // from point n to potentially visible point

                if(i==j){
                    // do nothing as the same
                } else {
                    // determine if reflex angle or not
                    boolean zerothCheck = true;
                    double m1 = (ordered4.get(i).y - ordered4.get(nMinus).y) / (ordered4.get(i).x - ordered4.get(nMinus).x);
                    double m2 = (ordered4.get(nPlus).y - ordered4.get(i).y) / (ordered4.get(nPlus).x - ordered4.get(i).x);
                    double mJ = (ordered4.get(j).y - ordered4.get(i).y) / (ordered4.get(j).x - ordered4.get(i).x);
                    double theta1 = Math.atan(m1);
                    double theta2 = Math.atan(m2);
                    double thetaJ = Math.atan(mJ);

                    double dm13 = Math.abs(mJ - m1) / Math.abs(m1);
                    double dm23 = Math.abs(m2 - mJ) / Math.abs(m2);
                    if (dm13 > 0.95 && dm13 < 1.05) {
                        // 5% of gradient so dont connect
                        zerothCheck = false;
                    }
                    if (dm23 > 0.95 && dm23 < 1.05) {
                        // within 5% of gradient so dont connect
                        zerothCheck = false;
                    }

                    zerothCheck = true;

                    if (zerothCheck) {

                        double AxB = A.x * B.y - A.y * B.x;
                        double AxC = A.x * C.y - A.y * C.x;
                        double CxB = C.x * B.y - C.y * B.x;
                        double CxA = C.x * A.y - C.y * B.x;

                        boolean firstCheck = false;

                        if (m1 > m2) {
                            // angle is not reflex
                            // use normal cross product to see if point is within angle of sight
                            if ((AxB * AxC >= 0) && (CxB * CxA >= 0)) {
                                // first check complete that destination is within angle
                                firstCheck = true;
                            }

                        } else {
                            // angle is reflex
                            // use opposite of normal cross product
                            if ((AxB * AxC >= 0) && (CxB * CxA >= 0)) {
                                // not what we want
                            } else {
                                // first check complete that destination is within angle
                                firstCheck = true;
                            }
                        }

                        boolean secondCheck = true; // assume true initially


                        if (firstCheck) {
                            // check point in question is within sight - i.e. not obscured by another line including new lines made across the polygon
                            double m3 = (ordered4.get(j).y - ordered4.get(i).y) / (ordered4.get(j).x - ordered4.get(i).x); // gradient from point n to point potentially in sight
                            for (int k = 0; k < edges.size; k++) {
                                // check each point pair
                                boolean secondPrime = true;
                                Vector2 start = ordered4.get(i);
                                Vector2 end = ordered4.get(j);
                                int k1 = (int) (edges.get(k).x);
                                int k2 = (int) (edges.get(k).y);
                                Vector vecK1 = ordered4.get(k1);
                                Vector vecK2 = ordered4.get(k2);
                                if (k1 == i || k2 == i) {
                                    if (k1 == j || k2 == j) {
                                        // both of these points are already linked
                                        secondPrime = false;
                                    }
                                }
                                if (secondPrime && secondCheck) {
                                    double m4 = (ordered4.get(k2).y - ordered4.get(k1).y) / (ordered4.get(k2).x - ordered4.get(k1).x);
                                    double c3 = ordered4.get(j).y - m3 * ordered4.get(j).x;
                                    double c4 = ordered4.get(k2).y - m4 * ordered4.get(k2).x;
                                    double xIntersect = (c3 - c4) / (m4 - m3);
                                    double yIntersect = m3 * xIntersect + c3;
                                    double xCross = (ordered4.get(k2).x - xIntersect) * (xIntersect - ordered4.get(k1).x);
                                    double yCross = (ordered4.get(k2).y - yIntersect) * (yIntersect - ordered4.get(k1).y);
                                    if (xCross > 0.01 && yCross > 0.01) {
                                        // there is a line in the way of line of sight
                                        secondCheck = false;
                                    }
                                }
                            }

                        }
                        if (firstCheck && secondCheck) {
                            // satisfies constraints, add node pair to edge array
                            boolean addNode = true;
                            for(int l=0;l<edges.size;l++){
                                int l1 = (int) edges.get(l).x;
                                int l2 = (int) edges.get(l).y;
                                if(i==l1 || i==l2){
                                    if(j==l1 || j==l2){
                                        // this edge already exists
                                        addNode = false;
                                    }
                                }
                            }
                            if(addNode) {
                                edges.add(new Vector2(i, j));
                            }
                        }
                    }
                }
            }
        }

        // now we have an array of edges that defines the polygon split entirely into triangles with no overlapping lines
        // next step is to follow these lines to form shapes
        Array<Vector3> shapes = new Array<>();
        for(int i=0;i<ordered4.size;i++){
            // for each node i, search for connecting nodes in edge j
            int node1 = i;
            Array<Integer> tempNodes = new Array<>();
            for(int j=0;j<edges.size;j++){
                // for each edge, see which contain current node
                if(edges.get(j).x == i){
                    // edge contains node - check what other node is
                    tempNodes.add((int) edges.get(j).y);
                }
                if(edges.get(j).y == i){
                    // edge contains node - check what other node is
                    tempNodes.add((int) edges.get(j).x);
                }
            }
            // now have all nodes connected to current node
            int node2;
            for(int j=0;j<tempNodes.size;j++){
                // for each tempNode, search edges for node that connects first and second
                node2 = tempNodes.get(j);
                for(int k=0;k<edges.size;k++){
                    if(edges.get(k).x == node2 || edges.get(k).y == node2){
                        // edge contains second node - check other node isnt first node
                        if(edges.get(k).x == node1 || edges.get(k).y == node1){
                            // edge contains first node - not the edge we want
                        } else {
                            // edge potentially contains the third node of the shape
                            int potNode3=-1;
                            boolean potenchShape = false;
                            if(edges.get(k).x == node2){
                                // this is the second node - we want the other one
                                potNode3 = (int) (edges.get(k).y);
                            }
                            if(edges.get(k).y == node2){
                                // this is the second node - we want the other one
                                potNode3 = (int) (edges.get(k).x);
                            }
                            int node3 = -1;
                            Array<Integer> thirdNodes = new Array<>();
                            for(int n1=0;n1<edges.size;n1++){
                                // for each edge, examine those containing the potential third node
                                if(edges.get(n1).x == node1){
                                    // edge contains node - check what other node is
                                    if(edges.get(n1).y == potNode3){
                                        // contains both edges
                                        potenchShape = true;
                                        thirdNodes.add(potNode3);
                                    }
                                }
                                // if not then check other way around
                                if(edges.get(n1).y == node1){
                                    // edge contains node - check what other node is
                                    if(edges.get(n1).x == potNode3){
                                        // contains both edges
                                        potenchShape = true;
                                        thirdNodes.add(potNode3);
                                    }
                                }
                            }
                            boolean createShape = true;
                            if(potenchShape){
                                if(thirdNodes.size>0) {
                                    for (int n2 = 0; n2 < thirdNodes.size; n2++) {
                                        node3 = (thirdNodes.get(n2));
                                        // check current shape list to see if shape already exists
                                        for (int l = 0; l < shapes.size; l++) {
                                            if (shapes.get(l).x == node1 || shapes.get(l).y == node1 || shapes.get(l).z == node1) {
                                                if (shapes.get(l).x == node2 || shapes.get(l).y == node2 || shapes.get(l).z == node2) {
                                                    if (shapes.get(l).x == node3 || shapes.get(l).y == node3 || shapes.get(l).z == node3) {
                                                        // shape already exists
                                                        createShape = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if(node1<0 || node2<0 || node3<0){
                                System.out.println("bade node");
                                createShape = false;
                            }
                            // otherwise free to create shape
                            if(createShape){
                                shapes.add(new Vector3(node1,node2,node3));
                            }
                        }
                    }
                }
            }
        }

        return shapes;
    }



    public Vector2 calcEscapeVel(double dX, double dY, double r,boolean orbitStar){
        // method to calculate velocity for planetary orbit
        float stellarMass;

        if(orbitStar){
            stellarMass = solarM;
        } else {
            stellarMass = planetM1;
        }

        int ome;
        if(dY>0){
            // player "above" planet
            if(playerVx>0){
                // travelling right and above so clockwise
                ome = 1;
            } else {
                // travelling left and above so anticlockwise
                ome = -1;
            }
        } else if(dY<0){
            // player "below" planet
            if(playerVx>0){
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
                if (playerVy > 0) {
                    // right and travelling up so anticlockwise
                    ome = -1;
                } else {
                    // right and travelling down so clockwise
                    ome = 1;
                }
            } else {
                // player left of planet
                if(playerVy>0){
                    // left and travelling up so clockwise
                    ome = 1;
                } else {
                    // left and travelling down so anticlockwise
                    ome = -1;
                }
            }
        }
        double V = Math.sqrt(G*stellarMass/Math.pow(r,(gravR-2)));
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

    // atlas for thrust control
    public TextureAtlas getThrustAt(){
        return thrustAt;
    }

    // atlas for hull health bars
    public TextureAtlas getHealthAt(){
        return healthAt;
    }

    // atlas for shield bars
    public TextureAtlas getShieldAt(){
        return shieldAt;
    }

    // atlas for ship icons
    public TextureAtlas getShipIconsAt() { return shipIconsAt; }

    // atlas for pointers in system view (ship ranks etc)
    public TextureAtlas getShipPointersAt() { return shipPointersAt; }

    @Override
    public void show () {
        System.out.println("play show");
        System.out.println("play show - created camera");
    }

    @Override
    public void render (float delta) {

        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        if(tracing) {
            game.batch.setProjectionMatrix(wStage.traceStage.getCamera().combined);
            wStage.traceStage.draw();
        }

        game.batch.setProjectionMatrix(wStage.backstage.getCamera().combined);
        wStage.backstage.draw();

        game.batch.setProjectionMatrix(wStage.stage.getCamera().combined);
        wStage.stage.draw();
        wStage.stage.act();

        game.batch.setProjectionMatrix(systemActors.stage.getCamera().combined);
        systemActors.stage.draw();
        systemActors.stage.act();

        game.batch.setProjectionMatrix(wStage.foreStage.getCamera().combined);
        wStage.foreStage.draw();

        if(stationSynced) {
            game.batch.setProjectionMatrix(proxStage.stage.getCamera().combined);
            proxStage.stage.draw();
            game.batch.setProjectionMatrix(proxStage.backstage.getCamera().combined);
            proxStage.backstage.draw();
        }

        if(paused) {
            game.batch.setProjectionMatrix(pauseHUD.stage.getCamera().combined);
            pauseHUD.stage.draw();
        }

    }

    @Override
    public void resize (int width, int height) {

        System.out.println("resize "+width+" by "+height);

        double newWidth;
        double newHeight;

        double currentAspect = width / height;
        double desiredAspect = game.aspect;
        if(currentAspect>desiredAspect){
            // width is larger than should be
            newHeight = height;
            newWidth = height*desiredAspect;
        } else {
            // height is larger than should be
            newWidth = width;
            newHeight = width/desiredAspect;
        }

        width = (int) newWidth;
        height = (int) newHeight;

        windowHeight = height;
        windowWidth = width;

        System.out.println("resize output "+width+" by "+height);

        wStage.stage.getViewport().update(width,height,true);
        wStage.traceStage.getViewport().update(width,height,true);
        gameport.update(width,height);
        gamecam.update();
        // update HUDs here
        //systemHUD.viewport.update(width,height);

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
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){                         // destination on screen Y
            if(!changedProx){//gamecam.zoom<maxZoom) {
                gamecam.zoom *= 1.02f;
                wStage.wStageCam.zoom*=1.02f;
            } else {
                proxStage.wStageCam.zoom*=1.02f;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            if(!changedProx){//gamecam.viewportWidth>minZoomX && gamecam.viewportHeight>minZoomY) {
                gamecam.zoom *= 0.98f;
                wStage.wStageCam.zoom *= 0.98f;
            } else {
                proxStage.wStageCam.zoom*=0.98f;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            // increase engine thrust
            if(playerThrust<100) {
                playerThrust++;
                systemActors.tBar.setRotation(-playerThrust * 180 / 100);
                systemActors.updateThrust(playerThrust);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            // increase engine thrust
            if(playerThrust>0) {
                playerThrust--;
                systemActors.tBar.setRotation(-playerThrust * 180 / 100);
                systemActors.updateThrust(playerThrust);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            if(!changedProx) {
            } else {
                proxAomega = 1;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            if(!changedProx) {
            } else {
                proxAomega = -1;
            }
        }


        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            if(!changedProx) {
                playerCamOffY--;
            } else {
                proxAyInt = 1;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            if(!changedProx) {
                playerCamOffY++;
            } else {
                proxAyInt = -1;
            }
        }
        if(!Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.S)) {
                proxAyInt = 0;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            if(!changedProx){
                playerCamOffX--;
            } else {
                proxAxInt = -1;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            if(!changedProx) {
                playerCamOffX++;
            } else {
                proxAxInt = 1;
            }
        }
        if(!Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.D)) {
                proxAxInt = 0;
            }
        }
        if(!Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.E)) {
                proxAomega = 0;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
            playerCamOffX=0;
            playerCamOffY=0;
        }


        if(Gdx.input.isKeyPressed(Input.Keys.H)){
            float aspectWoH = gamecam.viewportWidth / gamecam.viewportHeight;
            float height1 = gamecam.viewportHeight;
            float height2 = playerShipShown.getHeight();
            float backdropHeight = height2*2;
            float zoom = backdropHeight/height1;
            wStage.wStageCam.zoom = zoom;
            playerShipShown.transparentBox(backdropHeight,aspectWoH);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            int noSystems = playerShipShown.getNoSystems();
            int randSyst = (int) Math.round(Math.random()*noSystems);
            int curDamage = playerShipShown.getDamage(randSyst);
            int damage = (int) Math.round(Math.random()*curDamage);
            playerShipShown.updateDamage(randSyst,damage);
        }

    }

    public boolean touchDragged2(){//, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();
        gamecam.translate(-x,y);
        return true;
    }

    public void checkAirlocks(){
        // checks whether venting is still true
        int airlocksOpen = 0;
        for(int i=0;i<(vertices.length);i++) {
            if(vertices[i]==null){
            } else {
                if(vertices[i].thisRoom==null){
                } else {
                    if (vertices[i].thisRoom == Vertex.vertexRoom.door) {
                        // the vertex is a door
                        if(vertices[i].airlock){
                            // door is also an airlock
                            for(int j = 0;j<vertices[i].getNeighborCount();j++) {
                                Vertex tempVert1 = vertices[i].getNeighbor(j).getOne();
                                Vertex tempVert2 = vertices[i].getNeighbor(j).getTwo();
                                String tempVert1label = tempVert1.getLabel();
                                String tempVert2label = tempVert2.getLabel();
                                Vertex tempVert;
                                if (tempVert1.thisRoom == Vertex.vertexRoom.room) {
                                    // found room connected to airlock
                                    tempVert = tempVert1;
                                } else {
                                    tempVert = tempVert2;
                                }
                                if(vertices[i].doorOpen){
                                    tempVert.vacuuming = true;
                                    airlocksOpen++;
                                } else {
                                    tempVert.vacuuming = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(airlocksOpen>0){
            // at least one airlock still open
            venting = true;
        } else {
            venting = false;
            for(int i=0;i<(vertices.length);i++) {
                if (vertices[i] == null) {
                } else {
                    if (vertices[i].thisRoom == null) {
                    } else {
                        if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                            if(vertices[i].airlock){
                                vertices[i].vacuuming = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public void checkRoomVenting(){
        // initialise all rooms as not vacuuming
        for(int i=0;i<(vertices.length);i++) {
            if (vertices[i] == null) {
            } else {
                if (vertices[i].thisRoom == null) {
                } else {
                    if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                        if(vertices[i].airlock){

                        } else {
                            vertices[i].vacuuming = false;
                        }
                    }
                }
            }
        }
        // checks vacuum state of each room
        //System.out.println("checking room vacuums");
        for(int i=0;i<(vertices.length);i++) {
            if(vertices[i]==null){
            } else {
                if(vertices[i].thisRoom==null){
                } else {
                    if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                        // the vertex is a room
                        String temp = vertices[i].equip1;
                        String temp2 = vertices[i].equip2;
                        if(vertices[i].airlock){

                        } else {
                            String currentRoom = vertices[i].getLabel();
                            int ventingDoors = 0;
                            for (int j = 0; j < vertices[i].getNeighborCount(); j++) {
                                Vertex tempVert1 = vertices[i].getNeighbor(j).getOne();
                                Vertex tempVert2 = vertices[i].getNeighbor(j).getTwo();
                                if (tempVert1.thisRoom == Vertex.vertexRoom.door) {
                                    // found a door - check if its open
                                    if (tempVert1.doorOpen) {
                                        // door is open so check if room otherside is venting
                                        for (int k = 0; k < tempVert1.getNeighborCount(); k++) {
                                            Vertex tempVert3 = tempVert1.getNeighbor(k).getOne();
                                            Vertex tempVert4 = tempVert1.getNeighbor(k).getTwo();
                                            if (tempVert3.thisRoom == Vertex.vertexRoom.room) {
                                                if (tempVert3.getLabel() == currentRoom) {
                                                    // this room is the same room as we are in
                                                } else {
                                                    // found a connecting room
                                                    if (tempVert3.vacuuming) {
                                                        // connecting room is vacuuming so current room is also vacuuming
                                                        ventingDoors++;
                                                    }
                                                }
                                            }
                                            if (tempVert4.thisRoom == Vertex.vertexRoom.room) {
                                                if (tempVert4.getLabel() == currentRoom) {
                                                    // this room is the same room as we are in
                                                } else {
                                                    // found a connecting room
                                                    if (tempVert4.vacuuming) {
                                                        // connecting room is vacuuming so current room is also vacuuming
                                                        ventingDoors++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (tempVert2.thisRoom == Vertex.vertexRoom.door) {
                                    // found a door - check if its open
                                    if (tempVert2.doorOpen) {
                                        // door is open so check if room otherside is venting
                                        for (int k = 0; k < tempVert2.getNeighborCount(); k++) {
                                            Vertex tempVert3 = tempVert2.getNeighbor(k).getOne();
                                            Vertex tempVert4 = tempVert2.getNeighbor(k).getTwo();
                                            if (tempVert3.thisRoom == Vertex.vertexRoom.room) {
                                                if (tempVert3.getLabel() == currentRoom) {
                                                    // this room is the same room as we are in
                                                } else {
                                                    // found a connecting room
                                                    if (tempVert3.vacuuming) {
                                                        // connecting room is vacuuming so current room is also vacuuming
                                                        ventingDoors++;
                                                    }
                                                }
                                            }
                                            if (tempVert4.thisRoom == Vertex.vertexRoom.room) {
                                                if (tempVert4.getLabel() == currentRoom) {
                                                    // this room is the same room as we are in
                                                } else {
                                                    // found a connecting room
                                                    if (tempVert4.vacuuming) {
                                                        // connecting room is vacuuming so current room is also vacuuming
                                                        ventingDoors++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // checked connecting rooms in theory
                            if (ventingDoors > 0) {
                                // at least one room is venting with an open door
                                vertices[i].vacuuming = true;
                            } else {
                                vertices[i].vacuuming = false;
                            }
                        }
                    }
                }
            }
        }

    }

    public void checkVenting(float dt){
        int ventCheck = 0;
        if(venting){
            for(int i=0;i<(vertices.length);i++) {
                if(vertices[i]==null){
                } else {
                    if(vertices[i].thisRoom==null){
                    } else {
                        if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                            if (vertices[i].vacuuming) {
                                ventCheck++;
                                // room is under vacuum or linked to a room under vacuum
                                if (vertices[i].o2 > 0) {
                                    //System.out.println(vertices[i].getLabel() + " is venting. At " + vertices[i].o2 + "%");
                                    System.out.println("dt "+dt);
                                    vertices[i].o2 -= dt * 5;
                                }
                                System.out.println("vacuuming "+vertices[i].o2);

                                double a = 0.5-(0.5*vertices[i].o2/100);
                                if(a<0){ a=0; }
                                if(a>1){ a=1; }

                                playerShipShown.updateAir( (float) a,i);

                            } else {
                                if (vertices[i].o2 < 100) {
                                    //System.out.println(vertices[i].getLabel() + " is regenerating. At " + vertices[i].o2 + "%");
                                    vertices[i].o2 += dt * 5;

                                    //System.out.println("unvacuuming "+vertices[i].o2);

                                    double a = 1 - (vertices[i].o2 / 100);
                                    if (a < 0) {
                                        a = 0;
                                    }
                                    if (a > 1) {
                                        a = 1;
                                    }

                                    //System.out.println("a is "+a);

                                    playerShipShown.updateAir((float) a, i);
                                }

                            }
                        }
                    }
                }
            }
            //System.out.println("ventCheck is "+ventCheck);
        } else {
            for (int i = 0; i < (vertices.length); i++) {
                // search all vertices for rooms / tiles / doors
                if (vertices[i] == null) {
                } else {
                    if (vertices[i].thisRoom == null) {
                    } else {
                        if (vertices[i].thisRoom == Vertex.vertexRoom.room) {
                            if (vertices[i].vacuuming) {
                                System.out.println("vacuuming");
                                // room is under vacuum or linked to a room under vacuum
                                if (vertices[i].o2 > 0) {
                                    //System.out.println(vertices[i].getLabel() + " is venting. At " + vertices[i].o2 + "%");
                                    vertices[i].o2 -= dt * 1;
                                }
                            } else {
                                if (vertices[i].o2 < 100) {
                                    //System.out.println(vertices[i].getLabel() + " is regenerating. At " + vertices[i].o2 + "%");
                                    vertices[i].o2 += dt * 1;
                                }
                            }

                            if (vertices[i].o2 < 30) {
                                playerShipShown.updateAir(0.56f, i);
                            } else if (vertices[i].o2 < 40) {
                                playerShipShown.updateAir(0.48f, i);
                            } else if (vertices[i].o2 < 50) {
                                playerShipShown.updateAir(0.40f, i);
                            } else if (vertices[i].o2 < 60) {
                                playerShipShown.updateAir(0.32f, i);
                            } else if (vertices[i].o2 < 70) {
                                playerShipShown.updateAir(0.24f, i);
                            } else if (vertices[i].o2 < 80) {
                                playerShipShown.updateAir(0.16f, i);
                            } else if (vertices[i].o2 < 90) {
                                playerShipShown.updateAir(0.08f, i);
                            } else {
                                playerShipShown.updateAir(0, i);
                            }
                        }
                    }
                }
            }
        }
    }

    public Vector2 getPlayerPosition(){

        Vector2 playerPos = player.b2body.getPosition();

        return playerPos;
    }

    public double getPlayerPositionR(int bodyNum){
        Vector2 playerPos = player.b2body.getPosition();

        double distFromBody = Math.sqrt(Math.pow(gravData[bodyNum][1] - playerPos.x, 2) + Math.pow(gravData[bodyNum][2] - playerPos.y, 2));

        return distFromBody;
    }

    public double getPlayerSpeedR(){
        double playerSpeedR = Math.sqrt(Math.pow(playerVx,2)+Math.pow(playerVy,2));

        return playerSpeedR;
    }

    public void update(float dt){
        //System.out.println("update");
        if(rendering) {
            handleInput(dt);

            boolean statusCheck = false;

            if (!swtch) {
                playerShipShown.setUpShip();
                playerShipShown.resizeShip();
                swtch = true;
            }

            //world.step(1/60f,6,2);  //timeStep 60 per second
            accumulator += dt;

            float pX = 0;
            float pY = 0;

            double frameTime = 0;
            double newTime = TimeUtils.millis() / 1000.0;
            frameTime = newTime - currentTime;
            currentTime = newTime;
            float physTime = step;

            if (accumulator >= step) {
                doPhysics = true;
                statusCheck = true;
                accumulator -= step;
            }

            int i;

            if(!syncStation) {
                for (int k = 0; k < nP; k++) {
                    double distR = getPlayerPositionR(k + 1);
                    if (distR < wellsOuter[k].getWidth() / 2) {
                        // in gravity well
                        orbitNearestPlanet();
                    }
                }
            }

            if (doPhysics) {
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
                    } else {
                        dt = (float) frameTime;
                        double r;
                        float Ax = 0;
                        float Ay = 0;
                        playersX = player.b2body.getPosition().x;
                        playersY = player.b2body.getPosition().y;
                        if (orbitPlanet) {
                            r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - playersY, 2));
                            Ax = (float) (G * planetM1 * -(playersX - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            Ay = (float) (G * planetM1 * -(playersY - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            if (planetDeburn) {
                                G = G / 2;
                                Vector2 orbVel = calcEscapeVel((playersX - gravData[planetNum][1] / MyGdxGame.PPM), (playersY - gravData[planetNum][2] / MyGdxGame.PPM), r,false);
                                playerVx = orbVel.x;
                                playerVy = orbVel.y;
                                if (playerVx > orbVel.x || playerVy > orbVel.y) {
                                    if (playerVx > orbVel.x) {
                                        playerVx = orbVel.x;
                                        // Vx = Vx * 0.95f;
                                        planetDeburn = true;
                                    } else if (playerVy > orbVel.y) {
                                        playerVy = orbVel.y;
                                        planetDeburn = true;
                                    } else {
                                        planetDeburn = false;
                                    }
                                }
                                if (player.b2body.getLinearVelocity().x < orbVel.x && player.b2body.getLinearVelocity().y < orbVel.y) {
                                    playerVx = orbVel.x;
                                    playerVy = orbVel.y;
                                }
                                planetDeburn = false;
                            }

                            if (inCircle(gravData[planetNum][1] / MyGdxGame.PPM, gravData[planetNum][2] / MyGdxGame.PPM, gravData[planetNum][0] / MyGdxGame.PPM, player.b2body.getPosition().x, player.b2body.getPosition().y)) {
                                // transfer to planet screen
                                //game.setScreen(new practiceDynTiles(game));
                            }
                        } else {
                            double tempAx = 0;
                            double tempAy = 0;
                            double tempAr = 0;
                            double tempAr2 = 0;
                            if (!starOnly) {
                                for (i = 0; i < nP; i++) {
                                    r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - playersY, 2));
                                    if (r < planetOrbDia) {
                                        // within orbit of planet
                                        orbitPlanet = true;
                                        planetNum = i + 1;
                                        planetDeburn = true;
                                    }
                                    if(r<minR){
                                        r=minR;
                                    }
                                    Ax = Ax + (float) (G * planetM1 * -(playersX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                                    Ay = Ay + (float) (G * planetM1 * -(playersY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                                    tempAr2 = Math.sqrt(Math.pow(Ax,2) + Math.pow(Ay,2));
                                    if(tempAr2>tempAr){
                                        tempAr = tempAr2;
                                        tempAx = Ax;
                                        tempAy = Ay;
                                    }
                                }
                            }
                            r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - playersY, 2));
                            if(r<minR){
                                r=minR;
                            }
                            Ax = Ax + (float) (G * solarM * -(playersX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            Ay = Ay + (float) (G * solarM * -(playersY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            tempAr = Math.sqrt(Math.pow(Ax,2) + Math.pow(Ay,2));
                            if(tempAr2>tempAr){
                                tempAr = tempAr2;
                                tempAx = Ax;
                                tempAy = Ay;
                            }
                            //Ax = (float) tempAx;
                            //Ay = (float) tempAy;
                        }
                        if (engBurn) {
                            // apply acceleration due to engine burn here
                            Ax = Ax + engBurnX;//((playerThrust * maxBurnThrust * burnX / burnR) / 100);
                            Ay = Ay + engBurnY;//((playerThrust * maxBurnThrust * burnY / burnR) / 100);
                            System.out.println("engburnX "+engBurnX+" engburnY "+engBurnY);
                            if(targeting) {
                                targeting = false;
                                orbitingPlanet = false;
                                orbitingStar = false;
                                allStop = false;
                                tracing = true;
                            }
                        }

                        if(targeting){

                            if(orbitingPlanet || orbitingStar){

                                float targetZoom = windowHeight/(wellsOuter[targetBody].getWidth()*1.5f);
                                if(wStage.wStageCam.zoom>targetZoom){
                                    wStage.wStageCam.zoom*=0.98f;
                                }

                                tracing = false;

                                float margin = game.V_WIDTH/100;
                                double currentRad = getPlayerPositionR(targetBody);
                                double currentMargin = Math.abs(currentRad-targetRad);
                                if(!syncStation) {
                                    if (currentMargin / targetRad < 0.01) {
                                        // within 1% of target radius
                                        syncStation();
                                    }
                                }

                                boolean newMethod = true;

                                if(newMethod){
                                    double sX = playersX - gravData[targetBody][1];
                                    double sY = playersY - gravData[targetBody][2];
                                    double sR = Math.sqrt(Math.pow(sX,2) + Math.pow(sY,2));
                                    double gravM = sY/sX;
                                    double sM = playerVy / playerVx;
                                    targetAngle1 = targetAngle2;
                                    targetAngle2 = Math.atan(sY/sX);
                                    if(sX>0){
                                        if(sY>0){
                                            // angle 0 to 90
                                            // just targetAngle2
                                        } else {
                                            // angle 270 to 360
                                            targetAngle2 = 2*Math.PI + targetAngle2;
                                        }
                                    } else {
                                        if(sY>0){
                                            // angle 90 to 180
                                            targetAngle2 = Math.PI + targetAngle2;
                                        } else {
                                            // angle 180 to 270
                                            targetAngle2 = Math.PI + targetAngle2;
                                        }
                                    }
                                    double targetAngle3 = 0;
                                    if(prefDirAC){
                                        // going anticlockwise
                                        targetDir = -1;
                                        targetAngle3 = targetAngle2 + 5 * Math.PI / 180;
                                    } else {
                                        if (targetAngle1 > targetAngle2) {
                                            // going clockwise
                                            targetDir = 1;
                                            targetAngle3 = targetAngle2 - 5 * Math.PI / 180;
                                        } else {
                                            // going anticlockwise
                                            targetDir = -1;
                                            targetAngle3 = targetAngle2 + 5 * Math.PI / 180;
                                        }
                                    }
                                    if(targetAngle3>(360*Math.PI/180)){
                                        targetAngle3 -= (360*Math.PI/180);
                                    }
                                    if(targetAngle3<0){
                                        targetAngle3 += (360*Math.PI/180);
                                    }
                                    if(Math.abs(targetAngle1-targetAngle2)>Math.PI){
                                        // crossed a boundary
                                        if(targetAngle2>targetAngle1){
                                            // going clockwise from top right to bottom right
                                            targetAngle3 = targetAngle2 - 5*Math.PI/180;
                                        } else {
                                            targetAngle3 = targetAngle2 + 5*Math.PI/180;
                                        }
                                    }
                                    double target3X = gravData[targetBody][1] + targetRad*Math.cos(targetAngle3);
                                    double target3Y = gravData[targetBody][2] +targetRad*Math.sin(targetAngle3);

                                    double dX = target3X - playersX;
                                    double dY = target3Y - playersY;
                                    double dR = Math.sqrt( Math.pow(dX,2) + Math.pow(dY,2));
                                    double xRatio1 = dX/dR;
                                    double xRatio2 = dY/dR;

                                    double xRatio = sX/sR;
                                    double yRatio = sY/sR;

                                    double tangX = xRatio*targetRad;
                                    double tangY = yRatio*targetRad;
                                    double tangM = -1/gravM;
                                    boolean orbitStar;
                                    if(targetBody>0){
                                        orbitStar = false;
                                    } else {
                                        orbitStar = true;
                                    }
                                    Vector2 tangV = calcEscapeVel((tangX - gravData[targetBody][1]), (tangY - gravData[targetBody][2]), targetRad, orbitStar);
                                    double targetOmega = Math.PI/30;
                                    if(syncStation){
                                        double maxOmega = Math.PI/10;
                                        double stationX = stations[targetBody-1].getX() + stations[targetBody-1].getWidth()/2;
                                        double stationY = stations[targetBody-1].getY() + stations[targetBody-1].getHeight()/2;
                                        double stationR = Math.sqrt( Math.pow((stationX - playersX),2) + Math.pow((stationY - playersY),2) );
                                        double maxR = wells[targetBody].getWidth();
                                        // interpolate to decrease speed as catching up to station
                                        targetOmega = ( (maxOmega - 0.99*stationOmega) / maxR )*stationR + 0.99*stationOmega;
                                        double perR = stationR/maxR;
                                        if(perR<0.05){
                                            System.out.println("STATION STNCED");
                                            targetOmega = stationOmega;
                                            stationSynced = true;
                                            // need to set up station first time
                                            if(!changedProx){
                                                setupProximity();
                                            }
                                        }
                                    }
                                    double tangVr = targetRad*targetOmega;// Math.sqrt( Math.pow(tangV.x,2) + Math.pow(tangV.y,2) );
                                    double tangVx = Math.signum(playerVx)*Math.sqrt( Math.pow(tangVr,2) / (1+Math.pow(tangM,2)) );
                                    double tangVy = tangVx * (tangM);
                                    //System.out.println("tangX "+tangX+" tangY "+tangY+" tangM "+tangM);
                                    //System.out.println("tangVx "+tangVx+" tangVy "+tangVy);
                                    //System.out.println("playerVx "+playerVx+" playerVy "+playerVy);
                                    double targetM = (sM + tangM)/2;
                                    double turnM = currentMargin/1000;
                                    if(playerVy>0) {
                                        if ((currentRad - targetRad) > 0) {
                                            if (tangM > 0) {
                                                if (sX < 0) {
                                                    targetM = tangM - turnM;
                                                } else {
                                                    targetM = tangM + turnM;
                                                }
                                            } else {
                                                if (sX < 0) {
                                                    targetM = tangM + turnM;
                                                } else {
                                                    targetM = tangM - turnM;
                                                }
                                            }
                                        } else {
                                            if (tangM > 0) {
                                                if (sX < 0) {
                                                    targetM = tangM + turnM;
                                                } else {
                                                    targetM = tangM - turnM;
                                                }
                                            } else {
                                                if (sX < 0) {
                                                    targetM = tangM - turnM;
                                                } else {
                                                    targetM = tangM + turnM;
                                                }
                                            }
                                        }
                                    } else {
                                        if ((currentRad - targetRad) > 0) {
                                            if (tangM > 0) {
                                                if (sX < 0) {
                                                    targetM = tangM + turnM;
                                                } else {
                                                    targetM = tangM - turnM;
                                                }
                                            } else {
                                                if (sX < 0) {
                                                    targetM = tangM + turnM;
                                                } else {
                                                    targetM = tangM - turnM;
                                                }
                                            }
                                        } else {
                                            if (tangM > 0) {
                                                if (sX < 0) {
                                                    targetM = tangM - turnM;
                                                } else {
                                                    targetM = tangM + turnM;
                                                }
                                            } else {
                                                if (sX < 0) {
                                                    targetM = tangM + turnM;
                                                } else {
                                                    targetM = tangM - turnM;
                                                }
                                            }
                                        }
                                    }

                                    double targetVx;
                                    double targetVy;

                                    if(sY>0){
                                        // go down
                                        targetVy = -Math.sqrt( Math.pow(tangVr,2) / (1+1/Math.pow(targetM,2)) );
                                        targetVx = targetVy / (targetM);
                                    } else {
                                        // go up
                                        targetVx =Math.signum(playerVx)*Math.sqrt( Math.pow(tangVr,2) / (1+Math.pow(targetM,2)) );
                                        targetVy = targetVx * (targetM);
                                        targetVy = Math.sqrt( Math.pow(tangVr,2) / (1+1/Math.pow(targetM,2)) );
                                        targetVx = targetVy / (targetM);
                                    }

                                    targetVx = tangVr*xRatio1;
                                    targetVy = tangVr*xRatio2;
                                    double tempA = maxBurnThrust/800;
                                    if(orbitingPlanet) {
                                        tempA = maxBurnThrust/800;
                                    }
                                    double tempAx = Math.sqrt( Math.pow(tempA,2) / (1 + Math.pow(targetM,2)) );
                                    double tempAy = tempAx*targetM;
                                    //System.out.println("targetVx "+targetVx+" targetVy "+targetVy);//+" targetM "+targetM);
                                    //System.out.println("playerVx "+playerVx+" playerVy "+playerVy);

                                    double aXm = (targetVx - playerVx) / dt;
                                    double aYm = (targetVy - playerVy) / dt;
                                    //System.out.println("aX "+aXm+" aY "+aYm);
                                    double aRm = Math.sqrt(Math.pow(aXm, 2) + Math.pow(aYm, 2));
                                    double tanXratio = aXm / aRm;
                                    double tanYratio = aYm / aRm;
                                    if (aRm > maxBurnThrust) {
                                        aXm = tanXratio * maxBurnThrust;
                                        aYm = tanYratio * maxBurnThrust;
                                    }

                                    Ax = (float) (0 + aXm);
                                    Ay = (float) (0 + aYm);

                                    //System.out.println("-------------------------------------------------------------------------");


                                } else {

                                    if (currentMargin > margin) {
                                        // outside margin so do position matching
                                        double aA = 1 + Math.pow(playersX, 2) / Math.pow(playersY, 2);
                                        double aB = (-2) * playersX * Math.pow(targetRad, 2) / Math.pow(playersY, 2);
                                        double aC = Math.pow(targetRad, 4) / Math.pow(playersY, 2) - Math.pow(targetRad, 2);
                                        double aBsqrd = Math.pow(aB, 2);
                                        double a4ac = 4 * aA * aC;
                                        double aBsqrdMina4ac = aBsqrd - a4ac;
                                        double aroot = Math.sqrt(aBsqrdMina4ac);
                                        /*
                                        System.out.println("-ab " + (-aB) + " root " + aroot);
                                        System.out.println("inside root " + aBsqrdMina4ac);
                                        System.out.println("absqrd " + aBsqrd + " 4ac " + a4ac);
                                        System.out.println("A " + aA + " B " + aB + " C " + aC);
                                        System.out.println("playerX " + playersX + " playerY " + playersY);
                                        System.out.println("star at " + gravData[0][1] + " by " + gravData[0][2]);
                                        System.out.println("playerR " + getPlayerPositionR(0));
                                        System.out.println("targetRad " + targetRad);
                                        System.out.println("maxOrbRad " + maxOrbRad);
*/
                                        double a1 = (-aB + Math.sqrt(Math.pow(aB, 2) - 4 * aA * aC)) / (2 * aA);
                                        double b11 = Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a1, 2));
                                        double b12 = -Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a1, 2));
                                        double b1;
                                        if ((playersY - b11) < (playersY - b12)) {
                                            b1 = b11;
                                        } else {
                                            b1 = b12;
                                        }
                                        double m1 = (b1 - playersY) / (a1 - playersX);
                                        double a2 = (-aB - Math.sqrt(Math.pow(aB, 2) - 4 * aA * aC)) / (2 * aA);
                                        double b21 = Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a2, 2));
                                        double b22 = Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a2, 2));
                                        double b2;
                                        if ((playersY - b21) < (playersY - b22)) {
                                            b2 = b21;
                                        } else {
                                            b2 = b22;
                                        }
                                        double m2 = (b2 - playersY) / (a2 - playersX);

                                        System.out.println("m1 " + m1 + " x1 " + a1 + " y1 " + b1);
                                        System.out.println("m2 " + m2 + " x2 " + a2 + " y1 " + b2);
                                        System.out.println("-------------------------------------------------------------------------------");

                                        double mCur = playerVy / playerVx;

                                        double mDiff1 = Math.abs(m1 - mCur);
                                        double mDiff2 = Math.abs(m2 - mCur);
                                        double m;
                                        double x;
                                        double y;

                                        System.out.println("mDiff1 " + mDiff1 + " mDiff2 " + mDiff2);

                                        if (mDiff2 > mDiff1) {
                                            m = m1;
                                            x = a1;
                                            y = b1;
                                        } else {
                                            m = m2;
                                            x = a2;
                                            y = b2;
                                        }

                                        System.out.println("m " + m + " x " + x + " y " + y);

                                        test.setPosition((float) a1, (float) b1);
                                        test2.setPosition((float) a2, (float) b2);

                                        double tanDx = x - playersX;
                                        double tanDy = y - playersY;
                                        double tanDr = Math.sqrt(Math.pow(tanDx, 2) + Math.pow(tanDy, 2));
                                        double tanXratio = tanDx / tanDr;
                                        double tanYratio = tanDy / tanDr;
                                        Vector2 tanV = calcEscapeVel((x - gravData[0][1]), (y - gravData[0][2]), targetRad, true);
                                        double tanVr = Math.sqrt(Math.pow(tanV.x, 2) + Math.pow(tanV.y, 2));
                                        double tanVx = tanXratio * tanVr;//*tangentPer;
                                        double tanVy = tanYratio * tanVr;//*tangentPer;

                                        System.out.println("playerVx " + playerVx + " playerVy " + playerVy);
                                        System.out.println("tanVx " + tanV.x + " tanVy " + tanV.y);
                                        // parameters for manoeuvre from current position to tangent
                                        double sX = x - playersX;
                                        double sY = y - playersY;
                                        double aXm = 2 * (sX - playerVx * dt) / Math.pow(dt, 2);//(tanV.x - playerVx)/dt;
                                        double aYm = 2 * (sY - playerVy * dt) / Math.pow(dt, 2);//(tanV.y - playerVy)/dt;
                                        double aRm = Math.sqrt(Math.pow(aXm, 2) + Math.pow(aYm, 2));
                                        tanXratio = aXm / aRm;
                                        tanYratio = aYm / aRm;
                                        if (aRm > maxBurnThrust) {
                                            aXm = tanXratio * maxBurnThrust;
                                            aYm = tanYratio * maxBurnThrust;
                                        }
                                        Ax = (float) (0 + aXm);
                                        Ay = (float) (0 + aYm);
                                        System.out.println("aXm " + aXm + " aYm " + aYm);
                                        System.out.println("aX " + Ax + " Ay " + Ay);
                                    } else {

                                        // within margin so velocity matching
                                        double aA = 1 + Math.pow(playersX, 2) / Math.pow(playersY, 2);
                                        double aB = (-2) * playersX * Math.pow(targetRad, 2) / Math.pow(playersY, 2);
                                        double aC = Math.pow(targetRad, 4) / Math.pow(playersY, 2) - Math.pow(targetRad, 2);
                                        double aBsqrd = Math.pow(aB, 2);
                                        double a4ac = 4 * aA * aC;
                                        double aBsqrdMina4ac = aBsqrd - a4ac;
                                        double aroot = Math.sqrt(aBsqrdMina4ac);
                                        System.out.println("-ab " + (-aB) + " root " + aroot);
                                        System.out.println("inside root " + aBsqrdMina4ac);
                                        System.out.println("absqrd " + aBsqrd + " 4ac " + a4ac);
                                        System.out.println("A " + aA + " B " + aB + " C " + aC);
                                        System.out.println("playerX " + playersX + " playerY " + playersY);
                                        System.out.println("star at " + gravData[0][1] + " by " + gravData[0][2]);
                                        System.out.println("playerR " + getPlayerPositionR(0));
                                        System.out.println("targetRad " + targetRad);
                                        System.out.println("maxOrbRad " + maxOrbRad);

                                        double a1 = (-aB + Math.sqrt(Math.pow(aB, 2) - 4 * aA * aC)) / (2 * aA);
                                        double b11 = Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a1, 2));
                                        double b12 = -Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a1, 2));
                                        double b1;
                                        if ((playersY - b11) < (playersY - b12)) {
                                            b1 = b11;
                                        } else {
                                            b1 = b12;
                                        }
                                        double m1 = (b1 - playersY) / (a1 - playersX);
                                        double a2 = (-aB - Math.sqrt(Math.pow(aB, 2) - 4 * aA * aC)) / (2 * aA);
                                        double b21 = Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a2, 2));
                                        double b22 = Math.sqrt(Math.pow(targetRad, 2) - Math.pow(a2, 2));
                                        double b2;
                                        if ((playersY - b21) < (playersY - b22)) {
                                            b2 = b21;
                                        } else {
                                            b2 = b22;
                                        }
                                        double m2 = (b2 - playersY) / (a2 - playersX);

                                        System.out.println("m1 " + m1 + " x1 " + a1 + " y1 " + b1);
                                        System.out.println("m2 " + m2 + " x2 " + a2 + " y1 " + b2);
                                        System.out.println("-------------------------------------------------------------------------------");

                                        double mCur = playerVy / playerVx;

                                        double mDiff1 = Math.abs(m1 - mCur);
                                        double mDiff2 = Math.abs(m2 - mCur);
                                        double m;
                                        double x;
                                        double y;

                                        System.out.println("mDiff1 " + mDiff1 + " mDiff2 " + mDiff2);

                                        if (mDiff2 > mDiff1) {
                                            m = m1;
                                            x = a1;
                                            y = b1;
                                        } else {
                                            m = m2;
                                            x = a2;
                                            y = b2;
                                        }

                                        System.out.println("m " + m + " x " + x + " y " + y);

                                        test.setPosition((float) a1, (float) b1);
                                        test2.setPosition((float) a2, (float) b2);

                                        double tanDx = x - playersX;
                                        double tanDy = y - playersY;
                                        double tanDr = Math.sqrt(Math.pow(tanDx, 2) + Math.pow(tanDy, 2));
                                        double tanXratio = tanDx / tanDr;
                                        double tanYratio = tanDy / tanDr;
                                        boolean orbitStar;
                                        if(targetBody>0){
                                            orbitStar = false;
                                        } else {
                                            orbitStar = true;
                                        }
                                        Vector2 tanV = calcEscapeVel((x - gravData[targetBody][1]), (y - gravData[0][2]), targetRad, orbitStar);
                                        double tanVr = Math.sqrt(Math.pow(tanV.x, 2) + Math.pow(tanV.y, 2));
                                        double tanVx = tanXratio * tanVr;//*tangentPer;
                                        double tanVy = tanYratio * tanVr;//*tangentPer;

                                        System.out.println("playerVx " + playerVx + " playerVy " + playerVy);
                                        System.out.println("tanVx " + tanV.x + " tanVy " + tanV.y);
                                        // parameters for manoeuvre from current position to tangent
                                        double sX = x - playersX;
                                        double sY = y - playersY;
                                        double aXm = (tanV.x - playerVx) / dt;
                                        double aYm = (tanV.y - playerVy) / dt;
                                        double aRm = Math.sqrt(Math.pow(aXm, 2) + Math.pow(aYm, 2));
                                        tanXratio = aXm / aRm;
                                        tanYratio = aYm / aRm;
                                        if (aRm > maxBurnThrust) {
                                            aXm = tanXratio * maxBurnThrust;
                                            aYm = tanYratio * maxBurnThrust;
                                        }
                                        Ax = (float) (0 + aXm);
                                        Ay = (float) (0 + aYm);
                                        System.out.println("aXm " + aXm + " aYm " + aYm);
                                        System.out.println("aX " + Ax + " Ay " + Ay);

                                    }
                                }
                                //playerVx = newVelocity.x;
                                //playerVy = newVelocity.y;
                            } else if(allStop){
                                System.out.println("Stopping");
                                Vector2 A = allStop();
                                Ax = A.x;
                                Ay = A.y;
                            }
                        }

                        playerVx = (playerVx + Ax * dt);                    // new velocity at end of timestep
                        pX = (playerVx) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                        playerVy = (playerVy + Ay * dt);                    // new velocity at end of timestep
                        pY = (playerVy) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                        playersX = playersX + pX;
                        playersY = playersY + pY;
                        player.b2body.setTransform(playersX, playersY, 0);
                    }
                }
                world.step((float) physTime, 6, 2);
                setPlayerAngle();
                player.update((float) physTime);
                //playerLevelsInterm.b2body.setTransform(player.b2body.getPosition().x, player.b2body.getPosition().y, 0);
                //playerShipShown.setPosition(player.b2body.getPosition().x - playerShipShown.getWidth() / 2, player.b2body.getPosition().y - playerShipShown.getHeight() / 2);
                //playerLevelsInterm.update(physTime);

                if (tracing) {
                    getTrace(physTime);
                    wStage.traceStage.clear();
                    for (i = 0; i < pathCount - 1; i++) {
                        float x1 = path.get(i).x;
                        float x2 = path.get(i + 1).x;
                        float y1 = path.get(i).y;
                        float y2 = path.get(i + 1).y;
                        float dx = (x2 - x1);
                        float dy = (y2 - y1);
                        wStage.traceStage.addActor(new traceImage(game, this, x1, y1, dx, dy));
                    }
                }

                boolean colouring = false;
                if (colouring) {
                    if (wStage.wStageCam.zoom > 0.9 && level != Level.HIGHEST) {
                        // Change to HIGHEST level
                        Color tempColor = player.getColor();
                        player.setColor(tempColor.r, tempColor.g, tempColor.b, 1);
                        tempColor = playerLevelsInterm.getColor();
                        playerLevelsInterm.setColor(tempColor.r, tempColor.g, tempColor.b, 0);
                        tempColor = playerShipShown.getColor();
                        playerShipShown.setColor(tempColor.r, tempColor.g, tempColor.b, 0);
                        level = Level.HIGHEST;
                    } else if (wStage.wStageCam.zoom > 0.7 && level != Level.MIDDLE) {
                        // Change to MIDDLE level
                        Color tempColor = player.getColor();
                        player.setColor(tempColor.r, tempColor.g, tempColor.b, 0);
                        tempColor = playerLevelsInterm.getColor();
                        playerLevelsInterm.setColor(tempColor.r, tempColor.g, tempColor.b, 1);
                        tempColor = playerShipShown.getColor();
                        playerShipShown.setColor(tempColor.r, tempColor.g, tempColor.b, 0);
                        level = Level.MIDDLE;
                    } else if (wStage.wStageCam.zoom > 0.5 && level != Level.LOWEST) {
                        // Change to MIDDLE level
                        Color tempColor = player.getColor();
                        player.setColor(tempColor.r, tempColor.g, tempColor.b, 0);
                        tempColor = playerLevelsInterm.getColor();
                        playerLevelsInterm.setColor(tempColor.r, tempColor.g, tempColor.b, 0);
                        tempColor = playerShipShown.getColor();
                        playerShipShown.setColor(tempColor.r, tempColor.g, tempColor.b, 1);
                        level = Level.LOWEST;
                    }
                } else {
                    Color tempColor = player.getColor();
                    player.setColor(tempColor.r, tempColor.g, tempColor.b, 255);
                }
                checkVenting(dt);
            }
            // check if ship is too far from star
            if(outsideSystem()){
                // if so start burn such that ship circles star
                //System.out.println("outside system");

                if(!orbitingStar) {
                    orbitStar(true);
                }

            }


            if (statusCheck) {
                double r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - playersY, 2));
                shipManager.updateShipTempRad(r,starTemp,starRad,dt);
            }

            updateStations(dt);
            if(changedProx) {
                updateProximity(dt);
            }

            doPhysics = false;

            // Get camera to follow sprite x and y movements
            wStage.wStageCam.position.x = player.b2body.getPosition().x + playerCamOffX;//playerShipShown.getX()+playerShipShown.getWidth()/2;
            wStage.wStageCam.position.y = player.b2body.getPosition().y + playerCamOffY;//playerShipShown.getY()+playerShipShown.getHeight()/2;
            wStage.wStageCam.update();

            // update the star graphic
            starr.update(physTime);
            playerShipShown.update(physTime);

            checkPlayerGauge();

            // update camera and render
            gamecam.update();
            renderer.setView(gamecam);
        }
    }

    public void updateProximity(float dt){

        double A = 25; // px/s

        double Ax = A*proxAxInt;
        double Ay = A*proxAyInt;
        double Aomega = A*proxAomega/100;

        proxVx = (float) (proxVx + Ax*dt);
        proxVy = (float) (proxVy + Ay*dt);
        proxOmega = (float) (proxOmega + Aomega*dt);

        float proxSx = proxVx*dt;
        float proxSy = proxVy*dt;
        System.out.println("proxOmega "+proxOmega);
        //playerProx.rotateBody(proxOmega);
        proxStage.wStageCam.rotate(proxOmega);

        //playerProx.b2body.setTransform(playerProx.b2body.getPosition().x+proxSx,playerProx.b2body.getPosition().y+proxSy,0);
        //playerProx.update(0);

        playerProx.setCentre(playerProx.getCentre().x+proxSx,playerProx.getCentre().y+proxSy);
        playerProx.setPosition(playerProx.getCentre().x-playerProx.getWidth()/2,playerProx.getCentre().y-playerProx.getHeight()/2);
        playerShipShown.setPosition(playerProx.getX(),playerProx.getY());

        proxStage.wStageCam.position.x = playerProx.getCentre().x;////playerProx.getX() + playerProx.getWidth()/2;
        proxStage.wStageCam.position.y = playerProx.getCentre().y;//playerProx.getY() + playerProx.getHeight()/2;

        // check within reasonable radius to see whether to check for collision
        System.out.println("colTime "+colTime);
        if(colTime>3) {
            System.out.println("time high");
            boolean colSearch = true;
            int i = 0;
            if(playerProx.getCentre().y<(proxObjects.get(3).getY()+proxObjects.get(3).getHeight())){
                System.out.println("below top of dock point");
            }
            if(playerProx.getCentre().y<-1075){
                System.out.println("in shape?");
            }
            while(colSearch){
                if(proxObjects.get(i).getCollision()) {
                    double dx = proxObjects.get(i).getCentre().x - playerProx.getCentre().x;
                    double dy = proxObjects.get(i).getCentre().y - playerProx.getCentre().y;
                    double dr = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                    if (dr < proxObjects.get(i).getWidth()) {
                        // check if corners in volume
                        int colCount = 0;
                        int chosenShape = proxObjects.get(i).checkInsidePoly(playerProx.getCentre());
                        //for(int c=0;c<playerProx.getRectCorners().size;c++) {
                            if (chosenShape>=0) {
                                // point inside POLYGON
                                System.out.println("yo inside the polygon " + i + " yo");
                                colCount++;
                                proxCollision(i,colCount,chosenShape);
                                colSearch = false;
                            }
                        //}
                    }
                }
                i++;
                if(i>proxObjects.size-1){
                    colSearch = false;
                }
            }
        }
        colTime += dt;

    }

    public void proxCollision(int n,int colCount,int chosenShape){
        double d1 = 99999999;
        double d2 = d1*2;
        int p1 = 0;
        int p2 = 0;
        for(int i=0;i<3;i++){
            int shapeVert1 = (int) proxObjects.get(n).getShapes().get(chosenShape).x;
            int shapeVert2 = (int) proxObjects.get(n).getShapes().get(chosenShape).y;
            int shapeVert3 = (int) proxObjects.get(n).getShapes().get(chosenShape).z;
            double dx1 = proxObjects.get(n).getVertices().get(shapeVert1).x - playerProx.getCentre().x;
            double dy1 = proxObjects.get(n).getVertices().get(shapeVert1).y - playerProx.getCentre().y;
            double dx2 = proxObjects.get(n).getVertices().get(shapeVert2).x - playerProx.getCentre().x;
            double dy2 = proxObjects.get(n).getVertices().get(shapeVert2).y - playerProx.getCentre().y;
            double dx3 = proxObjects.get(n).getVertices().get(shapeVert3).x - playerProx.getCentre().x;
            double dy3 = proxObjects.get(n).getVertices().get(shapeVert3).y - playerProx.getCentre().y;
            double dr = Math.sqrt(Math.pow(dx1, 2) + Math.pow(dy1, 2));
            p1 = shapeVert1;
            d1 = dr;
            dr = Math.sqrt(Math.pow(dx2, 2) + Math.pow(dy2, 2));
            if(dr<d1){
                // vert2 is closer than vert1
                p2 = p1;
                d2 = d1;
                p1 = shapeVert2;
                d1 = dr;
            } else {
                p2 = shapeVert2;
                d2 = dr;
            }
            // now check final vert3
            dr = Math.sqrt(Math.pow(dx3, 2) + Math.pow(dy3, 2));
            if(dr<d2){
                // vert3 is closer than p2
                if(dr<d1){
                    // vert3 is also closer than p1
                    p2 = p1;
                    d2 = d1;
                    p1 = shapeVert3;
                    d1 = dr;
                } else {
                    p2 = shapeVert3;
                    d2 = dr;
                }
            } else {
                // vert3 is furthest away
            }
        }
        //Vector2 m = new Vector2((proxObjects.get(n).getVertices().get(p2).y - proxObjects.get(n).getVertices().get(p1).y),(proxObjects.get(n).getVertices().get(p2).x - proxObjects.get(n).getVertices().get(p1).x));
        double grad = (proxObjects.get(n).getVertices().get(p2).y - proxObjects.get(n).getVertices().get(p1).y)/(proxObjects.get(n).getVertices().get(p2).x - proxObjects.get(n).getVertices().get(p1).x);
        if(Double.isInfinite(grad)){
            grad = 1000;
        }
        Vector2 B = new Vector2((proxObjects.get(n).getVertices().get(p1).x+proxObjects.get(n).getVertices().get(p2).x)/2,(proxObjects.get(n).getVertices().get(p1).y+proxObjects.get(n).getVertices().get(p2).y)/2);
        Vector2 C = new Vector2(playerProx.getCentre().x,playerProx.getCentre().y);
        double invG = -1/grad;
        double c = B.y - invG*B.x;
        Vector2 A = new Vector2(C.x,((float) (invG*C.x+c)));
        double phi1 = Math.tan((B.x-A.x)/(B.y-A.y));
        double phi2 = Math.tan((C.x-A.x)/(C.y-A.y));
        double phi3 = 90 - phi1;
        double phi4 = 90 - phi2;
        double theta0 = 180 - phi1 - phi2;
        double thetaReb = 180 - theta0 - phi3;  // rebound angle of collision


        double b = 1;
        double a = -grad;
        float nx = (float) ( a /(Math.sqrt(Math.pow(a,2)+Math.pow(b,2))));
        float ny = (float) ( b /(Math.sqrt(Math.pow(a,2)+Math.pow(b,2))));
        Vector2 normal = new Vector2(nx,ny);
        Vector2 vIn = new Vector2(proxVx,proxVy);
        Vector2 vOut = new Vector2();
        vOut.x = -(2*(normal.x*vIn.x + normal.y*vIn.y)*normal.x - vIn.x);
        vOut.y = -(2*(normal.x*vIn.x + normal.y*vIn.y)*normal.y - vIn.y);

        proxVx = vOut.x;
        proxVy = vOut.y;
        colTime = 0;

    }

    public void updateStations(float dt){
        //System.out.println("dt is "+dt);
        for(int i=0;i<stations.length;i++){
            if(stations[i]!=null) {
                double theta = stations[i].theta + 0.5;
                if (theta > 360) {
                    theta -= 360;
                }
                stations[i].theta = theta;
                stations[i].dt += dt;
                float tempX = (float)  (stations[i].posX - stations[i].getWidth()/2 + wells[i].getWidth()*Math.cos(stations[i].dt*stationOmega) / 2);
                float tempY = (float) (stations[i].posY - stations[i].getWidth()/2 + wells[i].getWidth()*Math.sin(stations[i].dt*stationOmega) / 2);
                stations[i].setX(tempX);
                stations[i].setY(tempY);
            }
        }
    }

    public boolean outsideSystem(){
        boolean outside = false;

        double r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - playersY, 2));

        if(r>maxOrbRad){
            //System.out.println("r is "+r);
            outside = true;
        }

        return outside;
    }

    public void checkPlayerGauge(){
    }

    public void setPlayerAngle(){
        double playerAngle = 0;
        if(playerVx>0){
            if(playerVy>0){
                // in bottom right
                playerAngle = Math.abs(Math.atan(playerVy/playerVx))*180/Math.PI;
            } else if(playerVy<0){
                // in bottom left
                playerAngle = 270 + Math.abs(Math.atan(playerVx/playerVy))*180/Math.PI;
            } else {
                //System.out.println("PlayerVy zero");
            }
        } else if(playerVx<0){
            if(playerVy>0){
                playerAngle = 90 + Math.abs(Math.atan(playerVx/playerVy))*180/Math.PI;
            } else if(playerVy<0){
                playerAngle = 180 + Math.atan(playerVy/playerVx)*180/Math.PI;
            } else {
                //System.out.println("PlayerVy zero");
            }
        } else {
            //System.out.println("PlayerVx zero");
        }
        player.setRotation((float) playerAngle);
        //playerShipShown.setRotation((float) playerAngle);
        //playerLevelsInterm.setRotation((float) playerAngle);
        angle2 = (float) playerAngle;
        float cameraAngle = angle2 - angle1;
        if(playerCamOffY==0 && playerCamOffX==0) {
            wStage.wStageCam.rotate((float) (cameraAngle), 0, 0, 1);
            wStage.wStageCam2.rotate((float) (cameraAngle), 0, 0, 1);
        }
        angle1 = angle2;
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

    public shipManager getShipManager(){ return shipManager; }

    public void setBurnMultiplier(int InT){
        burnMultiplier = InT;
    }

    public float getPlanSpace(){ return planSpace; }

    public systemScreenActors getSystemActors(){ return systemActors; }

}
