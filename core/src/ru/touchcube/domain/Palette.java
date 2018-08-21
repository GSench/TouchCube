package ru.touchcube.domain;

import ru.touchcube.domain.utils.Color;

/**
 * Created by grish on 21.08.2018.
 */

public class Palette {

    private Color currentColor;

    public Palette(){
        currentColor = new Color(0,0,0,0, true);
    }

    public Color getCurrentColor() {
        return currentColor;
    }

}
