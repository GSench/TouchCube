package ru.touchcube.utils;


import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.drawable.DrawableWrapper;

/**
 * Created by grish on 29.10.2018.
 * This class is usen to draw no-color color in palette.
 * It is only used in PaletteViewHolder
 */

public class AliasingDrawableWrapper extends DrawableWrapper {
    private static final DrawFilter DRAW_FILTER =
            new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG, 0);

    @SuppressLint("RestrictedApi")
    public AliasingDrawableWrapper(Drawable wrapped) {
        super(wrapped);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void draw(Canvas canvas) {
        DrawFilter oldDrawFilter = canvas.getDrawFilter();
        canvas.setDrawFilter(DRAW_FILTER);
        super.draw(canvas);
        canvas.setDrawFilter(oldDrawFilter);
    }
}