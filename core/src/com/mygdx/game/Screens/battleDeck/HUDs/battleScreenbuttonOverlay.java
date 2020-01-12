package com.mygdx.game.Screens.battleDeck.HUDs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.Sprites.doorImage;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;

public class battleScreenbuttonOverlay implements Disposable {

    public static boolean burnFlag;
    public boolean planetFlag;
    public boolean butFlag;
    public Table table;
    public Stage stage;
    public Stage stage2;
    public Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private static Integer temp;

    private Label butLab;
    private Label fillerLabel;
    TextButton playerSelBut;
    TextButton enemySelBut;
    TextButton testBut;

    // Icon "buttons"
    TextButton playerO2;
    TextButton playerCamera;
    TextButton playerCross;
    TextButton playerEngine;
    TextButton playerPower;
    TextButton playerShields;
    TextButton playerSteering;
    TextButton enemyO2;
    TextButton enemyCamera;
    TextButton enemyCross;
    TextButton enemyEngine;
    TextButton enemyPower;
    TextButton enemyShields;
    TextButton enemySteering;
    TextButton shieldBar;
    TextButton healthBar;
    TextButton enemyShieldBar;
    TextButton enemyhealthBar;
    TextureAtlas iconAtlas;
    TextureAtlas barsAtlas;
    Skin iconSkin;
    Skin barsSkin;

    TextButton.TextButtonStyle o2PlayerUpStyle;
    TextButton.TextButtonStyle o2PlayerDownStyle;
    TextButton.TextButtonStyle o2EnemyUpStyle;
    TextButton.TextButtonStyle o2EnemyDownStyle;

    TextButton.TextButtonStyle cameraPlayerUpStyle;
    TextButton.TextButtonStyle cameraPlayerDownStyle;
    TextButton.TextButtonStyle cameraEnemyUpStyle;
    TextButton.TextButtonStyle cameraEnemyDownStyle;

    TextButton.TextButtonStyle crossPlayerUpStyle;
    TextButton.TextButtonStyle crossPlayerDownStyle;
    TextButton.TextButtonStyle crossEnemyUpStyle;
    TextButton.TextButtonStyle crossEnemyDownStyle;

    TextButton.TextButtonStyle enginePlayerUpStyle;
    TextButton.TextButtonStyle enginePlayerDownStyle;
    TextButton.TextButtonStyle engineEnemyUpStyle;
    TextButton.TextButtonStyle engineEnemyDownStyle;

    TextButton.TextButtonStyle powerPlayerUpStyle;
    TextButton.TextButtonStyle powerPlayerDownStyle;
    TextButton.TextButtonStyle powerEnemyUpStyle;
    TextButton.TextButtonStyle powerEnemyDownStyle;

    TextButton.TextButtonStyle shieldsPlayerUpStyle;
    TextButton.TextButtonStyle shieldsPlayerDownStyle;
    TextButton.TextButtonStyle shieldsEnemyUpStyle;
    TextButton.TextButtonStyle shieldsEnemyDownStyle;

    TextButton.TextButtonStyle steeringPlayerUpStyle;
    TextButton.TextButtonStyle steeringPlayerDownStyle;
    TextButton.TextButtonStyle steeringEnemyUpStyle;
    TextButton.TextButtonStyle steeringEnemyDownStyle;

    TextButton.TextButtonStyle barStyle;

    TextButton.TextButtonStyle playerSelStyle;
    TextButton.TextButtonStyle enemySelStyle;
    BitmapFont font;
    Skin skin;

    //TextButton.TextButtonStyle steeringStyle;

    TextureAtlas buttonAtlas;

    Label healthLabel;
    Label healthString;
    Label enemyhealthLabel;
    Label enemyhealthString;
    Label shieldLabel;
    Label shieldString;
    Label enemyshieldLabel;
    Label enemyshieldString;

    int tempI;


    public enum systemUpStates {o2,camera,cross,engine,power,shields,steering};

    public battleScreenbuttonOverlay(World world, SpriteBatch sb, battleShipScreen syst, float bottomButsH, float viewportWidth, float viewportHeight, float[][] pDoors) {

        //viewport = new ExtendViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        viewport = new FitViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        System.out.println("viewport width "+viewport.getWorldWidth()+" and height "+viewport.getWorldHeight());
        InputMultiplexer mplexer = new InputMultiplexer();
        stage = new Stage(viewport, sb);
        stage2 = new Stage(viewport,sb);
        Gdx.input.setInputProcessor(mplexer);
        mplexer.addProcessor(stage2);
        mplexer.addProcessor(stage);

        float sideSqr = viewportHeight - bottomButsH*2;    // the side is 80% of screen height

        playerSelStyle = new TextButton.TextButtonStyle();
        enemySelStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("batScreen/buttons/batScreenButtons.atlas"));
        table = new Table();
        //table.top();
        table.setFillParent(true);  //table is size of parent stage

