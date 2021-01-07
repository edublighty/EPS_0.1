package com.mygdx.game.Screens.systemScreen.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.systemScreen.systemScreen2;

public class playerImage extends Image {


    public World world;
    public Body b2body;
    private Array<Vector2> vertices;
    private float centreX;
    private float centreY;

    public playerImage(MyGdxGame game, World world, systemScreen2 screen, float toteSize,String level,float startX,float startY){
        super(screen.getShipIconsAt().findRegion(level));
        this.world = world;
        defineSprite(toteSize,startX,startY,true,"player");
        setBounds(0,0,toteSize/(MyGdxGame.PPM*100),toteSize/(MyGdxGame.PPM*100));
        setOrigin(getWidth()/2,getHeight()/2);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
    }

    public void setVertices(Array<Vector2> vertices) {
        this.vertices = vertices;
    }

    public void setCentre(float centreX, float centreY){
        this.centreX = centreX;
        this.centreY = centreY;
    }

    public Vector2 getCentre(){
        Vector2 vec = new Vector2(centreX,centreY);
        return vec;
    }

    public void defineSprite(float toteSize,float startX,float startY,boolean circleShape,String bodyType){
        if(circleShape) {
            BodyDef bdef = new BodyDef();
            bdef.position.set(startX, startY);
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(1 / MyGdxGame.PPM);
            fdef.filter.categoryBits = MyGdxGame.SHIP_BIT;
            fdef.filter.maskBits = MyGdxGame.DEFAULT_BIT | MyGdxGame.STAR_BIT | MyGdxGame.sDest_BIT;

            fdef.shape = shape;
            b2body.createFixture(fdef);
/*
            EdgeShape front = new EdgeShape();
            front.set(new Vector2(5 / MyGdxGame.PPM, 5 / MyGdxGame.PPM), new Vector2(5 / MyGdxGame.PPM, -5 / MyGdxGame.PPM));
            fdef.shape = front;
            */
            fdef.isSensor = true;   // no longer collides as sensor
            //b2body.createFixture(fdef).setUserData("front");

        } else {
            BodyDef bdef = new BodyDef();
            bdef.position.set(startX, startY);
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            //shape.set(vertices);

            fdef.shape = shape;
            b2body.createFixture(fdef);
            fdef.isSensor = true;   // no longer collides as sensor
            b2body.createFixture(fdef).setUserData(bodyType);
        }
    }

    public boolean checkInsidePoly(Vector2 P){

        // wn_PnPoly(): winding number test for a point in a polygon
//      Input:   P = a point,
//               V[] = vertex points of a polygon V[n+1] with V[n]=V[0]
//      Return:  wn = the winding number (=0 only when P is outside)
//
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
