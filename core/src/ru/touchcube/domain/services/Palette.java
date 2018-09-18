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
    }

    public Color getCurrentColor(){
        return palette[currentColor];
    }

    public void onColorClick(int pos){
        if(pos<0||pos>=COUNT) return;
        if(currentColor==pos && currentColor>0) face.openColorPicker(pos);
        else currentColor = pos;
    }

    public void setColor(Color color, int pos){
        if(pos<1||pos>=COUNT||color.noColor()) return;
        palette[pos] = color;
        face.updateColor(color, pos);
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                synchronized (palette){
                    savePalette();
                }
            }
        });
    }

    public void exit(){
        savePalette();
    }

    private void loadDefaultPalette(){
        palette[0] = new Color(0,0,0,1,true); //noColor
        palette[1] = new Color(255, 0,0,1, false); //red
        palette[2] = new Color(255, 255,0,1, false); //yellow
        palette[3] = new Color(0, 255,0,1, false); //green
        palette[4] = new Color(0, 0,255,1, false); //blue
    }

    private void savePalette(){
        String[] save = new String[COUNT];
        for(int i=0; i<COUNT; i++)
            save[i]=palette[i].toString();
        system.saveStringArray(PALETTE, save);
    }

    private boolean loadSavedPalette(){
        String[] load = system.getSavedStringArray(PALETTE, null);
        if(load==null) return false;
        if(load.length!=COUNT) return false;
        for(int i=0; i<COUNT; i++) palette[i]=new Color(load[i]);
        return true;
    }

}
