package com.mygdx.game.Screens.galScreen.Tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class galaxyGenerator {

    public galaxyGenerator() {

        //image dimension
        int width = 1200;
        int height = 1200;
        //double rad = radMax;
        int clr = 255;
        int clrRed = 1;
        int clrBlue = 1;
        int clrGreen = 1;

        int pointsPerEdge = 500;
        int inbets = 500 / pointsPerEdge;

        double[][] stars = new double[2][2000];
        for(int i=0;i<stars[0].length;i++){
            System.out.println("random number "+Math.random());
            stars[0][i] = (int) 1000*Math.random();
            stars[1][i] = (int) 1000*Math.random();
        }
        /*
        for(int i=0;i<pointsPerEdge*2;i++){
            stars[0][i] = inbets*(1+i);
            stars[1][i] = 500;
        }

        for(int i=0;i<pointsPerEdge*2;i++){
            stars[0][i+1000] = 500;
            stars[1][i+1000] = inbets*(1+i);
        }
*/
        double thetaMid = 40*Math.PI/180;
        int radMax = 500;
        for(int i=0;i<stars[0].length;i++){
            //double thetaRad = thetaMid - thetaMid*curRad/radMax;
            double thetaNew;/*
            if(stars[0][i]>0 && stars[1][i]>0){
                double thetaTotal = Math.atan(stars[1][i]/stars[0][i]);
                thetaNew = thetaTotal - thetaMid;
            } else if(stars[0][i]<0 && stars[1][i]<0) {
                double thetaTotal = Math.atan(stars[1][i]/stars[0][i]);
                thetaNew = thetaTotal - thetaMid;
            } else {
                double thetaOld = Math.atan(stars[1][i]/stars[0][i]);
                thetaNew = thetaOld + thetaMid;
            }*/
            double curRad = Math.sqrt(Math.pow((stars[0][i]-500),2) + Math.pow((stars[1][i]-500),2));
            System.out.println("curRad "+curRad);
            double thetaRad = thetaMid - thetaMid*curRad/radMax;
            stars[0][i] = Math.cos(thetaRad) * (stars[0][i] - 500) - Math.sin(thetaRad) * (stars[1][i] - 500) + 500 + 100;
            stars[1][i] = Math.sin(thetaRad) * (stars[0][i] - 500) + Math.cos(thetaRad) * (stars[1][i] - 500) + 500 + 100;
            /*stars[0][i] = (500+radMax*Math.sin(thetaNew));
            stars[1][i] = (500+radMax*Math.cos(thetaNew));*/
        }




/*
        for(int i=0;i<stars[0].length;i++){
            if(i<stars[0].length/2) {
                stars[0][i] = i + 5;
                stars[1][i] = stars[0][i];
            } else {
                stars[0][i] = 0;
                stars[1][i] = 0;
            }
        }*/



        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        //create random image pixel by pixel
        for (int i = 0; i < stars[0].length; i++) {
            int x = (int) stars[0][i];
            int y = (int) stars[1][i];

            System.out.println("FROM GAL GEN " + i + " coords " + x + " by " + y);
            int a = (int) (1 * clr); //alpha
            int r = (int) (1 * clrRed); //red
            int g = (int) (1 * clrGreen); //green
            int b = (int) (1 * clrBlue); //blue

            int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

            if (x < 0) {
                x=0;
            }
            if (y < 0) {
                y=0;
            }
            img.setRGB(x, y, p);
        }
/*
        double thetaMid = 20*Math.PI/180;
        int radMax = 500;
        for (int i = 0; i < stars[0].length; i++) {
            System.out.println("before X "+stars[0][i]+" Y "+stars[1][i]);
            double curRad = Math.sqrt(Math.pow((stars[0][i]),2) + Math.pow((stars[1][i]),2));
            System.out.println("curRad "+curRad);
            double thetaRad = thetaMid - thetaMid*curRad/radMax;
            System.out.println("thetaRad "+thetaRad);
            stars[0][i] = (500+curRad*Math.sin(thetaRad));
            stars[1][i] = (500+curRad*Math.cos(thetaRad));
            System.out.println("after X "+stars[0][i]+" Y "+stars[1][i]);
        }*/


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
