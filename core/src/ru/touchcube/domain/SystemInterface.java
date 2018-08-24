package ru.touchcube.domain;

import ru.touchcube.domain.utils.function;


/**
 * Created by grish on 01.05.2017.
 */

public interface SystemInterface {

    public void doOnBackground(function<Void> background);
    public void doOnForeground(function<Void> background);
    //public Pair<byte[], HttpParams> httpGet(URL url, HttpParams params) throws IOException;
    public String[] getSavedStringArray(String title, String[] def);
    public void saveStringArray(String title, String[] array);
    public String getSavedString(String title, String def);
    public void saveString(String title, String string);
    /**
    public int getSavedInt(String title, int def);
    public void saveInt(String title, int i);*/
    public void removeSaved(String str);
    byte[] loadCashFile(String filename, byte[] def);
    void saveToCashFile(String filename, byte[] bytes);
}
