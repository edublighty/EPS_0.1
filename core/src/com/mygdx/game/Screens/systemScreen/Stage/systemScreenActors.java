package com.mygdx.game.Screens.systemScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.Sprites.healthBar;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.Actors.systemControlHandle;
import com.mygdx.game.Screens.systemScreen.Actors.systemControlModule;
import com.mygdx.game.Screens.systemScreen.Actors.systemGauge;
import com.mygdx.game.Screens.systemScreen.Actors.systemHealthBars;
import com.mygdx.game.Screens.systemScreen.Actors.systemMapGalaxy;
import com.mygdx.game.Screens.systemScreen.Actors.systemMapPlanet;
import com.mygdx.game.Screens.systemScreen.Actors.systemMapSystem;
import com.mygdx.game.Screens.systemScreen.Actors.systemShieldBars;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustBar;
import com.mygdx.game.Screens.systemScreen.Actors.systemTiles;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustDown;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustLeft;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustRight;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustUp;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class  systemScreenActors implements Disposable {

    private MyGdxGame game;
    public Table table;
    public Stage stage;
    public Viewport viewport;
    public systemControlModule controlModule;
    public systemControlHandle controlHandle;
    public systemThrustUp thrustUp;
    public systemThrustDown thrustDown;
    public systemThrustLeft thrustLeft;
    public systemThrustRight thrustRight;
    private galaxyScreen screen;
    public boolean pointNotSelected;
    public int actorNumber;
    public int actorCurrent;
    public int actorDest;
    private int actorTemp;
    public float destX;
    public float destY;
    public float maxZoomX;
    public float maxZoomY;
    public systemHealthBars hBars;
    public systemShieldBars sBars;
    public systemThrustBar tBar;
    private Label thrustVar;
    private Label shieldsVar;
    private Label healthVar;
    private double o2Per;
    private Label o2Var;
    private double tempPer;
    private Label tempVar;
    private double radPer;
    private Label radVar;

    public systemScreenActors(MyGdxGame game, systemScreen2 screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);
        this.game = game;

        // ------------------------------------------------------------------------------------------------------------

        // MIDDLE RIGHT PANEL FOR ZOOM, AUTO ORBIT etc

        float margin = 0.05f;//.5f;

        String texture = "zoomsPanel";
        float tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        float tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectZooms = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float zoomsHeight = viewportHeight / 3;
        float zoomsWidth = zoomsHeight/aspectZooms;
        float zoomsWidthHidden = zoomsWidth/4;
        float zoomsX = viewportWidth - margin - zoomsWidth;
        float zoomsY = viewportHeight/2 - zoomsHeight/2 + margin*3;
        systemTiles zoomsTile = new systemTiles(screen,zoomsWidth,zoomsHeight,zoomsX,zoomsY,texture);
        stage.addActor(zoomsTile);

        texture = "zoomsPause";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectZoomsButton = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float zoomsButtonHeight = zoomsHeight/5;
        float zoomsButtonWidth = zoomsButtonHeight/aspectZoomsButton;
        float zoomsMargin = (zoomsHeight - 4*zoomsButtonHeight)/5;
        float zoomsPauseX = zoomsX + zoomsWidth/2 - zoomsButtonWidth/2;
        float zoomsPauseY = zoomsY + zoomsMargin*4 + zoomsButtonHeight*3;
        systemTiles zoomsPause = new systemTiles(screen,zoomsButtonWidth,zoomsButtonHeight,zoomsPauseX,zoomsPauseY,texture);
        zoomsPause.addListener(new ClickListener() {
           public void clicked(InputEvent event, float x, float y) {
               System.out.println("hello from pause click");
               //screen.testBool = true;
           }

           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               System.out.println("hello from pause down");
               // Switch to player
               return true;
           }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hello from pause up");
                //screen.testBool = true;
            }
        });
        stage.addActor(zoomsPause);

        texture = "zoomsPlus";
        float zoomsPlusX = zoomsPauseX;
        float zoomsPlusY = zoomsY + zoomsMargin*3 + zoomsButtonHeight*2;
        systemTiles zoomsPlus = new systemTiles(screen,zoomsButtonWidth,zoomsButtonHeight,zoomsPlusX,zoomsPlusY,texture);
        stage.addActor(zoomsPlus);

        texture = "zoomsMinus";
        float zoomsMinusX = zoomsPauseX;
        float zoomsMinusY = zoomsY + zoomsMargin*2 + zoomsButtonHeight;
        systemTiles zoomsMinus = new systemTiles(screen,zoomsButtonWidth,zoomsButtonHeight,zoomsMinusX,zoomsMinusY,texture);
        stage.addActor(zoomsMinus);

        texture = "zoomsAutoOrbit";
        float zoomsAutoX = zoomsPauseX;
        float zoomsAutoY = zoomsY + zoomsMargin;
        systemTiles zoomsAuto = new systemTiles(screen,zoomsButtonWidth,zoomsButtonHeight,zoomsAutoX,zoomsAutoY,texture);
        stage.addActor(zoomsAuto);

        // ----------------------------------------------------------------------------------------------------------------------------------
