package ru.touchcube.domain.presenter;

import java.util.ArrayList;

import ru.touchcube.domain.Cube;

/**
 * Created by grish on 21.08.2018.
 */

public interface WorldPresenter {
    ArrayList<Cube> loadFromCash();
    void init();
}
