package ru.touchcube;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.model.Color;

/**
 * Created by Григорий Сенченок on 23.08.2018.
 */

public class Palette {

    public final static int COUNT = 5;
    private static final String PALETTE = "palette";

    private SystemInterface system;

    private Color[] palette;

    public Palette(SystemInterface system){
        this.system=system;
        palette = new Color[COUNT];
    }

    public void init(){
        if(!loadSavedPalette()) loadDefaultPalette();
    }

    public void exit(){
        savePalette();
    }

    private void loadDefaultPalette(){
        palette[0] = new Color(0,0,0,1,true);
        palette[1] = new Color(255, 0,0,1, false);
        palette[2] = new Color(255, 255,0,1, false);
        palette[3] = new Color(0, 255,0,1, false);
        palette[4] = new Color(0, 0,255,1, false);
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
