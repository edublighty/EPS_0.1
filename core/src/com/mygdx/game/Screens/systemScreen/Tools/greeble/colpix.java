package com.mygdx.game.Screens.systemScreen.Tools.greeble;

public class colpix {

    // coordinates
    int XY[][][] = new int[2][2][2]; // [i][j][x/y]
    double x11;  // x bottom left
    double y11;  // y bottom left
    double x21;  // x bottom right
    double y21;  // y bottom right
    double x12; // x top left
    double y12; // y top left
    double x22; // x top right
    double y22; // y top right
    double i;   // nth from left
    double j;   // nth from bottom

    // colour properties
    int r;
    int g;
    int b;
    int a;

    public colpix(int x1,int x2,int y1,int y2,int a,int r,int g,int b,int i,int j){

        // initialise coordinates
        XY[0][0][0] = x1;
        XY[0][1][0] = x1;
        XY[1][0][0] = x2;
        XY[1][1][0] = x2;
        XY[0][0][1] = y1;
        XY[1][0][1] = y1;
        XY[0][1][1] = y2;
        XY[1][1][1] = y2;

        // initialise colour
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;

    }

    public void updateCoords(int[][][] newPos){
        // update coordinates
        XY = newPos;
    }

    public void updateColour(int a,int r,int g,int b){
        // update colour
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int[][][] getCoords(){
        int[][][] coords =  XY;

        return coords;
    }

    public int getColour(){
        int pixel;
        //System.out.println("colpix a "+a+" r "+r+" g "+g+" b "+b);
        pixel = (a << 24) | (r << 16) | (g << 8) | b; //pixel

        return pixel;
    }

}
