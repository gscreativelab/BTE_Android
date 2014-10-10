package com.cts.jnjbridgetoemploymentpoc.exception;

import java.lang.Thread.UncaughtExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.BTELoginActivity;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.util.Log;

/**
 * 
 * DefaultExceptionHandler is used to caught all the uncaught exceptions.
 * 
 * It will caught all the run time exceptions and navigate to BTE Home Page
 * 
 * 
 */

public class DefaultExceptionHandler implements UncaughtExceptionHandler {

	public static DefaultExceptionHandler exceptionHandler;
	private UncaughtExceptionHandler previousHandler;
	private Context context;
	private AlarmManager alarmManager;

	public DefaultExceptionHandler(Context ctx,
			UncaughtExceptionHandler prevHandler) {
		context = ctx;
		previousHandler = prevHandler;
	}

	/*
	 * UncaughtExceptionHandler should be singleton class because not to set
	 * uncaught exception more than one time
	 */
	public static synchronized DefaultExceptionHandler setInstance(Context ctx,
			UncaughtExceptionHandler prevHandler) {
		if (exceptionHandler == null) {
			exceptionHandler = new DefaultExceptionHandler(ctx, prevHandler);
		}
		return exceptionHandler;
	}

	public void uncaughtException(Thread t, Throwable e) {
		if (Constants.DEBUG) {
			Log.d(Constants.LOG_TAG_UNCAUGHTEXCEPTION, e.getMessage());
		}

		try {

			Throwable root = e;
			while (root.getCause() != null) {
				root = root.getCause();
			}

			Log.d(Constants.LOG_TAG_UNCAUGHTEXCEPTION, "UncaughException: "
					+ root.getClass().getSimpleName());

			/*
			 * Create a intent which we want to navigate when the system is
			 * crashed. we navigated to Home Screen
			 */
			Intent intent = new Intent(context, BTELoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			/*
			 * FLAG_ACTIVITY_CLEAR_TASK is used to clear all the task, its
			 * available in android api level 11 and above. so we used
			 * IntentCompat.FLAG_ACTIVITY_CLEAR_TASK for all the versions of
			 * android
			 */
			intent.addFlags(IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(intent);

			PendingIntent myActivity = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_ONE_SHOT);

			/*
			 * The application will be unresponsive and in order to restart the
			 * application we are navigate to home screen using alarm manager.
			 */
			alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000,
					myActivity);

			/*
			 * To avoid force closed dialog, we are using exit() to exit the
			 * application and avoid the force closed dialog
			 * 
			 * Re-throw critical exception further to the Operating System
			 * (important)
			 */
			System.exit(2);

		} catch (Exception exception) {
			Log.d(Constants.LOG_TAG_UNCAUGHTEXCEPTION, exception.getMessage());
		}

		// Handle exception here
		previousHandler.uncaughtException(t, e);

	}
}