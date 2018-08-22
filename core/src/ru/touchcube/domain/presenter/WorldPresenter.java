package ru.touchcube.domain.presenter;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;

/**
 * Created by grish on 21.08.2018.
 */

public interface WorldPresenter {
    ArrayList<Cube> loadFromCash();
    void init();
    CubeDrawing onCubeAdded(Cube newCube);
    void stopRendering();
    void showLoading();
}
