package com.mygdx.game.Screens.systemScreen.Tools.greeble;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Perlin.Perlin;
import com.mygdx.game.Screens.galScreen.Tools.dijkstra.dVertex;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.imageio.ImageIO;

public class greebleGenerator {

    int points[][] = new int[2][7];
    int currentN;
    double diffR;

    public greebleGenerator(MyGdxGame game){

        int width = 100;
        int height = width;

        // this is for the finished flattened image
        BufferedImage imgBack = new BufferedImage(width*12, height*12, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgUnderCoat = new BufferedImage(width*12, height*12, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img = new BufferedImage(width*12, height*12, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgFront = new BufferedImage(width*12, height*12, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgFlat = new BufferedImage(width*12, height*12, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgCompare = new BufferedImage(width*12, height*12, BufferedImage.TYPE_INT_ARGB);

        int noColpix = 100;
        int noSide = 5;//(int) Math.sqrt(noColpix);
        int lenColpix = width / noSide;

        for(int i=0;i<width*12;i++){
            for(int j=0;j<height*12;j++){
                int r1 = 64;
                int g1 = 64;
                int b1 = 64;
                int a1 = 0;
                int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                imgUnderCoat.setRGB(i,j,p);
                img.setRGB(i,j,p);
                imgFront.setRGB(i,j,p);
                imgBack.setRGB(i,j,p);
            }
        }

        int tiles[][] = new int[12][12];
        for(int i=0; i<12; i++){
            for(int j=0; j<12;j++){
                tiles[i][j] = 0;
            }
        }
        tiles[0][0] = 1;
        tiles[1][0] = 1;
        tiles[2][0] = 1;
        tiles[0][2] = 1;
        tiles[1][2] = 1;
        tiles[2][2] = 1;
        tiles[2][1] = 1;
        tiles[3][1] = 1;
        tiles[4][1] = 1;
        tiles[5][1] = 1;
        tiles[5][2] = 1;
        tiles[6][2] = 1;
        tiles[5][0] = 1;
        tiles[6][1] = 1;
        tiles[7][1] = 1;
        tiles[8][1] = 1;

        int cockpitI = 5;
        int cockpitJ = 0;

        colpix[][] colpixes = new colpix[noSide][noSide];

        int aoct1 = 1;
        int aoct2 = 1;
        int aoct3 = 1;
        int aoct4 = 1;
        int aoct5 = 1;
        int aoct6 = 1;
        int aoct7 = 6;
        Perlin perlin = new Perlin();
        float starArray[][] = new float[noSide][noSide];
        starArray = perlin.perlinFun(perlin,game,width*12,height*12, aoct1, aoct2, aoct3, aoct4, aoct5, aoct6, aoct7);

        for(int iTile=0;iTile<12;iTile++){
            for(int jTile=0;jTile<12;jTile++){
                if(tiles[iTile][jTile]>0){
                    // theres a room here
                    // do the thing Jew Lee

                    // initialise all collections of pixels (Cells) with uniform squares
                    for(int i=0;i<noSide;i++){
                        for(int j=0;j<noSide;j++){
                            int x1 = i*lenColpix;
                            int x2 = (i+1)*lenColpix;
                            int y1 = j*lenColpix;
                            int y2 = (j+1)*lenColpix;
                            colpixes[i][j] = new colpix(x1, x2, y1, y2, (int) ((starArray[i*(width/noSide)+iTile*width][j*(height/noSide)+jTile*height])*255), (int) (150), (int) (150), (int) (150), i, j);
                        }
                    }

                    for(int i=0;i<starArray[0].length;i++){
                        for(int j=0;j<starArray.length;j++){
                            int r1 = 255;
                            int g1 = 10;
                            int b1 = 10;
                            int a1 = (int) (255*starArray[i][j]);
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            // set background colour everywhere as grey
                            imgCompare.setRGB(i, j, p);
                        }
                    }

                    // then start altering cells so they are no longer regular uniform squares
                    for(int iN=0;iN<colpixes[0].length;iN++) {
                        for (int jN = 0; jN < colpixes.length; jN++) {
                            int[][][] currentPos = colpixes[iN][jN].getCoords();
                            int[][][] newPos = currentPos;
                            if(jN>0){
                                // below first row so make cell tops equal that of bottoms of cells above
                                int[][][] abovePos = colpixes[iN][jN-1].getCoords();
                                // 0,0 equals 0,1 above
                                newPos[0][0][0] = abovePos[0][1][0];
                                newPos[0][0][1] = abovePos[0][1][1];
                                // 1,0 equals 1,1 above
                                newPos[1][0][0] = abovePos[1][1][0];
                                newPos[1][0][1] = abovePos[1][1][1];
                            } else {
                                // 0,0 equals top
                                newPos[0][0][1] = 0;
                                // 1,0 equals top
                                newPos[1][0][1] = 0;
                            }
                            if(iN>0){
                                // to right of first column so make 0,1 equals to 1,1 of left cell
                                int[][][] leftPos = colpixes[iN-1][jN].getCoords();
                                // 0,1 equals 1,1 of left
                                newPos[0][1][0] = leftPos[1][1][0];
                                newPos[0][1][1] = leftPos[1][1][1];
                                // 0,0 equals 1,0 of left
                                newPos[0][0][0] = leftPos[1][0][0];
                                newPos[0][0][1] = leftPos[1][0][1];
                            } else {
                                // 0,0 equals left
                                newPos[0][0][0] = 0;
                                // 1,0 equals left
                                newPos[0][1][0] = 0;
                            }
                            // alter position slightly of X1,Y1
                            newPos[1][1][0] = (int) (newPos[1][1][0] - lenColpix/2 + lenColpix*Math.random());
                            newPos[1][1][1] = (int) (newPos[1][1][1] - lenColpix/2 + lenColpix*Math.random());

                            int x00 = newPos[0][0][0];
                            int y00 = newPos[0][0][1];

                            int x10 = newPos[1][0][0];
                            int y10 = newPos[1][0][1];

                            int x01 = newPos[0][1][0];
                            int y01 = newPos[0][1][1];

                            int x11 = newPos[1][1][0];
                            int y11 = newPos[1][1][1];

                            if(iN == (colpixes.length-1)){
                                // reached the end so make the right equal to the right of the square
                                newPos[1][0][0] = width;
                                newPos[1][1][0] = width;
                            }
                            if(jN == (colpixes.length-1)){
                                // reached the end so make the bottoms equal to the bottom of the square
                                newPos[0][1][1] = height;
                                newPos[1][1][1] = height;
                            }
                            colpixes[iN][jN].updateCoords(newPos);
                        }
                    }

                    // create all pixels
                    for(int i=0;i<width;i++){
                        for(int j=0;j<height;j++){
                            int r1 = 200;
                            int g1 = 200;
                            int b1 = 200;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            // set background colour everywhere as grey
                            img.setRGB(i+iTile*width, j+jTile*height, p);
                            //System.out.println("i "+i+" j "+j);
                            boolean notFound = true;
                            for(int iN=0;iN<colpixes[0].length;iN++){
                                for(int jN=0;jN<colpixes.length;jN++){
                                    Polygon polygon = new Polygon();
                                    int[][][] colpixPos = colpixes[iN][jN].getCoords();
                                    polygon.addPoint(colpixPos[0][0][0],colpixPos[0][0][1]);
                                    polygon.addPoint(colpixPos[1][0][0],colpixPos[1][0][1]);
                                    polygon.addPoint(colpixPos[1][1][0],colpixPos[1][1][1]);
                                    polygon.addPoint(colpixPos[0][1][0],colpixPos[0][1][1]);

                                    Point point = new Point(i,j);
                                    if(polygon.contains(point)){
                                        // pixel is within current sector
                                        p = colpixes[iN][jN].getColour();
                                        notFound = false;
                                        //System.out.println("pixel inside yay");
                                    }
                                }
                            }
                            if(notFound){
                                //System.out.println("no PIXEL LOCATION FOUND");
                            }
                            imgFront.setRGB(i+iTile*width, j+jTile*height, p);
                        }
                    }

                    int fillerWidth = (int) (width/15);

                    if(iTile>0){
                        // right of first column so check left
                        if(tiles[iTile-1][jTile]==0){
                            // add filler to left
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for(int i=0;i<fillerWidth;i++){
                                for(int j=0;j<height;j++){
                                    imgBack.setRGB(iTile*width-fillerWidth+i,jTile*height+j,p);
                                }
                            }
                        }
                    }

                    if(iTile<12){
                        // above last row so check below
                        if(tiles[iTile+1][jTile]==0){
                            // add filler right
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for(int i=0;i<fillerWidth;i++){
                                for(int j=0;j<height;j++){
                                    imgBack.setRGB(iTile*width+width+i,jTile*height+j,p);
                                }
                            }
                        }
                    }

                    if(jTile>0){
                        // below first row so check above
                        if(tiles[iTile][jTile-1]==0){
                            // add filler above
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for(int i=0;i<width;i++){
                                for(int j=0;j<fillerWidth;j++){
                                    imgBack.setRGB(iTile*width+i,jTile*height-fillerWidth+j,p);
                                }
                            }
                        }
                    }

                    if(jTile<12){
                        // above last row so check below
                        if(tiles[iTile][jTile+1]==0){
                            // add filler belw
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for(int i=0;i<width;i++){
                                for(int j=0;j<fillerWidth;j++){
                                    imgBack.setRGB(iTile*width+i,jTile*height+height+j,p);
                                }
                            }
                        }
                    }

                    if(iTile<12 && jTile<12) {
                        if ((tiles[iTile + 1][jTile] == 0) && (tiles[iTile][jTile + 1] == 0)) {
                            // fillet top right
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for (int theta = 0; theta < 90; theta++) {
                                int x = (int) (fillerWidth * Math.cos(theta * Math.PI / 180));
                                int y = (int) (fillerWidth * Math.sin(theta * Math.PI / 180));
                                for (int j = 0; j < y; j++) {
                                    imgBack.setRGB(((iTile + 1) * width + x), ((jTile + 1) * height + j), p);
                                }
                            }
                        }
                    }

                    if(iTile>0 && jTile<12) {
                        if ((tiles[iTile - 1][jTile] == 0) && (tiles[iTile][jTile + 1] == 0)) {
                            // fillet top left
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for (int theta = 0; theta < 90; theta++) {
                                int x = (int) (fillerWidth * Math.cos(theta * Math.PI / 180));
                                int y = (int) (fillerWidth * Math.sin(theta * Math.PI / 180));
                                for (int j = 0; j < y; j++) {
                                    imgBack.setRGB(((iTile) * width - x), ((jTile + 1) * height + j), p);
                                }
                            }
                        }
                    }

                    if(iTile<12 && jTile>0) {
                        if ((tiles[iTile + 1][jTile] == 0) && (tiles[iTile][jTile - 1] == 0)) {
                            // fillet bottom right
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for (int theta = 0; theta < 90; theta++) {
                                int x = (int) (fillerWidth * Math.cos(theta * Math.PI / 180));
                                int y = (int) (fillerWidth * Math.sin(theta * Math.PI / 180));
                                for (int j = 0; j < y; j++) {
                                    imgBack.setRGB(((iTile + 1) * width + x), ((jTile) * height - j), p);
                                }
                            }
                        }
                    }

                    if(iTile>0 && jTile>0) {
                        if ((tiles[iTile - 1][jTile] == 0) && (tiles[iTile][jTile - 1] == 0)) {
                            // fillet bottom left
                            int r1 = 1;
                            int g1 = 1;
                            int b1 = 1;
                            int a1 = 255;
                            int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                            for (int theta = 0; theta < 90; theta++) {
                                int x = (int) (fillerWidth * Math.cos(theta * Math.PI / 180));
                                int y = (int) (fillerWidth * Math.sin(theta * Math.PI / 180));
                                for (int j = 0; j < y; j++) {
                                    imgBack.setRGB(((iTile) * width - x), ((jTile) * height - j), p);
                                }
                            }
                        }
                    }

                    // create some black line panels
                    int r1 = 1;
                    int g1 = 1;
                    int b1 = 1;
                    int a1 = 175;
                    int p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                    int panWidth = (int) (0.75*width);
                    int panHeight = (int) (0.4*height);
                    int panSpace = (int) (height*0.2/3);
                    int straitWidth = (int) (0.7*panWidth);
                    int diffX = (int) ((panWidth-straitWidth)/2);
                    int straitHeight = (int) (panHeight - 2*diffX);
                    int diffY = (int) ((panHeight-straitHeight)/2);
                    int panRad = diffX;
                    diffX++;
                    diffY++;
                    int x1 = width/2 - straitWidth/2;
                    int y1 = height/2 - panHeight/2;
                    int x2 = x1 + straitWidth;
                    int y2 = height/2 + panHeight/2;
                    // draw horizontal lines
                    for(int i=x1;i<x2;i++){
                        imgUnderCoat.setRGB(iTile*width+i,jTile*height+y1,p);
                        imgUnderCoat.setRGB(iTile*width+i,jTile*height+y2,p);
                    }
                    // fillet top right
                    y1 += diffY;
                    for (int theta = 0; theta < 90; theta++) {
                        int y = (int) (diffY * Math.cos(theta * Math.PI / 180));
                        int x = (int) (diffX * Math.sin(theta * Math.PI / 180));
                        imgUnderCoat.setRGB((iTile*width + x2 + x), (jTile*height + y1 - y), p);
                    }
                    // fillet top left
                    for (int theta = 0; theta < 90; theta++) {
                        int y = (int) (diffY * Math.cos(theta * Math.PI / 180));
                        int x = (int) (diffX * Math.sin(theta * Math.PI / 180));
                        imgUnderCoat.setRGB((iTile*width + x1 - x), (jTile*height + y1 - y), p);
                    }
                    // fillet bottom right
                    y2 -= diffY;
                    for (int theta = 0; theta < 90; theta++) {
                        int y = (int) (diffY * Math.cos(theta * Math.PI / 180));
                        int x = (int) (diffX * Math.sin(theta * Math.PI / 180));
                        imgUnderCoat.setRGB((iTile*width + x2 + x), (jTile*height + y2 + y), p);
                    }
                    // fillet bottom left
                    for (int theta = 0; theta < 90; theta++) {
                        int y = (int) (diffY * Math.cos(theta * Math.PI / 180));
                        int x = (int) (diffX * Math.sin(theta * Math.PI / 180));
                        imgUnderCoat.setRGB((iTile*width + x1 - x), (jTile*height + y2 + y), p);
                    }
                    // draw vertical lines
                    x1 = width/2 - panWidth/2;
                    y1 = height/2 - straitHeight/2;
                    x2 = x1 + panWidth;
                    y2 = y1 + straitHeight;
                    for(int j=y1;j<y2;j++){
                        imgUnderCoat.setRGB(iTile*width+x1,jTile*height+j,p);
                        imgUnderCoat.setRGB(iTile*width+x2,jTile*height+j,p);
                    }




                    // now do object overlays
                    // cockpit window
                    if(iTile==cockpitI && jTile==cockpitJ){
                        // create border first
                        int borHeight = (int) (width*0.5);
                        int borWidth = (int) (width/2);
                        int borThick = (int) (borHeight/10);
                        Polygon polygon = new Polygon();
                        x1 = iTile + width/2;
                        x2 = x1 + borWidth;
                        y1 = jTile + height/2 - borHeight/2;
                        y2 = y1 + borHeight;
                        polygon.addPoint(x1,y1);
                        polygon.addPoint(x2,y1);
                        polygon.addPoint(x2,y2);
                        polygon.addPoint(x1,y2);
                        r1 = 1;
                        g1 = 1;
                        b1 = 1;
                        a1 = 255;
                        p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                        for(int i=0;i<width;i++){
                            for(int j=0;j<height;j++){
                                Point point = new Point(i,j);
                                if(polygon.contains(point)){
                                    // pixel is within current sector
                                    imgFront.setRGB(iTile*width+i,jTile*width+j,p);
                                }
                            }
                        }
                        polygon = new Polygon();
                        int winWidth = borWidth - 2*borThick;
                        int winHeight = borHeight - 2*borThick;
                        x1 = x1 + borThick;
                        x2 = x1 + winWidth;
                        y1 = y1 + borThick;
                        y2 = y1 + winHeight;
                        polygon.addPoint(x1,y1);
                        polygon.addPoint(x2,y1);
                        polygon.addPoint(x2,y2);
                        polygon.addPoint(x1,y2);
                        r1 = 255;
                        a1 = 255;
                        g1 = 255;
                        for(int i=0;i<width;i++){
                            for(int j=0;j<height;j++){
                                Point point = new Point(i,j);
                                if(polygon.contains(point)){
                                    // pixel is within current sector
                                    double c = i;
                                    b1 = (int) (255*((c/(width/2))-1));
                                    p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                                    imgFront.setRGB(iTile*width+i,jTile*width+j,p);
                                }
                            }
                        }
                    }

                    // draw light on ship side
                    boolean light = true;
                    int midX = (int) (width/2);
                    int midY = midX;
                    r1 = 255;
                    a1 = 255;
                    g1 = 255;
                    b1 = 255;
                    if(light){
                        for(int i=0;i<width;i++){
                            for(int j=0;j<height;j++){
                                double dist = Math.sqrt( Math.pow((i-midX),2) + Math.pow((j-midY),2) );
                                double maxDist = width/5;
                                a1 = (int) (255*(1-dist/maxDist));
                                if(a1<0){
                                    a1=0;
                                }
                                p = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1; //pixel
                                imgFront.setRGB(iTile*width+i,jTile*width+j,p);
                            }
                        }
                    }



                }
            }
        }

        // paint both images, preserving the alpha channels
        Graphics g1 = imgFlat.getGraphics();
        g1.drawImage(imgBack, 0, 0, null);
        g1.drawImage(img, 0, 0, null);
        g1.drawImage(imgUnderCoat, 0, 0, null);
        g1.drawImage(imgFront, 0, 0, null);

        File f = null;

        //write image
        try {
            f = new File("C:\\Image\\squareSectors.png");
            ImageIO.write(imgFlat, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

        //write image
        try {
            f = new File("C:\\Image\\squareSectorsComp.png");
            ImageIO.write(imgCompare, "png", f);
        } catch (
                IOException e) {
            System.out.println("Error: " + e);
        }

    }

    private int getMinimum(Set<Integer> vertexes, double diffR) {
        int minimum = -1;
        for (Integer vertex : vertexes) {
            if (minimum == -1) {
                minimum = vertex;
            } else {
                double tempR = getRadialDist(currentN, vertex);
                if (tempR < diffR) {
                    minimum = vertex;
                    diffR = tempR;
                }
            }
        }
        return minimum;
    }

        public double getRadialDist(int node1, int node2){
            double radialDist = 0;
            double diffX = points[0][node1] - points[0][node2];
            double diffY = points[1][node1] - points[1][node2];

            radialDist = Math.sqrt( Math.pow(diffX,2) + Math.pow(diffY,2) );

            return radialDist;
        }

        public void abandonedMain(){

            points[0][0] = 0;
            points[1][0] = 40;

            points[0][1] = 30;
            points[1][1] = 60;

            points[0][2] = 50;
            points[1][2] = 80;

            points[0][3] = 75;
            points[1][3] = 75;

            points[0][4] = 100;
            points[1][4] = 50;

            points[0][5] = 60;
            points[1][5] = 20;

            points[0][6] = 25;
            points[1][6] = 30;

            int nodes[] = new int[7];
            for(int i=0;i<7;i++){
                nodes[i]=i;
            }

            int order[] = new int[7];

            LinkedHashSet firstNodes = new LinkedHashSet<Integer>();
            LinkedHashSet secondNodes = new LinkedHashSet<Integer>();
            LinkedHashSet settledNodes = new LinkedHashSet<Integer>();
            HashSet unSettledNodes = new HashSet<Integer>();

            int leftest = 200;
            int leftestN = 0;

            for(int i=0;i<nodes.length;i++){
                unSettledNodes.add(nodes[i]);
                if(points[0][nodes[i]]<leftest){
                    leftestN = nodes[i];
                    leftest = points[0][nodes[i]];
                }
            }

            int rightest = -1;
            int rightestN = 0;

            for(int i=0;i<nodes.length;i++){
                if(points[0][nodes[i]]>rightest){
                    rightestN = nodes[i];
                    rightest = points[0][nodes[i]];
                }
            }

            // remove leftest starting node
            unSettledNodes.remove(leftestN);
            diffR = 200;
            currentN = leftestN;
            order[0] = leftestN;
            int count = 1;
            boolean top = true;
            firstNodes.add(leftestN);

            while (unSettledNodes.size() > 0) {
                System.out.println("unsettlednode size "+unSettledNodes.size());
                int node = getMinimum(unSettledNodes,diffR);
                settledNodes.add(node);
                unSettledNodes.remove(node);
                if(top){
                    firstNodes.add(node);
                    if(node == rightestN){
                        // reached other side
                        secondNodes.add(node);
                        top = false;
                    }
                } else {
                    secondNodes.add(node);
                }
                count++;
                currentN = node;
            }
            secondNodes.add(leftestN);

            int width = 100;
            int height = 100;

            // this is for the behind section of engine
            BufferedImage img1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);



            for(int i=0;i<width;i++){
                for(int j=0;j<height;j++){



                }
            }

        }


    }
