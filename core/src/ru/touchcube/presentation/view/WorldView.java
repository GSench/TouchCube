package ru.touchcube.presentation.view;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;

/**
 * Created by grish on 23.09.2018.
 */

public interface WorldView {

    CubeDrawing add(Cube newCube);
    Color getCurrentColor();
}
