package ru.touchcube.domain;

import ru.touchcube.domain.utils.function;


/**
 * Created by grish on 01.05.2017.
 * Some common interfaces for system abilities
 */

public interface SystemInterface {

    void doOnBackground(function<Void> background);
    void doOnForeground(function<Void> background);
    String getSavedString(String title, String def);
    void saveString(String title, String string);
    byte[] loadCashFile(String filename, byte[] def);
    void saveToCashFile(String filename, byte[] bytes);
}
