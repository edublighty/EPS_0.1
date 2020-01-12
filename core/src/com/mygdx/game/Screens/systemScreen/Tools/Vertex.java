package com.mygdx.game.Screens.systemScreen.Tools;

import com.mygdx.game.Screens.battleDeck.battleShipScreen;

import java.util.ArrayList;


/**
 004
 * This class models a vertex in a graph. For ease of
 005
 * the reader, a label for this vertex is required.
 006
 * Note that the Graph object only accepts one Vertex per label,
 007
 * so uniqueness of labels is important. This vertex's neighborhood
 008
 * is described by the Edges incident to it.
 009
 *
 010
 * @author Michael Levet
011
 * @date June 09, 2015
012
 */



public class Vertex {
    private ArrayList<Edge> neighborhood;
    private String label;
    public enum vertexRoom{room,door,tile};
    public String roomName;
    public vertexRoom thisRoom;
    // tileNo:
    // if ROOM: describes number of tiles ie 1x2
    // if TILE: describes position in grid ie (0,0) is bottom left tile in room
    // if DOOR: n/a - use BL to know which tile a vertical door is on right boundary of or horizontal door is on bottom boundary of
    public int[] tileNo = new int[2];
    public int[] BL = new int[2];   // describes coordinates of bottom left of either room, tile or door as (i,j)
    public int numROomsCount;
    public int coreRoomCount;
    public int vertexCount;
    public int[] doorVertices = new int[2];
    public String roomType;
    public String equip1;
    public String equip2;
    public boolean horizDoor;
    public double roomX;
    public double roomY;
    public double doorX;
    public double doorY;
    public String connectedTileName1;
    public String connectedTileName2;
    public int iBound;
    public int jBound;

    // room specific variables
    public boolean vacuuming;       // status of whether room is venting to space one way or another
    public double o2;
    public double roomDamage;

    // door specific variable
    public boolean airlock;     // details if the door is normal or airlock and open to space
    public int doorCount;       // number for door actor in array
    public boolean doorOpen;

    // tile specific variable
    public boolean onFire;
    public double fireHealth;
    public boolean fireDamaged;



        /**
         * @param label The unique label associated with this Vertex
         */

    public Vertex(String label){
        this.label = label;
        this.neighborhood = new ArrayList<Edge>();
        this.o2 = 100;
    }

        /**
         * This method adds an Edge to the incidence neighborhood of this graph iff
         * the edge is not already present.
         *
         * @param edge The edge to add
         */

    public void addNeighbor(Edge edge){
        if(this.neighborhood.contains(edge)){
            return;
        }
        this.neighborhood.add(edge);
    }

        /**
         * @param other The edge for which to search
         * @return true iff other is contained in this.neighborhood
         */

    public boolean containsNeighbor(Edge other){
        return this.neighborhood.contains(other);
    }

        /**
         * @param index The index of the Edge to retrieve
         * @return Edge The Edge at the specified index in this.neighborhood
         */

    public Edge getNeighbor(int index){
        return this.neighborhood.get(index);
    }

        /**
         * @param index The index of the edge to remove from this.neighborhood
         * @return Edge The removed Edge
         */

    Edge removeNeighbor(int index){
        return this.neighborhood.remove(index);
    }

        /**
         * @param e The Edge to remove from this.neighborhood
         */

    public void removeNeighbor(Edge e){
        this.neighborhood.remove(e);
    }

        /**
         * @return int The number of neighbors of this Vertex
         */

    public int getNeighborCount(){
        return this.neighborhood.size();
    }
        /**
         * @return String The label of this Vertex
         */

    public String getLabel(){
        return this.label;
    }
        /**
         * @return String A String representation of this Vertex
         */

    public String toString(){
        return "Vertex " + label;
    }

        /**
         * @return The hash code of this Vertex's label
         */

    public int hashCode(){
        return this.label.hashCode();
    }

        /**
         * @param other The object to compare
         * @return true iff other instanceof Vertex and the two Vertex objects have the same label
         */

    public boolean equals(Object other){
        if(!(other instanceof Vertex)){
            return false;
        }
        Vertex v = (Vertex)other;
        return this.label.equals(v.label);
    }

        /**
         129
         *
         130
         * @return ArrayList<Edge> A copy of this.neighborhood. Modifying the returned
        131
         * ArrayList will not affect the neighborhood of this Vertex
        132
         */

    public ArrayList<Edge> getNeighbors(){
        return new ArrayList<Edge>(this.neighborhood);
    }
}

