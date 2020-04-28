package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class backgroundGenerator4 {

    MyGdxGame game;

    public backgroundGenerator4() {

        //image dimension
        int width = 1500;
        int height = 1500;
        int clr = 255;
        int clrRem = 255 - clr;
        int clrRed = 225;
        int clrBlue = 225;
        int clrGreen = 225;
        int aMin = 0;
        double pow = 1;
        double pow2 = 7;

        float aArray[][] = new float[width][height];
        float a2Array[][] = new float[width][height];

        float a3Array[][] = new float[width][height];
        float a4Array[][] = new float[width][height];

        float a5Array[][] = new float[width][height];
        float a6Array[][] = new float[width][height];

        float a7Array[][] = new float[width][height];
        float a8Array[][] = new float[width][height];

        float a9Array[][] = new float[width][height];
        float a10Array[][] = new float[width][height];

        float a11Array[][] = new float[width][height];
        float a12Array[][] = new float[width][height];

        float a13Array[][] = new float[width][height];
        float a14Array[][] = new float[width][height];

        float a15Array[][] = new float[width][height];
        float a16Array[][] = new float[width][height];

        int aoct1 = 2;
        int aoct2 = 3;
        int aoct3 = 4;
        int aoct4 = 5;
        int aoct5 = 1;
        int aoct6 = 1;
        int aoct7 = 6;

        int a2oct1 = 1;
        int a2oct2 = 2;
        int a2oct3 = 3;
        int a2oct4 = 1;
        int a2oct5 = 1;
        int a2oct6 = 1;
        int a2oct7 = 6;
/*
        int aoct1 = 1;
        int aoct2 = 1;
        int aoct3 = 2;
        int aoct4 = 1;
        int aoct5 = 2;
        int aoct6 = 1;
        int aoct7 = 7;

        int a2oct1 = 7;
        int a2oct2 = 1;
        int a2oct3 = 3;
        int a2oct4 = 1;
        int a2oct5 = 3;
        int a2oct6 = 1;
        int a2oct7 = 4;
        */

        int oct1 = 1;
        int oct2 = 1;
        int oct3 = 1;
        int oct4 = 1;
        int oct5 = 3;
        int oct6 = 1;
        int oct7 = 8;

        Perlin perlin = new Perlin();

        aArray = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a2Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);


        a3Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a4Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a5Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a6Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a7Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a8Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a9Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a10Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a11Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a12Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a13Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a14Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a15Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a16Array = perlin.perlinFun(perlin,game,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 1 - dark purple
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(aArray[x][y],pow)) ;//* Math.pow(a2Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (58); //red
                g = (int) (0); //green
                b = (int) (130); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img1.setRGB(x, y, p);
            }
        }

        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 2 - dark blue
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a3Array[x][y],pow) * Math.pow(a4Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (4); //red
                g = (int) (0); //green
                b = (int) (130); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img2.setRGB(x, y, p);
            }
        }

        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 3 - cyan
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a5Array[x][y],pow) * Math.pow(a6Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (0); //red
                g = (int) (216); //green
                b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                //System.out.println("p is now "+p);

                img3.setRGB(x, y, p);
            }
        }

        BufferedImage img4 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 4 - light green
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a7Array[x][y],pow) * Math.pow(a8Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (7); //red
                g = (int) (255); //green
                b = (int) (127); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                //System.out.println("p is now "+p);

                img4.setRGB(x, y, p);
            }
        }

        BufferedImage img5 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 5 - light brown
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a9Array[x][y],pow) * Math.pow(a10Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (255); //red
                g = (int) (157); //green
                b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                //System.out.println("p is now "+p);

                img5.setRGB(x, y, p);
            }
        }

        BufferedImage img6 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 6 - red
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a11Array[x][y],pow) * Math.pow(a12Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (255); //red
                g = (int) (38); //green
                b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                //System.out.println("p is now "+p);

                img6.setRGB(x, y, p);
            }
        }

        BufferedImage img7 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 7 - white
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a13Array[x][y],pow) * Math.pow(a14Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (255); //red
                g = (int) (255); //green
                b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img7.setRGB(x, y, p);
            }
        }

        BufferedImage img8 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 8 - white AGAIN SONNNNN
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a15Array[x][y],pow) * Math.pow(a16Array[x][y],pow2));
                if(temp<0){
                    //temp = Math.pow(temp,2);
                } else {
                    //temp = Math.sqrt(temp);
                }
                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }
                /*if(a<50){
                    a=0;
                }
                if(a>225){
                    a=0;
                }*/
                int r;
                int g;
                int b;
                r = (int) (255); //red
                g = (int) (255); //green
                b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img8.setRGB(x, y, p);
            }
        }

        BufferedImage img9 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int noStars = 100;
        // make random stars across galwidth and galheight
        while(noStars>0){

            int X = (int) (width*Math.random());
            int Y = (int) (height*Math.random());

            clrRed = (int) 255;
            clrGreen = (int) 255;
            clrBlue = (int) 225;
            int a;
            int r;
            int g;
            int b;

            for (int y = 0; y < 2; y++) {
                for (int x = 0; x < 2; x++) {
                    // calculate distance from (0,3) in grid
                    double rad = Math.sqrt(Math.pow((x - 2), 2) + Math.pow((y - 3), 2));
                    // distance from (3,4) in grid to (5,0) where its planned for star to be dimmest
                    a = 255;//(int) (255 - (100 * rad / baseRad));
                    if (x == 0) {
                        if (y == 0) {
                            a = 0;
                        } else if (y == height - 1) {
                            a = 0;
                        }
                    } else if (x == width - 1) {
                        if (y == 0) {
                            a = 0;
                        } else if (y == height - 1) {
                            a = 0;
                        }
                    }
                    r = (int) clrRed;//(255-(130*rad/baseRad)); // blue
                    g = (int) clrGreen;//(255-(130*rad/baseRad)); // blue
                    b = (int) (clrBlue);// - (clrBlue * 0.25 * rad / baseRad)); // blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                    //System.out.println("p is now "+p);

                    try {
                        img9.setRGB(X + x, Y + y, p);
                    } catch (Exception e) {
                        // probably out of bounds, who cares
                    }
                }
            }

            noStars--;
        }


        // paint both images, preserving the alpha channels
        Graphics g1 = img.getGraphics();
        g1.drawImage(img9, 0, 0, null);
        g1.drawImage(img1, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);
        g1.drawImage(img3, 0, 0, null);
        g1.drawImage(img4, 0, 0, null);
        g1.drawImage(img5, 0, 0, null);
        g1.drawImage(img6, 0, 0, null);
        g1.drawImage(img7, 0, 0, null);
        g1.drawImage(img8, 0, 0, null);


        int filecount=2;

        //write image
        try {
            f = new File("C:\\Image\\background"+filecount+".png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        width = 6;
        height = 6;


        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // calculate distance from (0,3) in grid
                double rad = Math.sqrt(Math.pow((x-2),2)+Math.pow((y-3),2));
                // distance from (3,4) in grid to (5,0) where its planned for star to be dimmest
                double baseRad = Math.sqrt(Math.pow((5-2),2)+Math.pow((0-3),2));
                int a = (int) (255-(100*rad/baseRad));
                if(x==0){
                    if(y==0){
                        a=0;
                    } else if(y==height-1){
                        a=0;
                    }
                } else if(x==width-1){
                    if(y==0){
                        a=0;
                    } else if(y==height-1){
                        a=0;
                    }
                }
                int r;
                int g;
                int b;
                r = (int) 255;//(255-(130*rad/baseRad)); // blue
                g = (int) 255;//(255-(130*rad/baseRad)); // blue
                b = (int) (255-(225*rad/baseRad)); // blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                //System.out.println("p is now "+p);

                img9.setRGB(x, y, p);
            }
        }

        filecount=2;

        //write image
        try {
            f = new File("C:\\Image\\star"+filecount+".png");
            ImageIO.write(img7, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }



}
