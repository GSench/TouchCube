package ru.touchcube.domain;

import java.util.ArrayList;

import ru.touchcube.domain.presenter.WorldPresenter;
import ru.touchcube.domain.utils.function;

/**
 * Created by grish on 21.08.2018.
 */

public class TouchCubeWorld {

    private WorldPresenter presenter;
    private SystemInterface system;

    private ArrayList<Cube> cubes;

    public TouchCubeWorld(WorldPresenter presenter, SystemInterface system){
        cubes = new ArrayList<Cube>();
        this.presenter=presenter;
        this.system=system;
    }

    public void start(){
        presenter.init();
        final TouchCubeWorld world = this;
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
        });
    }

    public ArrayList<Cube> getCubes() {
        return cubes;
    }

}
