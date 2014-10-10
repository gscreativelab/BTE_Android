package com.cts.jnjbridgetoemploymentpoc.webservices;

import android.content.Context;

import com.cts.jnjbridgetoemploymentpoc.ui.activity.R;
import com.cts.jnjbridgetoemploymentpoc.utils.DataListner;
import com.facebook.HttpMethod;

/**
 * 
 * Class to post rsvp status
 * 
 * @author neerajareddy
 * 
 */
public class RsvpAsyncLoader {

	/**
	 * this method posts RSVP status
	 * 
	 * @param context
	 * @param eventID
	 * @param rsvpStatus
	 * @param state
	 * @param dataListner
	 */
	public void postRsvpStatus(final Context context, String eventID,
			String rsvpStatus, String state, final DataListner dataListner) {
		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				if (response.toString().equals("true"))
					dataListner
							.onDataRetrieved(context
									.getString(R.string.successfuly_updated_the_rsvp_status));
				else
					dataListner.onDataRetrieved(context
							.getString(R.string.eror_updating_the_rsvp_status));
			}
		};
		BaseAsyncLoader.invokeAsyncWebService(eventID + "/" + rsvpStatus, null,
				context, HttpMethod.POST, state, handler);

	}

}
