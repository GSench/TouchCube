package ru.touchcube;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.io.IOException;

import top.defaults.colorpicker.ColorPickerView;

/**
 * Created by grish on 02.09.2018.
 */

public class MainViewHolder {

    public ViewGroup parent;
    public RelativeLayout mainLayout;
    public RadioButton putRB;
    public RadioButton paintRB;
    public RadioButton deleteRB;
    public ImageView putIcon;
    public ImageView paintIcon;
    public ImageView deleteIcon;
    public Button filesButton;
    public Button saveButton;
    public Button settings_button;
    public Button centreButton;
    public Button clearButton;
    public LinearLayout palette;
    public Drawable cubeTexture;
    public LinearLayout colorPickerContainer;
    public ColorPickerView colorPickerView;
    public View colorPickerCloser;
    public View currentColor;

    public MainViewHolder(ViewGroup parent){
        this.parent=parent;
        mainLayout = parent.findViewById(R.id.mainLayout);
        putRB = parent.findViewById(R.id.putRB);
        paintRB = parent.findViewById(R.id.paintRB);
        deleteRB = parent.findViewById(R.id.deleteRB);
        putIcon = parent.findViewById(R.id.putIcon);
        paintIcon = parent.findViewById(R.id.paintIcon);
        deleteIcon = parent.findViewById(R.id.deleteIcon);
        filesButton = parent.findViewById(R.id.filesButton);
        saveButton = parent.findViewById(R.id.saveButton);
        settings_button = parent.findViewById(R.id.settings_button);
        centreButton = parent.findViewById(R.id.centreButton);
        clearButton = parent.findViewById(R.id.clearButton);
        palette = parent.findViewById(R.id.palette);
        colorPickerContainer = parent.findViewById(R.id.colorPickerContainer);
        initColorPickerView(colorPickerContainer);
        colorPickerCloser = parent.findViewById(R.id.colorPickerCloser);
        currentColor = parent.findViewById(R.id.selectedColor);
        try {
            cubeTexture = Drawable.createFromStream(parent.getContext().getAssets().open("cube_nc.png"), null);
        } catch (IOException e) {}//must be safe
    }

    private void initColorPickerView(ViewGroup parent){
        colorPickerView = new ColorPickerView(parent.getContext());
        colorPickerView.setEnabledAlpha(true);
        parent.addView(colorPickerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

}
