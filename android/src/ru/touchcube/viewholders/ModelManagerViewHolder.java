package ru.touchcube.viewholders;

import android.view.ViewGroup;
import android.widget.Button;

import ru.touchcube.R;

/**
 * Created by grish on 23.09.2018.
 */

public class ModelManagerViewHolder {

    public Button filesButton;
    public Button saveButton;

    public ModelManagerViewHolder(ViewGroup parent){
        filesButton = parent.findViewById(R.id.filesButton);
        saveButton = parent.findViewById(R.id.saveButton);
    }

}
