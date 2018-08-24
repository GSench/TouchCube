package ru.touchcube.domain.presenter;

import ru.touchcube.domain.model.Color;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public interface PalettePresenter {
    void updateColor(Color color, int pos);
    void openColorPicker();

}
