package com.mygdx.game.Screens.mainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.mainMenu.HUDs.mainMenuStage;

public class mainScreen implements Screen {

    private OrthogonalTiledMapRenderer renderer;
    private MyGdxGame game;
    OrthographicCamera gamecam;
    private Viewport gameport;
    private mainMenuStage mainStage;

    // Textures
    private Texture background;

    public mainScreen(MyGdxGame game){

        background = new Texture(Gdx.files.internal("mainMenu/titleScreen.png"));
        float screenRatio = 1;
        this.game = game;

        // camera to follow sprite
        gamecam = new OrthographicCamera();

        // viewport to maintain virtual aspect ratio despite screen size
        TiledMap map = new TiledMap();
        gameport = new FillViewport(MyGdxGame.V_WIDTH*screenRatio / MyGdxGame.PPM, MyGdxGame.V_HEIGHT*screenRatio / MyGdxGame.PPM, gamecam) {};
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        mainStage = new mainMenuStage(game,gameport.getWorldWidth(),gameport.getWorldHeight());

        gamecam.position.x = gameport.getWorldWidth()/2;
        gamecam.position.y = gameport.getWorldHeight()/2;

    }

    @Override
    public void show() {

    }

    public void update(float delta) {

    }


    @Override
    public void render(float delta) {

        //System.out.println("play render");
        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        renderer.render();
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        // draw stuff if needed
        game.batch.draw(background, 0, 0, gameport.getWorldWidth(), gameport.getWorldHeight());
        game.batch.end();

        game.batch.setProjectionMatrix(mainStage.stage.getCamera().combined);
        mainStage.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width,height);
        gamecam.update();
        mainStage.viewport.update(width,height);
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
}
