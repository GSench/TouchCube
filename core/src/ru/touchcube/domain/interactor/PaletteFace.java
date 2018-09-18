package ru.touchcube.domain.interactor;

import ru.touchcube.domain.model.Color;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public interface PaletteFace {
    void updateColor(Color color, int pos);
    void openColorPicker(int pos);

}
