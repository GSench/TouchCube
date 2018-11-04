package ru.touchcube.presentation.utils;

import ru.touchcube.domain.model.Color;

/**
 * Created by grish on 04.11.2018.
 */

public class LibGDXUtils {

    public static float toFloat(Color color){
        return com.badlogic.gdx.graphics.Color.toFloatBits(
                color.r(),
                color.g(),
                color.b(),
                color.a());
    }
}