        skin = new Skin(buttonAtlas);

        playerSelStyle.font = font;
        playerSelStyle.up = skin.getDrawable("PLAYERswitch");
        playerSelStyle.down = skin.getDrawable("PLAYERswitch");
        enemySelStyle.font = font;
        enemySelStyle.up = skin.getDrawable("ENEMYswitch");
        enemySelStyle.down = skin.getDrawable("ENEMYswitch");

        barsAtlas = new TextureAtlas(Gdx.files.internal("batScreen/status_bars/bars.atlas"));
        iconAtlas = new TextureAtlas(Gdx.files.internal("batScreen/status_icons/icons.atlas"));
        iconSkin = new Skin(iconAtlas);
        barsSkin = new Skin(barsAtlas);

        // o2 icons
        o2PlayerUpStyle = new TextButton.TextButtonStyle();
        o2PlayerUpStyle.font = font;
        o2PlayerUpStyle.up = iconSkin.getDrawable("o2PlayerUp");
        o2PlayerUpStyle.down = iconSkin.getDrawable("o2PlayerUp");
        //
        o2PlayerDownStyle = new TextButton.TextButtonStyle();
        o2PlayerDownStyle.font = font;
        o2PlayerDownStyle.up = iconSkin.getDrawable("o2PlayerDown");
        o2PlayerDownStyle.down = iconSkin.getDrawable("o2PlayerDown");
        //
        o2EnemyUpStyle = new TextButton.TextButtonStyle();
        o2EnemyUpStyle.font = font;
        o2EnemyUpStyle.up = iconSkin.getDrawable("o2EnemyUp");
        o2EnemyUpStyle.down = iconSkin.getDrawable("o2EnemyUp");
        //
        o2EnemyDownStyle = new TextButton.TextButtonStyle();
        o2EnemyDownStyle.font = font;
        o2EnemyDownStyle.up = iconSkin.getDrawable("o2EnemyDown");
        o2EnemyDownStyle.down = iconSkin.getDrawable("o2EnemyDown");

        // medbay icons
        crossPlayerUpStyle = new TextButton.TextButtonStyle();
        crossPlayerUpStyle.font = font;
        crossPlayerUpStyle.up = iconSkin.getDrawable("crossPlayerUp");
        crossPlayerUpStyle.down = iconSkin.getDrawable("crossPlayerUp");
        //
        crossPlayerDownStyle = new TextButton.TextButtonStyle();
        crossPlayerDownStyle.font = font;
        crossPlayerDownStyle.up = iconSkin.getDrawable("crossPlayerDown");
        crossPlayerDownStyle.down = iconSkin.getDrawable("crossPlayerDown");
        //
        crossEnemyUpStyle = new TextButton.TextButtonStyle();
        crossEnemyUpStyle.font = font;
        crossEnemyUpStyle.up = iconSkin.getDrawable("crossEnemyUp");
        crossEnemyUpStyle.down = iconSkin.getDrawable("crossEnemyUp");
        //
        crossEnemyDownStyle = new TextButton.TextButtonStyle();
        crossEnemyDownStyle.font = font;
        crossEnemyDownStyle.up = iconSkin.getDrawable("crossEnemyDown");
        crossEnemyDownStyle.down = iconSkin.getDrawable("crossEnemyDown");

        // surveillance icons
        cameraPlayerUpStyle = new TextButton.TextButtonStyle();
        cameraPlayerUpStyle.font = font;
        cameraPlayerUpStyle.up = iconSkin.getDrawable("cameraPlayerUp");
        cameraPlayerUpStyle.down = iconSkin.getDrawable("cameraPlayerUp");
        //
        cameraPlayerDownStyle = new TextButton.TextButtonStyle();
        cameraPlayerDownStyle.font = font;
        cameraPlayerDownStyle.up = iconSkin.getDrawable("cameraPlayerDown");
        cameraPlayerDownStyle.down = iconSkin.getDrawable("cameraPlayerDown");
        //
        cameraEnemyUpStyle = new TextButton.TextButtonStyle();
        cameraEnemyUpStyle.font = font;
        cameraEnemyUpStyle.up = iconSkin.getDrawable("cameraEnemyUp");
        cameraEnemyUpStyle.down = iconSkin.getDrawable("cameraEnemyUp");
        //
        cameraEnemyDownStyle = new TextButton.TextButtonStyle();
        cameraEnemyDownStyle.font = font;
        cameraEnemyDownStyle.up = iconSkin.getDrawable("cameraEnemyDown");
        cameraEnemyDownStyle.down = iconSkin.getDrawable("cameraEnemyDown");

