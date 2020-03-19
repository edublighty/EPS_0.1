package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.Perlin.Perlin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class backgroundGenerator2 {

    public backgroundGenerator2() {

        //image dimension
        int width = 1500;
        int height = 1500;
        int clr = 255;
        int clrRem = 255 - clr;
        int clrRed = 225;
        int clrBlue = 225;
        int clrGreen = 225;
        int aMin = 0;
        double pow = 3;
        double pow2 = 8;

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

        int oct1 = 1;
        int oct2 = 1;
        int oct3 = 1;
        int oct4 = 1;
        int oct5 = 3;
        int oct6 = 1;
        int oct7 = 8;

        Perlin perlin = new Perlin();

        aArray = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a2Array = perlin.perlinFun(perlin,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a3Array = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a4Array = perlin.perlinFun(perlin,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a5Array = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a6Array = perlin.perlinFun(perlin,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a7Array = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a8Array = perlin.perlinFun(perlin,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a9Array = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a10Array = perlin.perlinFun(perlin,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        a11Array = perlin.perlinFun(perlin,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a12Array = perlin.perlinFun(perlin,width,height, a2oct1, a2oct2, a2oct3, a2oct4, a2oct5, a2oct6, a2oct7);

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 1 - dark purple
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(aArray[x][y],pow) * Math.pow(a2Array[x][y],pow2));
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
/*

        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int p1 = img1.getRGB(x,y);
                int alpha1 = (p1 >> 24) & 0xFF;
                int red1 = (p1 >> 16) & 0xFF;
                int green1 = (p1 >> 8) & 0xFF;
                int blue1 = p1 & 0xFF;

                int p2 = img2.getRGB(x,y);
                int alpha2 = (p2 >> 24) & 0xFF;
                int red2 = (p2 >> 16) & 0xFF;
                int green2 = (p2 >> 8) & 0xFF;
                int blue2 = p2 & 0xFF;

                int p3 = img3.getRGB(x,y);
                int alpha3 = (p3 >> 24) & 0xFF;
                int red3 = (p3 >> 16) & 0xFF;
                int green3 = (p3 >> 8) & 0xFF;
                int blue3 = p3 & 0xFF;

                int p4 = img3.getRGB(x,y);
                int alpha4 = (p4 >> 24) & 0xFF;
                int red4 = (p4 >> 16) & 0xFF;
                int green4 = (p4 >> 8) & 0xFF;
                int blue4 = p4 & 0xFF;

                int p5 = img3.getRGB(x,y);
                int alpha5 = (p5 >> 24) & 0xFF;
                int red5 = (p5 >> 16) & 0xFF;
                int green5 = (p5 >> 8) & 0xFF;
                int blue5 = p5 & 0xFF;

                int p6 = img3.getRGB(x,y);
                int alpha6 = (p6 >> 24) & 0xFF;
                int red6 = (p6 >> 16) & 0xFF;
                int green6 = (p6 >> 8) & 0xFF;
                int blue6 = p6 & 0xFF;

*/
/*
                System.out.println("alpha1 "+alpha1+" alpha2 "+alpha2+" alpha3 "+alpha3);
                System.out.println("red1 "+red1+" red2 "+red2+" red3 "+red3);
                System.out.println("green1 "+green1+" green2 "+green2+" green3 "+green3);
                System.out.println("blue1 "+blue1+" blue2 "+blue2+" blue3 "+blue3);*//*


                */
/*int alpha = alpha1;
                if(alpha2>alpha){
                    alpha=alpha2;
                }
                if(alpha3>alpha){
                    alpha=alpha3;
                }*//*

                int alpha = (int) Math.pow((Math.pow(alpha1,2)+Math.pow(alpha2,2)+Math.pow(alpha3,2)+Math.pow(alpha4,2)+Math.pow(alpha5,2)+Math.pow(alpha6,2)),(1/2))/6;
                int red = (int) Math.pow((Math.pow(red1,2)+Math.pow(red2,2)+Math.pow(red3,2)+Math.pow(red4,2)+Math.pow(red5,2)+Math.pow(red6,2)),(1/2))/6;
                int green = (int) Math.pow((Math.pow(green1,2)+Math.pow(green2,2)+Math.pow(green3,2)+Math.pow(green4,2)+Math.pow(green5,2)+Math.pow(green6,2)),(1/2))/6;
                int blue = (int) Math.pow((Math.pow(blue1,2)+Math.pow(blue2,2)+Math.pow(blue3,2)+Math.pow(blue4,2)+Math.pow(blue5,2)+Math.pow(blue6,2)),(1/2))/6;

                int p = (alpha << 24) | (red << 16) | (green << 8) | blue; //pixel

                img.setRGB(x, y, p);
            }
        }
*/


        // paint both images, preserving the alpha channels
        Graphics g1 = img.getGraphics();
        g1.drawImage(img1, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);
        g1.drawImage(img3, 0, 0, null);
        g1.drawImage(img4, 0, 0, null);
        g1.drawImage(img5, 0, 0, null);
        g1.drawImage(img6, 0, 0, null);

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
        BufferedImage img7 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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

                img7.setRGB(x, y, p);
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
