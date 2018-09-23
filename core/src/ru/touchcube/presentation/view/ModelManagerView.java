package ru.touchcube.presentation.view;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;

/**
 * Created by grish on 23.09.2018.
 */

public interface ModelManagerView {

    void showSavingError(String title);
    void closeSavingMessage();
    void showLoadError(String modelName);
    void showDeleteError(String modelName);
    void updateModelList(CubeModelFile[] listModels);
    void openModelList(CubeModelFile[] listModels);
    void onNameError();
    void showSavingMessage(String title);
    ArrayList<Cube> getCurrentModel();
    void loadModel(ArrayList<Cube> cubes);
}
