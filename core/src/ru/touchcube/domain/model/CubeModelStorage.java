package ru.touchcube.domain.model;

import java.util.ArrayList;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 * File storage interface to be implemented in system's layer.
 */

public abstract class CubeModelStorage {

    public abstract CubeModelFile[] getList();
    public abstract CubeModelFile createNew(String filename) throws Exception;

}
