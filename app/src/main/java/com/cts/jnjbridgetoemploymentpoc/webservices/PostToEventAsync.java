package com.cts.jnjbridgetoemploymentpoc.webservices;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.BaseActivity;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import com.cts.jnjbridgetoemploymentpoc.utils.DataListner;
import com.facebook.HttpMethod;

/**
 * This class posts to an event asynchronously
 * 
 * @author neerajareddy
 * 
 */
public class PostToEventAsync {

	/**
	 * 
	 * Method to post to an event using HttpPost
	 * 
	 * @param context
	 * @param eventId
	 * @param message
	 * @param state
	 * @param dataListner
	 */
	public void postToEvent(final Context context, String eventId,
			String message, String state, final DataListner dataListner) {
		Bundle params = new Bundle();
		params.putString("message", message);

		FacebookResponseHandler handler = new FacebookResponseHandler() {
			@Override
			public void handleResponse(Object response) {
				if (response.toString().equals("success")) {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.message_post), Toast.LENGTH_SHORT)
							.show();
					((BaseActivity) context).hideProgressDialog();
					((BaseActivity) context).setResult(Activity.RESULT_OK);
					((Activity) context).finish();
				} else {
					ExceptionHandler.makeExceptionAlert(context,
							new NullPointerException("Response error"));
					((BaseActivity) context).hideProgressDialog();
				}
				Log.d("postRsvpStatus", " resCode -->" + response);

			}
		};

		BaseAsyncLoader.invokeAsyncWebService(eventId + "/feed", params,
				context, HttpMethod.POST, state, handler);

	}

}
