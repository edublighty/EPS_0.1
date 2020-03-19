package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.Perlin.OpenSimplexNoise;
import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class backgroundGeneratorSimplex {

    private static final double FEATURE_SIZE = 48;

    public backgroundGeneratorSimplex() {

        //image dimension
        int width = 500;
        int height = 500;
        int clr = 255;
        int clrRem = 255 - clr;
        int clrRed = 225;
        int clrBlue = 225;
        int clrGreen = 225;
        int aMin = 0;
        double pow = 1;
        float rArray[][] = new float[width][height];
        float gArray[][] = new float[width][height];
        float bArray[][] = new float[width][height];
        float aArray[][] = new float[width][height];

        int aoct1 = 3;
        int aoct2 = 1;
        int aoct3 = 1;
        int aoct4 = 1;
        int aoct5 = 1;
        int aoct6 = 1;
        int aoct7 = 1;

        int oct1 = 1;
        int oct2 = 1;
        int oct3 = 1;
        int oct4 = 1;
        int oct5 = 3;
        int oct6 = 1;
        int oct7 = 8;

        Perlin perlin = new Perlin();
        rArray = perlin.perlinFun(perlin,width,height, oct1, oct2, oct3, oct4, oct5, oct6, oct7);
        gArray = perlin.perlinFun(perlin,width,height, oct1, oct2, oct3, oct4, oct5, oct6, oct7);
        bArray = perlin.perlinFun(perlin,width,height, oct1, oct2, oct3, oct4, oct5, oct6, oct7);
        aArray = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        OpenSimplexNoise noise = new OpenSimplexNoise();
        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double value = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE);
                int a = (int) ((value) * clr) - aMin; //alpha
                if(a<0){
                    a=0;
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
                r = (int) (clrRed); //red
                g = (int) (1); //green
                b = (int) (1); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img1.setRGB(x, y, p);
            }
        }

        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (aArray[x][y] * clr) - aMin; //alpha
                if(a<0){
                    a=0;
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
                r = (int) (1); //red
                g = (int) (clrGreen); //green
                b = (int) (1); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img2.setRGB(x, y, p);
            }
        }

        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (aArray[x][y] * clr) - aMin; //alpha
                if(a<0){
                    a=0;
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
                r = (int) (1); //red
                g = (int) (1); //green
                b = (int) (clrBlue); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img3.setRGB(x, y, p);
            }
        }

        // paint both images, preserving the alpha channels
        Graphics g = img.getGraphics();
        g.drawImage(img1, 0, 0, null);
        g.drawImage(img2, 0, 0, null);
        g.drawImage(img3, 0, 0, null);

        int filecount=2;

        //write image
        try {
            f = new File("C:\\Image\\background"+filecount+".png");
            ImageIO.write(img1, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }



}
