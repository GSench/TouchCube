package ru.touchcube.domain;

/**
 * Created by grish on 21.08.2018.
 */

public abstract class Cube {

    private int x;
    private int y;
    private int z;
    private float color;
    private boolean noColor;
    private boolean[] drawSides;

    public Cube(int x, int y, int z, float color, boolean noColor) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.noColor = noColor;
        setDrawSides(new boolean[]{true, true, true, true, true, true});
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getColor() {
        return color;
    }

    public boolean hasNoColor() {
        return noColor;
    }

    public boolean[] getDrawSides() {
        return drawSides;
    }

    public void setDrawSides(boolean[] drawSides) {
        this.drawSides = drawSides;
    }

    public void drawSide(int side){
        drawSides[side] = true;
    }

    public void noDrawSide(int side){
        drawSides[side] = false;
    }

}