        // engine icons
        enginePlayerUpStyle = new TextButton.TextButtonStyle();
        enginePlayerUpStyle.font = font;
        enginePlayerUpStyle.up = iconSkin.getDrawable("enginePlayerUp");
        enginePlayerUpStyle.down = iconSkin.getDrawable("enginePlayerUp");
        //
        enginePlayerDownStyle = new TextButton.TextButtonStyle();
        enginePlayerDownStyle.font = font;
        enginePlayerDownStyle.up = iconSkin.getDrawable("enginePlayerDown");
        enginePlayerDownStyle.down = iconSkin.getDrawable("enginePlayerDown");
        //
        engineEnemyUpStyle = new TextButton.TextButtonStyle();
        engineEnemyUpStyle.font = font;
        engineEnemyUpStyle.up = iconSkin.getDrawable("engineEnemyUp");
        engineEnemyUpStyle.down = iconSkin.getDrawable("engineEnemyUp");
        //
        engineEnemyDownStyle = new TextButton.TextButtonStyle();
        engineEnemyDownStyle.font = font;
        engineEnemyDownStyle.up = iconSkin.getDrawable("engineEnemyDown");
        engineEnemyDownStyle.down = iconSkin.getDrawable("engineEnemyDown");

        // power icons
        powerPlayerUpStyle = new TextButton.TextButtonStyle();
        powerPlayerUpStyle.font = font;
        powerPlayerUpStyle.up = iconSkin.getDrawable("powerPlayerUp");
        powerPlayerUpStyle.down = iconSkin.getDrawable("powerPlayerUp");
        //
        powerPlayerDownStyle = new TextButton.TextButtonStyle();
        powerPlayerDownStyle.font = font;
        powerPlayerDownStyle.up = iconSkin.getDrawable("powerPlayerDown");
        powerPlayerDownStyle.down = iconSkin.getDrawable("powerPlayerDown");
        //
        powerEnemyUpStyle = new TextButton.TextButtonStyle();
        powerEnemyUpStyle.font = font;
        powerEnemyUpStyle.up = iconSkin.getDrawable("powerEnemyUp");
        powerEnemyUpStyle.down = iconSkin.getDrawable("powerEnemyUp");
        //
        powerEnemyDownStyle = new TextButton.TextButtonStyle();
        powerEnemyDownStyle.font = font;
        powerEnemyDownStyle.up = iconSkin.getDrawable("powerEnemyDown");
        powerEnemyDownStyle.down = iconSkin.getDrawable("powerEnemyDown");

        // steering icons
        steeringPlayerUpStyle = new TextButton.TextButtonStyle();
        steeringPlayerUpStyle.font = font;
        steeringPlayerUpStyle.up = iconSkin.getDrawable("steeringPlayerUp");
        steeringPlayerUpStyle.down = iconSkin.getDrawable("steeringPlayerUp");
        //
        steeringPlayerDownStyle = new TextButton.TextButtonStyle();
        steeringPlayerDownStyle.font = font;
        steeringPlayerDownStyle.up = iconSkin.getDrawable("steeringPlayerDown");
        steeringPlayerDownStyle.down = iconSkin.getDrawable("steeringPlayerDown");
        //
        steeringEnemyUpStyle = new TextButton.TextButtonStyle();
        steeringEnemyUpStyle.font = font;
        steeringEnemyUpStyle.up = iconSkin.getDrawable("steeringEnemyUp");
        steeringEnemyUpStyle.down = iconSkin.getDrawable("steeringEnemyUp");
        //
        steeringEnemyDownStyle = new TextButton.TextButtonStyle();
        steeringEnemyDownStyle.font = font;
        steeringEnemyDownStyle.up = iconSkin.getDrawable("steeringEnemyDown");
        steeringEnemyDownStyle.down = iconSkin.getDrawable("steeringEnemyDown");

