package com.mygdx.game.Screens.systemScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.Sprites.orbLinesSprite;
import com.mygdx.game.Sprites.planetSprite;
import com.mygdx.game.Screens.systemScreen.Sprites.planetSpriteGRAVY;
import com.mygdx.game.Screens.systemScreen.Sprites.starSpriteGRAVY;
import com.mygdx.game.Screens.systemScreen.Sprites.sysShipSpriteGRAVY;
import com.mygdx.game.HUDs.Hud;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.b2dWorldCreator;
import com.mygdx.game.Screens.systemScreen.Stage.buttonOverlay;
import com.mygdx.game.Screens.systemScreen.Tools.systemGenerator;

/**
 * Created by GCUser on 10/05/2018.f
 */

public class systemPlayGRAVY implements Screen {

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
    public Hud hud;
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
    public sysShipSpriteGRAVY player;
    public planetSprite plnt;
    public static boolean burnFlag;
    public static boolean planetFlag;
    private boolean moveToPoint;
    private double[][] gravData;
    private double[][] planetData;
    private double[][] stellarAccMap;
    private double[] starData;
    private double[][] velComps;
    private int iCount;
    private int nP;
    public planetSpriteGRAVY[] planets = new planetSpriteGRAVY[9];
    public starSpriteGRAVY starr;
    public orbLinesSprite[] orbline = new orbLinesSprite[9];
    public boolean notStarted;
    public float solarRad;
    public boolean autoOrbit;
    private boolean tracing;
    public int orbDir;
    private boolean starOnly;
    private boolean useAccMap;
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
    Array<Vector2> path = new Array<Vector2>();
    int wWid;
    int wHei;
    // last second
    double lastTime;
    //Time-step stuff
    private double accumulator;
    private double currentTime;
    private float step = 1f/60f;
    TimeStep timeStepType;
    public enum TimeStep { FIXED, FIXED_INTERPOLATION, VARIABLE }

    public systemPlayGRAVY(MyGdxGame game){

        // Time-step variables
        accumulator = 0.0;
        currentTime = TimeUtils.millis() / 1000.0;
        lastTime = currentTime;
        timeStepType = TimeStep.VARIABLE;
        TimeStep timeStep;
        tracing = true;
        Gdx.graphics.setWindowedMode(480,480);  // changes size of the overall window. these coordinates are used to draw lines
        shapeRenderer = new ShapeRenderer();
        moveToPoint = false;
        starOnly = true;
        useAccMap = true;
        systemGenerator sGen = new systemGenerator(game,null);
        sGen.planetGen();
        solarRad = sGen.toteSize;
        planetData = sGen.getPlanetData();
        stellarAccMap = new double[200][2];
        sGen.starGen();
        starData = sGen.getStarData();
        nP = planetData[0].length;
        gravData = new double[nP+1][4];
        gravData[0][0] = 10;
        gravData[0][1] = solarRad;
        gravData[0][2] = solarRad;

        velComps = new double[4][15];
        iCount=0;

        int x = 640/2;
        int y = 480/2;

        points = 50;

        atlas = new TextureAtlas("PNGsPacked/Pointers.atlas");
        Platlas = new TextureAtlas("PNGsPacked/allPlanets.atlas");
        Statlas = new TextureAtlas("PNGsPacked/Stars.atlas");

        this.game=game;
        burnFlag = false;
        planetFlag = false;

        // camera to follow sprite
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH/MyGdxGame.PPM,MyGdxGame.V_HEIGHT/MyGdxGame.PPM,gamecam) {             /*ScreenViewport allows player to see more with bigger screen*/        };

        // create game overlay
        hud = new Hud(game.batch);
        //buttonoverlay = new buttonOverlay(game.batch,this);

        // load map
        mapLoader = new TmxMapLoader();
        map = new TiledMap();// mapLoader.load("map/systems/Syst6.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);

        // initialise gamecam centrally
        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        wWid = (int) gameport.getWorldWidth();
        wHei = (int) gameport.getWorldHeight();
        System.out.println("width "+wWid+" height "+wHei);
        //buttonoverlay.stage.getCamera().position.y = 100;

        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        new b2dWorldCreator(world,map,this,planetData,null,0,0,0,0);

        float toteSize = (750*(nP+1)+500)/2;
        // create sprite in game world
        //player = new sysShipSpriteGRAVY(world, this,toteSize);

