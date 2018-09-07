package ru.touchcube.domain.services;

import java.util.ArrayList;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.interactor.WorldFace;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.utils.TouchCubeUtils;
import ru.touchcube.domain.utils.function;
import ru.touchcube.domain.utils.function_get;

/**
 * Created by grish on 21.08.2018.
 */

public class TouchCubeWorld {

    private static final int MODE_PUT = 1;
    private static final int MODE_DELETE = 2;
    private static final int MODE_PAINT = 3;

    private WorldFace face;
    private SystemInterface system;
    private function_get<Color> currentColor;

    private final ArrayList<CubeDrawing> cubes = new ArrayList<CubeDrawing>();

    private int mode = 0;

    public TouchCubeWorld(WorldFace face, function_get<Color> currentColor, SystemInterface system){
        this.face = face;
        this.currentColor=currentColor;
        this.system=system;
    }

    public void load(final ArrayList<Cube> cubesToLoad){
        face.stopRendering();
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                synchronized (cubes){
                    for(CubeDrawing cube: cubes) cube.onDelete();
                    cubes.clear();
                    for(Cube cube: cubesToLoad) put(cube);
                }
                face.continueRendering();
            }
        });
    }

    public void isPutMode(){
        mode=MODE_PUT;
    }

    public void isPaintMode(){
        mode=MODE_PAINT;
    }

    public void isDeleteMode(){
        mode=MODE_DELETE;
    }

    public ArrayList<CubeDrawing> getCubes() {
        return cubes;
    }

    public void tapOnCube(CubeDrawing touched, int side){
        switch (mode){
            case MODE_PUT:
                V3 near = TouchCubeUtils.getNearCubeCoordinates(touched.getCube(), side);
                if(getCubeOn(near)==null){
                    put(near, currentColor.get());
                    checkSidesFor(cubes.get(cubes.size()-1));
                }
                break;
            case MODE_PAINT:
                paint(touched, currentColor.get());
                checkSidesFor(touched);
                break;
            case MODE_DELETE:
                remove(touched);
                checkSidesFor(touched);
                break;
        }
    }

    public void onClearButton(){
        synchronized (cubes){
            for(CubeDrawing cube: cubes) cube.onDelete();
            cubes.clear();
        }
    }

    public void reloadCurrent() {
        synchronized (cubes){
            ArrayList<Cube> load = new ArrayList<Cube>(cubes.size());
            for(CubeDrawing cubeDrawing: cubes) load.add(cubeDrawing.getCube());
            for(CubeDrawing cube: cubes) cube.onDelete();
            cubes.clear();
            for(Cube cube: load) put(cube);
        }
    }

    private CubeDrawing getCubeOn(V3 v){
        for(CubeDrawing cubeDrawing: cubes)
            if(cubeDrawing.getCube().getPosition().equals(v)) return cubeDrawing;
        return null;
    }

    private void put(V3 pos, Color color){
        put(new Cube(pos, color));
    }

    private void put(Cube newCube){
        cubes.add(face.onCubeAdded(newCube));
        cubes.get(cubes.size()-1).onCreate();
    }

    private void remove(CubeDrawing cube){
        cubes.remove(cube);
        cube.onDelete();
    }

    private void paint(CubeDrawing cube, Color currentColor){
        cube.getCube().setColor(currentColor);
        cube.onColorChanged();
    }

    private void checkSidesFor(CubeDrawing cube){
        int checkedSides = 0;
        for(CubeDrawing otherC: cubes){ //looping on cubes
            if(TouchCubeUtils.pointsAreNear(cube.getCube().getPosition(), otherC.getCube().getPosition())){ //checking "otherC" is near "cube"
                for(int i=0; i<6; i++){ //looping on sides
                    if(otherC.getCube().getPosition().equals(TouchCubeUtils.getNearCubeCoordinates(cube.getCube(), i))){ //defining exact side
                        //cube "otherC" is near "cube" on cube's "i" side
                        Color c = cube.getCube().getColor();
                        Color nearC = otherC.getCube().getColor();
                        switch (mode){
                            case MODE_PUT:
                            case MODE_PAINT:
                                if(((nearC.a()<230)&(!nearC.noColor()))&((c.a()>230)|(c.noColor()))){
                                    cube.getCube().drawSide(i);
                                    otherC.getCube().noDrawSide(TouchCubeUtils.reverseSide(i));
                                } else if(((nearC.a()>230)|(nearC.noColor()))&((c.a()<230)&(!c.noColor()))){
                                    cube.getCube().noDrawSide(i);
                                    otherC.getCube().drawSide(TouchCubeUtils.reverseSide(i));
                                } else if(((nearC.a()>230)|(nearC.noColor()))&((c.a()>230)|(c.noColor()))){
                                    cube.getCube().noDrawSide(i);
                                    otherC.getCube().noDrawSide(TouchCubeUtils.reverseSide(i));
                                } else if(((nearC.a()<230)&(!nearC.noColor()))&((c.a()<230)&(!c.noColor()))){
                                    cube.getCube().drawSide(i);
                                    otherC.getCube().drawSide(TouchCubeUtils.reverseSide(i));
                                }
                                break;
                            case MODE_DELETE:
                                otherC.getCube().drawSide(TouchCubeUtils.reverseSide(i));
                                break;
                        }
                        checkedSides++; //counting checked side of "cube"
                        break;
                    }
                }
            }
            if(checkedSides>=6) break; //stopping looping on cubes if 6 sides of "cube" are checked
        }
    }

}
