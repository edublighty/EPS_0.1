package com.mygdx.game.Screens.galScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HUDs.sideHUD;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.Stage.galScreenActors;
import com.mygdx.game.Screens.galScreen.Stage.galScreenActorsSystemInfoOnly;
import com.mygdx.game.Screens.galScreen.Stage.galScreenActorsSystembyPlanets;
import com.mygdx.game.Screens.galScreen.Stage.galScreenHUD;
import com.mygdx.game.Screens.galScreen.Tools.galaxyGenerator;
import com.mygdx.game.Screens.galScreen.Tools.galaxyProcessor;
import com.mygdx.game.Screens.galScreen.Tools.starGenerator;
import com.mygdx.game.Screens.systemScreen.Tools.systemGenerator2;
import com.mygdx.game.Sprites.planetSprite;
import com.mygdx.game.Sprites.sysShipSprite;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.galOverlay;

import java.io.BufferedReader;
import java.io.IOException;

public class galaxyScreen implements Screen {

    // Tiled variables
    private TiledMap map;
    OrthographicCamera gamecam;
    OrthographicCamera gamecam2;
    private Viewport gameport;
    private MyGdxGame game;
    private TextureAtlas Platlas;           // atlas for planet objects
    private TextureAtlas Statlas;           // atlas for star objects
    public galScreenActors galActors;
    public galScreenHUD galHUD;
    public sideHUD pauseHUD;
    private galScreenActorsSystemInfoOnly galSystInfoOnly;
    private galScreenActorsSystembyPlanets galSystPlanetsToo;
    public float newX;
    public float newY;
    public boolean paused;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // sprites
    public sysShipSprite player;
    public static boolean burnFlag;
    public static boolean planetFlag;
    private float[][] galPoints;
    private String[] galNames;
    public TextureAtlas starsAt;
    public TextureAtlas starsAt2;
    public TextureAtlas starAnim;
    private TextureAtlas starSelsAt;
    private TextureAtlas tilesAt;

    public boolean notStarted;

    private InputMultiplexer multiplexer;
    private InputProcessor galActorsInputs;

    private float maxZoomX;
    private float maxZoomY;
    private float minZoomX;
    private float minZoomY;

    public boolean showingPlanets;
    public float planetsCutOffY;

    private String[][] systemData;

    private TextureAtlas thrustAt;

    public galaxyScreen(MyGdxGame game){

        //starGenerator imgGen = new starGenerator();
        systemGenerator2 systemGen = new systemGenerator2(game,this);
        //new galaxyProcessor();

        galPoints = new float[900][2];
        galNames = new String[900];
        paused = false;

        String sCurrentLine;
        starsAt = new TextureAtlas("galaxyScreen/bgGalaxy.atlas");
        starsAt2 = new TextureAtlas("galaxyScreen/Stars/starsAtlas.atlas");
        starSelsAt = new TextureAtlas("galaxyScreen/starSels3.atlas");
        thrustAt = new TextureAtlas("systemScreen/ui/thrustControl.atlas");
        Platlas = new TextureAtlas("PNGsPacked/allPlanets.atlas");
        Statlas = new TextureAtlas("PNGsPacked/Stars.atlas");
        starAnim = new TextureAtlas("systemScreen/starPNGs/Randomised/starsAnim.atlas");//conwaysGOL/starsConway.atlas");
        tilesAt = new TextureAtlas("PNGsPacked/biomespack50px.atlas");
        FileHandle handle = Gdx.files.internal("Arrays/galaxyAscaledAndTransed2.txt");
        BufferedReader reader = new BufferedReader(handle.reader());
        FileHandle handle2 = Gdx.files.internal("Arrays/galNamesSon.txt");
        BufferedReader reader2 = new BufferedReader(handle2.reader());

        this.game=game;

        // camera to follow touch and drag
        gamecam = new OrthographicCamera();
        gamecam2 = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam) {};

        /*gamecam.position.x = gameport.getWorldWidth()*MyGdxGame.PPM/2;
        gamecam.position.y = gameport.getWorldHeight()*MyGdxGame.PPM/2;*/

        // create game overlay
        //hud = new Hud(game.batch);
        //galoverlay = new galOverlay(game.batch,game,this);

        //buttonoverlay = new buttonOverlay(game.batch,game,this);

        //galaxyGenerator galGen = new galaxyGenerator();

