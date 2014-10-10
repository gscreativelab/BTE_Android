package com.cts.jnjbridgetoemploymentpoc.utils;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * This class checks for Network availability
 * 
 * @author neerajareddy
 * 
 */
public class NetworkUtility {
	public static boolean online = false;

	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm.getNetworkInfo(0) != null
					&& cm.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) {
				online = true;
			} else if (cm.getNetworkInfo(1) != null
					&& cm.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
				online = true;
			} else
				online = false;
		}

		catch (Exception e) {
			Log.d(Constants.LOG_TAG_NETWORK, "Exception-->" + e);
			ExceptionHandler.makeExceptionAlert(context, new Exception(context
					.getResources().getString(R.string.alert_offline_message)));
			online = false;
			return online;
		}
		return online;

	}
}
