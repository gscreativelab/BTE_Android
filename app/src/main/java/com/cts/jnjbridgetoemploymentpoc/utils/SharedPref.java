package com.cts.jnjbridgetoemploymentpoc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * This class allows to put and get data in shared preferences
 * 
 * @author neerajareddy
 * 
 */
public class SharedPref {

	SharedPreferences prefs;
	Editor editor;

	public SharedPref(Context context) {
		prefs = context.getSharedPreferences(Constants.PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	public void putString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		return prefs.getString(key, null);
	}
}
