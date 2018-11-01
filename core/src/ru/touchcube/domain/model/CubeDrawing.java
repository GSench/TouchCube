package ru.touchcube.domain.model;

/**
 * Created by grish on 21.08.2018.
 * Abstract class of cube to be realized in view's layer.
 * It implements cube's lifecycle.
 * In view's layer it should be an instance to draw.
 */

public abstract class CubeDrawing {

    private Cube cube;

    public CubeDrawing(Cube cube) {
        this.cube = cube;
    }

    public Cube getCube() {
        return cube;
    }

    public abstract void onDelete();

    public abstract void onCreate();

    public abstract void onColorChanged();

}
