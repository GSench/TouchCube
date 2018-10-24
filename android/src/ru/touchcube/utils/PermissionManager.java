package ru.touchcube.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import ru.touchcube.R;
import ru.touchcube.domain.utils.function;

/**
 * Created by Григорий Сенченок on 10.03.2017.
 */

public class PermissionManager {

    private static final int WRITE_EXTERNAL_STORAGE = 1;

    private Activity act;
    private function<Void> doAfter;
    private function<Void> onDenied;

    public PermissionManager(Activity act){
        this.act=act;
    }

    public void requestWriteESPermission(final function<Void> doAfter, final function<Void> onDenied){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&!writeExternalPermGranted(act)){
            new AlertDialog.Builder(act)
                    .setMessage(R.string.write_permission_msg)
                    .setPositiveButton(act.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                            PermissionManager.this.doAfter=doAfter;
                            PermissionManager.this.onDenied=onDenied;
                        }
                    })
                    .setNeutralButton(act.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            onDenied.run();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        } else {
            doAfter.run();
        }
    }

    private static boolean writeExternalPermGranted(Context context) {
        return (context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public void onPermissionCallback(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(doAfter==null) return;
                    doAfter.run();
                    doAfter =null;
                } else {
                    if(onDenied==null) return;
                    onDenied.run();
                    onDenied=null;
                }
            }
        }
    }
}