        int i = 0;
        int locX;// = 5750/2-size/2;
        int locY;// = (int) (5750/2 +  (Math.pow(-1,i))*((i)*375+750))-size/2;
        int size;


        while(i<nP) {
            System.out.println("Creating planet "+i);
            String stelObj = "terrs";
            int pType = (int) planetData[0][i];
            size = (int) planetData[1][i];
            System.out.println("Size "+size);/*
            locX = 5750/2-size/2;
            locY = (int) (5750/2 +  (Math.pow(-1,i))*((i)*375+750))-size/2;*/
            System.out.println("this is pType "+pType);
            switch (pType) {
                default:
                    System.out.println("PTYPE IS SOMETHING ELSE");
                    break;
                case 0:
                    // terrestrial
                    System.out.println("Terrestrial");
                    stelObj = "terr"+Integer.toString(1);
                    break;
                case 1:
                    // gaseous terrestrial
                    System.out.println("Gaseous Terrestrial");
                    stelObj = "terGas"+Integer.toString(1);
                    break;
                case 2:
                    // gas giant
                    System.out.println("Gas Giant");
                    stelObj = "gasGi"+Integer.toString(1);
                    break;
            }
            System.out.println("Switched and orbrad "+Math.round(planetData[8][i])+" and toteSize "+toteSize);
            //planets[i] = new planetSpriteGRAVY(world,this,stelObj,planetData,i,wWid,wHei);
            gravData[i+1][0] = 0.01;
            gravData[i+1][1] = planetData[2][i];
            gravData[i+1][2] = planetData[3][i];
            //orbline[i] = new orbLinesSprite(world,this,(int) Math.round(planetData[8][i]),toteSize);
            i++;
        }

        for(i=0;i<gravData.length;i++){
            System.out.println("gravity "+gravData[i][0]+" X "+gravData[i][1]+" Y "+gravData[i][2]);
        }

        int j;



        int sType = 1; //(int) starData[0];
        String sObj = "star"+Integer.toString(sType);
        System.out.print("star no. "+sObj);

        // set star data
        //starr = new starSpriteGRAVY(world, this, sObj, nP, wWid, wHei);

        //planets[0].setPosition(200,200);
        //planets[1].setPosition(200,100);

        // set contact listeners for objects
        world.setContactListener(new WorldContactListener());

        this.notStarted = true;
        autoOrbit = false;

        if(autoOrbit){
            System.out.println("initialise auto orbit vars");
            System.out.println("player pos x "+player.b2body.getPosition().x+" and y "+player.b2body.getPosition().y+" and toteSize "+toteSize+" and solarRad "+solarRad);
            dR = Math.sqrt(Math.pow(toteSize/MyGdxGame.PPM-player.b2body.getPosition().x,2)+Math.pow(toteSize/MyGdxGame.PPM-player.b2body.getPosition().y,2));
            omega = 2*Math.PI/(15);         // deg/s
            theta = 0;
            v = omega*dR;
            //omega = omega*180/Math.PI;
        }
        player.b2body.setLinearVelocity(new Vector2(1,-1));
        genAccMap();
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


    // Create sprites for later drawing
    public void genStellars(){
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

    @Override
    public void show () {
        System.out.println("play show");

        System.out.println("play show - created camera");
    }

    @Override
    public void render (float delta) {
        //System.out.println("delta "+delta);

        /*double newTime = TimeUtils.millis() / 1000.0;
        double frameTime = Math.min(newTime - currentTime, 0.25);
        float deltaTime = (float)frameTime;

        currentTime = newTime;

        while (accumulator >= step) {
            //world.step(step, velIterations, posIterations);
            accumulator -= step;
        }*/

        update(delta);

        //System.out.println("play render");
        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        int i = 0;

        game.batch.begin();
        player.draw(game.batch);
        while(i<nP){
            planets[i].draw(game.batch);
            i++;
        }
        starr.draw(game.batch);
        game.batch.end();

        //System.out.println("size of path "+path.size);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(i=0;i<99998;i++) {
            //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.line(path.get(i),path.get(i+1));
            //shapeRenderer.end();
        }
        shapeRenderer.end();


        /*// draw camera for sprite
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();*/

        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();

        game.batch.setProjectionMatrix(buttonoverlay.stage.getCamera().combined);
        buttonoverlay.stage.draw();
    }

