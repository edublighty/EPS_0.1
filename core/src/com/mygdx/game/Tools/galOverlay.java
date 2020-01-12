package com.mygdx.game.Tools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.systemPlay;
//import com.sun.glass.ui.Size;

public class galOverlay implements Disposable {

    public static boolean burnFlag;
    public boolean planetFlag;
    public boolean butFlag;
    public Table table;
    public Table tableB;
    public Stage stage;
    public Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private static Integer temp;

    private Label butLab;
    private Label fillerLabel;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    public galOverlay(SpriteBatch sb,MyGdxGame game,galaxyScreen syst) {

        viewport = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("button/simpButs.atlas"));
        skin = new Skin(buttonAtlas);
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");

        table = new Table();
        table.top();
        table.setFillParent(true);  //table is size of parent stage

        tableB = new Table();
        tableB.top();
        tableB.setFillParent(true);  //table is size of parent stage

        fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        butLab = new Label("BUM", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Label currLabel = new Label("CURRENT LOCATION", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label destLabel = new Label("DESTINATION", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label currPlace = new Label("System Name: here", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label destPlace = new Label("System Name: there", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label currPlans = new Label("Planets: "+ String.format("%02d",5), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label destPlans = new Label("Planets: "+ String.format("%02d",3), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label currStatus = new Label("Status: OK", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label destStatus = new Label("Status: HOSTILE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label warpDist = new Label("Distance to destination: "+ String.format("%04d",3000), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label warpStatus = new Label("WARP: OK", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("button/simpButs.atlas"));
        skin = new Skin(buttonAtlas);
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        button = new TextButton("", textButtonStyle);

        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("WARPING");
                game.galX = syst.newX;
                game.galY = syst.newY;
                ((Game) Gdx.app.getApplicationListener()).setScreen(new systemPlay(game));
                //syst.dispose();
            }
        });

        table.add(currLabel).expandX().padTop(2);     // takes up entire top row
        table.add(currPlace).expandX().padTop(2);     // takes up entire top row
        table.add(currPlans).expandX().padTop(2);     // takes up entire top row
        table.add(currStatus).expandX().padTop(2);     // takes up entire top row
        table.row();
        table.add(destLabel).expandX().padTop(2);     // takes up half
        table.add(destPlace).expandX().padTop(2);     // takes up entire top row
        table.add(destPlans).expandX().padTop(2);     // takes up entire top row
        table.add(destStatus).expandX().padTop(2);     // takes up entire top row
        table.add(fillerLabel).fillY().uniformY();//expandY().padTop(2);
        table.row();
        tableB.bottom();
        tableB.add(button).expandX().padTop(2);//fillX().uniformX();
        tableB.add(warpDist).expandX().padTop(2);     // takes up half
        tableB.add(warpStatus).expandX().padTop(2);     // takes up half
        tableB.setTouchable(Touchable.enabled);

        stage.addActor(table);
        stage.addActor(tableB);

    }

    @Override
    public void dispose() {

    }

}
