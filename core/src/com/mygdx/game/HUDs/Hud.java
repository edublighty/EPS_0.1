package com.mygdx.game.HUDs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.MyGdxGame;


import javax.swing.text.ViewFactory;

public class Hud implements Disposable{
    public static boolean burnFlag;
    public boolean planetFlag;
    public boolean butFlag;
    public Table table;
    public Stage stage;
    public Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private static Integer temp;

    Label healthLabel;
    Label worldLabel;
    Label livesLabel;
    Label countdownLabel;
    static Label tempLabel;
    Label fillerLabel;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount=0;
        score = 0;
        temp = 30;
        burnFlag = false;
        planetFlag = false;
        butFlag = false;

        viewport = new FitViewport(MyGdxGame.V_WIDTH/MyGdxGame.PPM,MyGdxGame.V_HEIGHT/MyGdxGame.PPM, new OrthographicCamera());
        stage = new Stage(viewport,sb);

        table = new Table();
        table.top();
        table.setFillParent(true);  //table is size of parent stage

        healthLabel = new Label(String.format("%03d",100), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("here", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesLabel = new Label(String.format("%01d",3), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countdownLabel = new Label(String.format("%03d",100), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        tempLabel = new Label(String.format("%03d",temp), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        textButtonStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("button/simpButs.atlas"));
        skin = new Skin(buttonAtlas);
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        button = new TextButton("", textButtonStyle);
        //stage.addActor(button);


        table.add(livesLabel).expandX().padTop(2);     // takes up entire top row
        table.add(worldLabel).expandX().padTop(2);     // takes up half
        table.add(healthLabel).expandX().padTop(2);    // takes up third
        table.add(countdownLabel).expandX().padTop(2);    // takes up quarter
        table.add(tempLabel).expandX().padTop(2);    // takes up fifth
        //table.row();
        //table.add(fillerLabel).expandY().padTop(2);
        //table.row();
        //table.add(button).expandX().padTop(2);
        stage.addActor(table);
    }

    public static void addTemp(int value){
        temp += value;
        //System.out.println(burnFlag);
        tempLabel.setText(String.format("%03d",temp));
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >=1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d",worldTimer));
            timeCount = 0;
            if(burnFlag){
                //System.out.println("Burning");
                addTemp(3);
            } else {

            }
            if(planetFlag){
                // player is above a planet
                if(!butFlag){
                    // button is not showing so add it to stage
                    table.add(button).expandX().padTop(2);
                }
            } else {
                // player not above planet
                if(butFlag){
                    // button should not be showing
                    table.clearChildren();
                }
            }
        }
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

}
