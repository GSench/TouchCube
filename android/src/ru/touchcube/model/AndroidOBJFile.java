package ru.touchcube.model;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

import ru.touchcube.domain.model.OBJFile;

/**
 * Created by grish on 08.12.2018.
 */

public class AndroidOBJFile extends OBJFile {

    private File file;

    public AndroidOBJFile(File file) {
        this.file=file;
    }

    @Override
    public String getFilename() {
        return file.getName();
    }

    @Override
    public void write(byte[] content) throws Exception {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            IOUtils.write(content, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    public String getFullPath() {
        return file.getAbsolutePath();
    }
}
