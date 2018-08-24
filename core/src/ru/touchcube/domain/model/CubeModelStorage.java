package ru.touchcube.domain.model;

import java.util.ArrayList;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public abstract class CubeModelStorage {

    public abstract ArrayList<CubeModelFile> getList();
    public abstract CubeModelFile createNew(String filename);

}
