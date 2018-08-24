package ru.touchcube.domain.model;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public abstract class CubeModelFile {

    private String filename;

    public CubeModelFile(String filename){
        this.filename=filename;
    }

    public String getModelName() {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public abstract byte[] read() throws Exception;

    public abstract void write(byte[] content) throws Exception;

    public abstract void delete() throws Exception;

    public abstract void share();

}
