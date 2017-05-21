package grisha.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class PictureConversation {
    Activity act;

    public PictureConversation(Activity act){
        this.act=act;
    }

    public Bitmap byteArrayToBitmap(byte[] file) {
        if(file==null) return null;
        return BitmapFactory.decodeByteArray(file, 0, file.length);
    }

    public Drawable bitmapToDrawable(Bitmap pic){
        if(pic==null) return null;
        return new BitmapDrawable(act.getResources(), pic);
    }

    public Drawable byteArrayToDrawable(byte[] file){
        if(file==null) return null;
        return bitmapToDrawable(byteArrayToBitmap(file));
    }

    /**
     * ??????????????? bitmap ? byte[]
     * @param pic ???????? ???????????
     * @param format ?????? ???????????: 0 - PNG , 1 - JPEG
     * */
    public byte[] bitmapToByteArrray(Bitmap pic, int format){
        if(pic==null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        switch(format){
            case 0:
                pic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                break;
            case 1:
                pic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                break;
        }
        byte[] bitmapdata = stream.toByteArray();
        try {
            stream.flush();
            stream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmapdata;
    }

    public Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }



    public byte[] drawableToByteArray(Drawable pic, int format){
        if(pic==null) return null;
        return bitmapToByteArrray(drawableToBitmap(pic), format);
    }
}
