package ru.touchcube.model;

import android.content.Context;
import android.net.Uri;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import ru.touchcube.domain.model.CubeModelFile;

/**
 * Created by grish on 22.10.2018.
 */

public class AndroidModelUri extends CubeModelFile {

    private Context context;
    private Uri uri;

    public AndroidModelUri(Uri uri, Context context) {
        super(uri.getLastPathSegment());
        this.uri=uri;
        this.context=context;
    }

    @Override
    public byte[] read() throws Exception {
        byte[] content = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            content = IOUtils.toByteArray(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return content;
    }

    @Override
    public void write(byte[] content) throws Exception {
        //not needed
    }

    @Override
    public void delete() throws Exception {
        //not needed
    }

    @Override
    public void share() {
        //not needed
    }
}
