package com.mygdx.game.Screens.galScreen.Tools.dijkstra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class aStar implements Serializable {

    private final List<dVertex> nodes;
    private final List<dEdge> edges;
    private Set<dVertex> settledNodes;
    private Set<dVertex> unSettledNodes;
    private Map<dVertex, dVertex> predecessors;
    private Map<dVertex, Integer> distance;
    private int maxJumpDist = 500000;

    public aStar(dGraph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<dVertex>(graph.getVertexes());
        this.edges = new ArrayList<dEdge>(graph.getEdges());
    }

    public void execute(dVertex source) {
        settledNodes = new HashSet<dVertex>();
        unSettledNodes = new HashSet<dVertex>();
        distance = new HashMap<dVertex, Integer>();
        predecessors = new HashMap<dVertex, dVertex>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            System.out.println("unsettlednode size "+unSettledNodes.size());
            dVertex node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(dVertex node) {
        List<dVertex> adjacentNodes = getNeighbors(node);
        for (dVertex target : adjacentNodes) {
            //System.out.println("distance is "+getDistance(node,target));
            if(getDistance(node, target)<maxJumpDist){
                if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                    distance.put(target, getShortestDistance(node) + getDistance(node, target));
                    predecessors.put(target, node);
                    unSettledNodes.add(target);
                }
            }
        }

    }

    private int getDistance(dVertex node, dVertex target) {
        for (dEdge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<dVertex> getNeighbors(dVertex node) {
        List<dVertex> neighbors = new ArrayList<dVertex>();
        for (dEdge edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }

    private dVertex getMinimum(Set<dVertex> vertexes) {
        dVertex minimum = null;
        for (dVertex vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(dVertex vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(dVertex destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<dVertex> getPath(dVertex target) {
        LinkedList<dVertex> path = new LinkedList<dVertex>();
        dVertex step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
