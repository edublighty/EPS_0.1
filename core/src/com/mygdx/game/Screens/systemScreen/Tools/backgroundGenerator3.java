package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.AlteredPerlin;
import com.mygdx.game.Perlin.Perlin;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class backgroundGenerator3 {

    MyGdxGame game;

    public backgroundGenerator3() {

        //image dimension
        int galWidth = 1500;
        int galHeight = 1500;
        int clr = 255;
        int clrRem = 255 - clr;
        int clrRed = 225;
        int clrBlue = 225;
        int clrGreen = 225;
        int aMin = 0;
        double pow = 3;
        double pow2 = 8;

        BufferedImage img = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img2 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img3 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img4 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img5 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img6 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img7 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img8 = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);

        int scanX = galWidth/2;
        int scanY = 500;
        int width = 6;
        int height = 6;
        // start placing stars within whole width of galaxy and then reduce

        Perlin perlin = new Perlin();
        float aArray[][] = new float[galWidth][galHeight];
        float a2Array[][] = new float[width][height];
        float a3Array[][] = new float[width][height];
        float a4Array[][] = new float[width][height];

        // first array for something
        aArray = perlin.perlinFun(perlin,game,galWidth,galHeight, 1, 1, 1, 1, 3, 1, 7);
        // for entire galactic arm
        a2Array = perlin.perlinFun(perlin,game,galWidth,galHeight, 1, 1, 1, 1, 3, 1, 7);
        // for galactic core
        a3Array = perlin.perlinFun(perlin,game,galWidth,galHeight, 1, 1, 1, 1, 3, 1, 7);
        // for general all square fade at end
        a4Array = perlin.perlinFun(perlin,game,galWidth,galHeight, 1, 1, 1, 1, 1, 1, 10);

        int r;
        int g;
        int b;
        int a;
        int removal=1;
        double baseRad = Math.sqrt(Math.pow((5-2),2)+Math.pow((0-3),2));
        int loopCycles = 3;
        float m = 50f/500f;
        float c = m*500f;
        int x0 = 0;
        int x1 = 500;
        int x2 = 1000;
        int x3 = 1500;

        // make stars along galactic core and arm
        while(scanX>12){
            if(scanX>250){
                scanY = (int) (2*(c-m*scanX));
            } else {
                scanY = (int) (Math.sqrt(62500-Math.pow(scanX,2)));
            }

            for(int i = 0; i < loopCycles; i++) {
                if(i==0) {
                    clrRed = (int) (255 * Math.random());
                    clrGreen = (int) (255 * Math.random());
                    clrBlue = (int) (255 * Math.random());
                    removal = 1;
                } else {
                    clrRed = (int) 255;
                    clrGreen = (int) 255;
                    clrBlue = (int) 255;
                    removal = 3;
                }
                // first star to the right
                int X = (int) (750 + scanX*Math.random());
                int Y = (int) (galHeight/2 - scanY +2*scanY*Math.random());
                System.out.println("scanX "+scanX+" scanY "+scanY);
                //System.out.println("star created at "+X+" "+Y);
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // calculate distance from (0,3) in grid
                        double rad = Math.sqrt(Math.pow((x - 2), 2) + Math.pow((y - 3), 2));
                        // distance from (3,4) in grid to (5,0) where its planned for star to be dimmest
                        a = (int) (255 - (100 * rad / baseRad));
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
                        b = (int) (clrBlue - (clrBlue * rad / baseRad)); // blue

                        int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                        //System.out.println("p is now "+p);

                        try {
                            img.setRGB(X + x, Y + y, p);
                        } catch (Exception e) {
                            // probably out of bounds, who cares
                        }
                    }
                }

                //second star to the left
                X = (int) (750 - scanX * Math.random());
                Y = (int) (galHeight / 2 - scanY + 2 * scanY * Math.random());
                System.out.println("star created at " + X + " " + Y);

                //create random image pixel by pixel
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // calculate distance from (0,3) in grid
                        double rad = Math.sqrt(Math.pow((x - 2), 2) + Math.pow((y - 3), 2));
                        // distance from (3,4) in grid to (5,0) where its planned for star to be dimmest
                        a = (int) (255 - (100 * rad / baseRad));
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
                        b = (int) (clrBlue - (clrBlue * rad / baseRad)); // blue

                        int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                        //System.out.println("p is now "+p);

                        try {
                            img.setRGB(X + x, Y + y, p);
                        } catch (Exception e) {
                            // probably out of bounds, who cares
                        }
                    }
                }
            }
            scanX -= removal;
        }

        int noStars = 300;
        // make random stars across galwidth and galheight
        while(noStars>0){

            int X = (int) (galWidth*Math.random());
            int Y = (int) (galHeight*Math.random());

            clrRed = (int) 255;
            clrGreen = (int) 255;
            clrBlue = (int) 225;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
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
                    b = (int) (clrBlue - (clrBlue * 0.25 * rad / baseRad)); // blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                    //System.out.println("p is now "+p);

                    try {
                        img2.setRGB(X + x, Y + y, p);
                    } catch (Exception e) {
                        // probably out of bounds, who cares
                    }
                }
            }

            noStars--;
        }

        // this layer makes galactic core brighter than rest
        //rad = 175;
        int rad = 125;
        m = 25f/(750f-rad);
        c = m*750f;
        aMin=0;

        for(int x=0;x<galWidth;x++){
            for(int y=0;y<galHeight;y++){

                boolean inside = false;

                int tempX = Math.abs((x-750));
                if(tempX<rad){
                    scanY = (int) (Math.sqrt(Math.pow(rad,2)-Math.pow(tempX,2)));
                    //System.out.println("in circle at x "+x+" and y "+y+" scanY "+scanY);
                    if(y<galHeight/2+scanY && y>galHeight/2-scanY){
                        inside = true;
                    }
                } else {
                    // do nothing
                }


                if(inside){
                    double tempRad = Math.sqrt(Math.pow((x-galWidth/2),2)+Math.pow((y-galHeight/2),2));
                    if(tempRad>rad){
                        a = 0;
                    } else {
                        a = (int) (255-(255*(tempRad/rad)));
                    }
                    if(a<0){
                        a=0;
                    }
                    if(a>255){
                        a=255;
                    }

                    r = (int) (255); //red
                    g = (int) (255); //green
                    b = (int) (255); //blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img7.setRGB(x, y, p);
                }

            }
        }

        // this layer puts in some blur over the galactic arm stars to add texture with blue
        rad = 175;
        m = 25f/(750f-rad);
        c = m*750f;
        aMin=0;

        for(int x=0;x<galWidth;x++){
            for(int y=0;y<galHeight;y++){

                boolean inside = false;

                int tempX = Math.abs((x-750));
                if(tempX<rad){
                    scanY = (int) (Math.sqrt(Math.pow(rad,2)-Math.pow(tempX,2)));
                    //System.out.println("in circle at x "+x+" and y "+y+" scanY "+scanY);
                } else {
                    tempX = Math.abs(750-x);
                    scanY = (int) (2*(c-m*tempX));
                    //System.out.println("else outside at x "+x+" and y "+y+" scanY "+scanY);
                }

                if(y<galHeight/2+scanY && y>galHeight/2-scanY){
                    inside = true;
                }

                if(inside){
                    a = (int) ((Math.pow((aArray[x][y]),3.2)*clr - aMin)); //alpha
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
                    r = (int) (1); //red
                    g = (int) (1); //green
                    b = (int) (100); //blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img5.setRGB(x, y, p);
                }

            }
        }

        // this layer puts in some blur over the galactic arm stars to add texture with red
        rad = 175;
        m = 25f/(750f-rad);
        c = m*750f;
        aMin=0;

        for(int x=0;x<galWidth;x++){
            for(int y=0;y<galHeight;y++){

                boolean inside = false;

                int tempX = Math.abs((x-750));
                if(tempX<rad){
                    scanY = (int) (Math.sqrt(Math.pow(rad,2)-Math.pow(tempX,2)));
                    //System.out.println("in circle at x "+x+" and y "+y+" scanY "+scanY);
                } else {
                    tempX = Math.abs(750-x);
                    scanY = (int) (2*(c-m*tempX));
                    //System.out.println("else outside at x "+x+" and y "+y+" scanY "+scanY);
                }

                if(y<galHeight/2+scanY && y>galHeight/2-scanY){
                    inside = true;
                }

                if(inside){
                    a = (int) ((Math.pow((a2Array[x][y]),4)*clr - aMin)); //alpha
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
                    r = (int) (255); //red
                    g = (int) (1); //green
                    b = (int) (1); //blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img6.setRGB(x, y, p);
                }

            }
        }

        // this layer puts in some blur over the galactic core to make bright
        rad = 125;
        m = 25f/(750f-rad);
        c = m*750f;
        aMin=0;

        for(int x=0;x<galWidth;x++){
            for(int y=0;y<galHeight;y++){

                boolean inside = false;

                int tempX = Math.abs((x-750));
                if(tempX<rad){
                    scanY = (int) (Math.sqrt(Math.pow(rad,2)-Math.pow(tempX,2)));
                    //System.out.println("in circle at x "+x+" and y "+y+" scanY "+scanY);
                    if(y<galHeight/2+scanY && y>galHeight/2-scanY){
                        inside = true;
                    }
                } else {
                    // do nothing
                }


                if(inside){
                    a = (int) ((Math.pow((a3Array[x][y]),3.2)*clr - aMin)); //alpha
                    if(a<0){
                        a=0;
                    }
                    if(a>255){
                        a=255;
                    }

                    r = (int) (255); //red
                    g = (int) (255); //green
                    b = (int) (1); //blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    //img7.setRGB(x, y, p);
                }

            }
        }

        // this layer puts in some blur over the entire square
        aMin=0;
        float aGeo = 0.2f;

        for(int x=0;x<galWidth;x++){
            for(int y=0;y<galHeight;y++){

                boolean inside = true;


                if(inside){
                    a = (int) ((Math.pow((a4Array[x][y]),1)*clr - aMin)); //alpha
                    a *= aGeo;
                    if(a<0){
                        a=0;
                    }
                    if(a>255){
                        a=255;
                    }

                    r = (int) (1); //red
                    g = (int) (255); //green
                    b = (int) (1); //blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img3.setRGB(x, y, p);
                }

            }
        }

        // paint both images, preserving the alpha channels
        Graphics g1 = img4.getGraphics();
        g1.drawImage(img, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);
        g1.drawImage(img3, 0, 0, null);
        g1.drawImage(img8, 0, 0, null);
        g1.drawImage(img5, 0, 0, null);
        g1.drawImage(img6 ,0, 0, null);
        g1.drawImage(img7 ,0, 0, null);

        File f = null;
        int filecount=2;

        //write image
        try {
            f = new File("C:\\Image\\galaxy"+filecount+".png");
            ImageIO.write(img4, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }

    public void originalStarSub(){

        //image dimension
        int galWidth = 1500;
        int galHeight = 1500;
        int clr = 255;
        int clrRem = 255 - clr;
        int clrRed = 225;
        int clrBlue = 225;
        int clrGreen = 225;
        int aMin = 0;
        double pow = 3;
        double pow2 = 8;

        BufferedImage img = new BufferedImage(galWidth, galHeight, BufferedImage.TYPE_INT_ARGB);
        int scanX = galWidth/2;
        int scanY = 500;
        int width = 6;
        int height = 6;
        // start placing stars within whole width of galaxy and then reduce

        int r;
        int g;
        int b;
        int a;
        double baseRad = Math.sqrt(Math.pow((5-2),2)+Math.pow((0-3),2));
        int loopCycles = 1;
        float m = 50f/500f;
        float c = m*750f;

        // first star to the right
        int X = (int) (750 + scanX*Math.random());
        int Y = (int) (galHeight/2 - scanY +2*scanY*Math.random());
        System.out.println("scanX "+scanX+" scanY "+scanY);
        //System.out.println("star created at "+X+" "+Y);

        for(int i = 0; i < loopCycles; i++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // calculate distance from (0,3) in grid
                    double rad = Math.sqrt(Math.pow((x - 2), 2) + Math.pow((y - 3), 2));
                    // distance from (3,4) in grid to (5,0) where its planned for star to be dimmest
                    a = (int) (255 - (100 * rad / baseRad));
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
                    r = (int) 255;//(255-(130*rad/baseRad)); // blue
                    g = (int) 255;//(255-(130*rad/baseRad)); // blue
                    b = (int) (255 - (225 * rad / baseRad)); // blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel
                    //System.out.println("p is now "+p);

                    try {
                        img.setRGB(X + x, Y + y, p);
                    } catch (Exception e) {
                        // probably out of bounds, who cares
                    }
                }
            }
        }
    }

}
