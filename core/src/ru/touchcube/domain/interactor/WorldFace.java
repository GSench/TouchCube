package ru.touchcube.domain.interactor;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;

/**
 * Created by grish on 21.08.2018.
 */

public interface WorldFace {
    CubeDrawing onCubeAdded(Cube newCube);
    void stopRendering();
    void continueRendering();
}
