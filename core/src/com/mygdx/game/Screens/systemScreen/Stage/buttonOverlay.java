package com.mygdx.game.Screens.systemScreen.Stage;

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
import com.mygdx.game.Screens.systemScreen.systemPlay;
import com.mygdx.game.Screens.systemScreen.systemPlayGRAVY;

public class buttonOverlay implements Disposable {

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

    private Label butLab;
    private Label fillerLabel;
    TextButton button;
    TextButton button2;
    TextButton.TextButtonStyle textButtonStyle;
    TextButton.TextButtonStyle textButtonStyle2;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

    public buttonOverlay(SpriteBatch sb, systemPlay syst) {

        viewport = new FitViewport(MyGdxGame.V_WIDTH*3/ MyGdxGame.PPM, MyGdxGame.V_HEIGHT*3/ MyGdxGame.PPM, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle2 = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("PNGsPacked/simpButs.atlas"));
        skin = new Skin(buttonAtlas);
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle2.font = font;
        textButtonStyle2.up = skin.getDrawable("orbButtonAlt");
        textButtonStyle2.down = skin.getDrawable("orbButtonAltT");

        table = new Table();
        //table.setSize(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM);
        table.top();
        table.setFillParent(true);  //table is size of parent stage

        fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        butLab = new Label("BUM", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        button = new TextButton("", textButtonStyle);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Approaching planet");
                //((Game) Gdx.app.getApplicationListener()).setScreen(new orbitPlay(game));
                System.out.println("systemPlay Disposed");
                //syst.dispose();

            }
        });

        button2 = new TextButton("",textButtonStyle2);
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Toggle orbit
                System.out.println("Toggle orbit");
                syst.orbButClick = true;
                syst.setAutoOrbit();
            }
        });

        button.setTransform(true);
        button2.setTransform(true);
        button.setScale(0.2f);
        //button2.setScale(0.2f);

        table.add(fillerLabel).expandY().padTop(2);
        table.row();
        table.add(button2).size(1500/MyGdxGame.PPM,1500/MyGdxGame.PPM);
        //table.add(fillerLabel).expandX();
        //table.add(button);//.size(200/MyGdxGame.PPM,200/MyGdxGame.PPM);//.fillX().uniformX();
        table.setTouchable(Touchable.enabled);

        stage.addActor(table);

    }

    @Override
    public void dispose() {

    }

}
