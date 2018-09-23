package ru.touchcube.domain.interactor;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public interface CubeModelManagerPresenter {
    void openModelList(CubeModelFile[] listModels);
    void onSavingError(String title);
    void onSaved(String title);
    void onLoadError(String modelName);
    void onDeleteError(String modelName);
    void updateModelList(CubeModelFile[] listModels);
    void onNameError(String title);
    void onStartSaving(String title);
    ArrayList<Cube> getCurrentModel();
    void loadModel(ArrayList<Cube> cubes);
}