        // shields icons
        shieldsPlayerUpStyle = new TextButton.TextButtonStyle();
        shieldsPlayerUpStyle.font = font;
        shieldsPlayerUpStyle.up = iconSkin.getDrawable("shieldsPlayerUp");
        shieldsPlayerUpStyle.down = iconSkin.getDrawable("shieldsPlayerUp");
        //
        shieldsPlayerDownStyle = new TextButton.TextButtonStyle();
        shieldsPlayerDownStyle.font = font;
        shieldsPlayerDownStyle.up = iconSkin.getDrawable("shieldsPlayerDown");
        shieldsPlayerDownStyle.down = iconSkin.getDrawable("shieldsPlayerDown");
        //
        shieldsEnemyUpStyle = new TextButton.TextButtonStyle();
        shieldsEnemyUpStyle.font = font;
        shieldsEnemyUpStyle.up = iconSkin.getDrawable("shieldsEnemyUp");
        shieldsEnemyUpStyle.down = iconSkin.getDrawable("shieldsEnemyUp");
        //
        shieldsEnemyDownStyle = new TextButton.TextButtonStyle();
        shieldsEnemyDownStyle.font = font;
        shieldsEnemyDownStyle.up = iconSkin.getDrawable("shieldsEnemyDown");
        shieldsEnemyDownStyle.down = iconSkin.getDrawable("shieldsEnemyDown");

        barStyle = new TextButton.TextButtonStyle();
        barStyle.font = font;
        barStyle.up = barsSkin.getDrawable("shieldBar");
        barStyle.down = barsSkin.getDrawable("shieldBar");

       /* int i = 0;
        for(systemUpStates dir : systemUpStates.values()){
            // Counts number of icons required
            i++;
        }
        TextureAtlas.AtlasRegion playerIcons[] = new TextureAtlas.AtlasRegion[i];
*/
        //i = 0;

        playerO2 = new TextButton("",o2PlayerUpStyle);
        playerCamera = new TextButton("",cameraPlayerDownStyle);
        playerCross = new TextButton("",crossPlayerUpStyle);
        playerEngine = new TextButton("",enginePlayerUpStyle);
        playerPower = new TextButton("",powerPlayerDownStyle);
        playerShields = new TextButton("",shieldsPlayerUpStyle);
        playerSteering = new TextButton("",steeringPlayerUpStyle);

        enemyO2 = new TextButton("",o2EnemyUpStyle);
        enemyCamera = new TextButton("",cameraEnemyUpStyle);
        enemyCross = new TextButton("",crossEnemyDownStyle);
        enemyEngine = new TextButton("",engineEnemyUpStyle);
        enemyPower = new TextButton("",powerEnemyUpStyle);
        enemyShields = new TextButton("",shieldsEnemyDownStyle);
        enemySteering = new TextButton("",steeringEnemyUpStyle);

        healthLabel = new Label("Health: "+String.format("%03d",100)+"%", new Label.LabelStyle(new BitmapFont(), Color.RED));
        shieldLabel = new Label("Shields: "+String.format("%03d",100)+"%", new Label.LabelStyle(new BitmapFont(), Color.RED));
        //healthString = new Label("Shields: ", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        enemyhealthLabel = new Label("Health: "+String.format("%03d",100)+"%", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        enemyshieldLabel = new Label("Shields: "+String.format("%03d",100)+"%", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        //enemyhealthString = new Label("Shields: ", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        shieldBar = new TextButton("",barStyle);
        healthBar = new TextButton("",barStyle);
        enemyShieldBar = new TextButton("",barStyle);
        enemyhealthBar = new TextButton("",barStyle);

        /*table = new Table();
        //table.setSize(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM);
        table.top();
        table.setFillParent(true);  //table is size of parent stage*/

