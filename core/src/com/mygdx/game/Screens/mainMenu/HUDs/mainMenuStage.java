package com.mygdx.game.Screens.mainMenu.HUDs;

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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.systemPlay;
import com.mygdx.game.Screens.systemScreen.systemScreen;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

import java.awt.Font;

public class mainMenuStage implements Disposable {

    public Table table;
    public Stage stage;
    public Viewport viewport;


    public mainMenuStage(MyGdxGame game, float viewportWidth, float viewportHeight){

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


        TextButton buttonContinue = new TextButton("Continue",buttonStyle);
        buttonContinue.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdowncontibue");
                // Travel to warp screen
                game.setScreen(new systemScreen2(game));
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdowncontibue");
            }
        });

        TextButton buttonNewGame = new TextButton("New Game",buttonStyle);
        TextButton buttonExit = new TextButton("Exit",buttonStyle);

        Label fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(fillerLabel);
        table.row();
        table.add(fillerLabel);
        table.row();
        table.add(fillerLabel);
        table.row();
        table.add(buttonContinue);
        table.row();
        table.add(buttonNewGame);
        table.row();
        table.add(buttonExit);
        table.row();
        table.add(fillerLabel);
        table.setTouchable(Touchable.enabled);
        table.pack();
        //table.debug();

        stage.addActor(table);

    }

    @Override
    public void dispose() {

    }
}
