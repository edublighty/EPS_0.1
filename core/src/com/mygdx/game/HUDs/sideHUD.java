package com.mygdx.game.HUDs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class sideHUD {

    public Table table;
    public Stage stage;
    public Viewport viewport;
    public String playerLoc;
    public String playerDest;
    public String distToDest;

    public sideHUD(MyGdxGame game, float viewportWidth, float viewportHeight) {

        viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        TextButton.TextButtonStyle menuButtons = new TextButton.TextButtonStyle();
        BitmapFont font = new BitmapFont();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("mainMenu/menuButts.atlas"));
        table = new Table();
        table.setFillParent(true);  //table is size of parent stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(buttonAtlas);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("button_up");
        buttonStyle.down = skin.getDrawable("button_down");


        TextButton galaxyScreen = new TextButton("Galaxy Map",buttonStyle);
        galaxyScreen.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new galaxyScreen(game));

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }
        });

        TextButton shipScreen = new TextButton("Ship Screen",buttonStyle);
        shipScreen.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new galaxyScreen(game));

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }
        });

        TextButton systemScreen = new TextButton("System Map",buttonStyle);
        systemScreen.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new galaxyScreen(game));

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }
        });

        TextButton planetScreen = new TextButton("Planet Screen",buttonStyle);
        planetScreen.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new galaxyScreen(game));

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }
        });

        TextButton unpause = new TextButton("UNPAUSE",buttonStyle);
        unpause.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new galaxyScreen(game));

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }
        });

        TextButton exit = new TextButton("EXIT",buttonStyle);
        exit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new galaxyScreen(game));

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
            }
        });

        Label fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(fillerLabel).colspan(1);
        table.add(fillerLabel).colspan(8).expandX().expandY();
        table.row();
        table.add(galaxyScreen);
        table.row();
        table.add(shipScreen);
        table.row();
        table.add(systemScreen);
        table.row();
        table.add(unpause);
        table.row();
        table.add(exit);
        table.row();
        table.add(fillerLabel).colspan(1);
        table.add(fillerLabel).colspan(8).expandX().expandY();
        table.setTouchable(Touchable.enabled);
        table.pack();
        table.debug();

        stage.addActor(table);
    }
}
