package com.mygdx.game.Screens.galScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.Actors.galTiles;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.Actors.systemTiles;
import com.mygdx.game.Screens.systemScreen.systemPlay;
import com.mygdx.game.Screens.systemScreen.systemPlayGRAVY;

public class galScreenHUD {

    public Table table;
    public Stage stage;
    public Viewport viewport;
    public String playerLoc;
    public String playerDest;
    public String distToDest;
    public Label currentLocVar;
    public Label destLocVar;
    public Label distToVar;
    private Label curLocVar;

    public galScreenHUD(MyGdxGame game, SpriteBatch sb, float viewportWidth, float viewportHeight, galaxyScreen screen) {

        viewport = new FillViewport(viewportWidth, viewportHeight, new OrthographicCamera());
        TextButton.TextButtonStyle menuButtons = new TextButton.TextButtonStyle();
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2f);
        TextureAtlas playPauseAt = new TextureAtlas(Gdx.files.internal("mainMenu/playPause.atlas"));
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("mainMenu/menuButts.atlas"));

        table = new Table();
        table.setFillParent(true);  //table is size of parent stage
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        // ------------------------------------------------------------------------------------------------------------

        // TOP LEFT PANEL FOR CURRENT LOCATION

        float margin = viewportHeight/100;//.5f;

        String texture = "TitleBG";
        float tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        float tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectcurLoc = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);


        String curLocS = "SHMEEMAR";

        Label curLocLabel = new Label("CURRENT LOCATION: ", new Label.LabelStyle(font, Color.WHITE));
        curLocVar = new Label(curLocS,new Label.LabelStyle(font, Color.YELLOW));

        //curLocLabel.setScale(2f);

        float curLocXLab = margin*5;
        float curLocYLab = viewportHeight - margin - curLocLabel.getHeight();
        curLocLabel.setX(curLocXLab);
        curLocLabel.setY(curLocYLab);
        float curLocXVar = curLocXLab + margin*5;
        float curLocYVar = curLocYLab - margin - curLocLabel.getHeight();
        curLocVar.setX(curLocXVar);
        curLocVar.setY(curLocYVar);


        float curLocTileHeight = curLocLabel.getHeight() + curLocVar.getHeight() + margin*2;
        float curLocTileWidth = viewportWidth/3;
        float curLocTileX = margin;
        float curLocTileY = curLocYVar - margin;
        galTiles curLocTile = new galTiles(screen,curLocTileWidth,curLocTileHeight,curLocTileX,curLocTileY,texture);
        stage.addActor(curLocTile);
        stage.addActor(curLocLabel);
        stage.addActor(curLocVar);
        // ----------------------------------------------------------------------------------------------------------------------------------

        // BOTTOM PANEL FOR SYSTEM MAP AND PLANET INFO




        Skin skin = new Skin(playPauseAt);
        Skin skin2 = new Skin(buttonAtlas);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle buttonStyle2 = new TextButton.TextButtonStyle();

        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("play");
        buttonStyle.down = skin.getDrawable("pause");

        buttonStyle2.font = font;
        buttonStyle2.up = skin2.getDrawable("button_up");
        buttonStyle2.down = skin2.getDrawable("button_down");

        TextButton playPause = new TextButton("", buttonStyle);
        playPause.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdowncontibue");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdowncontibue");
                // suck a dick
                if(screen.paused){
                    // currently paused
                    screen.paused = false;
                } else {
                    screen.paused = true;
                }

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchdowncontibue");
            }
        });
        playPause.setTransform(true);

        TextButton warpButt = new TextButton("WARP", buttonStyle2);
        warpButt.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");

            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                // suck a dick

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("touchdowncontibue");
                game.setScreen(new systemPlay(game));
            }
        });
        warpButt.setTransform(true);

        playerLoc = game.currentGalName;
        playerDest = "Dest";
        distToDest = "9";

        Label fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label currentLoc = new Label("Currently at: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        currentLocVar = new Label(playerLoc, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label destLoc = new Label("Destination: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        destLocVar = new Label(playerDest, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label distTo = new Label("Distance to dest'n: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        distToVar = new Label(distToDest, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //table.add(playPause).size(viewportHeight/20).colspan(1);
        table.add(currentLoc).colspan(2);
        table.add(fillerLabel).colspan(2);
        table.add(destLoc).colspan(2);
        table.add(distTo).colspan(2);
        table.row();
        table.add(fillerLabel).colspan(1);
        table.add(currentLocVar).colspan(2);
        table.add(fillerLabel).colspan(2);
        table.add(destLocVar).colspan(2);
        table.add(distToVar).colspan(2);
        table.row();
        table.add(fillerLabel).colspan(1);
        table.add(fillerLabel).colspan(8).expandX().expandY();
        table.row();
        table.add(fillerLabel).colspan(4).expandX();
        table.add(warpButt).size(viewportHeight/20).colspan(1);
        table.add(fillerLabel).colspan(4).expandX();
        table.setTouchable(Touchable.enabled);
        table.pack();
        table.debug();

        //stage.addActor(table);

    }

    public void setCurrentLocVar(String currentLoc){
        curLocVar.setText(currentLoc);
    }
}
