package com.mygdx.game.Screens.galScreen.Tools;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class starGenerator3 {

    private static MyGdxGame game;

    public starGenerator3() {

        //image dimension
        int width = 500;
        int height = 500;
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
        int imageTwo[][][] = new int[4][width][height];
        int imageChanges[][][] = new int[4][width][height];

        //create buffered image object img
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

                img.setRGB(x, y, p);
                imageOne[0][x][y] = a;
                imageOne[1][x][y] = r;
                imageOne[2][x][y] = g;
                imageOne[3][x][y] = b;
            }
        }

        // first create star shine
        // diameter of shine is equal to width of sprite and alpha fades with distance from centre
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                //System.out.println("X "+X+" Y "+Y);
                double aFloat = 1 - (rad/radMax);
                int a = (int) (aFloat * 255); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (1 * 127); //green
                int b = (int) (1 * 0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
                imageOne[0][X][Y] = a;
                imageOne[1][X][Y] = r;
                imageOne[2][X][Y] = g;
                imageOne[3][X][Y] = b;
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
        int[][] hotPoints = new int[2][3];
        hotPoints[0][0] = (int) ( midX + 3*radMax/4);   // x coordinate of hot point
        hotPoints[1][0] = (int) (midY + radMax/4);   // y coordinate of hot point
        hotPoints[0][1] = (int) ( midX - radMax/4);   // x coordinate of hot point
        hotPoints[1][1] = (int) (midY + radMax/6);   // y coordinate of hot point
        hotPoints[0][2] = (int) ( midX - radMax/4);   // x coordinate of hot point
        hotPoints[1][2] = (int) (midY - radMax/4);   // y coordinate of hot point
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                /*double sumDistancesSqrd = 0;
                int hotPointsCount = 0;
                for(int i=0; i<hotPoints[0].length; i++){
                    double tempRadSqrd = Math.pow((X-hotPoints[0][i]),2) + Math.pow((Y-hotPoints[1][i]),2);
                    sumDistancesSqrd += 1/Math.sqrt(tempRadSqrd);
                    hotPointsCount++;
                }
                //double multiplier = Math.sqrt((sumDistancesSqrd/4))/radMax;*/
                int colG = (int) (0 + 130*aArray[X][Y]);
                int a = (int) (1 * 255); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (colG); //green
                int b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
                imageOne[0][X][Y] = a;
                imageOne[1][X][Y] = r;
                imageOne[2][X][Y] = g;
                imageOne[3][X][Y] = b;
            }
        }

        // this section does star shine over the main star body
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                /*double sumDistancesSqrd = 0;
                int hotPointsCount = 0;
                for(int i=0; i<hotPoints[0].length; i++){
                    double tempRadSqrd = Math.pow((X-hotPoints[0][i]),2) + Math.pow((Y-hotPoints[1][i]),2);
                    sumDistancesSqrd += 1/Math.sqrt(tempRadSqrd);
                    hotPointsCount++;
                }
                //double multiplier = Math.sqrt((sumDistancesSqrd/4))/radMax;*/
                int alpha = (int) (255*Math.pow(((aArray2[X][Y])),2.25));
                int a = (int) (alpha); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (255); //green
                int b = (int) (200); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img2.setRGB(X, Y, p);
                imageOne[0][X][Y] = a;
                imageOne[1][X][Y] = r;
                imageOne[2][X][Y] = g;
                imageOne[3][X][Y] = b;
            }
        }

        // now the corona
        // has a width of starWidth/8 i.e. new radmax/8
        int coronaWidth = (int) radMax/50;
        int startRad = (int) radMax - coronaWidth;
        for(int rad = startRad;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                float currentCoronaWidth = rad - startRad;
                int alpha = (int) (255*(1-0.5*currentCoronaWidth/coronaWidth));
                int colG = (int) (151 - 75*(currentCoronaWidth/coronaWidth));
                int colB = (int) (151 - 2*75*(currentCoronaWidth/coronaWidth));
                int a = (int) (alpha);//alpha); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (colG);//colG); //green
                int b = (int) (colB);//colB); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
                imageOne[0][X][Y] = a;
                imageOne[1][X][Y] = r;
                imageOne[2][X][Y] = g;
                imageOne[3][X][Y] = b;
            }
        }

        // paint both images, preserving the alpha channels
        Graphics g1 = img3.getGraphics();
        g1.drawImage(img, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);

        //write image
        try {
            f = new File("C:\\Image\\stars\\star0.png");
            ImageIO.write(img3, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                int pixel = img3.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                imageOne[0][x][y] = alpha;
                imageOne[1][x][y] = red;
                imageOne[2][x][y] = green;
                imageOne[3][x][y] = blue;
            }
        }


        //////////////////////////////////////////////////////////////////
        // Create second star

        radMax = height * 0.49f;

        //create buffered image object img
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        f = null;

        //create clear background
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (0 * clr); //alpha
                int r = (int) (255); //red
                int g = (int) (255); //green
                int b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
                imageTwo[0][x][y] = a;
                imageTwo[1][x][y] = r;
                imageTwo[2][x][y] = g;
                imageTwo[3][x][y] = b;
            }
        }

        // first create star shine
        // diameter of shine is equal to width of sprite and alpha fades with distance from centre
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                //System.out.println("X "+X+" Y "+Y);
                double aFloat = 1 - (rad/radMax);
                int a = (int) (aFloat * 255); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (1 * 127); //green
                int b = (int) (1 * 0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
                imageTwo[0][X][Y] = a;
                imageTwo[1][X][Y] = r;
                imageTwo[2][X][Y] = g;
                imageTwo[3][X][Y] = b;
            }
        }

        perlin = new Perlin();

        // first array for something
        aArray = perlin.perlinFun(perlin,game,width,height, 1, 1, 1, 1, 3, 1, 7);
        aArray2 = perlin.perlinFun(perlin,game,width,height, 1, 1, 1, 1, 3, 1, 7);

        // halve radmax for star itself
        radMax /= 2;
        hotPoints = new int[2][3];
        hotPoints[0][0] = (int) ( midX + 3*radMax/4);   // x coordinate of hot point
        hotPoints[1][0] = (int) (midY + radMax/4);   // y coordinate of hot point
        hotPoints[0][1] = (int) ( midX - radMax/4);   // x coordinate of hot point
        hotPoints[1][1] = (int) (midY + radMax/6);   // y coordinate of hot point
        hotPoints[0][2] = (int) ( midX - radMax/4);   // x coordinate of hot point
        hotPoints[1][2] = (int) (midY - radMax/4);   // y coordinate of hot point
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                /*double sumDistancesSqrd = 0;
                int hotPointsCount = 0;
                for(int i=0; i<hotPoints[0].length; i++){
                    double tempRadSqrd = Math.pow((X-hotPoints[0][i]),2) + Math.pow((Y-hotPoints[1][i]),2);
                    sumDistancesSqrd += 1/Math.sqrt(tempRadSqrd);
                    hotPointsCount++;
                }
                //double multiplier = Math.sqrt((sumDistancesSqrd/4))/radMax;*/
                int colG = (int) (0 + 130*aArray[X][Y]);
                int a = (int) (1 * 255); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (colG); //green
                int b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
                imageTwo[0][X][Y] = a;
                imageTwo[1][X][Y] = r;
                imageTwo[2][X][Y] = g;
                imageTwo[3][X][Y] = b;
            }
        }

        // this section does star shine over the main star body
        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                /*double sumDistancesSqrd = 0;
                int hotPointsCount = 0;
                for(int i=0; i<hotPoints[0].length; i++){
                    double tempRadSqrd = Math.pow((X-hotPoints[0][i]),2) + Math.pow((Y-hotPoints[1][i]),2);
                    sumDistancesSqrd += 1/Math.sqrt(tempRadSqrd);
                    hotPointsCount++;
                }
                //double multiplier = Math.sqrt((sumDistancesSqrd/4))/radMax;*/
                int alpha = (int) (255*Math.pow(((aArray2[X][Y])),2.25));
                int a = (int) (alpha); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (255); //green
                int b = (int) (200); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img2.setRGB(X, Y, p);
                imageTwo[0][X][Y] = a;
                imageTwo[1][X][Y] = r;
                imageTwo[2][X][Y] = g;
                imageTwo[3][X][Y] = b;
            }
        }

        // now the corona
        // has a width of starWidth/8 i.e. new radmax/8
        coronaWidth = (int) radMax/50;
        startRad = (int) radMax - coronaWidth;
        for(int rad = startRad;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                float currentCoronaWidth = rad - startRad;
                int alpha = (int) (255*(1-0.5*currentCoronaWidth/coronaWidth));
                int colG = (int) (151 - 75*(currentCoronaWidth/coronaWidth));
                int colB = (int) (151 - 2*75*(currentCoronaWidth/coronaWidth));
                int a = (int) (alpha);//alpha); //alpha
                int r = (int) (1 * 255); //red
                int g = (int) (colG);//colG); //green
                int b = (int) (colB);//colB); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
                imageTwo[0][X][Y] = a;
                imageTwo[1][X][Y] = r;
                imageTwo[2][X][Y] = g;
                imageTwo[3][X][Y] = b;
            }
        }

        // paint both images, preserving the alpha channels
        g1 = img4.getGraphics();
        g1.drawImage(img, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);

        //write image
        try {
            f = new File("C:\\Image\\stars\\star90.png");
            ImageIO.write(img4, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                int pixel = img4.getRGB(x, y);
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                imageTwo[0][x][y] = alpha;
                imageTwo[1][x][y] = red;
                imageTwo[2][x][y] = green;
                imageTwo[3][x][y] = blue;
            }
        }


        for(int i=0; i<width; i++){
            for(int j=0; j<height; j++){

                imageChanges[0][i][j] = imageOne[0][i][j] - imageTwo[0][i][j];
                imageChanges[1][i][j] = imageOne[1][i][j] - imageTwo[1][i][j];
                imageChanges[2][i][j] = imageOne[2][i][j] - imageTwo[2][i][j];
                imageChanges[3][i][j] = imageOne[3][i][j] - imageTwo[3][i][j];

            }
        }

        ////////////////////////////////////////////////////////////////////////////////

        int imageCount = 5;
        int count =1;
        boolean weCanLeave = false;
        boolean starting = true;
        int imageCurrent[][][] = new int[4][width][height];

        for(int x=0;x<width;x++) {
            for (int y = 0; y < height; y++) {
                imageCurrent[0][x][y] = imageOne[0][x][y];
                imageCurrent[1][x][y] = imageOne[1][x][y];
                imageCurrent[2][x][y] = imageOne[2][x][y];
                imageCurrent[3][x][y] = imageOne[3][x][y];
            }
        }

        while(!weCanLeave){
            weCanLeave = true;

            BufferedImage img5 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for(int x=0;x<width;x++){
                for(int y=0;y<height;y++){

                    // alpha
                    if(imageCurrent[0][x][y]<imageTwo[0][x][y]){
                        imageCurrent[0][x][y]++;
                        weCanLeave = false;
                    } else if(imageCurrent[0][x][y]> imageTwo[0][x][y]){
                        imageCurrent[0][x][y]--;
                        weCanLeave = false;
                    } else {
                        // leave it son
                    }
                    int a = (int) (imageCurrent[0][x][y]);

                    // red
                    if(imageCurrent[1][x][y]<imageTwo[1][x][y]){
                        imageCurrent[1][x][y]++;
                        weCanLeave = false;
                    } else if(imageCurrent[1][x][y]> imageTwo[1][x][y]){
                        imageCurrent[1][x][y]--;
                        weCanLeave = false;
                    } else {
                        // leave it son
                    }
                    int r = (int) (imageCurrent[1][x][y]);

                    // green
                    if(imageCurrent[2][x][y]<imageTwo[2][x][y]){
                        imageCurrent[2][x][y]++;
                        weCanLeave = false;
                    } else if(imageCurrent[2][x][y]> imageTwo[2][x][y]){
                        imageCurrent[2][x][y]--;
                        weCanLeave = false;
                    } else {
                        // leave it son
                    }
                    int g = (int) (imageCurrent[2][x][y]);

                    // blue
                    if(imageCurrent[3][x][y]<imageTwo[3][x][y]){
                        imageCurrent[3][x][y]++;
                        weCanLeave = false;
                    } else if(imageCurrent[3][x][y]> imageTwo[3][x][y]){
                        imageCurrent[3][x][y]--;
                        weCanLeave = false;
                    } else {
                        // leave it son
                    }
                    int b = (int) (imageCurrent[3][x][y]);

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img5.setRGB(x, y, p);

                }

            }

            //write image
            try {
                f = new File("C:\\Image\\stars\\star"+count+".png");
                ImageIO.write(img5, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }
            count++;

        }

    }
}
