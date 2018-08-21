package ru.touchcube.domain;

import java.util.ArrayList;

import ru.touchcube.domain.presenter.WorldPresenter;
import ru.touchcube.domain.utils.Color;
import ru.touchcube.domain.utils.V3;

/**
 * Created by grish on 21.08.2018.
 */

public class TouchCubeWorld {

    public static final int MODE_PUT = 1;
    public static final int MODE_DELETE = 2;
    public static final int MODE_PAINT = 3;

    private WorldPresenter presenter;
    private SystemInterface system;

    private ArrayList<CubeDrawing> cubes;

    int mode = 0;
    private Color currentColor;

    public TouchCubeWorld(WorldPresenter presenter, SystemInterface system){
        cubes = new ArrayList<CubeDrawing>();
        this.presenter=presenter;
        this.system=system;
        currentColor = new Color(0,0,0,0, true);
    }

    public void start(){
        presenter.init();
        loadFromCash();
    }

    //TODO
    private void loadFromCash(){
        final TouchCubeWorld world = this;
        /**
        system.doOnBackground(new function<Void>(){
            @Override
            public void run(Void... params) {
                final ArrayList<Cube> cubes = presenter.loadFromCash();
                system.doOnForeground(new function<Void>() {
                    @Override
                    public void run(Void... params) {
                        if(cubes!=null) world.cubes=cubes;
                    }
                });
            }
        });*/
    }

    public ArrayList<CubeDrawing> getCubes() {
        return cubes;
    }

    public void tapOnCube(CubeDrawing touched, int side){
        switch (mode){
            case MODE_PUT:
                V3 near = TouchCubeUtils.getNearCubeCoordinates(touched.getCube(), side);
                if(getCubeOn(near)==null){
                    put(near, currentColor);
                    checkSidesFor(cubes.get(cubes.size()-1));
                }
                break;
            case MODE_PAINT:
                paint(touched);
                checkSidesFor(touched);
                break;
            case MODE_DELETE:
                remove(touched);
                checkSidesFor(touched);
                break;
        }
    }

    private CubeDrawing getCubeOn(V3 v){
        for(CubeDrawing cubeDrawing: cubes)
            if(cubeDrawing.getCube().getPosition().equals(v)) return cubeDrawing;
        return null;
    }

    private void put(V3 pos, Color color){
        put(pos, color, null);
    }

    private void put(V3 pos, Color color, boolean[] drawSides){
        Cube newCube = new Cube(pos, color);
        if(drawSides!=null) newCube.setDrawSides(drawSides);
        cubes.add(presenter.onCubeAdded(newCube));
        cubes.get(cubes.size()-1).onCreate();
    }

    private void remove(CubeDrawing cube){
        cubes.remove(cube);
        cube.onDelete();
    }

    private void paint(CubeDrawing cube){
        cube.getCube().setColor(currentColor);
        cube.onColorChanged();
    }

    private void checkSidesFor(CubeDrawing cube){
        for(CubeDrawing cubeDrawing: cubes){
            for(int i=0; i<6; i++){
                if(cubeDrawing.getCube().getPosition().equals(TouchCubeUtils.getNearCubeCoordinates(cube.getCube(), i))){
                    //cube "cubeDrawing" is near "cube" on cube's "i" side
                    switch (mode){
                        case MODE_PUT:
                        case MODE_PAINT:
                            //TODO
                            break;
                        case MODE_DELETE:
                            //TODO
                            break;
                    }
                }
            }
        }
    }

}
