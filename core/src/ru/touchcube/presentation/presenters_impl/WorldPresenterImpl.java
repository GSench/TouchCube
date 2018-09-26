package ru.touchcube.presentation.presenters_impl;

import java.util.ArrayList;

import ru.touchcube.domain.interactor.WorldPresenter;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.services.TouchCubeWorld;
import ru.touchcube.presentation.view.WorldView;

/**
 * Created by grish on 23.09.2018.
 */

public class WorldPresenterImpl implements WorldPresenter {

    private TouchCubeWorld interactor;
    private WorldView view;

    private boolean render = false;

    public WorldPresenterImpl(){
        interactor = new TouchCubeWorld(this);
    }

    public void setView(WorldView view){
        this.view=view;
    }

    public void start() {
        render = true;
    }

    public boolean isRendering() {
        return render;
    }

    public ArrayList<CubeDrawing> getCubes() {
        return interactor.getCubes();
    }

    public void loadModel(ArrayList<Cube> model){
        interactor.load(model);
    }

    public void tapOnCube(CubeDrawing cubeDrawing, int side){
        interactor.tapOnCube(cubeDrawing, side);
    }

    public void longTapOnCube(CubeDrawing cubeDrawing, int side){
        interactor.reloadCurrent();
    }

    public void onClearButtonPushed(){
        interactor.onClearButtonPushed();
    }

    public void isPutMode(){
        interactor.isPutMode();
    }

    public void isPaintMode(){
        interactor.isPaintMode();
    }

    public void isDeleteMode(){
        interactor.isDeleteMode();
    }

    public void onCentreButtonPushed() {
        interactor.reloadCurrent();
    }

    public void tapOnBackground() {
        interactor.tapOnBackground();
    }

    @Override
    public CubeDrawing onCubeAdded(Cube newCube) {
        return view.add(newCube);
    }

    @Override
    public void stopRendering() {
        render = false;
    }

    @Override
    public void continueRendering() {
        render = true;
    }

    @Override
    public Color getCurrentColor() {
        return view.getCurrentColor();
    }

}
