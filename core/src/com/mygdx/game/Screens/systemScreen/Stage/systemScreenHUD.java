package com.mygdx.game.Screens.systemScreen.Stage;

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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.systemPlay;
import com.mygdx.game.Screens.systemScreen.systemScreen;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class systemScreenHUD {

    public Table table;
    public Stage stage;
    public Viewport viewport;
    Label currentLoc;
    systemScreen2 screen;

    private Label fillerLabel;

    // Icon "buttons"
    TextButton shieldBar;
    TextButton healthBar;
    TextureAtlas barsAtlas;
    Skin barsSkin;

    TextButton.TextButtonStyle barStyle;

    Label healthLabel;
    Label healthString;
    Label shieldLabel;
    Label shieldString;
    Label currLocVar;

    public systemScreenHUD(MyGdxGame game, SpriteBatch sb, float viewportWidth, float viewportHeight, systemScreen2 screen){

        this.screen = screen;
        viewport = new FillViewport(viewportWidth*MyGdxGame.PPM, viewportHeight*MyGdxGame.PPM, new OrthographicCamera());
        TextButton.TextButtonStyle menuButtons = new TextButton.TextButtonStyle();
        BitmapFont font = new BitmapFont();
        //font.getData().setScale(1f/20);
        TextureAtlas buttonsAt = screen.getThrustAt();
        table = new Table();
        table.setFillParent(true);  //table is size of parent stage
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(buttonsAt);

        barsAtlas = new TextureAtlas(Gdx.files.internal("batScreen/status_bars/bars.atlas"));
        barsSkin = new Skin(barsAtlas);

        barStyle = new TextButton.TextButtonStyle();
        barStyle.font = font;
        barStyle.up = barsSkin.getDrawable("shieldBar");
        barStyle.down = barsSkin.getDrawable("shieldBar");

        Label fillerLabel = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        currentLoc = new Label("Currently at: ", new Label.LabelStyle(font, Color.WHITE));

        String healthPer = "100%";
        String shieldPer = "100%";
        healthLabel = new Label("Health: "+healthPer+String.format("%03d",100)+"%", new Label.LabelStyle(new BitmapFont(), Color.RED));
        shieldLabel = new Label("Shields: "+shieldPer+String.format("%03d",100)+"%", new Label.LabelStyle(new BitmapFont(), Color.RED));
        shieldBar = new TextButton("",barStyle);
        healthBar = new TextButton("",barStyle);
        Label currLoc = new Label("Current Location: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        currLocVar = new Label("harry plopper", new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        int o2Per = 100;
        int tempPer = 100;
        int radPer = 10;

        Label o2Label = new Label("02: "+o2Per+String.format("%03d",o2Per)+"%", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label tempLabel = new Label("Temperature: "+tempPer+String.format("%03d",tempPer)+"*C", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label radLabel = new Label("Radiation: "+radPer+String.format("%03d",radPer)+" rad/s", new Label.LabelStyle(new BitmapFont(), Color.RED));

        o2Label.setX(viewport.getWorldWidth()/2);
        o2Label.setY(viewport.getWorldHeight()/2);
        o2Label.setWidth(viewportWidth/1);
        o2Label.setHeight(viewportHeight/1);
        o2Label.setFontScale(2);
        //stage.addActor(o2Label);

        /*float fontScale = 7f;
        healthLabel.setFontScale(fontScale);
        shieldLabel.setFontScale(fontScale);*/

        table.add(fillerLabel).colspan(1);
        table.add(shieldLabel);//.size(healthLabel.getWidth(),healthLabel.getHeight());
        table.add(shieldBar).align(Align.left).colspan(2);
        table.add(fillerLabel).expandX();
        table.add(currLoc).row();
        table.add(fillerLabel);//.colspan(1);
        table.add(healthLabel);//.size(healthLabel.getWidth(),healthLabel.getHeight());
        table.add(healthBar).align(Align.left).colspan(2).expandX().fillX();
        table.add(fillerLabel).expandX();
        table.add(currLocVar).row();//.colspan(1).row();
        table.row();
        table.add(fillerLabel).expandY().row();
        table.add(fillerLabel).expandX();


        table.setTouchable(Touchable.enabled);
        table.pack();
        //table.debug();

        stage.addActor(table);

    }

    public void updateHUDFont(){
        currentLoc.setFontScale(screen.gamecam.zoom);
    }

}
