package com.cts.jnjbridgetoemploymentpoc.webservices;

import android.content.Context;
import android.util.Log;

import com.cts.jnjbridgetoemploymentpoc.ui.activity.BaseActivity;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

/**
 * SessionStatusCallback is a callback once session is created
 * 
 * @author neerajareddy
 *
 */
public class SessionStatusCallback implements Session.StatusCallback {
	FacebookResponseHandler handler;

	Context context;

	public SessionStatusCallback(Context context,
			FacebookResponseHandler handler) {
		this.context = context;
		this.handler = handler;

	}

	@Override
	public void call(final Session session, SessionState state,
			Exception exception) {
		Log.d(Constants.LOG_TAG_LOGIN, "isSessionOpen-->" + session.isOpened());
		Log.d(Constants.LOG_TAG_LOGIN,
				"Access token-->" + session.getAccessToken());

		if (session.isOpened()) {

			// make request to the /me API
			Request.newMeRequest(session, new Request.GraphUserCallback() {

				// callback after Graph API response with user
				// object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					handler.handleResponse(user);
				}

			}).executeAsync();
		} else {
			((BaseActivity) context).hideProgressDialog();
		}
	}
}
