package ru.touchcube.view_impl;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import ru.touchcube.AndroidInterface;
import ru.touchcube.R;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.interactors.Palette;
import ru.touchcube.presentation.presenters_impl.PalettePresenterImpl;
import ru.touchcube.presentation.view.PaletteView;
import ru.touchcube.viewholders.PaletteViewHolder;
import top.defaults.colorpicker.ColorObserver;

/**
 * Created by grish on 23.09.2018.
 * This class implements PaletteView.
 * It is initialized in AndroidLauncher.
 */

public class PaletteViewImpl implements PaletteView {

    private Activity act;
    private PalettePresenterImpl presenter;
    private PaletteViewHolder viewHolder;

    public PaletteViewImpl(Activity act){
        this.act=act;
        viewHolder = new PaletteViewHolder((ViewGroup) act.findViewById(R.id.app_layout), act);
        presenter = new PalettePresenterImpl(new AndroidInterface(act));
        presenter.setView(this);
        paletteInit();
    }

    public void start(){
        presenter.start();
    }

    public void finish(){
        presenter.finish();
    }

    private void paletteInit(){
        for(int i=0; i< Palette.COUNT; i++){
            final int pos = i;
            viewHolder.palette[i].colorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onColorClick(pos);
                }
            });
            viewHolder.palette[i].colorView.setBackgroundColor(0x00000000);
            viewHolder.paletteLayout.addView(viewHolder.palette[i].paletteColor);
        }
        viewHolder.colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser) {
                Color color1 = new Color(
                        android.graphics.Color.red(color),
                        android.graphics.Color.green(color),
                        android.graphics.Color.blue(color),
                        android.graphics.Color.alpha(color),
                        false);
                presenter.onColorPicked(color1);
            }
        });
        viewHolder.colorPickerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });
        viewHolder.colorPickerCloser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeColorPicker();
            }
        });
    }

    @Override
    public void updateColor(Color color, int pos) {
        if(color.noColor()) viewHolder.palette[pos].colorView.setBackground(viewHolder.cubeTexture);
        else viewHolder.palette[pos].colorView.setBackgroundColor(android.graphics.Color.argb(color.a(), color.r(), color.g(), color.b()));
    }

    @Override
    public void openColorPicker(Color initial) {
        viewHolder.colorPickerView.setInitialColor(android.graphics.Color.argb(initial.a(), initial.r(), initial.g(), initial.b()));
        viewHolder.colorPickerContainer.setVisibility(View.VISIBLE);
        viewHolder.colorPickerCloser.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isColorPickerOpened(){
        return viewHolder.colorPickerContainer.getVisibility()==View.VISIBLE;
    }

    @Override
    public void closeColorPicker() {
        presenter.onColorPickerClosed();
        viewHolder.colorPickerContainer.setVisibility(View.GONE);
        viewHolder.colorPickerCloser.setVisibility(View.GONE);
    }

    @Override
    public void setCurrentColor(int currentColor) {
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
        float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
        Point size = new Point();
        act.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        x = width-x;
        y = height/2-(y*Palette.COUNT)/2+currentColor*y;
        viewHolder.currentColor.setVisibility(View.VISIBLE);
        viewHolder.currentColor.setX(x);
        viewHolder.currentColor.setY(y);
    }

    public Color getCurrentColor() {
        return presenter.getCurrentColor();
    }
}
