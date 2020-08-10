package com.mygdx.game.Screens.systemScreen.Tools.roomGenerators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class engConeGenerator {

    MyGdxGame game;

    public engConeGenerator(MyGdxGame game) {

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
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                int posX = textureRegBottom.getRegionX()+x;
                int posY = textureRegBottom.getRegionY()+y;
                System.out.println("posX "+posX+" posY "+posY);
                int colorInt = pixmapBottom.getPixel(posX,posY);
                int r1 = colorInt >>> 24;
                int g1 = (colorInt & 0xFF0000) >>> 16;
                int b1 = (colorInt & 0xFF00) >>> 8;
                int a1 = colorInt & 0xFF;
                System.out.println("x "+x+" y "+y);
                System.out.println("a1 "+a1+" r1 "+r1+" g1 "+g1+" b1 "+b1);
                colorInt = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                img1.setRGB(x, y, colorInt);
            }
        }

        // this is for the engine cone created later below
        BufferedImage img2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // this is for the in front section of engine
        BufferedImage img3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        TextureRegion textureRegTop = new TextureRegion(game.getRoomsAt().findRegion("engineConeTop"));
        texture = textureRegTop.getTexture();
        texture.getTextureData().prepare();
        Pixmap pixmapTop = texture.getTextureData().consumePixmap();
        int topStartX = textureRegTop.getRegionX();
        int topStartY = textureRegTop.getRegionY();
        for (int x = 0; x < img3.getWidth(); x++) {
            for (int y = 0; y < img3.getHeight(); y++) {
                int posX = textureRegTop.getRegionX()+x;
                int posY = textureRegTop.getRegionY()+y;
                System.out.println("posX "+posX+" posY "+posY);
                int colorInt = pixmapTop.getPixel(posX,posY);
                int r1 = colorInt >>> 24;
                int g1 = (colorInt & 0xFF0000) >>> 16;
                int b1 = (colorInt & 0xFF00) >>> 8;
                int a1 = colorInt & 0xFF;
                System.out.println("xtop "+x+" ytop "+y);
                System.out.println("a1 "+a1+" r1 "+r1+" g1 "+g1+" b1 "+b1);
                colorInt = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                img3.setRGB(x, y, colorInt);
            }
        }

        // this is for the finished flattened image
        BufferedImage img4 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int r;
        int g;
        int b;
        int a;

        float baseRext = 0;
        float baseGext = 178;
        float baseBext = 255;
        float baseAext = 255;

        float R0 = 255;
        float G0 = 255;
        float B0 = 255;
        float A0 = 255;

        int thick = 10;
        int margin = width - thick - 1;

        while( margin > 0) {
            for (int i = 0; i < width; i++) {

                float baseR = baseRext + i * (R0 - baseRext) / width;
                float baseG = baseGext + i * (G0 - baseGext) / width;
                float baseB = baseBext + i * (B0 - baseBext) / width;
                float baseA = baseAext + i * (A0 - baseAext) / width;

                for (int j = 0; j < height; j++) {

                    scanY = (int) (i * (0.9 * height / 2) / width);
                    if (Math.abs(j - height / 2) > scanY) {
                        // outside the cone

                        int topColor = pixmapTop.getPixel(topStartX + i,topStartY + j);
                        int colorInt = pixmapTop.getPixel(topStartX + i,topStartY + j);
                        int topR = colorInt >>> 24;
                        int topG = (colorInt & 0xFF0000) >>> 16;
                        int topB = (colorInt & 0xFF00) >>> 8;
                        int topA = topColor & 0xFF;

                        //int topA = img3.getRGB(i,j) & 0xFF;
                        int p;

                        System.out.println("topStartX "+topStartX+" topstartY "+topStartY);
                        System.out.println("tpoA "+topA+" at i "+i+" j "+j);
                        System.out.println("topR "+topR+" topG "+topG+" topB "+topB);

                        if(topA > 0){
                            // top image has colour so overrides
                            colorInt = pixmapTop.getPixel(topStartX + i,topStartY + j);
                            int r1 = colorInt >>> 24;
                            int g1 = (colorInt & 0xFF0000) >>> 16;
                            int b1 = (colorInt & 0xFF00) >>> 8;
                            int a1 = colorInt & 0xFF;
                            p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                        } else {
                            colorInt = pixmapBottom.getPixel(bottomStartX + i,bottomStartY + j);
                            int r1 = colorInt >>> 24;
                            int g1 = (colorInt & 0xFF0000) >>> 16;
                            int b1 = (colorInt & 0xFF00) >>> 8;
                            int a1 = colorInt & 0xFF;
                            p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                        }

                        img2.setRGB(i, j, p);
                    } else {
                        // inside the cone
                        //int topA = img3.getRGB(i,j) & 0xFF;
                        int topColor = pixmapTop.getPixel(topStartX + i,topStartY + j);
                        int topA = topColor & 0xFF;
                        int p;
                        int extra = 0;

                        if(topA > 0){
                            // top image has colour so overrides
                            int colorInt = pixmapTop.getPixel(topStartX + i,topStartY + j);
                            int r1 = colorInt >>> 24;
                            int g1 = (colorInt & 0xFF0000) >>> 16;
                            int b1 = (colorInt & 0xFF00) >>> 8;
                            int a1 = colorInt & 0xFF;
                            p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                        } else {

                            float diffY = Math.abs(j - height / 2);
                            r = (int) (baseR - diffY * (baseR - baseRext) / scanY);
                            g = (int) (baseG - diffY * (baseG - baseGext) / scanY);
                            b = (int) (baseB - diffY * (baseB - baseBext) / scanY);
                            a = (int) (baseA - diffY * (baseA - baseAext) / scanY);
                            if (Math.random() > 0.7) {
                                a = 0;
                            }

                            p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                            //j = (int) (j - 5 + 10 * Math.random());

                            if ((i > (margin) && i < (margin + thick))) {
                                // in the big zone
                                if (j < height / 2) {
                                    extra = 4;
                                } else {
                                    extra = -4;
                                }
                            }
                        }

                        img2.setRGB(i, j + extra, p);

                    }

                }

            }



            File f = null;

            //write image
            try {
                f = new File("C:\\Image\\engConeBlue"+count+".png");
                ImageIO.write(img2, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }
            /*
            //write image
            try {
                f = new File("C:\\Image\\engineBottom"+margin+".png");
                ImageIO.write(img1, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }
            //write image
            try {
                f = new File("C:\\Image\\engineTop"+margin+".png");
                ImageIO.write(img3, "png", f);
            } catch (
                    IOException e) {
                System.out.println("Error: " + e);
            }
*/
            count++;

            margin -= thick;

            // reset the painted images
            for(int i = 0;i<width;i++){
                for(int j = 0; j<height;j++){
                    a = 0;
                    r = (int) 255;//(255-(130*rad/baseRad)); // blue
                    g = (int) 255;//(255-(130*rad/baseRad)); // blue
                    b = (int) 255; // blue

                    int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                    img2.setRGB(i, j, p);
                    img4.setRGB(i, j, p);
                }
            }

            a = 0;
            r = (int) 255;//(255-(130*rad/baseRad)); // blue
            g = (int) 255;//(255-(130*rad/baseRad)); // blue
            b = (int) 255; // blue

            int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

            //img.setRGB(i, j, p);

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
