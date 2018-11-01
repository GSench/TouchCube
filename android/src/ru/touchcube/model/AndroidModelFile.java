package ru.touchcube.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import ru.touchcube.R;
import ru.touchcube.domain.model.CubeModelFile;

/**
 * Created by grish on 02.09.2018.
 * This class implements r/w file on Android
 */

public class AndroidModelFile extends CubeModelFile {

    private File file;
    private Context context;

    public AndroidModelFile(File file, Context context) {
        super(FilenameUtils.getBaseName(file.getName()));
        this.file=file;
        this.context=context;
    }

    @Override
    public byte[] read() throws Exception {
        byte[] content = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            content = IOUtils.toByteArray(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return content;
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
    public void delete() throws Exception {
        if(!file.delete()) throw new Exception();
    }

    @Override
    public void share() {
        Uri data = Uri.fromFile(file);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, data);
        context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.share_file_using)));
    }
}
