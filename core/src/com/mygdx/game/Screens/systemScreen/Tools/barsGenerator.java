package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.Perlin.Perlin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class barsGenerator {

    public barsGenerator() {

        for (int count = 0; count < 100; count++) {

            //image dimension
            int width = 500;
            int height = 50;
            int length = width*count/100;
            System.out.println("length "+length);
            int clr = 225;

            //create buffered image object img
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            //file object
            File f = null;

            //create blanks
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int a = (int) 0;//(aArray[x][y] * clr+clrRem); //alpha
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img.setRGB(x, y, p);
                }
            }

            int clrRed;
            int clrGreen;
            int clrBlue;

            if(count<15){
                clrGreen = 0;
                clrRed = 255;
                clrBlue = 0;
            } else if(count<35){
                clrGreen = 255;
                clrRed = 255;
                clrBlue = 0;
            } else {
                clrGreen = 255;
                clrRed = 0;
                clrBlue = 0;
            }

            //create bars
            for (int y = (0); y < (height); y++) {
                for (int x = 0; x < length; x++) {
                    int a = (int) 255;//(aArray[x][y] * clr+clrRem); //alpha
                    int r = clrRed;
                    int g = clrGreen;
                    int b = clrBlue;

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img.setRGB(x, y, p);
                }
            }

            int filecount = 2;

            //write image
            try {
                f = new File("C:\\Image\\damage" + count + ".png");
                ImageIO.write(img, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }

        }

    }


}
