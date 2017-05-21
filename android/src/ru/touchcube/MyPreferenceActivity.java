package ru.touchcube;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyPreferenceActivity extends PreferenceActivity {

    static final int NOTHING_OPENED = -1;
    static final int DIALOG_HELP = 0;
    static final int DIALOG_CONTACTS = 1;
    static final int DIALOG_ABOUT = 2;
	
	int dialogOpened = -1;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		Preference helpButton = findPreference("help_button");
        helpButton.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				openHelp();
				return true;
			}
		});
        Preference contactsButton = findPreference("contacts_button");
        contactsButton.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openContactsInfo();
                return true;
            }
        });
        Preference aboutButton = findPreference("about_button");
        aboutButton.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openAbout();
                return true;
            }
        });
	}
	
	public void openHelp(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.about_title)
		.setView(getV())
		.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                dialogOpened=NOTHING_OPENED;
				dialog.cancel();
			}
		}).create().show();
        dialogOpened=DIALOG_HELP;
	}
	private View getV() {
		LinearLayout layout = new LinearLayout(this);
		View v = getLayoutInflater().inflate(R.layout.info_layout, layout, false);
		layout.addView(v);
		return layout;
	}

    public void openContactsInfo(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.contacts_title)
                .setView(getV1())
                .setPositiveButton(R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogOpened=NOTHING_OPENED;
                        dialog.cancel();
                    }
                }).create().show();
        dialogOpened=DIALOG_CONTACTS;
    }

    private View getV1() {
        LinearLayout layout = new LinearLayout(this);
        View v = getLayoutInflater().inflate(R.layout.support_info_layout, layout, false);
        ((TextView) (v.findViewById(R.id.link))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSite();
            }
        });
        ((TextView) (v.findViewById(R.id.mail))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
        layout.addView(v);
        return layout;
    }


    public void openAbout(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setView(getV2())
                .setPositiveButton(R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogOpened=NOTHING_OPENED;
                        dialog.cancel();
                    }
                }).create().show();
        dialogOpened=DIALOG_ABOUT;
    }

    private View getV2(){
        LinearLayout layout = new LinearLayout(this);
        View v = getLayoutInflater().inflate(R.layout.about_layout, layout, false);
        layout.addView(v);
        return layout;
    }

    public void openSite(){
        Uri webpack = Uri.parse(getResources().getString(R.string.site));
        Intent intent = new Intent(Intent.ACTION_VIEW, webpack);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void sendMail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState.getInt("dialog_opened")==DIALOG_HELP) openHelp();
        if(savedInstanceState.getInt("dialog_opened")==DIALOG_CONTACTS) openContactsInfo();
        if(savedInstanceState.getInt("dialog_opened")==DIALOG_ABOUT) openAbout();
	    super.onRestoreInstanceState(savedInstanceState);
	}
	 
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    outState.putInt("dialog_opened", dialogOpened);
	    super.onSaveInstanceState(outState);
	}

}
