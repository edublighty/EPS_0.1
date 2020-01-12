package com.mygdx.game.Screens.galScreen.Tools.dijkstra;

import java.io.Serializable;

public class dEdge implements Serializable {
    private final String id;
    private final dVertex source;
    private final dVertex destination;
    private final int weight;

    public dEdge(String id, dVertex source, dVertex destination, int weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }
    public dVertex getDestination() {
        return destination;
    }

    public dVertex getSource() {
        return source;
    }
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }
}
