package ru.touchcube.domain;

import ru.touchcube.domain.interactor.CubeModelManagerFace;
import ru.touchcube.domain.interactor.PaletteFace;
import ru.touchcube.domain.interactor.WorldFace;

/**
 * Created by grish on 01.09.2018.
 */

public interface MainPresenter extends WorldFace, PaletteFace, CubeModelManagerFace {
    void init();
}
