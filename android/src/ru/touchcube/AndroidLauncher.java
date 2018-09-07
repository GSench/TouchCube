package ru.touchcube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.model.AndroidModelStorage;
import ru.touchcube.presentation.MyTouchCube;
import ru.touchcube.presentation.presenters_impl.MainPresenterImpl;
import ru.touchcube.presentation.view.MainView;

public class AndroidLauncher extends AndroidApplication implements MainView {

    private MainViewHolder viewHolder;
    private MyTouchCube libgdx;
    private MainPresenterImpl presenter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewHolder = new MainViewHolder((ViewGroup) findViewById(R.id.app_layout));
        presenter = new MainPresenterImpl(new AndroidInterface(this), new AndroidModelStorage());
        libgdx = new MyTouchCube(presenter);
        presenter.setView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    protected void onStop() {
        presenter.finish();
        super.onStop();
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
        viewHolder.settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        View.OnClickListener onPut = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.putRB.setChecked(true);
                viewHolder.paintRB.setChecked(false);
                viewHolder.deleteRB.setChecked(false);
                presenter.isPutMode();
            }
        };
        View.OnClickListener onPaint = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.putRB.setChecked(false);
                viewHolder.paintRB.setChecked(true);
                viewHolder.deleteRB.setChecked(false);
                presenter.isPaintMode();
            }
        };
        View.OnClickListener onDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.putRB.setChecked(false);
                viewHolder.paintRB.setChecked(false);
                viewHolder.deleteRB.setChecked(true);
                presenter.isDeleteMode();
            }
        };
        viewHolder.putIcon.setOnClickListener(onPut);
        viewHolder.putRB.setOnClickListener(onPut);
        viewHolder.paintIcon.setOnClickListener(onPaint);
        viewHolder.paintRB.setOnClickListener(onPaint);
        viewHolder.deleteIcon.setOnClickListener(onDelete);
        viewHolder.deleteRB.setOnClickListener(onDelete);

        viewHolder.centreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCentreButtonPushed();
            }
        });
        viewHolder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClearButtonPushed();
            }
        });
    }

    private void openSettings() {
        //TODO
        System.out.println("SETTINGS ACTIVITY");
    }

    private void LibGDXViewSetting(){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View libGDXView = initializeForView(libgdx, config);
        viewHolder.mainLayout.addView(libGDXView);
    }

    @Override
    public void init() {
        buttonsSetting();
        LibGDXViewSetting();
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
    public void updateColor(Color color, int pos) {
        //TODO
    }

    @Override
    public void openColorPicker() {
        //TODO
    }

    @Override
    public CubeDrawing add(Cube newCube) {
        //TODO
        return null;
    }

    @Override
    public void openModelList(CubeModelFile[] listModels) {
        //TODO
    }

    @Override
    public void onNameError() {
        Toast.makeText(this, getResources().getString(R.string.bad_name), Toast.LENGTH_LONG).show();
    }

    private void openTitleInputDialog(){
        final EditText ed = new EditText(AndroidLauncher.this);
        ed.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        new AlertDialog.Builder(AndroidLauncher.this)
                .setTitle(getResources().getString(R.string.get_name_title))
                .setView(ed)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        String name =ed.getText().toString();
                        presenter.onSaveCurrentModel(name);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }
}
