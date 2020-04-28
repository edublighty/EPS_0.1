package com.mygdx.game.Screens.systemScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
import com.mygdx.game.Screens.systemScreen.Sprites.systemScreenShipGroup;
import com.mygdx.game.Screens.systemScreen.Sprites.traceImage;
import com.mygdx.game.Screens.systemScreen.Sprites.orbLinesSprite;
import com.mygdx.game.Screens.systemScreen.Sprites.planetImage;
import com.mygdx.game.Screens.systemScreen.Sprites.playerImage;
import com.mygdx.game.Screens.systemScreen.Sprites.starImage;
import com.mygdx.game.Screens.systemScreen.Stage.buttonOverlay;
import com.mygdx.game.Screens.systemScreen.Stage.detailedShipOverlay;
import com.mygdx.game.Screens.systemScreen.Stage.systemScreenActors;
import com.mygdx.game.Screens.systemScreen.Stage.systemScreenHUD;
import com.mygdx.game.Screens.systemScreen.Stage.worldStage;
import com.mygdx.game.Screens.systemScreen.Tools.Vertex;
import com.mygdx.game.Screens.systemScreen.Tools.backgroundGenerator2;
import com.mygdx.game.Screens.systemScreen.Tools.backgroundGenerator4;
import com.mygdx.game.Screens.systemScreen.Tools.backgroundGenerator5;
import com.mygdx.game.Screens.systemScreen.Tools.backgroundGenerator6;
import com.mygdx.game.Screens.systemScreen.Tools.backgroundGenerator6_Pixmap;
import com.mygdx.game.Screens.systemScreen.Tools.systemGenerator;
import com.mygdx.game.Tools.Managers.randoManager;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.b2dWorldCreator;
import com.mygdx.game.Tools.Managers.shipManager;

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
    private ShapeRenderer shapeRenderer;
    private MyGdxGame game;
    private TextureAtlas atlas;             // atlas for pointers
    private TextureAtlas Platlas;           // atlas for planet objects
    private TextureAtlas Statlas;           // atlas for star objects
    private TextureAtlas tilesAt;
    private TextureRegion rect;
    private TextureAtlas starAnim;
    private TextureAtlas thrustAt;
    private TextureAtlas healthAt;
    private TextureAtlas shieldAt;
    public Vector3 mousePos;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // sprites
    //public planetSpriteGRAVY[] planets = new planetSpriteGRAVY[9];
    public planetImage[] planets = new planetImage[9];
    public planetImage[] orbits = new planetImage
            [9];
    //public starSprite starr;
    public starImage starr;
    public orbLinesSprite[] orbline = new orbLinesSprite[9];
    private orbLinesSprite[] planetLines = new orbLinesSprite[9];
    //public sysShipSpriteGRAVY player;
    public playerImage player;
    public playerImage playerLevelsInterm;
    public systemScreenShipGroup playerShipShown;
    public shipManager shipManager;
    private traceImage dummy;

    // booleans
    private boolean rendering;
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
    private float G;
    private int gravR;
    public boolean venting;
    private float angle1;
    private float angle2;

    // system info
    private double[][] gravData;
    private double[][] planetData;
    private double[][] stellarAccMap;
    private double[] starData;
    //private int iCount;
    private int nP;
    public float solarRad;
    private float firstOrbRad;
    private float planSpace;
    private float orbRad;


    public double dR;               // radius of player from star
    public double omega;               // angular speed of orbit
    public double theta;
    private double playerAngle;
    private double devTheta;
    public double v;
    public boolean orbButClick;
    private ShapeRenderer sr;
    private double winWidth;
    private double winHeight;
    private double winWorldX;
    private double winWorldY;
    private double scrWidth;
    private double scrHeight;
    private int pathCount;
    private int pathCountLimit;//=(int) (99999*2);
    Array<Vector2> path = new Array<Vector2>();
    int wWid;
    int wHei;

    // ship-specific variables
    private float maxBurnThrust;    // maximum thrust at open throttle
    public float burnThrust;  // specific to start with as can take into account ship mass later
    private float burnPer;
    private float burnX;       // mouse click X
    private float burnY;       // mouse click Y
    private float burnR;       // distance between ship and mouse click


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
    double lastTime;
    //Time-step stuff
    private double accumulator;
    private double currentTime;
    private float step;// = 1f/60f*1000;
    private TimeStep timeStepType;
    public enum TimeStep { FIXED, FIXED_INTERPOLATION, VARIABLE }
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
    private boolean initialsing;
    private static final float maxZoom = 2;
    private float maxZoomX;
    private float maxZoomY;
    private float minZoomX;
    private float minZoomY;

    // HUD variables
    public systemScreenHUD systemHUD;
    private worldStage wStage;
    public detailedShipOverlay shipOverlay;
    public sideHUD pauseHUD;
    public systemScreenActors systemActors;
    public Vector2 deltas;
    public boolean handling;
    private InputMultiplexer multiplexer;

    // sprite variables
    private float playerVx;
    private float playerVy;
    private float playersX;
    private float playersY;
    private double dummyVx;
    private double dummyVy;
    private double dummysX;
    private double dummysY;

    public Vertex vertices[];
    private int playerHealth;
    private int playerShields;
    private int playerThrust;

    private double time1;
    private double time2;
    private boolean swtch;

    public enum Level { HIGHEST, MIDDLE, LOWEST }

    Level level;

    // Screen booleans
    public boolean paused;

    public systemScreen2(MyGdxGame game){

        backgroundGenerator6_Pixmap bg = new backgroundGenerator6_Pixmap(game);

        // update variables
        int starSystNo = 1+1;
        game.randManager.setBaseCounts(starSystNo);
        shipRadLevel = 4f;      // current level of ship to deal with rads - to be contribution from hull and shields
        shipTempLevel = 4f;     // current level of ship to deal with temp - to be contribution from hull and shields
        allStop = false;
        startingAllStop = false;
        velError = game.V_WIDTH/10000;
        rendering = false;
        swtch = false;
        time1 = System.nanoTime();
        time2 = System.nanoTime();
        playerHealth = 100;
        playerShields = 100;
        playerThrust = 0;
        venting = false;
        engBurn = false;
        starOnly = false;
        tracing = true;
        collision = false;
        useAccMap = false;
        timeStepType = TimeStep.VARIABLE;
        currentTime = TimeUtils.millis() / 1000.0;
        doPhysics = false;
        step = 1f/(60f);
        pathCountLimit=(int) (1000);
        orbitPlanet = false;
        planetDeburn = false;
        planetOrbDia = game.V_WIDTH/30;
        solarM = 100;
        planetM1 = 1;
        gravR = 3;      // power of radius in grav equations
        G = (1*game.V_WIDTH/30);
        handling = false;
        deltas = new Vector2(0,0);
        angle1=0;
        angle2=0;

        // ship-specific variables
        maxBurnThrust = 30f/50;
        burnThrust = maxBurnThrust;
        burnPer = 50f;



        //new testPlanetCreator();

        // initial player conditions
        float startVx = 0.2f;
        float startVy = 3f;

        // import of assets
        atlas = new TextureAtlas("PNGsPacked/Pointers.atlas");
        Platlas = new TextureAtlas("PNGsPacked/allPlanets.atlas");
        Statlas = new TextureAtlas("PNGsPacked/Stars.atlas");
        starAnim = new TextureAtlas("PNGsPacked/stars/starsPack.atlas");
        tilesAt = new TextureAtlas("PNGsPacked/biomespack50px.atlas");
        rect = tilesAt.findRegion("pDTTile50Orange");
        thrustAt = new TextureAtlas("systemScreen/ui/thrustControl.atlas");
        shieldAt = new TextureAtlas("systemScreen/ui/shieldBars.atlas");
        healthAt = new TextureAtlas("systemScreen/ui/healthBars.atlas");

        // set game variable
        this.game=game;

        map = new TiledMap();// mapLoader.load("map/systems/Syst6.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);
        shapeRenderer = new ShapeRenderer();

        // camera to follow touch and drag
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam) {};
        //System.out.println("viewport width "+gameport.getWorldWidth()+" and height "+gameport.getWorldHeight());

        // world variables
        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        // create game overlays
        wStage = new worldStage(game,this,game.batch,gameport.getWorldWidth(),gameport.getWorldHeight());

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
                mousePos = new Vector3(Gdx.input.getX()/MyGdxGame.PPM, Gdx.input.getY()/MyGdxGame.PPM, 0);
                float midX = windowWidth / (MyGdxGame.PPM*2*gamecam.zoom);
                float midY = windowHeight / (MyGdxGame.PPM*2*gamecam.zoom);
                float burnI = -(mousePos.x - midX);
                float burnJ = (mousePos.y - midY);
                getburnAngle(burnI,burnJ,angle1);

                allStop = false;
                engBurn = true;
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
        gravData[0][1] = game.V_WIDTH/2;
        gravData[0][2] = game.V_WIDTH/2;
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
        // create sprite in game world
        //player = new sysShipSpriteGRAVY(world, this,toteSize);


        //dummy = new traceImage(world, this,toteSize);

        int i = 0;
        int locX;// = 5750/2-size/2;
        int locY;// = (int) (5750/2 +  (Math.pow(-1,i))*((i)*375+750))-size/2;
        int size;

        int sType = 1; //(int) starData[0];
        String sObj = "star"+Integer.toString(sType);

        // set background image
        float backW = gameport.getWorldWidth()*1.5f;
        float backH = backW;
        float backX = (float) (gravData[0][1] - backW/2);
        float backY = (float) (gravData[0][2] - backH/2);
        backImage backImage = new backImage(this,backX,backY,backW,backH);
        Color color = backImage.getColor();
        System.out.println("background red is "+color.r);
        System.out.println("background alpha is "+color.a);
        //backImage.setColor(color.r,color.g,color.b,0.5f*color.a);
        backImage.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(bg.getPixmap()))));
        wStage.stage.addActor(backImage);

        // set star data
        starr = new starImage(world,this, sObj, nP, wWid, wHei,(int) gravData[0][0]);//new starSprite(world, this, sObj, nP, wWid, wHei,(int) gravData[0][0]);
        starr.setWidth(gameport.getWorldWidth()*(5f/60));
        starr.setHeight(gameport.getWorldWidth()*(5f/60));
        starr.setPosition((float) (gravData[0][1] - starr.getWidth()/2),(float) (gravData[0][2] - starr.getHeight()/2));
        System.out.println("starr X "+starr.getX()+" height "+starr.getY());
        System.out.println("starr width "+starr.getWidth()+" height "+starr.getHeight());
        wStage.stage.addActor(starr);

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
            firstOrbRad = sGen.getfirstOrbRad();
            planSpace = sGen.getPlanSpace();
            orbRad = firstOrbRad+planSpace*i;
            System.out.println("firstorbrad "+firstOrbRad);
            System.out.println("planSpace "+planSpace);
            System.out.println("orbRad "+orbRad);
            orbits[i].setWidth(orbRad*2);
            orbits[i].setHeight(orbRad*2);
            float orbThickness = orbits[i].getWidth()*(2/500);
            orbits[i].setPosition(starr.getX()+starr.getWidth()/2-orbits[i].getWidth()/2,starr.getY()+starr.getHeight()/2-orbits[i].getHeight()/2);
            planets[i] = new planetImage(world,this,"planetShine",planetData,i,wWid,wHei);//new planetSpriteGRAVY(world,this,stelObj,planetData,i,wWid,wHei);
            planets[i].setWidth(starr.getWidth()/10);
            planets[i].setHeight(starr.getWidth()/10);
            gravData[i+1][0] = 0.01;    // completely unnecessary
            gravData[i+1][1] = planetData[2][i];
            gravData[i+1][2] = planetData[3][i];
            planets[i].setPosition((float) (planetData[2][i]-planets[i].getWidth()/2 - orbThickness/2), (float) (planetData[3][i]-planets[i].getHeight()/2 - orbThickness/2));
            wStage.stage.addActor(planets[i]);

            orbline[i] = new orbLinesSprite(world,this,Math.round(planetData[8][i])/MyGdxGame.PPM,toteSize,toteSize);
            planetLines[i] = new orbLinesSprite(world,this,planetOrbDia,(float) planetData[2][i],(float) planetData[3][i]);

            radSqr = 2;     // define whether intensity is proportional to inverse or inverse square of distance from star
            tempSqr = 2;    // define whether intensity is proportional to inverse or inverse square of distance from star

            shipRadDef = (float) (shipRadLevel/(Math.pow(planSpace*4,radSqr)));           // Radiation Deflection constant - defined by score of 4 at radius 1500
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

        float startX = starr.getX() + starr.getWidth()*5f;
        float startY = starr.getY() + starr.getHeight()*2f;
        System.out.println("startX "+startX+" startY "+startY);
        player = new playerImage(game, world,this,toteSize,"Level1SHIP",startX,startY);
        //wStage.stage.addActor(player);
        playerLevelsInterm = new playerImage(game, world,this,toteSize,"Level2SHIP",startX,startY);
        //wStage.stage.addActor(playerLevelsInterm);

        // set up ship graphic
        float shipWidth = starr.getWidth();
        playerShipShown = new systemScreenShipGroup(game,world,this,game.batch,shipWidth);
        wStage.stage.addActor(playerShipShown);
        systemActors = new systemScreenActors(game,this,game.batch,gameport.getWorldWidth()*100, gameport.getWorldHeight()*100);
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

        // initially set other two layers transparent
        Color tempColor = playerLevelsInterm.getColor();
        playerLevelsInterm.setColor(tempColor.r,tempColor.g,tempColor.b,0);
        tempColor = playerShipShown.getColor();
        playerShipShown.setColor(tempColor.r,tempColor.g,tempColor.b,0);
        level = Level.HIGHEST;

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

    public void getTrace(float dt) {

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth*gamecam.zoom;
        scrHeight = (int) gamecam.viewportHeight*gamecam.zoom;
        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen
        collision = false;

        boolean buffer2 = true;

        // initialise trace dummy speed and posish
        //dummy.b2body.setTransform(player.b2body.getPosition().x,player.b2body.getPosition().y,0);
        dummysX = player.b2body.getPosition().x;
        dummysY = player.b2body.getPosition().y;
        dummyVx = playerVx;
        dummyVy = playerVy;
        //System.out.println("dt "+dt+" posX "+dummysX+" posY "+dummysY);
        //dummy.b2body.setLinearVelocity(player.b2body.getLinearVelocity());

        double sX;
        double sY;
        float Vx = player.b2body.getLinearVelocity().x;
        float Vy = player.b2body.getLinearVelocity().y;
        double VxNew;
        double VyNew;
        path.clear();
        pathCount = 0;

            boolean tempBool = true;

            while(buffer2){//pathCount<pathCountLimit){
                // cycle through points predicting player movement
                double r;
                double Ax = 0;
                double Ay = 0;
                double pX;// = 0;
                double pY;// = 0;

                //dummysX = dummy.b2body.getPosition().x;
                //dummysY = dummy.b2body.getPosition().y;
                sX = dummysX;
                sY = dummysY;
                if(orbitPlanet){
                    r = Math.sqrt(Math.pow(gravData[planetNum][1] / MyGdxGame.PPM - dummysX, 2) + Math.pow(gravData[planetNum][2] / MyGdxGame.PPM - dummysY, 2));
                    Ax = (float) (G * planetM1*-(dummysX - gravData[planetNum][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                    Ay = (float) (G * planetM1*-(dummysY - gravData[planetNum][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                } else {
                    if (!starOnly) {
                        for (int i = 0; i < nP; i++) {
                            r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - sY, 2));
                            Ax = Ax + (G * planetM1 * -(sX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            Ay = Ay + (G * planetM1 * -(sY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            //System.out.println("tracing i "+i+" Ax "+Ax+" Ay "+Ay);
                        /*if (!collision) {
                            collision = inCircle(gravData[i + 1][1] / MyGdxGame.PPM, gravData[i + 1][2] / MyGdxGame.PPM, gravData[i + 1][0] / MyGdxGame.PPM, sX, sY);
                        }*/
                        }
                    }
                    r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                    Ax = Ax + G * solarM * -(sX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR);
                    Ay = Ay + G * solarM * -(sY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR);
                    //System.out.println("tracing star "+" Ax "+Ax+" Ay "+Ay);
                }
                path.add(new Vector2((float) sX, (float) sY));
                /*if(!collision) {
                    collision = inCircle(gravData[0][1] / MyGdxGame.PPM, gravData[0][2] / MyGdxGame.PPM, gravData[0][0] / MyGdxGame.PPM, sX, sY);
                }*/
                dummyVx = (dummyVx + Ax * dt);                    // new velocity at end of timestep
                pX = (dummyVx) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                dummyVy = (dummyVy + Ay * dt);                    // new velocity at end of timestep
                pY = (dummyVy) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                dummysX = dummysX + pX;
                dummysY = dummysY + pY;
                //dummy.b2body.setTransform((float)dummysX,(float)dummysY,0);
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

    public void getburnAngle(double burnI, double burnJ, double omegaD){
        // method calculating burn angle taking into account camera rotation

        double alpha = omega - 90;
        burnR = (float) (Math.sqrt(Math.pow(burnI, 2) + Math.pow(burnJ, 2)));
        double theta0 = Math.atan(burnJ/burnI);
        double theta0D = theta0*180/Math.PI;
        //System.out.println("should be less than 90 "+theta0D);
        if(burnI>0){
            if(burnJ>0){
                // top right screen quadrant is -90 at the y axis and -0 at x axis
                // mod(theta)
                theta0D = Math.abs(theta0D);
            } else {
                // bottom right screen quadrant is 0 at x axis and 90 at y axis
                // 360 - mod(theta)
                theta0D = 360 - Math.abs(theta0D);
            }
        } else {
            if(burnJ>0){
                // top left screen quadrant is 0 at x axis and 90 and y axis
                // 180 - theta
                theta0D = 180 - theta0D;
            } else {
                // bottom left screen quadrant is -90 at the y axis and -0 at x axis
                // mod(theta)+180
                theta0D = 180 + Math.abs(theta0D);
            }
        }
        // got angle of screen click relative to camera coordinate axes
        // now add camera rotation angle
        double phiD = theta0D + omegaD;
        if(phiD>360){
            phiD = phiD - 360;
        }
        // now get angle for velocity triangle off world axes
        double alphaD;
        if(phiD>=0 && phiD<90){
            // still top right
            alphaD = phiD;
            alpha = alphaD*Math.PI/180;
            burnX = (float) (burnR*Math.cos(alpha));
            burnY = (float) (burnR*Math.sin(alpha));
        } else if(phiD>=90 && phiD<180){
            // top left
            alphaD = 180 - phiD;
            alpha = alphaD*Math.PI/180;
            burnX = - (float) (burnR*Math.cos(alpha));
            burnY = (float) (burnR*Math.sin(alpha));
        } else if(phiD>=180 && phiD<270){
            // bottom left
            alphaD = phiD - 180;
            alpha = alphaD*Math.PI/180;
            burnX = - (float) (burnR*Math.cos(alpha));
            burnY = - (float) (burnR*Math.sin(alpha));
        } else {
            // bottom right
            alphaD = 360 - phiD;
            alpha = alphaD*Math.PI/180;
            burnX = (float) (burnR*Math.cos(alpha));
            burnY = - (float) (burnR*Math.sin(alpha));
        }

        // burnI is distance from ship to mouse click in x-direction relative to the rotating ship plane
        // burnJ is distance from ship to mouse click in y-direction etc
        // if burnI is positive, then are decelerating in the ships direction
        // if burnJ is positive, then the vector is the normal to above
        // both of these have a world x-y impact

        burnR = (float) Math.sqrt(Math.pow(burnI,2)+Math.pow(burnJ,2));
        double ratioI = burnI/burnR;
        double ratioJ = burnJ/burnR;

        double burnIx = playerVx;
        double burnIy = playerVy;

        double burnJx = -burnIx;
        double burnJy = burnIy;

        System.out.println("burnIx "+burnIx+" burnIy "+burnIy);
        System.out.println("burnJx "+burnJx+" burnJy "+burnJy);

        double hypotI = Math.sqrt((Math.pow(burnIx,2))+(Math.pow(burnIy,2)));
        float xRatioI = (float) (burnIx/hypotI);
        float yRatioI = (float) (burnIy/hypotI);

        System.out.println("hypotI "+hypotI+" xRatioI "+xRatioI+" yRatioI "+yRatioI);

        double hypotJ = Math.sqrt(Math.pow(burnJx,2)+Math.pow(burnJy,2));
        float xRatioJ = (float) (burnIx/hypotI);
        float yRatioJ = (float) (burnIy/hypotI);

        System.out.println("hypotJ "+hypotJ+" xRatioJ "+xRatioJ+" yratioJ "+yRatioJ);

        float AxI = (float) ((playerThrust * maxBurnThrust * xRatioI * ratioI) / 100);
        float AyI = (float) ((playerThrust * maxBurnThrust * yRatioI * ratioI) / 100);

        System.out.println("AxI "+AxI+" AyI "+AyI);

        float AxJ = (float) ((playerThrust * maxBurnThrust * xRatioJ * ratioJ) / 100);
        float AyJ = (float) ((playerThrust * maxBurnThrust * yRatioJ * ratioJ) / 100);

        System.out.println("AxJ "+AxJ+" AyJ "+AyJ);

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

    public void orbitStar(){
        Vector2 playerPos = getPlayerPosition();

        Vector2 newVelocity = calcEscapeVel(playerPos.x - gravData[0][1],playerPos.y - gravData[0][2],getPlayerPositionR(0),true);

        playerVx = newVelocity.x;
        playerVy = newVelocity.y;
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
        double V = Math.sqrt(G*stellarMass/r);
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

        game.batch.setProjectionMatrix(wStage.traceStage.getCamera().combined);
        wStage.traceStage.draw();

        game.batch.setProjectionMatrix(wStage.stage.getCamera().combined);
        wStage.stage.draw();
        wStage.stage.act();

        game.batch.setProjectionMatrix(systemActors.stage.getCamera().combined);
        systemActors.stage.draw();
        systemActors.stage.act();

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

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth;
        scrHeight = (int) gamecam.viewportHeight;

        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen

        wStage.stage.getViewport().update(width,height,true);
        wStage.traceStage.getViewport().update(width,height,true);
        gameport.update(width,height);
        gamecam.update();
        // update HUDs here
        //systemHUD.viewport.update(width,height);

        //galHUD.viewport.update(width,height);

        if (!swtch) {
            rendering = true;
        }
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
            if(true){//gamecam.zoom<maxZoom) {
                gamecam.zoom*=1.02f;
                wStage.wStageCam.zoom*=1.02f;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            if(true){//gamecam.viewportWidth>minZoomX && gamecam.viewportHeight>minZoomY) {
                gamecam.zoom *= 0.98f;
                wStage.wStageCam.zoom *= 0.98f;
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

    public Vector2 getPlayerSpeed(){

        Vector2 playerSpeed = player.b2body.getLinearVelocity();

        return playerSpeed;
    }

    public double getPlayerSpeedR(){
        Vector2 playerSpeed = getPlayerSpeed();
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
                            if (!starOnly) {
                                for (i = 0; i < nP; i++) {
                                    r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - playersY, 2));
                                    //System.out.println("r is " + r);
                                    if (r < planetOrbDia) {
                                        // within orbit of planet
                                        orbitPlanet = true;
                                        planetNum = i + 1;
                                        planetDeburn = true;
                                    }
                                    Ax = Ax + (float) (G * planetM1 * -(playersX - gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                                    Ay = Ay + (float) (G * planetM1 * -(playersY - gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                                }
                            }
                            r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - playersY, 2));
                            Ax = Ax + (float) (G * solarM * -(playersX - gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, gravR));
                            Ay = Ay + (float) (G * solarM * -(playersY - gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, gravR));
                        }
                        if (engBurn) {
                            // apply acceleration due to engine burn here
                            System.out.println("burn " + playerThrust);
                            Ax = Ax + engBurnX;//((playerThrust * maxBurnThrust * burnX / burnR) / 100);
                            Ay = Ay + engBurnY;//((playerThrust * maxBurnThrust * burnY / burnR) / 100);
                        }
                        if(allStop){
                            System.out.println("Stopping");
                            Vector2 A = allStop();
                            Ax = A.x;
                            Ay = A.y;
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
                playerShipShown.setPosition(player.b2body.getPosition().x - playerShipShown.getWidth() / 2, player.b2body.getPosition().y - playerShipShown.getHeight() / 2);
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
                    playerShipShown.setColor(tempColor.r, tempColor.g, tempColor.b, 1);
                }
                checkVenting(dt);
            }


            if (statusCheck) {
                double r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - playersX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - playersY, 2));

                double powRad = (starRad / Math.pow(r,radSqr) - shipRadDef) * radMultiplier * dt;

                double powHeat = (starTemp / Math.pow(r,tempSqr) - shipTempDef) * tempMultiplier * dt;

                double newTemp = shipManager.getShipTemp();
                double newRad = shipManager.getShipRads();

                if (powHeat > 0) {
                    // heating up
                    newTemp += powHeat;
                } else {
                    // can cool down
                    powHeat = (0 - shipTempDef) * tempMultiplier * dt;
                    newTemp += powHeat;
                    if (newTemp < 20) {
                        newTemp = 20;
                    }
                }

                if(powRad<0){
                    powRad=0;
                }
                newRad += powRad;

                shipManager.updateShipTemp(newTemp,dt);
                shipManager.updateShipRad(newRad);

            }

            doPhysics = false;

            // Get camera to follow sprite x and y movements
            wStage.wStageCam.position.x = player.b2body.getPosition().x;//playerShipShown.getX()+playerShipShown.getWidth()/2;
            wStage.wStageCam.position.y = player.b2body.getPosition().y;//playerShipShown.getY()+playerShipShown.getHeight()/2;
            wStage.wStageCam.update();

            // update the star graphic
            starr.update(physTime);
            playerShipShown.update(physTime);

            time1 = System.nanoTime();
            checkPlayerGauge();

            // update camera and render
            gamecam.update();
            renderer.setView(gamecam);
        }
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
        playerShipShown.setRotation((float) playerAngle);
        playerLevelsInterm.setRotation((float) playerAngle);
        angle2 = (float) playerAngle;
        float cameraAngle = angle2 - angle1;
        wStage.wStageCam.rotate((float) (cameraAngle),0,0,1);
        angle1 = angle2;
        //System.out.println("player angle "+angle1);
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

    /*public boolean touchDragged2(){//, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        System.out.println("touch dragged "+x+" "+y);
        *//*if(x<1&&y<1){
            // point click - no drag
            posDX = Gdx.input.getX();                           // destination on screen X
            posDY = Gdx.input.getY();                           // destination on screen Y
            //System.out.println("DX "+posDX+" and DY "+posDY);
            setPointers();

        }*//*

        deltas = new Vector2(x,y);

        return true;
    }*/

}
