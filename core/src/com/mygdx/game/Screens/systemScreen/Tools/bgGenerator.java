package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;
import com.mygdx.game.Perlin.Perlin2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class bgGenerator {

    public bgGenerator(MyGdxGame game) {

        //image dimension
        int width = 5000;
        int height = 5000;
        int clr = 225;
        int clrRem = 255 - clr;
        int clrRed = 20;
        int clrBlue = 20;
        int clrGreen = 255;
        float rArray[][] = new float[width][height];
        float gArray[][] = new float[width][height];
        float bArray[][] = new float[width][height];
        float aArray[][] = new float[width][height];

        int aoct1 = 1;
        int aoct2 = 2;
        int aoct3 = 3;
        int aoct4 = 4;
        int aoct5 = 6;
        int aoct6 = 8;
        int aoct7 = 10;

        int oct1 = 1;
        int oct2 = 1;
        int oct3 = 2;
        int oct4 = 3;
        int oct5 = 3;
        int oct6 = 4;
        int oct7 = 5;

        Perlin perlin = new Perlin();
        rArray = perlin.perlinFun(perlin,game,width,height, oct1, oct2, oct3, oct4, oct5, oct6, oct7);
        gArray = perlin.perlinFun(perlin,game,width,height, oct1, oct2, oct3, oct4, oct5, oct6, oct7);
        bArray = perlin.perlinFun(perlin,game,width,height, oct1, oct2, oct3, oct4, oct5, oct6, oct7);
        aArray = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (aArray[x][y] * clr+clrRem); //alpha
                int r;
                int g;
                int b;
                r = (int) (rArray[x][y] * clrRed); //red
                g = (int) (gArray[x][y] * clrGreen); //green
                b = (int) (bArray[x][y] * clrBlue); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        int filecount=2;

        //write image
        try {
            f = new File("D:\\Image\\background"+filecount+".png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }



}