        // initialise gamecam centrally
        gamecam.position.set(gameport.getWorldWidth()*MyGdxGame.PPM/2,gameport.getWorldHeight()*MyGdxGame.PPM/2,0);

        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        float BLx = gameport.getWorldWidth()/2 - gameport.getWorldHeight()/2;
        float BLy = 0;

        boolean notStarted = true;

        int i = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                galPoints[i][0] = (Float.parseFloat(arr[0]))/5000;//*gameport.getWorldHeight()/5000+BLx;
                galPoints[i][1] = (Float.parseFloat(arr[1]))/5000;//*gameport.getWorldHeight()/5000+BLy;
                //galNames[i] = (arr[2]);

                if(notStarted){
                    game.currentGalX = galPoints[i][0];
                    game.currentGalY = galPoints[i][1];
                    game.currentGali = i;
                    notStarted = false;
                }
                i++;
                //System.out.println("i is "+i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        i = 0;
        try {
            while ((sCurrentLine = reader2.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                galNames[i] = (arr[0]);
                if(notStarted){
                    game.currentGalName = galNames[i];
                    notStarted = false;
                }
                //galNames[i] = (arr[2]);
                System.out.println("namne "+galNames[i]);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        galHUD = new galScreenHUD(game,game.batch,gameport.getWorldWidth()*MyGdxGame.PPM,gameport.getWorldHeight()*MyGdxGame.PPM,this);
        //galSystInfoOnly = new galScreenActorsSystemInfoOnly(game,this,game.batch,gameport.getWorldWidth()*MyGdxGame.PPM,gameport.getWorldHeight()*MyGdxGame.PPM);
        galSystPlanetsToo = new galScreenActorsSystembyPlanets(game,this,game.batch,gameport.getWorldWidth()*MyGdxGame.PPM,gameport.getWorldHeight()*MyGdxGame.PPM);
        galActors = new galScreenActors(game,this, galHUD, galSystPlanetsToo, game.batch, gameport.getWorldWidth(), gameport.getWorldHeight(), galPoints, gamecam, galNames);
        galHUD.setCurrentLocVar(systemData[game.currentGali][0]);
        pauseHUD = new sideHUD(game,gameport.getWorldWidth(),gameport.getWorldHeight());

        System.out.println("galActors exists? "+galActors);
        System.out.println("galActors stage exists? "+galActors.stage);

        maxZoomX = galActors.maxZoomX;
        maxZoomY = galActors.maxZoomY;
        minZoomX = galActors.maxZoomX*0.1f;
        minZoomY = galActors.maxZoomY*0.1f;

        // set contact listeners for objects
        world.setContactListener(new WorldContactListener());

        this.notStarted = true;

        multiplexer = new InputMultiplexer();
        GestureDetector gdActors = new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                System.out.println("touchdown galaxyScreen");
                return true;
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
                System.out.println("pan galaxyScreen");
                //galActors.camera.translate((int)(-deltaX/10), (int)(deltaY/10));
                float mouseY = Gdx.input.getY();
                float mouseYCut = gameport.getScreenHeight() - gameport.getScreenHeight()*planetsCutOffY;
                //System.out.println("mouseY "+mouseY+" vs planetsCutOff "+mouseYCut);
                if(showingPlanets && mouseY>mouseYCut) {
                    galSystPlanetsToo.camera2.position.add((-deltaX*30 / (MyGdxGame.PPM)), 0, 0);
                    galSystPlanetsToo.camera2.update();
                } else {
                    galActors.camera.position.add((-deltaX / (MyGdxGame.PPM)), (deltaY / (MyGdxGame.PPM)), 0);
                    galActors.camera.update();
                }
                return true;
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
        multiplexer.addProcessor(galActors.stage);
        multiplexer.addProcessor(galSystPlanetsToo.stage);
        multiplexer.addProcessor(galSystPlanetsToo.stage2);
        multiplexer.addProcessor(gdActors);
        multiplexer.addProcessor(galHUD.stage);
        Gdx.input.setInputProcessor(multiplexer);

    }

    public void setSystemData(String[][] systemData){
        this.systemData = systemData;
    }

    public String[][] getSystemData(){
        System.out.println(systemData.length);
        return systemData;
    }

    public TextureAtlas getStarsAt(){
        return starsAt;
    }

    public TextureAtlas getStarsAt2(){
        return starsAt2;
    }

    public TextureAtlas getStarSelsAt(){
        return starSelsAt;
    }

    public galScreenActors getGalActors(){ return galActors; }

    public TextureAtlas getTilesAt(){ return tilesAt; }

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

        game.batch.setProjectionMatrix(gamecam.combined);

        /*
        game.batch.begin();
        // draw stuff here
        game.batch.end();
*/

        game.batch.setProjectionMatrix(galActors.stage.getCamera().combined);
        galActors.stage.draw();

        game.batch.setProjectionMatrix(galActors.stage2.getCamera().combined);
        galActors.stage2.draw();

        game.batch.setProjectionMatrix(galHUD.stage.getCamera().combined);
        galHUD.stage.draw();

        if(paused) {
            game.batch.setProjectionMatrix(pauseHUD.stage.getCamera().combined);
            pauseHUD.stage.draw();
        }

        //game.batch.setProjectionMatrix(galSystInfoOnly.stage.getCamera().combined);
        //galSystInfoOnly.stage.draw();

        game.batch.setProjectionMatrix(galSystPlanetsToo.stage.getCamera().combined);
        galSystPlanetsToo.stage.draw();

        game.batch.setProjectionMatrix(galSystPlanetsToo.stage2.getCamera().combined);
        galSystPlanetsToo.stage2.draw();

    }

    @Override
    public void resize (int width, int height) {
        gameport.update(width,height);
        gamecam.update();
        //galActors.viewport.update(width,height);
        galHUD.viewport.update(width,height);
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
        world.dispose();
        b2dr.dispose();
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){                         // destination on screen Y
            if(galActors.camera.zoom < 2) {
                galActors.camera.zoom += 0.02;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            if(galActors.camera.zoom > 0.25) {
                galActors.camera.zoom -= 0.02;
            }
        }

    }

    public boolean touchDragged2(int screenX, int screenY){//, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        /*if(x<1&&y<1){
            // point click - no drag
            posDX = Gdx.input.getX();                           // destination on screen X
            posDY = Gdx.input.getY();                           // destination on screen Y
            //System.out.println("DX "+posDX+" and DY "+posDY);
            setPointers();

        }*/

        gamecam.translate(-x,y);
        return true;
    }

