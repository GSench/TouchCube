package ru.touchcube;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import grisha.support.AesEncipher;
import grisha.support.FileManager;
import grisha.support.SystemHelper;

public class SaveWindowManager {
	MainActivity app;
	ArrayList<String> names;
	ScrollView files;
	RelativeLayout fileView[];
	LinearLayout fileArray;
	int fileToLoad=-1;

	public SaveWindowManager(MainActivity act){
		app=act;
	}
	
	public View generateList(){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			TextView ret = new TextView(app);
			ret.setText(app.getResources().getString(R.string.storage_error));
			return ret;
		} else {
			File file = new File(Environment.getExternalStorageDirectory()+"/cube projects");
			names = new ArrayList<>();
			File f[] = file.listFiles();
			if(f==null){
				TextView ret = new TextView(app);
				ret.setText(app.getResources().getString(R.string.no_files));
				return ret;
			} else {
				for(int i=0; i<f.length; i++)
					if(f[i].isFile()&&f[i].getName().endsWith(".cu"))
						names.add(f[i].getName().substring(0, f[i].getName().indexOf(".")));
				if(names.size()==0){
					TextView ret = new TextView(app);
					ret.setText(app.getResources().getString(R.string.no_files));
					return ret;
				} else {
                    int height = (int) Math.round(SystemHelper.getHeight(app)*0.14);
                    float picPiece = (float) 0.5;
                    
					files = new ScrollView(app);
					fileView = new RelativeLayout[names.size()];
					fileArray = new LinearLayout(app);
					fileArray.setOrientation(LinearLayout.VERTICAL);
					for(int i=0; i<names.size(); i++){
						fileView[i]=new RelativeLayout(app);
						
						RelativeLayout.LayoutParams lpText = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						lpText.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						lpText.addRule(RelativeLayout.CENTER_VERTICAL);

						RelativeLayout.LayoutParams lpButton = new RelativeLayout.LayoutParams((int)(picPiece * height), (int)(picPiece * height));
						lpButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        lpButton.leftMargin=(int)(height*(1-picPiece)*0.5);
                        lpButton.rightMargin=(int)(height*(1-picPiece)*0.5);
						lpButton.addRule(RelativeLayout.CENTER_VERTICAL);

                        RelativeLayout.LayoutParams lpShare = new RelativeLayout.LayoutParams((int)(picPiece * height), (int)(picPiece * height));
                        lpShare.addRule(RelativeLayout.LEFT_OF, i+names.size()*2);
                        lpShare.leftMargin=(int)(height*(1-picPiece)*0.5);
                        lpShare.rightMargin=(int)(height*(1-picPiece)*0.5);
                        lpShare.addRule(RelativeLayout.CENTER_VERTICAL);

						RelativeLayout.LayoutParams lpLine = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
						lpLine.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
						
						TextView fileName = new TextView(app);
						fileName.setLayoutParams(lpText);
						fileName.setText(names.get(i));

						Button delButton = new Button(app);
						delButton.setLayoutParams(lpButton);
						delButton.setBackgroundResource(R.drawable.delete_file_button);
						delButton.setId(i+names.size()*2);
						delButton.setOnClickListener(new android.view.View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								deleteProj(names.get(arg0.getId()-names.size()*2));
							}
						});

                        Button shareButton = new Button(app);
                        shareButton.setLayoutParams(lpShare);
                        shareButton.setBackgroundResource(R.drawable.share_button);
                        shareButton.setId(names.size()+i);
                        shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareFile(v.getId()-names.size());
                            }
                        });

						RelativeLayout line = new RelativeLayout(app);
						line.setBackgroundColor(Color.GRAY);
						line.setLayoutParams(lpLine);

						fileView[i].setId(i);
						fileView[i].setOnClickListener(new android.view.View.OnClickListener() {
							@Override
							public void onClick(View arg0) {
								for(RelativeLayout vi:fileView) vi.setBackgroundColor(Color.argb(0, 0, 0, 0));
								if(fileToLoad==arg0.getId()) fileToLoad=-1;
								else {
									fileToLoad=arg0.getId();
									arg0.setBackgroundColor(Color.argb(200, 127, 127, 127));
								}
							}
						});
						fileView[i].addView(fileName);
						fileView[i].addView(delButton);
                        fileView[i].addView(shareButton);
						fileView[i].addView(line);
						fileArray.addView(fileView[i], new LayoutParams(LayoutParams.MATCH_PARENT, height));
					}
					files.addView(fileArray, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					return files;
				}
			}
		}
	}

	public void showSaveWindow(){
		Builder window = new AlertDialog.Builder(app);
		window.setTitle(app.getResources().getString(R.string.save_window_title));
		window.setView(generateList());
		window.setPositiveButton(app.getResources().getString(R.string.save_window_load), new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(fileToLoad!=-1) LoadFile();
				else Toast.makeText(app, app.getResources().getString(R.string.no_files_selected), Toast.LENGTH_SHORT).show();
				fileToLoad=-1;
				arg0.cancel();
			}
		});
		window.setNegativeButton(app.getResources().getString(R.string.cancel), new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				fileToLoad=-1;
				arg0.cancel();
			}
		});
		window.create();
		window.show();
	}

	public void deleteProj(final String name){
		new AlertDialog.Builder(app)
        .setTitle(app.getResources().getString(R.string.delete_file_dialog_title))
        .setMessage(app.getResources().getString(R.string.delete_file_dialog_message))
        .setPositiveButton(app.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	new FileManager(app).deleteFileSd("cube projects", name+".cu");
            	fileArray.removeView(fileView[names.indexOf(name)]);
            	fileToLoad=-1;
				for(RelativeLayout vi:fileView) vi.setBackgroundColor(Color.argb(0, 0, 0, 0));
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

    public void shareFile(int position){
        File share = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/cube projects/"+names.get(position)+".cu");
        Uri data = Uri.fromFile(share);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, data);
        app.startActivity(Intent.createChooser(sharingIntent, app.getResources().getString(R.string.share_file_using)));
    }

	public void LoadFile(){
		try{
			String read = new AesEncipher(app.getResources().getString(R.string.file_key)).decrypt(new FileManager(app).readTextSD("cube projects", names.get(fileToLoad)+".cu"));
			ArrayList<String> sav = new ArrayList<>();
			String sub;
			for (int i=0; i<read.length(); i++){
				if (read.substring(i, i+1).equals("b")){
					sub = read.substring(i);
					if (sub.indexOf("b", 3)<0){
						sav.add(sub.substring(1));
					} else {
						sav.add(sub.substring(1, sub.indexOf("b", 3)));
					}
				}
			}
			app.cubes.loadAll(sav);
		} catch (Exception e) {
			Toast.makeText(app, app.getResources().getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
		}
	}

    public void LoadFileUri(Uri uri){
        try{
            String enc = new String(new FileManager(app).readBytesSDAbs(uri));
            String read = new AesEncipher(app.getResources().getString(R.string.file_key)).decrypt(enc);
            ArrayList<String> sav = new ArrayList<>();
            String sub;
            for (int i=0; i<read.length(); i++){
                if (read.substring(i, i+1).equals("b")){
                    sub = read.substring(i);
                    if (sub.indexOf("b", 3)<0){
                        sav.add(sub.substring(1));
                    } else {
                        sav.add(sub.substring(1, sub.indexOf("b", 3)));
                    }
                }
            }
            app.cubes.loadAll(sav);
        } catch (Exception e) {
            Toast.makeText(app, app.getResources().getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
        }
    }

	public void saveProj(final ArrayList<String> proj){
		final EditText ed = new EditText(app);
		ed.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		new AlertDialog.Builder(app)
        .setTitle(app.getResources().getString(R.string.get_name_title))
        .setView(ed)
        .setPositiveButton(app.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	dialog.cancel();
            	String name =ed.getText().toString();
            	if(name.equals("")||(name.contains("."))||(name.contains("*"))||(name.contains("\\"))||(name.contains("/"))
            			||(name.contains(":"))||(name.contains("?"))||(name.contains("|"))||(name.contains("\"")))
            		Toast.makeText(app, app.getResources().getString(R.string.bad_name), Toast.LENGTH_LONG).show();
            	else {
            		startSaveThread(name, proj);
            	}
            }
        })
        .setNegativeButton(app.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
       .create().show();
	}

	@SuppressLint("HandlerLeak")
	public void startSaveThread(final String name, final ArrayList<String> proj){
		final ProgressDialog pd = new ProgressDialog(app);
		pd.setMessage(app.getResources().getString(R.string.saving));
		pd.setCancelable(false);
		pd.show();
		final Handler progress=new Handler(){
			@Override
			public void handleMessage(Message msg){
				pd.dismiss();
			}
		};
		Thread saving = new Thread(new Runnable() {
			@Override
			public void run() {
				String saveMe = new String();
            	for(String s: proj) saveMe=saveMe+"b"+s;
            	new FileManager(app).writeTextSD(new AesEncipher(app.getResources().getString(R.string.file_key)).encrypt(saveMe), "cube projects", name+".cu");
            	progress.sendEmptyMessage(0);
                app.loadUri();
			}
		});
		saving.start();
	}
}