    @Override
    public void resize (int width, int height) {
        System.out.println("play resize "+width+" x "+height);
        gameport.update(width,width);
        gamecam.update();
        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        scrWidth = (int) gamecam.viewportWidth;
        scrHeight = (int) gamecam.viewportHeight;
        System.out.println("scrW "+scrWidth+" scrH "+scrHeight);
        System.out.println("winW "+winWidth+" winH "+winHeight);

        winWorldX = (winWidth / scrWidth);        // ratio of window to screen
        winWorldY = (winHeight / scrHeight);      // ratio of window to screen

        System.out.println("scrW "+scrWidth+" scrH "+scrHeight+" ratioX "+winWorldX+" and ratio Y "+winWorldY);

        System.out.println("size of points "+points);
        int i;
        for(i=0;i<points;i++){
            path.add(new Vector2((float) ((scrWidth/2+5*i)*winWorldX),(float) ((scrHeight/2+5*i*i)*winWorldY)));
        }

        System.out.println("size of path 1 "+path.size+" first point X"+path.get(0).x+" n Y "+path.get(0).y);

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
        //System.out.println("systemPlay dispose");
        map.dispose();
        //System.out.println("map disposed");
        renderer.dispose();
        //System.out.println("renderer disposed");
        world.dispose();
        //System.out.println("world disposed");
        b2dr.dispose();
        //System.out.println("b2dr disposed");
        hud.dispose();
        //System.out.println("hud disposed");
    }

    public void handleInput(float dt){
        // If mouse pressed on world, move map left

        if(Gdx.input.isTouched()) {
            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            //System.out.println("X "+mousePos.x+" Y "+mousePos.y+" Z "+mousePos.z);
            //gamecam.unproject(mousePos);
            System.out.println(" and in stereo! X "+mousePos.x+" Y "+mousePos.y+" Z "+mousePos.z);
            if(mousePos.y>400){
                // Using button - don't update position
                System.out.println("PRESSING BUTTON");
            } else {
                // Not clicking button - move away my friend!
                System.out.println("moving position");
                if (moveToPoint) {
                    gamecam.unproject(mousePos); // mousePos is now in world coordinates
                    moveFlag = true;
                    posDX = Gdx.input.getX();                           // destination on screen X
                    posDY = Gdx.input.getY();                           // destination on screen Y
                    //System.out.println("dX "+posDX+" dY "+posDY);
                    posCX = player.b2body.getWorldCenter().x;           // current location of sprite on map x
                    posCY = player.b2body.getWorldCenter().y;           // current location of sprute on map y
                    //System.out.println("cX "+posCX+" cY "+posCY);
                    //thetaS = Math.atan(Math.abs(posDY-posCY)/Math.abs(posDX-posCX));

                    centerPosition = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
                    posDY = Gdx.graphics.getHeight() - posDY; //Inverse the Y
                    mouseLoc = new Vector2(posDX, posDY);
                    Vector2 direction = mouseLoc.sub(centerPosition);   // get direction as vector
                    //float mouseAngle  = direction.angle();              // not needed as use vector directly - perhaps for rotation
                    //setRotation(mouseAngle);
                    player.b2body.applyLinearImpulse(new Vector2(direction), player.b2body.getWorldCenter(), true);
                } else {
                    //mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    gamecam.unproject(mousePos); // mousePos is now in world coordinates
                    double X = player.b2body.getPosition().x - mousePos.x;
                    double Y = player.b2body.getPosition().y - mousePos.y;
                    double r = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
                    //System.out.println("X " + player.b2body.getPosition().x + " mouseX " + Gdx.input.getX());
                    float vX = (float) (0.3 * X / r);
                    float vY = (float) (0.3 * Y / r);
                    Vector2 vector = new Vector2(vX + player.b2body.getLinearVelocity().x, vY + player.b2body.getLinearVelocity().y);
                    player.b2body.setLinearVelocity(vector);
                }
            }
        }

        if (moveFlag){
            posCX = player.b2body.getWorldCenter().x;           // current location of sprite on map x
            posCY = player.b2body.getWorldCenter().y;           // current location of sprute on map y
            //centerPosition = new Vector2((float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2);
            mouseLoc = new Vector2(posDX, posDY);
            deltaZ = Math.sqrt(Math.pow((posCX-mousePos.x),2)+Math.pow((posCY-mousePos.y),2));
            //System.out.println(" current X and Y: "+centerPosition.x+" x "+centerPosition.y+"      and destination x and y: "+mouseLoc.x+" x "+mouseLoc.y);
            //System.out.println(" other current x and y: "+mousePos.x+" x "+mousePos.y+"     aaaaand x and y: "+posCX+" x "+posCY);
            //System.out.println("delta "+deltaZ);
            if(deltaZ<10){
                moveFlag = false;
                player.b2body.setLinearVelocity(0f,0f);
            }

        }

    }

