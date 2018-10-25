package ru.touchcube;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.utils.function;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.model.AndroidModelStorage;
import ru.touchcube.model.AndroidModelUri;
import ru.touchcube.presentation.presenters_impl.ModelManagerPresenterImpl;
import ru.touchcube.presentation.view.ModelManagerView;
import ru.touchcube.utils.PermissionManager;
import ru.touchcube.viewholders.CubeModelViewHolder;
import ru.touchcube.viewholders.ModelManagerViewHolder;

/**
 * Created by grish on 23.09.2018.
 */

public class ModelManagerViewImpl implements ModelManagerView {

    private Activity act;
    private ModelManagerViewHolder viewHolder;
    private ModelManagerPresenterImpl presenter;
    private PermissionManager permissionManager;
    private function_get<ArrayList<Cube>> getCurrentModel;
    private function<ArrayList<Cube>> loadModel;

    private LinearLayout modelList;
    private AlertDialog loadWindow;
    private ProgressDialog progressDialog;
    private CubeModelFile selected = null;

    ModelManagerViewImpl(Activity act, function_get<ArrayList<Cube>> getCurrentModel, function<ArrayList<Cube>> loadModel){
        this.act=act;
        permissionManager=new PermissionManager(act);
        this.getCurrentModel=getCurrentModel;
        this.loadModel=loadModel;
        viewHolder = new ModelManagerViewHolder((ViewGroup) act.findViewById(R.id.app_layout));
        presenter = new ModelManagerPresenterImpl(
                new AndroidInterface(act),
                new AndroidModelStorage(act));
        presenter.setView(this);
        buttonsSetting();
        listSetting();
    }

    public void start(){
        Intent intent = act.getIntent();
        String action = intent.getAction();
        if (action != null && !action.equals(Intent.ACTION_VIEW)){
            presenter.start(null);
            return;
        }
        Uri toOpen = intent.getData();
        if(toOpen==null){
            presenter.start(null);
            return;
        }
        AndroidModelUri androidModelUri = new AndroidModelUri(toOpen, act);
        presenter.start(androidModelUri);
    }

    public void finish(){
        presenter.finish();
    }

    private void buttonsSetting(){
        viewHolder.filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionManager.requestWriteESPermission(
                        new function<Void>() {
                            @Override
                            public void run(Void... params) {
                                presenter.onFilesButtonClicked();
                            }
                        },
                        new function<Void>() {
                            @Override
                            public void run(Void... params) {
                                Toast.makeText(act, act.getString(R.string.write_permission_denied), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
        viewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionManager.requestWriteESPermission(
                        new function<Void>() {
                            @Override
                            public void run(Void... params) {
                                openTitleInputDialog();
                            }
                        },
                        new function<Void>() {
                            @Override
                            public void run(Void... params) {
                                Toast.makeText(act, act.getString(R.string.write_permission_denied), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
    }

    private void listSetting(){
        modelList = new LinearLayout(act);
        modelList.setOrientation(LinearLayout.VERTICAL);
        ScrollView loadWindowView = new ScrollView(act);
        loadWindowView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadWindowView.addView(modelList, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadWindow = new AlertDialog.Builder(act)
                .setTitle(act.getResources().getString(R.string.save_window_title))
                .setView(loadWindowView)
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
                        selected=null;
                        dialog.cancel();
                    }
                })
                .create();
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
        Toast.makeText(act, act.getResources().getString(R.string.saving_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void closeSavingMessage() {
        if(progressDialog!=null) progressDialog.dismiss();
    }

    @Override
    public void showLoadError(String modelName) {
        Toast.makeText(act, act.getResources().getString(R.string.loading_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDeleteError(String modelName) {
        Toast.makeText(act, act.getResources().getString(R.string.error_deleting), Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateModelList(CubeModelFile[] listModels) {
        modelList.removeAllViews();
        for(final CubeModelFile modelFile : listModels){
            CubeModelViewHolder modelViewHolder = new CubeModelViewHolder(act, modelList);

            modelViewHolder.modelName.setText(modelFile.getModelName());
            modelViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteDialog(modelFile);
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
                    for(int j=0; j<modelList.getChildCount(); j++)
                        modelList.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    view.setBackgroundColor(act.getResources().getColor(R.color.selected));
                }
            });
            modelList.addView(modelViewHolder.cubeModel);
        }
    }

    private void onDeleteDialog(final CubeModelFile cubeModelFile){
        new AlertDialog.Builder(act)
                .setTitle(act.getResources().getString(R.string.delete_file_dialog_title))
                .setMessage(act.getResources().getString(R.string.delete_file_dialog_message))
                .setPositiveButton(act.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        presenter.onDeleteModel(cubeModelFile);
                    }
                })
                .setNegativeButton(act.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    @Override
    public void openModelList(CubeModelFile[] listModels) {
        selected = null;
        updateModelList(listModels);
        loadWindow.show();
    }

    @Override
    public void onNameError() {
        Toast.makeText(act, act.getResources().getString(R.string.bad_name), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingMessage(String title) {
        if(progressDialog==null){
            progressDialog = new ProgressDialog(act);
            progressDialog.setMessage(act.getResources().getString(R.string.saving));
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public ArrayList<Cube> getCurrentModel() {
        return getCurrentModel.get();
    }

    @Override
    public void loadModel(ArrayList<Cube> cubes) {
        loadModel.run(cubes);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionManager.onPermissionCallback(requestCode, permissions, grantResults);
    }
}
