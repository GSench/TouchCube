package ru.touchcube;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.StrictPolicy;

import java.io.File;

import grisha.support.AesEncipher;
import grisha.support.Downloader;

public class MainActivity extends AndroidApplication {

    MainActivity app;
    MyTouchCube cubes;

    RadioButton rp;
    RadioButton rd;
    RadioButton rpa;


    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhezJzH/D/2IcDR4mKk5JX9/mlpWUf+9LqU03dKkFNIDb4VUuvqJJPjPFQKX5tjaCB53XUfoigdqmP+fXyo0/xG32kZdKYSAXB2xstfuGS8I7UZkZAbnz6i2rCJocYgiu43Wb1j0BnDzoSJcMcI22eDguAJR0QKzJk+mbp+ZEhB4wb5HSGy55eht9T/0lKkjTG9Rmc+qJrRtOwQ1e26/bc+M1CceEme7Eeyp3NNgpFgilPp8ZoMh6B35vDUfQEWZE28/g+mKwQUeU9fvDy4ev1//HX6cPu0u14NLq2d1VxI/lN+bSHk3XbkQ9pgPcHtvII+Rb5HIU8MF27B6TQTmM9QIDAQAB";

    SparseArray<String> callbackMsg = new SparseArray<>();

    Handler handler;

    Uri download;

    private static final int ACCEPTED = 1;
    private static final int DENIED = 2;
    private static final int ERROR = 3;

