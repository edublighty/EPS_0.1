package com.mygdx.game.Screens.galScreen.Tools;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class galaxyProcessor {

    public galaxyProcessor(){

        //create buffered image object img
        BufferedImage img=null;
        int width;
        int height;
        int starCount = 0;
        double curRad=0;
        double totRad = 175;

        //write image
        try {
            img = ImageIO.read(new File("D:\\Image\\Input.png"));
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        width = img.getWidth();
        height = img.getHeight();
        int stars[][] = new int[2][(width*height/100)];

        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){

                int rgb = img.getRGB(x,y);
                //System.out.println("rgb is "+rgb);
                if(rgb==0){
                    // no pixel
                } else {
                    double maxRad = totRad + 100*Math.random() - 50;
                    boolean draw = true;
                    // create star here - maybe
                    if(starCount>1) {
                        boolean searching = true;
                        int c=starCount-1;
                        //System.out.println("starCount "+starCount);
                        while(searching){
                            curRad = Math.sqrt(Math.pow((x - stars[0][c]), 2) + Math.pow((y - stars[1][c]), 2));
                            //System.out.println("stars at c: "+c+" x "+stars[0][c]+" y "+stars[1][c]+" starCount "+starCount);
                            //System.out.println("curRad " + curRad + " maxRad " + maxRad + " x " + x + " y " + y);
                            if (curRad > maxRad){
                                //System.out.println(curRad+" is bigger than "+maxRad);
                                if( x > 60 && y > 7 && x < width - 60 && y < height - 7) {
                                    //System.out.println(x+" is greater than 7 and less than "+(width-7));
                                    //System.out.println(y+" is greater than 7 and less than "+(height-7));
                                    draw = true;
                                } else {
                                    //System.out.println(x+" is not greater than 7 or less than "+(width-7));
                                    //System.out.println(y+" is not greater than 7 or less than "+(height-7));
                                    draw = false;
                                    searching = false;
                                }
                            } else {
                                //System.out.println(curRad+" is not bigger than "+maxRad);
                                //System.out.println("starCount "+starCount);
                                draw = false;
                                searching = false;
                            }
                            //System.out.println("boolean result "+draw);
                            if(c>0) {
                                c--;
                            } else {
                                searching = false;
                            }
                        }
                    } else {
                        if (x > 7 && y > 7 && x < width - 7 && y < height - 7) {
                            draw = true;
                        } else {
                            draw = false;
                        }
                    }

                    if(draw) {
                        x = (int) (x + 75*Math.random() - 35);
                        //System.out.println("x "+x);
                        stars[0][starCount] = x;
                        stars[1][starCount] = y;
                        //System.out.println("Drawing "+starCount);
                        //System.out.println("stars x "+stars[0][starCount]+" y "+stars[0][starCount]);
                        //System.out.println("x "+x+" y "+y);//+" curRad "+curRad+" maxRad "+maxRad);

                        // paint stars as white crosses
                        int a = (int) (254); //alpha
                        int r = (int) (254); //red
                        int g = (int) (254); //green
                        int b = (int) (254); //blue
                        int p = (a << 24) | (r << 16) | (g << 8) | b; //pixel

                        for (int j = x - 3; j < x + 3; j++) {
                            img.setRGB(j, y, p);
                        }
                        for (int j = y - 3; j < y + 3; j++) {
                            img.setRGB(x, j, p);
                        }
                        for (int j = x - 2; j < x + 2; j++) {
                            img.setRGB(j, y + 1, p);
                        }
                        for (int j = x - 2; j < x + 2; j++) {
                            img.setRGB(j, y - 1, p);
                        }
                        starCount++;
                    }

                }
            }
        }

        File f;

        //write image
        try {
            f = new File("D:\\Image\\Output.png");
            ImageIO.write(img, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        final String FNAME = "systemPositions.txt";
        ArrayList<String> list_copy = new ArrayList<>();

        list_copy.add ("Line 1");
        list_copy.add ("Line 2");

        String line1 = " ";

        try ( BufferedWriter bw =
                      new BufferedWriter (new FileWriter(FNAME)) )
        {
            for (int i=0;i<stars[0].length;i++) {
                if(stars[0][i]==0 && stars[1][i]==0){
                    // do nothing
                } else {
                    bw.write(Integer.toString(stars[0][i]));
                    bw.write("\r\n");
                    bw.write(Integer.toString(stars[1][i]));
                    bw.write("\r\n");
                }
            }


            bw.close ();

        } catch (IOException e) {
            e.printStackTrace ();
        }

    }
}
