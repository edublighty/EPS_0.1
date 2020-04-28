package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class backgroundGenerator5 {

    public backgroundGenerator5() {

        //image dimension
        int width = 1500;
        int height = 1500;

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //file object
        File f = null;

        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int count1 = 1;
        int count2 = 1;
        double[] a1 = new double[count1];
        double[] b1 = new double[count1];
        double[] c1 = new double[count1];
        double[] d1 = new double[count1];
        double[] a2 = new double[count2];
        double[] b2 = new double[count2];
        double[] c2 = new double[count2];
        double[] d2 = new double[count2];

        int mult1 = 3;
        int mult2 = 10;
        int mult3 = 10;
        int mult4 = 0;

        for(int i=0;i<count1;i++){
            a1[i]=Math.random()*mult1;
            b1[i]=Math.random()*mult2;
            c1[i]=Math.random()*mult3;
            d1[i]=Math.random()*mult4;
            mult1 += 1;
        }

        mult1 = 1;
        mult2 = 2;

        for(int i=0;i<count2;i++){
            a2[i]=Math.random()*mult1;
            b2[i]=Math.random()*mult2;
            c2[i]=Math.random()*mult3;
            d2[i]=Math.random()*mult4;
            mult1 += 1;
        }

        double highest = -1000000;

        double lowest = 1000000;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {

                double fun1 = 0;
                double fun2 = 0;

                for(int i=0;i<count1;i++){
                    fun1 += sineWave(a1[i],b1[i],c1[i],d1[i],x,width);
                }


                for(int i=0;i<count2;i++){
                    fun2 += sineWave(a2[i],b2[i],c2[i],d2[i],y,height);
                }

                //System.out.println("fun1 "+fun1);
                //System.out.println("fun2 "+fun2);

                int a = (int) (255*(fun1+fun2));
                if(a>highest){
                    highest=a;
                }
                if(a<lowest){
                    lowest=a;
                }
            }
        }
        System.out.println("highest "+highest+" lowest "+lowest);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {

                double fun1 = 0;
                double fun2 = 0;

                for(int i=0;i<count1;i++){
                    fun1 += sineWave(a1[i],b1[i],c1[i],d1[i],x,width);
                }


                for(int i=0;i<count2;i++){
                    fun2 += sineWave(a2[i],b2[i],c2[i],d2[i],y,height);
                }

                int a = (int) (255*((255*(fun1+fun2))-lowest)/(highest-lowest));
                int r = (int) 255;
                int g = (int) 127;
                int b = (int) 0;

                int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                //System.out.println("p is now "+p);

                img1.setRGB(x, y, p);
            }
        }


        // paint both images, preserving the alpha channels
        Graphics g1 = img.getGraphics();
        g1.drawImage(img1, 0, 0, null);


        int filecount = 2;

        //write image
        try {
            f = new File("C:\\Image\\background" + filecount + ".png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }

    public double sineWave(double a, double b, double c, double d, double z, int width){

        double fun = (Math.sin(a*z*Math.PI/width+b)*c+d);

        return fun;

    }

}
