package com.mygdx.game.Screens.galScreen.Stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.battleDeck.Sprites.doorImage;
import com.mygdx.game.Screens.galScreen.Actors.betweenSystLines;
import com.mygdx.game.Screens.galScreen.Actors.systemImage;
import com.mygdx.game.Screens.galScreen.Actors.warpDistance;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dEdge;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dGraph;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dVertex;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dijkstra;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.serialTest;
import com.mygdx.game.Screens.galScreen.galaxyScreen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class galScreenActors implements Disposable {

    public Table table;
    public Stage stage;
    public Stage stage2;
    public Viewport viewport;
    public Image[] starSysts;
    private galaxyScreen screen;
    public boolean pointNotSelected;
    public int actorsTotal;
    public int actorCurrent;
    public int actorDest;
    private int actorTemp;
    public float destX;
    public float destY;
    public int iDest;
    public float maxZoomX;
    public float maxZoomY;
    public OrthographicCamera camera;
    private final float systGraphWidth;
    private Image backImage = new Image();
    private List<dVertex> nodes;
    private List<dEdge> edges;
    private MyGdxGame game;
    LinkedList<dVertex> path;
    private float viewportHeight;
    private int pathSize;
    String[][] systemData;

    public galScreenActors(MyGdxGame game, galaxyScreen screen, galScreenHUD galHUD, galScreenActorsSystembyPlanets galScreenPlanets, SpriteBatch sb, float viewportWidth, float viewportHeight, float[][] galPoints, OrthographicCamera camera2, String[] galNames){

        float time3 = System.nanoTime();
        this.viewportHeight = viewportHeight;

        this.game = game;
        this.screen = screen;
        pointNotSelected = true;
        camera = new OrthographicCamera();
        viewport = new FillViewport(viewportWidth,viewportHeight, camera);
        camera.position.set(viewportWidth/2,viewportHeight/2,0);
        stage = new Stage(viewport, sb);
        stage2 = new Stage(viewport, sb);

        Gdx.input.setInputProcessor(stage);
        TextureRegion backgrd2 = screen.getStarsAt().findRegion("galaxy_v0.1Small");

        starSysts = new Image[galPoints.length];
        SpriteDrawable sprite = new SpriteDrawable(new Sprite(backgrd2));

        backImage.setDrawable(new SpriteDrawable(sprite));
        float imageWidth = viewportWidth;
        float imageHeight = imageWidth*(backImage.getDrawable().getMinHeight())/(backImage.getDrawable().getMinHeight());
        backImage.setSize(imageWidth,imageHeight);
        backImage.setPosition(viewportWidth/2-imageWidth/2,viewportHeight/2-imageHeight/2);
        stage.addActor(backImage);
        maxZoomX = imageWidth;
        maxZoomY = imageHeight;
        systGraphWidth = viewportWidth/MyGdxGame.PPM;
        systemData = screen.getSystemData();

        game.currentGali = 15;
        double systX = (Double.parseDouble(systemData[game.currentGali][2]))*backImage.getWidth()/5000 + backImage.getX();
        double systY = (Double.parseDouble(systemData[game.currentGali][1]))*backImage.getHeight()/5000 + backImage.getY();
        game.currentGalX = (float) systX;
        game.currentGalY = (float) systY;
        game.currentGalName = systemData[game.currentGali][0];

        nodes = new ArrayList<dVertex>();
        edges = new ArrayList<dEdge>();

        stage.addActor(new warpDistance(screen, game.currentGalX, game.currentGalY, game.maxWarpDist));

        for(int i=0;i<systemData.length;i++) {
            //System.out.println("in galScreenActor i is "+systemData[i][1]+" and j "+systemData[i][2]);
            systX = (Double.parseDouble(systemData[i][2]))*backImage.getWidth()/5000 + backImage.getX();
            systY = (Double.parseDouble(systemData[i][1]))*backImage.getHeight()/5000 + backImage.getY();
            //System.out.println("in galScreenActor after parsing double i is "+Double.parseDouble(systemData[i][1])+" and j "+Double.parseDouble(systemData[i][2]));
            starSysts[i] = new systemImage(screen, systGraphWidth, (float) systX, (float) systY, galHUD,galScreenPlanets,"starmebbe5",i,galNames[i]);
            stage.addActor(starSysts[i]);
            dVertex location = new dVertex(Integer.toString(i),systemData[i][0],i,(float) systX, (float) systY);
            System.out.println("orig array "+systemData[i][0]+" X "+systX+" Y "+systY);
            System.out.println("vertex "+location.getName()+" X "+location.getX()+" Y "+location.getY());
            nodes.add(location);
            //System.out.println("galPoint X "+starSysts[i].getX()+" Y "+starSysts[i].getY()+" width "+starSysts[i].getWidth()+" height "+starSysts[i].getHeight());
        }

        boolean writeDijBool = true;
        boolean doingDijStuff = true;
        int tempDestInt = 5;
        pathSize = 0;

        if(doingDijStuff) {
            if (writeDijBool) {
                //drawPath(tempDestInt);
            } else {
                dijkstra dij2 = new serialTest().readSerial();
                float time = System.nanoTime();
                path = dij2.getPath(nodes.get(tempDestInt));
                float time2 = System.nanoTime() - time;
                System.out.println("reading the serial took " + time2);
            }
        }

        // current position actor
        stage.addActor(new systemImage(screen, systGraphWidth, game.currentGalX, game.currentGalY, galHUD, galScreenPlanets, "starmebbe7",game.currentGali,game.currentGalName));

        //actorsTotal = systemData.length;
        System.out.println("path size is "+pathSize);
        actorCurrent = systemData.length+2;
        actorDest = systemData.length+3;

        //stage.setDebugAll(true);
        time3 -= System.nanoTime();
        System.out.println("whole of galscreenactors took "+time3);
    }

    private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
        dEdge lane = new dEdge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration );
        edges.add(lane);
    }

    public void drawPath(){

        if(pointNotSelected){
            // point not yet selected so no path drawn
            System.out.println("NO DESTINATION SELECTED TO PLOT COURSE");
        } else {
            // delete current path
            stage2.clear();
            // add edges between vertices
            for (int i = 0; i < systemData.length; i++) {
                for (int j = 0; j < systemData.length; j++) {
                    //System.out.println("nodes i " + i + " vs j " + j);
                    if (i != j) {
                        // not at the same point so make an edge
                        int rad = (int) (1000 * Math.sqrt(Math.pow((starSysts[i].getX() - starSysts[j].getX()), 2) + Math.pow((starSysts[i].getY() - starSysts[j].getY()), 2)));
                        //System.out.println("rad is "+rad);
                        if (rad < game.maxWarpDist) {
                            addLane("" + i + "-"+j, i, j, rad);    // edge is made with factor of 1000 cos some fucktard made weighting an integer and cba to go through it all to make it a double. It'll be fine. Ish
                        }
                    }
                }
            }

            dGraph graph = new dGraph(nodes, edges);
            System.out.println("size of edges is "+edges.size());
            dijkstra dij = new dijkstra(graph);
            System.out.println("made dij");
            dij.execute(nodes.get(game.currentGali));
            System.out.println("executed dij");
            path = dij.getPath(nodes.get(iDest));
            System.out.println("got path "+path);


            // draw path
            boolean testBool = false;
            if(path==null) {
                System.out.println("PATH NOT POSSIBLE");
            } else {
                pathSize = path.size() - 1;
                for (int i = 0; i < pathSize; i++) {
                    stage2.addActor(new betweenSystLines(screen, path.get(i).getX(), path.get(i + 1).getX(), path.get(i).getY(), path.get(i + 1).getY(), viewportHeight / 500, systGraphWidth / 2, systGraphWidth / 2, "fuelOK"));
                    System.out.println("path X1 " + path.get(i).getX() + " Y1 " + path.get(i).getY() + " and then X2 " + path.get(i + 1).getX() + " Y2 " + path.get(i + 1).getY());
                    //System.out.println("actor X1 "+stage2.getActors().get(i).getX()+" Y1 "+stage2.getActors().get(i).getY());
                }
            }
        }



    }
    public void updateMarkers(float X, float Y,double spriteWidth, double spriteHeight,galScreenHUD galHUD, galScreenActorsSystembyPlanets galScreenPlanets, String name,int row){
        iDest = row;
        if(pointNotSelected){
            // havent selected point yet so create for first time
            stage.addActor(new systemImage(screen, systGraphWidth, X, Y, galHUD,galScreenPlanets,"starmebbeSel",actorDest,name));
            System.out.println("target X "+X+" and Y "+Y);
            //drawPath(row);
            destX = X;
            destY = Y;
            pointNotSelected = false;
            String systemData[][] = screen.getSystemData();
            game.destGali = row;
            galScreenPlanets.setDestination(systemData[row][0]);
        } else {
            stage.getActors().get(actorDest).remove();
            stage.addActor(new systemImage(screen, systGraphWidth, X, Y, galHUD,galScreenPlanets,"starmebbeSel",actorDest,name));
            System.out.println("target X "+X+" and Y "+Y);
            //drawPath(row);
            galHUD.destLocVar.setText(name);
            destX = X;
            destY = Y;
            String systemData[][] = screen.getSystemData();
            game.destGali = row;
            galScreenPlanets.setDestination(systemData[row][0]);
        }
    }

    @Override
    public void dispose() {

    }
}
