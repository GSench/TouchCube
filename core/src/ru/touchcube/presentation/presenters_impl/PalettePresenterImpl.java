package ru.touchcube.presentation.presenters_impl;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.presenters.PalettePresenter;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.interactors.Palette;
import ru.touchcube.presentation.view.PaletteView;

/**
 * Created by grish on 23.09.2018.
 * Implementation of PalettePresenter to connect View and Interactor
 */

public class PalettePresenterImpl implements PalettePresenter {

    private Palette interactor;
    private PaletteView view;

    public PalettePresenterImpl(SystemInterface system){
        interactor = new Palette(system, this);
    }

    public void setView(PaletteView view){
        this.view=view;
    }

    public void start(){
        interactor.init();
    }

    public Color getCurrentColor(){
        return interactor.getCurrentColor();
    }

    public void onColorPickerClosed() {
        interactor.onColorPickerClosed();
    }

    public void onColorClick(int pos){
        interactor.onColorClick(pos);
    }

    public void onColorPicked(Color color){
        interactor.onColorPicked(color);
    }

    public void finish(){
        interactor.exit();
    }

    @Override
    public void updateColor(Color color, int pos) {
        view.updateColor(color, pos);
    }

    @Override
    public void openColorPicker(Color initial) {
        view.openColorPicker(initial);
    }

    @Override
    public boolean isColorPickerOpened() {
        return view.isColorPickerOpened();
    }

    @Override
    public void closeColorPicker() {
        view.closeColorPicker();
    }

    @Override
    public void setCurrentColor(int currentColor) {
        view.setCurrentColor(currentColor);
    }

}