/*

        // SHIELD AND HEALTH BARS

        texture = "zoomsPanel";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectHS = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float HS_Height = viewportHeight / 10;
        float HS_Width = viewportWidth;
        float HS_X = 0;
        float HS_Y = viewportHeight - HS_Height;
        systemTiles HS_Tile = new systemTiles(screen,HS_Width,HS_Height,HS_X,HS_Y,texture);
        //stage.addActor(HS_Tile);

        int healthPer = 100;
        int shieldPer = 100;
        String healthPerS = " "+healthPer;
        String shieldPerS = " "+shieldPer;

        Label healthLabel = new Label("HEALTH: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label healthVar = new Label(healthPerS+"%",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        float healthXLab = HS_X + healthLabel.getWidth();
        float healthXVar = healthXLab + healthLabel.getWidth();
        float healthY = viewportHeight - healthLabel.getHeight();
        healthLabel.setX(healthXLab);
        healthLabel.setY(healthY);
        stage.addActor(healthLabel);
        healthVar.setX(healthXVar);
        healthVar.setY(healthY);
        stage.addActor(healthVar);

        Label shieldLabel = new Label("SHIELDS: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label shieldVar = new Label(shieldPerS+"%",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        float shieldXLab = healthXLab + healthLabel.getWidth()/2 - shieldLabel.getWidth()/2;
        float shieldXVar = shieldXLab + shieldLabel.getWidth();
        float shieldY = healthY - shieldLabel.getHeight();
        shieldLabel.setX(shieldXLab);
        shieldLabel.setY(shieldY);
        stage.addActor(shieldLabel);
        shieldVar.setX(shieldXVar);
        shieldVar.setY(shieldY);
        stage.addActor(shieldVar);

        texture = "healthBar";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectH = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float H_Height = healthLabel.getHeight();
        float H_Width = viewportWidth/3;        // say 1/3 corresponds to 100% health
        float H_X = healthVar.getX() + healthVar.getWidth();
        float H_Y = viewportHeight - H_Height;
        systemTiles healthBar = new systemTiles(screen,H_Width,H_Height,H_X,H_Y,texture);

        texture = "shieldBar";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectS = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float S_Height = shieldLabel.getHeight();
        float S_Width = healthBar.getWidth();        // say 1/3 corresponds to 100% health
        float S_X = shieldVar.getX() + shieldVar.getWidth();
        float S_Y = healthBar.getY() - S_Height;
        systemTiles shieldBar = new systemTiles(screen,S_Width,S_Height,S_X,S_Y,texture);

        if(shieldBar.getX()>healthBar.getX()){
            healthBar.setX(shieldBar.getX());
        } else {
            shieldBar.setX(healthBar.getX());
        }

        texture = "healthBack";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        aspectH = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float Hb_Height = H_Height;
        float Hb_Width = H_Width;;        // say 1/3 corresponds to 100% health
        float Hb_X = H_X;
        float Hb_Y = H_Y;
        systemTiles healthBack = new systemTiles(screen,Hb_Width,Hb_Height,Hb_X,Hb_Y,texture);

        stage.addActor(healthBack);
        healthBar.setColor(healthBar.getColor().r,healthBar.getColor().g,healthBar.getColor().b,0.5f);
        stage.addActor(healthBar);
        stage.addActor(shieldBar);

*/

        // ----------------------------------------------------------------------------------------------------------------------------------

        // MIDDLE LEFT PANEL FOR O2, RAD, TEMP STATS

        texture = "zoomsPanel";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectStats = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float statsHeight = viewportHeight / 5;
        float statsWidth = zoomsHeight/aspectZooms;
        float statsX = margin;
        float statsY = viewportHeight/2;
        systemTiles statsTile = new systemTiles(screen,statsWidth,statsHeight,statsX,statsY,texture);
        stage.addActor(statsTile);
