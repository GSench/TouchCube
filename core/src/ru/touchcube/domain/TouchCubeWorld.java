package ru.touchcube.domain;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.presenter.WorldPresenter;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.utils.TouchCubeUtils;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.utils.function;

/**
 * Created by grish on 21.08.2018.
 */

public class TouchCubeWorld {

    public static final int MODE_PUT = 1;
    public static final int MODE_DELETE = 2;
    public static final int MODE_PAINT = 3;

    private WorldPresenter presenter;
    private SystemInterface system;

    private final ArrayList<CubeDrawing> cubes = new ArrayList<CubeDrawing>();;

    int mode = 0;
    private Color currentColor;

    public TouchCubeWorld(WorldPresenter presenter, SystemInterface system){
        this.presenter=presenter;
        this.system=system;
        currentColor = new Color(0,0,0,0, true);
    }

    public void start(){
        presenter.init();
    }

    public void load(final ArrayList<Cube> cubesToLoad){
        presenter.showLoading();
        presenter.stopRendering();
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                synchronized (cubes){
                    for(CubeDrawing cube: cubes) cube.onDelete();
                    cubes.clear();
                    for(Cube cube: cubesToLoad) put(cube);
                }
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

    public void setCurrentColor(Color currentColor){
        this.currentColor=currentColor;
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

    public void onClearButton(){
        for(CubeDrawing cube: cubes) cube.onDelete();
        cubes.clear();
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
                                if(((nearC.a()<0.9)&(!nearC.noColor()))&((c.a()>0.9)|(c.noColor()))){
                                    cube.getCube().drawSide(i);
                                    otherC.getCube().noDrawSide(TouchCubeUtils.reverseSide(i));
                                } else if(((nearC.a()>0.9)|(nearC.noColor()))&((c.a()<0.9)&(!c.noColor()))){
                                    cube.getCube().noDrawSide(i);
                                    otherC.getCube().drawSide(TouchCubeUtils.reverseSide(i));
                                } else if(((nearC.a()>0.9)|(nearC.noColor()))&((c.a()>0.9)|(c.noColor()))){
                                    cube.getCube().noDrawSide(i);
                                    otherC.getCube().noDrawSide(TouchCubeUtils.reverseSide(i));
                                } else if(((nearC.a()<0.9)&(!nearC.noColor()))&((c.a()<0.9)&(!c.noColor()))){
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
