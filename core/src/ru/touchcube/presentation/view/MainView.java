package ru.touchcube.presentation.view;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;

/**
 * Created by grish on 01.09.2018.
 */

public interface MainView {

    void init();
    void showSavingError(String title);
    void closeSavingMessage();
    void showLoadError(String modelName);
    void showDeleteError(String modelName);
    void updateModelList();
    void updateColor(Color color, int pos);
    void openColorPicker();
    CubeDrawing add(Cube newCube);

}
