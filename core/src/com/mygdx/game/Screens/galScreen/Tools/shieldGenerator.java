package com.mygdx.game.Screens.galScreen.Tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class shieldGenerator {

    public shieldGenerator() {

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
        int clrRed = 0;
        int clrBlue = 165;
        int clrGreen = 255;

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (0 * clr); //alpha
                int r = (int) (0 * clrRed); //red
                int g = (int) (0 * clrGreen); //green
                int b = (int) (0 * clrBlue); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        for(int rad = 1;rad<radMax;rad++){
            // for each r
            double clearness = 255*Math.pow((rad/radMax),5);
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                //System.out.println("X "+X+" Y "+Y);
                int a = (int) (1 * clearness); //alpha
                int r = (int) (1 * clrRed); //red
                int g = (int) (1 * clrGreen); //green
                int b = (int) (1 * clrBlue); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
            }
        }


        //write image
        try {
            f = new File("C:\\Image\\Output.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
