package com.mygdx.game.Screens.battleDeck.HUDs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.battleDeck.Actor.testActor;
import com.mygdx.game.Screens.battleDeck.Sprites.doorImage;
import com.mygdx.game.Screens.battleDeck.battleShipScreen;

import java.util.ArrayList;
import java.util.List;

public class battleScreenActorOverlay implements Disposable {

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
    TextButton testBut;

    private Label butLab;
    private Label fillerLabel;
    TextButton playerSelBut;
    TextButton enemySelBut;

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

    private TextureAtlas shipObjsAt;

    public enum systemUpStates {o2,camera,cross,engine,power,shields,steering};

    public battleScreenActorOverlay(World world, SpriteBatch sb, battleShipScreen syst, float bottomButsH, float viewportWidth, float viewportHeight, List<Float> testDoor) {

        System.out.println("pDoors time "+testDoor.size());
        viewport = new ExtendViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);
        shipObjsAt = new TextureAtlas("batScreen/objects/shipObjects.atlas");

        playerSelStyle = new TextButton.TextButtonStyle();
        enemySelStyle = new TextButton.TextButtonStyle();
        font = new BitmapFont();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("batScreen/buttons/batScreenButtons.atlas"));
        Texture texture = shipObjsAt.findRegion("doorH").getTexture();
        table = new Table();
        Skin skin2 = new Skin(shipObjsAt);
        //table.top();
        table.setFillParent(true);  //table is size of parent stage

        skin = new Skin(buttonAtlas);

        playerSelStyle.font = font;
        playerSelStyle.up = skin.getDrawable("PLAYERswitch");
        playerSelStyle.down = skin.getDrawable("PLAYERswitch");

       /* int i = 0;
        for(systemUpStates dir : systemUpStates.values()){
            // Counts number of icons required
            i++;
        }
        TextureAtlas.AtlasRegion playerIcons[] = new TextureAtlas.AtlasRegion[i];
*/
        //i = 0;


        /*table = new Table();
        //table.setSize(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM);
        table.top();
        table.setFillParent(true);  //table is size of parent stage*/
        //butLab = new Label("BUM", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //button.setScale(0.2f);
        //button2.setScale(0.2f);
        //bottomButsH=bottomButsH*2;


