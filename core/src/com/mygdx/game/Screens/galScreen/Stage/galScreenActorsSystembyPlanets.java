package com.mygdx.game.Screens.galScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.Actors.galTiles;
import com.mygdx.game.Screens.galScreen.Actors.planetTiles;
import com.mygdx.game.Screens.galScreen.Actors.seePlanetsButton;
import com.mygdx.game.Screens.galScreen.Actors.setCourseButton;
import com.mygdx.game.Screens.galScreen.Actors.starTiles;
import com.mygdx.game.Screens.galScreen.Actors.systemImage;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class galScreenActorsSystembyPlanets implements Disposable {

    public Table table;
    public Stage stage;
    public Stage stage2;
    public Viewport viewport;
    public Viewport viewport2;
    public Image[] starSysts;
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
    public OrthographicCamera camera;
    public OrthographicCamera camera2;
    private Label destVar;
    private float destTileHeight;
    private float destTileX;
    private float destTileY;
    private float margin;
    private String[][] systemData;
    private MyGdxGame game;

    public galScreenActorsSystembyPlanets(MyGdxGame game, galaxyScreen screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        this.screen = screen;
        this.game = game;
        this.systemData = screen.getSystemData();
        camera = new OrthographicCamera();
        viewport = new FillViewport(viewportWidth,viewportHeight, camera);
        camera.position.set(viewportWidth/2,viewportHeight/2,0);
        stage = new Stage(viewport, sb);

        camera2 = new OrthographicCamera();
        viewport2 = new FillViewport(viewportWidth,viewportHeight,camera2);
        camera2.position.set(viewportWidth/2,viewportHeight/2,0);
        stage2 = new Stage(viewport2,sb);
        Gdx.input.setInputProcessor(stage2);

        TextureRegion terrReg = screen.getThrustAt().findRegion("terr");
        TextureRegion terGasReg = screen.getThrustAt().findRegion("terGas");
        TextureRegion gasGiReg = screen.getThrustAt().findRegion("gasGi");

        // ------------------------------------------------------------------------------------------------------------

        // TOP RIGHT PANEL FOR CURRENT LOCATION

        margin = viewportHeight/100;//.5f;

        String texture = "TitleBG";
        destTileHeight = viewportHeight / 4;
        float destTileWidth = viewportWidth;
        destTileX = 0;
        destTileY = 0;
        galTiles destTile = new galTiles(screen,destTileWidth,destTileHeight,destTileX,destTileY,texture);
        stage.addActor(destTile);

        // ------------------------------------------------------------------------------------------------------------

        // BOTTOM STAGE SHOWING STAR / PLANET SPRITES

        String systDestS = " ";

        Label destLabel = new Label("DESTINATION: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        destVar = new Label(systDestS,new Label.LabelStyle(new BitmapFont(), Color.YELLOW));

        float destXLab = destTileX + margin;
        float destXVar = destXLab + margin + destLabel.getWidth();
        float destY = destTile.getY() + destTile.getHeight() - destLabel.getHeight();// - margin;
        destLabel.setX(destXLab);
        destLabel.setY(destY);
        stage.addActor(destLabel);
        destVar.setX(destXVar);
        destVar.setY(destY);
        stage.addActor(destVar);

        float planButtsWidth = viewportWidth / 40;
        float planButtsX = viewportWidth/2 - planButtsWidth;
        float planButtsY = destTile.getY() + destTile.getHeight() - planButtsWidth;
        float hideDistance = planButtsY-destTile.getY();
        seePlanetsButton seePlanButts = new seePlanetsButton(screen,this,planButtsWidth,planButtsX,planButtsY,"arrowUp",false,hideDistance);
        stage.addActor(seePlanButts);
        screen.planetsCutOffY = planButtsY/viewportHeight;

        float setCourseButtHeight = planButtsWidth;
        float setCourseButtWidth = setCourseButtHeight*4;
        float setCourseButtX = viewportWidth - setCourseButtWidth - margin;
        float setCourseButtY = planButtsY;
        setCourseButton setCourseButt = new setCourseButton(screen,this,setCourseButtWidth,setCourseButtHeight,setCourseButtX,setCourseButtY,"PLOTCOURSE");
        stage.addActor(setCourseButt);

        float tempA = screen.getStAnim().findRegion("1").originalHeight;
        float tempB = screen.getStAnim().findRegion("1").originalWidth;
        float aspectStarTile = tempA / tempB;
        float starHeight = destTileHeight/2;
        float starWidth = starHeight/aspectStarTile;
        float starX = destTileX + margin;
        float starY = destTileY + destTileHeight/2 - starHeight/2;
        starTiles starTile = new starTiles(screen,starWidth,starHeight,starX,starY,"1");
        stage2.addActor(starTile);

        String destStarS = "BLARGH2";

        //Label destLabel = new Label("DESTINATION: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label destStarVar = new Label(destStarS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float destStarXVar = starX + starWidth/2 - destStarVar.getWidth()/2;
        float destStarY = starY - destStarVar.getHeight();
        destStarVar.setX(destStarXVar);
        destStarVar.setY(destStarY);
        stage2.addActor(destStarVar);

        tempA = screen.getPlAtlas().findRegion("terr1").originalHeight;
        tempB = screen.getPlAtlas().findRegion("terr1").originalWidth;
        float aspectPlanetTile = tempA / tempB;
        tempA = screen.getPlAtlas().findRegion("terr1").originalHeight;
        tempB = screen.getStAnim().findRegion("1").originalHeight;
        float planetHeight = starHeight*tempA/tempB;
        float planetWidth = planetHeight/aspectStarTile;
        float planetX = starX + starWidth*2 + margin;
        float planetY = starY + starHeight/2 - planetHeight/2;
        planetTiles planetTile = new planetTiles(screen,planetWidth,planetHeight,planetX,planetY,"terr1");
        stage2.addActor(planetTile);

        String destPlanetS = "BLARGH2 A";

        //Label destLabel = new Label("DESTINATION: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label destPlanetVar = new Label(destPlanetS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float destPlanetXVar = planetX + planetWidth/2 - destPlanetVar.getWidth()/2;
        float destPlanetY = planetY - destPlanetVar.getHeight();
        destPlanetVar.setX(destPlanetXVar);
        destPlanetVar.setY(destPlanetY);
        stage2.addActor(destPlanetVar);

        camera.position.y += hideDistance;
        camera2.position.y += hideDistance;

        //stage.setDebugAll(true);
        //stage2.setDebugAll(true);
    }

    public void setDestination(String destination){
        // update text showing destination
        destVar.setText(destination);
        // delete all current graphics on stage2
        stage2.clear();
        // add star sprite and name
        float tempA = screen.getStAnim().findRegion("1").originalHeight;
        float tempB = screen.getStAnim().findRegion("1").originalWidth;
        float aspectStarTile = tempA / tempB;
        float starHeight = destTileHeight/2;
        float starWidth = starHeight/aspectStarTile;
        float starX = destTileX + margin;
        float starY = destTileY + destTileHeight/2 - starHeight/2;
        starTiles starTile = new starTiles(screen,starWidth,starHeight,starX,starY,"1");
        stage2.addActor(starTile);

        String destStarS = destination;

        //Label destLabel = new Label("DESTINATION: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label destStarVar = new Label(destStarS,new Label.LabelStyle(new BitmapFont(), Color.YELLOW));

        float destStarXVar = starX + starWidth/2 - destStarVar.getWidth()/2;
        float destStarY = starY - destStarVar.getHeight();
        destStarVar.setX(destStarXVar);
        destStarVar.setY(destStarY);
        stage2.addActor(destStarVar);

        //System.out.println("destination clicked is "+game.destGali);
        int plansInSyst = (int) Double.parseDouble(systemData[game.destGali][14]);
        //System.out.println("number of planets from new click "+plansInSyst);

        float lastX = starX;
        float lastY = starY;
        float lastWidth = starWidth*1.5f;
        float lastHeight = starHeight;
        int planCols = 16;

        for(int i=0;i<plansInSyst;i++){

            String destPlanetS = systemData[game.destGali][16 +i*planCols];

            //Label destLabel = new Label("DESTINATION: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
            Label destPlanetVar = new Label(destPlanetS,new Label.LabelStyle(new BitmapFont(), Color.WHITE));

            if(destPlanetVar.getWidth()>lastWidth){
                lastWidth=destPlanetVar.getWidth();
            }

            String planetType = systemData[game.destGali][17 + i*planCols];

            //System.out.println("Planet type is "+planetType+" for "+systemData[game.destGali][16 + i*planCols]);
            tempA = screen.getPlAtlas().findRegion(planetType).originalHeight;
            tempB = screen.getStAnim().findRegion("1").originalHeight;
            float planetHeight = starHeight*tempA/tempB;
            float planetWidth = planetHeight/aspectStarTile;
            float planetX = lastX + lastWidth + margin*2;
            float planetY = lastY + lastHeight/2 - planetHeight/2;
            planetTiles planetTile = new planetTiles(screen,planetWidth,planetHeight,planetX,planetY,planetType);
            stage2.addActor(planetTile);

            float destPlanetXVar = planetX + planetWidth/2 - destPlanetVar.getWidth()/2;
            float destPlanetY = planetY - destPlanetVar.getHeight();
            destPlanetVar.setX(destPlanetXVar);
            destPlanetVar.setY(destPlanetY);
            stage2.addActor(destPlanetVar);

            lastX = planetX;
            lastY = planetY;
            lastWidth = planetWidth;
            lastHeight = planetHeight;
        }

    }

    @Override
    public void dispose() {

    }
}
