package ru.touchcube.viewholders;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.touchcube.R;

/**
 * Created by grish on 22.10.2018.
 * ViewHolder for Tutorial
 */

public class TutorialViewHolder {

    public ViewGroup main;
    public RelativeLayout tutorial;
    public ImageView img;
    public TextView txt;
    public Button buttonBack;

    public TutorialViewHolder(ViewGroup main, Activity act){
        this.main=main;
        tutorial = (RelativeLayout) act.getLayoutInflater().inflate(R.layout.tutorial, main, false);
        img = (ImageView) tutorial.findViewById(R.id.tutorial_image);
        txt = (TextView) tutorial.findViewById(R.id.tutorial_text);
        buttonBack = tutorial.findViewById(R.id.button_back);
    }

}
