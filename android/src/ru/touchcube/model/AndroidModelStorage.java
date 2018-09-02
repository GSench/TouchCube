package ru.touchcube.model;

import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;

/**
 * Created by grish on 02.09.2018.
 */

public class AndroidModelStorage extends CubeModelStorage {

    public AndroidModelStorage(){

    }

    @Override
    public CubeModelFile[] getList() {
        return new CubeModelFile[0];
    }

    @Override
    public CubeModelFile createNew(String filename) throws Exception {
        return null;
    }
}
