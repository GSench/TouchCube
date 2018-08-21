package ru.touchcube.domain;

/**
 * Created by grish on 21.08.2018.
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
