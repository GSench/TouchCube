package ru.touchcube.domain.presenters;

import ru.touchcube.domain.model.Color;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 * PalettePresenter for Palette interaction with its view
 */

public interface PalettePresenter {
    void updateColor(Color color, int pos);
    void openColorPicker(Color initial);
    boolean isColorPickerOpened();
    void closeColorPicker();
    void setCurrentColor(int currentColor);
}
