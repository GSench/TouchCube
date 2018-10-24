package ru.touchcube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.utils.function;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.presentation.MyTouchCube;
import ru.touchcube.viewholders.MainViewHolder;

public class AndroidLauncher extends AndroidApplication {

    private static final String FIRST_START = "FIRST_START";

    private MainViewHolder viewHolder;
    private MyTouchCube libgdx;
    private ModelManagerViewImpl modelManagerView;
    private PaletteViewImpl paletteView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewHolder = new MainViewHolder((ViewGroup) findViewById(R.id.app_layout));

        paletteView = new PaletteViewImpl(this);
        libgdx = new MyTouchCube(new function_get<Color>() {
            @Override
            public Color get() {
                return paletteView.getCurrentColor();
            }
        });
        modelManagerView = new ModelManagerViewImpl(this, new function_get<ArrayList<Cube>>() {
            @Override
            public ArrayList<Cube> get() {
                return libgdx.getCurrentModel();
            }
        }, new function<ArrayList<Cube>>() {
            @Override
            public void run(ArrayList<Cube>[] params) {
                libgdx.loadModel(params[0]);
            }
        });

        buttonsSetting();
        LibGDXViewSetting();
        paletteView.start();
        modelManagerView.start();
        tutorial();
    }

    @Override
    protected void onDestroy() {
        modelManagerView.finish();
        paletteView.finish();
        super.onDestroy();
    }

    private void tutorial(){
        if(!getSharedPreferences(FIRST_START, MODE_PRIVATE).getBoolean(FIRST_START, true)) return;
        Tutorial tutorial = new Tutorial(viewHolder.parent, this, new function<Void>() {
            @Override
            public void run(Void... params) {
                getSharedPreferences(FIRST_START, MODE_PRIVATE).edit().putBoolean(FIRST_START, false).apply();
            }
        });
        tutorial.start();
    }

    private void buttonsSetting(){
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
                libgdx.isPutMode();
            }
        };
        View.OnClickListener onPaint = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.putRB.setChecked(false);
                viewHolder.paintRB.setChecked(true);
                viewHolder.deleteRB.setChecked(false);
                libgdx.isPaintMode();
            }
        };
        View.OnClickListener onDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.putRB.setChecked(false);
                viewHolder.paintRB.setChecked(false);
                viewHolder.deleteRB.setChecked(true);
                libgdx.isDeleteMode();
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
                libgdx.onCentreButtonPushed();
            }
        });
        viewHolder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearDialog();
            }
        });
    }

    private void onClearDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.clear_dialog))
                .setMessage(getResources().getString(R.string.clear_dialog_message))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        libgdx.onClearButtonPushed();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    private void openSettings() {
        startActivity(new Intent(this, MyPreferenceActivity.class));
    }

    private void LibGDXViewSetting(){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View libGDXView = initializeForView(libgdx, config);
        viewHolder.mainLayout.addView(libGDXView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        modelManagerView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