/*        table.add(fillerLabel).colspan(1);
        table.add(fillerLabel).colspan(4).expandX();
        table.add(fillerLabel).colspan(1).row();*/


        /*enemyShieldBar.setSize(5,5);
        enemyShieldBar.setScale(10f);*/
        TextureAtlas.AtlasRegion doors[];
        doors = new TextureAtlas.AtlasRegion[2];
        doors[0] = shipObjsAt.findRegion("doorH");
        doors[1] = shipObjsAt.findRegion("doorV");
        int sWidth=(int) viewport.getWorldWidth();
        int sHeight=(int) viewport.getWorldHeight();
        float margin = sHeight/10;
        float sideSqr = sHeight - margin*2;    // the side is 80% of screen height
        float doorHWidth = doors[0].getRegionWidth();
        float doorHHeight = doors[0].getRegionHeight();
        float doorVWidth = doors[1].getRegionWidth();
        float doorVHeight = doors[1].getRegionHeight();
        float sideGrphc = 6*100;
        // ratio of screen to graphic is:
        float sGrphcRatio = sideSqr/sideGrphc;
        List<Float> tempDoor = new ArrayList<Float>();
        tempDoor.add(doorHWidth*sGrphcRatio);
        tempDoor.add(doorHHeight*sGrphcRatio);
        float doorX = 50 + 100*sGrphcRatio/2 + doorHWidth*sGrphcRatio/2;
        tempDoor.add(doorX);
        float doorY = 50 + 100*sGrphcRatio - doorHHeight*sGrphcRatio/2;
        tempDoor.add(doorY);
        /*
        doorImage doorI = new doorImage(world,syst,tempDoor,"doorH");
        doorI.setTouchable(Touchable.enabled);
        doorI.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("flciked enit");
                    syst.testBool = true;
                    //System.out.println("X:" + x + " Y:" + y);
                    //return true;
                    //System.out.println("Player view");
                    // Switch to player
                    //syst.playerView = true;
                }

            @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    //System.out.println("X:" + x + " Y:" + y);
                    System.out.println("doorr lcok down");
                // Switch to player
                syst.testBool = true;
                syst.playerView = true;
                return true;
                }

            @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("dorr clock up");
                syst.testBool = true;
                }
        });
*/
        testActor tAct = new testActor(syst);
        tAct.setTouchable(Touchable.enabled);
        tAct.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("flciked enit");
                syst.testBool = true;
                //System.out.println("X:" + x + " Y:" + y);
                //return true;
                //System.out.println("Player view");
                // Switch to player
                //syst.playerView = true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                System.out.println("doorr lcok down");
                // Switch to player
                syst.testBool = true;
                syst.playerView = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("dorr clock up");
                syst.testBool = true;
            }
        });

        Image img = new Image(skin2.getDrawable("doorV"));

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
                System.out.println("testbut size "+testBut.getX()+" "+testBut.getY()+" "+testBut.getWidth()+" "+testBut.getHeight());
                System.out.println("img size "+img.getX()+" "+img.getY()+" "+img.getWidth()+" "+img.getHeight());
                // Switch to player
                syst.playerView = true;
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("test but boopity");
                System.out.println("testbut size "+testBut.getX()+" "+testBut.getY()+" "+testBut.getWidth()+" "+testBut.getHeight());
                System.out.println("img size "+img.getX()+" "+img.getY()+" "+img.getWidth()+" "+img.getHeight());
            }
        });


        img.setTouchable(Touchable.enabled);
        tAct.setTouchable(Touchable.enabled);
        img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("best boop view");
                //return true;
                //System.out.println("Player view");
                // Switch to player
                //syst.playerView = true;
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //System.out.println("X:" + x + " Y:" + y);
                System.out.println("btes boop view");
                // Switch to player
                syst.playerView = true;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(" btest boopity");
            }
        });

        testBut.setPosition(250,255);
        testBut.setSize(bottomButsH/1, bottomButsH);
        //stage.addActor(testBut);


        img.setPosition(testDoor.get(2),testDoor.get(3));
        img.setSize(testDoor.get(1)*6, testDoor.get(0)*6);
        System.out.println("gabriel "+img.getX()+" "+img.getY()+" "+img.getWidth()+" "+img.getHeight());
        img.setBounds(img.getX(),img.getY(),img.getWidth(),img.getHeight());

        stage.addActor(img);
        stage.setDebugAll(true);


        tAct.setPosition(testDoor.get(2),testDoor.get(3));
        tAct.setSize(testDoor.get(1)*6, testDoor.get(0)*6);
        System.out.println("gabriel "+tAct.getX()+" "+tAct.getY()+" "+tAct.getWidth()+" "+tAct.getHeight());
        tAct.setBounds(tAct.getX(),tAct.getY(),tAct.getWidth(),tAct.getHeight());
        //stage.addActor(testBut);
        //stage.act();
        System.out.println("getting listener "+img.getListeners());
        //enemyShieldBar.setSize(5,5);
/*
        playerSelBut.setPosition(250,5);
        playerSelBut.setSize(bottomButsH/playerButRatio, bottomButsH);
        stage.addActor(playerSelBut);

        enemySelBut.setPosition(1500,5);
        enemySelBut.setSize(bottomButsH/playerButRatio, bottomButsH);
        stage.addActor(enemySelBut);*/

    }

    public TextureAtlas getDoorsAt(){
        return shipObjsAt;
    }

    public void update() {
        //System.out.println("updating enemy bar");
        //enemyShieldBar.setSize(600,50);
        //enemyShieldBar.setScaleX(0.5f);
    }

    @Override
    public void dispose() {

    }

}
