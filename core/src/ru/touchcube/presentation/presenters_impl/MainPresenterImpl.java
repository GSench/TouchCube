package ru.touchcube.presentation.presenters_impl;

import java.util.ArrayList;

import ru.touchcube.domain.MainPresenter;
import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.interactor.MainInteractor;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;
import ru.touchcube.presentation.view.MainView;

/**
 * Created by grish on 01.09.2018.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainInteractor interactor;
    private MainView view;

    public boolean isRendering() {
        return render;
    }

    private boolean render = true;

    public MainPresenterImpl(SystemInterface system, CubeModelStorage storage){
        interactor = new MainInteractor(this, system, storage);
    }

    public void setView(MainView view){
        this.view=view;
    }

    public void start(){
        render = true;
        interactor.start();
    }

    public ArrayList<CubeDrawing> getCubes() {
        return interactor.getCubes();
    }

    public void tapOnCube(CubeDrawing cubeDrawing, int side){
        interactor.tapOnCube(cubeDrawing, side);
    }

    public void longTapOnCube(CubeDrawing cubeDrawing, int side){
        interactor.longTapOnCube(cubeDrawing, side);
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

    public void onColorClick(int pos){
        interactor.onColorClick(pos);
    }

    public void onColorPicked(Color color, int pos){
        interactor.onColorPicked(color, pos);
    }

    public void onFilesButtonClicked(){
        interactor.onFilesButtonClicked();
    }

    public void onSaveCurrentModel(String title){
        interactor.onSaveCurrentModel(title);
    }

    public void onLoadModel(CubeModelFile file){
        interactor.onLoadModel(file);
    }

    public void onDeleteModel(CubeModelFile file){
        interactor.onDeleteModel(file);
    }

    public void onShareModel(CubeModelFile file){
        interactor.onShareModel(file);
    }

    public void finish(){
        interactor.finish();
    }

    @Override
    public void openModelList(CubeModelFile[] listModels) {
        view.openModelList(listModels);
    }

    @Override
    public void onSavingError(String title) {
        view.showSavingError(title);
    }

    @Override
    public void onSaved(String title) {
        view.closeSavingMessage();
    }

    @Override
    public void onLoadError(String modelName) {
        view.showLoadError(modelName);
    }

    @Override
    public void onDeleteError(String modelName) {
        view.showDeleteError(modelName);
    }

    @Override
    public void updateModelList(CubeModelFile[] listModels) {
        view.updateModelList(listModels);
    }

    @Override
    public void onNameError(String title) {
        view.onNameError();
    }

    @Override
    public void updateColor(Color color, int pos) {
        view.updateColor(color, pos);
    }

    @Override
    public void openColorPicker() {
        view.openColorPicker();
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
    public void init() {
        view.init();
    }

    public void onCentreButtonPushed() {
        interactor.onCentreButtonPushed();
    }
}
