package ru.touchcube.viewholders;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;

import ru.touchcube.R;
import ru.touchcube.domain.interactors.Palette;
import ru.touchcube.utils.AliasingDrawableWrapper;
import top.defaults.colorpicker.ColorPickerView;

/**
 * Created by grish on 23.09.2018.
 * ViewHolder for PaletteView
 */

public class PaletteViewHolder {

    public LinearLayout paletteLayout;
    public Drawable cubeTexture;
    public LinearLayout colorPickerContainer;
    public ColorPickerView colorPickerView;
    public View colorPickerCloser;
    public View currentColor;
    public PaletteColorViewHolder[] palette;

    public PaletteViewHolder(ViewGroup parent, Activity act){
        paletteLayout = parent.findViewById(R.id.palette);
        colorPickerContainer = parent.findViewById(R.id.colorPickerContainer);
        initColorPickerView(colorPickerContainer);
        colorPickerCloser = parent.findViewById(R.id.colorPickerCloser);
        currentColor = parent.findViewById(R.id.selectedColor);
        try {
            Drawable cubeTexture = Drawable.createFromStream(act.getAssets().open("cube_nc.png"), null);
            this.cubeTexture = new AliasingDrawableWrapper(cubeTexture);
        } catch (IOException e) {}//must be safe
        palette = new PaletteColorViewHolder[Palette.COUNT];
        for(int i=0; i< Palette.COUNT; i++) {
            palette[i] = new PaletteColorViewHolder(act, paletteLayout);
        }
    }

    private void initColorPickerView(ViewGroup parent){
        colorPickerView = new ColorPickerView(parent.getContext());
        colorPickerView.setEnabledAlpha(true);
        parent.addView(colorPickerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
