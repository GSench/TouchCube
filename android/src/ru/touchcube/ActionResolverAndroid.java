package ru.touchcube;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import ru.touchcube.ActionResolver;

public class ActionResolverAndroid implements ActionResolver {
	Handler thread;
	Context context;
	
	public ActionResolverAndroid(Context context){
		this.context=context;
		thread = new Handler();
	}

	@Override
	public void showToast(final String arg0) {
		thread.post(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(context, arg0, Toast.LENGTH_LONG).show();
			}
		});
	}

}
