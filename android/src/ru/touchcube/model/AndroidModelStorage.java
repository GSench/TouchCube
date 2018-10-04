package ru.touchcube.model;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.comparator.ExtensionFileComparator;

import java.io.File;
import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;
import ru.touchcube.domain.utils.CubeModelFileDescriptor;
import ru.touchcube.domain.utils.Pair;

/**
 * Created by grish on 02.09.2018.
 */

public class AndroidModelStorage extends CubeModelStorage {

    private static final String PATH = "cube projects";
    public static final String EXTENSION = "cu";

    private File path;
    private Context context;

    public AndroidModelStorage(Context context){
        path = new File(Environment.getExternalStorageDirectory(), PATH);
        path.mkdirs();
        this.context=context;
    }

    @Override
    public CubeModelFile[] getList() {
        File[] files = path.listFiles();
        int count = files.length;
        for(int i=0; i<files.length; i++)
            if(!EXTENSION.equals(FilenameUtils.getExtension(files[i].getName()))) {
                files[i] = null;
                count--;
            }
        CubeModelFile[] list = new CubeModelFile[count];
        int i=0;
        for(File file: files)
            if(file!=null)
                list[i++] = new AndroidModelFile(file, context);
        return list;
    }

    @Override
    public CubeModelFile createNew(String filename) throws Exception {
        File file = new File(path, filename+"."+EXTENSION);
        if(file.exists()||file.createNewFile()) return new AndroidModelFile(file, context);
        else throw new Exception();
    }
}
