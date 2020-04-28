package com.mygdx.game.Screens.galScreen.Tools;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class starGenerator2 {

    private static MyGdxGame game;

    public starGenerator2(MyGdxGame game) {

        this.game=game;

        int width = 500;
        int height = 500;

        int[][][] imageOne = generateStar(width,height,"1");
        int[][][] imageTwo = generateStar(width,height,"2");
        int[][][] image15 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image15[k][i][j] = (int) ((imageOne[k][i][j]+imageTwo[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image15[0][x][y]); //alpha
                int r = (int) (image15[1][x][y]); //red
                int g = (int) (image15[2][x][y]); //green
                int b = (int) (image15[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        File f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star1 5.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        /////////////
        // image between 1 and average
        int[][][] image13 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image13[k][i][j] = (int) ((imageOne[k][i][j]+image15[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image13[0][x][y]); //alpha
                int r = (int) (image13[1][x][y]); //red
                int g = (int) (image13[2][x][y]); //green
                int b = (int) (image13[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star1 3.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        /////////////
        // image between average and 2
        int[][][] image17 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image17[k][i][j] = (int) ((imageTwo[k][i][j]+image15[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image17[0][x][y]); //alpha
                int r = (int) (image17[1][x][y]); //red
                int g = (int) (image17[2][x][y]); //green
                int b = (int) (image17[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star1 7.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        ////////////////////
        ///// create 3rd image

        int[][][] image31 = generateStar(width,height,"3");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image31[0][x][y]); //alpha
                int r = (int) (image31[1][x][y]); //red
                int g = (int) (image31[2][x][y]); //green
                int b = (int) (image31[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        ////////////////////////////////////
        // create average between 2 and 3

        int[][][] image25 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image25[k][i][j] = (int) ((imageTwo[k][i][j]+image31[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image25[0][x][y]); //alpha
                int r = (int) (image25[1][x][y]); //red
                int g = (int) (image25[2][x][y]); //green
                int b = (int) (image25[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        //write image
        try {
            f = new File("C:\\Image\\stars\\star2 5.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        ///////////////////////////////////
        // image between 2 and 2.5
        int[][][] image23 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image23[k][i][j] = (int) ((imageTwo[k][i][j]+image25[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image23[0][x][y]); //alpha
                int r = (int) (image23[1][x][y]); //red
                int g = (int) (image23[2][x][y]); //green
                int b = (int) (image23[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star2 3.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        /////////////
        // image between average and 2
        int[][][] image27 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image27[k][i][j] = (int) ((image25[k][i][j]+image31[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image27[0][x][y]); //alpha
                int r = (int) (image27[1][x][y]); //red
                int g = (int) (image27[2][x][y]); //green
                int b = (int) (image27[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star2 7.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        ////////////////////////////////////
        // create average between 3 and 1

        int[][][] image35 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image35[k][i][j] = (int) ((image31[k][i][j]+imageOne[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image35[0][x][y]); //alpha
                int r = (int) (image35[1][x][y]); //red
                int g = (int) (image35[2][x][y]); //green
                int b = (int) (image35[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        //write image
        try {
            f = new File("C:\\Image\\stars\\star3 5.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        ///////////////////////////////////
        // image between 3 and 3.5
        int[][][] image33 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image33[k][i][j] = (int) ((image31[k][i][j]+image35[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image33[0][x][y]); //alpha
                int r = (int) (image33[1][x][y]); //red
                int g = (int) (image33[2][x][y]); //green
                int b = (int) (image33[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star3 3.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        /////////////
        // image between 3.5 and 1
        int[][][] image37 = new int[4][width][height];

        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                for(int k=0;k<4;k++){
                    //System.out.println("colour "+k+" value 1 "+imageOne[k][i][j]+" value 2 "+imageTwo[k][i][j]);
                    image37[k][i][j] = (int) ((image35[k][i][j]+imageOne[k][i][j])/2);
                    //System.out.println("average output "+imageAverage[k][i][j]);
                }
            }
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (image37[0][x][y]); //alpha
                int r = (int) (image37[1][x][y]); //red
                int g = (int) (image37[2][x][y]); //green
                int b = (int) (image37[3][x][y]); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        f = null;

        //write image
        try {
            f = new File("C:\\Image\\stars\\star3 7.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }


    }

    public int[][][] generateStar(int width,int height,String count){

        //image dimension
        int numTheta = 2000;
        double radMax = height * 0.49f;
        int midX = width / 2;
        int midY = height / 2;
        int X;
        int Y;
        //double rad = radMax;
        int clr = 255;
        int clrRed = 255;
        int clrBlue = 127;
        int clrGreen = 255;

        int imageOne[][][] = new int[4][width][height];

        //create buffered image object img
        BufferedImage img0 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img11 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img12 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img13 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img4 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        //create clear background
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (0 * clr); //alpha
                int r = (int) (255); //red
                int g = (int) (255); //green
                int b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img0.setRGB(x, y, p);
                img1.setRGB(x, y, p);
                img11.setRGB(x, y, p);
                img12.setRGB(x, y, p);
                img13.setRGB(x, y, p);
                img.setRGB(x, y, p);
                img3.setRGB(x, y, p);
                img2.setRGB(x, y, p);
            }
        }

        int halfRadMax = (int) radMax/2;
        // first create star shine
        // diameter of shine is equal to width of sprite and alpha fades with distance from centre
        for(int rad = halfRadMax/2;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<numTheta;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/numTheta));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/numTheta));
                //System.out.println("X "+X+" Y "+Y);
                double aFloat = (4/3)*(1 - (rad/radMax));
                int a = (int) (aFloat * 255); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (1 * 127); //green
                int b = (int) (1 * 0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img0.setRGB(X, Y, p);
            }
        }

        Perlin perlin = new Perlin();
        float aArray[][] = new float[width][height];
        float aArray2[][] = new float[width][height];

        // first array for something
        aArray = perlin.perlinFun(perlin,game,width,height, 1, 1, 1, 1, 3, 1, 7);
        aArray2 = perlin.perlinFun(perlin,game,width,height, 1, 1, 1, 1, 3, 1, 7);

        // halve radmax for star itself
        radMax /= 2;
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<numTheta;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/numTheta));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/numTheta));
                int colG = (int) (157 + 100*aArray[X][Y]);
                int colB = (int) (30 + 70*aArray[X][Y]);
                int a = (int) (1 * 255); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (colG); //green
                int b = (int) (colB); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
            }
        }

        // this section does star shine over the main star body
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<numTheta;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/numTheta));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/numTheta));
                int alpha = (int) (255*Math.pow(((aArray2[X][Y])),2.25));
                int a = (int) (alpha); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (255); //green
                int b = (int) (200); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                //img2.setRGB(X, Y, p);
            }
        }

        float aArray3[][] = new float[numTheta][1];
        float aArray4[][] = new float[numTheta][1];

        // first array for something
        aArray3 = perlin.perlinFun(perlin,game,numTheta,1, 1, 1, 1, 1, 1, 1, 3);
        aArray4 = perlin.perlinFun(perlin,game,numTheta,1, 1, 1, 1, 1, 1, 1, 3);
        int tDiv = 64;  // thickness divisor
        float aArray5[][] = new float[numTheta][1];
        float aArray6[][] = new float[numTheta][1];

        // first array for something
        aArray5 = perlin.perlinFun(perlin,game,numTheta,1, 1, 1, 1, 1, 1, 1, 7);
        aArray6 = perlin.perlinFun(perlin,game,numTheta,1, 1, 1, 1, 1, 1, 1, 5);

        for(int theta=0;theta<numTheta;theta++) {
            double radBelow = radMax/tDiv + aArray3[theta][0]*radMax/tDiv;
            double radAbove = radMax/tDiv + aArray4[theta][0]*radMax/tDiv;
            double startRad = radMax - radBelow;
            double endRad = radMax + radAbove;
            int coronaWidth = (int) radMax / 8;
            double radMax2 = coronaWidth / 2 + coronaWidth / 2 * Math.random();
            // yellow ring on edge of sun
            for (int rad = (int) startRad; rad < endRad; rad++) {
                X = midX + (int) (rad * Math.cos(theta * 2 * Math.PI / numTheta));
                Y = midY + (int) (rad * Math.sin(theta * 2 * Math.PI / numTheta));
                double currentCoronaWidth = rad - startRad;
                int alpha = (int) (255 * (1 - 0.5 * currentCoronaWidth / coronaWidth));
                if (rad > (radMax - startRad) / 2) {
                    if (Math.random() > 0.5) {
                        alpha = 0;
                    } else {
                        alpha = 255;
                    }
                } else {
                    alpha = 255;
                }
                int colG = (int) (151 - 75 * (currentCoronaWidth / coronaWidth));
                int colB = (int) (151 - 2 * 75 * (currentCoronaWidth / coronaWidth));
                int a = (int) (0.85*255);//alpha); //alpha
                int r = (int) (255); //red
                int g = (int) (255);//colG); //green
                int b = (int) (75);//colB); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img1.setRGB(X, Y, p);
            }
        }

        tDiv = 12;

        for(int theta=0;theta<numTheta;theta++) {
            // orange ring outside not necessarily in contact
            int thickness = (int) ((aArray6[theta][0]-0.4) * radMax / tDiv);
            int midPoint = (int) (radMax + radMax/(2*tDiv) + aArray5[theta][0] * radMax / tDiv);
            int startRad = midPoint - thickness;
            int endRad = midPoint + thickness;
            if(thickness>0) {
                for (int rad = (int) startRad; rad < endRad; rad++) {
                    X = midX + (int) (rad * Math.cos(theta * 2 * Math.PI / numTheta));
                    Y = midY + (int) (rad * Math.sin(theta * 2 * Math.PI / numTheta));
                    double currentCoronaWidth = rad - startRad;
                    int a = (int) (0.5 * 255);//alpha); //alpha
                    int r = (int) (255); //red
                    int g = (int) (127);//colG); //green
                    int b = (int) (0);//colB); //blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img11.setRGB(X, Y, p);
                }
            }
        }

        float aArray7[][] = new float[width][height];
        float aArray8[][] = new float[width][height];

        // first array for something
        aArray7 = perlin.perlinFun(perlin,game,width,height, 1, 1, 1, 1, 1, 1, 6);
        aArray8 = perlin.perlinFun(perlin,game,width,height, 1, 1, 1, 1, 1, 1, 6);

        // put see through orange blobs on the star for texture
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<numTheta;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/numTheta));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/numTheta));
                int a;
                if(aArray7[X][Y]>0.55){
                    a = (int) (0.45 * 255); //alpha
                } else {
                    a = 0;
                }
                int r = (int) (255); //red
                int g = (int) (127); //green
                int b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img12.setRGB(X, Y, p);
            }
        }

        // put see through white blobs on the star for texture
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<numTheta;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/numTheta));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/numTheta));
                int a;
                if(aArray7[X][Y]>0.8){
                    a = (int) (0.8 * 255); //alpha
                } else {
                    a = 0;
                }
                int r = (int) (255); //red
                int g = (int) (255); //green
                int b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img13.setRGB(X, Y, p);
            }
        }

        // paint both images, preserving the alpha channels
        Graphics g1 = img3.getGraphics();
        g1.drawImage(img0, 0, 0, null);
        g1.drawImage(img, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);
        g1.drawImage(img1, 0, 0, null);
        g1.drawImage(img11, 0, 0, null);
        g1.drawImage(img12, 0, 0, null);
        g1.drawImage(img13, 0, 0, null);


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = img3.getRGB(i, j);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                imageOne[0][i][j] = alpha;
                imageOne[1][i][j] = red;
                imageOne[2][i][j] = green;
                imageOne[3][i][j] = blue;
            }
        }

        //write image
        try {
            f = new File("C:\\Image\\stars\\star"+count+" 1.png");
            ImageIO.write(img3, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        return imageOne;

    }
}
