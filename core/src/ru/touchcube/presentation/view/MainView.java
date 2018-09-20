package ru.touchcube.presentation.view;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.CubeModelFile;

/**
 * Created by grish on 01.09.2018.
 */

public interface MainView {

    void init();
    void showSavingError(String title);
    void closeSavingMessage();
    void showLoadError(String modelName);
    void showDeleteError(String modelName);
    void updateModelList(CubeModelFile[] listModels);
    void updateColor(Color color, int pos);
    void openColorPicker(Color initial);
    boolean isColorPickerOpened();
    CubeDrawing add(Cube newCube);
    void openModelList(CubeModelFile[] listModels);
    void onNameError();
    void showSavingMessage(String title);
    void closeColorPicker();
    void setCurrentColor(int currentColor);
}
