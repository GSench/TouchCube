package ru.touchcube.domain.presenter;

import java.util.ArrayList;

import ru.touchcube.domain.Cube;
import ru.touchcube.domain.CubeDrawing;

/**
 * Created by grish on 21.08.2018.
 */

public interface WorldPresenter {
    ArrayList<Cube> loadFromCash();
    void init();
    CubeDrawing onCubeAdded(Cube newCube);
}
