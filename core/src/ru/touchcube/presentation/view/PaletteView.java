package ru.touchcube.presentation.view;

import ru.touchcube.domain.model.Color;

/**
 * Created by grish on 23.09.2018.
 * View interface for Palette
 */

public interface PaletteView {

    void updateColor(Color color, int pos);
    void openColorPicker(Color initial);
    boolean isColorPickerOpened();
    void closeColorPicker();
    void setCurrentColor(int currentColor);

}
