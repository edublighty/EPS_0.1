package com.mygdx.game.Screens.systemScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.galaxyScreen;
import com.mygdx.game.Screens.systemScreen.Actors.systemControlHandle;
import com.mygdx.game.Screens.systemScreen.Actors.systemControlModule;
import com.mygdx.game.Screens.systemScreen.Actors.systemGauge;
import com.mygdx.game.Screens.systemScreen.Actors.systemHealthBars;
import com.mygdx.game.Screens.systemScreen.Actors.systemShieldBars;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustBar;
import com.mygdx.game.Screens.systemScreen.Actors.systemTiles;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustDown;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustLeft;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustRight;
import com.mygdx.game.Screens.systemScreen.Actors.systemThrustUp;
import com.mygdx.game.Screens.systemScreen.Sprites.alarmButtons;
import com.mygdx.game.Screens.systemScreen.Sprites.shipRoomButton;
import com.mygdx.game.Screens.systemScreen.Sprites.shipRoomSprite;
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
    private Group viewPanel;
    private boolean viewShowing;
    private Group actionPanel;
    private boolean actionShowing;
    private Group screenPanel;
    private boolean screenShowing;
    private Group orbitPanel;
    private boolean orbitShowing;
    private boolean paneUp;
    private float paneShowing;
    private float paneHidden;
    private Label mouseLabel;
    private shipRoomSprite[] playerRoomsSystems;
    private shipRoomButton[] playerRoomsSystemsEdge;
    private shipRoomButton[] playerRoomsSystemsName;
    private shipRoomButton[] playerRoomsSystemsDamage;
    private Group shipDetailPanel;

    public systemScreenActors(MyGdxGame game, systemScreen2 screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);
        this.game = game;

        // ----------------------------------------------------------------------------------------------------------------------------------

        // Tabs for menu / action navigation
        float totalWidth = viewportWidth;
        float paneHeight = viewportWidth / 16;
        paneShowing = 0 + paneHeight/2;
        paneHidden = 0 - paneHeight/2;
        paneUp = false;
        // we will have four tabs, assuming all horizontal along bottom
        float tabWidth = totalWidth / 16;
        float tabHeight = tabWidth / 4;
        float tabMargin = tabWidth / 100;

        // Font preparation
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/data-latin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = (int) Math.ceil(paneHeight/5);
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = fontParameter.size/5;
        BitmapFont font12 = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        fontGenerator.dispose(); // don't forget to dispose to avoid memory leaks!

        mouseLabel = new Label("TESTING SON", new Label.LabelStyle(font12,font12.getColor()));
        mouseLabel.setX(viewportWidth/2);
        mouseLabel.setY(viewportHeight/2);
        stage.addActor(mouseLabel);

        // View panel
        viewPanel = new Group();//Stage(new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera()),sb);
        float viewPaneX = 0;
        float viewPaneY = paneHidden;
        systemTiles viewPane = new systemTiles(screen, totalWidth, paneHeight, viewPaneX, viewPaneY,"bottomUI");
        viewPanel.addActor(viewPane);
        float viewTabX = totalWidth/20;
        float viewTabY = viewPaneY + paneHeight;
        systemTiles viewTab = new systemTiles(screen,tabWidth,tabHeight,viewTabX,viewTabY,"titleUI");
        viewShowing = false;
        viewTab.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("hello from view tab click");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hello from view tab down");
                // Switch to player
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hello from view tab up");
                toggleViewPane();
            }
        });
        viewPanel.addActor(viewTab);
        viewPanel.setY(paneHidden);
        stage.addActor(viewPanel);
        float buttonHeight = paneHeight*2/3;
        float buttonMargin = totalWidth/20;
        float buttonX = viewPaneX + buttonMargin;
        float buttonY = viewPaneY + paneHeight/2 - buttonHeight/2;
        systemTiles zoomToShip = new systemTiles(screen,buttonHeight,buttonHeight,buttonX,buttonY,"zoomToShip");
        viewPanel.addActor(zoomToShip);
        zoomToShip.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ignore if mouse was already over
                if(isOver()) return;

                super.enter(event, x, y, pointer, fromActor);

                // Ignore if mouse is not over
                if(!isOver()) return;

                toggleTextOverlay(true,zoomToShip.getX()+zoomToShip.getWidth()/2-mouseLabel.getWidth()/2,paneHeight/2 + zoomToShip.getHeight()*0.6f,"Zoom to ship");

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                super.exit(event, x, y, pointer, fromActor);

                toggleTextOverlay(false,0,0,null);

            }
        });
        buttonX += buttonHeight + buttonMargin;
        systemTiles zoomToSystem = new systemTiles(screen,buttonHeight,buttonHeight,buttonX,buttonY,"zoomToSystem");
        zoomToSystem.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ignore if mouse was already over
                if(isOver()) return;

                super.enter(event, x, y, pointer, fromActor);

                // Ignore if mouse is not over
                if(!isOver()) return;

                toggleTextOverlay(true,zoomToSystem.getX()+zoomToSystem.getWidth()/2-mouseLabel.getWidth()/2,paneHeight/2 + zoomToSystem.getHeight()*0.6f,"Zoom to system");

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                super.exit(event, x, y, pointer, fromActor);

                toggleTextOverlay(false,0,0,null);

            }
        });
        viewPanel.addActor(zoomToSystem);

        // Action panel
        actionPanel = new Group();//Stage(new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera()),sb);
        float actionPaneX = 0;
        float actionPaneY = paneHidden;
        systemTiles actionPane = new systemTiles(screen, totalWidth, paneHeight, actionPaneX, actionPaneY,"bottomUI");
        actionPanel.addActor(actionPane);
        float actionTabX = viewTabX + tabWidth + tabMargin;
        float actionTabY = actionPaneY + paneHeight;
        systemTiles actionTab = new systemTiles(screen,tabWidth,tabHeight,actionTabX,actionTabY,"titleUI");
        actionShowing = false;
        actionTab.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("hello from action tab click");
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hello from action tab down");
                // Switch to player
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hello from action tab up");
                toggleActionPane();
            }
        });
        actionPanel.addActor(actionTab);
        actionPanel.setY(paneHidden);
        systemTiles allStop = new systemTiles(screen,buttonHeight,buttonHeight,buttonX,buttonY,"zoomToSystem");
        allStop.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.toggleAllStop();
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ignore if mouse was already over
                if(isOver()) return;

                super.enter(event, x, y, pointer, fromActor);

                // Ignore if mouse is not over
                if(!isOver()) return;

                toggleTextOverlay(true,allStop.getX()+allStop.getWidth()/2-mouseLabel.getWidth()/2,paneHeight/2 + allStop.getHeight()*0.6f,"All stop");

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                super.exit(event, x, y, pointer, fromActor);

                toggleTextOverlay(false,0,0,null);

            }
        });
        actionPanel.addActor(allStop);
        stage.addActor(actionPanel);

        // Screen panel
        screenPanel = new Group();//Stage(new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera()),sb);
        float screenPaneX = 0;
        float screenPaneY = paneHidden;
        systemTiles screenPane = new systemTiles(screen, totalWidth, paneHeight, screenPaneX, screenPaneY,"bottomUI");
        screenPanel.addActor(screenPane);
        float screenTabX = actionTabX + tabWidth + tabMargin;
        float screenTabY = screenPaneY + paneHeight;
        systemTiles screenTab = new systemTiles(screen,tabWidth,tabHeight,screenTabX,screenTabY,"titleUI");
        screenShowing = false;
        screenTab.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Switch to player
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                toggleScreenPane();
            }
        });
        screenPanel.addActor(screenTab);
        screenPanel.setY(paneHidden);
        stage.addActor(screenPanel);
        buttonHeight = paneHeight*2/3;
        buttonX = viewPaneX + buttonMargin;
        buttonY = viewPaneY + paneHeight/2 - buttonHeight/2;
        systemTiles galaxyMenu = new systemTiles(screen,buttonHeight,buttonHeight,buttonX,buttonY,"galaxyMenu");
        galaxyMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new galaxyScreen(game));
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ignore if mouse was already over
                if(isOver()) return;

                super.enter(event, x, y, pointer, fromActor);

                // Ignore if mouse is not over
                if(!isOver()) return;

                toggleTextOverlay(true,x+mouseLabel.getWidth()*0.6f,y+mouseLabel.getHeight()*0.6f,"Go to galaxy menu");

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                super.exit(event, x, y, pointer, fromActor);

                toggleTextOverlay(false,0,0,null);

            }
        });
        screenPanel.addActor(galaxyMenu);

        // Orbit panel
        orbitPanel = new Group();//Stage(new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera()),sb);
        float orbitPaneX = 0;
        float orbitPaneY = paneHidden;
        systemTiles orbitPane = new systemTiles(screen, totalWidth, paneHeight, orbitPaneX, orbitPaneY,"bottomUI");
        orbitPanel.addActor(orbitPane);
        float orbitTabX = screenTabX + tabWidth + tabMargin;
        float orbitTabY = orbitPaneY + paneHeight;
        systemTiles orbitTab = new systemTiles(screen,tabWidth,tabHeight,orbitTabX,orbitTabY,"titleUI");
        orbitShowing = false;
        orbitTab.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                toggleOrbitPane();
            }
        });
        orbitPanel.addActor(orbitTab);
        orbitPanel.setY(paneHidden);
        stage.addActor(orbitPanel);
        buttonHeight = paneHeight*2/3;
        buttonX = viewPaneX + buttonMargin;
        buttonY = viewPaneY + paneHeight/2 - buttonHeight/2;
        systemTiles orbitStar = new systemTiles(screen,buttonHeight,buttonHeight,buttonX,buttonY,"zoomsAutoOrbit");
        orbitStar.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                screen.orbitStar();
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ignore if mouse was already over
                if(isOver()) return;

                super.enter(event, x, y, pointer, fromActor);

                // Ignore if mouse is not over
                if(!isOver()) return;

                toggleTextOverlay(true,orbitStar.getX()+orbitStar.getWidth()/2-mouseLabel.getWidth()/2,paneHeight/2 + orbitStar.getHeight()*0.6f,"Orbit star");

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                super.exit(event, x, y, pointer, fromActor);

                toggleTextOverlay(false,0,0,null);

            }
        });
        orbitPanel.addActor(orbitStar);
        buttonX += buttonHeight + buttonMargin;
        systemTiles orbitPlanet = new systemTiles(screen,buttonHeight,buttonHeight,buttonX,buttonY,"orbitPlanet");
        orbitPlanet.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event,x,y);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Ignore if mouse was already over
                if(isOver()) return;

                super.enter(event, x, y, pointer, fromActor);

                // Ignore if mouse is not over
                if(!isOver()) return;

                toggleTextOverlay(true,orbitPlanet.getX()+orbitPlanet.getWidth()/2-mouseLabel.getWidth()/2,paneHeight/2 + orbitPlanet.getHeight()*0.6f,"Orbit nearest planet");

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                super.exit(event, x, y, pointer, fromActor);

                toggleTextOverlay(false,0,0,null);

            }
        });
        orbitPanel.addActor(orbitPlanet);

        // ----------------------------------------------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------------------------------------------
        // Top right system gauge

        float aspectCM = screen.getThrustAt().findRegion("playerGauge2").originalHeight / screen.getThrustAt().findRegion("playerGauge2").originalWidth;
        float CMheight = viewportHeight / 3;
        float CMwidth = CMheight / aspectCM;
        float thrustHeight = CMheight/6;
        float thrustWidth = CMwidth/6;
        float CMx = viewportWidth - CMwidth;
        float CMy = viewportHeight - CMheight;//margin;

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
        String o2PerS = ""+String.valueOf(o2Per);
        String tempPerS = ""+String.valueOf(tempPer);
        String radPerS = ""+String.valueOf(radPer);
        String thrustPerS = ""+String.valueOf(thrustPer);
        String shieldsPerS = ""+String.valueOf(shieldsPer);
        String healthPerS = ""+String.valueOf(healthPer);
        int fontSizeDiv = 20;

        // Font preparation
        int borderDiv = 20;
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/data-latin.ttf"));
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = (int) Math.ceil(CMheight/fontSizeDiv);
        fontParameter.borderColor = Color.YELLOW;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontO2 = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        fontParameter.size = (int) Math.ceil(CMheight/fontSizeDiv);
        fontParameter.borderColor = Color.ORANGE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontTemp = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        fontParameter.size = (int) Math.ceil(CMheight/fontSizeDiv);
        fontParameter.borderColor = Color.CHARTREUSE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontRad = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        fontParameter.size = (int) Math.ceil(CMheight/fontSizeDiv);
        fontParameter.borderColor = Color.SCARLET;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontThrust = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        fontParameter.size = (int) Math.ceil(CMheight/fontSizeDiv);
        fontParameter.borderColor = Color.CYAN;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontShields = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        fontParameter.size = (int) Math.ceil(CMheight/fontSizeDiv);
        fontParameter.color = Color.GREEN;
        fontParameter.borderColor = Color.GREEN;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontHealth = fontGenerator.generateFont(fontParameter); // font size 12 pixels

        Label o2Label = new Label("02: ", new Label.LabelStyle(fontO2, fontO2.getColor()));
        Label tempLabel = new Label("TEMP: ", new Label.LabelStyle(fontTemp,fontTemp.getColor()));
        Label radLabel = new Label("RAD: ", new Label.LabelStyle(fontRad,fontRad.getColor()));

        o2Var = new Label(o2PerS+"%",new Label.LabelStyle(fontO2, fontO2.getColor()));
        tempVar = new Label(tempPerS+"*C",new Label.LabelStyle(fontTemp,fontTemp.getColor()));
        radVar = new Label(radPerS+" rad/s",new Label.LabelStyle(fontRad,fontRad.getColor()));
        thrustVar = new Label(thrustPerS+"%",new Label.LabelStyle(fontThrust,fontThrust.getColor()));
        shieldsVar = new Label(shieldsPerS+"%",new Label.LabelStyle(fontShields,fontShields.getColor()));
        healthVar = new Label(healthPerS+"%",new Label.LabelStyle(fontHealth,fontHealth.getColor()));

        float statsStatsHeight = o2Label.getHeight();
        //float statsMargin = (viewportHeight / 5 - 3*statsStatsHeight)/4;

        float statsTempXLab = CMx + gauge.getWidth()/10;// + statsWidth/2 - tempLabel.getWidth();
        tempLabel.setX(statsTempXLab);
        float statsTempXVar = tempLabel.getX() + tempLabel.getWidth() + tempLabel.getHeight();
        float statsTempY = CMy + gauge.getHeight()/2 - tempLabel.getHeight()/2;
        tempLabel.setY(statsTempY);
        stage.addActor(tempLabel);
        tempVar.setX(statsTempXVar);
        tempVar.setY(statsTempY);
        stage.addActor(tempVar);

        float statso2XLab = tempLabel.getX() + tempLabel.getWidth() - o2Label.getWidth();// + statsWidth/2 - o2Label.getWidth();
        float statso2XVar = statsTempXVar;
        float statso2Y = tempLabel.getY() + tempLabel.getHeight() + o2Label.getHeight();
        o2Label.setX(statso2XLab);
        o2Label.setY(statso2Y);
        stage.addActor(o2Label);
        o2Var.setX(statso2XVar);
        o2Var.setY(statso2Y);
        stage.addActor(o2Var);

        float statsRadXLab = tempLabel.getX() + tempLabel.getWidth() - radLabel.getWidth();
        float statsRadXVar = statsTempXVar;
        float statsRadY = tempLabel.getY() - radLabel.getHeight()*2;
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
        float statsHealthY = CMy + gauge.getHeight()/2 - healthVar.getHeight()/2;
        healthVar.setX(statsHealthXVar);
        healthVar.setY(statsHealthY);
        stage.addActor(healthVar);

        // ----------------------------------------------------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------------------------------------------------
        // SHIP DETAIL PANELS

        // group containing all ship info panels
        shipDetailPanel = new Group();

        // //////////////////
        // SHIP SYSTEMS PANEL

        float systemsTabHeight = viewportHeight*3f/8;   // left panel heights
        float scrollerWidth = systemsTabHeight*4/3;     // panel widths - this wont be sustainable for multiple screen sizes
        float scrollerMargins = (viewportHeight - systemsTabHeight*2)/3;    // margins used for panels
        fontParameter.size = (int) Math.ceil(CMheight/15);  // arbitrary font size
        fontParameter.color = Color.BLACK;
        fontParameter.borderColor = Color.YELLOW;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        BitmapFont fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels

        playerRoomsSystems = screen.playerShipShown.getSystems();
        playerRoomsSystemsEdge = screen.playerShipShown.getSystemSwitches();
        playerRoomsSystemsName = screen.playerShipShown.getSystemLabels();
        playerRoomsSystemsDamage = screen.playerShipShown.getSystemDamageBars();

        Label temp = new Label("SURVEILLANCE 0000000", new Label.LabelStyle(fontTest, fontTest.getColor()));
        float asp1 = temp.getWidth() / temp.getHeight();
        float asp2 = playerRoomsSystemsEdge[0].getAspect();
        float asp3 = playerRoomsSystemsDamage[0].getAspect();
        float commH = scrollerWidth/(asp1+asp2+asp3);   // common height used for text, button and damage bar for systems

        temp = new Label("SHIP INVENTORY", new Label.LabelStyle(fontTest, fontTest.getColor()));
        asp1 = temp.getWidth() / temp.getHeight();
        float commH_PanTitles = (scrollerWidth/(asp1)*3/4);   // common height used for text, button and damage bar for systems

        fontParameter.size = (int) Math.ceil(commH);
        fontParameter.color = Color.BLACK;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels

        Table scrollTable = new Table();
        temp = new Label("SYSTEM", new Label.LabelStyle(fontTest, fontTest.getColor()));
        scrollTable.add(temp);
        temp = new Label("TOGGLE", new Label.LabelStyle(fontTest, fontTest.getColor()));
        scrollTable.add(temp);
        temp = new Label("DAMAGE", new Label.LabelStyle(fontTest, fontTest.getColor()));
        scrollTable.add(temp);
        scrollTable.row();

        fontParameter.size = (int) Math.ceil(commH);
        fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels

        for(int i = 0; i < playerRoomsSystems.length; i++){
            if (playerRoomsSystems[i] == null) {
                // nothign to draw
            } else {
                System.out.println("printing label "+playerRoomsSystems[i].getRoomType());
                temp = new Label(playerRoomsSystems[i].getRoomType(), new Label.LabelStyle(fontTest, fontTest.getColor()));
                scrollTable.add(temp);//.width(Value.percentWidth(.65f,scrollTable));
                //playerRoomsSystemsEdge[i].setWidth(temp.getWidth());
                scrollTable.add(playerRoomsSystemsEdge[i]).height(commH).width(asp2*commH);//.width(Value.percentWidth(.15f,scrollTable));
                //playerRoomsSystemsDamage[i].setHeight(commH).s;
                //playerRoomsSystemsDamage[i].setHeight(temp.getHeight());
                scrollTable.add(playerRoomsSystemsDamage[i]).height(commH).width(asp3*commH);
                scrollTable.row();
            }
        }
        scrollTable.pack();

        WidgetGroup SYSTEMSgroup = new WidgetGroup();
        SYSTEMSgroup.setWidth(scrollerWidth);
        SYSTEMSgroup.setHeight(systemsTabHeight);
        SYSTEMSgroup.setX(scrollerMargins);
        SYSTEMSgroup.setY(viewportHeight/2 + scrollerMargins/2);
        Image backdrop = new Image();
        backdrop.setDrawable(new TextureRegionDrawable(game.getTilesAt().findRegion("pDTTile50Grey")));
        backdrop.setHeight(SYSTEMSgroup.getHeight());
        backdrop.setWidth(SYSTEMSgroup.getWidth());
        backdrop.setX(0);
        backdrop.setY(0);
        backdrop.setColor(backdrop.getColor().r,backdrop.getColor().g,backdrop.getColor().b,backdrop.getColor().a/2);
        SYSTEMSgroup.addActor(backdrop);

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        ScrollPane scroller = new ScrollPane(scrollTable,skin);
        fontParameter.size = (int) Math.ceil(commH_PanTitles);
        fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        temp = new Label("SHIP CONTROLS", new Label.LabelStyle(fontTest, fontTest.getColor()));
        temp.setHeight(commH_PanTitles);
        temp.setPosition(SYSTEMSgroup.getWidth()/2 - temp.getWidth()/2, SYSTEMSgroup.getHeight() - temp.getHeight());
        SYSTEMSgroup.addActor(temp);
        scroller.setWidth(SYSTEMSgroup.getWidth());
        scroller.setHeight(SYSTEMSgroup.getHeight() - temp.getHeight());
        scroller.setX(SYSTEMSgroup.getWidth()/2 - scroller.getWidth()/2);
        scroller.setY(temp.getY() - scroller.getHeight());
        SYSTEMSgroup.addActor(scroller);

        shipDetailPanel.addActor(SYSTEMSgroup);

        // ///////////////////////////////////////////////////////////
        // ALARMS AND SHIP INFO PANEL

        Table scrollTableINFO = new Table();

        fontParameter.size = (int) Math.ceil(commH);
        fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels

        alarmButtons acceptAll = new alarmButtons(game,"acceptAll",0);
        alarmButtons clearAll = new alarmButtons(game,"clearAll",0);
        alarmButtons acceptAlarm = new alarmButtons(game,"acceptAlarm",0);
        alarmButtons clearAlarm = new alarmButtons(game,"clearAlarm",0);

        temp = new Label("SURVEILLANCE SYSTEM DOWN", new Label.LabelStyle(fontTest, fontTest.getColor()));
        asp1 = temp.getWidth() / temp.getHeight();
        asp2 = acceptAlarm.getAspect();
        asp3 = acceptAlarm.getAspect();
        scrollerWidth = systemsTabHeight*4/3;
        commH = scrollerWidth/(asp1+asp2+asp3);

        for(int i = 0; i < 30; i++){
            temp = new Label("ALARM "+i, new Label.LabelStyle(fontTest, fontTest.getColor()));
            scrollTableINFO.add(temp);
            acceptAlarm = new alarmButtons(game,"acceptAlarm",0);
            clearAlarm = new alarmButtons(game,"clearAlarm",0);
            scrollTableINFO.add(acceptAlarm).height(commH).width(asp2*commH);//.width(Value.percentWidth(.15f,scrollTable));
            scrollTableINFO.add(clearAlarm).height(commH).width(asp3*commH);
            scrollTableINFO.row();
        }
        scrollTableINFO.pack();

        ScrollPane scrollerINFO = new ScrollPane(scrollTableINFO,skin);

        asp2 = acceptAll.getAspect();
        asp3 = clearAll.getAspect();
        float commH2 = (scrollerWidth)/(asp2+asp3);

        fontParameter.size = (int) Math.ceil(commH_PanTitles);
        fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels

        float INFOboxW = scrollerWidth;
        float INFOboxH = systemsTabHeight;
        float INFOboxX = scrollerMargins;
        float INFOboxY = viewportHeight/2 - scrollerMargins/2 - INFOboxH;

        // ALARMS PANEL GROUP
        WidgetGroup groupINFO = new WidgetGroup();
        groupINFO.setBounds(INFOboxX,INFOboxY,INFOboxW,INFOboxH);
        Image backdropINFO = new Image();
        backdropINFO.setDrawable(new TextureRegionDrawable(game.getTilesAt().findRegion("pDTTile50Grey")));
        backdropINFO.setHeight(INFOboxH);
        backdropINFO.setWidth(INFOboxW);
        backdropINFO.setX(0);
        backdropINFO.setY(0);
        backdropINFO.setColor(backdropINFO.getColor().r,backdropINFO.getColor().g,backdropINFO.getColor().b,backdropINFO.getColor().a/2);
        groupINFO.addActor(backdropINFO);
        temp = new Label("SHIP ALARMS", new Label.LabelStyle(fontTest, fontTest.getColor()));
        temp.setPosition(groupINFO.getWidth()/2 - temp.getWidth()/2,groupINFO.getHeight() - temp.getHeight());
        groupINFO.addActor(temp);
        float acceptAllX = 0;
        float acceptAllY = temp.getY() - temp.getHeight();
        float acceptAllW = groupINFO.getWidth()/2;
        float acceptAllH = temp.getHeight();
        acceptAll.setBounds(acceptAllX,acceptAllY,acceptAllW,acceptAllH);
        groupINFO.addActor(acceptAll);
        float clearAllX = acceptAll.getX() + acceptAll.getWidth();
        float clearAllY = temp.getY() - temp.getHeight();
        float clearAllW = groupINFO.getWidth()/2;
        float clearAllH = temp.getHeight();
        clearAll.setBounds(clearAllX,clearAllY,clearAllW,clearAllH);
        groupINFO.addActor(clearAll);
        float scrollerINFO_W = groupINFO.getWidth();
        float scrollerINFO_H = groupINFO.getHeight() - temp.getHeight() - acceptAll.getHeight();
        scrollerINFO.setSize(scrollerINFO_W,scrollerINFO_H);
        float scrollerINFO_X = groupINFO.getWidth()/2 - scrollerINFO.getWidth()/2;
        float scrollerINFO_Y = acceptAll.getY() - scrollerINFO.getHeight();
        scrollerINFO.setPosition(scrollerINFO_X,scrollerINFO_Y);
        groupINFO.addActor(scrollerINFO);

        shipDetailPanel.addActor(groupINFO);

        // //////////////////////////////////////////////////////////
        // SHIP INVENTORY PANEL

        Table scrollTableINV = new Table();

        fontParameter.size = (int) Math.ceil(commH);
        fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        //fontGenerator.dispose(); // don't forget to dispose to avoid memory leaks!

        for(int i = 0; i < 30; i++){
            temp = new Label("INVENTORY "+i, new Label.LabelStyle(fontTest, fontTest.getColor()));
            scrollTableINV.add(temp);
            scrollTableINV.row();
        }
        scrollTableINV.pack();

        WidgetGroup INVgroup = new WidgetGroup();
        INVgroup.setWidth(scrollerWidth);
        INVgroup.setHeight(viewportHeight/2);
        INVgroup.setX(viewportWidth - scrollerMargins - INVgroup.getWidth());
        INVgroup.setY(scrollerMargins);
        Image backdropINV = new Image();
        backdropINV.setDrawable(new TextureRegionDrawable(game.getTilesAt().findRegion("pDTTile50Grey")));
        backdropINV.setHeight(INVgroup.getHeight());
        backdropINV.setWidth(INVgroup.getWidth());
        backdropINV.setX(0);
        backdropINV.setY(0);
        backdropINV.setColor(backdropINV.getColor().r,backdropINV.getColor().g,backdropINV.getColor().b,backdropINV.getColor().a/2);
        INVgroup.addActor(backdropINV);

        ScrollPane scrollerINV = new ScrollPane(scrollTableINV,skin);

        fontParameter.size = (int) Math.ceil(commH_PanTitles);
        fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.WHITE;
        fontParameter.borderWidth = fontParameter.size/borderDiv;
        fontTest = fontGenerator.generateFont(fontParameter); // font size 12 pixels
        temp = new Label("SHIP INVENTORY", new Label.LabelStyle(fontTest, fontTest.getColor()));
        temp.setPosition(INVgroup.getWidth()/2 - temp.getWidth()/2,INVgroup.getHeight() - temp.getHeight());
        INVgroup.addActor(temp);
        scrollerINV.setSize(INVgroup.getWidth(),INVgroup.getHeight() - temp.getHeight());
        scrollerINV.setPosition(INVgroup.getWidth()/2 - scrollerINV.getWidth()/2, temp.getY() - scrollerINV.getHeight());
        INVgroup.addActor(scrollerINV);

        shipDetailPanel.addActor(INVgroup);

        scroller.setForceScroll(false,true);
        scrollerINFO.setForceScroll(false,true);
        scrollerINV.setForceScroll(false,true);
        fontGenerator.dispose(); // don't forget to dispose to avoid memory leaks!
        stage.addActor(shipDetailPanel);
        shipDetailPanel.setZIndex(0);       // SET TO BACK OF ALL UIs

        //stage.setDebugAll(true);
    }

    public void toggleTextOverlay(boolean showText,float x,float y,String setText){
        if(showText){
            System.out.println("text at "+x+" by "+y);
            int actorsNo = stage.getActors().size;
            mouseLabel.setZIndex(actorsNo);
            mouseLabel.setText(setText);
            mouseLabel.setVisible(true);
            mouseLabel.setPosition(x,y);
        } else {
            mouseLabel.setVisible(false);
        }
    }

    public void toggleViewPane(){
        if(viewShowing){
            // showing - hide it
            viewPanel.setY(paneHidden);
            actionPanel.setY(paneHidden);
            orbitPanel.setY(paneHidden);
            screenPanel.setY(paneHidden);
            viewShowing = false;
            screenShowing = false;
            orbitShowing = false;
            actionShowing = false;
        } else {
            // hiding - show it
            viewPanel.setY(paneShowing);
            actionPanel.setY(paneShowing);
            orbitPanel.setY(paneShowing);
            screenPanel.setY(paneShowing);
            viewPanel.setZIndex(5);
            viewShowing = true;
            screenShowing = false;
            orbitShowing = false;
            actionShowing = false;
        }
    }

    public void toggleScreenPane(){
        if(screenShowing){
            // showing - hide it
            viewPanel.setY(paneHidden);
            actionPanel.setY(paneHidden);
            orbitPanel.setY(paneHidden);
            screenPanel.setY(paneHidden);
            viewShowing = false;
            screenShowing = false;
            orbitShowing = false;
            actionShowing = false;
        } else {
            // hiding - show it
            viewPanel.setY(paneShowing);
            actionPanel.setY(paneShowing);
            orbitPanel.setY(paneShowing);
            screenPanel.setY(paneShowing);
            screenPanel.setZIndex(5);
            viewShowing = false;
            screenShowing = true;
            orbitShowing = false;
            actionShowing = false;
        }
    }

    public void toggleActionPane(){
        if(actionShowing){
            // showing - hide it
            viewPanel.setY(paneHidden);
            actionPanel.setY(paneHidden);
            orbitPanel.setY(paneHidden);
            screenPanel.setY(paneHidden);
            viewShowing = false;
            screenShowing = false;
            orbitShowing = false;
            actionShowing = false;
        } else {
            // hiding - show it
            viewPanel.setY(paneShowing);
            actionPanel.setY(paneShowing);
            orbitPanel.setY(paneShowing);
            screenPanel.setY(paneShowing);
            actionPanel.setZIndex(5);
            viewShowing = false;
            screenShowing = false;
            orbitShowing = false;
            actionShowing = true;
        }
    }

    public void toggleOrbitPane(){
        if(orbitShowing){
            // showing - hide it
            viewPanel.setY(paneHidden);
            actionPanel.setY(paneHidden);
            orbitPanel.setY(paneHidden);
            screenPanel.setY(paneHidden);
            viewShowing = false;
            screenShowing = false;
            orbitShowing = false;
            actionShowing = false;
        } else {
            // hiding - show it
            viewPanel.setY(paneShowing);
            actionPanel.setY(paneShowing);
            orbitPanel.setY(paneShowing);
            screenPanel.setY(paneShowing);
            orbitPanel.setZIndex(5);
            viewShowing = false;
            screenShowing = false;
            orbitShowing = true;
            actionShowing = false;
        }
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
