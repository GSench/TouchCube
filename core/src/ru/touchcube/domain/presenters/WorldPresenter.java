package ru.touchcube.domain.presenters;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;

/**
 * Created by grish on 21.08.2018.
 * WorldPresenter for TouchCubeWorld interaction with its view
 */

public interface WorldPresenter {
    CubeDrawing onCubeAdded(Cube newCube);
    void stopRendering();
    void continueRendering();
    Color getCurrentColor();
}
