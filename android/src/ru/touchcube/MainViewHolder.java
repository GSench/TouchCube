package ru.touchcube;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

/**
 * Created by grish on 02.09.2018.
 */

public class MainViewHolder {

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

    public MainViewHolder(ViewGroup parent){
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
    }

}
