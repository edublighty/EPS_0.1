package com.mygdx.game.Screens.galScreen.Superseded;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.HUDs.Hud;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.planetSprite;
import com.mygdx.game.Sprites.selectSprite;
import com.mygdx.game.Sprites.sysShipSprite;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.galOverlay;

import java.io.BufferedReader;
import java.io.IOException;

public class galaxyScreen implements Screen {

    // Tiled variables
    private TiledMap map;
    private TmxMapLoader mapLoader;
    OrthographicCamera gamecam;
    private Viewport gameport;
    private OrthogonalTiledMapRenderer renderer;
    private ShapeRenderer shRenderer;
    private MyGdxGame game;
    private TextureAtlas atlas;             // atlas for pointers
    private TextureAtlas Platlas;           // atlas for planet objects
    private TextureAtlas Statlas;           // atlas for star objects
    public Hud hud;
    public galOverlay galoverlay;
    private int posDX;      // Destination X
    private int posDY;      // Destination Y
    private float posCX;      // Current X
    private float posCY;      // Current Y
    private double thetaS;        // angle of travel for player ship
    private float zS = 25;
    private boolean moveFlag;
    private double deltaZ;
    Vector2 centerPosition;
    Vector2 mouseLoc;
    Vector3 mousePos;
    public float newX;
    public float newY;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // sprites
    public sysShipSprite player;
    public planetSprite plnt;
    public static boolean burnFlag;
    public static boolean planetFlag;
    private double[][] galPoints;
    private String[] galNames;
    private double[] starData;
    private int nP;
    private selectSprite[] systems = new selectSprite[900];
    private selectSprite current;
    private selectSprite dest;
    private selectSprite curr;
    private TextureRegion nextT;
    private TextureRegion thisT;

    public boolean notStarted;

    public float mainWidth;
    public float mainHeight;

    public galaxyScreen(MyGdxGame game){

        galPoints = new double[900][2];
        galNames = new String[900];

        String sCurrentLine;
        FileHandle handle = Gdx.files.internal("Arrays/galaxyAscaledAndTransed2.txt");
        BufferedReader reader = new BufferedReader(handle.reader());

        int i = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                galPoints[i][0] = Double.parseDouble(arr[0]);
                galPoints[i][1] = Double.parseDouble(arr[1]);
                //galNames[i] = (arr[2]);
                i++;
                //System.out.println("i is "+i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }


        atlas = new TextureAtlas("PNGsPacked/galSelections2.atlas");
        TextureAtlas.AtlasRegion selectAReg = atlas.findRegion("selectPointsmol");
        TextureRegion selectT = new TextureRegion(selectAReg,0,0,50,50);
        TextureAtlas.AtlasRegion nextAReg = atlas.findRegion("selector2");
        nextT = new TextureRegion(nextAReg,0,0,100,100);
        TextureAtlas.AtlasRegion currAReg = atlas.findRegion("selectPointsmolCurr");
        thisT = new TextureRegion(currAReg, 0,0, 50, 50);

        this.game=game;
        burnFlag = false;
        planetFlag = false;

        // camera to follow touch and drag
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new ExtendViewport(150,150,gamecam);//MyGdxGame.V_WIDTH/MyGdxGame.PPM,MyGdxGame.V_HEIGHT/MyGdxGame.PPM,gamecam) {             /*ScreenViewport allows player to see more with bigger screen*/        };

        // create game overlay
        //hud = new Hud(game.batch);
        //galoverlay = new galOverlay(game.batch,game,this);
        //buttonoverlay = new buttonOverlay(game.batch,game,this);

        // load map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("galaxyScreen/galTiledMap3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);
        shRenderer = new ShapeRenderer();

        // initialise gamecam centrally
        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        int wWid = (int) gameport.getWorldWidth();
        int wHei = (int) gameport.getWorldHeight();
        System.out.println("width "+wWid+" height "+wHei);
        //buttonoverlay.stage.getCamera().position.y = 0;

        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        //new b2dWorldCreator(world,map,this,planetData);
/*
        i=0;
        // create sprite in game world
        while(i<100){
            new circObj( world, map, (float) galPoints[i][0], (float) galPoints[i][1], 50);
            i++;
        }*/
        //player = new sysShipSprite(world, this);
        i=0;
        while(i<869) {
            //System.out.println("X "+galPoints[i][0]+" and Y "+galPoints[i][1]);
            systems[i] = new selectSprite(selectT, (float) galPoints[i][0], (float) galPoints[i][1]);
            i++;
        }
        int sType = 1; //(int) starData[0];
        String sObj = "star"+Integer.toString(sType);
        System.out.print("star no. "+sObj);
        //starr = new starSprite(world, this, sObj, nP, wWid, wHei);

        System.out.println("pre new width");

        // set contact listeners for objects
        world.setContactListener(new WorldContactListener());

        this.notStarted = true;

    }

