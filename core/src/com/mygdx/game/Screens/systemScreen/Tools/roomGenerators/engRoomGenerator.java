package com.mygdx.game.Screens.systemScreen.Tools.roomGenerators;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class engRoomGenerator {

    MyGdxGame game;

    public engRoomGenerator(MyGdxGame game) {

        //image dimension
        int width = 100;
        int height = 100;

        int count = 1;
        int scanX = 0;//galWidth/2;
        int scanY = 500;

        // this is for the behind section of engine
        BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        TextureRegion textureRegBottom = new TextureRegion(game.getRoomsAt().findRegion("engineConeBottom"));
        Texture texture = textureRegBottom.getTexture();
        texture.getTextureData().prepare();
        Pixmap pixmapBottom = texture.getTextureData().consumePixmap();
        System.out.println("pixmap width "+pixmapBottom.getWidth()+"pixmap height "+pixmapBottom.getHeight());
        int bottomStartX = textureRegBottom.getRegionX();
        int bottomStartY = textureRegBottom.getRegionY();
        boolean striping = true;
        boolean baseStriping = true;
        for (int x = 0; x < width; x++) {
            if(x<20){
                scanY = 50;
                baseStriping = false;
            } else if(x<40){
                scanY = (int) (85 - 3.5*x/2);
                baseStriping = false;
            } else {
                scanY = 15;
                baseStriping = false;
            }
            count = 0;
            for (int y = scanY-1; y > 0; y--) {
                if(baseStriping) {
                    if (count > 1) {
                        count = 0;
                        if (striping) {
                            striping = false;
                        } else {
                            striping = true;
                        }
                    }
                } else {
                    striping = false;
                }
                int r1 = 175;
                int g1 = 175;
                int b1 = 175;
                int a1 = 255;
                if(striping){
                    r1 = 75;
                    g1 = 255;
                    b1 = 255;
                }

                int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                img1.setRGB(x, height/2-y-1, p);
                img1.setRGB(x, height/2+y, p);
                count++;
            }
        }

        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for(int counter=0;counter<10;counter++) {

            // this is for the engine cone created later below
            scanY = 5 + counter;
            for (int x = 40; x < width; x++) {
                for (int y = 0; y < scanY; y++) {
                    double a11 = y;
                    double a12 = scanY;
                    int r1 = (int) (255 * (1 - a11 / a12));
                    int g1 = (int) (80 + 170*(1 - a11 / a12));
                    int b1 = (int) (255);//
                    System.out.println("y " + y + " a11 " + a11);
                    int a1 = (int) 200;//(255 * (1 - a11 / a12));
                    int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                    img2.setRGB(x, height / 2 + y, p);
                    img2.setRGB(x, height / 2 - y - 1, p);
                }
            }
            File f = null;

            //write image
            try {
                f = new File("C:\\Image\\engRoomCone" + counter + ".png");
                ImageIO.write(img2, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }
        }

        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g1 = img3.getGraphics();
        g1.drawImage(img1, 0, 0, null);
        g1.drawImage(img2, 0, 0, null);

            File f = null;

            //write image
            try {
                f = new File("C:\\Image\\engRoom"+count+".png");
                ImageIO.write(img1, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }

            count++;

        //write image
        try {
            f = new File("C:\\Image\\engRoom"+count+".png");
            ImageIO.write(img2, "png", f);
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
