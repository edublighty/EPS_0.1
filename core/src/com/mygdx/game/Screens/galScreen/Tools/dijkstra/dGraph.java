package com.mygdx.game.Screens.galScreen.Tools.dijkstra;

import java.io.Serializable;
import java.util.List;

public class dGraph implements Serializable {

    private final List<dVertex> vertexes;
    private final List<dEdge> edges;

    public dGraph(List<dVertex> vertexes, List<dEdge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<dVertex> getVertexes() {
        return vertexes;
    }

    public List<dEdge> getEdges() {
        return edges;
    }


}
