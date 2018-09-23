package ru.touchcube;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.utils.function;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.model.AndroidModelStorage;
import ru.touchcube.presentation.presenters_impl.ModelManagerPresenterImpl;
import ru.touchcube.presentation.view.ModelManagerView;
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

    public ModelManagerViewImpl(Activity act, function_get<ArrayList<Cube>> getCurrentModel, function<ArrayList<Cube>> loadModel){
        this.act=act;
        this.getCurrentModel=getCurrentModel;
        this.loadModel=loadModel;
        viewHolder = new ModelManagerViewHolder((ViewGroup) act.findViewById(R.id.app_layout));
        presenter = new ModelManagerPresenterImpl(new AndroidInterface(act), new AndroidModelStorage());
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
        //TODO
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