/*

        int o2Per = 100;
        int tempPer = 60;
        int radPer = 3;
        String o2PerS = " "+String.valueOf(o2Per);
        String tempPerS = " "+String.valueOf(tempPer);
        String radPerS = " "+String.valueOf(radPer);

        BitmapFont font = new BitmapFont();
        //font.getData().setScale(1f/15);
        int scale = 70;
        float xScale = 15f;
        float yScale = 3f;
        //font.getData().setScale(xScale/scale,yScale/scale);
        //font.getData().se

        Label o2Label = new Label("02: ", new Label.LabelStyle(font, Color.BLUE));
        Label tempLabel = new Label("TEMP: ", new Label.LabelStyle(new BitmapFont(), Color.RED));
        Label radLabel = new Label("RAD: ", new Label.LabelStyle(new BitmapFont(), Color.YELLOW));
        Label o2Var = new Label(o2PerS+"%",new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        Label tempVar = new Label(tempPerS+"*C",new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        Label radVar = new Label(radPerS+" rad/s",new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float statsStatsHeight = o2Label.getHeight();
        float statsMargin = (statsHeight - 3*statsStatsHeight)/4;

        float statso2XLab = statsX + statsWidth/2 - o2Label.getWidth();
        float statso2XVar = statso2XLab + o2Label.getWidth();
        float statso2Y = statsY + statsMargin*3 + statsStatsHeight*2;
        o2Label.setX(statso2XLab);
        o2Label.setY(statso2Y);
        stage.addActor(o2Label);
        o2Var.setX(statso2XVar);
        o2Var.setY(statso2Y);
        stage.addActor(o2Var);

        float statsTempXLab = statsX + statsWidth/2 - tempLabel.getWidth();
        float statsTempXVar = statsTempXLab + tempLabel.getWidth();
        float statsTempY = statsY + statsMargin*2 + statsStatsHeight;
        tempLabel.setX(statsTempXLab);
        tempLabel.setY(statsTempY);
        stage.addActor(tempLabel);
        tempVar.setX(statsTempXVar);
        tempVar.setY(statsTempY);
        stage.addActor(tempVar);

        float statsRadXLab = statsX + statsWidth/2 - radLabel.getWidth();
        float statsRadXVar = statsRadXLab + radLabel.getWidth();
        float statsRadY = statsY + statsMargin;
        radLabel.setX(statsRadXLab);
        radLabel.setY(statsRadY);
        stage.addActor(radLabel);
        radVar.setX(statsRadXVar);
        radVar.setY(statsRadY);
        stage.addActor(radVar);
*/

        // ----------------------------------------------------------------------------------------------------------------------------------

        // bottom left panel for maps

        texture = "MAPSTILE";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectMaps = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float mapHeight = viewportHeight / 3;
        float mapWidth = mapHeight/aspectMaps;
        float mapsX = margin;
        float mapsY = margin;
        systemTiles mapsTile = new systemTiles(screen,mapWidth,mapHeight,mapsX,mapsY,texture);
        stage.addActor(mapsTile);

        tempA = screen.getThrustAt().findRegion("GALBUTTON").originalHeight;
        tempB = screen.getThrustAt().findRegion("GALBUTTON").originalWidth;
        float aspectGal = tempA / tempB;//screen.getThrustAt().findRegion("GALBUTTON").originalHeight / screen.getThrustAt().findRegion("GALBUTTON").originalWidth;
        float galWidth = mapWidth*0.95f;
        float galHeight = galWidth*aspectGal;
        float galX = mapsX + mapWidth/2 - galWidth/2;
        float galY = mapsY + galHeight*2 + margin*3;
        systemMapGalaxy galButton = new systemMapGalaxy(screen,galWidth,galHeight,galX,galY);
        stage.addActor(galButton);

        tempA = screen.getThrustAt().findRegion("SYSTEMBUTTON").originalHeight;
        tempB = screen.getThrustAt().findRegion("SYSTEMBUTTON").originalWidth;
        float aspectSyst = tempA / tempB;//screen.getThrustAt().findRegion("SYSTEMBUTTON").originalHeight / screen.getThrustAt().findRegion("SYSTEMBUTTON").originalWidth;
        float systWidth = galWidth;
        float systHeight = galHeight;
        float systX = galX;
        float systY = mapsY + systHeight + margin*2;
        systemMapSystem systButton = new systemMapSystem(screen,systWidth,systHeight,systX,systY);
        stage.addActor(systButton);

        tempA = screen.getThrustAt().findRegion("PLANETBUTTON").originalHeight;
        tempB = screen.getThrustAt().findRegion("PLANETBUTTON").originalWidth;
        float aspectPlanet = tempA / tempB;//screen.getThrustAt().findRegion("PLANETBUTTON").originalHeight / screen.getThrustAt().findRegion("PLANETBUTTON").originalWidth;
        float planetWidth = galWidth;
        float planetHeight = galHeight;
        float planetX = galX;
        float planetY = mapsY + margin;
        systemMapPlanet planetButton = new systemMapPlanet(screen,planetWidth,planetHeight,planetX,planetY);
        stage.addActor(planetButton);

        // ----------------------------------------------
        // Bottom right thrust panel



        float aspectCM = screen.getThrustAt().findRegion("playerGauge2").originalHeight / screen.getThrustAt().findRegion("playerGauge2").originalWidth;
        float CMheight = mapHeight;
        float CMwidth = CMheight / aspectCM;
        float thrustHeight = CMheight/6;
        float thrustWidth = CMwidth/6;
        float CMx = viewportWidth - CMwidth;
        float CMy = margin;

        hBars = new systemHealthBars(screen, CMwidth, CMheight, CMx, CMy);
        stage.addActor(hBars);

        sBars = new systemShieldBars(screen, CMwidth, CMheight, CMx, CMy);
        stage.addActor(sBars);

        systemGauge gauge = new systemGauge(screen, CMwidth, CMheight, CMx, CMy);
        stage.addActor(gauge);

        tBar = new systemThrustBar(screen, CMwidth, CMheight, CMx, CMy);
        tBar.setOrigin(tBar.getWidth()/2,tBar.getHeight()/2);
        stage.addActor(tBar);

        o2Per = 100;
        tempPer = 60;
        radPer = 3;
        int thrustPer = 0;
        int shieldsPer = 100;
        int healthPer = 100;
        String o2PerS = " "+String.valueOf(o2Per);
        String tempPerS = " "+String.valueOf(tempPer);
        String radPerS = " "+String.valueOf(radPer);
        String thrustPerS = " "+String.valueOf(thrustPer);
        String shieldsPerS = " "+String.valueOf(shieldsPer);
        String healthPerS = " "+String.valueOf(healthPer);

        BitmapFont font = new BitmapFont();
        //font.getData().setScale(1f/15);
        float scale = (30/game.V_WIDTH)*1f/23;
        float xScale = 15f;
        float yScale = 3f;
        //font.getData().setScale(xScale/scale,yScale/scale);
        font.getData().setScale(scale);

        Label o2Label = new Label("02: ", new Label.LabelStyle(font, Color.BLUE));
        Label tempLabel = new Label("TEMP: ", new Label.LabelStyle(font, Color.RED));
        Label radLabel = new Label("RAD: ", new Label.LabelStyle(font, Color.YELLOW));

        o2Var = new Label(o2PerS+"%",new Label.LabelStyle(font, Color.BLUE));
        tempVar = new Label(tempPerS+"*C",new Label.LabelStyle(font, Color.RED));
        radVar = new Label(radPerS+" rad/s",new Label.LabelStyle(font, Color.YELLOW));
        thrustVar = new Label(thrustPerS+"%",new Label.LabelStyle(font, Color.ORANGE));
        shieldsVar = new Label(shieldsPerS+"%",new Label.LabelStyle(font, Color.BLUE));
        healthVar = new Label(healthPerS+"%",new Label.LabelStyle(font, Color.GREEN));

        float statsStatsHeight = o2Label.getHeight();
        float statsMargin = (statsHeight - 3*statsStatsHeight)/4;

        float statso2XLab = CMx + gauge.getWidth()/8;// + statsWidth/2 - o2Label.getWidth();
        float statso2XVar = statso2XLab + o2Label.getWidth();
        float statso2Y = CMy + gauge.getHeight()/2 + statsStatsHeight + statsMargin;
        o2Label.setX(statso2XLab);
        o2Label.setY(statso2Y);
        stage.addActor(o2Label);
        o2Var.setX(statso2XVar);
        o2Var.setY(statso2Y);
        stage.addActor(o2Var);

        float statsTempXLab = CMx + gauge.getWidth()/8;// + statsWidth/2 - tempLabel.getWidth();
        float statsTempXVar = statsTempXLab + tempLabel.getWidth();
        float statsTempY = CMy + gauge.getHeight()/2;
        tempLabel.setX(statsTempXLab);
        tempLabel.setY(statsTempY);
        stage.addActor(tempLabel);
        tempVar.setX(statsTempXVar);
        tempVar.setY(statsTempY);
        stage.addActor(tempVar);

        float statsRadXLab = CMx + gauge.getWidth()/8 + statsWidth/2 - radLabel.getWidth();
        float statsRadXVar = statsRadXLab + radLabel.getWidth();
        float statsRadY = CMy + gauge.getHeight()/2 - statsStatsHeight - statsMargin;
        radLabel.setX(statsRadXLab);
        radLabel.setY(statsRadY);
        stage.addActor(radLabel);
        radVar.setX(statsRadXVar);
        radVar.setY(statsRadY);
        stage.addActor(radVar);

        float statsThrustXVar = CMx + gauge.getWidth()*0f;
        float statsThrustY = CMy + gauge.getHeight()*0.9f;
        thrustVar.setWidth(gauge.getWidth()*1f);
        thrustVar.setX(statsThrustXVar);
        thrustVar.setY(statsThrustY);
        stage.addActor(thrustVar);

        float statsShieldsXVar = CMx + gauge.getWidth()*0.85f;
        float statsShieldsY = CMy + gauge.getHeight()*0.9f;
        shieldsVar.setX(statsShieldsXVar);
        shieldsVar.setY(statsShieldsY);
        stage.addActor(shieldsVar);

        float statsHealthXVar = CMx + gauge.getWidth()*0.65f;
        float statsHealthY = CMy + gauge.getHeight()*0.4f;
        healthVar.setX(statsHealthXVar);
        healthVar.setY(statsHealthY);
        stage.addActor(healthVar);

