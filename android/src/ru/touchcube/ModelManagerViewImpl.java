package ru.touchcube;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.utils.function;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.model.AndroidModelStorage;
import ru.touchcube.presentation.presenters_impl.ModelManagerPresenterImpl;
import ru.touchcube.presentation.view.ModelManagerView;
import ru.touchcube.viewholders.CubeModelViewHolder;
import ru.touchcube.viewholders.ModelManagerViewHolder;

/**
 * Created by grish on 23.09.2018.
 */

public class ModelManagerViewImpl implements ModelManagerView {

    private Activity act;
    private ModelManagerViewHolder viewHolder;
    private ModelManagerPresenterImpl presenter;
    private function_get<ArrayList<Cube>> getCurrentModel;
    private function<ArrayList<Cube>> loadModel;

    private CubeModelFile selected = null;
    private CubeModelViewHolder[] cubeModelViewHolders;

    public ModelManagerViewImpl(Activity act, function_get<ArrayList<Cube>> getCurrentModel, function<ArrayList<Cube>> loadModel){
        this.act=act;
        this.getCurrentModel=getCurrentModel;
        this.loadModel=loadModel;
        viewHolder = new ModelManagerViewHolder((ViewGroup) act.findViewById(R.id.app_layout));
        presenter = new ModelManagerPresenterImpl(
                new AndroidInterface(act),
                new AndroidModelStorage(act));
        presenter.setView(this);
        buttonsSetting();
    }

    public void start(){
        presenter.start();
    }

    public void finish(){
        presenter.finish();
    }

    private void buttonsSetting(){
        viewHolder.filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onFilesButtonClicked();
            }
        });
        viewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTitleInputDialog();
            }
        });
    }

    private void openTitleInputDialog(){
        final EditText ed = new EditText(act);
        ed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        new AlertDialog.Builder(act)
                .setTitle(act.getResources().getString(R.string.get_name_title))
                .setView(ed)
                .setPositiveButton(act.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        String name =ed.getText().toString();
                        presenter.onSaveCurrentModel(name);
                    }
                })
                .setNegativeButton(act.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    @Override
    public void showSavingError(String title) {
        //TODO
        System.out.println("showSavingError for "+title);
    }

    @Override
    public void closeSavingMessage() {
        //TODO
    }

    @Override
    public void showLoadError(String modelName) {
        //TODO
        System.out.println("showLoadError for "+modelName);
    }

    @Override
    public void showDeleteError(String modelName) {
        //TODO
        System.out.println("showDeleteError for "+modelName);
    }

    @Override
    public void updateModelList(CubeModelFile[] listModels) {
        //TODO
    }

    @Override
    public void openModelList(CubeModelFile[] listModels) {

        selected = null;
        cubeModelViewHolders = new CubeModelViewHolder[listModels.length];

        LinearLayout modelList = new LinearLayout(act);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        modelList.setLayoutParams(layoutParams);

        int i=0;
        for(final CubeModelFile modelFile: listModels){
            final CubeModelViewHolder modelViewHolder = new CubeModelViewHolder(act, modelList);

            modelViewHolder.modelName.setText(modelFile.getModelName());
            modelViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onDeleteModel(modelFile);
                }
            });
            modelViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onShareModel(modelFile);
                }
            });
            modelViewHolder.cubeModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = modelFile;
                    for(CubeModelViewHolder viewHolder: cubeModelViewHolders)
                        viewHolder.cubeModel.setBackgroundColor(Color.TRANSPARENT);
                    modelViewHolder.cubeModel.setBackgroundColor(act.getResources().getColor(R.color.selected));
                }
            });
            cubeModelViewHolders[i++]=modelViewHolder;
            modelList.addView(modelViewHolder.cubeModel);
        }

        new AlertDialog.Builder(act)
                .setTitle(act.getResources().getString(R.string.save_window_title))
                .setView(modelList)
                .setPositiveButton(act.getResources().getString(R.string.save_window_load), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selected!=null) presenter.onLoadModel(selected);
                        else Toast.makeText(act, act.getResources().getString(R.string.no_files_selected), Toast.LENGTH_SHORT).show();
                        selected=null;
                        dialog.cancel();
                    }
                })
                .setNegativeButton(act.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    @Override
    public void onNameError() {
        Toast.makeText(act, act.getResources().getString(R.string.bad_name), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingMessage(String title) {
        //TODO
    }

    @Override
    public ArrayList<Cube> getCurrentModel() {
        return getCurrentModel.get();
    }

    @Override
    public void loadModel(ArrayList<Cube> cubes) {
        loadModel.run(cubes);
    }
}
