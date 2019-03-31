package ru.touchcube.viewholders;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.touchcube.R;

// ViewHolder for CubeModel File as a part of CubeModelManager

public class CubeModelViewHolder {

    public RelativeLayout cubeModel;
    public TextView modelName;
    public Button shareButton;
    public Button deleteButton;
    public Button exportObjButton;

    public CubeModelViewHolder(Activity act, ViewGroup modelList){
        cubeModel = (RelativeLayout) act.getLayoutInflater().inflate(R.layout.cube_model_layout, modelList, false);
        modelName = cubeModel.findViewById(R.id.model_name);
        shareButton = cubeModel.findViewById(R.id.share_model_btn);
        deleteButton = cubeModel.findViewById(R.id.delete_model_btn);
        exportObjButton = cubeModel.findViewById(R.id.export_obj_btn);
    }
}
