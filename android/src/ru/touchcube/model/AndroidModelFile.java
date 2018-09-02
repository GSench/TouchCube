package ru.touchcube.model;

import ru.touchcube.domain.model.CubeModelFile;

/**
 * Created by grish on 02.09.2018.
 */

public class AndroidModelFile extends CubeModelFile {

    public AndroidModelFile(String filename) {
        super(filename);
    }

    @Override
    public byte[] read() throws Exception {
        return new byte[0];
    }

    @Override
    public void write(byte[] content) throws Exception {

    }

    @Override
    public void delete() throws Exception {

    }

    @Override
    public void share() {

    }
}
