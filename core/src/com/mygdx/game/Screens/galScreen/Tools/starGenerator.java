package com.mygdx.game.Screens.galScreen.Tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class starGenerator {

    public starGenerator() {

        //image dimension
        int width = 500;
        int height = 500;
        double radMax = height * 0.4f;
        int midX = width / 2;
        int midY = height / 2;
        int X;
        int Y;
        //double rad = radMax;
        int clr = 255;
        int clrRed = 255;
        int clrBlue = 127;
        int clrGreen = 255;

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int a = (int) (1 * clr); //alpha
                int r = (int) (0 * clrRed); //red
                int g = (int) (0 * clrGreen); //green
                int b = (int) (0 * clrBlue); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(x, y, p);
            }
        }

        for(int rad = 1;rad<radMax;rad++){
            // for each r
            for(int theta=0;theta<2000;theta++){
                X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
                Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
                //System.out.println("X "+X+" Y "+Y);
                int a = (int) (1 * 255); //alpha
                int r = (int) (1 * clrRed); //red
                int g = (int) (1 * clrGreen); //green
                int b = (int) (1 * clrBlue); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img.setRGB(X, Y, p);
            }
            if(rad>radMax/2){
                clr++;
                clrRed++;
                clrGreen++;
                clrBlue++;
                clrBlue++;
            } else {
                clr--;
                clrRed--;
                clrGreen--;
                clrBlue--;
            }
        }

        double rad = radMax;
        clr=255;
        clrRed = 255;
        clrBlue = 255;
        clrGreen = 255;

        for(int theta=0;theta<2000;theta++){
            X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
            Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
            double rando = Math.random();
            if(rando<0.25){
                X++;
            } else if(rando<0.5){
                X--;
            } else if(rando<0.75){
                Y++;
            } else {
                Y--;
            }
            //System.out.println("X "+X+" Y "+Y);
            int a = (int) (1 * 255); //alpha
            int r = (int) (1 * clrRed); //red
            int g = (int) (1 * clrGreen); //green
            int b = (int) (1 * clrBlue); //blue

            int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

            img.setRGB(X, Y, p);
        }

        clr=255;
        clrRed = 255;
        clrBlue = 255;
        clrGreen = 255;

        for(int theta=0;theta<2000;theta++){
            X = midX + (int) (rad*Math.cos(theta*2*Math.PI/2000));
            Y = midY + (int) (rad*Math.sin(theta*2*Math.PI/2000));
            double rando = Math.random();
            if(rando<0.25){
                X++;
                X++;
            } else if(rando<0.5){
                X--;
                X--;
            } else if(rando<0.75){
                Y++;
                Y++;
            } else {
                Y--;
                Y--;
            }
            //System.out.println("X "+X+" Y "+Y);
            int a = (int) (1 * 255); //alpha
            int r = (int) (1 * clrRed); //red
            int g = (int) (1 * clrGreen); //green
            int b = (int) (1 * clrBlue); //blue

            int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

            img.setRGB(X, Y, p);
        }

        // horizontal starshine
        // mid line
            int length = (int) (width*0.95 - 0*width*0.9/3);
            int startX = (width - length)/2;

            int a = (int) (1 * 255); //alpha
            int r = (int) (1 * clrRed); //red
            int g = (int) (1 * clrGreen); //green
            int b = (int) (1 * clrBlue); //blue

            int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

            for(int Xcount=startX;Xcount<startX+length;Xcount++){
                img.setRGB(Xcount, midY, p);
            }
        // next mid lines
        length = (int) (width*0.95 - 1*width*0.9/3);
        startX = (width - length)/2;

        p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

        for(int Xcount=startX;Xcount<startX+length;Xcount++){
            img.setRGB(Xcount, midY+1, p);
            img.setRGB(Xcount, midY-1, p);
        }
        // Final mid lines
        length = (int) (width*0.95 - 2*width*0.9/3);
        startX = (width - length)/2;

        p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

        for(int Xcount=startX;Xcount<startX+length;Xcount++){
            img.setRGB(Xcount, midY+2, p);
            img.setRGB(Xcount, midY-2, p);
        }


        // horizontal starshine
        // mid line
        length = (int) (height*0.95 - 0*height*0.9/3);
        int startY = (height - length)/2;

        p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

        for(int Ycount=startY;Ycount<startY+length;Ycount++){
            img.setRGB(midX, Ycount, p);
        }
        // next mid lines
        length = (int) (height*0.95 - 1*height*0.9/3);
        startY = (height - length)/2;

        p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

        for(int Ycount=startY;Ycount<startY+length;Ycount++){
            img.setRGB(midX+1, Ycount, p);
            img.setRGB(midX-1, Ycount, p);
        }
        // Final mid lines
        length = (int) (height*0.95 - 2*height*0.9/3);
        startY = (height - length)/2;

        p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

        for(int Ycount=startY;Ycount<startY+length;Ycount++){
            img.setRGB(midX+2, Ycount, p);
            img.setRGB(midX-2, Ycount, p);
        }



        //write image
        try {
            f = new File("D:\\Image\\Output.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
