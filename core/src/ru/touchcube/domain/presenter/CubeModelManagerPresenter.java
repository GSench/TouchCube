package ru.touchcube.domain.presenter;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public interface CubeModelManagerPresenter {
    void loadModel(ArrayList<Cube> cubes);
    ArrayList<Cube> getCurrentModel();
    void onSavingError(String title);
    void onSaved(String title);
}
