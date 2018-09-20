package ru.touchcube.domain.services;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.interactor.PaletteFace;
import ru.touchcube.domain.utils.function;

/**
 * Created by Григорий Сенченок on 23.08.2018.
 */

public class Palette {

    public static final int COUNT = 5;
    private static final String PALETTE = "palette";
    private static final String PALETTE_SEPARATOR = ";";

    private PaletteFace face;
    private SystemInterface system;

    private final Color[] palette = new Color[COUNT];
    private int currentColor = 0;

    public Palette(SystemInterface system, PaletteFace face){
        this.system=system;
        this.face =face;
    }

    public void init(){
        if(!loadSavedPalette()) loadDefaultPalette();
        for(int i=0; i<COUNT; i++) face.updateColor(palette[i], i);
        face.setCurrentColor(currentColor);
    }

    public Color getCurrentColor(){
        return palette[currentColor];
    }

    public void onColorClick(int pos){
        if(pos<0||pos>=COUNT) return;
        face.setCurrentColor(pos);
        if(currentColor==pos) {
            if (face.isColorPickerOpened()) face.closeColorPicker();
            else if (currentColor > 0) face.openColorPicker(palette[currentColor]);
        }
        else {
            currentColor = pos;
            if(currentColor==0) face.closeColorPicker();
            else if(face.isColorPickerOpened()) face.openColorPicker(palette[currentColor]);
        }
    }

    public void colorPicked(Color color){
        if(currentColor>0){
            palette[currentColor] = color;
            face.updateColor(color, currentColor);
        }
    }

    public void exit(){
        savePalette();
    }

    private void loadDefaultPalette(){
        palette[0] = new Color(0,0,0,255,true); //noColor
        palette[1] = new Color(255, 0,0,255, false); //red
        palette[2] = new Color(255, 255,0,255, false); //yellow
        palette[3] = new Color(0, 255,0,255, false); //green
        palette[4] = new Color(0, 0,255,255, false); //blue
    }

    private void savePalette(){
        StringBuilder save = new StringBuilder();
        for(int i=0; i<COUNT; i++)
            save.append(palette[i].toString()).append(PALETTE_SEPARATOR);
        save.deleteCharAt(save.length()-1);
        system.saveString(PALETTE, save.toString());
    }

    private boolean loadSavedPalette(){
        String loadStr = system.getSavedString(PALETTE, null);
        if(loadStr==null) return false;
        String[] load = loadStr.split(PALETTE_SEPARATOR);
        if(load.length!=COUNT) return false;
        for(int i=0; i<COUNT; i++) palette[i]=new Color(load[i]);
        return true;
    }

    public void onColorPickerClosed() {
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                synchronized (palette){
                    savePalette();
                }
            }
        });
    }
}
