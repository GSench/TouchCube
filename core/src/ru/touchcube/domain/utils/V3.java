package ru.touchcube.domain.utils;

/**
 * Created by grish on 21.08.2018.
 */

public class V3 {

    private int xi;
    private int yi;
    private int zi;

    public int x(){return xi;}
    public int y(){return yi;}
    public int z(){return zi;}

    public V3(int x, int y, int z) {
        this.xi = x;
        this.yi = y;
        this.zi = z;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof V3))return false;
        V3 otherV3 = (V3) other;
        return otherV3.x()==xi&&otherV3.y()==yi&&otherV3.z()==zi;
    }

}
