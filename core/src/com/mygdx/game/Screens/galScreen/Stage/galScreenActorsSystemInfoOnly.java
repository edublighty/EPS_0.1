package com.mygdx.game.Screens.galScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.galScreen.Actors.galTiles;
import com.mygdx.game.Screens.galScreen.Actors.systemImage;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

public class galScreenActorsSystemInfoOnly implements Disposable {

    public Table table;
    public Stage stage;
    public Viewport viewport;
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

    public galScreenActorsSystemInfoOnly(MyGdxGame game, galaxyScreen screen, SpriteBatch sb, float viewportWidth, float viewportHeight){

        this.screen = screen;
        viewport = new FillViewport(viewportWidth,viewportHeight, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Gdx.input.setInputProcessor(stage);

        // ----------------------------------------------------------------------------------------------------------------------------------

        // RIGHT PANEL FOR SYSTEM INFO

        float margin = viewportHeight/100;//.5f;

        String texture;// = "zoomsPanel";
        float tempA;// = screen.getThrustAt().findRegion(texture).originalHeight;
        float tempB;// = screen.getThrustAt().findRegion(texture).originalWidth;

        texture = "zoomsPanel";
        tempA = screen.getThrustAt().findRegion(texture).originalHeight;
        tempB = screen.getThrustAt().findRegion(texture).originalWidth;
        float aspectsystOnlyTile = tempA / tempB;//(screen.getThrustAt().findRegion("MAPSTILE").originalHeight) / (screen.getThrustAt().findRegion("MAPSTILE").originalWidth);
        float systOnlyTileHeight = viewportHeight / 3;
        float systOnlyTileWidth = viewportWidth/5;
        float systOnlyTileX = viewportWidth - systOnlyTileWidth;
        float systOnlyTileY = viewportHeight/3;
        galTiles systOnlyTile = new galTiles(screen,systOnlyTileWidth,systOnlyTileHeight,systOnlyTileX,systOnlyTileY,texture);
        stage.addActor(systOnlyTile);

        String systNameS = "BLARGH2";

        Label systNameLabel = new Label("NAME: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label systnameVar = new Label(systNameS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float systNameXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - systNameLabel.getWidth()/2;
        float systNameXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - systnameVar.getWidth()/2;
        float systNameY = systOnlyTile.getY() + systOnlyTile.getHeight() - systNameLabel.getHeight() - margin;
        systNameLabel.setX(systNameXLab);
        systNameLabel.setY(systNameY);
        stage.addActor(systNameLabel);
        systnameVar.setX(systNameXVar);
        systnameVar.setY(systNameY);
        stage.addActor(systnameVar);

        String starClassS = "UNKNOWN";

        Label starClassLabel = new Label("STAR CLASS: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label starClassVar = new Label(starClassS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float starClassXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - starClassLabel.getWidth()/2;
        float starClassXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - starClassVar.getWidth()/2;
        float starClassY = systNameLabel.getY() - margin - starClassLabel.getHeight();
        starClassLabel.setX(starClassXLab);
        starClassLabel.setY(starClassY);
        stage.addActor(starClassLabel);
        starClassVar.setX(starClassXVar);
        starClassVar.setY(starClassY);
        stage.addActor(starClassVar);

        String tempS = "100*C";

        Label tempLabel = new Label("TEMPERATURE: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label tempVar = new Label(tempS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float tempXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - tempLabel.getWidth()/2;
        float tempXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - tempVar.getWidth()/2;
        float tempY = starClassLabel.getY() - margin - tempLabel.getHeight();
        tempLabel.setX(tempXLab);
        tempLabel.setY(tempY);
        stage.addActor(tempLabel);
        tempVar.setX(tempXVar);
        tempVar.setY(tempY);
        stage.addActor(tempVar);

        String radS = "10 Rad/s";

        Label radLabel = new Label("RADIATION: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label radVar = new Label(radS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float radXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - radLabel.getWidth()/2;
        float radXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - radVar.getWidth()/2;
        float radY = tempLabel.getY() - margin - radLabel.getHeight();
        radLabel.setX(radXLab);
        radLabel.setY(radY);
        stage.addActor(radLabel);
        radVar.setX(radXVar);
        radVar.setY(radY);
        stage.addActor(radVar);

        String allegS = "TERRAN";

        Label allegLabel = new Label("ALLEGIANCE: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label allegVar = new Label(allegS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float allegXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - allegLabel.getWidth()/2;
        float allegXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - allegVar.getWidth()/2;
        float allegY = radLabel.getY() - margin - allegLabel.getHeight();
        allegLabel.setX(allegXLab);
        allegLabel.setY(allegY);
        stage.addActor(allegLabel);
        allegVar.setX(allegXVar);
        allegVar.setY(allegY);
        stage.addActor(allegVar);

        String stabS = "HOSTILE";

        Label stabLabel = new Label("STABILITY: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label stabVar = new Label(stabS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float stabXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - stabLabel.getWidth()/2;
        float stabXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - stabVar.getWidth()/2;
        float stabY = allegLabel.getY() - margin - stabLabel.getHeight();
        stabLabel.setX(stabXLab);
        stabLabel.setY(stabY);
        stage.addActor(stabLabel);
        stabVar.setX(stabXVar);
        stabVar.setY(stabY);
        stage.addActor(stabVar);

        String gdpS = "1.5 McRs";

        Label gdpLabel = new Label("GDP: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label gdpVar = new Label(gdpS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float gdpXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - gdpLabel.getWidth()/2;
        float gdpXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - gdpVar.getWidth()/2;
        float gdpY = stabLabel.getY() - margin - gdpLabel.getHeight();
        gdpLabel.setX(gdpXLab);
        gdpLabel.setY(gdpY);
        stage.addActor(gdpLabel);
        gdpVar.setX(gdpXVar);
        gdpVar.setY(gdpY);
        stage.addActor(gdpVar);

        String industS = "MANUFACTURE";

        Label industLabel = new Label("INDUSTRY: ", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        Label industVar = new Label(industS,new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        float industXLab = systOnlyTileX + margin + systOnlyTile.getWidth()/4 - industLabel.getWidth()/2;
        float industXVar = systOnlyTileX + margin + systOnlyTile.getWidth()*3/4 - industVar.getWidth()/2;
        float industY = gdpLabel.getY() - margin - industLabel.getHeight();
        industLabel.setX(industXLab);
        industLabel.setY(industY);
        stage.addActor(industLabel);
        industVar.setX(industXVar);
        industVar.setY(industY);
        stage.addActor(industVar);

        // ----------------------------------------------------------------------------------------------------------------------------------

        stage.setDebugAll(true);
    }

    @Override
    public void dispose() {

    }
}
