package grisha.support;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Downloader {
	Activity act;
	PictureConversation pc;
	
	public Downloader(Activity act){
		this.act=act;
		pc = new PictureConversation(act);
	}
	
	public Drawable loadPictureURL(String url){
		return pc.byteArrayToDrawable(loadFileURL(url));
	}
	
	public String loadTextURL(String url){
		return new String(loadFileURL(url));
	}
	
	public byte[] loadFileURL(String url){
		URL myUrl;
		InputStream inpstr = null;
		try {
			myUrl = new URL(url);
			inpstr = myUrl.openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Byte> f = new ArrayList<Byte>();
		Integer b;
		try {
			while ((b = inpstr.read()) != -1) {
			    f.add(b.byteValue());
			  }
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] file = new byte[f.size()];
		for(int i=0; i<f.size(); i++){
			file[i]=f.get(i);
		}
		return file;
	}
	
	public boolean isOnline() {
		  String cs = Context.CONNECTIVITY_SERVICE;
		  ConnectivityManager cm = (ConnectivityManager)
		    act.getSystemService(cs);
		  if (cm.getActiveNetworkInfo() == null) {
		    return false;
		  }
		  return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		}

}
