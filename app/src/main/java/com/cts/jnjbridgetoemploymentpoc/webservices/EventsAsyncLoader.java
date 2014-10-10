package com.cts.jnjbridgetoemploymentpoc.webservices;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.model.User;
import com.cts.jnjbridgetoemploymentpoc.parsers.FeedParser;
import com.cts.jnjbridgetoemploymentpoc.exception.ExceptionHandler;
import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DataListner;
import com.cts.jnjbridgetoemploymentpoc.utils.SharedPref;
import com.squareup.picasso.Picasso;

/**
 * Class to get Event details asynchronously
 * 
 * @author neerajareddy
 * 
 */
public class EventsAsyncLoader {

	/**
	 * get Event Image from API as cover image
	 * 
	 * @param eventID
	 * @param groupImageView
	 * @param context
	 */

	public void setEventImage(final String eventID,
			final ImageView groupImageView, final Context context,
			final String state) {

		final SharedPref pref = new SharedPref(context);

		final String imgURL = pref.getString(eventID);

		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				JSONObject JSONresponse = (JSONObject) response;
				try {
					if (JSONresponse != null && JSONresponse.has("source")) {
						String src = (String) JSONresponse.get("source");
						if (src != null) {
							URL img_url;
							img_url = new URL(JSONresponse.get("source")
									.toString());
							String imageURL = JSONresponse.get("source")
									.toString();
							pref.putString(eventID, imageURL);
							Log.d(Constants.LOG_TAG_EVENTSASYNC, img_url + "");
							// imageViewQ.displayImage(imageURL,
							// groupImageView);
							Picasso.with(context).load(imageURL)
									.placeholder(R.drawable.event)
									.error(R.drawable.event)
									.into(groupImageView);
						}
					} else {
						// set default image here
						pref.putString(eventID, "NOSource");
					}

				} catch (IOException e) {
					Log.d(Constants.LOG_TAG_EVENTSASYNC, "IOException:" + e);
					ExceptionHandler.makeExceptionAlert(context,
							new IOException(e.getMessage()));
				} catch (JSONException e) {
					Log.d(Constants.LOG_TAG_EVENTSASYNC, "JSONException:" + e);
					ExceptionHandler.makeExceptionAlert(context,
							new JSONException(e.getMessage()));
				}

			}
		};

		if (imgURL != null) {
			// already having the image url so no need to download it again
			// get it from prefs
			if (!imgURL.equalsIgnoreCase("NOSource"))
				Picasso.with(context).load(imgURL)
						.placeholder(R.drawable.event).error(R.drawable.event)
						.into(groupImageView);
		} else {
			Bundle params = new Bundle();
			params.putString("fields", "cover");
			BaseAsyncLoader.invokeAsyncWebService(eventID, params, context,
					null, state, handler);
		}

	}

	/**
	 * gets Events Feed
	 * 
	 * @param context
	 * @param eventID
	 * @param state
	 * @param dataListner
	 */

	public void getEventFeed(final Context context, String eventID,
			String state, final String organizedBy,
			final DataListner dataListner) {
		Bundle params = new Bundle();
		params.putString("fields", "feed");
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				if (response != null) {
					FeedParser feedParser = new FeedParser(
							(JSONObject) response, context);
					ArrayList<Events> arrayList = feedParser
							.getEventDetails(organizedBy);
					dataListner.onDataRetrieved(arrayList);
				} else
					dataListner.onDataRetrieved(null);
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(eventID, params, context, null,
				state, handler);

	}

	/**
	 * gets Event Organizer details
	 * 
	 * @param context
	 * @param eventID
	 * @param state
	 * @param dataListner
	 */

	public void getEventOrganiserDetails(final Context context, String eventID,
			String state, final DataListner dataListner) {
		Bundle params = new Bundle();
		params.putString("fields", "owner");
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				User user = new User();
				String ownerName = "";
				String ownerId = "";
				try {
					ownerName = ((JSONObject) response).getString("name");
					ownerId = ((JSONObject) response).getString("id");

					user.setId(ownerId);
					user.setName(ownerName);
				} catch (JSONException e) {
					Log.d(Constants.LOG_TAG_EVENTSASYNC, "JSONException:" + e);
					ExceptionHandler.makeExceptionAlert(context,
							new JSONException(e.getMessage()));
				}
				dataListner.onDataRetrieved(user);
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(eventID, params, context, null,
				state, handler);
	}

}