        fillerLabel = new Label(" ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //butLab = new Label("BUM", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        playerSelBut = new TextButton("PLAYER", playerSelStyle);
        playerSelBut.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                //return true;
                //System.out.println("Player view");
                // Switch to player
                //syst.playerView = true;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                System.out.println("Player view");
                // Switch to player
                syst.playerView = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchup");
            }
        });

        testBut = new TextButton("PLAYER", playerSelStyle);
        testBut.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                //return true;
                //System.out.println("Player view");
                // Switch to player
                //syst.playerView = true;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                System.out.println("boop view");
                // Switch to player
                syst.playerView = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("boopity maybe?");
            }
        });

        enemySelBut = new TextButton("ENEMY",enemySelStyle);
        enemySelBut.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                //return true;
                //System.out.println("Enemy view");
                // Switch to ENEMY
                //syst.playerView = false;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                System.out.println("Enemy view");
                // Switch to ENEMY
                syst.playerView = false;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchup");
            }
        });

        playerSelBut.setTransform(true);
        enemySelBut.setTransform(true);
        playerCross.setTransform(true);
        playerCamera.setTransform(true);
        playerShields.setTransform(true);
        playerPower.setTransform(true);
        enemyShieldBar.setTransform(true);
        float playerButRatio = playerSelBut.getHeight()/playerSelBut.getWidth();
        float enemyButRatio = enemySelBut.getHeight()/enemySelBut.getWidth();
        float butHeight = viewportHeight/9;    // split height between 7 icons

        float fontScale = 7f;

        healthLabel.setFontScale(fontScale);
        shieldLabel.setFontScale(fontScale);
        enemyhealthLabel.setFontScale(fontScale);
        enemyshieldLabel.setFontScale(fontScale);

        table.add(fillerLabel);//.colspan(1);
        table.add(shieldLabel);//.size(healthLabel.getWidth(),healthLabel.getHeight());
        table.add(shieldBar).align(Align.left).colspan(2).expandX().fillX();
        table.add(fillerLabel).size(200,200).expandX();
        table.add(enemyShieldBar).align(Align.left).colspan(2).expandX().fillX();
        table.add(enemyshieldLabel);//.expandX().colspan(1);
        table.add(fillerLabel).row();//.colspan(1).row();

        table.add(fillerLabel);//.colspan(1);
        table.add(healthLabel);//.size(healthLabel.getWidth(),healthLabel.getHeight());
        table.add(healthBar).align(Align.left).colspan(2).expandX().fillX();
        table.add(fillerLabel).size(200,200).expandX();
        table.add(enemyhealthBar).align(Align.left).colspan(2).expandX().fillX();
        table.add(enemyhealthLabel);//.expandX().colspan(1);
        table.add(fillerLabel).row();//.colspan(1).row();

        table.add(fillerLabel).expandY().row();

        table.add(playerCamera).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemyCamera).size(butHeight,butHeight).colspan(1).row();

        table.add(playerCross).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemyCross).size(butHeight,butHeight).colspan(1).row();

        table.add(playerEngine).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemyEngine).size(butHeight,butHeight).colspan(1).row();

        table.add(playerO2).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemyO2).size(butHeight,butHeight).colspan(1).row();

        table.add(playerPower).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemyPower).size(butHeight,butHeight).colspan(1).row();

        table.add(playerShields).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemyShields).size(butHeight,butHeight).colspan(1).row();

        table.add(playerSteering).size(butHeight,butHeight).colspan(1);
        table.add(fillerLabel).colspan(7).expandX();
        table.add(enemySteering).size(butHeight,butHeight).colspan(1).row();

        table.add(fillerLabel).expandY().row();

        table.add(fillerLabel).colspan(1);
        table.add(playerSelBut).size(butHeight*playerButRatio*2).colspan(3).expandX();
        table.add(fillerLabel).colspan(1);
        table.add(enemySelBut).size(butHeight*enemyButRatio*2).colspan(3).expandX();
        table.add(fillerLabel).colspan(1).row();

        TextureAtlas shipObjsAt = new TextureAtlas("batScreen/objects/shipObjects.atlas");
        Skin skin2 = new Skin(shipObjsAt);
        String doorString;

        System.out.println("length of pDoors "+pDoors[0].length);
        //Image[] pDoorImgs = new Image[pDoors[0].length];
        doorImage[] pDoorImgs = new doorImage[pDoors[0].length];
        boolean[] pDoorsOpen = new boolean[pDoors[0].length];
        for(int i=0;i<pDoorImgs.length;i++){
            if(pDoors[0][i]>pDoors[1][i]){
                if(pDoors[4][i]==1){
                    doorString = "airlockH";
                } else {
                    doorString = "doorH";
                }
            } else {
                if(pDoors[4][i]==1) {
                    doorString = "airlockV";
                } else {
                    doorString = "doorV";
                }
            }

            pDoorImgs[i] = new doorImage(world,syst,pDoors,i,doorString);
            //pDoorImgs[i].setTouchable(Touchable.enabled);
            stage2.addActor(pDoorImgs[i]);
        }

        doorImage img = new doorImage(world,syst,pDoors,2,"doorH");
        //stage.setDebugAll(true);
        stage2.setDebugAll(true);
        table.setTouchable(Touchable.enabled);
        //table.setDebug(true);
        table.pack();


        stage.addActor(table);

        //stage2.addActor(img);
        testBut.setPosition(500,500);
        testBut.setSize(bottomButsH/playerButRatio, bottomButsH);
        //stage.addActor(testBut);
        stage.getDebugColor();
        //stage.addActor(testBut);

    }

    public void update() {
    }

    @Override
    public void dispose() {

    }

}
