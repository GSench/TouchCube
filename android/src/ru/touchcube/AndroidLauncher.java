package ru.touchcube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
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
import ru.touchcube.domain.services.Palette;
import ru.touchcube.model.AndroidModelStorage;
import ru.touchcube.presentation.MyTouchCube;
import ru.touchcube.presentation.presenters_impl.MainPresenterImpl;
import ru.touchcube.presentation.view.MainView;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerPopup;

public class AndroidLauncher extends AndroidApplication implements MainView {

    private MainViewHolder viewHolder;
    private PaletteColorViewHolder[] palette;
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
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.finish();
        super.onDestroy();
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
        paletteInit();
    }

    private void paletteInit(){
        palette = new PaletteColorViewHolder[Palette.COUNT];
        for(int i=0; i< Palette.COUNT; i++){
            final int pos = i;
            palette[i] = new PaletteColorViewHolder(this, viewHolder.palette);
            palette[i].colorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onColorClick(pos);
                }
            });
            palette[i].colorView.setBackgroundColor(0x00000000);
            viewHolder.palette.addView(palette[i].paletteColor);
        }
        viewHolder.colorPickerView.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser) {
                Color color1 = new Color(
                        android.graphics.Color.red(color),
                        android.graphics.Color.green(color),
                        android.graphics.Color.blue(color),
                        android.graphics.Color.alpha(color),
                        false);
                presenter.onColorPicked(color1);
            }
        });
        viewHolder.colorPickerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do nothing
            }
        });
        viewHolder.colorPickerCloser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeColorPicker();
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
        if(color.noColor()) palette[pos].colorView.setBackground(viewHolder.cubeTexture);
        else palette[pos].colorView.setBackgroundColor(android.graphics.Color.argb(color.a(), color.r(), color.g(), color.b()));
    }

    @Override
    public void openColorPicker(Color initial) {
        viewHolder.colorPickerView.setInitialColor(android.graphics.Color.argb(initial.a(), initial.r(), initial.g(), initial.b()));
        viewHolder.colorPickerContainer.setVisibility(View.VISIBLE);
        viewHolder.colorPickerCloser.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isColorPickerOpened(){
        return viewHolder.colorPickerContainer.getVisibility()==View.VISIBLE;
    }

    @Override
    public CubeDrawing add(Cube newCube) {
        return libgdx.add(newCube);
    }

    @Override
    public void openModelList(CubeModelFile[] listModels) {
        //TODO
    }

    @Override
    public void onNameError() {
        Toast.makeText(this, getResources().getString(R.string.bad_name), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSavingMessage(String title) {
        //TODO
    }

    @Override
    public void closeColorPicker() {
        presenter.onColorPickerClosed();
        viewHolder.colorPickerContainer.setVisibility(View.GONE);
        viewHolder.colorPickerCloser.setVisibility(View.GONE);
    }

    @Override
    public void setCurrentColor(int currentColor) {
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
        float y = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, Resources.getSystem().getDisplayMetrics());
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        x = width-x;
        y = height/2-(y*Palette.COUNT)/2+currentColor*y;
        viewHolder.currentColor.setVisibility(View.VISIBLE);
        viewHolder.currentColor.setX(x);
        viewHolder.currentColor.setY(y);
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
