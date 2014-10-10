package com.cts.jnjbridgetoemploymentpoc.webservices;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.BaseActivity;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * This class is a Base class for all the async activities
 * 
 * @author neerajareddy
 * 
 */
public class BaseAsyncLoader {

	/**
	 * This method is used to get the current session to communicate with fb via
	 * graph api
	 * 
	 * @param context
	 * @param statusCallBack
	 * @return
	 */

	public static Session getCurrentSession(Context context,
			final SessionStatusCallback statusCallBack) {
		Session currentSession = Session.getActiveSession();

		if (currentSession == null || currentSession.getState().isClosed()) {
			Session session = new Session.Builder(context).build();
			Session.setActiveSession(session);
			currentSession = session;
		}

		if (currentSession.isOpened()) {
			if (statusCallBack != null)
				// currentSession.addCallback(statusCallBack);
				Session.openActiveSession((BaseActivity) context, false,
						statusCallBack);
		} else if (!currentSession.isOpened()) {
			Session.OpenRequest op = new Session.OpenRequest(
					(BaseActivity) context);
			// op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
			op.setPermissions(Arrays.asList("public_profile", "user_groups",
					"publish_actions", "user_events", "rsvp_event",
					"user_photos"));
			if (statusCallBack != null)
				op.setCallback(statusCallBack);

			currentSession = new Session.Builder((BaseActivity) context)
					.build();
			Session.setActiveSession(currentSession);
			currentSession.openForPublish(op);
		}
		return currentSession;
	}

	/**
	 * Base Asyschronous task to provide both httpPost and httpGet operations
	 * and response is handled by callback methods handlePostResponse and
	 * handleGetResponse
	 * 
	 * @param eventID
	 * @param params
	 * @param context
	 * @param httpMethod
	 * @param state
	 * @param handler
	 */

	public static void invokeAsyncWebService(final String eventID,
			final Bundle params, final Context context,
			final HttpMethod httpMethod, final String state,
			final FacebookResponseHandler handler) {

		RequestBatch requestBatch = new RequestBatch();
		Session currentSession = getCurrentSession(context, null);
		requestBatch.add(new Request(currentSession, eventID, params,
				httpMethod, new Request.Callback() {
					public void onCompleted(Response response) {
						Log.d("response", "respnse-->" + response);
						if (response == null) {

							ExceptionHandler.makeExceptionAlert(context,
									new NullPointerException("Response NULL"));
							((BaseActivity) context).hideProgressDialog();
							return;
						} else if (response.getError() != null) {
							ExceptionHandler.makeExceptionAlert(context,
									new Exception(""
											+ response.getError()
													.getErrorCode()));
							((BaseActivity) context).hideProgressDialog();
							return;

						} else if (response.getGraphObject() == null) {
							ExceptionHandler.makeExceptionAlert(context,
									new NullPointerException(
											"getGraphObject NULL"));
							((BaseActivity) context).hideProgressDialog();
							return;
						}
						JSONObject jsonObject = null;
						GraphObject graphObject = response.getGraphObject();
						if (state.equalsIgnoreCase(Constants.STATE_PERMISSIONS)) {

							try {
								jsonObject = new JSONObject(response
										.getGraphObject().getInnerJSONObject()
										.toString());
							} catch (JSONException e) {
								Log.d(Constants.LOG_TAG_LOGIN, "Exception:" + e);
								ExceptionHandler.makeExceptionAlert(context,
										new JSONException(e.getMessage()));
								((BaseActivity) context).hideProgressDialog();
								return;
							}
						}
						if (state.equalsIgnoreCase(Constants.STATE_LOGIN)) {

							try {
								jsonObject = new JSONObject(response
										.getGraphObject().getInnerJSONObject()
										.toString());
							} catch (JSONException e) {
								Log.d(Constants.LOG_TAG_LOGIN, "Exception:" + e);
								ExceptionHandler.makeExceptionAlert(context,
										new JSONException(e.getMessage()));
								((BaseActivity) context).hideProgressDialog();
								return;
							}
						} else if (state.equalsIgnoreCase(Constants.STATE_HOME)
								|| state.equalsIgnoreCase(Constants.STATE_EVENTDETAILS_IMAGE)) {

							jsonObject = (JSONObject) graphObject
									.getProperty("cover");
							if (graphObject.getProperty("id") == null)
								return;
						} else if (state
								.equalsIgnoreCase(Constants.STATE_HOMEFEED)
								|| state.equalsIgnoreCase(Constants.STATE_EVENTDETAILS_FEED)) {
							jsonObject = (JSONObject) graphObject
									.getProperty("feed");
						} else if (state
								.equalsIgnoreCase(Constants.STATE_GROUP)) {
							jsonObject = (JSONObject) graphObject
									.getProperty("members");
						} else if (state
								.equalsIgnoreCase(Constants.STATE_EVENTLIST)) {
							jsonObject = (JSONObject) graphObject
									.getProperty("events");
						} else if (state
								.equalsIgnoreCase(Constants.STATE_EVENTDETAILS_ORGANIZER)) {
							jsonObject = (JSONObject) graphObject
									.getProperty("owner");
						} else if (state
								.equalsIgnoreCase(Constants.STATE_EVENTDETAILS_POSTRSVP)) {
							String value = graphObject.getProperty("success")
									.toString();
							handler.handleResponse(value);
							return;
						} else if (state
								.equalsIgnoreCase(Constants.STATE_ATTENDEES_LIST)) {
							jsonObject = (JSONObject) graphObject
									.getProperty("invited");
						} else if (state
								.equalsIgnoreCase(Constants.STATE_POSTTOEVENT)) {

							if (graphObject.getProperty("id") != null)
								handler.handleResponse("success");
							else
								handler.handleResponse("failed");
							return;
						}

						handler.handleResponse(jsonObject);
					}
				}));
		requestBatch.executeAsync();
	}
}
