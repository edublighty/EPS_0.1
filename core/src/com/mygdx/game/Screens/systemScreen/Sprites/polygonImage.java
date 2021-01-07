package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class polygonImage extends Image {


    public World world;
    public Body b2body;
    private Array<Vector2> vertices;
    private Array<Vector3> shapes;
    private Vector2 centre;
    private float theta;
    private Array<Vector2> rectCorners;
    private Array<Vector2> rectEdges;
    private Array<Vector2> edgesOut;
    private boolean collision;
    private String type;

    public polygonImage(MyGdxGame game, World world, systemScreen2 screen, float toteSize, String level, float startX, float startY){
        super(screen.getShipIconsAt().findRegion(level));
        type = level;
        setBounds(0,0,toteSize/(MyGdxGame.PPM*100),toteSize/(MyGdxGame.PPM*100));
        setOrigin(getWidth()/2,getHeight()/2);
    }

    public void setVertices(Array<Vector2> vertices) {

        this.vertices = vertices;
        setOutsideEdges();

    }

    public Array<Vector2> getVertices(){
        return vertices;
    }

    public void setShapes(Array<Vector3> shapes) { this.shapes = shapes; }

    public Array<Vector3> getShapes() { return shapes; }

    public void rotateBody(float theta){
        this.theta = theta;
    }

    public boolean getCollision(){ return collision;  }

    public void setCollision(boolean collision){
        this.collision = collision;
    }

    public void setOutsideEdges(){
        Array<Vector2> edges = new Array<>();
        edgesOut = new Array<Vector2>();
        for(int i=0;i<vertices.size;i++){
            int[] tempInt = new int[2];
            if(i<vertices.size-1){
                tempInt[0]=i;
                tempInt[1]=i+1;
            } else {
                tempInt[0] = i;
                tempInt[1] = 0;
            }
            edgesOut.add(new Vector2(tempInt[0],tempInt[1]));
        }
    }

    public Array<Vector2> getOutsideEdges(){ return edgesOut; };

    public void setRectVertices(){
        // this is for simple polygon like player that features 8 vertices
        // four at the x/y centres - no contribution to angular displacement
        // four at the corners - contributing to angular displacement
        rectEdges = new Array<>();
        rectCorners = new Array<>();
        // Edges first
        rectEdges.add(new Vector2(getCentre().x,getCentre().y+this.getHeight()/2));
        rectEdges.add(new Vector2(getCentre().x,getCentre().y-this.getHeight()/2));
        rectEdges.add(new Vector2(getCentre().x+getWidth()/2,getCentre().y));
        rectEdges.add(new Vector2(getCentre().x-getWidth()/2,getCentre().y));
        // Then corners
        rectCorners.add(new Vector2(getCentre().x+this.getWidth()/2,getCentre().y+this.getHeight()/2));
        rectCorners.add(new Vector2(getCentre().x-this.getWidth()/2,getCentre().y+this.getHeight()/2));
        rectCorners.add(new Vector2(getCentre().x-this.getWidth()/2,getCentre().y-this.getHeight()/2));
        rectCorners.add(new Vector2(getCentre().x+this.getWidth()/2,getCentre().y-this.getHeight()/2));
    }

    public Array<Vector2> getRectCorners(){ return rectCorners; }

    public Array<Vector2> getRectEdges(){ return rectEdges; }

    public void setCentre(float centreX, float centreY){
        centre = new Vector2(centreX,centreY);
        setRectVertices();
    }

    public Vector2 getCentre(){
        Vector2 vec = centre;
        return vec;
    }

    public boolean insideView(Vector2 vec1,Vector2 vec2,Vector2 vecP){
        // vector vec1 and vec2 are from middle vertex to other vertices of triangle
        // vector vecP is from middle vertex to point P potentially in sight
        boolean inSight = false;
        double checkVal1 = (vec1.x*vecP.y - vec1.y*vecP.x)*(vec1.x*vec2.y - vec1.y*vec2.x);
        double checkVal2 = (vec2.x*vecP.y - vec2.y*vecP.x)*(vec2.x*vec1.y - vec2.y*vec1.x);
        if(checkVal1>0 && checkVal2>0){
            // vector P is inside vectors vec1 and vec2
            inSight = true;
        }
        return inSight;
    }

    public int checkInsidePoly(Vector2 P) {

        // start by assuming outside shape
        double dxtemp = P.x - getCentre().x;
        double dytemp = P.y - getCentre().y;
        double drtemp = Math.sqrt(Math.pow(dxtemp,2)+Math.pow(dytemp,2));
        if(P.y<-1075){
            System.out.println("in shape?");
        }
        boolean insideShape = false;
        boolean searching = true;
        int chosenShape = -1;
        boolean isInsidePoly = false;
        boolean isInsidePolyX;
        boolean isInsidePolyY;
        boolean isInsidePolyZ;
        // The shape is divided into triangles
        // Check each poly whether inside
        for(int i=0;i<shapes.size;i++){
            // for each vertex on triangle, check cross product if point inside angle of view
            // assume outside
            isInsidePolyX = false;
            isInsidePolyY = false;
            isInsidePolyZ = false;
            // node numbers of current poly shape
            Vector3 curPoly = shapes.get(i);
            // coordinates of each point
            Vector2 pointX = vertices.get((int) curPoly.x);
            Vector2 pointY = vertices.get((int) curPoly.y);
            Vector2 pointZ = vertices.get((int) curPoly.z);
            // Vectors between the points
            Vector2 XY = new Vector2((pointY.x - pointX.x), (pointY.y - pointX.y));
            Vector2 YZ = new Vector2((pointZ.x - pointY.x), (pointZ.y - pointY.y));
            Vector2 ZX = new Vector2((pointX.x - pointZ.x), (pointX.y - pointZ.y));
            // start with x as middle vertex
            Vector2 vec1 = new Vector2((pointY.x - pointX.x), (pointY.y - pointX.y));
            Vector2 vec2 = new Vector2((pointZ.x - pointX.x), (pointZ.y - pointX.y));
            Vector2 vecP = new Vector2((P.x - pointX.x), (P.y - pointX.y));
            boolean insideView = insideView(vec1, vec2, vecP);
            if (insideView) {
                isInsidePolyX = true;
            }
            // start with y as middle vertex
            vec1 = new Vector2((pointZ.x - pointY.x), (pointZ.y - pointY.y));
            vec2 = new Vector2((pointX.x - pointY.x), (pointX.y - pointY.y));
            vecP = new Vector2((P.x - pointY.x), (P.y - pointY.y));
            insideView = insideView(vec1, vec2, vecP);
            if (insideView) {
                isInsidePolyY = true;
            }
            // start with z as middle vertex
            vec1 = new Vector2((pointX.x - pointZ.x), (pointX.y - pointZ.y));
            vec2 = new Vector2((pointY.x - pointZ.x), (pointY.y - pointZ.y));
            vecP = new Vector2((P.x - pointZ.x), (P.y - pointZ.y));
            insideView = insideView(vec1, vec2, vecP);
            if (insideView) {
                isInsidePolyZ = true;
            }
            /*
            if(isInsidePoly){
                // covers if find inside poly early or we were inside literally the final poly
                searching = false;
                insideShape = true;
            }
            */
            if(isInsidePolyX && isInsidePolyY && isInsidePolyZ){
                // inside the shape
                insideShape = true;
                chosenShape = i;
            }
        }

        return chosenShape;

    }


    public void checkInsidePolyOrig(Vector2 P){

        // wn_PnPoly(): winding number test for a point in a polygon
//      Input:   P = a point,
//               V[] = vertex points of a polygon V[n+1] with V[n]=V[0]
//      Return:  wn = the winding number (=0 only when P is outside)
//
        /*

        int    wn = 0;    // the  winding number counter
        int n = this.vertices.size-1;

        // loop through all edges of the polygon
        for (int i=0; i<n; i++) {   // edge from V[i] to  V[i+1]
            if (vertices.get(i).y <= P.y) {          // start y <= P.y
                if (vertices.get(i+1).y  > P.y)      // an upward crossing
                    if (isPointLeft( vertices.get(i), vertices.get(i+1), P) > 0)  // P left of  edge
                        ++wn;            // have  a valid up intersect
            } else {                        // start y > P.y (no test needed)
                if (vertices.get(i+1).y  <= P.y)     // a downward crossing
                    if (isPointLeft( vertices.get(i), vertices.get(i+1), P) < 0)  // P right of  edge
                        --wn;            // have  a valid down intersect
            }
        }

        if(wn==0){
            return false;
        } else {
            return true;
        }

        */

    }

    public float isPointLeft(Vector2 P1, Vector2 P2,Vector2 P0){
        // isLeft(): tests if a point is Left|On|Right of an infinite line.
//    Input:  three points P0, P1, and P2
//    Return: >0 for P2 left of the line through P0 and P1
//            =0 for P2  on the line
//            <0 for P2  right of the line
//    See: Algorithm 1 "Area of Triangles and Polygons"

        float result = ( (P1.x - P0.x) * (P2.y - P0.y) - (P2.x -  P0.x) * (P1.y - P0.y) );


        return result;

    }
}
