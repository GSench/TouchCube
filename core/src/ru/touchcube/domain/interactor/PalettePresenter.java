package ru.touchcube.domain.interactor;

import ru.touchcube.domain.model.Color;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public interface PalettePresenter {
    void updateColor(Color color, int pos);
    void openColorPicker(Color initial);
    boolean isColorPickerOpened();
    void closeColorPicker();
    void setCurrentColor(int currentColor);
}
