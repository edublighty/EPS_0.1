package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class backgroundGenerator6 {

    private BufferedImage img;
    MyGdxGame game;

    public backgroundGenerator6() {

        //image dimension
        int width = 1500;
        int height = 1500;
        int clr = 255;
        int clrRem = 255 - clr;
        int clrRed = 225;
        int clrBlue = 225;
        int clrGreen = 225;
        int aMin = 0;
        double pow = 75;

        float starArray[][] = new float[width][height];
        float a1Array[][] = new float[width][height];
        float a2Array[][] = new float[width][height];
        float a3Array[][] = new float[width][height];

        // for stars, all 1s and pow between 33 and 250
        // for clouds pow is about 10 and first three octaves 1, 2, 3

        int aoct1 = 1;
        int aoct2 = 1;
        int aoct3 = 1;
        int aoct4 = 1;
        int aoct5 = 1;
        int aoct6 = 1;
        int aoct7 = 1;

        Perlin perlin = new Perlin();

        starArray = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        aoct1 = 1;
        aoct2 = 2;
        aoct3 = 3;
        aoct4 = 1;
        aoct5 = 1;
        aoct6 = 1;
        aoct7 = 8;
        a1Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a2Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);
        a3Array = perlin.perlinFun(perlin,game,width,height, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);

        //create buffered image object img
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        BufferedImage imgStars = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 2 - white yellow
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(starArray[x][y],pow)) ;//* Math.pow(a2Array[x][y],pow2));
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
                int r;
                int g;
                int b;
                r = (int) (255); //red
                g = (int) (255); //green
                b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                imgStars.setRGB(x, y, p);
            }
        }

        pow = 7;
        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 1 - light blue
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a1Array[x][y],pow));

                int a = (int) (temp * clr) - aMin; //alpha
                if(a<0){
                    a=0;
                }
                if(a>255){
                    a=255;
                }

                int r;
                int g;
                int b;
                r = (int) (0); //red
                g = (int) (200); //green
                b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img1.setRGB(x, y, p);
            }
        }

        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 2 - purple
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a2Array[x][y],pow)) ;//* Math.pow(a2Array[x][y],pow2));
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
                r = (int) (211); //red
                g = (int) (109); //green
                b = (int) (255); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img2.setRGB(x, y, p);
            }
        }

        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 3 - red overlay
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a3Array[x][y],pow)) ;//* Math.pow(a2Array[x][y],pow2));
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
                g = (int) (50); //green
                b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img3.setRGB(x, y, p);
            }
        }

        BufferedImage img4 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 4 - light green
        //create random image pixel by pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double temp = (Math.pow(a1Array[x][y],pow)) ;//* Math.pow(a2Array[x][y],pow2));
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
                g = (int) (255); //green
                b = (int) (0); //blue

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                img4.setRGB(x, y, p);
            }
        }

        // paint both images, preserving the alpha channels
        Graphics g1 = img.getGraphics();
        g1.drawImage(img1, 0, 0, null);
        g1.drawImage(imgStars, 0, 0, null);
        g1.drawImage(img3, 0, 0, null);
        g1.drawImage(img4, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);

        int filecount=2;

        //write image
        try {
            f = new File("C:\\Image\\background"+filecount+".png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }

    public BufferedImage getImg(){ return img; }

}
