package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.orbShipSprite;
import com.mygdx.game.HUDs.Hud;
import com.mygdx.game.Tools.WorldContactListener;
import com.mygdx.game.Tools.b2dWorldCreator;
import com.mygdx.game.Screens.systemScreen.Stage.buttonOverlay;

public class orbitPlay implements Screen {

    // Tiled variables
    private TiledMap map;
    private TmxMapLoader mapLoader;
    OrthographicCamera gamecam;
    private Viewport gameport;
    private OrthogonalTiledMapRenderer renderer;
    private MyGdxGame game;
    private TextureAtlas atlas;
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
    public static boolean inAtmos;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;

    // sprites
    public orbShipSprite player;

    public orbitPlay(MyGdxGame game){

        atlas = new TextureAtlas("PNGsPacked/Pointers.atlas");

        this.game=game;
        inAtmos = false;

        System.out.println("orbitPlay - created tmrenderer");
        // camera to follow sprite
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        gameport = new FitViewport(MyGdxGame.V_WIDTH/MyGdxGame.PPM,MyGdxGame.V_HEIGHT/MyGdxGame.PPM,gamecam) {             /*ScreenViewport allows player to see more with bigger screen*/        };

        // create game overlay
        hud = new Hud(game.batch);
        //buttonoverlay = new buttonOverlay(game.batch,game);

        // load map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("orbMaps/oSys.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);

        // initialise gamecam centrally
        gamecam.position.set(0,0,0);//gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        //buttonoverlay.stage.getCamera().position.y = 0;

        world = new World(new Vector2(0,0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        // method for locating objects from TMX map
        new b2dWorldCreator(world,map,this,null,null,0,0,0,0);

        // create sprite in game world
        player = new orbShipSprite(world, this);

        // contact listener for objects
        WorldContactListener WCL = new WorldContactListener();
        WCL.setGame(game,this);
        world.setContactListener(WCL);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show () {
    }

    @Override
    public void render (float delta) {
        update(delta);

        // Clear background colour
        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        // draw camera for sprite
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

        // HUD projection
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(inAtmos){
            game.setScreen(new atmosPlay(game));
        }

        // Potential second HUD if required
        //game.batch.setProjectionMatrix(buttonoverlay.stage.getCamera().combined);
        //buttonoverlay.stage.draw();
    }

    @Override
    public void resize (int width, int height) {
        System.out.println("play resize");
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
        System.out.println("orbitPlay dispose");
        map.dispose();
        System.out.println("map disposed");
        renderer.dispose();
        System.out.println("renderer disposed and world: "+world);
        world.dispose();
        System.out.println("world disposed");
        b2dr.dispose();
        System.out.println("b2dr disposed");
        hud.dispose();
        System.out.println("hud disposed");
    }

    public void handleInput(float dt){
        // If mouse pressed on world, move map left
        if(Gdx.input.isTouched()){
            mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
            gamecam.unproject(mousePos); // mousePos is now in world coordinates
            moveFlag = true;
            posDX = Gdx.input.getX();                           // destination on screen X
            posDY = Gdx.input.getY();                           // destination on screen Y
            //System.out.println("dX "+posDX+" dY "+posDY);
            posCX = player.b2body.getWorldCenter().x;           // current location of sprite on map x
            posCY = player.b2body.getWorldCenter().y;           // current location of sprute on map y
            //System.out.println("cX "+posCX+" cY "+posCY);

            //thetaS = Math.atan(Math.abs(posDY-posCY)/Math.abs(posDX-posCX));

            centerPosition = new Vector2((float)Gdx.graphics.getWidth() / 2, (float)Gdx.graphics.getHeight() / 2);
            posDY = Gdx.graphics.getHeight() - posDY ; //Inverse the Y
            mouseLoc = new Vector2(posDX, posDY);
            Vector2 direction = mouseLoc.sub(centerPosition);   // get direction as vector
            //float mouseAngle  = direction.angle();              // not needed as use vector directly - perhaps for rotation
            //setRotation(mouseAngle);

            player.b2body.applyLinearImpulse(new Vector2(direction), player.b2body.getWorldCenter(), true);
        }

        if(moveFlag){
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

        world.step(1/60f,6,2);  //timeStep 60 per second

        // used later for entering a station
        /*if(stationFlag) {
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
        }*/

        // move sprite image to location of sprite with respect to above controls
        player.update(dt);
        hud.update(dt);

        // Get camera to follow sprite x and y movements
        gamecam.position.x = player.b2body.getPosition().x;
        gamecam.position.y = player.b2body.getPosition().y;

        // update camera and render
        gamecam.update();
        renderer.setView(gamecam);
    }

    public void setAtmos(){

        System.out.println("trying to go atmosphere screen");
        dispose();
        game.setScreen(new atmosPlay(game));

}
}
