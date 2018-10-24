package ru.touchcube.presentation.presenters_impl;

import java.util.ArrayList;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.interactor.CubeModelManagerPresenter;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;
import ru.touchcube.domain.services.CubeModelManager;
import ru.touchcube.presentation.view.ModelManagerView;

/**
 * Created by grish on 23.09.2018.
 */

public class ModelManagerPresenterImpl implements CubeModelManagerPresenter {

    private CubeModelManager interactor;
    private ModelManagerView view;

    public ModelManagerPresenterImpl(SystemInterface system, CubeModelStorage storage){
        interactor = new CubeModelManager(this, storage, system);
    }

    public void setView(ModelManagerView view){
        this.view=view;
    }

    public void start(CubeModelFile cubeModelFile){
        interactor.loadFromCash(cubeModelFile);
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
        interactor.exit();
    }

    @Override
    public void openModelList(CubeModelFile[] listModels) {
        view.openModelList(listModels);
    }

    @Override
    public void onSavingError(String title) {
        view.closeSavingMessage();
        view.showSavingError(title);
    }

    @Override
    public void onStartSaving(String title) {
        view.showSavingMessage(title);
    }

    @Override
    public ArrayList<Cube> getCurrentModel() {
        return view.getCurrentModel();
    }

    @Override
    public void loadModel(ArrayList<Cube> cubes) {
        view.loadModel(cubes);
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
}
