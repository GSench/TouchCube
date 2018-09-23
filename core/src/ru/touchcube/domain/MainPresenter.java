package ru.touchcube.domain;

import ru.touchcube.domain.interactor.CubeModelManagerPresenter;
import ru.touchcube.domain.interactor.PalettePresenter;
import ru.touchcube.domain.interactor.WorldPresenter;

/**
 * Created by grish on 01.09.2018.
 */

public interface MainPresenter extends WorldPresenter, PalettePresenter, CubeModelManagerPresenter {
    void init();
}
