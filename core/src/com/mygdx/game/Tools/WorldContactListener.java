package com.mygdx.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.HUDs.Hud;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.atmosPlay;
import com.mygdx.game.Screens.orbitPlay;
import com.mygdx.game.Screens.systemScreen.systemPlay;
import com.mygdx.game.TileObjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {
    public String stellaObj;
    public MyGdxGame game;
    public orbitPlay oPlay;
    @Override
    public void beginContact(Contact contact) {
        // called when two fixtures begin to collide
        //Gdx.app.log("Begin contact","");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        System.out.println(fixA.getBody()+" has hit "+ fixB.getBody());

        if(fixA.getBody().getUserData() == "star"){
            // fixture A is star
            System.out.println("Star!");
            stellaObj = "star";
        }else if(fixB.getBody().getUserData() == "star"){
            // fixture B is star
            System.out.println("Star!");
            stellaObj = "star";
        }

        if(fixA.getBody().getUserData() == "planet"){
            // fixture A is planet
            System.out.println("PLANET!");
            stellaObj = "planet";
        }else if(fixB.getBody().getUserData() == "planet"){
            // fixture B is planet
            System.out.println("PLANET!");
            stellaObj = "planet";
        }

        if(fixA.getBody().getUserData() == "station"){
            // fixture A is planet
            System.out.println("STATIon!");
            stellaObj = "station";
        }else if(fixB.getBody().getUserData() == "station"){
            // fixture B is planet
            System.out.println("STATION!");
            stellaObj = "station";
        }

        if(fixA.getBody().getUserData() == "warning"){
            // fixture A is planet
            System.out.println("WARNING!");
            stellaObj = "warning";
        }else if(fixB.getBody().getUserData() == "warning"){
            // fixture B is planet
            System.out.println("WARNING!");
            stellaObj = "warning";
        }

        if(fixA.getBody().getUserData() == "atmos"){
            // fixture A is planet
            System.out.println("ATMOSPHERE!");
            stellaObj = "atmos";
        }else if(fixB.getBody().getUserData() == "atmos"){
            // fixture B is planet
            System.out.println("ATMOSPHERE!");
            stellaObj = "atmos";
        }

        if(fixA.getBody().getUserData() == "bldg"){
            // fixture A is planet
            System.out.println("BUILDING!");
            stellaObj = "bldg";
        }else if(fixB.getBody().getUserData() == "bldg"){
            // fixture B is planet
            System.out.println("BUILDING!");
            stellaObj = "bldg";
        }

        if(fixA.getUserData() == "front" || fixB.getUserData() == "front"){
            Fixture front = fixA.getUserData() == "front" ? fixA : fixB;
            Fixture object = front == fixA ? fixB :fixA;
            // above logic determines which one is the ship front and other is the object

            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject) object.getUserData()).frontHit();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        // when two fixtures end contact
        //Gdx.app.log("End contact","");
        //System.out.println("end contact");
        Hud.burnFlag = false;
        systemPlay.planetFlag = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // change characteristics of collision on contact
        //System.out.println("preSolve");
        contact.setEnabled(false);
        if(stellaObj=="star"){
            Hud.burnFlag = true;
        }
        if(stellaObj=="planet"){
            systemPlay.planetFlag = true;
        }
        if(stellaObj=="warning"){

        }
        if(stellaObj=="atmos"){
            // enter atmosphere
            //toAtmos();
            //oPlay.setAtmos();
            orbitPlay.inAtmos = true;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // result of collision e.g. angle of break off
        System.out.println("post solving");
        /*if(stellaObj=="atmos"){
            // enter atmosphere
            System.out.println("going atmos");
            oPlay.setAtmos();
        }*/
    }

    public void setGame(MyGdxGame gam, orbitPlay play){
        this.game = gam;
        this.oPlay = play;
    }

    public void toAtmos(){
        game.setScreen(new atmosPlay(game));
    }
}
