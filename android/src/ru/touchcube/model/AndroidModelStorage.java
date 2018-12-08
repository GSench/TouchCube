package ru.touchcube.model;

import android.content.Context;
import android.os.Environment;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;
import ru.touchcube.domain.model.OBJFile;
import ru.touchcube.domain.utils.Pair;

/**
 * Created by grish on 02.09.2018.
 * This class implements cubes' files' storage on Android
 */

public class AndroidModelStorage extends CubeModelStorage {

    private static final String PATH = "cube projects";
    public static final String EXTENSION = "cu";
    public static final String OBJ = "obj";
    public static final String MTL = "mtl";

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

    @Override
    public Pair<OBJFile, OBJFile> createNewObj(String modelName) throws Exception {
        File obj = new File(path, modelName+"."+OBJ);
        File mtl = new File(path, modelName+"."+MTL);
        if(!obj.exists()) obj.createNewFile();
        if(!mtl.exists()) mtl.createNewFile();
        if(obj.exists()&&mtl.exists()) return new Pair<OBJFile, OBJFile>(new AndroidOBJFile(obj), new AndroidOBJFile(mtl));
        else throw new Exception();
    }
}
