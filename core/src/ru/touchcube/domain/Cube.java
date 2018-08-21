package ru.touchcube.domain;

import ru.touchcube.domain.utils.Color;
import ru.touchcube.domain.utils.V3;

/**
 * Created by grish on 21.08.2018.
 */

public class Cube {

    // indexes of cube's sides
    // 0: z-1
    // 1: x+1
    // 2: z+1
    // 3: x-1
    // 4: y-1
    // 5: y+1

    private V3 coordinates;
    private Color color;
    private boolean[] drawSides;

    public Cube(V3 coordinates, Color color) {
        this.coordinates=coordinates;
        this.color = color;
        setDrawSides(new boolean[]{true, true, true, true, true, true});
    }

    public V3 getPosition(){
        return coordinates;
    }

    public Color getColor() {
        return color;
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

    public void setColor(Color color) {
        this.color = color;
    }

}
