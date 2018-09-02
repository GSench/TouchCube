package ru.touchcube;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
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

            }
        });
        viewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });
        viewHolder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClearButtonPushed();
            }
        });
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

    }

    @Override
    public void closeSavingMessage() {

    }

    @Override
    public void showLoadError(String modelName) {

    }

    @Override
    public void showDeleteError(String modelName) {

    }

    @Override
    public void updateModelList() {

    }

    @Override
    public void updateColor(Color color, int pos) {

    }

    @Override
    public void openColorPicker() {

    }

    @Override
    public CubeDrawing add(Cube newCube) {
        return null;
    }
}
