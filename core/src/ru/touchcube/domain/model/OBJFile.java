package ru.touchcube.domain.model;

/**
 * Created by grish on 08.12.2018.
 */

public abstract class OBJFile {

    public abstract String getFilename();

    public abstract void write(byte[] data) throws Exception;

    public abstract String getFullPath();
}
