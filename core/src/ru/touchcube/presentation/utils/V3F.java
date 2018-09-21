package ru.touchcube.presentation.utils;

public class V3F {

    private float xi;
    private float yi;
    private float zi;

    public float x(){return xi;}
    public float y(){return yi;}
    public float z(){return zi;}

    public V3F(float x, float y, float z) {
        this.xi = x;
        this.yi = y;
        this.zi = z;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof V3F))return false;
        V3F otherV3 = (V3F) other;
        return otherV3.x()==xi&&otherV3.y()==yi&&otherV3.z()==zi;
    }

}
