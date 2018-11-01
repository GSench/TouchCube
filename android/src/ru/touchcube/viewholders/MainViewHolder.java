package ru.touchcube.viewholders;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import ru.touchcube.R;

/**
 * Created by grish on 02.09.2018.
 * ViewHolder for AndroidLauncher.
 * It keeps Android buttons, which are used in MyTouchCube
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
    public Button settings_button;
    public Button centreButton;
    public Button clearButton;
    public Button orthoPerspSwitch;
    public Drawable orthographyBtn;
    public Drawable perspectiveBtn;


    public MainViewHolder(ViewGroup parent){
        this.parent=parent;
        mainLayout = parent.findViewById(R.id.mainLayout);
        putRB = parent.findViewById(R.id.putRB);
        paintRB = parent.findViewById(R.id.paintRB);
        deleteRB = parent.findViewById(R.id.deleteRB);
        putIcon = parent.findViewById(R.id.putIcon);
        paintIcon = parent.findViewById(R.id.paintIcon);
        deleteIcon = parent.findViewById(R.id.deleteIcon);
        settings_button = parent.findViewById(R.id.settings_button);
        centreButton = parent.findViewById(R.id.centreButton);
        clearButton = parent.findViewById(R.id.clearButton);
        orthoPerspSwitch = parent.findViewById(R.id.ortho_persp_switch);
        orthographyBtn = parent.getContext().getResources().getDrawable(R.drawable.orthography_btn);
        perspectiveBtn = parent.getContext().getResources().getDrawable(R.drawable.perspective_btn);
    }

}
