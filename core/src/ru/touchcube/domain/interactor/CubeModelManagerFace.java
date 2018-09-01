package ru.touchcube.domain.interactor;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public interface CubeModelManagerFace {
    void onSavingError(String title);
    void onSaved(String title);
    void onLoadError(String modelName);
    void onDeleteError(String modelName);
    void updateModelList();
}