/*

        float CMheight = mapHeight;
        float CMwidth = CMheight / aspectCM;
        float thrustHeight = CMheight/6;
        float thrustWidth = CMwidth/6;
        float CMx = viewportWidth - CMwidth;
        float CMy = margin;
        controlModule = new systemControlModule(screen, CMwidth, CMheight, CMx, CMy);
        //stage.addActor(controlModule);

        float handleWidth = CMwidth;
        float handleHeight = CMheight;
        float handleX = CMx;
        float handleY = CMy;
        controlHandle = new systemControlHandle(screen, handleWidth, handleHeight, handleX, handleY);
        //stage.addActor(controlHandle);

        float thrustUpX = CMx + CMwidth/2 - thrustWidth/2;// - CMwidth/2 + thrustWidth/2;
        float thrustUpY = CMy + margin;
        thrustUp = new systemThrustUp(screen, thrustWidth, thrustHeight, thrustUpX, thrustUpY);
        //stage.addActor(thrustUp);

        float thrustDownX = thrustUpX;
        float thrustDownY = CMy + CMheight - thrustHeight - margin;
        thrustDown = new systemThrustDown(screen, thrustWidth, thrustHeight, thrustDownX, thrustDownY);
        //stage.addActor(thrustDown);

        float thrustLeftX = CMx + margin;
        float thrustLeftY = CMy + CMheight/2 - thrustHeight/2;
        thrustLeft = new systemThrustLeft(screen, thrustWidth, thrustHeight, thrustLeftX, thrustLeftY);
        //stage.addActor(thrustLeft);

        float thrustRightX = CMx + CMwidth - thrustHeight - margin;
        float thrustRightY = thrustLeftY;
        thrustRight = new systemThrustRight(screen, thrustWidth, thrustHeight, thrustRightX, thrustRightY);
        //stage.addActor(thrustRight);
*/

        //stage.setDebugAll(true);
    }

    public void updateThrust(int thrustPer){
        thrustVar.setText(thrustPer+"%");
    }

    public void updateShields(int shieldPer){
        shieldsVar.setText(shieldPer+"%");
    }

    public void updateHealth(int healthPer){
        healthVar.setText(healthPer+"%");
    }

    public void updateTemp(double newTemp){
        tempPer = newTemp;
        int tempTemp = (int) (tempPer);
        tempVar.setText(tempTemp+"*C");
    }
    public double getTempPer(){return tempPer;}

    public void updateRad(double newRad){
        radPer = newRad;
        int tempRad = (int) (radPer);
        radVar.setText(tempRad+" rad/s");
    }
    public double getRadPer(){
        return radPer;
    }

    public void update02(double new02){
        o2Per = new02;
        int tempO2 = (int) (o2Per);
        o2Var.setText(tempO2+"%");
    }
    public double getO2Per(){
        return o2Per;
    }

    /*public void updateHandle(){
        controlHandle.updateMarker();
    }*/

    @Override
    public void dispose() {

    }
}
