package com.cts.jnjbridgetoemploymentpoc.webservices;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.cts.jnjbridgetoemploymentpoc.model.Attendees;
import com.cts.jnjbridgetoemploymentpoc.parsers.AttendeesParser;
import com.cts.jnjbridgetoemploymentpoc.utils.DataListner;

/**
 * This class gets the invitees for an event through aysnchronous task
 * 
 * @author neerajareddy
 * 
 */
public class AtendeesAsyncLoader {

	public void getInvitees(final Context context, String eventID,
			String state, final DataListner dataListner) {
		Bundle params = new Bundle();
		params.putString("fields", "invited");

		FacebookResponseHandler handler = new FacebookResponseHandler() {

			@Override
			public void handleResponse(Object response) {
				AttendeesParser attendeesParser = new AttendeesParser(
						(JSONObject) response, context);
				HashMap<String, ArrayList<Attendees>> hashMap = attendeesParser
						.getAttendees();

				dataListner.onDataRetrieved(hashMap);
			}
		};

		BaseAsyncLoader.invokeAsyncWebService(eventID, params, context, null,
				state, handler);

	}

}