    @SuppressLint("HandlerLeak") @Override
    protected void onCreate (Bundle savedInstanceState) {

        //activity setting
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        app=this;

        callbackMsg.put(0x0100, "LICENSED");
        callbackMsg.put(0x0231, "NOT_LICENSED");
        callbackMsg.put(0x0123, "RETRY");
        callbackMsg.put(0x0, "LICENSED");
        callbackMsg.put(0x1, "NOT_LICENSED");
        callbackMsg.put(0x2, "LICENSED_OLD_KEY");
        callbackMsg.put(0x3, "ERROR_NOT_MARKET_MANAGED");
        callbackMsg.put(0x4, "ERROR_SERVER_FAILURE");
        callbackMsg.put(0x5, "ERROR_OVER_QUOTA");
        callbackMsg.put(0x101, "ERROR_CONTACTING_SERVER");
        callbackMsg.put(0x102, "ERROR_INVALID_PACKAGE_NAME");
        callbackMsg.put(0x103, "ERROR_NON_MATCHING_UID");

        libgdxSetting();
        buttonSetting();

        //handler description
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what!=ACCEPTED){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(app);
                    String message = ""; String title = "";
                    switch(msg.what){
                        case DENIED:
                            Log.i("TC License Checking", "DENIED");
                            title = getResources().getString(R.string.license_error_title);
                            message = getResources().getString(R.string.error_message)+callbackMsg.get(msg.arg1, getResources().getString(R.string.unexpected_error))+" ("+msg.arg1+")";						break;
                        case ERROR:
                            Log.i("TC License Checking", "ERROR");
                            title = getResources().getString(R.string.error_title);
                            message = getResources().getString(R.string.error_message)+callbackMsg.get(msg.arg1, getResources().getString(R.string.unexpected_error))+" ("+msg.arg1+")";						break;
                    }
                    dialog.setTitle(title);
                    dialog.setMessage(message);
                    dialog.setCancelable(false);
                    dialog.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            app.finish();
                        }
                    });
                    if(msg.arg1==291){
                        dialog.setPositiveButton(getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                checkLicense();
                            }
                        });
                    }
                    dialog.create().show();
                }
            }
        };

        //start license checking thread
        checkLicense();

        if(getSharedPreferences("FIRST_START", MODE_PRIVATE).getBoolean("FIRST_START", true)) startTutorial();

        loadFile();
    }

    private class Tutorial{
        String msg; Drawable img;
        Tutorial(String msg, Drawable img){
            this.msg=msg; this.img=img;
        }
    }

    Tutorial [] tutorialMsg;
    int cur;
    RelativeLayout main;
    RelativeLayout tutorial;
    ImageView img;
    TextView txt;
    public void startTutorial(){
        File filePath = new File(Environment.getExternalStorageDirectory()+ "/cube projects");
        filePath.mkdirs();
        tutorialMsg = new Tutorial[]{
                new Tutorial(getResources().getString(R.string.tutorial_1), getResources().getDrawable(R.drawable.single_tap_pic)),
                new Tutorial(getResources().getString(R.string.tutorial_2), getResources().getDrawable(R.drawable.single_finger_rotate_pic)),
                new Tutorial(getResources().getString(R.string.tutorial_3), getResources().getDrawable(R.drawable.pinch_pic)),
                new Tutorial(getResources().getString(R.string.tutorial_4), getResources().getDrawable(R.drawable.press_hold_pic)),
                new Tutorial(getResources().getString(R.string.tutorial_5), getResources().getDrawable(R.drawable.double_tap_pic)),
        };
        cur = 0;
        main = (RelativeLayout) findViewById(R.id.app_layout);
        tutorial = (RelativeLayout) getLayoutInflater().inflate(R.layout.tutorial, main, false);
        tutorial.setOnClickListener(LvlUp);
        img = (ImageView) tutorial.findViewById(R.id.tutorial_image);
        txt = (TextView) tutorial.findViewById(R.id.tutorial_text);
        (tutorial.findViewById(R.id.button_back)).setOnClickListener(LvlDown);

        img.setImageDrawable(tutorialMsg[cur].img);
        txt.setText(tutorialMsg[cur].msg);
        main.addView(tutorial);
    }

    public void loadFile(){
        Intent data = getIntent();
        String action = data.getAction();
        if(!action.equals(Intent.ACTION_VIEW)) return;
        download = data.getData();
        final SaveWindowManager save = new SaveWindowManager(app);

        new AlertDialog.Builder(app).
                setTitle(getResources().getString(R.string.clear_dialog)).
                setMessage(getResources().getString(R.string.save_workspace)).
                setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).
                setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadUri();
                    }
                }).
                setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save.saveProj(cubes.blocks);
                    }
                }).create().show();
    }

    public void loadUri(){
        Log.d("cucu", "loading"+(download!=null));
        if(download!=null) new SaveWindowManager(app).LoadFileUri(download);
        download=null;
    }

    private OnClickListener LvlDown = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cur==0) return;
            else {
                cur--;
                img.setImageDrawable(tutorialMsg[cur].img);
                txt.setText(tutorialMsg[cur].msg);
            }
        }
    };

    private OnClickListener LvlUp = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cur==4){
                main.removeView(tutorial);
                getSharedPreferences("FIRST_START", MODE_PRIVATE).edit().putBoolean("FIRST_START", false).commit();
            }
            else {
                cur++;
                img.setImageDrawable(tutorialMsg[cur].img);
                txt.setText(tutorialMsg[cur].msg);
            }
        }
    };

    boolean checked;
    boolean licensed;
    boolean online;
    boolean timeisUp;
    public void checkLicense(){
        Time now = new Time();
        now.setToNow();

        SharedPreferences LICENSE = getSharedPreferences("CHECKED", MODE_PRIVATE);
        AesEncipher enc = new AesEncipher(BASE64_PUBLIC_KEY);
        final int nowDay = now.yearDay+365*now.year;
        int lastCheckDay = Integer.parseInt(enc.decrypt((LICENSE.getString("LAST_CHECK", enc.encrypt("" + (nowDay - 7))))));

        checked = Boolean.parseBoolean(enc.decrypt((LICENSE.getString("CHECKED", enc.encrypt("false")))));
        licensed = Boolean.parseBoolean(enc.decrypt((LICENSE.getString("LICENSED", enc.encrypt("false")))));
        online = new Downloader(app).isOnline();
        timeisUp = (nowDay-lastCheckDay)>=7;

        Log.i("TC License Checking", "checked = "+checked+" licensed = "+licensed+" online = "+online+" timeisUp = "+timeisUp);

        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                LicenseChecker mChecker = new LicenseChecker(app, new StrictPolicy(), BASE64_PUBLIC_KEY);
                mChecker.checkAccess(new LicenseCheckerCallback(){
                    @Override
                    public void allow(int reason) {
                        if (isFinishing()) return;
                        SharedPreferences LICENSE = getSharedPreferences("CHECKED", MODE_PRIVATE);
                        AesEncipher enc = new AesEncipher(BASE64_PUBLIC_KEY);
                        Editor ed = LICENSE.edit();
                        ed.putString("CHECKED", enc.encrypt("true"));
                        ed.putString("LICENSED", enc.encrypt("true"));
                        ed.putString("LAST_CHECK", enc.encrypt(""+nowDay));
                        ed.commit();
                        handler.sendEmptyMessage(ACCEPTED);
                        Log.i("v", "Check result: ALLOW reason: "+reason);
                    }

                    @Override
                    public void dontAllow(int reason) {
                        if (isFinishing()) return;
                        SharedPreferences LICENSE = getSharedPreferences("CHECKED", MODE_PRIVATE);
                        AesEncipher enc = new AesEncipher(BASE64_PUBLIC_KEY);
                        Editor ed = LICENSE.edit();
                        ed.putString("CHECKED", enc.encrypt("true"));
                        ed.putString("LICENSED", enc.encrypt("false"));
                        ed.commit();
                        handler.sendMessage(handler.obtainMessage(DENIED, reason, 0));
                        Log.i("TC License Checking", "Check result: DONTALLOW reason: "+reason);
                    }

                    @Override
                    public void applicationError(int errorCode) {
                        if (isFinishing()) return;
                        if((online)&(licensed)&(timeisUp)) Toast.makeText(app, app.getResources().getString(R.string.error_message) + errorCode, Toast.LENGTH_SHORT).show();
                        else handler.sendMessage(handler.obtainMessage(ERROR, errorCode, 0));
                        Log.i("TC License Checking", "Check result: ERROR reason: "+errorCode);
                    }
                });
            }
        });

        if( ((!checked)&(!online)) || ((checked)&(licensed)&(!timeisUp)) ) handler.sendEmptyMessage(ACCEPTED);
        else if((checked)&(!online)&(!licensed)) handler.sendEmptyMessage(DENIED);
        else if(((online)&(!licensed))||((online)&(licensed)&(timeisUp))){
            t.start();
        }
    }

    public void libgdxSetting(){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        cubes = new MyTouchCube(new ActionResolverAndroid(app));
        View libGDXView = initializeForView(cubes, config);
        RelativeLayout main = (RelativeLayout) findViewById(R.id.mainLayout);
        main.addView(libGDXView);
    }

    public void buttonSetting(){
        Button centreB = (Button) findViewById(R.id.centreButton);
        centreB.setOnClickListener(centre);

        Button clearB = (Button) findViewById(R.id.clearButton);
        clearB.setOnClickListener(clear);

        Button saveWindow = (Button) findViewById(R.id.filesButton);
        saveWindow.setOnClickListener(openSaveWindow);

        Button saveProj = (Button) findViewById(R.id.saveButton);
        saveProj.setOnClickListener(save);

        Button settings = (Button) findViewById(R.id.settings_button);
        settings.setOnClickListener(settingsAct);

        rp = (RadioButton) findViewById(R.id.putRB);
        rp.setOnClickListener(onIcClick);
        rd = (RadioButton) findViewById(R.id.deleteRB);
        rd.setOnClickListener(onIcClick);
        rpa = (RadioButton) findViewById(R.id.paintRB);
        rpa.setOnClickListener(onIcClick);
        int elementSize = rp.getWidth();

        ImageView imp = (ImageView) findViewById(R.id.putIcon);
        imp.setMaxWidth(elementSize);
        imp.setMinimumWidth(elementSize);
        imp.setOnClickListener(onIcClick);

        ImageView imd = (ImageView) findViewById(R.id.deleteIcon);
        imd.setMaxWidth(elementSize);
        imd.setMinimumWidth(elementSize);
        imd.setOnClickListener(onIcClick);

        ImageView impa = (ImageView) findViewById(R.id.paintIcon);
        impa.setMaxWidth(elementSize);
        impa.setMinimumWidth(elementSize);
        impa.setOnClickListener(onIcClick);
    }

    OnClickListener clear = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            new AlertDialog.Builder(app)
                    .setTitle(app.getResources().getString(R.string.clear_dialog))
                    .setMessage(app.getResources().getString(R.string.clear_dialog_message))
                    .setPositiveButton(app.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cubes.loadAll(null);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(app.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create().show();
        }
    };

    OnClickListener centre = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            cubes.centralize();
        }
    };

    OnClickListener openSaveWindow = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            new SaveWindowManager(app).showSaveWindow();
        }
    };

    OnClickListener save = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            new SaveWindowManager(app).saveProj(cubes.blocks);
        }
    };

    OnClickListener settingsAct = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(app, MyPreferenceActivity.class));
        }
    };

    OnClickListener onIcClick = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if(arg0.getId()==R.id.putIcon||arg0.getId()==R.id.putRB){
                rp.setChecked(true);
                rd.setChecked(false);
                rpa.setChecked(false);
                cubes.changeDeleteMode(1);
            } else if(arg0.getId()==R.id.paintIcon||arg0.getId()==R.id.paintRB){
                rpa.setChecked(true);
                rp.setChecked(false);
                rd.setChecked(false);
                cubes.changeDeleteMode(2);
            } else if(arg0.getId()==R.id.deleteIcon||arg0.getId()==R.id.deleteRB){
                rd.setChecked(true);
                rp.setChecked(false);
                rpa.setChecked(false);
                cubes.changeDeleteMode(3);
            }
        }
    };
}