    public void update(float dt){
        handleInput(dt);

        double frameTime = 0;

        // Step the world according to the selected method
        if(timeStepType == TimeStep.VARIABLE) {
            double newTime = TimeUtils.millis() / 1000.0;
            frameTime = newTime - currentTime;
            currentTime = newTime;
            world.step((float) frameTime, 6, 2);
        }
        else if(timeStepType == TimeStep.FIXED){
            double newTime = TimeUtils.millis() / 1000.0;
            frameTime = Math.min(newTime - currentTime, 0.25);

            currentTime = newTime;
            accumulator += frameTime;

            while (accumulator >= step) {
                world.step(step, 6, 2);
                accumulator -= step;

            }
        }

        //world.step(tStep,6,2);  //timeStep 60 per second

        int i;
        int G=15;
        int m=500;
/*

        if(iCount<13){
            if(iCount<3){
                System.out.println("vX "+player.b2body.getLinearVelocity().x+" vY "+player.b2body.getLinearVelocity().y);
            }
            velComps[0][iCount]=player.b2body.getPosition().x;
            velComps[1][iCount]=player.b2body.getPosition().y;
            iCount++;
        } else {
            // time to output data
            int j;

            System.out.println("START OUTPUT");
            for(j=0;j<13;j++){
                System.out.println("posX "+velComps[0][j]+" posY "+velComps[1][j]+" predX "+velComps[2][j]+" predY "+velComps[3][j]);
            }
            System.out.println("END OUTPUT");


            final String FNAME = "velComps.txt";
            ArrayList<String> list_copy = new ArrayList<>();

            list_copy.add ("Line 1");
            list_copy.add ("Line 2");

            String line1 = "";

            try ( BufferedWriter bw =
                          new BufferedWriter(new FileWriter(FNAME)) )
            {
                for (j=0;j<13;j++) {
                    for (i = 0; i < 4; i++) {
                        line1 = "";
                        line1 = line1 + " " + String.valueOf(velComps[i][j]);
                        //line1 = line1 + " " + String.valueOf(perlinNoise[i][j]);

                        bw.write(line1 + "\n");
                        //bw.write("STOP NOW");
//                        bw.write("\r\n");
//                        line1 = "";
                        //System.out.println("Printing "+i+" + "+j);
                    }
                    bw.write("\r\n");
                }


                bw.close ();

            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
*/

        if(tracing) {
            getTrace(G,step);
            //tracing = false;
        }


        if(burnFlag){
            // applying burning to player and hud
        }

        if(planetFlag) {
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
        }



        if(autoOrbit){
            //System.out.println("Naughty auto oribt 1");
            // increment position in orbit - initially around star
            //System.out.println("dr "+dR);
            //System.out.println("x "+(float) (solarRad+dR*Math.sin(theta))+" and y "+(float) (solarRad+dR*Math.cos(theta)));
            Vector2 vec2 =  new Vector2((float) (/*Math.pow(-1,orbDir)**/(orbDir*v*Math.cos(theta))),(float) (/*Math.pow(-1,orbDir)**/-orbDir*v*Math.sin(theta)));
            player.b2body.setLinearVelocity(vec2);
            //System.out.println("THeta is "+theta*180/Math.PI);
            if(theta<2*Math.PI){
                theta=theta+2*Math.PI;
            }
            theta = (theta + orbDir*omega*dt);
            //player.setPosition((float) (solarRad+dR*Math.sin(theta)),(float) (solarRad+dR*Math.cos(theta)));
            //player.setCenter((float) (solarRad+dR*Math.sin(theta)/MyGdxGame.PPM),(float) (solarRad+dR*Math.cos(theta))/MyGdxGame.PPM);//setBounds((float) (solarRad+dR*Math.sin(theta)),(float) (solarRad+dR*Math.cos(theta)),128/MyGdxGame.PPM,128/MyGdxGame.PPM);
        } else {
            // do the solar system thing
            if(autoOrbit) {
                //System.out.println("Naughty auto oribt 2");
                for (i = 0; i < nP; i++) {
                    double r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                    gravData[i + 1][3] = G * 1000 * gravData[i + 1][0] * m / Math.pow(r, 2);
                }
            } else {
                if (useAccMap) {
                    System.out.println("X "+player.b2body.getPosition().x+" Y "+player.b2body.getPosition().y);
                    Vector2 As = interpolateAcc(player.b2body.getPosition().x,player.b2body.getPosition().y);
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
                        double Ax = G * -(player.b2body.getPosition().x - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                        double Ay = G * -(player.b2body.getPosition().y - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
                        float Vx = (float) (player.b2body.getLinearVelocity().x + Ax * dt);
                        float Vy = (float) (player.b2body.getLinearVelocity().y + Ay * dt);
                        Vector2 vec2 = new Vector2(Vx, Vy);
                        player.b2body.setLinearVelocity(vec2);
                    } else {
                        //System.out.println("Naughty not Star Only");
                        dt = (float) frameTime;
                        double r;
                        double Ax = 0;
                        double Ay = 0;
                        for (i = 0; i < nP; i++) {
                            r = Math.sqrt(Math.pow(gravData[i + 1][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[i + 1][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                            Ax = Ax + (G * -(player.b2body.getPosition().x - gravData[i + 1][1] / MyGdxGame.PPM) * (gravData[i + 1][1] / MyGdxGame.PPM) / Math.pow(r, 3));
                            Ay = Ay + (G * -(player.b2body.getPosition().y - gravData[i + 1][2] / MyGdxGame.PPM) * (gravData[i + 1][2] / MyGdxGame.PPM) / Math.pow(r, 3));
                        }
                        r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - player.b2body.getPosition().x, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - player.b2body.getPosition().y, 2));
                        Ax = Ax + G * -(player.b2body.getPosition().x - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                        Ay = Ay + G * -(player.b2body.getPosition().y - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
                        float Vx = (float) (player.b2body.getLinearVelocity().x + Ax * dt);
                        float Vy = (float) (player.b2body.getLinearVelocity().y + Ay * dt);
                        Vector2 vec2 = new Vector2(Vx, Vy);
                        player.b2body.setLinearVelocity(vec2);
                    }
                }
            }
        }

        // move sprite image to location of sprite with respect to above controls
        player.update((float) frameTime);
        hud.update((float) frameTime);

        // Get camera to follow sprite x and y movements
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        int points = 4;

        // update camera and render
        gamecam.update();
        renderer.setView(gamecam);
    }

    public void getTrace(int G,float dt) {
        //System.out.println("Calculating traj delta "+dt);
        if (starOnly) {
            dt = step / 100;
            int i;
            double initX = 0;
            double initY = 0;
            double sX = player.b2body.getPosition().x;
            double sY = player.b2body.getPosition().y;
            float pX = 0;
            float pY = 0;
            float Vx = player.b2body.getLinearVelocity().x;
            float Vy = player.b2body.getLinearVelocity().y;
            float VxNew;
            float VyNew;
            //System.out.println("Vx "+Vx+" Vy "+Vy);
            //System.out.println("player pos 2 "+player.b2body.getPosition().x+" by "+player.b2body.getPosition().y);
            path.clear();
            iCount = 0;
            for (i = 0; i < 100000; i++) {
                // cycle through points predicting player movement
                double r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                double Ax = G * -(sX - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                double Ay = G * -(sY - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
                sX = sX + pX;
                sY = sY + pY;
                path.add(new Vector2((float) (winWidth / 2 + (initX) * winWorldX), (float) (winHeight / 2 + (initY) * winWorldY)));
                if (iCount < 13) {
                    velComps[2][iCount] = sX;
                    velComps[3][iCount] = sY;
                    iCount++;
                }
                //System.out.println("scrWidth "+wWid+" pX "+pX+" pY "+pY+" X "+((initX)*winWorldX)+" Y "+((initY)*winWorldY));
                VxNew = (float) (Vx + Ax * dt);                    // new velocity at end of timestep
                pX = (float) (VxNew) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                //Vx = (float) (Vx + Ax*dt);
                VyNew = (float) (Vy + Ay * dt);                    // new velocity at end of timestep
                pY = (float) (VyNew) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                //Vy = (float) (Vy + Ay*dt);
                Vx = VxNew;
                Vy = VyNew;
                initX = initX + pX;
                initY = initY + pY;
            }

            iCount = 1;
            /*
            SUVAT
            S ?
            U tick
            V tick
            A tick
            T tick

            */

        } else {
            //System.out.println("tracing for planets too");
            dt = step / 100;
            int i;
            int j;
            double initX = 0;
            double initY = 0;
            double sX = player.b2body.getPosition().x;
            double sY = player.b2body.getPosition().y;
            float pX = 0;
            float pY = 0;
            float Vx = player.b2body.getLinearVelocity().x;
            float Vy = player.b2body.getLinearVelocity().y;
            float VxNew;
            float VyNew;
            //System.out.println("Vx "+Vx+" Vy "+Vy);
            //System.out.println("player pos 2 "+player.b2body.getPosition().x+" by "+player.b2body.getPosition().y);
            path.clear();
            iCount = 0;
            for (j = 0; j < 100000; j++) {
                // cycle through points predicting player movement
                //System.out.println("i "+i);
                double r;
                double Ax = 0;
                double Ay = 0;
                for(i=0;i<nP;i++){
                    //System.out.println("i "+i+" nP"+nP);
                    r = Math.sqrt(Math.pow(gravData[i+1][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[i+1][2] / MyGdxGame.PPM - sY, 2));
                    Ax = Ax + (G * -(sX - gravData[i+1][1] / MyGdxGame.PPM) * (gravData[i+1][1] / MyGdxGame.PPM) / Math.pow(r, 3));
                    Ay = Ay + (G * -(sY - gravData[i+1][2] / MyGdxGame.PPM) * (gravData[i+1][2] / MyGdxGame.PPM) / Math.pow(r, 3));
                }
                r = Math.sqrt(Math.pow(gravData[0][1] / MyGdxGame.PPM - sX, 2) + Math.pow(gravData[0][2] / MyGdxGame.PPM - sY, 2));
                Ax = G * -(sX - gravData[0][1] / MyGdxGame.PPM) * (gravData[0][1] / MyGdxGame.PPM) / Math.pow(r, 3);
                Ay = G * -(sY - gravData[0][2] / MyGdxGame.PPM) * (gravData[0][2] / MyGdxGame.PPM) / Math.pow(r, 3);
                sX = sX + pX;
                sY = sY + pY;
                path.add(new Vector2((float) (winWidth / 2 + (initX) * winWorldX), (float) (winHeight / 2 + (initY) * winWorldY)));
                //System.out.println("scrWidth "+wWid+" pX "+pX+" pY "+pY+" X "+((initX)*winWorldX)+" Y "+((initY)*winWorldY));
                VxNew = (float) (Vx + Ax * dt);                    // new velocity at end of timestep
                pX = (float) (VxNew) * dt;// + Ax*Math.pow(dt,2)/2);     // displacement across timestep
                //Vx = (float) (Vx + Ax*dt);
                VyNew = (float) (Vy + Ay * dt);                    // new velocity at end of timestep
                pY = (float) (VyNew) * dt;// + Ay*Math.pow(dt,2)/2);     // displacement across timestep
                //Vy = (float) (Vy + Ay*dt);
                Vx = VxNew;
                Vy = VyNew;
                initX = initX + pX;
                initY = initY + pY;
            }

            iCount = 1;

        }
    }

    public void setAutoOrbit(){
        // Auto orbit most gravitationally-strong stellar body
        System.out.println("Should nullify the click");
        //orbButClick=false;
        System.out.println("autoOrbit "+autoOrbit);
        if(autoOrbit==true){
            autoOrbit=false;
        } else {
            // start auto orbitting
            autoOrbit=true;
            dR = Math.sqrt(Math.pow(solarRad/MyGdxGame.PPM-player.b2body.getPosition().x,2)+Math.pow(solarRad/MyGdxGame.PPM-player.b2body.getPosition().y,2));
            omega = 2*Math.PI/(15);         // deg/s
            theta = Math.atan((player.b2body.getPosition().x-solarRad/MyGdxGame.PPM)/(player.b2body.getPosition().y-solarRad/MyGdxGame.PPM));
            System.out.println("calcing theta "+theta*180/Math.PI);
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
                    System.out.println("ZERO VELOCITY");
                }
            }
            System.out.println("calced theta "+theta*180/Math.PI);
            theta = 2*Math.PI + theta;
            v = omega*dR;
        }
    }


   /* //@Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        gamecam.translate(-x,y);
        return true;
    }
*/
}