    public TextureAtlas getThrustAt(){
        return thrustAt;
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

    public void setPointers(){
        //Vector3 vec=new Vector3(posDX,posDY,0);
        //gamecam.unproject(vec);
        float GCX = gamecam.position.x;
        float GCY = gamecam.position.y;
        //System.out.println("DX2 "+vec.x+" and DY2 "+vec.y);
        //System.out.println("GCX "+GCX+" and GCY "+GCY);
        //System.out.println("width "+Gdx.graphics.getWidth()+" and height "+Gdx.graphics.getHeight());
        //newX = (vec.x)*10-50;
       // newY = (vec.y)*10-50;

        int i=0;
        double R1;
        double R2;
        double ansR;
        int ansI;
        R1 = Math.sqrt(Math.pow((newX-galPoints[i][0]),2)+Math.pow((newY-galPoints[i][1]),2));
        i++;
        ansR = R1;
        ansI = i;
        //System.out.println("newX "+newX+" and X "+galPoints[i][0]);
        //System.out.println("newY "+newY+" and Y "+galPoints[i][1]);
        while(i<870){
            // search array for closest point
            R2 = Math.sqrt(Math.pow((newX-galPoints[i][0]),2)+Math.pow((newY-galPoints[i][1]),2));
            if(R2<ansR){
                //System.out.println("i is "+i);
                ansR=R2;
                ansI = i;
            }
            R1=R2;
            i++;
        }

    }


    public void update(float dt){
        handleInput(dt);

        world.step(1/60f,6,2);  //timeStep 60 per second
/*

        if(burnFlag){

        }

        if(planetFlag) {
            // player over planet
            //System.out.println("trying to raise it: " + buttonoverlay.stage.getCamera().position.y);
            if(buttonoverlay.stage.getCamera().position.y > 150) {
                buttonoverlay.stage.getCamera().position.y = buttonoverlay.stage.getCamera().position.y - 1;
            } else {
                // stage in position
            }
        } else {
            // player not over planet
            //System.out.println("trying to lower it: " + buttonoverlay.stage.getCamera().position.y);
            if (buttonoverlay.stage.getCamera().position.y < 180) {
                buttonoverlay.stage.getCamera().position.y = buttonoverlay.stage.getCamera().position.y + 1;
            }
        }
*//*

        // move sprite image to location of sprite with respect to above controls
        player.update(dt);
        hud.update(dt);

        // Get camera to follow sprite x and y movements
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;
*/
        // update camera and render
        gamecam.update();
        //renderer.setView(gamecam);
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