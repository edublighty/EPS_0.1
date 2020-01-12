package com.mygdx.game.Perlin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Tools.b2dWorldCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class perlinDrawTest implements Screen {
    Perlin2 noise = new Perlin2();
    Perlin noiseOrig = new Perlin();
    World world;
    int gravity;
    Box2DDebugRenderer b2dr;
    Viewport gameport;
    int sWidth;
    int sHeight;
    OrthographicCamera gamecam;
    TiledMapRenderer renderer;
    int mapWidth;
    int mapHeight;
    MyGdxGame game;
    public float[][] perlinNoise;
    public float[][] humidityNoise;
    public float[][] tempNoise;
    public float[][] terrainProfile;
    public int[][] biomesData;
    public int[][] biomesLayout;
    int biomeLayout[][];
    b2dWorldCreator b2dWC;
    Array<Body> bodies;
    private TiledMap map;

    public perlinDrawTest(MyGdxGame game) {
        this.game=game;
        this.noise = noise;
        gravity = 0;
        sHeight=2000;
        sWidth=3000;
        mapWidth = 3200;
        mapHeight = 3000;

        map = new TmxMapLoader().load("practiceDynTiles/pDTMap3.tmx");

        gamecam = new OrthographicCamera();
        perlinNoise = new float[sWidth][2];

        gameport = new FitViewport(5000 , 5000 , gamecam) {};
        world = new World(new Vector2(0, 0), true); // 0,0 transpires as no gravity
        b2dr = new Box2DDebugRenderer();

        String sCurrentLine;
        biomesData = new int[6][10];
        FileHandle handle = Gdx.files.internal("Arrays/biomes.txt");
        BufferedReader reader = new BufferedReader(handle.reader());

        int i;
        int j = 0;
        try {
            while ((sCurrentLine = reader.readLine()) != null) {
                String[] arr = sCurrentLine.split("\\s+");
                //System.out.println(arr[0] + " and " + arr[1] + " and " + arr[2]);
                for(i=2;i<7;i++) {
                    biomesData[i-2][j] = Integer.parseInt(arr[i]);
                }
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to complete line reading process");
        }

        System.out.println("biome 1 "+biomesData[0][0]);

        // Generates biome lengths and assigns to array
        genTerrain();

        // create bodies from terrain profile
        b2dWC = new b2dWorldCreator(world,map,this,null,null,0,0,sWidth,sHeight);
        bodies = new Array<Body>();
        world.getBodies(bodies);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);

        // initialise gamecam centrally
        gamecam.position.set(bodies.get(0).getPosition().x, bodies.get(0).getPosition().y, 0);

        System.out.println("size of bodies "+bodies.size);

    }

    public void genTerrain(){

        int i;
        int mWidth = 3200;      // size of map in tile number
        int iCount=0;           // length of current biome
        boolean searching=true;
        int temp;
        int humid;
        int j = 0;
        biomeLayout = new int[50][3];
        humidityNoise = perlinFun(mapWidth,3,1,4,10);
        System.out.println("humidity "+humidityNoise[0][1]);
        temp = (int) Math.round(50*MathUtils.sin(MathUtils.PI*4*0/mapWidth-MathUtils.PI/2)+50);
        humid = Math.round(humidityNoise[0][1] * 100);
        System.out.println("humidity "+humid+" and temp "+temp);
        System.out.println("humidity limits "+biomesData[2][0]+" and "+biomesData[3][0]+" and temp limits "+biomesData[0][0]+" and "+biomesData[1][0]);

        // allocate biomes based on noise
        while (searching) {
            // looking for correct biome
            if (temp >= biomesData[0][j] && temp < biomesData[1][j] && humid >= biomesData[2][j] && humid < biomesData[3][j]) {
                // found a biome!
                searching = false;
                biomeLayout[0][0] = j;                      // give biome type
                biomeLayout[0][2] = biomesData[4][j];       // give flatness factor
            } else {
                // keep searching
                j++;
                //System.out.println("j "+j);
                if(j>10){
                    System.out.println("Biome location error");
                }
            }
        }

        System.out.println("initialised biome data - init biome "+biomeLayout[0][0]);

        int n = 0;
        j = 0;
        searching = true;

        System.out.println("humidityNoise size "+humidityNoise.length+" by "+humidityNoise[0].length);

        // Start measuring out sections of biomes
        for(i=0;i<mapWidth;i++) {
            //System.out.println("i "+i+" and iCount "+iCount);
            if (iCount < 200) {
                // do nothing - not long enough yet
                iCount++;
            } else {
                // time to change perhaps - record previous length
                temp = (int) Math.round(50*MathUtils.sin(MathUtils.PI*4*i/mapWidth-MathUtils.PI/2)+50);
                humid = Math.round(humidityNoise[i][1] * 100);
                System.out.println("temp "+temp+" and humidity "+humid+" i "+i+" "+mapWidth);
                j=biomeLayout[n][0];
                if(temp >= biomesData[0][j] && temp < biomesData[1][j] && humid >= biomesData[2][j] && humid < biomesData[3][j]){
                    // same biome - continue counting
                    iCount++;
                } else {
                    // different biome - go find it!
                    j=0;
                    biomeLayout[n][1] = iCount;
                    iCount = 0;
                    n++;
                    while (searching) {
                        // looking for correct biome
                        if (temp >= biomesData[0][j] && temp < biomesData[1][j] && humid >= biomesData[2][j] && humid < biomesData[3][j]) {
                            // found a biome - exit while loop!
                            searching = false;
                            biomeLayout[n][0] = j;
                            biomeLayout[n][2] = biomesData[4][j];
                            //System.out.println("biome "+biomeLayout[n][0]+" and flatness "+biomeLayout[n][2]);
                        } else if (temp>=100) {
                            if (humid < 25) {
                                // Desert
                                j = 0;
                            } else if (humid < 75) {
                                // Savannah
                                j = 4;
                            } else {
                                // Volcanoes and shit!
                                j = 8;
                            }
                            searching = false;
                        } else if (humid>=100){
                            if (temp<25){
                                // Mountainous Tundra
                                j = 7;
                            } else if (temp<75) {
                                // Rainforest
                                j = 9;
                            } else {
                                // Volcanoes
                                j = 8;
                            }
                            searching = false;
                        } else {
                            // keep searching
                            j++;
                        }
                    }
                    // reset for next search for a biome if required
                    searching = true;
                    j = 0;
                }
            }
        }

        // place final part of iCount
        biomeLayout[n][1] = iCount;

        System.out.println("Biome gen finished - as follows");

        for(i=0;i<50;i++){
            System.out.println("biome "+biomeLayout[i][0]+" and length "+biomeLayout[i][1]+" and flatness "+biomeLayout[i][2]);
        }

        System.out.println("about to test");
        i=0;

        try {
            while (biomeLayout[i][1] != 0) {
                //System.out.println("testing "+biomeLayout[i][1]);
                i++;
            }
        } catch(RuntimeException e) {
            e.printStackTrace ();
            // Assume reached end of array
            i=32;
        }

        System.out.println("i "+i);

        biomesLayout = new int[i][3];
        for(j=0;j<i;j++){
            biomesLayout[j][0] = biomeLayout[j][0];
            biomesLayout[j][1] = biomeLayout[j][1];
            biomesLayout[j][2] = biomeLayout[j][2];
        }

        System.out.println("BL at "+i+" "+biomesLayout[i-1][0]+" and "+biomesLayout[i-1][1]);

        //System.out.println("biomes Lengt h "+biomesLayout.length+" vs "+biomesLayout[0].length);
        List<float[][]> biomesArray = new ArrayList<float[][]>();
        //biomesArray.add(new float[1][2]);
        float lastY = 1600;
        int tempX;
        int oct = 1;
        int oct6 = 7;
        int oct7 = 9;

        System.out.println("biomeslayout length "+biomesLayout.length);

        // Takes array for biomes and their lenghts, converts to terrain profile for world circumference using Perlin noise
        for(i=0;i<biomesLayout.length;i++){
            if(biomesLayout[i][2]==1){
                // flat
                oct = 1;
                oct6 = 1;
                oct7 = 13;
            } else if(biomesLayout[i][2]==2){
                // mild
                oct = 1;
                oct6 = 1;
                oct7 = 11;
            } else if(biomesLayout[i][2]==3){
                // mountainous
                oct = 1;
                oct6 = 7;
                oct7 = 9;
            } else {
                // No flatness factor - ERROR
                System.out.println("ERROR NO FLATNESS FACTOR ASSIGNED");
            }
            tempNoise = perlinFun2(lastY,biomesLayout[i][1],3,oct,oct6,oct7,mapHeight);
            tempNoise[0][0] = biomeLayout[i][0];            // biome
            tempNoise[0][2] = biomeLayout[i][2];            // flatness factor - yes that is a thing (1=flat, 2 = mild, 3 = mountainous)
            //System.out.println("flatness factor "+biomeLayout[0][2]);
            //System.out.println("length tempNosie "+tempNoise.length+" and "+tempNoise[0].length);
            tempX = (int) (biomesLayout[i][1]);
            lastY=tempNoise[tempX-1][1];
            //System.out.println("lastY "+lastY);
            biomesArray.add(tempNoise);
        }

        terrainProfile = new float[mapWidth+1000][3];
        iCount=0;
        j = 0;
        System.out.println("mapwidth "+mapWidth);
        lastY = 0;

        // Generate terrain profile from noise profiles
        for(i=0;i<mapWidth;i++){
            //System.out.println("i "+i+" and iCount "+iCount+" j "+j+" biomelength "+biomesArray.get(j).length+"bArray size "+biomesArray.size());
            //System.out.println("flatness "+biomesArray.get(j)[0][2]);
            if(j<biomesArray.size()-1){
                // within Perlin Noise array still
                if(iCount>=biomesArray.get(j).length){
                    j++;
                    lastY = terrainProfile[i-1][1];
                    //System.out.println("lasty "+lastY);
                    iCount=0;
                }
                //System.out.println("current bArray value "+biomesArray.get(j)[iCount][1]);
                terrainProfile[i][0]=biomesArray.get(j)[0][0];              // biomes
                terrainProfile[i][2]=biomesArray.get(j)[0][2];              // flatness factor
                terrainProfile[i][1]=biomesArray.get(j)[iCount][1];//lastY+biomesArray.get(j)[iCount][1]*biomesArray.get(j)[0][2]*1000-biomesArray.get(j)[0][2]*1000/2;;    // height
                System.out.println("assigning terrain. biome "+biomesArray.get(j)[iCount][0]+" length "+biomesArray.get(j)[iCount][1]+" and flatness "+biomesArray.get(j)[iCount][2]);
                iCount++;
            } else {
                // Reached end - fill the gap
                terrainProfile[i][1]=biomesArray.get(j)[iCount][1];//*biomesArray.get(j)[0][2]*1000;
            }
        }

        for(j=0;j<biomesArray.size();j++) {
            //System.out.println("biome j " + biomesArray.get(j)[0][0] + " height " + biomesArray.get(j)[0][1] + " flatness "+biomesArray.get(j)[0][2]);
        }

        for(i=0;i<mapWidth;i++) {
            System.out.println("terrain at " + i + " is " + terrainProfile[i][1]+" and biome "+terrainProfile[i][0]+" flatness factor "+terrainProfile[i][2]);
        }

        // Now use loop to make up delta between two world sides
        float initY = terrainProfile[0][1];
        float finalY = terrainProfile[mapWidth-1][1];
        System.out.println("initial "+initY+" final "+finalY);
        float m =(finalY-initY)/800;
        for(i=mapWidth;i<mapWidth+800;i++){
            terrainProfile[i][1] = terrainProfile[i-1][1]-m;
        }
        for(i=mapWidth+800;i<mapWidth+1000;i++){
            terrainProfile[i][1] = initY;
        }
        System.out.println("final 2 "+terrainProfile[mapWidth+999][1]);

        int nHeight = 10;
        tempNoise = perlinFun2(5,terrainProfile.length,2,1,3,4,nHeight);

        for(i=0;i<terrainProfile.length;i++){
            //System.out.println("tempNoise "+tempNoise[i][0]);
            terrainProfile[i][1] = terrainProfile[i][1]+tempNoise[i][0];
        }

    }


    public float[][] perlinFun(int width, int height,int oct,int oct6,int oct7) {
        // PXTotal is total width of world
        // Height world at like 10 screens?
        //System.out.println("perly width "+width+" and height "+height);
        //time1 = System.currentTimeMillis();
        /*spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("assets/data/textures/basictextures.png"));

        regions[0] = new TextureRegion(texture,0,0,16,16); //grass
        regions[1] = new TextureRegion(texture,16,0,16,16); //water
        regions[2] = new TextureRegion(texture,0,17,16,16);48 //sand
        regions[3] = new TextureRegion(texture,17,17,16,16); //rock*/

        int oct1 = oct;
        int oct2 = oct;
        int oct3 = oct;
        int oct4 = oct;
        int oct5 = oct;

        float[][] seed = noiseOrig.GenerateWhiteNoise(width, height);
        /*for (int i = 0;i < seed.length; i++){
            for ( int j = 0; j < seed[i].length; j++){
                System.out.println(seed[i][j] + " ");
            }
        }*/

        float[][] seedC = noiseOrig.GenerateSmoothNoise(seed, oct1);


        float[][] seedD = noiseOrig.GenerateSmoothNoise(seedC, oct2);



        //System.out.println("size of seed "+seed.length+" by "+seed[0].length);

        float[][] seedE = noiseOrig.GenerateSmoothNoise(seedD, oct3);


        /*for (int i = 0;i < seedE.length; i++){
            for ( int j = 0; j < seedE[i].length; j++){
                System.out.println(seedE[i][j] + " ");
            }

        }*/

        float[][] seedF = noiseOrig.GenerateSmoothNoise(seedE, oct4);


        float[][] seedG = noiseOrig.GenerateSmoothNoise(seedF, oct5);



        float[][] seedH = noiseOrig.GenerateSmoothNoise(seedG, oct6);


        float[][] perlinNoise = noiseOrig.GeneratePerlinNoise(seedH, oct7);
        /*for (int i = 0;i < perlinNoise.length; i++){
            for ( int j = 0; j < perlinNoise[i].length; j++){
                System.out.println(perlinNoise[i][j] + " ");
            }
        }*/
/*

        String FNAME = "perlinOct"+"PER"+height+".txt";
        ArrayList list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";
        int i;
        int j;

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<height;j++) {
                for (i = 1; i < width; i++) {
                    line1 = line1 + " " + String.valueOf(perlinNoise[i][j]);

                    bw.write(line1);// + "\n");
                    //bw.write("STOP NOW");
                    bw.write("\r\n");
                    line1 = "";
                    //System.out.println("Printing "+i+" + "+j);
                }
            }

            //System.out.println("Printed supposedly");
            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }
*/

        //System.out.println("Finished text stuff");


        this.perlinNoise = perlinNoise;
/*

        time1 = System.currentTimeMillis() - time1;
        System.out.println("time to gen perlin nosie "+time1);
        time1 = System.currentTimeMillis();
*/

/*
        i = 0;
        if (height > 2) {
            mapWidth = perlinNoise.length;
            mapHeight = perlinNoise[i].length;
        } else {

        }*/
/*

        final String FNAME = "test"+"A"+height+".txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<height;j++) {
                for (i = 1; i < width; i++) {
                    line1 = line1 + " " + String.valueOf(perlinNoise[i][j]);
                }
                bw.write(line1 + "\n");
                bw.write("STOP NOW");
                bw.write("\r\n");
                line1 = "";
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }



        time1 = System.currentTimeMillis() - time1;

        System.out.println("and time to write perlin nosie "+time1);

        return perlinNoise;

        */
        return perlinNoise;
    }

    public float[][] perlinFun2(float lastY, int width, int height,int oct,int oct6,int oct7,int nHeight) {
        // PXTotal is total width of world
        // Height world at like 10 screens?
        //System.out.println("perly width "+width+" and height "+height);
        //time1 = System.currentTimeMillis();
        /*spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("assets/data/textures/basictextures.png"));

        regions[0] = new TextureRegion(texture,0,0,16,16); //grass
        regions[1] = new TextureRegion(texture,16,0,16,16); //water
        regions[2] = new TextureRegion(texture,0,17,16,16);48 //sand
        regions[3] = new TextureRegion(texture,17,17,16,16); //rock*/

        int oct1 = oct;
        int oct2 = oct;
        int oct3 = oct;
        int oct4 = oct;
        int oct5 = oct;

        float[][] seed = noise.GenerateWhiteNoise(width, height,lastY,nHeight);
        /*for (int i = 0;i < seed.length; i++){
            for ( int j = 0; j < seed[i].length; j++){
                System.out.println(seed[i][j] + " ");
            }
        }*/

        float[][] seedC = noise.GenerateSmoothNoise(seed, oct1);


        float[][] seedD = noise.GenerateSmoothNoise(seedC, oct2);



        //System.out.println("size of seed "+seed.length+" by "+seed[0].length);

        float[][] seedE = noise.GenerateSmoothNoise(seedD, oct3);


        /*for (int i = 0;i < seedE.length; i++){
            for ( int j = 0; j < seedE[i].length; j++){
                System.out.println(seedE[i][j] + " ");
            }

        }*/

        float[][] seedF = noise.GenerateSmoothNoise(seedE, oct4);


        float[][] seedG = noise.GenerateSmoothNoise(seedF, oct5);



        float[][] seedH = noise.GenerateSmoothNoise(seedG, oct6);


        float[][] perlinNoise = noise.GeneratePerlinNoise(seedH, oct7);
        /*for (int i = 0;i < perlinNoise.length; i++){
            for ( int j = 0; j < perlinNoise[i].length; j++){
                System.out.println(perlinNoise[i][j] + " ");
            }
        }*/
/*

        String FNAME = "perlinOct"+"PER"+height+".txt";
        ArrayList list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";
        int i;
        int j;

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<height;j++) {
                for (i = 1; i < width; i++) {
                    line1 = line1 + " " + String.valueOf(perlinNoise[i][j]);

                    bw.write(line1);// + "\n");
                    //bw.write("STOP NOW");
                    bw.write("\r\n");
                    line1 = "";
                    //System.out.println("Printing "+i+" + "+j);
                }
            }

            //System.out.println("Printed supposedly");
            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }

        //System.out.println("Finished text stuff");
*/


        this.perlinNoise = perlinNoise;
/*

        time1 = System.currentTimeMillis() - time1;
        System.out.println("time to gen perlin nosie "+time1);
        time1 = System.currentTimeMillis();
*/

/*
        i = 0;
        if (height > 2) {
            mapWidth = perlinNoise.length;
            mapHeight = perlinNoise[i].length;
        } else {

        }*/
/*

        final String FNAME = "test"+"A"+height+".txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = "";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (j=1;j<height;j++) {
                for (i = 1; i < width; i++) {
                    line1 = line1 + " " + String.valueOf(perlinNoise[i][j]);
                }
                bw.write(line1 + "\n");
                bw.write("STOP NOW");
                bw.write("\r\n");
                line1 = "";
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }



        time1 = System.currentTimeMillis() - time1;

        System.out.println("and time to write perlin nosie "+time1);

        return perlinNoise;

        */
        return perlinNoise;
    }

    @Override
    public void show() {

    }

    public void update(float dt){
        gamecam.update();
        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //System.out.println("Render");
        update(delta);

        //System.out.println("play render");
        Gdx.gl.glClearColor(0, 0, 0, 1);    // clears screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);                   // clears screen

        // render game map
        renderer.render();

        game.batch.setProjectionMatrix(gamecam.combined);

        //posTX = Math.floor(posX/50);
        //posTY = Math.floor(posY/50);

        int i;// = Math.round(posX*MyGdxGame.PPM)/50;
        int j;// = Math.round(posY*MyGdxGame.PPM)/50;
        game.batch.begin();
        //System.out.println("Batching hard, i "+i+" and j "+j+" and maplayout "+mapLayout[i][j]);
        //player.draw(game.batch);

        //game.batch.draw(atile[mapLayout[i][j]], (posX), (posY), 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        game.batch.end();

        // render Box2D Debug lines
        b2dr.render(world,gamecam.combined);
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height, false);
        gamecam.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