    // Create sprites for later drawing
    public void genStellars(){

    }

    // atlas for pointers
    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show () {
        System.out.println("play show");

        System.out.println("play show - created camera");
    }

    @Override
    public void render (float delta) {
        int wWid = (int) gameport.getWorldWidth();
        int wHei = (int) gameport.getWorldHeight();
        //System.out.println("width "+wWid+" height "+wHei);
        update(delta);

        //System.out.println("play render");
        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        if(notStarted) {
            //System.out.println(notStarted);
            game.batch.setProjectionMatrix(gamecam.combined);
            game.batch.begin();
            int i = 0;
            while(i<869){
                //System.out.println("trying to draw planet "+i);
                systems[i].draw(game.batch);
                i++;
            }
            if(dest != null){
                dest.draw(game.batch);
                curr.draw(game.batch);
            }

            game.batch.end();
/*
            shRenderer.setColor(Color.BLUE);
            shRenderer.begin(ShapeRenderer.ShapeType.Line);
            int i = 0;
            while(i<500) {
                shRenderer.circle((float) galPoints[i][0], (float) galPoints[i][1], 3);
                i++;
            }
            shRenderer.end();*/

            //notStarted = false;
        } else {
            game.batch.setProjectionMatrix(gamecam.combined);
            game.batch.begin();
            player.draw(game.batch);
            game.batch.end();
        }

        /*// draw camera for sprite
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();*/

        //game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //hud.stage.draw();

        game.batch.setProjectionMatrix(galoverlay.stage.getCamera().combined);
        galoverlay.stage.draw();
    }

    @Override
    public void resize (int width, int height) {
        System.out.println("play resize");
        //System.out.println("width "+width+" and height "+height);
        gameport.update(width,height);
        gamecam.update();
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
        // THIS IS WHERE TOUCH DRAG WILL OCCUR
        if(Gdx.input.isTouched()) {
            int X = Gdx.input.getX();                           // destination on screen X
            int Y = Gdx.input.getY();                           // destination on screen Y
            touchDragged(X,Y);
        }
    }

    public boolean touchDragged(int screenX, int screenY){//, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();

        System.out.println("x "+x+" and y "+y);

        if(x<1&&y<1){
            // point click - no drag
            posDX = Gdx.input.getX();                           // destination on screen X
            posDY = Gdx.input.getY();                           // destination on screen Y
            //System.out.println("DX "+posDX+" and DY "+posDY);
            setPointers();

        }

        gamecam.translate(-x,y);
        return true;
    }

    public void setPointers(){
        Vector3 vec=new Vector3(posDX,posDY,0);
        gamecam.unproject(vec);
        float GCX = gamecam.position.x;
        float GCY = gamecam.position.y;
        //System.out.println("DX2 "+vec.x+" and DY2 "+vec.y);
        //System.out.println("GCX "+GCX+" and GCY "+GCY);
        //System.out.println("width "+Gdx.graphics.getWidth()+" and height "+Gdx.graphics.getHeight());
        newX = (vec.x)*10-50;
        newY = (vec.y)*10-50;

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

        //System.out.println("newX "+newX+" and ansX "+galPoints[ansI][0]);
        //System.out.println("newY "+newY+" and ansY "+galPoints[ansI][1]);
        //System.out.println("ansR "+ansR+" and ansI "+ansI);

        dest = new selectSprite( nextT,(float) galPoints[ansI][0]-0,(float) galPoints[ansI][1]+0);

        curr = new selectSprite( thisT, (float) galPoints[80][0]-0,(float) galPoints[80][1]+0);
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
        renderer.setView(gamecam);
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