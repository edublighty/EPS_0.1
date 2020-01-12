package com.mygdx.game.Screens.galScreen.Tools.dijkstra;

import java.io.Serializable;

public class dVertex implements Serializable {
    final private String id;
    final private String name;
    final private int id2;
    final private float x;
    final private float y;

    public dVertex(String id, String name, int id2,float x,float y) {
        this.id = id;
        this.name = name;
        this.id2 = id2;
        this.x = x;
        this.y = y;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getX() { return x; }

    public float getY() { return y; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        dVertex other = (dVertex) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
}
